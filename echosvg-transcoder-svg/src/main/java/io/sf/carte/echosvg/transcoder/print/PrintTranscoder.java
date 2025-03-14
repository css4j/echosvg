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
package io.sf.carte.echosvg.transcoder.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.ext.awt.RenderingHintsKeyExt;
import io.sf.carte.echosvg.transcoder.Transcoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.keys.BooleanKey;
import io.sf.carte.echosvg.transcoder.keys.LengthKey;
import io.sf.carte.echosvg.transcoder.keys.StringKey;
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;

/**
 * This class is a <code>Transcoder</code> that prints SVG images. This class
 * works as follows: any-time the transcode method is invoked, the corresponding
 * input is cached and nothing else happens. <br>
 * However, the <code>PrintTranscoder</code> is also a Printable. If used in a
 * print operation, it will print each of the input it cached, one input per
 * page. <br>
 * The <code>PrintTranscoder</code> uses several different hints that guide its
 * printing:<br>
 * <ul>
 * <li><code>KEY_LANGUAGE, KEY_USER_STYLESHEET_URI, KEY_PIXEL_TO_MM,
 *       KEY_XML_PARSER_CLASSNAME</code> can be used to set the defaults for the
 * various SVG properties.</li>
 * <li><code>KEY_PAGE_WIDTH, KEY_PAGE_HEIGHT, KEY_MARGIN_TOP, KEY_MARGIN_BOTTOM,
 *       KEY_MARGIN_LEFT, KEY_MARGIN_RIGHT</code> and
 * <code>KEY_PAGE_ORIENTATION</code> can be used to specify the printing page
 * characteristics.</li>
 * <li><code>KEY_WIDTH, KEY_HEIGHT</code> can be used to specify how to scale
 * the SVG image</li>
 * <li><code>KEY_SCALE_TO_PAGE</code> can be used to specify whether or not the
 * SVG image should be scaled uniformly to fit into the printed page or if it
 * should just be centered into the printed page.</li>
 * </ul>
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class PrintTranscoder extends SVGAbstractTranscoder implements Printable {

	public static final String KEY_AOI_STR = "aoi";
	public static final String KEY_HEIGHT_STR = "height";
	public static final String KEY_LANGUAGE_STR = "language";
	public static final String KEY_MARGIN_BOTTOM_STR = "marginBottom";
	public static final String KEY_MARGIN_LEFT_STR = "marginLeft";
	public static final String KEY_MARGIN_RIGHT_STR = "marginRight";
	public static final String KEY_MARGIN_TOP_STR = "marginTop";
	public static final String KEY_PAGE_HEIGHT_STR = "pageHeight";
	public static final String KEY_PAGE_ORIENTATION_STR = "pageOrientation";
	public static final String KEY_PAGE_WIDTH_STR = "pageWidth";
	public static final String KEY_PIXEL_TO_MM_STR = "pixelToMm";
	public static final String KEY_SCALE_TO_PAGE_STR = "scaleToPage";
	public static final String KEY_SHOW_PAGE_DIALOG_STR = "showPageDialog";
	public static final String KEY_SHOW_PRINTER_DIALOG_STR = "showPrinterDialog";
	public static final String KEY_USER_STYLESHEET_URI_STR = "userStylesheet";
	public static final String KEY_WIDTH_STR = "width";
	public static final String VALUE_MEDIA_PRINT = "print";
	public static final String VALUE_PAGE_ORIENTATION_LANDSCAPE = "landscape";
	public static final String VALUE_PAGE_ORIENTATION_PORTRAIT = "portrait";
	public static final String VALUE_PAGE_ORIENTATION_REVERSE_LANDSCAPE = "reverseLandscape";

	/**
	 * Set of inputs this transcoder has been requested to transcode so far. Purpose
	 * is not really clear: some data is added, and it is copied into printedInputs.
	 * But it is never read or cleared...
	 */
	private List<TranscoderInput> inputs = new ArrayList<>();

	/**
	 * Currently printing set of pages. This vector is created as a clone of inputs
	 * when the first page is printed.
	 */
	private List<TranscoderInput> printedInputs = null;

	/**
	 * Index of the page corresponding to root
	 */
	private int curIndex = -1;

	/**
	 * Place to cache BridgeContext so we can dispose of it when it is appropriate.
	 * The Baseclass would dispose of it too soon.
	 */
	private BridgeContext theCtx;

	/**
	 * Constructs a new transcoder that prints images.
	 */
	public PrintTranscoder() {
		super();

		hints.put(KEY_MEDIA, VALUE_MEDIA_PRINT);
	}

	@Override
	public void transcode(TranscoderInput in, TranscoderOutput out) {
		if (in != null) {
			inputs.add(in);
		}
	}

	/**
	 * Transcodes the specified Document as an image in the specified output.
	 *
	 * @param document the document to transcode
	 * @param uri      the uri of the document or null if any
	 * @param output   the ouput where to transcode
	 * @exception TranscoderException if an error occured while transcoding
	 */
	@Override
	protected void transcode(Document document, String uri, TranscoderOutput output) throws TranscoderException {
		super.transcode(document, uri, output);

		// We do this to hide 'ctx' from the SVGAbstractTranscoder
		// otherwise it will dispose of the context before we can
		// print the document.
		theCtx = getBridgeContext();
		setBridgeContext(null);
	}

	/**
	 * Convenience method
	 */
	public void print() throws PrinterException {
		//
		// Now, request the transcoder to actually perform the
		// printing job.
		//
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		PageFormat pageFormat = printerJob.defaultPage();

		//
		// Set the page parameters from the hints
		//
		Paper paper = pageFormat.getPaper();

		Float pageWidth = (Float) hints.get(KEY_PAGE_WIDTH);
		Float pageHeight = (Float) hints.get(KEY_PAGE_HEIGHT);
		if (pageWidth != null) {
			paper.setSize(pageWidth, paper.getHeight());
		}
		if (pageHeight != null) {
			paper.setSize(paper.getWidth(), pageHeight);
		}

		float x = 0, y = 0;
		float width = (float) paper.getWidth();
		float height = (float) paper.getHeight();

		Float leftMargin = (Float) hints.get(KEY_MARGIN_LEFT);
		Float topMargin = (Float) hints.get(KEY_MARGIN_TOP);
		Float rightMargin = (Float) hints.get(KEY_MARGIN_RIGHT);
		Float bottomMargin = (Float) hints.get(KEY_MARGIN_BOTTOM);

		if (leftMargin != null) {
			x = leftMargin;
			width -= leftMargin;
		}
		if (topMargin != null) {
			y = topMargin;
			height -= topMargin;
		}
		if (rightMargin != null) {
			width -= rightMargin;
		}
		if (bottomMargin != null) {
			height -= bottomMargin;
		}

		paper.setImageableArea(x, y, width, height);

		String pageOrientation = (String) hints.get(KEY_PAGE_ORIENTATION);
		if (VALUE_PAGE_ORIENTATION_PORTRAIT.equalsIgnoreCase(pageOrientation)) {
			pageFormat.setOrientation(PageFormat.PORTRAIT);
		} else if (VALUE_PAGE_ORIENTATION_LANDSCAPE.equalsIgnoreCase(pageOrientation)) {
			pageFormat.setOrientation(PageFormat.LANDSCAPE);
		} else if (VALUE_PAGE_ORIENTATION_REVERSE_LANDSCAPE.equalsIgnoreCase(pageOrientation)) {
			pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
		}

		pageFormat.setPaper(paper);
		pageFormat = printerJob.validatePage(pageFormat);

		//
		// If required, pop up a dialog to adjust the page format
		//
		Boolean showPageFormat = (Boolean) hints.get(KEY_SHOW_PAGE_DIALOG);
		if ((showPageFormat != null) && (showPageFormat)) {
			PageFormat tmpPageFormat = printerJob.pageDialog(pageFormat);
			if (tmpPageFormat == pageFormat) {
				// Dialog was cancelled, meaning that the print process should
				// be stopped.
				return;
			}

			pageFormat = tmpPageFormat;
		}

		// Set printable before showing printer dialog so
		// it can update the pageFormat if it wishes...
		printerJob.setPrintable(this, pageFormat);

		//
		// If required, pop up a dialog to select the printer
		//
		Boolean showPrinterDialog;
		showPrinterDialog = (Boolean) hints.get(KEY_SHOW_PRINTER_DIALOG);
		if (showPrinterDialog != null && showPrinterDialog) {
			if (!printerJob.printDialog()) {
				// Dialog was cancelled, meaning that the print process
				// should be stopped.
				return;
			}
		}

		// Print now
		printerJob.print();

	}

	/**
	 * Printable implementation
	 */
	@Override
	public int print(Graphics _g, PageFormat pageFormat, int pageIndex) {
		//
		// On the first page, take a snapshot of the vector of
		// TranscodeInputs.
		//
		if (printedInputs == null) {
			printedInputs = new ArrayList<>(inputs);
		}

		//
		// If we have already printed each page, return
		//
		if (pageIndex >= printedInputs.size()) {
			curIndex = -1;
			if (theCtx != null)
				theCtx.dispose();
			getUserAgent().displayMessage("Done");
			return NO_SUCH_PAGE;
		}

		//
		// Load a new document now if we are printing a new page
		//
		if (curIndex != pageIndex) {
			if (theCtx != null)
				theCtx.dispose();

			// The following call will invoke this class' transcode
			// method which takes a document as an input. That method
			// builds the GVT root tree.{
			try {
				int width = (int) pageFormat.getImageableWidth();
				int height = (int) pageFormat.getImageableHeight();
				setWidth(width);
				setHeight(height);
				super.transcode(printedInputs.get(pageIndex), null);
				curIndex = pageIndex;
			} catch (TranscoderException e) {
				drawError(_g, e);
				return PAGE_EXISTS;
			}
		}

		// Cast to Graphics2D to access Java 2D features
		Graphics2D g = (Graphics2D) _g;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING, RenderingHintsKeyExt.VALUE_TRANSCODING_PRINTING);

		//
		// Compute transform so that the SVG document fits on one page
		//
		AffineTransform t = g.getTransform();
		Shape clip = g.getClip();

		// System.err.println("X/Y: " + pageFormat.getImageableX() + ", " +
		// pageFormat.getImageableY());
		// System.err.println("W/H: " + width + ", " + height);
		// System.err.println("Clip: " + clip.getBounds2D());

		// Offset 0,0 to the start of the imageable Area.
		g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		//
		// Append transform to selected area
		//
		g.transform(getCurrentAOITransform());

		//
		// Delegate rendering to painter
		//
		try {
			getCurrentGVTree().paint(g);
		} catch (Exception e) {
			g.setTransform(t);
			g.setClip(clip);
			drawError(_g, e);
		}

		//
		// Restore transform and clip
		//
		g.setTransform(t);
		g.setClip(clip);

		// g.setPaint(Color.black);
		// g.drawString(uris[pageIndex], 30, 30);

		//
		// Return status indicated that we did paint a page
		//
		return PAGE_EXISTS;
	}

	/**
	 * Sets document size according to the hints. Global variables width and height
	 * are modified.
	 *
	 * @param docWidth  Width of the document.
	 * @param docHeight Height of the document.
	 */
	@Override
	protected void setImageSize(float docWidth, float docHeight) {
		// Check hint to know if scaling is really needed
		Boolean scaleToPage = (Boolean) hints.get(KEY_SCALE_TO_PAGE);
		if (scaleToPage != null && !scaleToPage) {
			float w = docWidth;
			float h = docHeight;
			if (hints.containsKey(KEY_AOI)) {
				Rectangle2D aoi = (Rectangle2D) hints.get(KEY_AOI);
				w = (float) aoi.getWidth();
				h = (float) aoi.getHeight();
			}
			super.setImageSize(w, h);
		}
	}

	/**
	 * Prints an error on the output page
	 */
	private void drawError(Graphics g, Exception e) {
		getUserAgent().displayError(e);
		// Should also probably draw exception on page.
	}

	// --------------------------------------------------------------------
	// Keys definition
	// --------------------------------------------------------------------

	/**
	 * The showPageDialog key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_SHOW_PAGE_DIALOG</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">false</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specifies whether or not the transcoder should pop up a
	 * dialog box for selecting the page format.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_SHOW_PAGE_DIALOG = new BooleanKey();

	/**
	 * The showPrinterDialog key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_SHOW_PAGE_DIALOG</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">false</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specifies whether or not the transcoder should pop up a
	 * dialog box for selecting the printer. If the dialog box is not shown, the
	 * transcoder will use the default printer.</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_SHOW_PRINTER_DIALOG = new BooleanKey();

	/**
	 * The pageWidth key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_PAGE_WIDTH</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Length</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">None</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The width of the print page</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_PAGE_WIDTH = new LengthKey();

	/**
	 * The pageHeight key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_PAGE_HEIGHT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Length</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">none</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The height of the print page</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_PAGE_HEIGHT = new LengthKey();

	/**
	 * The marginTop key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MARGIN_TOP</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Length</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">None</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The print page top margin</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MARGIN_TOP = new LengthKey();

	/**
	 * The marginRight key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MARGIN_RIGHT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Length</TD>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">None</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The print page right margin</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MARGIN_RIGHT = new LengthKey();

	/**
	 * The marginBottom key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MARGIN_BOTTOM</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Length</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">None</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The print page bottom margin</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MARGIN_BOTTOM = new LengthKey();

	/**
	 * The marginLeft key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_MARGIN_LEFT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Length</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">None</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The print page left margin</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_MARGIN_LEFT = new LengthKey();

	/**
	 * The pageOrientation key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_PAGE_ORIENTATION</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">String</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">VALUE_PAGE_ORIENTATION_PORTRAIT</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">The print page's orientation</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_PAGE_ORIENTATION = new StringKey();

	/**
	 * The scaleToPage key.
	 * <table style="border: 0; border-collapse: collapse; padding: 1px;">
	 * <caption></caption>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Key:</th>
	 * <td style="vertical-align: top">KEY_SCALE_TO_PAGE</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Value:</th>
	 * <td style="vertical-align: top">Boolean</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Default:</th>
	 * <td style="vertical-align: top">true</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Required:</th>
	 * <td style="vertical-align: top">No</td>
	 * </tr>
	 * <tr>
	 * <th style="text-align: end; vertical-align: top">Description:</th>
	 * <td style="vertical-align: top">Specifies whether or not the SVG images are scaled to fit
	 * into the printed page</td>
	 * </tr>
	 * </table>
	 */
	public static final TranscodingHints.Key KEY_SCALE_TO_PAGE = new BooleanKey();

	public static final String USAGE = "java io.sf.carte.echosvg.transcoder.print.PrintTranscoder <svgFileToPrint>";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println(USAGE);
			System.exit(0);
		}

		//
		// Builds a PrintTranscoder
		//
		PrintTranscoder transcoder = new PrintTranscoder();

		//
		// Set the hints, from the command line arguments
		//

		// Language
		setTranscoderFloatHint(transcoder, KEY_LANGUAGE_STR, KEY_LANGUAGE);

		// User stylesheet
		setTranscoderFloatHint(transcoder, KEY_USER_STYLESHEET_URI_STR, KEY_USER_STYLESHEET_URI);

		// Scale to page
		setTranscoderBooleanHint(transcoder, KEY_SCALE_TO_PAGE_STR, KEY_SCALE_TO_PAGE);

		// AOI
		setTranscoderRectangleHint(transcoder, KEY_AOI_STR, KEY_AOI);

		// Image size
		setTranscoderFloatHint(transcoder, KEY_WIDTH_STR, KEY_WIDTH);
		setTranscoderFloatHint(transcoder, KEY_HEIGHT_STR, KEY_HEIGHT);

		// Pixel to millimeter
		setTranscoderFloatHint(transcoder, KEY_PIXEL_TO_MM_STR, KEY_PIXEL_UNIT_TO_MILLIMETER);

		// Page orientation
		setTranscoderStringHint(transcoder, KEY_PAGE_ORIENTATION_STR, KEY_PAGE_ORIENTATION);

		// Page size
		setTranscoderFloatHint(transcoder, KEY_PAGE_WIDTH_STR, KEY_PAGE_WIDTH);
		setTranscoderFloatHint(transcoder, KEY_PAGE_HEIGHT_STR, KEY_PAGE_HEIGHT);

		// Margins
		setTranscoderFloatHint(transcoder, KEY_MARGIN_TOP_STR, KEY_MARGIN_TOP);
		setTranscoderFloatHint(transcoder, KEY_MARGIN_RIGHT_STR, KEY_MARGIN_RIGHT);
		setTranscoderFloatHint(transcoder, KEY_MARGIN_BOTTOM_STR, KEY_MARGIN_BOTTOM);
		setTranscoderFloatHint(transcoder, KEY_MARGIN_LEFT_STR, KEY_MARGIN_LEFT);

		// Dialog options
		setTranscoderBooleanHint(transcoder, KEY_SHOW_PAGE_DIALOG_STR, KEY_SHOW_PAGE_DIALOG);

		setTranscoderBooleanHint(transcoder, KEY_SHOW_PRINTER_DIALOG_STR, KEY_SHOW_PRINTER_DIALOG);

		//
		// First, request the transcoder to transcode
		// each of the input files
		//
		for (String arg : args) {
			transcoder.transcode(new TranscoderInput(new File(arg).toURI().toURL().toString()), null);
		}

		//
		// Now, print...
		//
		transcoder.print();

		System.exit(0);
	}

	public static void setTranscoderFloatHint(Transcoder transcoder, String property, TranscodingHints.Key key) {
		String str = System.getProperty(property);
		if (str != null) {
			try {
				Float value = Float.parseFloat(str);
				transcoder.addTranscodingHint(key, value);
			} catch (NumberFormatException e) {
				handleValueError(property, str);
			}
		}
	}

	public static void setTranscoderRectangleHint(Transcoder transcoder, String property, TranscodingHints.Key key) {
		String str = System.getProperty(property);
		if (str != null) {
			StringTokenizer st = new StringTokenizer(str, " ,");
			if (st.countTokens() != 4) {
				handleValueError(property, str);
			}

			try {
				String x = st.nextToken();
				String y = st.nextToken();
				String width = st.nextToken();
				String height = st.nextToken();
				Rectangle2D r = new Rectangle2D.Float(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(width),
						Float.parseFloat(height));
				transcoder.addTranscodingHint(key, r);
			} catch (NumberFormatException e) {
				handleValueError(property, str);
			}
		}
	}

	public static void setTranscoderBooleanHint(Transcoder transcoder, String property, TranscodingHints.Key key) {
		String str = System.getProperty(property);
		if (str != null) {
			Boolean value = "true".equalsIgnoreCase(str) ? Boolean.TRUE : Boolean.FALSE;
			transcoder.addTranscodingHint(key, value);
		}
	}

	public static void setTranscoderStringHint(Transcoder transcoder, String property, TranscodingHints.Key key) {
		String str = System.getProperty(property);
		if (str != null) {
			transcoder.addTranscodingHint(key, str);
		}
	}

	public static void handleValueError(String property, String value) {
		System.err.println("Invalid " + property + " value : " + value);
		System.exit(1);
	}
}
