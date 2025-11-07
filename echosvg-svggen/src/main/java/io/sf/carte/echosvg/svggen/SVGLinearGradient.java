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

import io.sf.carte.echosvg.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.geom.Point2D;

/**
 * Utility class that converts a Java LinearGradientPaint into an SVG linear gradient
 * element
 *
 * @author <a href="mailto:schegge42@gmail.com">Jens Kaiser</a>
 */
public class SVGLinearGradient extends AbstractSVGGradient {

	/**
	 * @param generatorContext used to build Elements
	 */
	public SVGLinearGradient(SVGGeneratorContext generatorContext) {
		super(generatorContext);
	}

	/**
	 * Converts part or all of the input GraphicContext into a set of
	 * attribute/value pairs and related definitions
	 *
	 * @param gc GraphicContext to be converted
	 * @return descriptor of the attributes required to represent some or all of the
	 * GraphicContext state, along with the related definitions
	 * @see SVGDescriptor
	 */
	@Override
	public SVGDescriptor toSVG(GraphicContext gc) {
		Paint paint = gc.getPaint();
		return toSVG((LinearGradientPaint) paint);
	}

	/**
	 * @param gradient the LinearGradientPaint to be converted
	 * @return a description of the SVG paint and opacity corresponding to the
	 * gradient Paint. The definiton of the linearGradient is put in the
	 * linearGradientDefsMap
	 */
	public SVGPaintDescriptor toSVG(LinearGradientPaint gradient) {
		// Reuse definition if gradient has already been converted
		return (SVGPaintDescriptor) descMap.computeIfAbsent(gradient, g -> createtSvgPaintDescriptor((LinearGradientPaint) g));
	}

	private SVGPaintDescriptor createtSvgPaintDescriptor(LinearGradientPaint gradient) {
		Document domFactory = getGeneratorContext().getDOMFactory();
		Element gradientDef = domFactory.createElementNS(SVG_NAMESPACE_URI, SVG_LINEAR_GRADIENT_TAG);
		gradientDef.setAttribute(SVG_GRADIENT_UNITS_ATTRIBUTE, SVG_USER_SPACE_ON_USE_VALUE);

		Point2D p1 = gradient.getStartPoint();
		Point2D p2 = gradient.getEndPoint();

		String id = getGeneratorContext().getIDGenerator().generateID(ID_PREFIX_LINEAR_GRADIENT);
		gradientDef.setAttribute(SVG_ID_ATTRIBUTE, id);
		gradientDef.setAttribute(SVG_X1_ATTRIBUTE, doubleString(p1.getX()));
		gradientDef.setAttribute(SVG_Y1_ATTRIBUTE, doubleString(p1.getY()));
		gradientDef.setAttribute(SVG_X2_ATTRIBUTE, doubleString(p2.getX()));
		gradientDef.setAttribute(SVG_Y2_ATTRIBUTE, doubleString(p2.getY()));
		gradientDef.setAttribute(SVG_SPREAD_METHOD_ATTRIBUTE, getSpreadMethod(gradient));

		addGradientStops(gradient, gradientDef, domFactory);

		defSet.add(gradientDef);

		return new SVGPaintDescriptor(URL_PREFIX + SIGN_POUND + id + URL_SUFFIX, SVG_OPAQUE_VALUE, gradientDef);
	}
}
