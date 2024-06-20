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
package io.sf.carte.echosvg.bridge;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.DefaultBrokenLinkProvider;
import io.sf.carte.echosvg.gvt.CompositeGraphicsNode;
import io.sf.carte.echosvg.gvt.ShapeNode;
import io.sf.carte.echosvg.gvt.filter.GraphicsNodeRable8Bit;

/**
 * This interface is to be used to provide alternate ways of generating a
 * placeholder image when the ImageTagRegistry fails to handle a given
 * reference.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGBrokenLinkProvider extends DefaultBrokenLinkProvider implements ErrorConstants {

	public SVGBrokenLinkProvider() {
	}

	/**
	 * This method is responsible for constructing an image that will represent the
	 * missing image in the document. This method receives information about the
	 * reason a broken link image is being requested in the <code>code</code> and
	 * <code>params</code> parameters. These parameters may be used to generate
	 * nicely localized messages for insertion into the broken link image, or for
	 * selecting the broken link image returned.
	 *
	 * @param code   This is the reason the image is unavailable should be taken
	 *               from ErrorConstants.
	 * @param params This is more detailed information about the circumstances of
	 *               the failure.
	 */
	@Override
	public Filter getBrokenLinkImage(Object base, String code, Object[] params) {
		String message = formatMessage(base, code, params);
		Map<String, Object> props = new HashMap<>();
		props.put(BROKEN_LINK_PROPERTY, message);

		CompositeGraphicsNode cgn = new CompositeGraphicsNode();
		ShapeNode shapeNode = new ShapeNode();
		shapeNode.setShape(new java.awt.Rectangle(100, 100));
		TextNode textNode = new TextNode();
		AttributedString atts = new AttributedString(message);
		atts.addAttribute(StrokingTextPainter.GVT_FONTS, new LinkedList<>());
		atts.addAttribute(TextAttribute.SIZE, 14f);
		textNode.setAttributedCharacterIterator(atts.getIterator());
		cgn.add(shapeNode);
		cgn.add(textNode);

		return new GraphicsNodeRable8Bit(cgn, props);
	}

}
