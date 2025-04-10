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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * Base class for {@link RegistryEntry} implementations.
 * 
 * @version $Id$
 */
public class AbstractRegistryEntry implements RegistryEntry, ErrorConstants {

	private String name;
	private float priority;
	private List<String> exts;
	private List<String> mimeTypes;

	public AbstractRegistryEntry(String name, float priority, String[] exts, String[] mimeTypes) {
		this.name = name;
		this.priority = priority;

		this.exts = new ArrayList<>(exts.length);
		for (String ext : exts)
			this.exts.add(ext);
		this.exts = Collections.unmodifiableList(this.exts);

		this.mimeTypes = new ArrayList<>(mimeTypes.length);
		for (String mimeType : mimeTypes)
			this.mimeTypes.add(mimeType);
		this.mimeTypes = Collections.unmodifiableList(this.mimeTypes);
	}

	public AbstractRegistryEntry(String name, float priority, String ext, String mimeType) {
		this.name = name;
		this.priority = priority;

		this.exts = new ArrayList<>(1);
		this.exts.add(ext);
		this.exts = Collections.unmodifiableList(exts);

		this.mimeTypes = new ArrayList<>(1);
		this.mimeTypes.add(mimeType);
		this.mimeTypes = Collections.unmodifiableList(mimeTypes);
	}

	@Override
	public String getFormatName() {
		return name;
	}

	@Override
	public List<String> getStandardExtensions() {
		return exts;
	}

	@Override
	public List<String> getMimeTypes() {
		return mimeTypes;
	}

	@Override
	public float getPriority() {
		return priority;
	}

	protected Filter getFormatBrokenLinkImage(ParsedURL origURL) {
		final String errCode;
		final Object[] errParam;
		if (origURL != null) {
			errCode = ERR_URL_FORMAT_UNREADABLE;
			errParam = new Object[] { getFormatName(), origURL };
		} else {
			errCode = ERR_STREAM_FORMAT_UNREADABLE;
			errParam = new Object[] { getFormatName() };
		}

		return ImageTagRegistry.getBrokenLinkImage(this, errCode, errParam);
	}

	protected Filter getFormatMsgBrokenLinkImage(ParsedURL origURL, Throwable t) {
		String message = t.getClass().getSimpleName();

		final String errCode;
		final Object[] errParam;
		if (origURL != null) {
			errCode = ERR_URL_FORMAT_UNREADABLE_MSG;
			errParam = new Object[] { getFormatName(), origURL, message };
		} else {
			errCode = ERR_STREAM_FORMAT_UNREADABLE_MSG;
			errParam = new Object[] { getFormatName(), message };
		}

		return ImageTagRegistry.getBrokenLinkImage(this, errCode, errParam);
	}

}
