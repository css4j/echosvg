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
package io.sf.carte.echosvg.transcoder.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage; 

import java.io.ByteArrayOutputStream;

import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.DOMImplementation;

/**
 * Test the ImageTranscoder input with a DOM tree.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id$
 */
public class DOMTest extends AbstractImageTranscoderTest {

    /**
     * Constructs a new <code>DOMTest</code>.
     */
    public DOMTest() {
    }

    /**
     * Creates the <code>TranscoderInput</code>.
     */
    protected TranscoderInput createTranscoderInput() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = impl.createDocument(svgNS, "svg", null);

        Element root = doc.getDocumentElement();

        root.setAttributeNS(null, "width", "400");
        root.setAttributeNS(null, "height", "400");

        Element r = doc.createElementNS(svgNS, "rect");
        r.setAttributeNS(null, "x", "0");
        r.setAttributeNS(null, "y", "0");
        r.setAttributeNS(null, "width", "400");
        r.setAttributeNS(null, "height", "400");
        r.setAttributeNS(null, "style", "fill:black");
        root.appendChild(r);

        r = doc.createElementNS(svgNS, "rect");
        r.setAttributeNS(null, "x", "100");
        r.setAttributeNS(null, "y", "50");
        r.setAttributeNS(null, "width", "100");
        r.setAttributeNS(null, "height", "50");
        r.setAttributeNS(null, "style", "stroke:red; fill:none");
        root.appendChild(r);

        return new TranscoderInput(doc);
    }

    /**
     * Returns the reference image for this test.
     */
    protected byte [] getReferenceImageData() {
        try {
            BufferedImage img = new BufferedImage
                (400, 400, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, 400, 400);
            g2d.setColor(Color.red);
            g2d.drawRect(100, 50, 100, 50);
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            PNGTranscoder t = new PNGTranscoder();
            TranscoderOutput output = new TranscoderOutput(ostream);
            t.writeImage(img, output);
            return ostream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("DOMTest error");
        }
    }
}
