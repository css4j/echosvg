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
package io.sf.carte.echosvg.dom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import org.w3c.dom.DOMImplementation;

import io.sf.carte.echosvg.dom.events.DocumentEventSupport;
import io.sf.carte.echosvg.dom.events.EventSupport;
import io.sf.carte.echosvg.i18n.Localizable;
import io.sf.carte.echosvg.i18n.LocalizableSupport;

/**
 * This class implements the {@link org.w3c.dom.DOMImplementation},
 * {@link org.w3c.dom.css.DOMImplementationCSS} interfaces.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public abstract class AbstractDOMImplementation implements DOMImplementation, Localizable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The error messages bundle class name.
	 */
	protected static final String RESOURCES = "io.sf.carte.echosvg.dom.resources.Messages";

	/**
	 * The localizable support for the error messages.
	 */
	protected LocalizableSupport localizableSupport = new LocalizableSupport(RESOURCES, getClass().getClassLoader());

	/**
	 * The supported features.
	 */
	protected final HashMap<String, Object> features = new HashMap<>();
	{
		// registerFeature("BasicEvents", "3.0");
		registerFeature("Core", new String[] { "2.0", "3.0" });
		registerFeature("XML", new String[] { "1.0", "2.0", "3.0" });
		registerFeature("Events", new String[] { "2.0", "3.0" });
		registerFeature("UIEvents", new String[] { "2.0", "3.0" });
		registerFeature("MouseEvents", new String[] { "2.0", "3.0" });
		registerFeature("TextEvents", "3.0");
		registerFeature("KeyboardEvents", "3.0");
		registerFeature("MutationEvents", new String[] { "2.0", "3.0" });
		registerFeature("MutationNameEvents", "3.0");
		registerFeature("Traversal", "2.0");
		registerFeature("XPath", "3.0");
	}

	/**
	 * Registers a DOM feature.
	 */
	protected void registerFeature(String name, Object value) {
		features.put(name.toLowerCase(), value);
	}

	/**
	 * Creates a new AbstractDOMImplementation object.
	 */
	protected AbstractDOMImplementation() {
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.DOMImplementation#hasFeature(String,String)}.
	 */
	@Override
	public boolean hasFeature(String feature, String version) {
		if (feature == null || feature.length() == 0) {
			return false;
		}
		if (feature.charAt(0) == '+') {
			// All features are directly castable.
			feature = feature.substring(1);
		}
		Object v = features.get(feature.toLowerCase());
		if (v == null) {
			return false;
		}
		if (version == null || version.length() == 0) {
			return true;
		}
		if (v instanceof String) {
			return version.equals(v);
		} else {
			String[] va = (String[]) v;
			for (String aVa : va) {
				if (version.equals(aVa)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.DOMImplementation#getFeature(String,String)}. No compound
	 * document support, so just return this DOMImlpementation where appropriate.
	 */
	@Override
	public Object getFeature(String feature, String version) {
		if (hasFeature(feature, version)) {
			return this;
		}
		return null;
	}

	/**
	 * Creates an DocumentEventSupport object suitable for use with this
	 * implementation.
	 */
	public DocumentEventSupport createDocumentEventSupport() {
		return new DocumentEventSupport();
	}

	/**
	 * Creates an EventSupport object for a given node.
	 */
	public EventSupport createEventSupport(AbstractNode n) {
		return new EventSupport(n);
	}

	// Localizable //////////////////////////////////////////////////////

	/**
	 * Implements {@link Localizable#setLocale(Locale)}.
	 */
	@Override
	public void setLocale(Locale l) {
		localizableSupport.setLocale(l);
	}

	/**
	 * Implements {@link Localizable#getLocale()}.
	 */
	@Override
	public Locale getLocale() {
		return localizableSupport.getLocale();
	}

	protected void initLocalizable() {
	}

	/**
	 * Implements {@link Localizable#formatMessage(String,Object[])}.
	 */
	@Override
	public String formatMessage(String key, Object[] args) throws MissingResourceException {
		return localizableSupport.formatMessage(key, args);
	}

}
