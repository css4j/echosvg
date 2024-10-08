/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * DOM implementation.
 * 
 * @uses org.xml.sax.XMLReader
 * @uses io.sf.carte.echosvg.dom.DomExtension
 */
module io.sf.carte.echosvg.dom {
	exports io.sf.carte.echosvg.dom.util;
	exports io.sf.carte.echosvg.dom.traversal;
	exports io.sf.carte.echosvg.dom;
	exports io.sf.carte.echosvg.dom.xbl;
	exports io.sf.carte.echosvg.dom.events;

	requires transitive io.sf.carte.echosvg.css;
	requires io.sf.carte.echosvg.constants;
	requires io.sf.carte.echosvg.external;
	requires io.sf.carte.echosvg.i18n;
	requires io.sf.carte.echosvg.xml;

	requires io.sf.carte.xml.dtd;

	requires transitive org.w3c.dom.view;

	requires transitive java.xml;
	requires transitive jdk.xml.dom;

	uses org.xml.sax.XMLReader;
	uses io.sf.carte.echosvg.dom.DomExtension;
}
