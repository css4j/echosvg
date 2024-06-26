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
package io.sf.carte.echosvg.swing.svg;

import java.util.EventObject;

import org.w3c.dom.svg.SVGDocument;

/**
 * This class represents an event which indicate an event originated from a
 * SVGDocumentLoader instance.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGDocumentLoaderEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	/**
	 * The associated SVG document.
	 */
	protected SVGDocument svgDocument;

	/**
	 * Creates a new SVGDocumentLoaderEvent.
	 * 
	 * @param source the object that originated the event, ie. the
	 *               SVGDocumentLoader.
	 * @param doc    The associated document.
	 */
	public SVGDocumentLoaderEvent(Object source, SVGDocument doc) {
		super(source);
		svgDocument = doc;
	}

	/**
	 * Returns the associated SVG document, or null if the loading was just started
	 * or an error occured.
	 */
	public SVGDocument getSVGDocument() {
		return svgDocument;
	}

}
