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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Testing shear.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ShearTest implements Painter {

	@Override
	public void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Shape
		Ellipse2D circle = new Ellipse2D.Float(0, 0, 50, 60);

		// Thick stroke
		BasicStroke stroke = new BasicStroke(15);

		g.shear(0, 1);
		g.translate(100, 100);

		g.setStroke(stroke);
		g.setPaint(Color.gray);
		g.draw(circle);

		java.awt.geom.AffineTransform txf = g.getTransform();
		Shape ellipse = txf.createTransformedShape(circle);

		g.setTransform(new java.awt.geom.AffineTransform());
		g.translate(0, -150);

		g.draw(ellipse);
	}

}
