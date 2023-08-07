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
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.stylesheets.LinkStyle;
import org.w3c.dom.svg.SVGStyleElement;

import io.sf.carte.echosvg.constants.XMLConstants;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStyleSheetNode;
import io.sf.carte.echosvg.css.engine.StyleSheet;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.util.XMLSupport;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class implements {@link SVGStyleElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMStyleElement extends SVGOMElement implements CSSStyleSheetNode, SVGStyleElement, LinkStyle {

//     /**
//      * Table mapping XML attribute names to TraitInformation objects.
//      */
//     protected static DoublyIndexedTable<String,String> xmlTraitInformation;
//     static {
//         DoublyIndexedTable<String,String> t =
//             new DoublyIndexedTable<>(SVGOMElement.xmlTraitInformation);
//         t.put(null, SVG_MEDIA_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_TITLE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_TYPE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         xmlTraitInformation = t;
//     }

	private static final long serialVersionUID = 1L;

	/**
	 * The attribute initializer.
	 */
	protected static final AttributeInitializer attributeInitializer;

	static {
		attributeInitializer = new AttributeInitializer(1);
		attributeInitializer.addAttribute(XMLConstants.XML_NAMESPACE_URI, "xml", "space", "preserve");
	}

	/**
	 * The style sheet.
	 */
	protected transient org.w3c.dom.stylesheets.StyleSheet sheet;

	/**
	 * The DOM CSS style-sheet.
	 */
	protected transient StyleSheet styleSheet;

	/**
	 * The listener used to track the content changes.
	 */
	protected transient EventListener domCharacterDataModifiedListener = new DOMCharacterDataModifiedListener();

	/**
	 * Creates a new SVGOMStyleElement object.
	 */
	protected SVGOMStyleElement() {
	}

	/**
	 * Creates a new SVGOMStyleElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMStyleElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_STYLE_TAG;
	}

	/**
	 * Returns the associated style-sheet.
	 */
	@Override
	public StyleSheet getCSSStyleSheet() {
		if (styleSheet == null) {
			if (getType().equals(CSSConstants.CSS_MIME_TYPE)) {
				SVGOMDocument doc = (SVGOMDocument) getOwnerDocument();
				CSSEngine e = doc.getCSSEngine();
				String text = "";
				Node n = getFirstChild();
				if (n != null) {
					StringBuilder sb = new StringBuilder();
					while (n != null) {
						if (n.getNodeType() == Node.CDATA_SECTION_NODE || n.getNodeType() == Node.TEXT_NODE)
							sb.append(n.getNodeValue());
						n = n.getNextSibling();
					}
					text = sb.toString();
				}
				ParsedURL burl = null;
				String bu = getBaseURI();
				if (bu != null) {
					burl = new ParsedURL(bu);
				}
				String media = getAttributeNS(null, SVG_MEDIA_ATTRIBUTE);
				styleSheet = e.parseStyleSheet(text, burl, media);
				addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI, "DOMCharacterDataModified",
						domCharacterDataModifiedListener, false, null);
			}
		}
		return styleSheet;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.stylesheets.LinkStyle#getSheet()}.
	 */
	@Override
	public org.w3c.dom.stylesheets.StyleSheet getSheet() {
		throw new UnsupportedOperationException("LinkStyle.getSheet() is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#getXMLspace()}.
	 */
	@Override
	public String getXMLspace() {
		return XMLSupport.getXMLSpace(this);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#setXMLspace(String)}.
	 */
	@Override
	public void setXMLspace(String space) throws DOMException {
		setAttributeNS(XML_NAMESPACE_URI, XML_SPACE_QNAME, space);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#getType()}.
	 */
	@Override
	public String getType() {
		if (hasAttributeNS(null, SVG_TYPE_ATTRIBUTE))
			return getAttributeNS(null, SVG_TYPE_ATTRIBUTE);
		else
			return CSSConstants.CSS_MIME_TYPE;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#setType(String)}.
	 */
	@Override
	public void setType(String type) throws DOMException {
		setAttributeNS(null, SVG_TYPE_ATTRIBUTE, type);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#getMedia()}.
	 */
	@Override
	public String getMedia() {
		return getAttribute(SVG_MEDIA_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#setMedia(String)}.
	 */
	@Override
	public void setMedia(String media) throws DOMException {
		setAttribute(SVG_MEDIA_ATTRIBUTE, media);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#getTitle()}.
	 */
	@Override
	public String getTitle() {
		return getAttribute(SVG_TITLE_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGStyleElement#setTitle(String)}.
	 */
	@Override
	public void setTitle(String title) throws DOMException {
		setAttribute(SVG_TITLE_ATTRIBUTE, title);
	}

	/**
	 * Returns the AttributeInitializer for this element type.
	 * 
	 * @return null if this element has no attribute with a default value.
	 */
	@Override
	protected AttributeInitializer getAttributeInitializer() {
		return attributeInitializer;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMStyleElement();
	}

//     /**
//      * Returns the table of TraitInformation objects for this element.
//      */
//     protected DoublyIndexedTable<String,String> getTraitInformationTable() {
//         return xmlTraitInformation;
//     }

	/**
	 * The DOMCharacterDataModified listener.
	 */
	protected class DOMCharacterDataModifiedListener implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			styleSheet = null;
		}
	}
}
