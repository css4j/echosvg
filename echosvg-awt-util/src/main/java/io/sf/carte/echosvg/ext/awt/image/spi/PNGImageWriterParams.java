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

	/**
	 * Default constructor.
	 */
	public PNGImageWriterParams() {
		super();
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
