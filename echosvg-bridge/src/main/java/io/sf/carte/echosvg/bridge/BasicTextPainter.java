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

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Basic implementation of TextPainter which renders the attributed character
 * iterator of a <code>TextNode</code>. Suitable for use with "standard"
 * java.awt.font.TextAttributes only.
 * 
 * @see java.awt.font.TextAttribute
 *
 * @author <a href="mailto:bill.haneman@ireland.sun.com">Bill Haneman</a>
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class BasicTextPainter implements TextPainter {

	private static TextLayoutFactory textLayoutFactory = new ConcreteTextLayoutFactory();

	/**
	 * The font render context to use.
	 */
	protected FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);

	protected FontRenderContext aaOffFontRenderContext = new FontRenderContext(new AffineTransform(), false, true);

	protected TextLayoutFactory getTextLayoutFactory() {
		return textLayoutFactory;
	}

	/**
	 * Given an X, y coordinate, AttributedCharacterIterator, return a Mark which
	 * encapsulates a "selection start" action. The standard order of method calls
	 * for selection is: selectAt(); [selectTo(),...], selectTo(); getSelection().
	 */
	@Override
	public Mark selectAt(double x, double y, TextNode node) {
		return hitTest(x, y, node);
	}

	/**
	 * Given an X, y coordinate, starting Mark, AttributedCharacterIterator, return
	 * a Mark which encapsulates a "selection continued" action. The standard order
	 * of method calls for selection is: selectAt(); [selectTo(),...], selectTo();
	 * getSelection().
	 */
	@Override
	public Mark selectTo(double x, double y, Mark beginMark) {
		if (beginMark == null) {
			return null;
		} else {
			return hitTest(x, y, beginMark.getTextNode());
		}
	}

	/**
	 * Get a Rectangle2D in userspace coords which encloses the textnode glyphs just
	 * including the geometry info.
	 * 
	 * @param node the TextNode to measure
	 */
	@Override
	public Rectangle2D getGeometryBounds(TextNode node) {
		return getOutline(node).getBounds2D();
	}

	/**
	 * Returns the mark for the specified parameters.
	 */
	protected abstract Mark hitTest(double x, double y, TextNode node);

	// ------------------------------------------------------------------------
	// Inner class - implementation of the Mark interface
	// ------------------------------------------------------------------------

	/**
	 * This TextPainter's implementation of the Mark interface.
	 */
	protected static class BasicMark implements Mark {

		private TextNode node;
		private TextHit hit;

		/**
		 * Constructs a new Mark with the specified parameters.
		 */
		protected BasicMark(TextNode node, TextHit hit) {
			this.hit = hit;
			this.node = node;
		}

		public TextHit getHit() {
			return hit;
		}

		@Override
		public TextNode getTextNode() {
			return node;
		}

		/**
		 * Returns the index of the character that has been hit.
		 *
		 * @return The character index.
		 */
		@Override
		public int getCharIndex() {
			return hit.getCharIndex();
		}

	}

}
