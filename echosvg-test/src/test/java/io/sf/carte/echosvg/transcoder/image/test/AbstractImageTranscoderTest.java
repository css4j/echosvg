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
package io.sf.carte.echosvg.transcoder.image.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageTagRegistry;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriter;
import io.sf.carte.echosvg.ext.awt.image.spi.ImageWriterRegistry;
import io.sf.carte.echosvg.test.image.ImageComparator;
import io.sf.carte.echosvg.test.misc.TestLocations;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;

/**
 * The base class for the ImageTranscoder tests.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractImageTranscoderTest {

	private static final String RENDERING_CANDIDATE_REF_PATH = "test-references/io/sf/carte/echosvg/transcoder/image/candidate-ref/";

	private static final String RENDERING_CANDIDATE_VAR_PATH = "test-references/io/sf/carte/echosvg/transcoder/image/candidate-variation/";

	private static final String RENDERING_ACCEPTED_VAR_PATH = "test-references/io/sf/carte/echosvg/transcoder/image/accepted-variation/";

	private String filename;

	/**
	 * Constructs a new <code>AbstractImageTranscoderTest</code>.
	 */
	AbstractImageTranscoderTest() {
	}

	/**
	 * Resolves the input string as a URL. If it is an invalid
	 * URI, an IllegalArgumentException is thrown.
	 */
	URL resolveURI(String url) {
		url = TestLocations.PROJECT_ROOT_URL + url;
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(url);
		}
	}

	/**
	 * Runs this test.
	 * 
	 * @throws TranscoderException
	 */
	public void runTest() throws TranscoderException {
		DiffImageTranscoder transcoder = new DiffImageTranscoder(getReferenceImageData());

		Map<Key, Object> hints = createTranscodingHints();
		if (hints != null) {
			transcoder.setTranscodingHints(hints);
		}

		TranscoderInput input = createTranscoderInput();
		transcoder.transcode(input, null);

		if (!transcoder.isIdentical()) {
			String message;
			String uri = input.getURI();
			if (uri != null) {
				try {
					message = "Image comparison failed: rendering " + new URL(uri).getPath() + ",\nobtained "
							+ RENDERING_CANDIDATE_REF_PATH + new File(filename).getName();
				} catch (MalformedURLException e) {
					message = "Image comparison with a reference (in test-references) failed.";
				}
			} else {
				message = "Image comparison failed: see result at " + RENDERING_CANDIDATE_REF_PATH
						+ new File(filename).getName();
			}
			fail(message);
		}
	}

	/**
	 * Creates the <code>TranscoderInput</code>.
	 */
	protected abstract TranscoderInput createTranscoderInput();

	/**
	 * Creates a Map that contains additional transcoding hints.
	 */
	protected Map<Key, Object> createTranscodingHints() {
		return null;
	}

	/**
	 * Returns the reference image for this test.
	 */
	protected abstract byte[] getReferenceImageData();

	//////////////////////////////////////////////////////////////////////////
	// Convenient methods
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Gives the specified exception as a string.
	 */
	public static String toString(Exception ex) {
		StringWriter trace = new StringWriter();
		ex.printStackTrace(new PrintWriter(trace));
		return trace.toString();
	}

	/**
	 * Loads an image from a URL
	 */
	byte[] createBufferedImageData(URL url) {
		try {
			filename = url.getFile();
			InputStream istream = url.openStream();
			byte[] imgData = null;
			byte[] buf = new byte[1024];
			int length;
			while ((length = istream.read(buf, 0, buf.length)) == buf.length) {
				if (imgData != null) {
					byte[] imgDataTmp = new byte[imgData.length + length];
					System.arraycopy(imgData, 0, imgDataTmp, 0, imgData.length);
					System.arraycopy(buf, 0, imgDataTmp, imgData.length, length);
					imgData = imgDataTmp;
				} else {
					imgData = new byte[length];
					System.arraycopy(buf, 0, imgData, 0, length);
				}
			}
			if (imgData != null) {
				byte[] imgDataTmp = new byte[imgData.length + length];
				System.arraycopy(imgData, 0, imgDataTmp, 0, imgData.length);
				System.arraycopy(buf, 0, imgDataTmp, imgData.length, length);
				imgData = imgDataTmp;
			} else {
				imgData = new byte[length];
				System.arraycopy(buf, 0, imgData, 0, length);
			}
			istream.close();
			return imgData;
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * A custom ImageTranscoder for testing.
	 */
	private class DiffImageTranscoder extends ImageTranscoder {

		/** The result of the image comparison. */
		private boolean state;

		/** The reference image. */
		private byte[] refImgData;

		/**
		 * Constructs a new <code>DiffImageTranscoder</code>.
		 *
		 * @param refImgData the reference image data
		 */
		public DiffImageTranscoder(byte[] refImgData) {
			this.refImgData = refImgData;
		}

		/**
		 * Creates a new image with the specified dimension.
		 * 
		 * @param w the image width in pixels
		 * @param h the image height in pixels
		 */
		@Override
		public BufferedImage createImage(int w, int h) {
			return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}

		/**
		 * Compares the specified image with the reference image and set the state flag.
		 *
		 * @param img    the image to write
		 * @param output the output (ignored)
		 * @throw TranscoderException if an error occured while storing the image
		 */
		@Override
		public void writeImage(BufferedImage img, TranscoderOutput output) throws TranscoderException {

			try {
				state = compareImage(img);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeCandidateReference(byte[] imgData) {
			writeCandidateFile(imgData, RENDERING_CANDIDATE_REF_PATH);
		}

		private void writeCandidateVariation(byte[] diff) {
			writeCandidateFile(diff, RENDERING_CANDIDATE_VAR_PATH);
		}

		private File writeCandidateFile(byte[] data, String dirname) {
			File f = null;
			try {
				String s = new File(filename).getName();
				File candidateDir = new File(resolveURI(dirname).getFile());
				if (!candidateDir.exists()) {
					if (!candidateDir.mkdir()) {
						return null;
					}
				}
				f = new File(candidateDir, s);
				FileOutputStream ostream = new FileOutputStream(f);
				ostream.write(data, 0, data.length);
				ostream.flush();
				ostream.close();
			} catch (Exception e) {
			}
			return f;
		}

		/**
		 * Compares both source and result images and set the state flag.
		 */
		private boolean compareImage(BufferedImage img) throws TranscoderException, IOException {
			// compare the resulting image with the reference image
			// state = true if refImg is the same than img

			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			TranscoderOutput output = new TranscoderOutput(out);
			PNGTranscoder t = new PNGTranscoder();
			Map<Key, Object> hints = createTranscodingHints();
			if (hints != null) {
				t.setTranscodingHints(hints);
			}
			t.writeImage(img, output);
			byte[] imgData = out.toByteArray();

			if (refImgData == null) {
				// No reference image, create one
				writeCandidateReference(imgData);
				return false;
			}

			if (!Arrays.equals(refImgData, imgData)) {
				byte[] actualDiff = createDiffImage(img);
				File acceptedDiffFile = new File(
						resolveURI(RENDERING_ACCEPTED_VAR_PATH).getFile(), new File(filename).getName());
				if (!(acceptedDiffFile.exists() && dataFromFileEqual(acceptedDiffFile, actualDiff))) {
					writeCandidateReference(imgData);
					writeCandidateVariation(actualDiff);
					return false;
				}
			}

			return true;
		}

		private byte[] createDiffImage(BufferedImage actualImage) throws IOException {
			BufferedImage referenceImage = getImage(new ByteArrayInputStream(refImgData),
					actualImage.getColorModel().getColorSpace());
			BufferedImage diffImage = ImageComparator.createDiffImage(referenceImage, actualImage);
			ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");
			ByteArrayOutputStream diff = new ByteArrayOutputStream(2048);
			writer.writeImage(diffImage, diff);
			return diff.toByteArray();
		}

		private boolean dataFromFileEqual(File file, byte[] data) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
			try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
				is.transferTo(out);
			}
			return Arrays.equals(out.toByteArray(), data);
		}

		/**
		 * Returns true if the reference image is the same than the generated image,
		 * false otherwise.
		 */
		boolean isIdentical() {
			return state;
		}

	}

	private BufferedImage getImage(InputStream is, ColorSpace colorSpace) throws IOException {
		ImageTagRegistry reg = ImageTagRegistry.getRegistry();
		Filter filt = reg.readStream(is, colorSpace);
		if (filt == null)
			throw new IOException("Couldn't read Stream");

		RenderedImage red = filt.createDefaultRendering();
		if (red == null)
			throw new IOException("Couldn't render Stream");

		BufferedImage img;
		if (red instanceof BufferedImage) {
			img = (BufferedImage) red;
		} else if (red.getColorModel().getColorSpace().isCS_sRGB()) {
			img = new BufferedImage(red.getWidth(), red.getHeight(), BufferedImage.TYPE_INT_ARGB);
			red.copyData(img.getRaster());
		} else {
			ColorModel cm = red.getColorModel();
			WritableRaster wr = (WritableRaster) red.getData();
			img = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
		}

		return img;
	}

}
