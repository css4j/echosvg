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
 * Parameters for the encoder which is accessed through the ImageWriter
 * interface.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImageWriterParams {

	private Integer resolution;
	private Float jpegQuality;
	private Boolean jpegForceBaseline;
	private String compressionMethod;
	private Integer compressionLevel;

	/**
	 * Default constructor.
	 */
	public ImageWriterParams() {
		// nop
	}

	/**
	 * @return the image resolution in dpi, or null if undefined
	 */
	public Integer getResolution() {
		return this.resolution;
	}

	/**
	 * @return the quality value for encoding a JPEG image (0.0-1.0), or null if
	 *         undefined
	 */
	public Float getJPEGQuality() {
		return this.jpegQuality;
	}

	/**
	 * Get the level to be used for lossless compression. Only applies to lossless
	 * formats.
	 * 
	 * @return the compression level, or {@code null} if the default should be
	 *         applied.
	 */
	public Integer getCompressionLevel() {
		return compressionLevel;
	}

	/**
	 * @return true if the baseline quantization table is forced, or null if
	 *         undefined.
	 */
	public Boolean getJPEGForceBaseline() {
		return this.jpegForceBaseline;
	}

	/** @return the compression method for encoding the image */
	public String getCompressionMethod() {
		return this.compressionMethod;
	}

	/**
	 * Sets the target resolution of the bitmap image to be written.
	 * 
	 * @param dpi the resolution in dpi
	 */
	public void setResolution(int dpi) {
		this.resolution = dpi;
	}

	/**
	 * Sets the quality setting for encoding JPEG images.
	 * 
	 * @param quality       the quality setting (0.0-1.0)
	 * @param forceBaseline force baseline quantization table
	 */
	public void setJPEGQuality(float quality, boolean forceBaseline) {
		this.jpegQuality = quality;
		this.jpegForceBaseline = forceBaseline ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Sets the level used in lossless compression. Only applies to lossless
	 * formats.
	 * 
	 * @param level the level. The interpretation of the value depends on the
	 *              compression algorithm (for PNG images, {@code Deflate} is used
	 *              and the value must be in the range [0-9]).
	 */
	public void setCompressionLevel(int level) {
		this.compressionLevel = Integer.valueOf(level);
	}

	/**
	 * Set the compression method that shall be used to encode the image.
	 * 
	 * @param method the compression method
	 */
	public void setCompressionMethod(String method) {
		this.compressionMethod = method;
	}

}
