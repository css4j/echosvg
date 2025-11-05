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

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.ext.awt.g2d.GraphicContext;

/**
 * Utility class that converts a Paint object into an SVG element.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 * @see SVGLinearGradient
 * @see SVGTexturePaint
 */
public class SVGPaint implements SVGConverter {

	/**
	 * All GradientPaint convertions are handed to svgLinearGradient
	 */
	private SVGGradient svgGradient;

	/**
	 * All LinearGradientPaint convertions are handed to svgLinearGradient
	 */
	private SVGLinearGradient svgLinearGradient;

	/**
	 * All RadialGradientPaint convertions are handed to svgRadialGradient
	 */
	private SVGRadialGradient svgRadialGradient;

	/**
	 * All TexturePaint convertions are handed to svgTextureGradient
	 */
	private SVGTexturePaint svgTexturePaint;

	/**
	 * All Color convertions are handed to svgColor
	 */
	private SVGColor svgColor;

	/**
	 * All custom Paint convetions are handed to svgCustomPaint
	 */
	private SVGCustomPaint svgCustomPaint;

	/**
	 * Used to generate DOM elements
	 */
	private SVGGeneratorContext generatorContext;

	/**
	 * @param generatorContext the context.
	 */
	public SVGPaint(SVGGeneratorContext generatorContext) {
		this.svgGradient = new SVGGradient(generatorContext);
		this.svgLinearGradient = new SVGLinearGradient(generatorContext);
		this.svgRadialGradient = new SVGRadialGradient(generatorContext);
		this.svgTexturePaint = new SVGTexturePaint(generatorContext);
		this.svgCustomPaint = new SVGCustomPaint(generatorContext);
		this.svgColor = new SVGColor(generatorContext);
		this.generatorContext = generatorContext;
	}

	/**
	 * @return Set of Elements defining the Paints this converter has processed
	 *         since it was created
	 */
	@Override
	public List<Element> getDefinitionSet() {
		List<Element> paintDefs = new LinkedList<>(svgGradient.getDefinitionSet());
		paintDefs.addAll(svgLinearGradient.getDefinitionSet());
		paintDefs.addAll(svgRadialGradient.getDefinitionSet());
		paintDefs.addAll(svgTexturePaint.getDefinitionSet());
		paintDefs.addAll(svgCustomPaint.getDefinitionSet());
		paintDefs.addAll(svgColor.getDefinitionSet());
		return paintDefs;
	}

	public SVGTexturePaint getTexturePaintConverter() {
		return svgTexturePaint;
	}

	public SVGGradient getGradientPaintConverter() {
		return svgGradient;
	}

	public SVGLinearGradient getLinearGradientPaintConverter() {
		return svgLinearGradient;
	}

	public SVGRadialGradient getRadialGradientPaintConverter() {
		return svgRadialGradient;
	}

	public SVGCustomPaint getCustomPaintConverter() {
		return svgCustomPaint;
	}

	public SVGColor getColorConverter() {
		return svgColor;
	}

	/**
	 * Converts part or all of the input GraphicContext into a set of
	 * attribute/value pairs and related definitions
	 *
	 * @param gc GraphicContext to be converted
	 * @return descriptor of the attributes required to represent some or all of the
	 *         GraphicContext state, along with the related definitions
	 * @see SVGDescriptor
	 */
	@Override
	public SVGDescriptor toSVG(GraphicContext gc) {
		return toSVG(gc.getPaint());
	}

	/**
	 * @param paint Paint to be converted to SVG
	 * @return a descriptor of the corresponding SVG paint
	 */
	public SVGPaintDescriptor toSVG(Paint paint) {
		// we first try the extension handler because we may
		// want to override the way a Paint is managed!
		SVGPaintDescriptor paintDesc = svgCustomPaint.toSVG(paint);
		if (paintDesc != null) {
			return paintDesc;
		}

		if (paint instanceof Color)
			paintDesc = SVGColor.toSVG((Color) paint, generatorContext);
		else if (paint instanceof GradientPaint)
			paintDesc = svgGradient.toSVG((GradientPaint) paint);
		else if (paint instanceof LinearGradientPaint)
			paintDesc = svgLinearGradient.toSVG((LinearGradientPaint) paint);
		else if (paint instanceof RadialGradientPaint)
			paintDesc = svgRadialGradient.toSVG((RadialGradientPaint) paint);
		else if (paint instanceof TexturePaint)
			paintDesc = svgTexturePaint.toSVG((TexturePaint) paint);

		return paintDesc;
	}

}
