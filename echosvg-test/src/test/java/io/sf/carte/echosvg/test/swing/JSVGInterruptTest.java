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
import java.awt.geom.AffineTransform;
import java.io.IOException;

import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.gvt.GVTTreeRendererEvent;
import io.sf.carte.echosvg.swing.gvt.GVTTreeRendererListener;
import io.sf.carte.echosvg.swing.svg.GVTTreeBuilderEvent;
import io.sf.carte.echosvg.swing.svg.GVTTreeBuilderListener;
import io.sf.carte.echosvg.swing.svg.SVGDocumentLoaderEvent;
import io.sf.carte.echosvg.swing.svg.SVGDocumentLoaderListener;
import io.sf.carte.echosvg.swing.svg.SVGLoadEventDispatcherEvent;
import io.sf.carte.echosvg.swing.svg.SVGLoadEventDispatcherListener;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JSVGInterruptTest extends JSVGMemoryLeakTest {

	@Override
	public String getName() {
		return "JSVGInterruptTest." + getFilename();
	}

	public JSVGInterruptTest() {
	}

	/* JSVGCanvasHandler.Delegate Interface */
	private Runnable stopRunnable;

	private int state = 0;
	private MyLoaderListener loadListener = new MyLoaderListener();
	private MyBuildListener buildListener = new MyBuildListener();
	private MyOnloadListener onloadListener = new MyOnloadListener();
	private MyRenderListener renderListener = new MyRenderListener();

	private DelayRunnable stopper = null;

	private static final int COMPLETE = 1;
	private static final int CANCELLED = 2;
	private static final int FAILED = 4;
	private static final int MAX_WAIT = 40000;

	@Override
	public JSVGCanvasHandler createHandler() {
		return new JSVGCanvasHandler(this, this) {
			@Override
			public void runCanvas(String desc) throws IOException {
				this.desc = desc;
				setupCanvas();

				if (abort)
					return;
				try {
					synchronized (renderMonitor) {
						delegate.canvasInit(canvas);
						if (abort)
							return;

						while (!done) {
							checkRender();
							if (abort)
								return;
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					delegate.canvasDone(canvas);
					dispose();
				}
			}

			@Override
			public void checkSomething(Object monitor, String errorCode) {
				synchronized (monitor) {
					try {
						monitor.wait();
					} catch (InterruptedException ie) {
						/* nothing */ }
				}
			}
		};
	}

	@Override
	public boolean canvasInit(final JSVGCanvas canvas) {
		// System.err.println("In Init");
		setCanvas(canvas);
		setJFrame(getCanvasHandler().getFrame());
		registerObjectDesc(canvas, "JSVGCanvas");
		registerObjectDesc(getCanvasHandler().getFrame(), "JFrame");

		stopRunnable = new StopRunnable(canvas);

		tweakIt(canvas, getURI());

		return false;
	}

	@Override
	public void canvasDone(JSVGCanvas canvas) {
		loadListener = null;
		buildListener = null;
		renderListener = null;
		onloadListener = null;
		stopper = null;
		stopRunnable = null;
	}

	public void tweakIt(final JSVGCanvas canvas, final String uri) {
		Thread t = new Thread() {
			@Override
			public void run() {
				int state;
				Runnable setURI = new Runnable() {
					@Override
					public void run() {
						canvas.setURI(uri);
					}
				};
				//System.err.println("Starting Load Tweak");
				canvas.addSVGDocumentLoaderListener(loadListener);
				state = doTweak(setURI, loadListener);
				canvas.removeSVGDocumentLoaderListener(loadListener);
				//System.err.println("Finished Load Tweak: " + state);

				final SVGDocument doc = canvas.getSVGDocument();
				Runnable setDoc = new Runnable() {
					@Override
					public void run() {
						canvas.setSVGDocument(doc);
					}
				};
				//System.err.println("Starting setDoc Tweak");
				canvas.addGVTTreeBuilderListener(buildListener);
				state = doTweak(setDoc, buildListener);
				canvas.removeGVTTreeBuilderListener(buildListener);
				//System.err.println("Finished setDoc Tweak: " + state);

				if (canvas.isDynamic()) {
					//System.err.println("Starting onload Tweak");
					canvas.addSVGLoadEventDispatcherListener(onloadListener);
					state = doTweak(setDoc, onloadListener);
					canvas.removeSVGLoadEventDispatcherListener(onloadListener);
					//System.err.println("Finished onload Tweak: " + state);
				}

				Runnable setTrans = new Runnable() {
					@Override
					public void run() {
						canvas.setRenderingTransform(new AffineTransform(), true);
					}
				};
				//System.err.println("Starting render Tweak");
				canvas.addGVTTreeRendererListener(renderListener);
				state = doTweak(setTrans, renderListener);
				canvas.removeGVTTreeRendererListener(renderListener);
				//System.err.println("Finished render Tweak: " + state);

				getCanvasHandler().scriptDone();
			}
		};
		t.setDaemon(true);
		t.start();
	}

	private int doTweak(Runnable r, SetDelayable delayable) {
		synchronized (JSVGInterruptTest.this) {
			int delay = 0;
			int delayInc = 3;
			int delayIncInc = 4;
			int ret = 0;
			state = 0;
			while ((state & (COMPLETE | FAILED)) == 0) {
				ret |= state;
				state = 0;
				//System.err.println("Tweaking: " + delay);
				delayable.setDelay(delay);
				EventQueue.invokeLater(r);

				long start = System.currentTimeMillis();
				long end = start + MAX_WAIT;
				long curr = start;
				while ((state == 0) && (curr < end)) {
					// No 'complete' event generated yet and
					// Still willing to wait a bit...
					try {
						JSVGInterruptTest.this.wait(end - curr);
					} catch (InterruptedException ie) {
					}
					curr = System.currentTimeMillis();
				}
				if (state == 0) {
					throw new IllegalArgumentException("Timed out - proabably indicates failure");
				}
				delay += delayInc + (int) (curr - start - delay) / 8;
				delayInc += delayIncInc;
			}
			ret |= state;
			return ret;
		}
	}

	private void triggerStopProcessing(int delay) {
		stopper = new DelayRunnable(delay, stopRunnable);
		stopper.start();
	}

	public boolean stopStopper() {
		return stopper.abort();
	}

	interface SetDelayable {

		void setDelay(int delay);

	}

	private class MyLoaderListener implements SVGDocumentLoaderListener, SetDelayable {

		int delay = 0;

		@Override
		public void setDelay(int delay) {
			this.delay = delay;
		}

		@Override
		public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
			triggerStopProcessing(delay);
		}

		@Override
		public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
			stopStopper();
			synchronized (JSVGInterruptTest.this) {
				state |= COMPLETE;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
			synchronized (JSVGInterruptTest.this) {
				state |= CANCELLED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
			synchronized (JSVGInterruptTest.this) {
				state |= FAILED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

	}

	private class MyBuildListener implements GVTTreeBuilderListener, SetDelayable {

		int delay = 0;

		@Override
		public void setDelay(int delay) {
			this.delay = delay;
		}

		@Override
		public void gvtBuildStarted(GVTTreeBuilderEvent e) {
			// System.err.println("Build Start: " + e.getSource());
			triggerStopProcessing(delay);
		}

		@Override
		public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
			stopStopper();
			// System.err.println("Build Complete: " + e.getSource());
			synchronized (JSVGInterruptTest.this) {
				state |= COMPLETE;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
			// System.err.println("Build Cancelled");
			synchronized (JSVGInterruptTest.this) {
				state |= CANCELLED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void gvtBuildFailed(GVTTreeBuilderEvent e) {
			// System.err.println("Build Failed");
			synchronized (JSVGInterruptTest.this) {
				state |= FAILED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

	}

	private class MyOnloadListener implements SVGLoadEventDispatcherListener, SetDelayable {

		int delay = 0;

		@Override
		public void setDelay(int delay) {
			this.delay = delay;
		}

		@Override
		public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent e) {
			// System.err.println("Onload Start: " + e.getSource());
			triggerStopProcessing(delay);
		}

		@Override
		public void svgLoadEventDispatchCompleted(SVGLoadEventDispatcherEvent e) {
			stopStopper();
			// System.err.println("Onload Complete: " + e.getSource());
			synchronized (JSVGInterruptTest.this) {
				state |= COMPLETE;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void svgLoadEventDispatchCancelled(SVGLoadEventDispatcherEvent e) {
			// System.err.println("Onload Cancelled");
			synchronized (JSVGInterruptTest.this) {
				state |= CANCELLED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void svgLoadEventDispatchFailed(SVGLoadEventDispatcherEvent e) {
			// System.err.println("Onload Failed");
			synchronized (JSVGInterruptTest.this) {
				state |= FAILED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

	}

	private class MyRenderListener implements GVTTreeRendererListener, SetDelayable {

		int delay = 0;

		@Override
		public void setDelay(int delay) {
			this.delay = delay;
		}

		@Override
		public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
			// System.err.println("Render Prep");
			triggerStopProcessing(delay);
		}

		@Override
		public void gvtRenderingStarted(GVTTreeRendererEvent e) {
			// System.err.println("Render Start");
		}

		@Override
		public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
			stopStopper();
			// System.err.println("Render Complete");
			synchronized (JSVGInterruptTest.this) {
				state |= COMPLETE;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
			// System.err.println("Render Cancelled");
			synchronized (JSVGInterruptTest.this) {
				state |= CANCELLED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

		@Override
		public void gvtRenderingFailed(GVTTreeRendererEvent e) {
			// System.err.println("Render Failed");
			synchronized (JSVGInterruptTest.this) {
				state |= FAILED;
				JSVGInterruptTest.this.notifyAll();
			}
		}

	}

	private static class StopRunnable implements Runnable {

		JSVGCanvas canvas;

		public StopRunnable(JSVGCanvas canvas) {
			this.canvas = canvas;
		}

		@Override
		public void run() {
			if (EventQueue.isDispatchThread())
				canvas.stopProcessing();
			else
				EventQueue.invokeLater(this);
		}

	}

	/**
	 * a Runnable is run after the given delay has elapsed. A call to abort() can
	 * <i>prevent</i> the start of the Runable before it is started - it does not
	 * abort after it started.
	 */
	static class DelayRunnable extends Thread {

		/**
		 * delay in milliSeconds - must not change after creation.
		 */
		private final int delay;

		/**
		 * the Runnable to start - must not change after creation.
		 */
		private final Runnable r;

		volatile boolean stop = false;
		volatile boolean complete = false;

		/**
		 * @param delay to wait before r is started in milliSeconds
		 * @param r     a Runnable to start after delay
		 */
		DelayRunnable(int delay, Runnable r) {

			if (delay < 0) {
				throw new IllegalArgumentException("delay must be >= 0 ! is:" + delay);
			}
			if (r == null) {
				throw new IllegalArgumentException("Runnable must not be null!");
			}

			this.delay = delay;
			this.r = r;
			setDaemon(true);
		}

		public boolean getComplete() {
			return complete;
		}

		public boolean abort() {
			synchronized (this) {
				if (complete)
					return false;
				stop = true;
				return true;
			}
		}

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			long end = start + delay;
			long curr = start;
			while (curr < end) {
				try {
					Thread.sleep(end - curr);
				} catch (InterruptedException ie) {
				}
				curr = System.currentTimeMillis();
			}
			synchronized (this) {
				if (stop)
					return;
				r.run();
				complete = true;
			}
		}

	}

}
