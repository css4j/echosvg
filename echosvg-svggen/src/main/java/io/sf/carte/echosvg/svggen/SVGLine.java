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

import java.awt.geom.Line2D;

import org.w3c.dom.Element;

/**
 * Utility class that converts a Line2D object into a corresponding SVG line
 * element.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGLine extends SVGGraphicObjectConverter {
	/**
	 * @param generatorContext used to build Elements
	 */
	public SVGLine(SVGGeneratorContext generatorContext) {
		super(generatorContext);
	}

	/**
	 * @param line the Line2D object to be converted
	 */
	public Element toSVG(Line2D line) {
		Element svgLine = getGeneratorContext().getDOMFactory().createElementNS(SVG_NAMESPACE_URI, SVG_LINE_TAG);
		svgLine.setAttributeNS(null, SVG_X1_ATTRIBUTE, doubleString(line.getX1()));
		svgLine.setAttributeNS(null, SVG_Y1_ATTRIBUTE, doubleString(line.getY1()));
		svgLine.setAttributeNS(null, SVG_X2_ATTRIBUTE, doubleString(line.getX2()));
		svgLine.setAttributeNS(null, SVG_Y2_ATTRIBUTE, doubleString(line.getY2()));
		return svgLine;
	}
}
