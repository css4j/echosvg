/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.ext.awt.image.spi;

import java.awt.color.ColorSpace;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.ServiceLoader;
import java.util.concurrent.locks.ReentrantLock;

import io.sf.carte.echosvg.ext.awt.image.URLImageCache;
import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class handles the registered Image tag handlers. These are instances of
 * RegistryEntry in this package.
 *
 * <p>
 * Original author: Thomas DeWeese. For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class ImageTagRegistry implements ErrorConstants {

	private List<RegistryEntry> entries = new LinkedList<>();
	private List<String> extensions = null;
	private List<String> mimeTypes = null;

	// Registry lock (more efficient than synchronization)
	private transient ReentrantLock regLock = new ReentrantLock();

	private final URLImageCache imgCache;

	ImageTagRegistry() {
		imgCache = new URLImageCache();
	}

	/**
	 * Removes all decoded raster images from the cache. All Images will be reloaded
	 * from the original source if decoded again.
	 */
	public void flushCache() {
		imgCache.flush();
	}

	/**
	 * Removes the given URL from the cache. Only the Image associated with that URL
	 * will be removed from the cache.
	 */
	public void flushImage(ParsedURL purl) {
		imgCache.clear(purl);
	}

	public Filter checkCache(ParsedURL purl) {
		Filter ret = imgCache.request(purl);

		if (ret == null) {
			imgCache.clear(purl);
		}

		return ret;
	}

	public Filter readURL(ParsedURL purl) {
		return readURL(null, purl, null, true, true);
	}

	public Filter readURL(InputStream is, ParsedURL purl, ColorSpace colorSpace,
			boolean allowOpenStream, boolean returnBrokenLink) {
		if ((is != null) && !is.markSupported())
			// Doesn't support mark so wrap with
			// BufferedInputStream that does.
			is = new BufferedInputStream(is);

		Filter ret = null;

		if (purl != null) {

			ret = imgCache.request(purl);
			if (ret != null) {
				return ret;
			}
		}

		boolean openFailed = false;
		List<String> mimeTypes = getRegisteredMimeTypes();

		for (RegistryEntry re : entries) {
			if (re instanceof URLRegistryEntry) {
				if ((purl == null) || !allowOpenStream)
					continue;

				URLRegistryEntry ure = (URLRegistryEntry) re;
				if (ure.isCompatibleURL(purl)) {
					ret = ure.handleURL(purl);

					// Check if we got an image.
					if (ret != null)
						break;
				}
				continue;
			}

			if (re instanceof StreamRegistryEntry) {
				StreamRegistryEntry sre = (StreamRegistryEntry) re;
				// Quick out last time the open didn't work for this
				// URL so don't try again...
				if (openFailed)
					continue;

				try {
					if (is == null) {
						// Haven't opened the stream yet let's try.
						if ((purl == null) || !allowOpenStream)
							break; // No purl nothing we can do...
						try {
							is = purl.openStream(mimeTypes.iterator());
						} catch (IOException ioe) {
							// Couldn't open the stream, go to next entry.
							openFailed = true;
							continue;
						}

						if (!is.markSupported())
							// Doesn't support mark so wrap with
							// BufferedInputStream that does.
							is = new BufferedInputStream(is);
					}

					if (sre.isCompatibleStream(is)) {
						ret = sre.handleStream(is, purl, colorSpace);
						if (ret != null)
							break;
					}
				} catch (StreamCorruptedException sce) {
					// Stream is messed up so setup to reopen it..
					is = null;
				}
				continue;
			}
		}

		if (purl != null) {
			imgCache.put(purl, ret);
		}

		if (ret == null) {
			if (!returnBrokenLink)
				return null;
			if (openFailed)
				// Technially it's possible that it's an unknown
				// 'protocol that caused the open to fail but probably
				// it's a bad URL...
				return getBrokenLinkImage(this, ERR_URL_UNREACHABLE, null);

			// We were able to get to the data we just couldn't
			// make sense of it...
			return getBrokenLinkImage(this, ERR_URL_UNINTERPRETABLE, null);
		}

		if (!returnBrokenLink && BrokenLinkProvider.hasBrokenLinkProperty(ret)) {
			// Don't Return Broken link image unless requested
			ret = null;
		}

		return ret;
	}

	public Filter readStream(InputStream is) {
		return readStream(is, null);
	}

	public Filter readStream(InputStream is, ColorSpace colorSpace) {
		if (!is.markSupported())
			// Doesn't support mark so wrap with BufferedInputStream that does.
			is = new BufferedInputStream(is);

		Filter ret = null;

		for (RegistryEntry re : entries) {

			if (!(re instanceof StreamRegistryEntry))
				continue;
			StreamRegistryEntry sre = (StreamRegistryEntry) re;

			try {
				if (sre.isCompatibleStream(is)) {
					ret = sre.handleStream(is, null, colorSpace);

					if (ret != null)
						break;
				}
			} catch (StreamCorruptedException sce) {
				break;
			}
		}

		if (ret == null)
			ret = getBrokenLinkImage(this, ERR_STREAM_UNREADABLE, null);

		return ret;
	}

	/**
	 * Register a new entry.
	 * 
	 * @param newRE the new entry.
	 */
	public void register(RegistryEntry newRE) {
		float priority = newRE.getPriority();

		regLock.lock();
		try {
			ListIterator<RegistryEntry> li = entries.listIterator();
			while (li.hasNext()) {
				RegistryEntry re = li.next();
				if (re.getPriority() > priority) {
					li.previous();
					break; // Insertion point found.
				}
			}

			li.add(newRE);

			extensions = null;
			mimeTypes = null;
		} finally {
			regLock.unlock();
		}
	}

	/**
	 * Returns a List that contains String of all the extensions that can be handled
	 * by the various registered image format handlers.
	 */
	public List<String> getRegisteredExtensions() {
		List<String> ext = extensions;
		if (ext != null)
			return ext;

		regLock.lock();
		try {
			// Check again
			if (extensions != null)
				return extensions;
			ext = new ArrayList<>(entries.size() + 5);
			for (RegistryEntry re : entries) {
				ext.addAll(re.getStandardExtensions());
			}
			extensions = Collections.unmodifiableList(ext);
		} finally {
			regLock.unlock();
		}

		return extensions;
	}

	/**
	 * Returns a List that contains String of all the mime types that can be
	 * handled by the various registered image format handlers.
	 */
	public List<String> getRegisteredMimeTypes() {
		List<String> mt = mimeTypes;
		if (mt != null)
			return mt;

		regLock.lock();
		try {
			// Check again
			if (mimeTypes != null)
				return mimeTypes;
			mt = new ArrayList<>(entries.size() + 5);
			for (RegistryEntry re : entries) {
				mt.addAll(re.getMimeTypes());
			}
			mimeTypes = Collections.unmodifiableList(mt);
		} finally {
			regLock.unlock();
		}

		return mimeTypes;
	}

	private static final ImageTagRegistry registry = createImageTagRegistry();

	/**
	 * Get the instance of {@code ImageTagRegistry}.
	 * 
	 * @return the instance.
	 */
	public static ImageTagRegistry getRegistry() {
		return registry;
	}

	private static ImageTagRegistry createImageTagRegistry() {
		ImageTagRegistry registry = new ImageTagRegistry();

		registry.register(new JDKRegistryEntry());

		ServiceLoader<RegistryEntry> loader = ServiceLoader.load(RegistryEntry.class);

		for (RegistryEntry re : loader) {
			registry.register(re);
		}

		return registry;
	}

	private static BrokenLinkProvider defaultProvider = new DefaultBrokenLinkProvider();

	private static BrokenLinkProvider brokenLinkProvider = null;

	public static Filter getBrokenLinkImage(Object base, String code, Object[] params) {
		Filter ret = null;
		if (brokenLinkProvider != null)
			ret = brokenLinkProvider.getBrokenLinkImage(base, code, params);

		if (ret == null)
			ret = defaultProvider.getBrokenLinkImage(base, code, params);

		return ret;
	}

	public static void setBrokenLinkProvider(BrokenLinkProvider provider) {
		brokenLinkProvider = provider;
	}

}
