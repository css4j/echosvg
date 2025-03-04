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
package io.sf.carte.echosvg.swing.svg;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.InterruptedBridgeException;
import io.sf.carte.echosvg.bridge.UpdateManager;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.util.EventDispatcher;
import io.sf.carte.echosvg.util.EventDispatcher.Dispatcher;
import io.sf.carte.echosvg.util.HaltingThread;

/**
 * This class dispatches the SVGLoadEvent event on a SVG document.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGLoadEventDispatcher extends HaltingThread {

	/**
	 * The SVG document to give to the bridge.
	 */
	protected SVGDocument svgDocument;

	/**
	 * The root graphics node.
	 */
	protected GraphicsNode root;

	/**
	 * The bridge context to use.
	 */
	protected BridgeContext bridgeContext;

	/**
	 * The update manager.
	 */
	protected UpdateManager updateManager;

	/**
	 * The listeners.
	 */
	protected List<Object> listeners = Collections.synchronizedList(new LinkedList<>());

	/**
	 * The exception thrown.
	 */
	protected Exception exception;

	/**
	 * Creates a new SVGLoadEventDispatcher.
	 */
	public SVGLoadEventDispatcher(GraphicsNode gn, SVGDocument doc, BridgeContext bc, UpdateManager um) {
		svgDocument = doc;
		root = gn;
		bridgeContext = bc;
		updateManager = um;
	}

	/**
	 * Runs the dispatcher.
	 */
	@Override
	public void run() {
		SVGLoadEventDispatcherEvent ev;
		ev = new SVGLoadEventDispatcherEvent(this, root);
		try {
			fireEvent(startedDispatcher, ev);

			if (isHalted()) {
				fireEvent(cancelledDispatcher, ev);
				return;
			}

			updateManager.dispatchSVGLoadEvent();

			if (isHalted()) {
				fireEvent(cancelledDispatcher, ev);
				return;
			}

			fireEvent(completedDispatcher, ev);
		} catch (InterruptedException | InterruptedBridgeException e) {
			fireEvent(cancelledDispatcher, ev);
		} catch (Exception e) {
			exception = e;
			fireEvent(failedDispatcher, ev);
		} catch (ThreadDeath td) {
			exception = new Exception(td.getMessage());
			fireEvent(failedDispatcher, ev);
			throw td;
		} catch (Throwable t) {
			t.printStackTrace();
			exception = new Exception(t.getMessage());
			fireEvent(failedDispatcher, ev);
		}
	}

	/**
	 * Returns the update manager.
	 */
	public UpdateManager getUpdateManager() {
		return updateManager;
	}

	/**
	 * Returns the exception, if any occured.
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * Adds a SVGLoadEventDispatcherListener to this SVGLoadEventDispatcher.
	 */
	public void addSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener l) {
		listeners.add(l);
	}

	/**
	 * Removes a SVGLoadEventDispatcherListener from this SVGLoadEventDispatcher.
	 */
	public void removeSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener l) {
		listeners.remove(l);
	}

	public void fireEvent(Dispatcher dispatcher, Object event) {
		EventDispatcher.fireEvent(dispatcher, listeners, event, true);
	}

	static Dispatcher startedDispatcher = new Dispatcher() {
		@Override
		public void dispatch(Object listener, Object event) {
			((SVGLoadEventDispatcherListener) listener)
					.svgLoadEventDispatchStarted((SVGLoadEventDispatcherEvent) event);
		}
	};

	static Dispatcher completedDispatcher = new Dispatcher() {
		@Override
		public void dispatch(Object listener, Object event) {
			((SVGLoadEventDispatcherListener) listener)
					.svgLoadEventDispatchCompleted((SVGLoadEventDispatcherEvent) event);
		}
	};

	static Dispatcher cancelledDispatcher = new Dispatcher() {
		@Override
		public void dispatch(Object listener, Object event) {
			((SVGLoadEventDispatcherListener) listener)
					.svgLoadEventDispatchCancelled((SVGLoadEventDispatcherEvent) event);
		}
	};

	static Dispatcher failedDispatcher = new Dispatcher() {
		@Override
		public void dispatch(Object listener, Object event) {
			((SVGLoadEventDispatcherListener) listener).svgLoadEventDispatchFailed((SVGLoadEventDispatcherEvent) event);
		}
	};

}
