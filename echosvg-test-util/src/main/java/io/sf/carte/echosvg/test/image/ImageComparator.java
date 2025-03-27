/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.sf.carte.echosvg.test.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;

/**
 * Methods for comparing two images.
 * <p>
 * This class allows simple comparisons between two images (with a possible
 * allowance for a certain amount of different pixels), as well as more complex
 * comparisons with an arbitrary number of variants to match. See
 * {@link ImageVariants} for a description of the variants mechanism.
 * </p>
 * <p>
 * This software was written for image comparisons in the context of the EchoSVG
 * project. Usage outside of that context is not supported.
 * </p>
 */
public class ImageComparator {

	/**
	 * The two images match.
	 */
	public static final short MATCH = 0;

	/**
	 * The images have different sizes.
	 */
	public static final short DIFFERENT_SIZES = 1;

	/**
	 * The images have different transparencies.
	 */
	public static final short DIFFERENT_TRANSPARENCIES = 2;

	/**
	 * The images are of different types.
	 */
	public static final short DIFFERENT_TYPES = 3;

	/**
	 * The images have different color spaces.
	 */
	public static final short DIFFERENT_COLOR_SPACES = 4;

	/**
	 * The images have a different number of bands.
	 */
	public static final short DIFFERENT_BANDS = 5;

	/**
	 * The images use different buffer data types.
	 */
	public static final short DIFFERENT_DATA_TYPES = 6;

	/**
	 * The images have too many below-threshold different pixels.
	 */
	public static final short DIFFERENT_PIXELS_BELOW_THRESHOLD = 10;

	/**
	 * The images have too many over-threshold different pixels.
	 */
	public static final short DIFFERENT_PIXELS_OVER_THRESHOLD = 11;

	/**
	 * A variant comparison was executed but no variants were found.
	 * <p>
	 * This code is not considered an error nor an unexpected situation.
	 * </p>
	 */
	public static final short NO_VARIANTS = 20;

	/**
	 * At least one of the image variants is wrong (wrong size, etc).
	 */
	public static final short VARIANT_ERROR = 21;

	/**
	 * The factor to multiply each pixel diff when creating a diff-image.
	 */
	private static final int diffPixelFactor = 10;

	private static int minDiffPixelsForNonzeroAllowance = 30;

	/**
	 * Set the bare minimum number of accepted different pixels, to apply when the
	 * allowance is greater than zero but less than 1%, and when it is smaller than
	 * the smallest of the image sides.
	 * <p>
	 * Applies independently to pixels above and below the threshold.
	 * </p>
	 * <p>
	 * This is useful for small images. Default value is 30.
	 * </p>
	 * 
	 * @param minDiffPixelsForNonzeroAllowance a positive integer.
	 */
	public static void setMinDiffPixelsForNonzeroAllowance(int minDiffPixelsForNonzeroAllowance) {
		if (minDiffPixelsForNonzeroAllowance < 0) {
			throw new IllegalArgumentException("Number of pixels must be at least 0.");
		}
		ImageComparator.minDiffPixelsForNonzeroAllowance = minDiffPixelsForNonzeroAllowance;
	}

