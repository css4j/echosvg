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

package io.sf.carte.echosvg.transcoder.wmf.tosvg;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.echosvg.svggen.SVGGraphics2D;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.svggen.ToSVGAbstractTranscoder;

/**
 * This class implements the <code>Transcoder</code> interface and can convert a
 * WMF input document into an SVG document.
 * <p>
 * This class is copied from batik
 * io.sf.carte.echosvg.transcoder.wmf.tosvg.WMFTranscoder class.
 * </p>
 * <p>
 * It can use <code>TranscoderInput</code> that are either a URI or a
 * <code>InputStream</code> or a <code>Reader</code>. The <code>XMLReader</code>
 * and <code>Document</code> <code>TranscoderInput</code> types are not
 * supported.
 * </p>
 *
 * <p>
 * This transcoder can use <code>TranscoderOutputs</code> that are of any type
 * except the <code>XMLFilter</code> type.
 * </p>
 *
 * <p>
 * Corrected bugs from the original class:
 * </p>
 * <ul>
 * <li>Manage images size</li>
 * </ul>
 * <p>
 * Exemple of use :
 * </p>
 * 
 * <pre>
 * WMFTranscoder transcoder = new WMFTranscoder();
 * try {
 * 	TranscoderInput wmf = new TranscoderInput(wmffile.toURL().toString());
 * 	FileOutputStream fos = new FileOutputStream(svgFile);
 * 	TranscoderOutput svg = new TranscoderOutput(new OutputStreamWriter(fos, "UTF-8"));
 * 	transcoder.transcode(wmf, svg);
 * } catch (MalformedURLException e) {
 * 	throw new TranscoderException(e);
 * } catch (IOException e) {
 * 	throw new TranscoderException(e);
 * }
 * </pre>
 * <p>
 * Several transcoding hints are available for this transcoder :
 * </p>
 * KEY_INPUT_WIDTH, KEY_INPUT_HEIGHT, KEY_XOFFSET, KEY_YOFFSET : this Integer
 * values allows to set the portion of the image to transcode, defined by the
 * width, height, and offset of this portion in Metafile units.
 * 
 * <pre>
 * transcoder.addTranscodingHint(FromWMFTranscoder.KEY_INPUT_WIDTH, Integer.valueOf(input_width));
 * </pre>
 * 
 * KEY_WIDTH, KEY_HEIGHT : this Float values allows to force the width and
 * height of the output:
 * 
 * <pre>
 * transcoder.addTranscodingHint(FromWMFTranscoder.KEY_WIDTH, Float.valueOf(width));
 * </pre>
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class WMFTranscoder extends ToSVGAbstractTranscoder {

	/**
	 * Default constructor
	 */
	public WMFTranscoder() {
	}

	/**
	 * Transcodes the specified input in the specified output.
	 * 
	 * @param input  the input to transcode
	 * @param output the ouput where to transcode
	 * @exception TranscoderException if an error occured while transcoding
	 */
	@Override
	public void transcode(TranscoderInput input, TranscoderOutput output) throws TranscoderException {
		//
		// Extract the input
		//
		DataInputStream is = getCompatibleInput(input);

		//
		// Build a RecordStore from the input
		//
		WMFRecordStore currentStore = new WMFRecordStore();
		try {
			currentStore.read(is);
		} catch (IOException e) {
			handler.fatalError(new TranscoderException(e));
			return;
		}

		// determines the width and height of output image
		float wmfwidth; // width in pixels
		float wmfheight; // height in pixels
		float conv = 1.0f; // conversion factor

		if (hints.containsKey(KEY_INPUT_WIDTH)) {
			wmfwidth = (Integer) hints.get(KEY_INPUT_WIDTH);
			wmfheight = (Integer) hints.get(KEY_INPUT_HEIGHT);
		} else {
			wmfwidth = currentStore.getWidthPixels();
			wmfheight = currentStore.getHeightPixels();
		}
		float width = wmfwidth;
		float height = wmfheight;

		// change the output width and height if required
		if (hints.containsKey(KEY_WIDTH)) {
			width = (Float) hints.get(KEY_WIDTH);
			conv = width / wmfwidth;
			height = height * width / wmfwidth;
		}

		// determine the offset values
		int xOffset = 0;
		int yOffset = 0;
		if (hints.containsKey(KEY_XOFFSET)) {
			xOffset = (Integer) hints.get(KEY_XOFFSET);
		}
		if (hints.containsKey(KEY_YOFFSET)) {
			yOffset = (Integer) hints.get(KEY_YOFFSET);
		}

		// Set the size and viewBox on the output document
		float sizeFactor = currentStore.getUnitsToPixels() * conv;

		int vpX = (int) (currentStore.getVpX() * sizeFactor);
		int vpY = (int) (currentStore.getVpY() * sizeFactor);

		int vpW;
		int vpH;
		// if we took only a part of the image, we use its dimension for computing
		if (hints.containsKey(KEY_INPUT_WIDTH)) {
			vpW = (int) ((Integer) hints.get(KEY_INPUT_WIDTH) * conv);
			vpH = (int) ((Integer) hints.get(KEY_INPUT_HEIGHT) * conv);
			// else we took the whole image dimension
		} else {
			vpW = (int) (currentStore.getWidthUnits() * sizeFactor);
			vpH = (int) (currentStore.getHeightUnits() * sizeFactor);
		}

		// Build a painter for the RecordStore
		WMFPainter painter = new WMFPainter(currentStore, xOffset, yOffset, conv);

		// Use SVGGraphics2D to generate SVG content
		Document doc = this.createDocument(output);
		svgGenerator = new SVGGraphics2D(doc);

		/**
		 * set precision otherwise Ellipses aren't working (for example) (because of
		 * Decimal format modifications in SVGGenerator Context)
		 */
		svgGenerator.getGeneratorContext().setPrecision(4);

		painter.paint(svgGenerator);

		svgGenerator.setSVGCanvasSize(new Dimension(vpW, vpH));

		Element svgRoot = svgGenerator.getRoot();

		svgRoot.setAttributeNS(null, SVG_VIEW_BOX_ATTRIBUTE, String.valueOf(vpX) + ' ' + vpY + ' ' + vpW + ' ' + vpH);

		// Now, write the SVG content to the output
		writeSVGToOutput(svgGenerator, svgRoot, output);
	}

	/**
	 * Checks that the input is one of URI or an <code>InputStream</code> returns it
	 * as a DataInputStream
	 */
	private DataInputStream getCompatibleInput(TranscoderInput input) throws TranscoderException {
		// Cannot deal with null input
		if (input == null) {
			handler.fatalError(new TranscoderException(String.valueOf(ERROR_NULL_INPUT)));
			return null;
		}

		// Can deal with InputStream
		InputStream in = input.getInputStream();
		if (in != null) {
			return new DataInputStream(new BufferedInputStream(in));
		}

		// Can deal with URI
		String uri = input.getURI();
		if (uri != null) {
			try {
				URL url = new URL(uri);
				in = url.openStream();
				return new DataInputStream(new BufferedInputStream(in));
			} catch (IOException e) {
				handler.fatalError(new TranscoderException(e));
			}
		}

		handler.fatalError(new TranscoderException(String.valueOf(ERROR_INCOMPATIBLE_INPUT_TYPE)));
		return null;
	}

	public static final String WMF_EXTENSION = ".wmf";
	public static final String SVG_EXTENSION = ".svg";

	/**
	 * Unit testing : Illustrates how the transcoder might be used.
	 */
	public static void main(String[] args) throws TranscoderException {
		if (args.length < 1) {
			System.out.println("Usage : WMFTranscoder.main <file 1> ... <file n>");
			System.exit(1);
		}

		WMFTranscoder transcoder = new WMFTranscoder();

		for (String fileName : args) {
			if (!fileName.toLowerCase().endsWith(WMF_EXTENSION)) {
				System.err.println(fileName + " does not have the " + WMF_EXTENSION + " extension. It is ignored");
			} else {
				System.out.print("Processing : " + fileName + "...");
				String outputFileName = fileName.substring(0, fileName.toLowerCase().indexOf(WMF_EXTENSION))
						+ SVG_EXTENSION;
				File inputFile = new File(fileName);
				File outputFile = new File(outputFileName);
				try {
					TranscoderInput input = new TranscoderInput(inputFile.toURI().toURL().toString());
					TranscoderOutput output = new TranscoderOutput(new FileOutputStream(outputFile));
					transcoder.transcode(input, output);
				} catch (IOException e) {
					throw new TranscoderException(e);
				}
				System.out.println(".... Done");
			}
		}

		System.exit(0);
	}
}
