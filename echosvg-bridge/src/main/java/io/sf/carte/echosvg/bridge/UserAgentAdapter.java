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
package io.sf.carte.echosvg.bridge;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAElement;
import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.gvt.event.EventDispatcher;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVGFeatureStrings;

/**
 * An abstract user agent adaptor implementation. It exists to simply the
 * creation of UserAgent instances.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class UserAgentAdapter implements UserAgent {
	protected Set<String> FEATURES = new HashSet<>();
	protected Set<String> extensions = new HashSet<>();

	/**
	 * The BridgeContext to use for error information.
	 */
	protected BridgeContext ctx;

	/**
	 * Sets the BridgeContext to be used for error information.
	 */
	public void setBridgeContext(BridgeContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Adds the standard SVG feature strings to the set of features supported by
	 * this user agent.
	 */
	public void addStdFeatures() {
		SVGFeatureStrings.addSupportedFeatureStrings(FEATURES);
	}

	/**
	 * Returns the default size of this user agent (400x400).
	 */
	@Override
	public Dimension2D getViewportSize() {
		return new Dimension(1, 1);
	}

	/**
	 * Display the specified message.
	 */
	@Override
	public void displayMessage(String message) {
	}

	/**
	 * Display the specified error message (forwards call to displayMessage).
	 */
	public void displayError(String message) {
		displayMessage(message);
	}

	/**
	 * Display the specified error (forwards call to displayError(String))
	 */
	@Override
	public void displayError(Exception e) {
		displayError(e.getMessage());
	}

	/**
	 * Shows an alert dialog box.
	 */
	@Override
	public void showAlert(String message) {
	}

	/**
	 * Shows a prompt dialog box.
	 */
	@Override
	public String showPrompt(String message) {
		return null;
	}

	/**
	 * Shows a prompt dialog box.
	 */
	@Override
	public String showPrompt(String message, String defaultValue) {
		return null;
	}

	/**
	 * Shows a confirm dialog box.
	 */
	@Override
	public boolean showConfirm(String message) {
		return false;
	}

	/**
	 * Returns the size of a px CSS unit in millimeters.
	 */
	@Override
	public float getPixelUnitToMillimeter() {
		return 0.26458333333333333333333333333333f; // 96dpi
	}

	/**
	 * Returns the default font family.
	 */
	@Override
	public String getDefaultFontFamily() {
		return "Arial, Helvetica, sans-serif";
	}

	/**
	 * Returns the medium font size.
	 */
	@Override
	public float getMediumFontSize() {
		// 9pt (72pt = 1in)
		return 9f * 25.4f / (72f * getPixelUnitToMillimeter());
	}

	/**
	 * Returns a lighter font-weight.
	 */
	@Override
	public float getLighterFontWeight(float f) {
		return getStandardLighterFontWeight(f);
	}

	/**
	 * Returns a bolder font-weight.
	 */
	@Override
	public float getBolderFontWeight(float f) {
		return getStandardBolderFontWeight(f);
	}

	/**
	 * Returns the user language "en" (english).
	 */
	@Override
	public String getLanguages() {
		return "en";
	}

	/**
	 * Returns this user agent's CSS media.
	 */
	@Override
	public String getMedia() {
		return "all";
	}

	/**
	 * Returns this user agent's alternate style-sheet title.
	 */
	@Override
	public String getAlternateStyleSheet() {
		return null;
	}

	/**
	 * Returns the user stylesheet
	 */
	@Override
	public String getUserStyleSheetURI() {
		return null;
	}

	/**
	 * Returns <code>false</code>. The XML parser is not in validation mode.
	 */
	@Override
	public boolean isXMLParserValidating() {
		return false;
	}

	/**
	 * Unsupported operation.
	 */
	@Override
	public EventDispatcher getEventDispatcher() {
		return null;
	}

	/**
	 * Unsupported operation.
	 */
	@Override
	public void openLink(SVGAElement elt) {
	}

	/**
	 * Unsupported operation.
	 */
	@Override
	public void setSVGCursor(Cursor cursor) {
	}

	/**
	 * This user agent doesn't display text selections.
	 */
	@Override
	public void setTextSelection(Mark start, Mark end) {
	}

	/**
	 * This user agent doesn't display text selections so nothing to clear.
	 */
	@Override
	public void deselectAll() {
	}

	/**
	 * Unsupported operation.
	 */
	public void runThread(Thread t) {
	}

	/**
	 * Unsupported operation.
	 */
	@Override
	public AffineTransform getTransform() {
		return null;
	}

	/**
	 * Unsupported operation.
	 */
	@Override
	public void setTransform(AffineTransform at) {
		// Do nothing.
	}

	/**
	 * Unsupported operation.
	 */
	@Override
	public Point getClientAreaLocationOnScreen() {
		return new Point();
	}

	/**
	 * Tells whether the given feature is supported by this user agent.
	 */
	@Override
	public boolean hasFeature(String s) {
		return FEATURES.contains(s);
	}

	/**
	 * Tells whether the given extension is supported by this user agent.
	 */
	@Override
	public boolean supportExtension(String s) {
		return extensions.contains(s);
	}

	/**
	 * Lets the bridge tell the user agent that the following ex tension is
	 * supported by the bridge.
	 */
	@Override
	public void registerExtension(BridgeExtension ext) {
		Iterator<String> i = ext.getImplementedExtensions();
		while (i.hasNext())
			extensions.add(i.next());
	}

	/**
	 * Notifies the UserAgent that the input element has been found in the document.
	 * This is sometimes called, for example, to handle &lt;a&gt; or &lt;title&gt;
	 * elements in a UserAgent-dependant way.
	 */
	@Override
	public void handleElement(Element elt, Object data) {
	}

	/**
	 * Returns the security settings for the given script type, script url and
	 * document url
	 * 
	 * @param scriptType type of script, as found in the type attribute of the
	 *                   &lt;script&gt; element.
	 * @param scriptURL  url for the script, as defined in the script's xlink:href
	 *                   attribute. If that attribute was empty, then this parameter
	 *                   should be null
	 * @param docURL     url for the document into which the script was found.
	 */
	@Override
	public ScriptSecurity getScriptSecurity(String scriptType, ParsedURL scriptURL, ParsedURL docURL) {
		return new DefaultScriptSecurity(scriptType, scriptURL, docURL);
	}

	/**
	 * This method throws a SecurityException if the script of given type, found at
	 * url and referenced from docURL should not be loaded.
	 * 
	 * This is a convenience method to call checkLoadScript on the ScriptSecurity
	 * strategy returned by getScriptSecurity.
	 *
	 * @param scriptType type of script, as found in the type attribute of the
	 *                   &lt;script&gt; element.
	 * @param scriptURL  url for the script, as defined in the script's xlink:href
	 *                   attribute. If that attribute was empty, then this parameter
	 *                   should be null
	 * @param docURL     url for the document into which the script was found.
	 */
	@Override
	public void checkLoadScript(String scriptType, ParsedURL scriptURL, ParsedURL docURL) throws SecurityException {
		ScriptSecurity s = getScriptSecurity(scriptType, scriptURL, docURL);
		if (s != null) {
			s.checkLoadScript();
		}
	}

	/**
	 * Returns the security settings for the given resource url and document url
	 * 
	 * @param resourceURL url for the resource, as defined in the resource's
	 *                    xlink:href attribute. If that attribute was empty, then
	 *                    this parameter should be null
	 * @param docURL      url for the document into which the resource was found.
	 */
	@Override
	public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL resourceURL,
			ParsedURL docURL) {
		return new DefaultExternalResourceSecurity(resourceURL, docURL);
	}

	/**
	 * This method throws a SecurityException if the resource found at url and
	 * referenced from docURL should not be loaded.
	 * 
	 * This is a convenience method to call checkLoadExternalResource on the
	 * ExternalResourceSecurity strategy returned by getExternalResourceSecurity.
	 *
	 * @param resourceURL url for the resource, as defined in the resource's
	 *                    xlink:href attribute. If that attribute was empty, then
	 *                    this parameter should be null
	 * @param docURL      url for the document into which the resource was found.
	 */
	@Override
	public void checkLoadExternalResource(ParsedURL resourceURL, ParsedURL docURL) throws SecurityException {
		ExternalResourceSecurity s = getExternalResourceSecurity(resourceURL, docURL);

		if (s != null) {
			s.checkLoadExternalResource();
		}
	}

	/**
	 * Returns a lighter font-weight.
	 */
	public static float getStandardLighterFontWeight(float f) {
		// Round f to nearest 100...
		int weight = ((int) ((f + 50) / 100)) * 100;
		switch (weight) {
		case 100:
			return 100;
		case 200:
			return 100;
		case 300:
			return 200;
		case 400:
			return 300;
		case 500:
			return 400;
		case 600:
			return 400;
		case 700:
			return 400;
		case 800:
			return 400;
		case 900:
			return 400;
		default:
			throw new IllegalArgumentException("Bad Font Weight: " + f);
		}
	}

	/**
	 * Returns a bolder font-weight.
	 */
	public static float getStandardBolderFontWeight(float f) {
		// Round f to nearest 100...
		int weight = ((int) ((f + 50) / 100)) * 100;
		switch (weight) {
		case 100:
			return 600;
		case 200:
			return 600;
		case 300:
			return 600;
		case 400:
			return 600;
		case 500:
			return 600;
		case 600:
			return 700;
		case 700:
			return 800;
		case 800:
			return 900;
		case 900:
			return 900;
		default:
			throw new IllegalArgumentException("Bad Font Weight: " + f);
		}
	}

	/**
	 * This Implementation simply throws a BridgeException.
	 *
	 * @param e       The &lt;image&gt; element that can't be loaded.
	 * @param url     The resolved url that can't be loaded.
	 * @param message As best as can be determined the reason it can't be loaded
	 *                (not available, corrupt, unknown format,...).
	 */
	@Override
	public SVGDocument getBrokenLinkDocument(Element e, String url, String message) {
		throw new BridgeException(ctx, e, ErrorConstants.ERR_URI_IMAGE_BROKEN, new Object[] { url, message });
	}

	/**
	 * This method should load a new document described by the supplied URL.
	 *
	 * @param url The url to be loaded as a string.
	 */
	@Override
	public void loadDocument(String url) {
		// Do nothing.
	}

	/**
	 * Returns the Font Family Resolver
	 */
	@Override
	public FontFamilyResolver getFontFamilyResolver() {
		return DefaultFontFamilyResolver.SINGLETON;
	}
}
