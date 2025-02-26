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
package io.sf.carte.echosvg.ext.awt.image.codec.imageio;

import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;

/**
 * ImageWriter implementation that uses Image I/O to write images.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImageIOImageWriter implements ImageWriter, IIOWriteWarningListener {

	private String targetMIME;

	/**
	 * Main constructor.
	 * 
	 * @param mime the MIME type of the image format
	 */
	public ImageIOImageWriter(String mime) {
		this.targetMIME = mime;
	}

	@Override
	public void writeImage(RenderedImage image, OutputStream out) throws IOException {
		writeImage(image, out, null);
	}

	@Override
	public void writeImage(RenderedImage image, OutputStream out, ImageWriterParams params) throws IOException {
		Iterator<javax.imageio.ImageWriter> iter = ImageIO.getImageWritersByMIMEType(getMIMEType());
		javax.imageio.ImageWriter iiowriter = null;
		try {
			iiowriter = iter.next();
			if (iiowriter != null) {
				iiowriter.addIIOWriteWarningListener(this);

				try (ImageOutputStream imgout = ImageIO.createImageOutputStream(out)) {
					ImageWriteParam iwParam = getDefaultWriteParam(iiowriter, image, params);

					ImageTypeSpecifier type;
					if (iwParam.getDestinationType() != null) {
						type = iwParam.getDestinationType();
					} else {
						type = ImageTypeSpecifier.createFromRenderedImage(image);
					}

					// Handle metadata
					IIOMetadata meta = iiowriter.getDefaultImageMetadata(type, iwParam);
					// meta might be null for some JAI codecs as they don't support metadata
					if (meta != null) {
						updateColorMetadata(meta, params, image.getColorModel().getColorSpace());
						if (params != null) {
							meta = updateMetadata(meta, params);
						}
					}

					// Write image
					iiowriter.setOutput(imgout);
					IIOImage iioimg = new IIOImage(image, null, meta);
					iiowriter.write(null, iioimg, iwParam);
				}
			} else {
				throw new UnsupportedOperationException(
						"No ImageIO codec for writing " + getMIMEType() + " is available!");
			}
		} finally {
			if (iiowriter != null) {
				iiowriter.dispose();
			}
		}
	}

	/**
	 * Returns the default write parameters for encoding the image.
	 * 
	 * @param iiowriter The IIO ImageWriter that will be used
	 * @param image     the image to be encoded
	 * @param params    the parameters for this writer instance
	 * @return the IIO ImageWriteParam instance
	 */
	protected ImageWriteParam getDefaultWriteParam(javax.imageio.ImageWriter iiowriter, RenderedImage image,
			ImageWriterParams params) {
		ImageWriteParam param = iiowriter.getDefaultWriteParam();
		if ((params != null) && (params.getCompressionMethod() != null)) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionType(params.getCompressionMethod());
		}
		return param;
	}

	/**
	 * Updates the metadata information based on the color space and the parameters
	 * to this writer.
	 * 
	 * @param meta       the metadata
	 * @param params     the parameters for this writer instance, or {@code null} if
	 *                   none.
	 * @param colorSpace the color space
	 */
	protected void updateColorMetadata(IIOMetadata meta, ImageWriterParams params, ColorSpace colorSpace) {
	}

	/**
	 * Updates the metadata information based on the parameters to this writer.
	 * 
	 * @param meta   the metadata
	 * @param params the parameters
	 * @return the updated metadata
	 */
	protected IIOMetadata updateMetadata(IIOMetadata meta, ImageWriterParams params) {
		final String stdmeta = "javax_imageio_1.0";
		if (meta.isStandardMetadataFormatSupported()) {
			IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(stdmeta);
			IIOMetadataNode dim = getChildNode(root, "Dimension");
			IIOMetadataNode child;
			if (params.getResolution() != null) {
				child = getChildNode(dim, "HorizontalPixelSize");
				if (child == null) {
					child = new IIOMetadataNode("HorizontalPixelSize");
					dim.appendChild(child);
				}
				child.setAttribute("value", Double.toString(params.getResolution().doubleValue() / 25.4));
				child = getChildNode(dim, "VerticalPixelSize");
				if (child == null) {
					child = new IIOMetadataNode("VerticalPixelSize");
					dim.appendChild(child);
				}
				child.setAttribute("value", Double.toString(params.getResolution().doubleValue() / 25.4));

				try {
					meta.mergeTree(stdmeta, root);
				} catch (IIOInvalidTreeException e) {
					throw new RuntimeException("Cannot update image metadata: " + e.getMessage());
				}
			}
		}
		return meta;
	}

	/**
	 * Returns a specific metadata child node
	 * 
	 * @param n    the base node
	 * @param name the name of the child
	 * @return the requested child node
	 */
	protected static IIOMetadataNode getChildNode(Node n, String name) {
		NodeList nodes = n.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node child = nodes.item(i);
			if (name.equals(child.getNodeName())) {
				return (IIOMetadataNode) child;
			}
		}
		return null;
	}

	/**
	 * @see ImageWriter#getMIMEType()
	 */
	@Override
	public String getMIMEType() {
		return this.targetMIME;
	}

	/**
	 * @see javax.imageio.event.IIOWriteWarningListener#warningOccurred(javax.imageio.ImageWriter,
	 *      int, java.lang.String)
	 */
	@Override
	public void warningOccurred(javax.imageio.ImageWriter source, int imageIndex, String warning) {
		System.err.println("Problem while writing image using ImageI/O: " + warning);
	}
}
