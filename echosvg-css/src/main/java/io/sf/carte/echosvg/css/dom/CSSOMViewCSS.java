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

package io.sf.carte.echosvg.css.dom;

import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.dom.Element;
import org.w3c.dom.view.ViewCSS;
import org.w3c.dom.views.DocumentView;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;

/**
 * This class represents an object which provides the computed styles of the
 * elements of a document.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class CSSOMViewCSS implements ViewCSS {

	/**
	 * The associated CSS engine.
	 */
	protected CSSEngine cssEngine;

	/**
	 * Creates a new ViewCSS.
	 */
	public CSSOMViewCSS(CSSEngine engine) {
		cssEngine = engine;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.views.AbstractView#getDocument()}.
	 */
	@Override
	public DocumentView getDocument() {
		return (DocumentView) cssEngine.getDocument();
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.ViewCSS#getComputedStyle(Element,String)}.
	 */
	@Override
	public CSSStyleDeclaration getComputedStyle(Element elt, String pseudoElt) {
		if (elt instanceof CSSStylableElement) {
			return new CSSOMComputedStyle(cssEngine, (CSSStylableElement) elt, pseudoElt);
		}
		return null;
	}

}
