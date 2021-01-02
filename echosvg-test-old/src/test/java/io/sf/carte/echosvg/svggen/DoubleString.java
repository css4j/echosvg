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
package io.sf.carte.echosvg.svggen;

import java.awt.geom.Rectangle2D;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.dom.GenericDOMImplementation;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.TestReport;

/**
 * Checks that no NullPointerException is thrown by default
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DoubleString extends AbstractTest {
	@Override
	public TestReport runImpl() throws Exception {
		// Get a DOMImplementation
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document
		Document document = domImpl.createDocument(null, "svg", null);

		SVGGraphics2D g = new SVGGraphics2D(document);
		Rectangle2D r = new Rectangle2D.Float(0.5f, 0.5f, 2.33f, 2.33f);
		g.fill(r);

		return reportSuccess();
	}
}
