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
package io.sf.carte.echosvg.ext.awt.image.spi;

/**
 * Parameters for the PNG encoder which is accessed through the ImageWriter
 * interface.
 *
 * @version $Id$
 */
public class PNGImageWriterParams extends ImageWriterParams {

	/** Constant for use with the sRGB chunk. */
	public static final int INTENT_PERCEPTUAL = 0;

	/** Constant for use with the sRGB chunk. */
	public static final int INTENT_RELATIVE = 1;

	/** Constant for use with the sRGB chunk. */
	public static final int INTENT_SATURATION = 2;

	/** Constant for use with the sRGB chunk. */
	public static final int INTENT_ABSOLUTE = 3;

	/**
	 * Default constructor.
	 */
	public PNGImageWriterParams() {
		super();
	}

	// cHRM chunk

	private float[] chromaticity = null;

	/**
	 * Sets the white point and primary chromaticities in CIE (x, y) space.
	 *
	 * <p>
	 * The <code>chromaticity</code> parameter must be a <code>float</code> array
	 * of length 8 containing the white point X and Y, red X and Y, green X and Y,
	 * and blue X and Y values in order.
	 * </p>
	 * <p>
	 * The 'cHRM' chunk will encode this information.
	 * </p>
	 */
	public void setChromaticity(float[] chromaticity) {
		if (chromaticity.length != 8) {
			throw new IllegalArgumentException();
		}
		this.chromaticity = chromaticity.clone();
	}

	/**
	 * A convenience method that calls the array version.
	 */
	public void setChromaticity(float whitePointX, float whitePointY, float redX, float redY, float greenX,
			float greenY, float blueX, float blueY) {
		float[] chroma = new float[8];
		chroma[0] = whitePointX;
		chroma[1] = whitePointY;
		chroma[2] = redX;
		chroma[3] = redY;
		chroma[4] = greenX;
		chroma[5] = greenY;
		chroma[6] = blueX;
		chroma[7] = blueY;
		this.chromaticity = chroma;
	}

	/**
	 * Returns the white point and primary chromaticities in CIE (x, y) space.
	 *
	 * <p>
	 * See the documentation for the <code>setChromaticity</code> method for the
	 * format of the returned data.
	 * </p>
	 * <p>
	 * If the chromaticity has not previously been set, or has been unset, an
	 * <code>IllegalStateException</code> will be thrown.
	 * </p>
	 * @throws IllegalStateException if the chromaticity is not set.
	 */
	public float[] getChromaticity() {
		if (chromaticity == null) {
			throw new IllegalStateException("chromaticity not set");
		}
		return chromaticity.clone();
	}

	/**
	 * Returns true if a 'cHRM' chunk will be output.
	 */
	public boolean isChromaticitySet() {
		return chromaticity != null;
	}

	// gAMA chunk

	private float gamma;
	private boolean gammaSet = false;

	/**
	 * Sets the file gamma value for the image.
	 *
	 * <p>
	 * The 'gAMA' chunk will encode this information.
	 * </p>
	 */
	public void setGamma(float gamma) {
		this.gamma = gamma;
		gammaSet = true;
	}

	/**
	 * Returns the file gamma value for the image.
	 *
	 * <p>
	 * If the file gamma has not previously been set, or has been unset, an
	 * <code>IllegalStateException</code> will be thrown.
	 * </p>
	 *
	 * @throws IllegalStateException if the gamma is not set.
	 */
	public float getGamma() {
		if (!gammaSet) {
			throw new IllegalStateException("Gamma not set.");
		}
		return gamma;
	}

	/**
	 * Suppresses the 'gAMA' chunk from being output.
	 */
	public void unsetGamma() {
		gammaSet = false;
	}

	/**
	 * Returns true if a 'gAMA' chunk will be output.
	 */
	public boolean isGammaSet() {
		return gammaSet;
	}

	// sRGB chunk

	private int sRGBIntent = -1;

	/**
	 * Sets the sRGB rendering intent to be stored with this image.
	 *
	 * <p>
	 * The 'sRGB' chunk will encode this information.
	 * </p>
	 * 
	 * @param sRGBIntent the intent. The legal values are 0 = Perceptual, 1 =
	 *                   Relative Colorimetric, 2 = Saturation, and 3 = Absolute
	 *                   Colorimetric. Refer to the PNG specification for
	 *                   information on these values.
	 * @throws IllegalArgumentException if the intent is not known.
	 */
	public void setSRGBIntent(int sRGBIntent) throws IllegalArgumentException {
		if (sRGBIntent < 0 || sRGBIntent > 3) {
			throw new IllegalArgumentException("Unsupported sRGB intent: " + sRGBIntent);
		}
		this.sRGBIntent = sRGBIntent;
	}

