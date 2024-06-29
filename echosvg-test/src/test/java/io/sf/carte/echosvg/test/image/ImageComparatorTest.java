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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.test.ResourceLoader;

public class ImageComparatorTest {

	@Test
	public void testCompareSameImage() throws IOException {
		BufferedImage img = loadImage("geom.png");
		BufferedImage other = loadImage("geom.png");

		short result = ImageComparator.compareImages(img, other, 0, 0f, 0f);
		assertEquals(ImageComparator.MATCH, result);
	}

	@Test
	public void testCompareImagesDifferentPixels() throws IOException {
		BufferedImage img = loadImage("domSVGColor.png");
		BufferedImage other = loadImage("domSVGColor-2.png");

		short result = ImageComparator.compareImages(img, other, 0, 0f, 0f);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);
	}

	/*
	 * The purpose of this test is to show that two images with all different
	 * pixels match when allowance is 100%, but not when it is 99.9999%.
	 */
	@Test
	public void testCompareImagesAllDifferentPixels() throws IOException {
		BufferedImage img = loadImage("blank.png");
		BufferedImage other = loadImage("void.png");

		short result = ImageComparator.compareImages(img, other, 0, 0f, 99.9999f);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);

		result = ImageComparator.compareImages(img, other, 0, 0f, 100f);
		assertEquals(ImageComparator.MATCH, result);
	}

	@Test
	public void testCompareImagesDifferentPixelsAliasing() throws IOException {
		BufferedImage img = loadImage("geom.png");
		BufferedImage other = loadImage("geom-2.png");

		short result = ImageComparator.compareImages(img, other, 8, 1.5f, 2.7f);
		assertEquals(ImageComparator.MATCH, result);

		// Now attempt a variant match
		BufferedImage diff = ImageComparator.createExactDiffImage(img, other);
		BufferedImage[] variantImages = new BufferedImage[1];
		variantImages[0] = diff;

		MyVariants variants = new MyVariants(variantImages);
		result = ImageComparator.compareVariantImages(img, other, 8, 0.0004f, 2.1f, variants);
		assertEquals(ImageComparator.MATCH, result);
	}

	@Test
	public void testCompareVariantImages() throws IOException {
		BufferedImage img = loadImage("systemColors.png");
		BufferedImage other1 = loadImage("systemColors-candidate.png");
		BufferedImage other2 = loadImage("systemColors-candidate-xfce.png");
		BufferedImage variant1 = loadImage("systemColors_xfce.png");

		short result = ImageComparator.compareImages(img, other1, 0, 0f, 0f);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);

		// It requires too broad tolerances to match
		result = ImageComparator.compareImages(img, other1, 8, 2.6f, 4.7f);
		assertEquals(ImageComparator.MATCH, result);

		result = ImageComparator.compareImages(img, other2, 0, 0f, 0f);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);

		// Create a diff for the range variant
		BufferedImage rangeVariant = ImageComparator.createExactDiffImage(img, other1);

		// First, a range match
		BufferedImage[] variantImages = new BufferedImage[1];
		variantImages[0] = rangeVariant;

		MyVariants variants = new MyVariants(variantImages);
		result = ImageComparator.compareVariantImages(img, other1, 0, 0f, 0f, variants);
		assertEquals(ImageComparator.MATCH, result);
		// Now a non-match
		result = ImageComparator.compareVariantImages(img, other2, 0, 0f, 0f, variants);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);

		// Another non-match
		variantImages = new BufferedImage[2];
		variantImages[0] = rangeVariant;
		variantImages[1] = loadImage("void.png");
		variants = new MyVariants(variantImages);
		result = ImageComparator.compareVariantImages(img, other2, 0, 0f, 0f, variants);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);

		// Now, match with variant1
		variantImages = new BufferedImage[3];
		variantImages[0] = rangeVariant;
		variantImages[1] = loadImage("void.png");
		variantImages[2] = variant1;
		variants = new MyVariants(variantImages);

		result = ImageComparator.compareVariantImages(img, other2, 0, 0f, 0f, variants);
		assertEquals(ImageComparator.MATCH, result);

		// A non-match with a blank range variant
		variantImages[0] = loadImage("blank.png");
		result = ImageComparator.compareVariantImages(img, other1, 0, 0f, 0f, variants);
		assertEquals(ImageComparator.DIFFERENT_PIXELS_OVER_THRESHOLD, result);
	}

	@Test
	public void testCompareVariantImages2() throws IOException {
		BufferedImage img = loadImage("textBiDi2.png");
		BufferedImage other1 = loadImage("textBiDi2-candidate.png");
		BufferedImage variant1 = loadImage("textBiDi2_xfce.png");

		BufferedImage[] variantImages = new BufferedImage[2];
		variantImages[1] = variant1;
		MyVariants variants = new MyVariants(variantImages);

		short result = ImageComparator.compareVariantImages(img, other1, 0, 0f, 0f, variants);
		assertEquals(ImageComparator.MATCH, result);
	}

	private static BufferedImage loadImage(String filename) throws IOException {
		InputStream is = classpathStream(filename);
		ImageTagRegistry reg = ImageTagRegistry.getRegistry();
		Filter filt = reg.readStream(is);
		assertNotNull(filt);
		RenderedImage rend = filt.createDefaultRendering();
		assertNotNull(rend);
		BufferedImage image = new BufferedImage(rend.getWidth(), rend.getHeight(), BufferedImage.TYPE_INT_ARGB);
		rend.copyData(image.getRaster());
		is.close();
		return image;
	}

	private static InputStream classpathStream(final String filename) {
		return ResourceLoader.getInstance().getResourceAsStream(ImageComparatorTest.class, filename);
	}

	private static class MyVariants implements ImageComparator.ImageVariants {

		private final BufferedImage[] variants;

		private MyVariants(BufferedImage[] variants) {
			super();
			this.variants = variants;
		}

		@Override
		public int getVariantCount() {
			return variants.length;
		}

		@Override
		public BufferedImage getVariantImage(int index) {
			if (index < 0 || index > variants.length) {
				return null;
			}
			return variants[index];
		}

	}

}
