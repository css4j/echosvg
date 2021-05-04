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

package io.sf.carte.echosvg.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * This class describes the XML resources needed to use the various EchoSVG
 * modules.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class XMLResourceDescriptor {

	/**
	 * The XML parser class name key.
	 */
	public static final String XML_PARSER_CLASS_NAME_KEY = "org.xml.sax.driver";

	/**
	 * The resources file name
	 */
	public static final String RESOURCES = "resources/XMLResourceDescriptor.properties";

	/**
	 * The resource bundle
	 */
	protected static Properties parserProps = null;

	/**
	 * The class name of the XML parser to use.
	 */
	protected static String xmlParserClassName;

	protected static synchronized Properties getParserProps() {
		if (parserProps != null)
			return parserProps;

		parserProps = new Properties();
		try {
			Class<XMLResourceDescriptor> cls = XMLResourceDescriptor.class;
			InputStream is = cls.getResourceAsStream(RESOURCES);
			parserProps.load(is);
			is.close();
		} catch (IOException ioe) {
			throw new MissingResourceException(ioe.getMessage(), RESOURCES, null);
		}
		return parserProps;
	}

	/**
	 * Returns the class name of the XML parser to use.
	 *
	 * <p>
	 * This method first checks if any XML parser has been specified using the
	 * <code>setXMLParserClassName</code> method. If any, this method will return
	 * the value of the property 'org.xml.sax.driver' specified in the
	 * <code>resources/XMLResourceDescriptor.properties</code> resource file.
	 */
	public static String getXMLParserClassName() {
		if (xmlParserClassName == null) {
			xmlParserClassName = getParserProps().getProperty(XML_PARSER_CLASS_NAME_KEY);
		}
		return xmlParserClassName;
	}

	/**
	 * Sets the class name of the XML parser to use.
	 *
	 * @param xmlParserClassName the classname of the XML parser
	 */
	public static void setXMLParserClassName(String xmlParserClassName) {
		XMLResourceDescriptor.xmlParserClassName = xmlParserClassName;
	}

}
