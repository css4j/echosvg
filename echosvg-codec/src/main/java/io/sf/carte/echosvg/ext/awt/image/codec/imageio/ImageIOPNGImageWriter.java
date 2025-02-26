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
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;

import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import io.sf.carte.echosvg.ext.awt.image.codec.impl.CodecUtil;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterParams;
import io.sf.carte.echosvg.ext.awt.image.spi.PNGImageWriterParams;

/**
 * ImageWriter that encodes PNG images using Image I/O.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ImageIOPNGImageWriter extends ImageIOImageWriter {

	private static final String PNG_NATIVE_FORMAT = "javax_imageio_png_1.0";

	/**
	 * Main constructor.
	 */
	public ImageIOPNGImageWriter() {
		super("image/png");
	}

	@Override
	protected void updateColorMetadata(IIOMetadata meta, ImageWriterParams params, ColorSpace colorSpace) {
		if (!meta.isStandardMetadataFormatSupported()) {
			return;
		}

		PNGImageWriterParams pngParams = (PNGImageWriterParams) params;
		/*
		 * If the color space is built-in, set the correct gamma and chromaticities.
		 * Otherwise, embed the profile.
		 */
		IIOMetadataNode root = null;
		if (colorSpace.isCS_sRGB()) {
			// Gamma means we don't want automatic sRGB intent
			if (pngParams == null || (!pngParams.isSRGBIntentSet() && !pngParams.isGammaSet())) {
				root = setSRGBNode(meta, PNGImageWriterParams.INTENT_PERCEPTUAL);
			}
		} else if (colorSpace == ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB)) {
			// Do not embed profile but set chromaticities
			if (pngParams == null || !pngParams.isGammaSet()) {
				root = setGAMANode(meta, 1f);
			}
			if (pngParams == null || !pngParams.isChromaticitySet()) {
				root = setChromaticityNode(meta, CodecUtil.SRGB_CHROMA);
			}
		} else if (colorSpace instanceof ICC_ColorSpace) {
			ICC_Profile profile = ((ICC_ColorSpace) colorSpace).getProfile();
			byte[] comprProfData = compressProfile(profile.getData(), params);
			String profName = profileName(profile);

			root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);
			IIOMetadataNode iCCP = getChildNode(root, "iCCP");
			if (iCCP == null) {
				iCCP = new IIOMetadataNode("iCCP");
				root.appendChild(iCCP);
			}
			iCCP.setAttribute("profileName", profName);
			iCCP.setAttribute("compressionMethod", "deflate");
			iCCP.setUserObject(comprProfData);
		}

		if (root != null) {
			try {
				meta.mergeTree(PNG_NATIVE_FORMAT, root);
			} catch (IIOInvalidTreeException e) {
				throw new RuntimeException("Cannot update image metadata.", e);
			}
		}
	}

	private static String profileName(ICC_Profile profile) {
		String name = CodecUtil.getProfileName(profile);
		if (name == null) {
			name = profile.getClass().getSimpleName();
			if (name.length() > 79) {
				name = name.substring(0, 79);
			}
		}
		return name;
	}

	private static byte[] compressProfile(byte[] data, ImageWriterParams params) {
		// Determine the compression level
		int lvl = Deflater.DEFAULT_COMPRESSION;
		if (params != null) {
			Integer level = params.getCompressionLevel();
			if (level != null) {
				lvl = level.intValue();
				if (lvl < -1) {
					lvl = Deflater.DEFAULT_COMPRESSION;
				} else if (lvl > Deflater.BEST_COMPRESSION) {
					lvl = Deflater.BEST_COMPRESSION;
				}
			}
		}
		// ByteArrayOutputStream for the compressed output
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length + 16);
		// Compress profile
		// Buffer size should be at least 6
		byte[] buffer = new byte[Math.min(data.length + 5, 1024)];
		Deflater defl = new Deflater(lvl);
		defl.setInput(data);
		defl.finish();
		do {
			int cmprLen = defl.deflate(buffer);
			out.write(buffer, 0, cmprLen);
		} while (!defl.finished());
		defl.end();

		return out.toByteArray();
	}

	@Override
	protected ImageWriteParam getDefaultWriteParam(ImageWriter iiowriter, RenderedImage image,
			ImageWriterParams params) {
		ImageWriteParam param = iiowriter.getDefaultWriteParam();

		if (params != null) {
			/*
			 * Compression
			 */
			String comprMethod = params.getCompressionMethod();
			Integer level = params.getCompressionLevel();
			int lvl;
			if (level != null && (lvl = level.intValue()) >= 0 && lvl < 10) {
				try {
					param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					param.setCompressionType(comprMethod != null ? comprMethod : "Deflate");
					// ImageIO sets the compression level from a quality in the 0-1 range...
					float quality = 1f - lvl / 9f;
					param.setCompressionQuality(quality);
				} catch (UnsupportedOperationException e) {
					// The inability to set a compression level isn't a critical failure
					e.printStackTrace();
				}
			} else if (comprMethod != null) {
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionType(comprMethod);
			}
		}

		return param;
	}

	@Override
	protected IIOMetadata updateMetadata(IIOMetadata meta, ImageWriterParams params) {
		meta = super.updateMetadata(meta, params);

		if (meta.isStandardMetadataFormatSupported() && params instanceof PNGImageWriterParams) {
			IIOMetadataNode root = null;

			PNGImageWriterParams pngParams = (PNGImageWriterParams) params;

			boolean needsMerge = false;

			if (pngParams.isSRGBIntentSet()) {
				root = setSRGBNode(meta, pngParams.getSRGBIntent());

				needsMerge = true;
			} else {
				if (pngParams.isChromaticitySet()) {

					float[] chroma = pngParams.getChromaticity();

					root = setChromaticityNode(meta, chroma);

					needsMerge = true;
				}

				if (pngParams.isGammaSet()) {
					float g = pngParams.getGamma();

					root = setGAMANode(meta, g);

					needsMerge = true;
				}
			}

			if (pngParams.isTextSet()) {
				String[] text = pngParams.getText();

				if (text.length % 2 > 0) {
					throw new RuntimeException("Wrong tEXt data: array must have length multiple of 2.");
				}

				root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);

				IIOMetadataNode tEXt = getChildNode(root, "tEXt");
				if (tEXt == null) {
					tEXt = new IIOMetadataNode("tEXt");
					root.appendChild(tEXt);
				}

				for (int i = 0; i < text.length; i += 2) {
					IIOMetadataNode tEXtEntry = new IIOMetadataNode("tEXtEntry");
					tEXt.appendChild(tEXtEntry);
					tEXtEntry.setAttribute("keyword", text[i]);
					tEXtEntry.setAttribute("value", text[i + 1]);
				}

				needsMerge = true;
			}

			if (pngParams.isInternationalTextSet()) {
				String[] text = pngParams.getInternationalText();

				if (text.length % 4 > 0) {
					throw new RuntimeException("Wrong iTXt data: array must have length multiple of 4.");
				}

				if (root == null) {
					root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);
				}

				IIOMetadataNode iTXt = getChildNode(root, "iTXt");
				if (iTXt == null) {
					iTXt = new IIOMetadataNode("iTXt");
					root.appendChild(iTXt);
				}

				for (int i = 0; i < text.length; i += 4) {
					IIOMetadataNode entry = new IIOMetadataNode("iTXtEntry");
					iTXt.appendChild(entry);
					entry.setAttribute("keyword", text[i]);
					entry.setAttribute("compressionFlag", "TRUE");
					entry.setAttribute("compressionMethod", "1");
					entry.setAttribute("languageTag", text[i + 1]);
					entry.setAttribute("translatedKeyword", text[i + 2]);
					entry.setAttribute("text", text[i + 3]);
				}

				needsMerge = true;
			}

			if (pngParams.isCompressedTextSet()) {
				String[] text = pngParams.getCompressedText();

				if (text.length % 2 > 0) {
					throw new RuntimeException("Wrong zTXt data: array must have length multiple of 2.");
				}

				if (root == null) {
					root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);
				}

				IIOMetadataNode zTXt = getChildNode(root, "zTXt");
				if (zTXt == null) {
					zTXt = new IIOMetadataNode("zTXt");
					root.appendChild(zTXt);
				}

				for (int i = 0; i < text.length; i += 2) {
					IIOMetadataNode entry = new IIOMetadataNode("zTXtEntry");
					zTXt.appendChild(entry);
					entry.setAttribute("keyword", text[i]);
					entry.setAttribute("compressionMethod", "deflate");
					entry.setAttribute("text", text[i + 1]);
				}

				needsMerge = true;
			}

			if (needsMerge) {
				try {
					meta.mergeTree(PNG_NATIVE_FORMAT, root);
				} catch (IIOInvalidTreeException e) {
					throw new RuntimeException("Cannot update image metadata.", e);
				}
			}
		}

		return meta;
	}

	private static IIOMetadataNode setSRGBNode(IIOMetadata meta, int srgbIntent) {
		String rIntent;
		switch (srgbIntent) {
		case PNGImageWriterParams.INTENT_PERCEPTUAL:
		default:
			rIntent = "Perceptual";
			break;
		case PNGImageWriterParams.INTENT_RELATIVE:
			rIntent = "Relative colorimetric";
			break;
		case PNGImageWriterParams.INTENT_SATURATION:
			rIntent = "Saturation";
			break;
		case PNGImageWriterParams.INTENT_ABSOLUTE:
			rIntent = "Absolute colorimetric";
			break;
		}

		IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);

		IIOMetadataNode sRGB = getChildNode(root, "sRGB");
		if (sRGB == null) {
			sRGB = new IIOMetadataNode("sRGB");
			root.appendChild(sRGB);
		}

		sRGB.setAttribute("renderingIntent", rIntent);

		return root;
	}

	private static IIOMetadataNode setGAMANode(IIOMetadata meta, float g) {
		IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);

		IIOMetadataNode gAMA = getChildNode(root, "gAMA");
		if (gAMA == null) {
			gAMA = new IIOMetadataNode("gAMA");
			root.appendChild(gAMA);
		}

		gAMA.setAttribute("value", Integer.toString(Math.round(g * 100000f)));

		return root;
	}

	private static IIOMetadataNode setChromaticityNode(IIOMetadata meta, float[] chroma) {
		IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(PNG_NATIVE_FORMAT);

		IIOMetadataNode cHRM = getChildNode(root, "cHRM");
		if (cHRM == null) {
			cHRM = new IIOMetadataNode("cHRM");
			root.appendChild(cHRM);
		}

		cHRM.setAttribute("whitePointX", Integer.toString(Math.round(chroma[0] * 100000f)));

		cHRM.setAttribute("whitePointY", Integer.toString(Math.round(chroma[1] * 100000f)));

		cHRM.setAttribute("redX", Integer.toString(Math.round(chroma[2] * 100000f)));

		cHRM.setAttribute("redY", Integer.toString(Math.round(chroma[3] * 100000f)));

		cHRM.setAttribute("greenX", Integer.toString(Math.round(chroma[4] * 100000f)));

		cHRM.setAttribute("greenY", Integer.toString(Math.round(chroma[5] * 100000f)));

		cHRM.setAttribute("blueX", Integer.toString(Math.round(chroma[6] * 100000f)));

		cHRM.setAttribute("blueY", Integer.toString(Math.round(chroma[7] * 100000f)));

		return root;
	}

}
