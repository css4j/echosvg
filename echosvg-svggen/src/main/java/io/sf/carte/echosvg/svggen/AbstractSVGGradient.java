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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.Color;
import java.awt.MultipleGradientPaint;

/**
 * Abstract Utility class that converts a Java MultipleGradientPaint into an SVG gradient
 * element
 *
 * @author <a href="mailto:schegge42@gmail.com">Jens Kaiser</a>
 */
public abstract class AbstractSVGGradient extends AbstractSVGConverter {
	public AbstractSVGGradient(SVGGeneratorContext generatorContext) {
		super(generatorContext);
	}

	protected static String getSpreadMethod(MultipleGradientPaint gradient) {
		switch (gradient.getCycleMethod()) {
			case REFLECT:
				return SVG_REFLECT_VALUE;
			case REPEAT:
				return SVG_REPEAT_VALUE;
			case NO_CYCLE:
			default:
				return SVG_PAD_VALUE;
		}
	}

	protected Element getGradientStop(Document domFactory, float[] gradientFractions, int i, Color[] gradientColors) {
		Element gradientStop = domFactory.createElementNS(SVG_NAMESPACE_URI, SVG_STOP_TAG);
		gradientStop.setAttribute(SVG_OFFSET_ATTRIBUTE, doubleString(gradientFractions[i] * 100) + "%");

		SVGPaintDescriptor colorDesc = SVGColor.toSVG(gradientColors[i], getGeneratorContext());
		gradientStop.setAttribute(SVG_STOP_COLOR_ATTRIBUTE, colorDesc.getPaintValue());
		gradientStop.setAttribute(SVG_STOP_OPACITY_ATTRIBUTE, colorDesc.getOpacityValue());
		return gradientStop;
	}

	protected void addGradientStops(MultipleGradientPaint gradient, Element gradientDef, Document domFactory) {
		Color[] gradientColors = gradient.getColors();
		float[] gradientFractions = gradient.getFractions();

		for (int i = 0; i < gradientColors.length; i++) {
			gradientDef.appendChild(getGradientStop(domFactory, gradientFractions, i, gradientColors));
		}
	}
}