	/**
	 * Compare two images, allowing a percentage of different pixels according to a
	 * threshold value.
	 * <p>
	 * If the pixels are different but the difference is below a given threshold, a
	 * percentage of up to {@code allowedPercentBelowThreshold} of these pixels is
	 * allowed. If the difference is over the threshold, a percentage of up to
	 * {@code allowedPercentOverThreshold} is allowed as well.
	 * </p>
	 * <p>
	 * If the returned code is greater than {@code MATCH} but smaller than
	 * {@code DIFFERENT_PIXELS_BELOW_THRESHOLD}, the images cannot be compared.
	 * </p>
	 * 
	 * @param ref                          the reference image.
	 * @param can                          the other (candidate) image.
	 * @param pixelThreshold               the threshold to classify the differences
	 *                                     among the pixel values.
	 * @param allowedPercentBelowThreshold the allowed percentage of below-threshold
	 *                                     different pixels.
	 * @param allowedPercentOverThreshold  the allowed percentage of over-threshold
	 *                                     different pixels.
	 * @return
	 *         <ul>
	 *         <li>{@code MATCH} if the images match.</li>
	 *         <li>{@code DIFFERENT_SIZES} if the sizes are different.</li>
	 *         <li>{@code DIFFERENT_TRANSPARENCIES} if the transparencies do not
	 *         match.</li>
	 *         <li>{@code DIFFERENT_TYPES} if the image types are not the same.</li>
	 *         <li>{@code DIFFERENT_COLOR_SPACES} if the images have different color
	 *         spaces.</li>
	 *         <li>{@code DIFFERENT_PIXELS_BELOW_THRESHOLD} if the images have too
	 *         many below-threshold different pixels .</li>
	 *         <li>{@code DIFFERENT_PIXELS_OVER_THRESHOLD} if the images have too
	 *         many over-threshold different pixels.</li>
	 *         </ul>
	 */
	public static short compareImages(BufferedImage ref, BufferedImage can, int pixelThreshold,
			float allowedPercentBelowThreshold, float allowedPercentOverThreshold) {
		// Obtain reference image size
		final int w = ref.getWidth();
		final int h = ref.getHeight();

		// Compare width and height
		if (w != can.getWidth() || h != can.getHeight()) {
			return DIFFERENT_SIZES;
		}

		// Compare transparency
		if (ref.getTransparency() != can.getTransparency()) {
			return DIFFERENT_TRANSPARENCIES;
		}

		// Compare type
		if (ref.getType() != can.getType()) {
			if (ref.getType() != BufferedImage.TYPE_CUSTOM && can.getType() != BufferedImage.TYPE_CUSTOM) {
				return DIFFERENT_TYPES;
			}
		}

		if (ref.getType() == BufferedImage.TYPE_CUSTOM) {
			ColorSpace refColorSpace = ref.getColorModel().getColorSpace();
			ColorSpace canColorSpace = can.getColorModel().getColorSpace();
			// Compare color spaces
			if (refColorSpace.getType() != canColorSpace.getType()
					|| !isSameColorSpace(refColorSpace, canColorSpace)) {
				return DIFFERENT_COLOR_SPACES;
			}
		}

		final int numBands = ref.getSampleModel().getNumBands();

		if (numBands != can.getSampleModel().getNumBands()) {
			return DIFFERENT_BANDS;
		}

		if (ref.getSampleModel().getDataType() != can.getSampleModel().getDataType()) {
			return DIFFERENT_DATA_TYPES;
		}

		final float numpxFrac = w * h * numBands * 0.01f;
		final int maxDiffPx = computeMaxDiffPixels(numpxFrac, allowedPercentOverThreshold, w, h);
		final int maxDiffPxBelow = computeMaxDiffPixels(numpxFrac, allowedPercentBelowThreshold, w,
				h);

		WritableRaster refWR = ref.getRaster();
		WritableRaster canWR = can.getRaster();

		// Pre-multiply alpha channel(s), if needed
		final boolean refPre = ref.isAlphaPremultiplied();
		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, true);
			ref = new BufferedImage(cm, refWR, true, null);
		}
		final boolean canPre = can.isAlphaPremultiplied();
		if (!canPre) {
			ColorModel cm = can.getColorModel();
			cm = GraphicsUtil.coerceData(canWR, cm, true);
			can = new BufferedImage(cm, canWR, true, null);
		}

		/*
		 * Compare pixels
		 */
		short result = MATCH;
		int countDiffPx = 0, countDiffPxBelow = 0;
		int[] refPix = null;
		int[] canPix = null;

		topLoop: for (int y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			canPix = canWR.getPixels(0, y, w, 1, canPix);
			for (int i = 0; i < refPix.length; i++) {
				int diff = canPix[i] - refPix[i];
				if (diff != 0) {
					// Different pixel
					if (Math.abs(diff) <= pixelThreshold) {
						countDiffPxBelow++;
						if (countDiffPxBelow > maxDiffPxBelow) {
							result = DIFFERENT_PIXELS_BELOW_THRESHOLD;
							break topLoop;
						}
					} else {
						countDiffPx++;
						if (countDiffPx > maxDiffPx) {
							result = DIFFERENT_PIXELS_OVER_THRESHOLD;
							break topLoop;
						}
					}
				}
			}
		}

		// De-multiply, if appropriate
		if (!canPre) {
			ColorModel cm = can.getColorModel();
			cm = GraphicsUtil.coerceData(canWR, cm, false);
		}

		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, false);
		}

		return result;
	}

	private static boolean isSameColorSpace(ColorSpace refColorSpace, ColorSpace canColorSpace) {
		if (refColorSpace == canColorSpace) {
			return true;
		}
		if (!(refColorSpace instanceof ICC_ColorSpace) || !(canColorSpace instanceof ICC_ColorSpace)) {
			// False, otherwise they would be the same object
			return false;
		}
		ICC_Profile refProfile = ((ICC_ColorSpace) refColorSpace).getProfile();
		ICC_Profile canProfile = ((ICC_ColorSpace) canColorSpace).getProfile();
		if (refProfile.getProfileClass() != canProfile.getProfileClass()
				|| refProfile.getMajorVersion() != canProfile.getMajorVersion()
				|| refProfile.getMinorVersion() != canProfile.getMinorVersion()
				|| Math.abs(refProfile.getData().length - canProfile.getData().length) > 16) {
			// Allow up to 16 different bytes of length, to prevent potential padding issues
			return false;
		}
		return true;
	}

	private static int computeMaxDiffPixels(float numpxFrac, float allowedPercent, int width,
			int height) {
		int maxDiffPx;

		if (allowedPercent == 0f) {
			maxDiffPx = 0;
		} else {
			maxDiffPx = Math.round(numpxFrac * allowedPercent);
			// If allowedPercent is small but nonzero, maybe there are too few accepted misses.
			if (maxDiffPx < minDiffPixelsForNonzeroAllowance && allowedPercent < 1f) {
				// We want to accept at most the number of pixels in a side, minus one.
				int minDiff = Math.min(width, height) - 1;
				minDiff = Math.min(minDiff, minDiffPixelsForNonzeroAllowance);
				if (maxDiffPx < minDiff) {
					// Allow at least minDiff pixels for (0 < allowedPercent < 1).
					maxDiffPx = minDiff;
				}
			}
		}

		return maxDiffPx;
	}

	/**
	 * Compare a candidate image to a reference, taking into account one or more
	 * possible variations.
	 * <p>
	 * Before calling this method, it is expected that
	 * {@link #compareImages(BufferedImage, BufferedImage, int, float, float)} will
	 * be called first, and only if that comparison does not match will this one be
	 * attempted: this method does not do a direct comparison between the reference
	 * and candidate images, except for basic compatibility.
	 * </p>
	 * <p>
	 * If the returned code is greater than {@code MATCH} but smaller than
	 * {@code DIFFERENT_PIXELS_BELOW_THRESHOLD}, the images cannot be compared.
	 * </p>
	 * 
	 * @param ref                          the reference image.
	 * @param can                          the other (candidate) image.
	 * @param pixelThreshold               the threshold to classify the differences
	 *                                     among the pixel values.
	 * @param allowedPercentBelowThreshold the allowed percentage of below-threshold
	 *                                     different pixels.
	 * @param allowedPercentOverThreshold  the allowed percentage of over-threshold
	 *                                     different pixels.
	 * @param variants                     the accepted image variations.
	 * @return
	 *         <ul>
	 *         <li>{@code MATCH} if the images match.</li>
	 *         <li>{@code DIFFERENT_SIZES} if the sizes are different.</li>
	 *         <li>{@code DIFFERENT_TRANSPARENCIES} if the transparencies do not
	 *         match.</li>
	 *         <li>{@code DIFFERENT_TYPES} if the image types are not the same.</li>
	 *         <li>{@code DIFFERENT_COLOR_SPACES} if the images have different color
	 *         spaces.</li>
	 *         <li>{@code DIFFERENT_PIXELS_BELOW_THRESHOLD} if the images have too
	 *         many below-threshold different pixels .</li>
	 *         <li>{@code DIFFERENT_PIXELS_OVER_THRESHOLD} if the images have too
	 *         many over-threshold different pixels.</li>
	 *         <li>{@code NO_VARIANTS}: no variants were found.</li>
	 *         <li>{@code VARIANT_ERROR}: at least one of the image variants is
	 *         wrong (wrong size, etc).</li>
	 *         </ul>
	 */
	public static short compareVariantImages(BufferedImage ref, BufferedImage can, int pixelThreshold,
			float allowedPercentBelowThreshold, float allowedPercentOverThreshold, ImageVariants variants) {
		// Obtain reference image size
		final int w = ref.getWidth();
		final int h = ref.getHeight();

		// Compare width and height
		if (w != can.getWidth() || h != can.getHeight()) {
			return DIFFERENT_SIZES;
		}

		// Compare transparency
		if (ref.getTransparency() != can.getTransparency()) {
			return DIFFERENT_TRANSPARENCIES;
		}

		// Compare type
		if (ref.getType() != can.getType()) {
			return DIFFERENT_TYPES;
		}

		ColorSpace refCS = ref.getColorModel().getColorSpace();
		int colorSpace = refCS.getType();
		// Compare color spaces
		if (colorSpace != can.getColorModel().getColorSpace().getType()) {
			return DIFFERENT_COLOR_SPACES;
		}

		final int numBands = ref.getSampleModel().getNumBands();
		final float numpxFrac = w * h * numBands * 0.01f;
		final int maxDiffPx = computeMaxDiffPixels(numpxFrac, allowedPercentOverThreshold, w, h);
		final int maxDiffPxBelow = computeMaxDiffPixels(numpxFrac, allowedPercentBelowThreshold, w,
				h);

		WritableRaster refWR = ref.getRaster();
		WritableRaster canWR = can.getRaster();

		// Pre-multiply alpha channel(s), if needed
		final boolean refPre = ref.isAlphaPremultiplied();
		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, true);
			ref = new BufferedImage(cm, refWR, true, null);
		}
		final boolean canPre = can.isAlphaPremultiplied();
		if (!canPre) {
			ColorModel cm = can.getColorModel();
			cm = GraphicsUtil.coerceData(canWR, cm, true);
			can = new BufferedImage(cm, canWR, true, null);
		}

		// Prepare range variant
		BufferedImage variant = variants.getVariantImage(0, refCS);

		short result = NO_VARIANTS;

		/*
		 * Compare with the range variant, if available
		 */
		if (variant != null) {
			if (w != variant.getWidth() || h != variant.getHeight()) {
				if (!canPre) {
					GraphicsUtil.coerceData(canWR, can.getColorModel(), false);
				}
				if (!refPre) {
					GraphicsUtil.coerceData(refWR, ref.getColorModel(), false);
				}
				return VARIANT_ERROR;
			}

			result = rangeCompare(ref, canWR, variant);
		}

		if (result != MATCH) {
			// Compare to the other variants
			final int vCount = variants.getVariantCount();
			for (int i = 1; i < vCount; i++) {
				variant = variants.getVariantImage(i, refCS);
				if (variant != null) {
					if (w != variant.getWidth() || h != variant.getHeight()) {
						result = VARIANT_ERROR;
						break;
					}
					// Compare with the variant
					result = compareVariant(ref, canWR, variant, pixelThreshold, maxDiffPxBelow, maxDiffPx);
					if (result == MATCH) {
						break;
					}
				}
			}
		}

		// De-multiply, if appropriate
		if (!canPre) {
			ColorModel cm = can.getColorModel();
			GraphicsUtil.coerceData(canWR, cm, false);
		}

		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			GraphicsUtil.coerceData(refWR, cm, false);
		}

		return result;
	}

	private static short rangeCompare(BufferedImage ref, WritableRaster genWR, BufferedImage variant) {
		final int w = ref.getWidth();
		final int h = ref.getHeight();

		WritableRaster refWR = ref.getRaster();
		WritableRaster varWR = variant.getRaster();

		/*
		 * Compare pixels
		 */
		short result = MATCH;
		int countDiffPx = 0;

		// Allowed number of out-of-range pixels
		final int maxDiffPx = Math.round(w * h * 0.0001f);

		int[] refPix = null;
		int[] genPix = null;
		int[] varPix = null;

		topLoop: for (int y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			genPix = genWR.getPixels(0, y, w, 1, genPix);
			varPix = varWR.getPixels(0, y, w, 1, varPix);
			for (int i = 0; i < refPix.length; i++) {
				int diff = genPix[i] - refPix[i];
				if (diff != 0) {
					// Different pixel
					final int varPx = varPix[i];
					if (varPx != 0) {
						// We do expect a diff here, check whether it is
						// in range with a margin of 1
						if (Math.abs(diff) <= varPx + 1) {
							continue;
						}
					}
					// We allow a small amount of out-of-range pixels
					countDiffPx++;
					if (countDiffPx > maxDiffPx) {
						result = DIFFERENT_PIXELS_OVER_THRESHOLD;
						break topLoop;
					}
				}
			}
		}

		return result;
	}

	private static short compareVariant(BufferedImage ref, WritableRaster genWR, BufferedImage variant,
			int pixelThreshold, int maxDiffPxBelow, int maxDiffPx) {
		final int w = ref.getWidth();
		final int h = ref.getHeight();

		WritableRaster refWR = ref.getRaster();
		WritableRaster varWR = variant.getRaster();

		/*
		 * Compare pixels
		 */
		short result = MATCH;
		int countDiffPx = 0, countDiffPxBelow = 0;
		int[] refPix = null;
		int[] genPix = null;
		int[] varPix = null;

		topLoop: for (int y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			genPix = genWR.getPixels(0, y, w, 1, genPix);
			varPix = varWR.getPixels(0, y, w, 1, varPix);
			for (int i = 0; i < refPix.length; i++) {
				int diff = genPix[i] - refPix[i];
				if (diff != 0) {
					// Different pixel
					final int varPx = varPix[i];
					if (varPx == 0) {
						// This may have been an out-of-bounds pixel
						if (diff < -12) { // -13 * diffPixelFactor = -130
							continue;
						}
					} else if (varPx == 255) {
						// This may have been an out-of-bounds pixel
						if (diff > 12) { // 13 * diffPixelFactor = 130
							continue;
						}
					}
					final int acceptDiff = Math.round(((float) (varPx - 128)) / diffPixelFactor);
					diff = Math.abs(diff - acceptDiff);
					// We give a margin of 1
					if (diff <= 1) {
						continue;
					}
					if (diff <= pixelThreshold) {
						countDiffPxBelow++;
						if (countDiffPxBelow > maxDiffPxBelow) {
							result = DIFFERENT_PIXELS_BELOW_THRESHOLD;
							break topLoop;
						}
					} else {
						countDiffPx++;
						if (countDiffPx > maxDiffPx) {
							result = DIFFERENT_PIXELS_OVER_THRESHOLD;
							break topLoop;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Provides indexed access to image variants.
	 * <p>
	 * The first variant is always the <i>range</i> variant, and the candidate image
	 * will match if its pixels are in the range between the reference and this
	 * variant. The range variant should be an exact diff (see
	 * {@link ImageComparator#createExactDiffImage(BufferedImage, BufferedImage)
	 * createExactDiffImage(BufferedImage, BufferedImage)}).
	 * </p>
	 * <p>
	 * The other variants (generally platform variants) are matched differently: the
	 * candidate image must match the variation within the accepted pixel error
	 * margins. These variants are expected to have been created by
	 * {@link ImageComparator#createDiffImage(BufferedImage, BufferedImage)
	 * createDiffImage(BufferedImage, BufferedImage)}.
	 * </p>
	 */
	public interface ImageVariants {

		/**
		 * Gives the maximum number of image variants, including the range variant.
		 * <p>
		 * Should be at least 1, otherwise it is expected that a variant comparison
		 * would not be performed at all.
		 * </p>
		 * <p>
		 * A count larger than {@code 0} does not guarantee that a variant will be
		 * available when retrieved via {@link #getVariantImage(int, ColorSpace)}.
		 * </p>
		 * 
		 * @return the total count of image variants.
		 */
		int getVariantCount();

		/**
		 * Provides the variant image for the given index.
		 * <p>
		 * May return {@code null} even if the index is greater than zero and lower than
		 * {@link #getVariantCount()}, if no variant is available for that index.
		 * </p>
		 * 
		 * @param index      the index. If {@code 0}, must be the range variant.
		 * @param colorSpace the desired color space, {@code null} means sRGB.
		 * @return the variant image, or {@code null} if there is no such image.
		 */
		BufferedImage getVariantImage(int index, ColorSpace colorSpace);

	}

	public static String getResultDescription(short code) {
		String desc;
		switch (code) {
		case MATCH:
			desc = "Match";
			break;
		case DIFFERENT_SIZES:
			desc = "The images have different sizes";
			break;
		case DIFFERENT_TRANSPARENCIES:
			desc = "The images have different sizes";
			break;
		case DIFFERENT_TYPES:
			desc = "The images are of different types";
			break;
		case DIFFERENT_COLOR_SPACES:
			desc = "The images have different color spaces";
			break;
		case DIFFERENT_BANDS:
			desc = "The images have a different number of bands";
			break;
		case DIFFERENT_DATA_TYPES:
			desc = "The images use different buffer data types";
			break;
		case DIFFERENT_PIXELS_BELOW_THRESHOLD:
			desc = "The images have too many below-threshold different pixels";
			break;
		case DIFFERENT_PIXELS_OVER_THRESHOLD:
			desc = "The images have too many over-threshold different pixels";
			break;
		case NO_VARIANTS:
			desc = "A variant comparison was executed but no variants were found";
			break;
		case VARIANT_ERROR:
			desc = "At least one of the image variants is wrong (wrong size, etc)";
			break;
		default:
			desc = "unknown code";
		}
		return desc;
	}

	/**
	 * Creates an image comparing the two given images, one on each side.
	 * 
	 * @param ref the reference image.
	 * @param gen the other image.
	 * @return an image comparing the two given images, one on each side.
	 */
	public static BufferedImage createCompareImage(BufferedImage ref, BufferedImage gen) {
		BufferedImage cmp = createImageOfType(ref.getWidth() * 2, ref.getHeight(), ref);

		Graphics2D g = cmp.createGraphics();
		g.setPaint(Color.white);
		g.fillRect(0, 0, cmp.getWidth(), cmp.getHeight());
		g.drawImage(ref, 0, 0, null);
		g.translate(ref.getWidth(), 0);
		g.drawImage(gen, 0, 0, null);
		g.dispose();

		return cmp;
	}

	/**
	 * Creates a new image that is the difference between the two input images,
	 * multiplied by a factor 10.
	 * <p>
	 * Due to the enhanced difference, the resulting image is more suitable for
	 * visual inspection than the one produced by
	 * {@link #createExactDiffImage(BufferedImage, BufferedImage)}.
	 * </p>
	 * 
	 * @param ref the reference image.
	 * @param gen the other image.
	 * @return a new BufferedImage that is the (enhanced) difference between the two
	 *         input images.
	 */
	public static BufferedImage createDiffImage(BufferedImage ref, BufferedImage gen) {
		final int w = ref.getWidth();
		final int h = ref.getHeight();

		BufferedImage diff = createImageOfType(w, h, ref);

		WritableRaster refWR = ref.getRaster();
		WritableRaster genWR = gen.getRaster();
		WritableRaster dstWR = diff.getRaster();

		// Pre-multiply alpha channel(s), if needed
		final boolean refPre = ref.isAlphaPremultiplied();
		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, true);
			ref = new BufferedImage(cm, refWR, true, null);
		}
		final boolean genPre = gen.isAlphaPremultiplied();
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, true);
			gen = new BufferedImage(cm, genWR, true, null);
		}

		// Draw the diff image
		int[] refPix = null;
		int[] genPix = null;
		for (int y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			genPix = genWR.getPixels(0, y, w, 1, genPix);
			for (int i = 0; i < refPix.length; i++) {
				int val = ((genPix[i] - refPix[i]) * diffPixelFactor) + 128;
				if ((val & 0xFFFFFF00) != 0)
					if ((val & 0x80000000) != 0)
						val = 0;
					else
						val = 255;
				genPix[i] = val;
			}
			dstWR.setPixels(0, y, w, 1, genPix);
		}

		// De-multiply, if appropriate
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, false);
		}

		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, false);
		}

		return diff;
	}

	private static BufferedImage createImageOfType(int width, int height, BufferedImage ref) {
		BufferedImage image;

		int type = ref.getType();
		if (type != BufferedImage.TYPE_CUSTOM) {
			image = new BufferedImage(width, height, type);
		} else {
			ColorModel cm = ref.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
			image = new BufferedImage(cm, raster, false, null);
		}

		return image;
	}

	/**
	 * Creates a new image that is the exact difference between the two input
	 * images.
	 * 
	 * @param ref the reference image.
	 * @param gen the other image.
	 * @return a new BufferedImage that is the exact difference between the two
	 *         input images.
	 */
	public static BufferedImage createExactDiffImage(BufferedImage ref, BufferedImage gen) {
		final int w = ref.getWidth();
		final int h = ref.getHeight();
		if (w != gen.getWidth()) {
			throw new IllegalArgumentException(
					"Ref. image has width " + w + " but generated image is " + gen.getWidth());
		}
		if (h != gen.getHeight()) {
			throw new IllegalArgumentException(
					"Ref. image has height " + h + " but generated image is " + gen.getHeight());
		}

		BufferedImage diff = createImageOfType(w, h, ref);

		WritableRaster refWR = ref.getRaster();
		WritableRaster genWR = gen.getRaster();
		WritableRaster dstWR = diff.getRaster();

		// Pre-multiply alpha channel(s), if needed
		final boolean refPre = ref.isAlphaPremultiplied();
		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, true);
			ref = new BufferedImage(cm, refWR, true, null);
		}
		final boolean genPre = gen.isAlphaPremultiplied();
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, true);
			gen = new BufferedImage(cm, genWR, true, null);
		}

		// Draw the diff image
		int[] refPix = null;
		int[] genPix = null;
		for (int y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			genPix = genWR.getPixels(0, y, w, 1, genPix);
			for (int i = 0; i < refPix.length; i++) {
				int val = Math.abs(genPix[i] - refPix[i]);
				genPix[i] = val;
			}
			dstWR.setPixels(0, y, w, 1, genPix);
		}

		// De-multiply, if appropriate
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, false);
		}

		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, false);
		}

		return diff;
	}

	/**
	 * Creates a new image that is the difference between the two input images,
	 * merged with a previous exact diff.
	 * 
	 * @param ref       the reference image.
	 * @param gen       the other image.
	 * @param rangeDiff the current range diff being used against the reference.
	 * @return a new BufferedImage that is the difference between the two input
	 *         images, merged with the previous diff.
	 */
	public static BufferedImage createMergedDiffImage(BufferedImage ref, BufferedImage gen, BufferedImage rangeDiff) {
		final int w = ref.getWidth();
		final int h = ref.getHeight();

		BufferedImage diff = createImageOfType(w, h, ref);

		WritableRaster refWR = ref.getRaster();
		WritableRaster genWR = gen.getRaster();
		WritableRaster rangeWR = rangeDiff.getRaster();
		WritableRaster dstWR = diff.getRaster();

		// Pre-multiply alpha channel(s), if needed
		final boolean refPre = ref.isAlphaPremultiplied();
		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, true);
			ref = new BufferedImage(cm, refWR, true, null);
		}
		final boolean genPre = gen.isAlphaPremultiplied();
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, true);
			gen = new BufferedImage(cm, genWR, true, null);
		}

		// Draw the diff image
		int[] refPix = null;
		int[] genPix = null;
		int[] rangePix = null;
		for (int y = 0; y < h; y++) {
			refPix = refWR.getPixels(0, y, w, 1, refPix);
			genPix = genWR.getPixels(0, y, w, 1, genPix);
			rangePix = rangeWR.getPixels(0, y, w, 1, rangePix);
			for (int i = 0; i < refPix.length; i++) {
				int val = Math.abs(genPix[i] - refPix[i]);
				int range = rangePix[i];
				if (val > range) {
					range = val;
				}
				genPix[i] = range;
			}
			dstWR.setPixels(0, y, w, 1, genPix);
		}

		// De-multiply, if appropriate
		if (!genPre) {
			ColorModel cm = gen.getColorModel();
			cm = GraphicsUtil.coerceData(genWR, cm, false);
		}

		if (!refPre) {
			ColorModel cm = ref.getColorModel();
			cm = GraphicsUtil.coerceData(refWR, cm, false);
		}

		return diff;
	}

}
