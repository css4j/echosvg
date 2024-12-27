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

import org.w3c.dom.Document;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DOM3Test {

	static String SVG_NAMESPACE_URI = "http://www.w3.org/2000/svg";
	static String HTML_NAMESPACE_URI = "http://www.w3.org/1999/xhtml";
	static String EX_NAMESPACE_URI = "http://www.example.org/";
	static String XML_NAMESPACE_URI = "http://www.w3.org/XML/1998/namespace";
	static String XMLNS_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
	static String XML_EVENTS_NAMESPACE_URI = "http://www.w3.org/2001/xml-events";

	Document newDoc() {
		return GenericDOMImplementation.getDOMImplementation().createDocument(HTML_NAMESPACE_URI, "html", null);
	}

}
