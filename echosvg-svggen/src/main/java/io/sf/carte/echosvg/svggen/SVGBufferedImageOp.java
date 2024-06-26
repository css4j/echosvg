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

import java.awt.Rectangle;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * Utility class that converts a BufferedImageOp object into an SVG filter.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 * @see io.sf.carte.echosvg.svggen.SVGCustomBufferedImageOp
 * @see io.sf.carte.echosvg.svggen.SVGLookupOp
 * @see io.sf.carte.echosvg.svggen.SVGRescaleOp
 * @see io.sf.carte.echosvg.svggen.SVGConvolveOp
 */
public class SVGBufferedImageOp extends AbstractSVGFilterConverter {

	/**
	 * All LookupOp convertion is handed to svgLookupOp
	 */
	private SVGLookupOp svgLookupOp;

	/**
	 * All RescaleOp convertion is handed to svgRescaleOp
	 */
	private SVGRescaleOp svgRescaleOp;

	/**
	 * All ConvolveOp convertion is handed to svgConvolveOp
	 */
	private SVGConvolveOp svgConvolveOp;

	/**
	 * All custom BufferedImageOp convertion is handed to '
	 * svgCustomBufferedImageOp.
	 */
	private SVGCustomBufferedImageOp svgCustomBufferedImageOp;

	/**
	 * @param generatorContext used by the converter to create Element and other
	 *                         needed DOM objects and to handle unknown
	 *                         BufferedImageOp implementations.
	 */
	public SVGBufferedImageOp(SVGGeneratorContext generatorContext) {
		super(generatorContext);
		this.svgLookupOp = new SVGLookupOp(generatorContext);
		this.svgRescaleOp = new SVGRescaleOp(generatorContext);
		this.svgConvolveOp = new SVGConvolveOp(generatorContext);
		this.svgCustomBufferedImageOp = new SVGCustomBufferedImageOp(generatorContext);
	}

	/**
	 * @return Set of filter Elements defining the BufferedImageOp this Converter
	 *         has processed since it was created.
	 */
	@Override
	public List<Element> getDefinitionSet() {
		List<Element> filterSet = new LinkedList<>(svgLookupOp.getDefinitionSet());
		filterSet.addAll(svgRescaleOp.getDefinitionSet());
		filterSet.addAll(svgConvolveOp.getDefinitionSet());
		filterSet.addAll(svgCustomBufferedImageOp.getDefinitionSet());
		return filterSet;
	}

	public SVGLookupOp getLookupOpConverter() {
		return svgLookupOp;
	}

	public SVGRescaleOp getRescaleOpConverter() {
		return svgRescaleOp;
	}

	public SVGConvolveOp getConvolveOpConverter() {
		return svgConvolveOp;
	}

	public SVGCustomBufferedImageOp getCustomBufferedImageOpConverter() {
		return svgCustomBufferedImageOp;
	}

	/**
	 * @param op         BufferedImageOp to be converted to SVG
	 * @param filterRect Rectangle, in device space, that defines the area to which
	 *                   filtering applies. May be null, meaning that the area is
	 *                   undefined.
	 * @return an SVGFilterDescriptor representing the SVG filter equivalent of the
	 *         input BufferedImageOp
	 */
	@Override
	public SVGFilterDescriptor toSVG(BufferedImageOp op, Rectangle filterRect) {
		SVGFilterDescriptor filterDesc = svgCustomBufferedImageOp.toSVG(op, filterRect);

		if (filterDesc == null) {
			if (op instanceof LookupOp)
				filterDesc = svgLookupOp.toSVG(op, filterRect);
			else if (op instanceof RescaleOp)
				filterDesc = svgRescaleOp.toSVG(op, filterRect);
			else if (op instanceof ConvolveOp)
				filterDesc = svgConvolveOp.toSVG(op, filterRect);
		}

		return filterDesc;
	}

}
