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

import org.w3c.dom.Node;

/**
 * XML serializer.
 * 
 * <p>
 * Implementations should be able to correctly serialize SVG.
 * </p>
 */
public interface XmlSerializer {

	/**
	 * Serialize the given XML node to the given writer.
	 * <p>
	 * Callers of this method should catch {@code RuntimeException} in addition to
	 * the {@link SVGGraphics2DRuntimeException}.
	 * </p>
	 * 
	 * @param node    the node to serialize.
	 * @param writer  the writer to write the serialization output.
	 * @param escaped defines if the characters in Text nodes and attribute values
	 *                should be escaped.
	 * @throws IOException                   if an I/O error occurs.
	 * @throws SVGGraphics2DRuntimeException or other runtime exception if the node
	 *                                       hierarchy is inconsistent or contains
	 *                                       unknown nodes.
	 */
	default void serializeXML(Node node, Writer writer, boolean escaped)
			throws IOException, SVGGraphics2DRuntimeException {
		XmlWriter xmlWri = new XmlWriter(writer, escaped);
		xmlWri.writeXml(node);
	}

	/**
	 * Serialize a document type with the given identifiers.
	 * 
	 * @param writer   the writer to write the serialization output.
	 * @param name     the document type name.
	 * @param publicId the public identifier, or {@code null} if none.
	 * @param systemId the system identifier, or {@code null} if none.
	 * @throws IOException if an I/O error occurs.
	 */
	default void writeDocumentType(Writer writer, String name, String publicId, String systemId)
			throws IOException {
		XmlWriter.writeDocumentType(writer, name, publicId, systemId);
	}

}
