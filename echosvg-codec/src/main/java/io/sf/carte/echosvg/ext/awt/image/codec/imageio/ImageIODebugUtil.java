/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.ext.awt.image.codec.imageio;

import javax.imageio.metadata.IIOMetadata;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

/**
 * Helper class for debugging stuff in Image I/O.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImageIODebugUtil {

	public static void dumpMetadata(IIOMetadata meta) {
		String format = meta.getNativeMetadataFormatName();
		Node node = meta.getAsTree(format);
		dumpNode(node);
	}

	public static void dumpNode(Node node) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			Source src = new DOMSource(node);
			Result res = new StreamResult(System.out);
			t.transform(src, res);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
