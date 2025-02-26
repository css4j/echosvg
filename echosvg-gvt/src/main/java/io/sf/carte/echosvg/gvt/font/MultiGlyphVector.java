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
package io.sf.carte.echosvg.gvt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.util.List;

import io.sf.carte.echosvg.gvt.text.AttributedCharacterSpanIterator;

/**
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class MultiGlyphVector implements GVTGlyphVector {

	GVTGlyphVector[] gvs;
	int[] nGlyphs;
	int[] off;

	int nGlyph;

	public MultiGlyphVector(List<GVTGlyphVector> gvs) {
		int nSlots = gvs.size();
		this.gvs = new GVTGlyphVector[nSlots];
		this.nGlyphs = new int[nSlots];
		this.off = new int[nSlots];

		int i = 0;
		for (GVTGlyphVector gv : gvs) {
			off[i] = nGlyph;

			this.gvs[i] = gv;
			nGlyphs[i] = gv.getNumGlyphs();
			nGlyph += nGlyphs[i];
			i++;
		}
		nGlyphs[i - 1]++;
	}

	/**
	 * Returns the number of glyphs in this GlyphVector.
	 */
	@Override
	public int getNumGlyphs() {
		return nGlyph;
	}

	int getGVIdx(int glyphIdx) {
		if (glyphIdx > nGlyph)
			return -1;
		if (glyphIdx == nGlyph)
			return gvs.length - 1;
		for (int i = 0; i < nGlyphs.length; i++)
			if (glyphIdx - off[i] < nGlyphs[i])
				return i;
		return -1;
	}

	/**
	 * Returns the Font associated with this GlyphVector.
	 */
	@Override
	public GVTFont getFont() {
		throw new IllegalArgumentException("Can't be correctly Implemented");
	}

	/**
	 * Returns the FontRenderContext associated with this GlyphVector.
	 */
	@Override
	public FontRenderContext getFontRenderContext() {
		return gvs[0].getFontRenderContext();
	}

	/**
	 * Returns the glyphcode of the specified glyph.
	 */
	@Override
	public int getGlyphCode(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphCode(glyphIndex - off[idx]);
	}

	/**
	 * Returns the justification information for the glyph at the specified index
	 * into this GlyphVector.
	 */
	@Override
	public GlyphJustificationInfo getGlyphJustificationInfo(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphJustificationInfo(glyphIndex - off[idx]);
	}

	/**
	 * Returns the logical bounds of the specified glyph within this GlyphVector.
	 */
	@Override
	public Shape getGlyphLogicalBounds(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphLogicalBounds(glyphIndex - off[idx]);
	}

	/**
	 * Returns the metrics of the glyph at the specified index into this
	 * GlyphVector.
	 */
	@Override
	public GVTGlyphMetrics getGlyphMetrics(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphMetrics(glyphIndex - off[idx]);
	}

	/**
	 * Returns a Shape whose interior corresponds to the visual representation of
	 * the specified glyph within this GlyphVector.
	 */
	@Override
	public Shape getGlyphOutline(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphOutline(glyphIndex - off[idx]);
	}

	/**
	 * Returns the bounding box of the specified glyph, considering only the glyph's
	 * metrics (ascent, descent, advance) rather than the actual glyph shape.
	 */
	@Override
	public Rectangle2D getGlyphCellBounds(int glyphIndex) {
		return getGlyphLogicalBounds(glyphIndex).getBounds2D();
	}

	/**
	 * Returns the position of the specified glyph within this GlyphVector.
	 */
	@Override
	public Point2D getGlyphPosition(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphPosition(glyphIndex - off[idx]);
	}

	/**
	 * Gets the transform of the specified glyph within this GlyphVector.
	 */
	@Override
	public AffineTransform getGlyphTransform(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphTransform(glyphIndex - off[idx]);
	}

	/**
	 * Returns the visual bounds of the specified glyph within the GlyphVector.
	 */
	@Override
	public Shape getGlyphVisualBounds(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].getGlyphVisualBounds(glyphIndex - off[idx]);
	}

	/**
	 * Sets the position of the specified glyph within this GlyphVector.
	 */
	@Override
	public void setGlyphPosition(int glyphIndex, Point2D newPos) {
		int idx = getGVIdx(glyphIndex);
		// System.out.println("setting: " + idx + " - " + (glyphIndex-off[idx]) +
		// " -> " + newPos);
		gvs[idx].setGlyphPosition(glyphIndex - off[idx], newPos);
	}

	/**
	 * Sets the transform of the specified glyph within this GlyphVector.
	 */
	@Override
	public void setGlyphTransform(int glyphIndex, AffineTransform newTX) {
		int idx = getGVIdx(glyphIndex);
		gvs[idx].setGlyphTransform(glyphIndex - off[idx], newTX);
	}

	/**
	 * Tells the glyph vector whether or not to draw the specified glyph.
	 */
	@Override
	public void setGlyphVisible(int glyphIndex, boolean visible) {
		int idx = getGVIdx(glyphIndex);
		gvs[idx].setGlyphVisible(glyphIndex - off[idx], visible);
	}

	/**
	 * Returns true if specified glyph will be drawn.
	 */
	@Override
	public boolean isGlyphVisible(int glyphIndex) {
		int idx = getGVIdx(glyphIndex);
		return gvs[idx].isGlyphVisible(glyphIndex - off[idx]);
	}

	/**
	 * Returns an array of glyphcodes for the specified glyphs.
	 */
	@Override
	public int[] getGlyphCodes(int beginGlyphIndex, int numEntries, int[] codeReturn) {
		int[] ret = codeReturn;
		if (ret == null)
			ret = new int[numEntries];
		int[] tmp = null;

		int gvIdx = getGVIdx(beginGlyphIndex);
		int gi = beginGlyphIndex - off[gvIdx];
		int i = 0;
		GVTGlyphVector gv;
		while (numEntries != 0) {
			int len = numEntries;
			if (gi + len > nGlyphs[gvIdx])
				len = nGlyphs[gvIdx] - gi;
			gv = gvs[gvIdx];
			if (i == 0) {
				gv.getGlyphCodes(gi, len, ret);
			} else {
				if ((tmp == null) || (tmp.length < len))
					tmp = new int[len];

				gv.getGlyphCodes(gi, len, tmp);
				System.arraycopy(tmp, 0, ret, i, len);
			}
			gi = 0;
			gvIdx++;
			numEntries -= len;
			i += len;
		}
		return ret;
	}

	/**
	 * Returns an array of glyph positions for the specified glyphs
	 */
	@Override
	public float[] getGlyphPositions(int beginGlyphIndex, int numEntries, float[] positionReturn) {
		float[] ret = positionReturn;
		if (ret == null)
			ret = new float[numEntries * 2];
		float[] tmp = null;

		int gvIdx = getGVIdx(beginGlyphIndex);
		int gi = beginGlyphIndex - off[gvIdx];
		int i = 0;
		GVTGlyphVector gv;
		while (numEntries != 0) {
			int len = numEntries;
			if (gi + len > nGlyphs[gvIdx])
				len = nGlyphs[gvIdx] - gi;

			gv = gvs[gvIdx];
			if (i == 0) {
				gv.getGlyphPositions(gi, len, ret);
			} else {
				if ((tmp == null) || (tmp.length < len * 2))
					tmp = new float[len * 2];

				gv.getGlyphPositions(gi, len, tmp);
				System.arraycopy(tmp, 0, ret, i, len * 2);
			}
			gi = 0;
			gvIdx++;
			numEntries -= len;
			i += len * 2;
		}
		return ret;
	}

	/**
	 * Returns the logical bounds of this GlyphVector.
	 */
	@Override
	public Rectangle2D getLogicalBounds() {
		Rectangle2D ret = null;
		for (GVTGlyphVector gv : gvs) {
			Rectangle2D b = gv.getLogicalBounds();
			if (ret == null)
				ret = b;
			// else ret = ret.createUnion(b);
			else
				ret.add(b); // same as union
		}
		return ret;
	}

	/**
	 * Returns a Shape whose interior corresponds to the visual representation of
	 * this GlyphVector.
	 */
	@Override
	public Shape getOutline() {
		GeneralPath ret = null;
		for (GVTGlyphVector gv : gvs) {
			Shape s = gv.getOutline();
			if (ret == null)
				ret = new GeneralPath(s);
			else
				ret.append(s, false);
		}
		return ret;
	}

	/**
	 * Returns a Shape whose interior corresponds to the visual representation of
	 * this GlyphVector, offset to x, y.
	 */
	@Override
	public Shape getOutline(float x, float y) {
		Shape outline = getOutline();
		AffineTransform tr = AffineTransform.getTranslateInstance(x, y);
		outline = tr.createTransformedShape(outline);
		return outline;
	}

	/**
	 * Returns the bounds of this GlyphVector. This includes stroking effects.
	 */
	@Override
	public Rectangle2D getBounds2D(AttributedCharacterIterator aci) {
		Rectangle2D ret = null;
		int begin = aci.getBeginIndex();
		for (GVTGlyphVector gv : gvs) {
			int end = gv.getCharacterCount(0, gv.getNumGlyphs()) + 1;
			Rectangle2D b = gv.getBounds2D(new AttributedCharacterSpanIterator(aci, begin, end));
			if (ret == null)
				ret = b;
			// else ret = ret.createUnion(b);
			else
				ret.add(b);
			begin = end;
		}
		return ret;
	}

	/**
	 * Returns the geometric bounds of this GlyphVector. The geometric bounds is the
	 * bounds of the geometry of the glyph vector, disregarding stroking.
	 */
	@Override
	public Rectangle2D getGeometricBounds() {
		Rectangle2D ret = null;
		for (GVTGlyphVector gv : gvs) {
			Rectangle2D b = gv.getGeometricBounds();
			if (ret == null)
				ret = b;
			// else ret = ret.createUnion(b);
			else
				ret.add(b);
		}
		return ret;
	}

	@Override
	public void performDefaultLayout() {
		for (GVTGlyphVector gv : gvs) {
			gv.performDefaultLayout();
		}
	}

	/**
	 * Returns the number of chars represented by the glyphs within the specified
	 * range.
	 *
	 * @param startGlyphIndex The index of the first glyph in the range.
	 * @param endGlyphIndex   The index of the last glyph in the range.
	 * @return The number of chars.
	 */
	@Override
	public int getCharacterCount(int startGlyphIndex, int endGlyphIndex) {
		int idx1 = getGVIdx(startGlyphIndex);
		int idx2 = getGVIdx(endGlyphIndex);
		int ret = 0;
		for (int idx = idx1; idx <= idx2; idx++) {
			int gi1 = startGlyphIndex - off[idx];
			int gi2 = endGlyphIndex - off[idx];
			if (gi2 >= nGlyphs[idx]) {
				gi2 = nGlyphs[idx] - 1;
			}
			ret += gvs[idx].getCharacterCount(gi1, gi2);
			startGlyphIndex += (gi2 - gi1 + 1);
		}
		return ret;
	}

	@Override
	public boolean isReversed() {
		return false;
	}

	@Override
	public void maybeReverse(boolean mirror) {
	}

	/**
	 * Draws the glyph vector.
	 */
	@Override
	public void draw(Graphics2D g2d, AttributedCharacterIterator aci) {
		int begin = aci.getBeginIndex();
		for (GVTGlyphVector gv : gvs) {
			int end = gv.getCharacterCount(0, gv.getNumGlyphs()) + 1;
			gv.draw(g2d, new AttributedCharacterSpanIterator(aci, begin, end));
			begin = end;
		}
	}

}
