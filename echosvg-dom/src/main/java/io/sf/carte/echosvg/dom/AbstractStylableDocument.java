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

import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.view.DocumentCSS;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

import io.sf.carte.echosvg.css.engine.CSSEngine;

/**
 * A Document that supports CSS styling.
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractStylableDocument extends AbstractDocument implements DocumentCSS, DocumentView {

	private static final long serialVersionUID = 1L;

	/**
	 * The default view.
	 */
	protected transient AbstractView defaultView;

	/**
	 * The CSS engine.
	 */
	protected transient CSSEngine cssEngine;

	protected AbstractStylableDocument() {
	}

	/**
	 * Creates a new document.
	 */
	protected AbstractStylableDocument(DocumentType dt, DOMImplementation impl) {
		super(dt, impl);
	}

	/**
	 * Sets the CSS engine.
	 */
	public void setCSSEngine(CSSEngine ctx) {
		cssEngine = ctx;
	}

	/**
	 * Returns the CSS engine.
	 */
	public CSSEngine getCSSEngine() {
		return cssEngine;
	}

	// DocumentStyle /////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.stylesheets.DocumentStyle#getStyleSheets()}.
	 */
	@Override
	public StyleSheetList getStyleSheets() {
		throw new RuntimeException(" !!! Not implemented");
	}

	// DocumentView ///////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link DocumentView#getDefaultView()}.
	 * 
	 * @return a ViewCSS object.
	 */
	@Override
	public AbstractView getDefaultView() {
		if (defaultView == null) {
			ExtensibleDOMImplementation impl;
			impl = (ExtensibleDOMImplementation) implementation;
			defaultView = impl.createViewCSS(this);
		}
		return defaultView;
	}

	/**
	 * Clears the view CSS.
	 */
	public void clearViewCSS() {
		defaultView = null;
		if (cssEngine != null) {
			cssEngine.dispose();
		}
		cssEngine = null;
	}

	// DocumentCSS ////////////////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements {@link DocumentCSS#getOverrideStyle(Element,String)}.
	 */
	@Override
	public CSSStyleDeclaration getOverrideStyle(Element elt, String pseudoElt) {
		throw new RuntimeException(" !!! Not implemented");
	}

}
