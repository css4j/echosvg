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

import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

/**
 * Utility class that converts a Java RadialGradientPaint into an SVG radial gradient
 * element
 *
 * @author <a href="mailto:schegge42@gmail.com">Jens Kaiser</a>
 */
public class SVGRadialGradient extends AbstractSVGGradient {
	public SVGRadialGradient(SVGGeneratorContext generatorContext) {
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
		return toSVG((RadialGradientPaint) gc.getPaint());
	}


	/**
	 * @param gradient the RadialGradientPaint to be converted
	 * @return a description of the SVG paint and opacity corresponding to the
	 * gradient Paint. The definiton of the radialGradient is put in the
	 * radialGradientDefsMap
	 */
	public SVGPaintDescriptor toSVG(RadialGradientPaint gradient) {
		return (SVGPaintDescriptor) descMap.computeIfAbsent(gradient, g -> createtSvgPaintDescriptor((RadialGradientPaint) g));
	}

	private SVGPaintDescriptor createtSvgPaintDescriptor(RadialGradientPaint gradient) {
		Document domFactory = getGeneratorContext().getDOMFactory();
		Element gradientDef = domFactory.createElementNS(SVG_NAMESPACE_URI, SVG_RADIAL_GRADIENT_TAG);
		gradientDef.setAttribute(SVG_GRADIENT_UNITS_ATTRIBUTE, SVG_USER_SPACE_ON_USE_VALUE);

		Point2D center = gradient.getCenterPoint();
		Point2D focus = gradient.getFocusPoint();

		String id = getGeneratorContext().getIDGenerator().generateID(ID_PREFIX_RADIAL_GRADIENT);
		gradientDef.setAttribute(SVG_ID_ATTRIBUTE, id);
		gradientDef.setAttribute(SVG_CX_ATTRIBUTE, doubleString(center.getX()));
		gradientDef.setAttribute(SVG_CY_ATTRIBUTE, doubleString(center.getY()));
		gradientDef.setAttribute(SVG_FX_ATTRIBUTE, doubleString(focus.getX()));
		gradientDef.setAttribute(SVG_FY_ATTRIBUTE, doubleString(focus.getY()));
		gradientDef.setAttribute(SVG_R_ATTRIBUTE, doubleString(gradient.getRadius()));
		gradientDef.setAttribute(SVG_SPREAD_METHOD_ATTRIBUTE, getSpreadMethod(gradient));

		addGradientStops(gradient, gradientDef, domFactory);

		defSet.add(gradientDef);

		return new SVGPaintDescriptor(URL_PREFIX + SIGN_POUND + id + URL_SUFFIX, SVG_OPAQUE_VALUE, gradientDef);
	}
}
