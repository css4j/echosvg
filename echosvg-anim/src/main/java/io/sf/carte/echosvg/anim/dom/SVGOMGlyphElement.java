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

import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGGlyphElement;

import io.sf.carte.echosvg.dom.AbstractDocument;

/**
 * This class implements {@link SVGGlyphElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMGlyphElement extends SVGStylableElement implements SVGGlyphElement {

//     /**
//      * Table mapping XML attribute names to TraitInformation objects.
//      */
//     protected static DoublyIndexedTable<String,String> xmlTraitInformation;
//     static {
//         DoublyIndexedTable<String,String> t = new DoublyIndexedTable<>(SVGStylableElement.xmlTraitInformation);
//         t.put(null, SVG_UNICODE_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_GLYPH_NAME_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_CDATA));
//         t.put(null, SVG_D_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_PATH_DATA));
//         t.put(null, SVG_ORIENTATION_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_ARABIC_FORM_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_IDENT));
//         t.put(null, SVG_LANG_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_LANG_LIST));
//         t.put(null, SVG_HORIZ_ADV_X_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_NUMBER));
//         t.put(null, SVG_VERT_ORIGIN_X_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_NUMBER));
//         t.put(null, SVG_VERT_ORIGIN_Y_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_NUMBER));
//         t.put(null, SVG_VERT_ADV_Y_ATTRIBUTE,
//                 new TraitInformation(false, SVGTypes.TYPE_NUMBER));
//         xmlTraitInformation = t;
//     }

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SVGOMGlyphElement object.
	 */
	protected SVGOMGlyphElement() {
	}

	/**
	 * Creates a new SVGOMGlyphElement object.
	 * 
	 * @param prefix The namespace prefix.
	 * @param owner  The owner document.
	 */
	public SVGOMGlyphElement(String prefix, AbstractDocument owner) {
		super(prefix, owner);
	}

	/**
	 * <b>DOM</b>: Implements {@link Node#getLocalName()}.
	 */
	@Override
	public String getLocalName() {
		return SVG_GLYPH_TAG;
	}

	/**
	 * Returns a new uninitialized instance of this object's class.
	 */
	@Override
	protected Node newNode() {
		return new SVGOMGlyphElement();
	}

//     /**
//      * Returns the table of TraitInformation objects for this element.
//      */
//     protected DoublyIndexedTable<String,String> getTraitInformationTable() {
//         return xmlTraitInformation;
//     }
}
