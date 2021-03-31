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
package io.sf.carte.echosvg.util.gui.xmleditor;

import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleContext;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * This is the set of things needed by a text component to be a reasonably
 * functioning editor for xml type document.
 *
 * @author <a href="mailto:tonny@kiyut.com">Tonny Kohar</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class XMLEditorKit extends DefaultEditorKit {

	private static final long serialVersionUID = 1L;

	public static final String XML_MIME_TYPE = "text/xml";

	protected XMLContext context;
	protected ViewFactory factory = null;

	/** Creates a new instance of XMLEditorKit */
	public XMLEditorKit() {
		this(null);
	}

	/**
	 * Creates a new instance of XMLEditorKit
	 * 
	 * @param context XMLContext
	 */
	public XMLEditorKit(XMLContext context) {
		super();
		factory = new XMLViewFactory();
		if (context == null) {
			this.context = new XMLContext();
		} else {
			this.context = context;
		}
	}

	/**
	 * @return XMLContext
	 */
	public XMLContext getStylePreferences() {
		return context;
	}

	/**
	 * Overriden to set the JEditorPane font to match with the XMLContext
	 * {@inheritDoc}
	 */
	@Override
	public void install(JEditorPane c) {
		super.install(c);

		Object obj = context.getSyntaxFont(StyleContext.DEFAULT_STYLE);
		if (obj != null) {
			c.setFont((Font) obj);
		}
	}

	/**
	 * Get the MIME type of the data that this kit represents support for. This kit
	 * supports the type <code>text/xml</code>.
	 */
	@Override
	public String getContentType() {
		return XML_MIME_TYPE;
	}

	/** {@inheritDoc} */
	@Override
	public Object clone() {
		XMLEditorKit kit = new XMLEditorKit();
		kit.context = context;
		return kit;
	}

	/** {@inheritDoc} */
	@Override
	public Document createDefaultDocument() {
		XMLDocument doc = new XMLDocument(context);
		return doc;
	}

	/** {@inheritDoc} */
	@Override
	public ViewFactory getViewFactory() {
		return factory;
	}

	/**
	 * A simple view factory implementation.
	 */
	protected class XMLViewFactory implements ViewFactory {
		// Creates the XML View.
		@Override
		public View create(Element elem) {
			return new XMLView(context, elem);
		}
	}
}