	/**
	 * Returns the sRGB rendering intent to be stored with this image.
	 *
	 * <p>
	 * If the sRGB intent has not previously been set, or has been unset, an
	 * <code>IllegalStateException</code> will be thrown.
	 *
	 * @throws IllegalStateException if the sRGB intent is not set.
	 */
	public int getSRGBIntent() throws IllegalStateException {
		if (sRGBIntent < 0) {
			throw new IllegalStateException();
		}
		return sRGBIntent;
	}

	/**
	 * Suppresses the 'sRGB' chunk from being output.
	 */
	public void unsetSRGBIntent() {
		sRGBIntent = -1;
	}

	/**
	 * Returns true if an 'sRGB' chunk will be output.
	 */
	public boolean isSRGBIntentSet() {
		return sRGBIntent >= 0;
	}

	// tEXt chunk

	private String[] text = null;
	private boolean textSet = false;

	/**
	 * Sets the textual data to be stored in uncompressed form with this image. The
	 * data is passed to this method as an array of <code>String</code>s.
	 *
	 * <p>
	 * The 'tEXt' chunk will encode this information.
	 * </p>
	 */
	public void setText(String[] text) {
		this.text = text;
		textSet = true;
	}

	/**
	 * Returns the text strings to be stored in uncompressed form with this image as
	 * an array of <code>String</code>s.
	 *
	 * <p>
	 * If the text strings have not previously been set, or have been unset, an
	 * <code>IllegalStateException</code> will be thrown.
	 *
	 * @throws IllegalStateException if the text strings are not set.
	 */
	public String[] getText() {
		if (!textSet) {
			throw new IllegalStateException(Messages.getString("PNG.tEXt.not.set"));
		}
		return text;
	}

	/**
	 * Suppresses the 'tEXt' chunk from being output.
	 */
	public void unsetText() {
		text = null;
		textSet = false;
	}

	/**
	 * Returns true if a 'tEXt' chunk will be output.
	 */
	public boolean isTextSet() {
		return textSet;
	}

	// iTXt chunk

	private String[] iText = null;
	private boolean iTextSet = false;

	/**
	 * Sets an array of <code>String</code>s containing, in this order, the keyword,
	 * the language tag, the translated keyword and the internationalized text.
	 * <p>
	 * Therefore, the array must be multiple of 4.
	 * </p>
	 * <p>
	 * The 'iTXt' chunk will encode this information.
	 * </p>
	 */
	public void setInternationalText(String[] text) {
		this.iText = text;
		iTextSet = true;
	}

	/**
	 * Returns the internationalized text strings to be stored in compressed form
	 * with this image as an array of <code>String</code>s.
	 *
	 * <p>
	 * If the internationalized text strings have not previously been set, or have
	 * been unset, an <code>IllegalStateException</code> will be thrown.
	 *
	 * @throws IllegalStateException if the internationalized text strings are not
	 *                               set.
	 */
	public String[] getInternationalText() {
		if (!iTextSet) {
			throw new IllegalStateException(Messages.getString("PNG.iTXt.not.set"));
		}
		return iText;
	}

	/**
	 * Suppresses the 'iTXt' chunk from being output.
	 */
	public void unsetInternationalText() {
		iText = null;
		iTextSet = false;
	}

	/**
	 * Returns true if an 'iTXt' chunk will be output.
	 */
	public boolean isInternationalTextSet() {
		return iTextSet;
	}

	// zTXt chunk

	private String[] zText = null;
	private boolean zTextSet = false;

	/**
	 * Sets the text strings to be stored in compressed form with this image. The
	 * data is passed to this method as an array of <code>String</code>s.
	 *
	 * <p>
	 * The 'zTXt' chunk will encode this information.
	 */
	public void setCompressedText(String[] text) {
		this.zText = text;
		zTextSet = true;
	}

	/**
	 * Returns the text strings to be stored in compressed form with this image as
	 * an array of <code>String</code>s.
	 *
	 * <p>
	 * If the compressed text strings have not previously been set, or have been
	 * unset, an <code>IllegalStateException</code> will be thrown.
	 *
	 * @throws IllegalStateException if the compressed text strings are not set.
	 */
	public String[] getCompressedText() {
		if (!zTextSet) {
			throw new IllegalStateException(Messages.getString("PNG.zTXt.not.set"));
		}
		return zText;
	}

	/**
	 * Suppresses the 'zTXt' chunk from being output.
	 */
	public void unsetCompressedText() {
		zText = null;
		zTextSet = false;
	}

	/**
	 * Returns true if a 'zTXt' chunk will be output.
	 */
	public boolean isCompressedTextSet() {
		return zTextSet;
	}

}
