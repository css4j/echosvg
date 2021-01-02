/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.bridge;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.dom.AbstractNode;
import io.sf.carte.echosvg.gvt.font.GVTFontFace;
import io.sf.carte.echosvg.gvt.font.GVTFontFamily;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class represents a &lt;font-face&gt; element or @font-face rule
 *
 * @author <a href="mailto:bella.robinson@cmis.csiro.au">Bella Robinson</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class FontFace extends GVTFontFace implements ErrorConstants {

	/**
	 * List of ParsedURL's referencing SVGFonts or TrueType fonts, or Strings naming
	 * locally installed fonts.
	 */
	List<Object> srcs;

	/**
	 * Constructes an SVGFontFace with the specfied font-face attributes.
	 */
	public FontFace(List<Object> srcs, String familyName, float unitsPerEm, String fontWeight, String fontStyle,
			String fontVariant, String fontStretch, float slope, String panose1, float ascent, float descent,
			float strikethroughPosition, float strikethroughThickness, float underlinePosition,
			float underlineThickness, float overlinePosition, float overlineThickness) {
		super(familyName, unitsPerEm, fontWeight, fontStyle, fontVariant, fontStretch, slope, panose1, ascent, descent,
				strikethroughPosition, strikethroughThickness, underlinePosition, underlineThickness, overlinePosition,
				overlineThickness);
		this.srcs = srcs;
	}

	/**
	 * Constructes an SVGFontFace with the specfied fontName.
	 */
	protected FontFace(String familyName) {
		super(familyName);
	}

	public static CSSFontFace createFontFace(String familyName, FontFace src) {
		return new CSSFontFace(new LinkedList<>(src.srcs), familyName, src.unitsPerEm, src.fontWeight, src.fontStyle,
				src.fontVariant, src.fontStretch, src.slope, src.panose1, src.ascent, src.descent,
				src.strikethroughPosition, src.strikethroughThickness, src.underlinePosition, src.underlineThickness,
				src.overlinePosition, src.overlineThickness);
	}

	/**
	 * Returns the font associated with this rule or element.
	 */
	public GVTFontFamily getFontFamily(BridgeContext ctx) {
		final FontFamilyResolver fontFamilyResolver = ctx.getFontFamilyResolver();
		GVTFontFamily family = fontFamilyResolver.resolve(familyName, this);
		if (family != null) {
			return family;
		}

		for (Object o : srcs) {
			if (o instanceof String) {
				family = fontFamilyResolver.resolve((String) o, this);
				if (family != null) {
					return family;
				}
			} else if (o instanceof ParsedURL) {
				try {
					GVTFontFamily ff = getFontFamily(ctx, (ParsedURL) o);
					if (ff != null)
						return ff;
				} catch (SecurityException ex) {
					// Security violation notify the user but keep going.
					ctx.getUserAgent().displayError(ex);
				} catch (BridgeException ex) {
					// If Security violation notify
					// the user but keep going.
					if (ERR_URI_UNSECURE.equals(ex.getCode()))
						ctx.getUserAgent().displayError(ex);
				} catch (Exception ex) {
					// Do nothing couldn't get Referenced URL.
				}
			}
		}

		return null;
	}

	/**
	 * Tries to build a GVTFontFamily from a URL reference
	 */
	protected GVTFontFamily getFontFamily(BridgeContext ctx, ParsedURL purl) {
		String purlStr = purl.toString();

		Element e = getBaseElement(ctx);
		SVGDocument svgDoc = (SVGDocument) e.getOwnerDocument();
		String docURL = svgDoc.getURL();
		ParsedURL pDocURL = null;
		if (docURL != null)
			pDocURL = new ParsedURL(docURL);

		// try to load an SVG document
		String baseURI = AbstractNode.getBaseURI(e);
		purl = new ParsedURL(baseURI, purlStr);
		UserAgent userAgent = ctx.getUserAgent();

		try {
			userAgent.checkLoadExternalResource(purl, pDocURL);
		} catch (SecurityException ex) {
			// Can't load font - Security violation.
			// We should not throw the error that is for certain, just
			// move down the font list, but do we display the error or not???
			// I'll vote yes just because it is a security exception (other
			// exceptions like font not available etc I would skip).
			userAgent.displayError(ex);
			return null;
		}

		if (purl.getRef() != null) {
			// Reference must be to a SVGFont.
			Element ref = ctx.getReferencedElement(e, purlStr);
			if (!ref.getNamespaceURI().equals(SVG_NAMESPACE_URI) || !ref.getLocalName().equals(SVG_FONT_TAG)) {
				return null;
			}

			SVGDocument doc = (SVGDocument) e.getOwnerDocument();
			SVGDocument rdoc = (SVGDocument) ref.getOwnerDocument();

			Element fontElt = ref;
			if (doc != rdoc) {
				fontElt = (Element) doc.importNode(ref, true);
				String base = AbstractNode.getBaseURI(ref);
				Element g = doc.createElementNS(SVG_NAMESPACE_URI, SVG_G_TAG);
				g.appendChild(fontElt);
				g.setAttributeNS(XMLConstants.XML_NAMESPACE_URI, "xml:base", base);
				CSSUtilities.computeStyleAndURIs(ref, fontElt, purlStr);
			}

			// Search for a font-face element
			Element fontFaceElt = null;
			for (Node n = fontElt.getFirstChild(); n != null; n = n.getNextSibling()) {
				if ((n.getNodeType() == Node.ELEMENT_NODE) && n.getNamespaceURI().equals(SVG_NAMESPACE_URI)
						&& n.getLocalName().equals(SVG_FONT_FACE_TAG)) {
					fontFaceElt = (Element) n;
					break;
				}
			}
			// todo : if the above loop fails to find a fontFaceElt, a null is passed to
			// createFontFace()

			SVGFontFaceElementBridge fontFaceBridge;
			fontFaceBridge = (SVGFontFaceElementBridge) ctx.getBridge(SVG_NAMESPACE_URI, SVG_FONT_FACE_TAG);
			GVTFontFace gff = fontFaceBridge.createFontFace(ctx, fontFaceElt);

			return new SVGFontFamily(gff, fontElt, ctx);
		}
		// Must be a reference to a 'Web Font'.
		try {
			return ctx.getFontFamilyResolver().loadFont(purl.openStream(), this);
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Default implementation uses the root element of the document associated with
	 * BridgeContext. This is useful for CSS case.
	 */
	protected Element getBaseElement(BridgeContext ctx) {
		SVGDocument d = (SVGDocument) ctx.getDocument();
		return d.getRootElement();
	}

}
