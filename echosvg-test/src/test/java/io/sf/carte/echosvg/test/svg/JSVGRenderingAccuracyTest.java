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
package io.sf.carte.echosvg.test.svg;

import static org.junit.Assert.fail;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.gvt.Overlay;
import io.sf.carte.echosvg.swing.test.JSVGCanvasHandler;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JSVGRenderingAccuracyTest extends RenderingTest implements JSVGCanvasHandler.Delegate {

	public static String fmt(String key, Object[] args) {
		return Messages.formatMessage(key, args);
	}

	/**
	 * For subclasses
	 */
	public JSVGRenderingAccuracyTest() {
	}

	private URL srcURL;
	private FileOutputStream fos;
	private boolean done;
	private JSVGCanvasHandler handler = null;

	protected JSVGCanvasHandler createCanvasHandler() {
		return new JSVGCanvasHandler(this, this);
	}

	@Override
	protected void encode(URL srcURL, FileOutputStream fos) throws IOException {
		this.srcURL = srcURL;
		this.fos = fos;

		handler = createCanvasHandler();
		done = false;
		handler.runCanvas(srcURL.toString());

		handler = null;
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
	public void canvasDone(JSVGCanvas canvas) throws IOException {
		synchronized (this) {
			done = true;

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
		}
	}

	@Override
	public void failure(String cause) {
		synchronized (this) {
			done = true;
			fail(cause);
		}
	}

	private static BufferedImage copyImage(BufferedImage bi) {
		// Copy off the image just rendered.
		BufferedImage ret;
		ret = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		bi.copyData(ret.getRaster());
		return ret;
	}
}
