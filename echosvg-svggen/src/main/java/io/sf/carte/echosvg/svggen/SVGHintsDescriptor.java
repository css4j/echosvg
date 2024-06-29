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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * Describes a set of SVG hints
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 * @see io.sf.carte.echosvg.svggen.SVGRenderingHints
 */
public class SVGHintsDescriptor implements SVGDescriptor, SVGSyntax {

	private String colorInterpolation;
	private String colorRendering;
	private String textRendering;
	private String shapeRendering;
	private String imageRendering;

	/**
	 * Constructor
	 */
	public SVGHintsDescriptor(String colorInterpolation, String colorRendering, String textRendering,
			String shapeRendering, String imageRendering) {
		if (colorInterpolation == null || colorRendering == null || textRendering == null || shapeRendering == null
				|| imageRendering == null)
			throw new SVGGraphics2DRuntimeException(ErrorConstants.ERR_HINT_NULL);

		this.colorInterpolation = colorInterpolation;
		this.colorRendering = colorRendering;
		this.textRendering = textRendering;
		this.shapeRendering = shapeRendering;
		this.imageRendering = imageRendering;
	}

	@Override
	public Map<String, String> getAttributeMap(Map<String, String> attrMap) {
		if (attrMap == null)
			attrMap = new HashMap<>();

		attrMap.put(SVG_COLOR_INTERPOLATION_ATTRIBUTE, colorInterpolation);
		attrMap.put(SVG_COLOR_RENDERING_ATTRIBUTE, colorRendering);
		attrMap.put(SVG_TEXT_RENDERING_ATTRIBUTE, textRendering);
		attrMap.put(SVG_SHAPE_RENDERING_ATTRIBUTE, shapeRendering);
		attrMap.put(SVG_IMAGE_RENDERING_ATTRIBUTE, imageRendering);

		return attrMap;
	}

	@Override
	public List<Element> getDefinitionSet(List<Element> defSet) {
		if (defSet == null)
			defSet = new LinkedList<>();

		return defSet;
	}

}
