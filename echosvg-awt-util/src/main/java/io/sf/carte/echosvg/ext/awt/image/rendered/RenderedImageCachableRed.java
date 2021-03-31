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
package io.sf.carte.echosvg.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Vector;

/**
 * This implements CachableRed around a RenderedImage. You can use this to wrap
 * a RenderedImage that you want to appear as a CachableRed. It essentially
 * ignores the dependency and dirty region methods.
 *
 * @author <a href="mailto:Thomas.DeWeeese@Kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RenderedImageCachableRed implements CachableRed {

	public static CachableRed wrap(RenderedImage ri) {
		if (ri instanceof CachableRed)
			return (CachableRed) ri;
		if (ri instanceof BufferedImage)
			return new BufferedImageCachableRed((BufferedImage) ri);
		return new RenderedImageCachableRed(ri);
	}

	private RenderedImage src;
	private Vector<RenderedImage> srcs = new Vector<>(0);

	public RenderedImageCachableRed(RenderedImage src) {
		if (src == null) {
			throw new IllegalArgumentException();
		}
		this.src = src;
	}

	@Override
	public Vector<RenderedImage> getSources() {
		return srcs; // should always be empty...
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getMinX(), // could we cache the rectangle??
				getMinY(), getWidth(), getHeight());
	}

	@Override
	public int getMinX() {
		return src.getMinX();
	}

	@Override
	public int getMinY() {
		return src.getMinY();
	}

	@Override
	public int getWidth() {
		return src.getWidth();
	}

	@Override
	public int getHeight() {
		return src.getHeight();
	}

	@Override
	public ColorModel getColorModel() {
		return src.getColorModel();
	}

	@Override
	public SampleModel getSampleModel() {
		return src.getSampleModel();
	}

	@Override
	public int getMinTileX() {
		return src.getMinTileX();
	}

	@Override
	public int getMinTileY() {
		return src.getMinTileY();
	}

	@Override
	public int getNumXTiles() {
		return src.getNumXTiles();
	}

	@Override
	public int getNumYTiles() {
		return src.getNumYTiles();
	}

	@Override
	public int getTileGridXOffset() {
		return src.getTileGridXOffset();
	}

	@Override
	public int getTileGridYOffset() {
		return src.getTileGridYOffset();
	}

	@Override
	public int getTileWidth() {
		return src.getTileWidth();
	}

	@Override
	public int getTileHeight() {
		return src.getTileHeight();
	}

	@Override
	public Object getProperty(String name) {
		return src.getProperty(name);
	}

	@Override
	public String[] getPropertyNames() {
		return src.getPropertyNames();
	}

	@Override
	public Raster getTile(int tileX, int tileY) {
		return src.getTile(tileX, tileY);
	}

	@Override
	public WritableRaster copyData(WritableRaster raster) {
		return src.copyData(raster);
	}

	@Override
	public Raster getData() {
		return src.getData();
	}

	@Override
	public Raster getData(Rectangle rect) {
		return src.getData(rect);
	}

	@Override
	public Shape getDependencyRegion(int srcIndex, Rectangle outputRgn) {
		throw new IndexOutOfBoundsException("Nonexistant source requested.");
	}

	@Override
	public Shape getDirtyRegion(int srcIndex, Rectangle inputRgn) {
		throw new IndexOutOfBoundsException("Nonexistant source requested.");
	}
}
