/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package io.sf.carte.echosvg.transcoder.wmf.tosvg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * This class provides generic methods that must be used by a particular
 * WMFPainter.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AbstractWMFPainter {

	public static final String WMF_FILE_EXTENSION = ".wmf";
	protected WMFFont wmfFont = null;
	protected int currentHorizAlign = 0;
	protected int currentVertAlign = 0;

	public static final int PEN = 1;
	public static final int BRUSH = 2;
	public static final int FONT = 3;
	public static final int NULL_PEN = 4;
	public static final int NULL_BRUSH = 5;
	public static final int PALETTE = 6;
	public static final int OBJ_BITMAP = 7;
	public static final int OBJ_REGION = 8;

	protected WMFRecordStore currentStore;
	protected transient boolean bReadingWMF = true;
	protected transient BufferedInputStream bufStream = null;

	/**
	 * Return the image associated with a bitmap in a Metafile. 24 bits and 8 bits
	 * bitmaps are handled.
	 * 
	 * @param bit    the bitmap byte array
	 * @param width  the bitmap assumed width
	 * @param height the bitmap assumed height
	 * @return the Image associated with the bitmap (null if the dimensions detected
	 *         in the header are not consistent with the assumed dimensions)
	 */
	protected BufferedImage getImage(byte[] bit, int width, int height) {
		// get the header of the bitmap, first the width and height
		int _width = ((bit[7] & 0x00ff) << 24) | ((bit[6] & 0x00ff) << 16) | ((bit[5] & 0x00ff) << 8) | bit[4] & 0x00ff;
		int _height = ((bit[11] & 0x00ff) << 24) | ((bit[10] & 0x00ff) << 16) | ((bit[9] & 0x00ff) << 8)
				| bit[8] & 0x00ff;

		// if width and height of the bitmap are different from advertised, we abort
		if ((width != _width) || (height != _height))
			return null;
		return getImage(bit);
	}

	protected Dimension getImageDimension(byte[] bit) {
		// get the header of the bitmap, first the width and height
		int _width = ((bit[7] & 0x00ff) << 24) | ((bit[6] & 0x00ff) << 16) | ((bit[5] & 0x00ff) << 8) | bit[4] & 0x00ff;
		int _height = ((bit[11] & 0x00ff) << 24) | ((bit[10] & 0x00ff) << 16) | ((bit[9] & 0x00ff) << 8)
				| bit[8] & 0x00ff;
		return new Dimension(_width, _height);
	}

	/**
	 * Return the image associated with a bitmap in a Metafile. 24 bits and 8 bits
	 * bitmaps are handled.
	 * 
	 * @param bit the bitmap byte array
	 * @return the Image associated with the bitmap (null if the dimensions detected
	 *         in the header are not consistent with the assumed dimensions)
	 */
	protected BufferedImage getImage(byte[] bit) {
		// get the header of the bitmap, first the width and height
		int _width = ((bit[7] & 0x00ff) << 24) | ((bit[6] & 0x00ff) << 16) | ((bit[5] & 0x00ff) << 8) | bit[4] & 0x00ff;
		int _height = ((bit[11] & 0x00ff) << 24) | ((bit[10] & 0x00ff) << 16) | ((bit[9] & 0x00ff) << 8)
				| bit[8] & 0x00ff;

		// OK, we can safely create the data array now
		int[] bitI = new int[_width * _height];
		BufferedImage img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = img.getRaster();

		// retrieve useful informations in bitmap header
		// size of header
		int _headerSize = ((bit[3] & 0x00ff) << 24) | ((bit[2] & 0x00ff) << 16) | ((bit[1] & 0x00ff) << 8)
				| bit[0] & 0x00ff;
		// number of planes
		int _planes = ((bit[13] & 0x00ff) << 8) | bit[12] & 0x00ff;
		// number of bits per pixel
		int _nbit = ((bit[15] & 0x00ff) << 8) | bit[14] & 0x00ff;
		// compression factor : unused
		// size of the image
		int _size = ((bit[23] & 0x00ff) << 24) | ((bit[22] & 0x00ff) << 16) | ((bit[21] & 0x00ff) << 8)
				| bit[20] & 0x00ff;
		// infer the size of image if it is not given in the file
		if (_size == 0)
			_size = ((((_width * _nbit) + 31) & ~31) >> 3) * _height;

		// number of used colors
		int _clrused = ((bit[35] & 0x00ff) << 24) | ((bit[34] & 0x00ff) << 16) | ((bit[33] & 0x00ff) << 8)
				| bit[32] & 0x00ff;

		// 24 bit image
		if (_nbit == 24) {
			// read the scan lines
			int pad = (_size / _height) - _width * 3;
			int offset = _headerSize; // begin to read data after header
			// populate the int array
			for (int j = 0; j < _height; j++) {
				for (int i = 0; i < _width; i++) {
					bitI[_width * (_height - j - 1) + i] = (255 & 0x00ff) << 24 | ((bit[offset + 2] & 0x00ff) << 16)
							| ((bit[offset + 1] & 0x00ff) << 8) | bit[offset] & 0x00ff;
					offset += 3;
				}
				offset += pad;
			}
			// 8 bit image
		} else if (_nbit == 8) {
			// Determine the number of colors
			int nbColors = 0;
			if (_clrused > 0)
				nbColors = _clrused;
			else
				nbColors = (1 & 0x00ff) << 8;
			// Read the palette colors.
			int offset = _headerSize;
			int[] palette = new int[nbColors];
			for (int i = 0; i < nbColors; i++) {
				palette[i] = (255 & 0x00ff) << 24 | ((bit[offset + 2] & 0x00ff) << 16)
						| ((bit[offset + 1] & 0x00ff) << 8) | bit[offset] & 0x00ff;
				offset += 4;
			}

			// populate the int array
			/*
			 * need to recalculate size because the offset used for palette must be
			 * substracted to overall size, else we will go after the end of the byte
			 * array...
			 */
			_size = bit.length - offset;
			int pad = (_size / _height) - _width;
			for (int j = 0; j < _height; j++) {
				for (int i = 0; i < _width; i++) {
					bitI[_width * (_height - j - 1) + i] = palette[(bit[offset] & 0x00ff)];
					offset++;
				}
				offset += pad;
			}
			// black and white image
		} else if (_nbit == 1) {
			// 2 colors only (black and white image)
			int nbColors = 2;
			// Read the palette colors.
			int offset = _headerSize;
			int[] palette = new int[nbColors];
			for (int i = 0; i < nbColors; i++) {
				palette[i] = (255 & 0x00ff) << 24 | ((bit[offset + 2] & 0x00ff) << 16)
						| ((bit[offset + 1] & 0x00ff) << 8) | bit[offset] & 0x00ff;
				offset += 4;
			}

			// populate the int array : each pixel corresponds to a bit in the byte array
			int pos = 7;
			byte currentByte = bit[offset];
			// padded to long words
			int pad = (_size / _height) - _width / 8;
			for (int j = 0; j < _height; j++) {
				for (int i = 0; i < _width; i++) {
					if ((currentByte & (1 << pos)) != 0)
						bitI[_width * (_height - j - 1) + i] = palette[1];
					else
						bitI[_width * (_height - j - 1) + i] = palette[0];
					pos--;
					if (pos == -1) {
						pos = 7;
						offset++;
						if (offset < bit.length)
							currentByte = bit[offset];
					}
				}
				offset += pad;
				pos = 7;
				if (offset < bit.length)
					currentByte = bit[offset];
			}
		}
		raster.setDataElements(0, 0, _width, _height, bitI);
		return img;
	}

	/**
	 * Create an AttributedCharacterIterator with the current definition of the WMF
	 * Font, and the input String.
	 */
	protected AttributedCharacterIterator getCharacterIterator(Graphics2D g2d, String sr, WMFFont wmffont) {
		return getAttributedString(g2d, sr, wmffont).getIterator();
	}

	/**
	 * Create an AttributedCharacterIterator with the current definition of the WMF
	 * Font, and the input String.
	 */
	protected AttributedCharacterIterator getCharacterIterator(Graphics2D g2d, String sr, WMFFont wmffont, int align) {
		AttributedString ats = getAttributedString(g2d, sr, wmffont);

		return ats.getIterator();
	}

	protected AttributedString getAttributedString(Graphics2D g2d, String sr, WMFFont wmffont) {
		AttributedString ats = new AttributedString(sr);
		Font font = g2d.getFont();
		ats.addAttribute(TextAttribute.SIZE, font.getSize2D());
		ats.addAttribute(TextAttribute.FONT, font);
		if (wmfFont.underline != 0)
			ats.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		if (wmfFont.italic != 0)
			ats.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		else
			ats.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
		if (wmfFont.weight > 400)
			ats.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		else
			ats.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);

		return ats;
	}

	/**
	 * Sets the WMFRecordStore this WMFPainter should use to render
	 */
	public void setRecordStore(WMFRecordStore currentStore) {
		if (currentStore == null) {
			throw new IllegalArgumentException();
		}

		this.currentStore = currentStore;
	}

	/**
	 * Returns the WMFRecordStore this WMFPainter renders
	 */
	public WMFRecordStore getRecordStore() {
		return currentStore;
	}

	protected int addObject(WMFRecordStore store, int type, Object obj) {
		return currentStore.addObject(type, obj);
	}

	protected int addObjectAt(WMFRecordStore store, int type, Object obj, int idx) {
		return currentStore.addObjectAt(type, obj, idx);
	}
}
