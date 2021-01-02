/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.test.svg;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.JSVGCanvasHandler;
import io.sf.carte.echosvg.swing.gvt.Overlay;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JSVGRenderingAccuracyTest extends SamplesRenderingTest implements JSVGCanvasHandler.Delegate {

	/**
	 * Error when canvas can't peform render update SVG file. {0} The file/url that
	 * could not be updated..
	 */
	public static final String ERROR_SAVE_FAILED = "JSVGRenderingAccuracyTest.message.error.save.failed";

	public static String fmt(String key, Object[] args) {
		return Messages.formatMessage(key, args);
	}

	/**
	 * For subclasses
	 */
	public JSVGRenderingAccuracyTest() {
	}

	protected URL srcURL;
	protected FileOutputStream fos;
	protected TestReport failReport = null;
	protected boolean done;
	protected JSVGCanvasHandler handler = null;

	public JSVGCanvasHandler createCanvasHandler() {
		return new JSVGCanvasHandler(this, this);
	}

	@Override
	public TestReport encode(URL srcURL, FileOutputStream fos) {
		this.srcURL = srcURL;
		this.fos = fos;

		handler = createCanvasHandler();
		done = false;
		handler.runCanvas(srcURL.toString());

		handler = null;

		if (failReport != null)
			return failReport;

		DefaultTestReport report = new DefaultTestReport(this);
		report.setPassed(true);
		return report;
	}

	public void scriptDone() {
		synchronized (this) {
			done = true;
			handler.scriptDone();
		}
	}

	/* JSVGCanvasHandler.Delegate Interface */
	@Override
	public boolean canvasInit(JSVGCanvas canvas) {
		canvas.setURI(srcURL.toString());
		return true;
	}

	@Override
	public void canvasLoaded(JSVGCanvas canvas) {
	}

	@Override
	public void canvasRendered(JSVGCanvas canvas) {
	}

	@Override
	public boolean canvasUpdated(JSVGCanvas canvas) {
		synchronized (this) {
			return done;
		}
	}

	@Override
	public void canvasDone(JSVGCanvas canvas) {
		synchronized (this) {
			done = true;
			if (failReport != null)
				return;

			try {
				// Get the base image
				BufferedImage theImage = copyImage(canvas.getOffScreen());

				// Capture the overlays
				List<Overlay> overlays = canvas.getOverlays();

				// Paint the overlays
				Graphics2D g = theImage.createGraphics();
				for (Overlay overlay : overlays) {
					overlay.paint(g);
				}

				saveImage(theImage, fos);
			} catch (IOException ioe) {
				StringWriter trace = new StringWriter();
				ioe.printStackTrace(new PrintWriter(trace));
				DefaultTestReport report = new DefaultTestReport(this);
				report.setErrorCode(ERROR_SAVE_FAILED);
				report.setDescription(
						new TestReport.Entry[] { new TestReport.Entry(fmt(ENTRY_KEY_ERROR_DESCRIPTION, null),
								fmt(ERROR_SAVE_FAILED, new Object[] { srcURL.toString(), trace.toString() })) });
				report.setPassed(false);
				failReport = report;
			}
		}
	}

	@Override
	public void failure(TestReport report) {
		synchronized (this) {
			done = true;
			failReport = report;
		}
	}

	public static BufferedImage copyImage(BufferedImage bi) {
		// Copy off the image just rendered.
		BufferedImage ret;
		ret = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		bi.copyData(ret.getRaster());
		return ret;
	}
}
