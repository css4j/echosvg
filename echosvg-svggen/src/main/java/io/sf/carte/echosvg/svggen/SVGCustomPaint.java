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

import java.awt.Paint;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.ext.awt.g2d.GraphicContext;

/**
 * Utility class that converts an custom Paint object into a set of SVG
 * properties and definitions.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 * @see io.sf.carte.echosvg.svggen.SVGPaint
 */
public class SVGCustomPaint extends AbstractSVGConverter {
	/**
	 * @param generatorContext the context.
	 */
	public SVGCustomPaint(SVGGeneratorContext generatorContext) {
		super(generatorContext);
	}

	/**
	 * Converts part or all of the input GraphicContext into a set of
	 * attribute/value pairs and related definitions
	 *
	 * @param gc GraphicContext to be converted
	 * @return descriptor of the attributes required to represent some or all of the
	 *         GraphicContext state, along with the related definitions
	 * @see io.sf.carte.echosvg.svggen.SVGDescriptor
	 */
	@Override
	public SVGDescriptor toSVG(GraphicContext gc) {
		return toSVG(gc.getPaint());
	}

	/**
	 * @param paint the Paint object to convert to SVG
	 * @return a description of the SVG paint and opacity corresponding to the
	 *         Paint. The definiton of the paint is put in the linearGradientDefsMap
	 */
	public SVGPaintDescriptor toSVG(Paint paint) {
		SVGPaintDescriptor paintDesc = (SVGPaintDescriptor) descMap.get(paint);

		if (paintDesc == null) {
			// First time this paint is used. Request handler
			// to do the conversion
			paintDesc = getGeneratorContext().getExtensionHandler().handlePaint(paint, getGeneratorContext());

			if (paintDesc != null) {
				Element def = paintDesc.getDef();
				if (def != null)
					defSet.add(def);
				descMap.put(paint, paintDesc);
			}
		}

		return paintDesc;
	}
}
