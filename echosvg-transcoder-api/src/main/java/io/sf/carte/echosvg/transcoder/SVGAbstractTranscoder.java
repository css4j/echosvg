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
package io.sf.carte.echosvg.transcoder;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.DOMImplementation;

import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.dom.util.DocumentFactory;
import io.sf.carte.echosvg.transcoder.keys.BooleanKey;
import io.sf.carte.echosvg.transcoder.keys.FloatKey;
import io.sf.carte.echosvg.transcoder.keys.LengthKey;
import io.sf.carte.echosvg.transcoder.keys.Rectangle2DKey;
import io.sf.carte.echosvg.transcoder.keys.StringKey;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This class may be the base class of all transcoders which take an SVG
 * document as input and which need to build a DOM tree. The
 * <code>SVGAbstractTranscoder</code> uses several different hints that guide
 * it's behaviour:<br>
 *
 * <ul>
 * <li><code>KEY_WIDTH, KEY_HEIGHT</code> can be used to specify how to scale
 * the SVG image</li>
 * </ul>
 *
 * <p>
 * Original author: <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class SVGAbstractTranscoder extends XMLAbstractTranscoder {
	/**
	 * Value used as a default for the default font-family hint
	 */
	public static final String DEFAULT_DEFAULT_FONT_FAMILY = "Arial, Helvetica, sans-serif";

	/**
	 * Current area of interest.
	 */
	private Rectangle2D curAOI;

	/**
	 * Transform needed to render the current area of interest
	 */
	private AffineTransform curTxf;

	/**
	 * Image's width and height (init to 400x400).
	 */
	protected float width = 400, height = 400;

	protected SVGAbstractTranscoder() {
		hints.put(KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
		hints.put(KEY_DOCUMENT_ELEMENT, SVGConstants.SVG_SVG_TAG);
		hints.put(KEY_MEDIA, "screen");
		hints.put(KEY_DEFAULT_FONT_FAMILY, DEFAULT_DEFAULT_FONT_FAMILY);
		hints.put(KEY_EXECUTE_ONLOAD, Boolean.FALSE);
		hints.put(KEY_ALLOWED_SCRIPT_TYPES, DEFAULT_ALLOWED_SCRIPT_TYPES);
	}

	/**
	 * Create a {@code DocumentFactory} appropriate for the given document element.
	 * 
	 * @param namespaceURI the document element namespace URI.
	 * @return the {@code DocumentFactory}.
	 */
	@Override
	protected DocumentFactory createDocumentFactory(String namespaceURI) {
		DocumentFactory f;
		if (SVGConstants.SVG_NAMESPACE_URI.equalsIgnoreCase(namespaceURI) || namespaceURI == null) {
			f = createSVGDocumentFactory();
		} else {
			DOMImplementation domImpl = (DOMImplementation) hints.get(KEY_DOM_IMPLEMENTATION);
			if (domImpl == null) {
				domImpl = GenericDOMImplementation.getDOMImplementation();
			}
			f = createDocumentFactory(domImpl);
		}
		return f;
	}

	/**
	 * Creates a <code>DocumentFactory</code> that is used to create an SVG DOM
	 * tree.
	 */
	protected abstract DocumentFactory createSVGDocumentFactory();

	/*
	 * XXX: Nothing uses this.
	 */
	Rectangle2D getCurrentAOI() {
		return curAOI;
	}

	protected void setCurrentAOI(Rectangle2D curAOI) {
		this.curAOI = curAOI;
	}

	protected AffineTransform getCurrentAOITransform() {
		return curTxf;
	}

	protected void setCurrentAOITransform(AffineTransform curTxf) {
		this.curTxf = curTxf;
	}

	protected void setWidth(float width) {
		this.width = width;
	}

	protected void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Sets document size according to the hints. Global variables width and height
	 * are modified.
	 *
	 * @param docWidth  Width of the document.
	 * @param docHeight Height of the document.
	 */
	protected void setImageSize(float docWidth, float docHeight) {

		// Compute the image's width and height according the hints
		float imgWidth = -1;
		if (hints.containsKey(KEY_WIDTH)) {
			imgWidth = (Float) hints.get(KEY_WIDTH);
		}
		float imgHeight = -1;
		if (hints.containsKey(KEY_HEIGHT)) {
			imgHeight = (Float) hints.get(KEY_HEIGHT);
		}

		if (imgWidth > 0 && imgHeight > 0) {
			width = imgWidth;
			height = imgHeight;
		} else if (imgHeight > 0) {
			width = (docWidth * imgHeight) / docHeight;
			height = imgHeight;
		} else if (imgWidth > 0) {
			width = imgWidth;
			height = (docHeight * imgWidth) / docWidth;
		} else {
			width = docWidth;
			height = docHeight;
		}

		// Limit image size according to the maximuxm size hints.
		float imgMaxWidth = -1;
		if (hints.containsKey(KEY_MAX_WIDTH)) {
			imgMaxWidth = (Float) hints.get(KEY_MAX_WIDTH);
		}
		float imgMaxHeight = -1;
		if (hints.containsKey(KEY_MAX_HEIGHT)) {
			imgMaxHeight = (Float) hints.get(KEY_MAX_HEIGHT);
		}

		if ((imgMaxHeight > 0) && (height > imgMaxHeight)) {
			width = (docWidth * imgMaxHeight) / docHeight;
			height = imgMaxHeight;
		}
		if ((imgMaxWidth > 0) && (width > imgMaxWidth)) {
			width = imgMaxWidth;
			height = (docHeight * imgMaxWidth) / docWidth;
		}
	}

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * The image width key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_WIDTH</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">The width of the topmost svg element</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the width of the image to create.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_WIDTH = new LengthKey();

	/**
	 * The image height key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_HEIGHT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">The height of the topmost svg element</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the height of the image to create.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_HEIGHT = new LengthKey();

	/**
	 * The maximum width of the image key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MAX_WIDTH</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">The width of the topmost svg element</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the maximum width of the image to create. The value
	 * will set the maximum width of the image even when a bigger width is specified
	 * in a document or set with KEY_WIDTH.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MAX_WIDTH = new LengthKey();

	/**
	 * The maximux height of the image key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MAX_HEIGHT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">The height of the topmost svg element</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the maximum height of the image to create. The value
	 * will set the maximum height of the image even when bigger height is specified
	 * in a document or set with KEY_HEIGHT.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MAX_HEIGHT = new LengthKey();

	/**
	 * The area of interest key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_AOI</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Rectangle2D</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">The document's size</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the area of interest to render. The rectangle
	 * coordinates must be specified in pixels and in the document coordinates
	 * system.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_AOI = new Rectangle2DKey();

	/**
	 * The language key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_LANGUAGE</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">"en"</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the preferred language of the document.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_LANGUAGE = new StringKey();

	/**
	 * The media key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MEDIA</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">"screen"</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the media to use with CSS.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MEDIA = new StringKey();

	/**
	 * The preferred color scheme key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_PREFERS_COLOR_SCHEME</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">"light"</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the preferred color scheme to use in
	 * CSS media queries.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_PREFERS_COLOR_SCHEME = new StringKey();

	/**
	 * The CSS selector key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_SVG_SELECTOR</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">If the document is HTML, use this CSS
	 * selector to locate the desired SVG element.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_SVG_SELECTOR = new StringKey();

	/**
	 * The default font-family key.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_DEFAULT_FONT_FAMILY</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">"Arial, Helvetica, sans-serif"</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Controls the default value used by the CSS engine for the
	 * font-family property when that property is unspecified.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_DEFAULT_FONT_FAMILY = new StringKey();

	/**
	 * The alternate stylesheet key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_ALTERNATE_STYLESHEET</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the alternate style sheet title.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_ALTERNATE_STYLESHEET = new StringKey();

	/**
	 * The user stylesheet URI key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_USER_STYLESHEET_URI</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">null</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify the user style sheet.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_USER_STYLESHEET_URI = new StringKey();

	/**
	 * The number of millimeters in each pixel key.
	 * <p>
	 * Using a concept of physical pixels instead of CSS pixels is a bad idea which
	 * leads to distorted shapes, unexpected font sizes and wrong aspect ratios.
	 * Physical pixels were removed from Web standards decades ago, please use
	 * {@code KEY_RESOLUTION_DPI} instead.
	 * </p>
	 * @deprecated as of EchoSVG 2.0
	 * @see #KEY_RESOLUTION_DPI
	 *      <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 *      <caption></caption>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Key:</th>
	 *      <td style="vertical-align: top">KEY_PIXEL_UNIT_TO_MILLIMETER</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Value:</th>
	 *      <td style="vertical-align: top">Float</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Default:</th>
	 *      <td style="vertical-align: top">0.264583</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Required:</th>
	 *      <td style="vertical-align: top">No</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Description:</th>
	 *      <td style="vertical-align: top">Specify the size of a px CSS unit in
	 *      millimeters.</td>
	 *      </tr>
	 *      </table>
	 */
	@Deprecated
	public static final TranscodingHints.Key KEY_PIXEL_UNIT_TO_MILLIMETER = new FloatKey();

	/**
	 * The pixel to millimeter conversion factor key.
	 * 
	 * @deprecated As of Batik Version 1.5b3
	 * @see #KEY_PIXEL_UNIT_TO_MILLIMETER
	 *
	 *      <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 *      <caption></caption>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Key:</th>
	 *      <td style="vertical-align: top">KEY_PIXEL_TO_MM</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Value:</th>
	 *      <td style="vertical-align: top">Float</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Default:</th>
	 *      <td style="vertical-align: top">0.264583</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Required:</th>
	 *      <td style="vertical-align: top">No</td>
	 *      </tr>
	 *      <tr>
	 *      <th style="text-align: end; vertical-align: top">Description:</th>
	 *      <td style="vertical-align: top">Specify the size of a px CSS unit in millimeters.</td>
	 *      </tr>
	 *      </table>
	 */
	@Deprecated
	public static final TranscodingHints.Key KEY_PIXEL_TO_MM = KEY_PIXEL_UNIT_TO_MILLIMETER;

	/**
	 * The resolution expressed in {@code dpi} key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_RESOLUTION_DPI</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">96</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The resolution expressed in {@code dpi}. If
	 * not set, implementations may check for {@code KEY_PIXEL_UNIT_TO_MILLIMETER}
	 * and compute the resolution from that value.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_RESOLUTION_DPI = new FloatKey();

	/**
	 * The 'onload' execution key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_EXECUTE_ONLOAD</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">false</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specify if scripts added on the 'onload' event attribute
	 * must be invoked.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_EXECUTE_ONLOAD = new BooleanKey();

	/**
	 * The snapshot time key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_SNAPSHOT_TIME</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Float</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">0</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specifies the document time to seek to before rasterization.
	 * Only applies if {@link #KEY_EXECUTE_ONLOAD} is set to <code>true</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_SNAPSHOT_TIME = new FloatKey();

	/**
	 * The set of supported script languages (i.e., the set of possible values for
	 * the &lt;script&gt; tag's type attribute).
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_ALLOWED_SCRIPT_TYPES</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String (Comma separated values)</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">text/ecmascript, application/java-archive</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specifies the allowed values for the type attribute in the
	 * &lt;script&gt; element. This is a comma separated list. The special value '*'
	 * means that all script types are allowed.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_ALLOWED_SCRIPT_TYPES = new StringKey();

	/**
	 * Default value for the KEY_ALLOWED_SCRIPT_TYPES key
	 */
	public static final String DEFAULT_ALLOWED_SCRIPT_TYPES = SVGConstants.SVG_SCRIPT_TYPE_ECMASCRIPT + ", "
			+ SVGConstants.SVG_SCRIPT_TYPE_APPLICATION_ECMASCRIPT + ", " + SVGConstants.SVG_SCRIPT_TYPE_JAVASCRIPT
			+ ", " + SVGConstants.SVG_SCRIPT_TYPE_APPLICATION_JAVASCRIPT + ", " + SVGConstants.SVG_SCRIPT_TYPE_JAVA;

	/**
	 * Controls whether or not scripts can only be loaded from the same location as
	 * the document which references them.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_CONSTRAIN_SCRIPT_ORIGIN</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">true</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">When set to true, script elements referencing files from a
	 * different origin (server) than the document containing the script element
	 * will not be loaded. When set to true, script elements may reference script
	 * files from any origin.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_CONSTRAIN_SCRIPT_ORIGIN = new BooleanKey();

	/**
	 * Controls whether or not scripts are allowed to load resources without any
	 * restriction.
	 *
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_ALLOW_EXTERNAL_RESOURCES</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">false</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">When set to true, script elements will be
	 * able to load resources without any restriction. Setting it to true may cause
	 * serious issues if the script is not trusted or can be abused in any way, and
	 * you are advised against doing that.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_ALLOW_EXTERNAL_RESOURCES = new BooleanKey();

}
