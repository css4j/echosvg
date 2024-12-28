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
package io.sf.carte.echosvg.test.swing;

import java.awt.EventQueue;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.svg.JSVGComponent;
import io.sf.carte.echosvg.test.misc.TestLocations;

/**
 * Test setDocument on JSVGComponent with non-EchoSVG SVGOMDocument.
 *
 * This test constructs a generic Document with SVG content then it ensures that
 * when this is passed to JSVGComponet.setDocument it is properly imported to an
 * SVGOMDocument and rendered from there.
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NullSetSVGDocumentTest extends JSVGMemoryLeakTest {

	public NullSetSVGDocumentTest() {
	}

	private static final String TEST_NON_NULL_URI = TestLocations.PROJECT_ROOT_URL + "samples/anne.svg";

	@Override
	public String getName() {
		return "NullSetSVGDocumentTest." + getFilename();
	}

	@Override
	public JSVGCanvasHandler createHandler() {
		return new JSVGCanvasHandler(this, this) {
			@Override
			public JSVGCanvas createCanvas() {
				return new JSVGCanvas() {
					private static final long serialVersionUID = 1L;
					@Override
					protected void installSVGDocument(SVGDocument doc) {
						super.installSVGDocument(doc);
						if (doc != null)
							return;
						getCanvasHandler().scriptDone();
					}
				};
			}
		};
	}

	public Runnable getRunnable(final JSVGCanvas canvas) {
		return new Runnable() {
			@Override
			public void run() {
				canvas.setSVGDocument(null);
			}
		};
	}

	/* JSVGCanvasHandler.Delegate Interface */
	@Override
	public boolean canvasInit(JSVGCanvas canvas) {
		setCanvas(canvas);
		setJFrame(getCanvasHandler().getFrame());

		canvas.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
		canvas.setURI(TEST_NON_NULL_URI);

		registerObjectDesc(canvas, "JSVGCanvas");
		registerObjectDesc(getCanvasHandler().getFrame(), "JFrame");
		return true; // We did trigger a load event.
	}

	@Override
	public void canvasRendered(JSVGCanvas canvas) {
		super.canvasRendered(canvas);
		try {
			EventQueue.invokeAndWait(getRunnable(canvas));
		} catch (Throwable t) {
			StringWriter trace = new StringWriter();
			t.printStackTrace(new PrintWriter(trace));
			setFailReport(trace.toString());
		}
	}

	@Override
	public boolean canvasUpdated(JSVGCanvas canvas) {
		return true;
	}

	@Override
	public void canvasDone(JSVGCanvas canvas) {
		synchronized (this) {
			// Check that the original SVG
			// Document and GVT tree are cleared.
			checkObjects(new String[] { "SVGDoc", "GVT", "updateManager" });

			if (canvas.getOffScreen() == null)
				return;
			// Canvas not cleared
			setFailReport("Display image was not cleared from canvas component");
			return;
		}
	}

}
