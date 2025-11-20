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

package io.sf.carte.echosvg.svggen;

import java.io.IOException;
import java.io.Writer;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;

/**
 * Compact SVG serialization.
 */
public class CompactXmlSerializer implements XmlSerializer {

	@Override
	public void serializeXML(Node node, Writer writer, boolean escaped)
			throws IOException, SVGGraphics2DRuntimeException {
		XmlWriter.IndentWriter iwriter = new XmlWriter.IndentWriter(writer) {

			@Override
			public void printIndent() throws IOException {
			}

		};

		XmlWriter xmlWri = new XmlWriter(iwriter, escaped) {

			@Override
			protected void writeXml(DocumentType docType) throws IOException {
				IndentWriter out = getWriter();
				out.write("<!DOCTYPE ");
				out.write(docType.getName());

				String publicId = docType.getPublicId();
				if (publicId != null) {
					out.write(" PUBLIC '");
					out.write(publicId);
					out.write('\'');
				}

				String systemId = docType.getSystemId();
				if (systemId != null) {
					out.write(" '");
					out.write(systemId);
					out.write('\'');
				}

				out.write('>');
			}

		};

		xmlWri.writeXml(node);
	}

	@Override
	public void writeDocumentType(Writer out, String name, String publicId, String systemId)
			throws IOException {
		out.write("<!DOCTYPE ");
		out.write(name);
		if (publicId != null) {
			out.write(" PUBLIC '");
			out.write(publicId);
			out.write('\'');
		}
		if (systemId != null) {
			out.write(" '");
			out.write(systemId);
			out.write('\'');
		}
		out.write('>');
	}

}
