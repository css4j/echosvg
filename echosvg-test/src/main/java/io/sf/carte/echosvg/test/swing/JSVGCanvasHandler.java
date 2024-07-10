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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import io.sf.carte.echosvg.bridge.ScriptingEnvironment;
import io.sf.carte.echosvg.bridge.UpdateManager;
import io.sf.carte.echosvg.bridge.UpdateManagerEvent;
import io.sf.carte.echosvg.bridge.UpdateManagerListener;
import io.sf.carte.echosvg.script.Interpreter;
import io.sf.carte.echosvg.script.InterpreterException;
import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.gvt.GVTTreeRendererAdapter;
import io.sf.carte.echosvg.swing.gvt.GVTTreeRendererEvent;
import io.sf.carte.echosvg.swing.svg.SVGDocumentLoaderAdapter;
import io.sf.carte.echosvg.swing.svg.SVGDocumentLoaderEvent;
import io.sf.carte.echosvg.swing.svg.SVGLoadEventDispatcher;
import io.sf.carte.echosvg.swing.svg.SVGLoadEventDispatcherAdapter;
import io.sf.carte.echosvg.swing.svg.SVGLoadEventDispatcherEvent;
import io.sf.carte.echosvg.util.RunnableQueue;

/**
 * JSVGCanvasHandler
 * 
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JSVGCanvasHandler {

	public interface Delegate {

		String getName();

		// Returns true if a load event was triggered. In this case
		// the handler will wait for the load event to complete/fail.
		boolean canvasInit(JSVGCanvas canvas);

		void canvasLoaded(JSVGCanvas canvas);

		void canvasRendered(JSVGCanvas canvas);

		boolean canvasUpdated(JSVGCanvas canvas);

		void canvasDone(JSVGCanvas canvas) throws IOException;

		void failure(String cause);

	}

	public static final String REGARD_TEST_INSTANCE = "regardTestInstance";
	public static final String REGARD_START_SCRIPT = "try { regardStart(); } catch (er) {}";

	/**
	 * Error when canvas can't load SVG file. {0} The file/url that could not be
	 * loaded.
	 */
	public static final String ERROR_CANNOT_LOAD_SVG = "JSVGCanvasHandler.message.error.could.not.load.svg";

	/**
	 * Error when canvas can't render SVG file. {0} The file/url that could not be
	 * rendered.
	 */
	public static final String ERROR_SVG_RENDER_FAILED = "JSVGCanvasHandler.message.error.svg.render.failed";

	/**
	 * Error when canvas can't peform render update SVG file. {0} The file/url that
	 * could not be updated..
	 */
	public static final String ERROR_SVG_UPDATE_FAILED = "JSVGCanvasHandler.message.error.svg.update.failed";

	/**
	 * Entry describing the error
	 */
	public static final String ENTRY_KEY_ERROR_DESCRIPTION = "JSVGCanvasHandler.entry.key.error.description";

	public static String fmt(String key, Object[] args) {
		return TestMessages.formatMessage(key, args);
	}

	JFrame frame = null;
	JSVGCanvas canvas = null;
	WeakReference<UpdateManager> updateManager = null;
	WindowListener wl = null;
	InitialRenderListener irl = null;
	LoadListener ll = null;
	SVGLoadEventListener sll = null;
	UpdateRenderListener url = null;

	boolean failed;
	boolean abort = false;
	boolean done = false;

	final Object loadMonitor = new Object();

	final Object renderMonitor = new Object();

	Delegate delegate;
	Object host;
	String desc;

	public JSVGCanvasHandler(Object host, Delegate delegate) {
		this.host = host;
		this.delegate = delegate;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JSVGCanvas getCanvas() {
		return canvas;
	}

	public JSVGCanvas createCanvas() {
		return new JSVGCanvas();
	}

	public void runCanvas(String desc) throws IOException {
		this.desc = desc;

		setupCanvas();

		if (abort)
			return;

		try {
			synchronized (renderMonitor) {
				synchronized (loadMonitor) {
					if (delegate.canvasInit(canvas)) {
						checkLoad();
					}
				}

				if (abort)
					return;

				checkRender();
				if (abort)
					return;

				if (updateManager == null || updateManager.get() == null)
					return;

				while (!done) {
					checkUpdate();
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

	public void setupCanvas() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					frame = new JFrame(delegate.getName());
					canvas = createCanvas();
					canvas.setPreferredSize(new Dimension(450, 500));
					frame.getContentPane().add(canvas);
					frame.pack();
					wl = new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							synchronized (loadMonitor) {
								abort = true;
								loadMonitor.notifyAll();
							}
							synchronized (renderMonitor) {
								abort = true;
								renderMonitor.notifyAll();
							}
						}
					};
					frame.addWindowListener(wl);
					frame.setVisible(true);

					irl = new InitialRenderListener();
					canvas.addGVTTreeRendererListener(irl);
					ll = new LoadListener();
					canvas.addSVGDocumentLoaderListener(ll);
					sll = new SVGLoadEventListener();
					canvas.addSVGLoadEventDispatcherListener(sll);

				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void scriptDone() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				UpdateManager um = getUpdateManager();
				if (um != null)
					um.forceRepaint();
				synchronized (renderMonitor) {
					done = true;
					failed = false;
					renderMonitor.notifyAll();
				}
			}
		};
		UpdateManager um = getUpdateManager();
		if ((um == null) || (!um.isRunning())) {
			// Don't run it in this thread or we deadlock the event queue.
			Thread t = new Thread(r);
			t.start();
		} else {
			um.getUpdateRunnableQueue().invokeLater(r);
		}
	}

	public void dispose() {
		if (frame != null) {
			frame.removeWindowListener(wl);
			frame.setVisible(false);
		}
		wl = null;
		if (canvas != null) {
			canvas.removeGVTTreeRendererListener(irl);
			irl = null;
			canvas.removeSVGDocumentLoaderListener(ll);
			ll = null;
			canvas.removeUpdateManagerListener(url);
			url = null;
		}
		updateManager = null;
		canvas = null;
		frame = null;
	}

	public void checkSomething(Object monitor, String errorCode) {
		synchronized (monitor) {
			failed = true;
			try {
				monitor.wait();
			} catch (InterruptedException ie) {
				/* nothing */ }
			if (abort || failed) {
				delegate.failure(errorCode + " " + desc);
				done = true;
				return;
			}
		}
	}

	public void checkLoad() {
		checkSomething(loadMonitor, ERROR_CANNOT_LOAD_SVG);
		delegate.canvasLoaded(canvas);
	}

	public void checkRender() throws InvocationTargetException, InterruptedException {
		checkSomething(renderMonitor, ERROR_SVG_RENDER_FAILED);
		delegate.canvasRendered(canvas);
	}

	public void checkUpdate() {
		checkSomething(renderMonitor, ERROR_SVG_UPDATE_FAILED);
		if (!done)
			done = delegate.canvasUpdated(canvas);
	}

	public void bindHost() {
		UpdateManager um = getUpdateManager();
		RunnableQueue rq;
		rq = um.getUpdateRunnableQueue();
		rq.invokeLater(new Runnable() {
			UpdateManager um = getUpdateManager();

			@Override
			public void run() {
				ScriptingEnvironment scriptEnv;
				scriptEnv = um.getScriptingEnvironment();
				Interpreter interp;
				interp = scriptEnv.getInterpreter();
				interp.bindObject(REGARD_TEST_INSTANCE, host);
				try {
					interp.evaluate(REGARD_START_SCRIPT);
				} catch (InterpreterException ie) {
					// Could not wait if no start script.
				}
			}
		});
	}

	protected UpdateManager getUpdateManager() {
		if (updateManager != null) {
			return updateManager.get();
		}
		return null;
	}

	class UpdateRenderListener implements UpdateManagerListener {

		@Override
		public void updateCompleted(UpdateManagerEvent e) {
			synchronized (renderMonitor) {
				failed = false;
				renderMonitor.notifyAll();
			}
		}

		@Override
		public void updateFailed(UpdateManagerEvent e) {
			synchronized (renderMonitor) {
				renderMonitor.notifyAll();
			}
		}

		@Override
		public void managerStarted(UpdateManagerEvent e) {
			bindHost();
		}

		@Override
		public void managerSuspended(UpdateManagerEvent e) {
		}

		@Override
		public void managerResumed(UpdateManagerEvent e) {
		}

		@Override
		public void managerStopped(UpdateManagerEvent e) {
		}

		@Override
		public void updateStarted(UpdateManagerEvent e) {
		}

	}

	class InitialRenderListener extends GVTTreeRendererAdapter {

		@Override
		public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
			synchronized (renderMonitor) {
				failed = false;
				renderMonitor.notifyAll();
			}
		}

		@Override
		public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
			synchronized (renderMonitor) {
				renderMonitor.notifyAll();
			}
		}

		@Override
		public void gvtRenderingFailed(GVTTreeRendererEvent e) {
			synchronized (renderMonitor) {
				renderMonitor.notifyAll();
			}
		}

	}

	class LoadListener extends SVGDocumentLoaderAdapter {

		@Override
		public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
			synchronized (loadMonitor) {
				failed = false;
				loadMonitor.notifyAll();
			}
		}

		@Override
		public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
			synchronized (loadMonitor) {
				loadMonitor.notifyAll();
			}
		}

		@Override
		public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
			synchronized (loadMonitor) {
				loadMonitor.notifyAll();
			}
		}

	}

	class SVGLoadEventListener extends SVGLoadEventDispatcherAdapter {

		@Override
		public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent e) {
			SVGLoadEventDispatcher dispatcher;
			dispatcher = (SVGLoadEventDispatcher) e.getSource();
			UpdateManager um = dispatcher.getUpdateManager();
			updateManager = new WeakReference<>(um);
			url = new UpdateRenderListener();
			um.addUpdateManagerListener(url);
		}

	}

}
