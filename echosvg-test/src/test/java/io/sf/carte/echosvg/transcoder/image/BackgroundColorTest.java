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
package io.sf.carte.echosvg.transcoder.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;

/**
 * Test the ImageTranscoder with the KEY_BACKGROUND_COLOR transcoding hint.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class BackgroundColorTest extends AbstractImageTranscoderTest {

	/**
	 * Runs a new <code>BackgroundColorTest</code>.
	 * @throws TranscoderException 
	 */
	@org.junit.Test
	public void testBackgroundColor() throws TranscoderException {
		runTest();
	}

	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	@Override
	protected TranscoderInput createTranscoderInput() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		Element root = doc.getDocumentElement();

		root.setAttributeNS(null, "width", "400");
		root.setAttributeNS(null, "height", "400");

		Element r = doc.createElementNS(svgNS, "rect");
		r.setAttributeNS(null, "x", "100");
		r.setAttributeNS(null, "y", "50");
		r.setAttributeNS(null, "width", "100");
		r.setAttributeNS(null, "height", "50");
		r.setAttributeNS(null, "style", "fill:red");
		root.appendChild(r);

		return new TranscoderInput(doc);
	}

	/**
	 * Creates a Map that contains additional transcoding hints.
	 */
	@Override
	protected Map<Key, Object> createTranscodingHints() {
		Map<Key, Object> hints = new HashMap<>(7);
		hints.put(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.blue);
		return hints;
	}

	/**
	 * Returns the reference image for this test.
	 */
	@Override
	protected byte[] getReferenceImageData() {
		try {
			BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.setColor(Color.blue);
			g2d.fillRect(0, 0, 400, 400);
			g2d.setColor(Color.red);
			g2d.fillRect(100, 50, 100, 50);
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			PNGTranscoder t = new PNGTranscoder();
			TranscoderOutput output = new TranscoderOutput(ostream);
			t.writeImage(img, output);
			return ostream.toByteArray();
		} catch (Exception ex) {
			throw new RuntimeException("BackgroundColorTest error");
		}
	}
}
