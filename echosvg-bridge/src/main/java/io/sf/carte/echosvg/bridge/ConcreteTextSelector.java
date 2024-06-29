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
package io.sf.carte.echosvg.bridge;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.gvt.RootGraphicsNode;
import io.sf.carte.echosvg.gvt.Selectable;
import io.sf.carte.echosvg.gvt.Selector;
import io.sf.carte.echosvg.gvt.event.GraphicsNodeChangeEvent;
import io.sf.carte.echosvg.gvt.event.GraphicsNodeEvent;
import io.sf.carte.echosvg.gvt.event.GraphicsNodeKeyEvent;
import io.sf.carte.echosvg.gvt.event.GraphicsNodeMouseEvent;
import io.sf.carte.echosvg.gvt.event.SelectionEvent;
import io.sf.carte.echosvg.gvt.event.SelectionListener;

/**
 * A simple implementation of GraphicsNodeMouseListener for text selection.
 *
 * @author <a href="mailto:bill.haneman@ireland.sun.com">Bill Haneman</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class ConcreteTextSelector implements Selector {

	private ArrayList<SelectionListener> listeners;
	private GraphicsNode selectionNode;
	private RootGraphicsNode selectionNodeRoot;

	public ConcreteTextSelector() {
	}

	@Override
	public void mouseClicked(GraphicsNodeMouseEvent evt) {
		checkSelectGesture(evt);
	}

	@Override
	public void mouseDragged(GraphicsNodeMouseEvent evt) {
		checkSelectGesture(evt);
	}

	@Override
	public void mouseEntered(GraphicsNodeMouseEvent evt) {
		checkSelectGesture(evt);
	}

	@Override
	public void mouseExited(GraphicsNodeMouseEvent evt) {
		checkSelectGesture(evt);
	}

	@Override
	public void mouseMoved(GraphicsNodeMouseEvent evt) {
	}

	@Override
	public void mousePressed(GraphicsNodeMouseEvent evt) {
		checkSelectGesture(evt);
	}

	@Override
	public void mouseReleased(GraphicsNodeMouseEvent evt) {
		checkSelectGesture(evt);
	}

	@Override
	public void keyPressed(GraphicsNodeKeyEvent evt) {
		report(evt, "keyPressed");
	}

	@Override
	public void keyReleased(GraphicsNodeKeyEvent evt) {
		report(evt, "keyReleased");
	}

	@Override
	public void keyTyped(GraphicsNodeKeyEvent evt) {
		report(evt, "keyTyped");
	}

	@Override
	public void changeStarted(GraphicsNodeChangeEvent gnce) {
	}

	@Override
	public void changeCompleted(GraphicsNodeChangeEvent gnce) {
		if (selectionNode == null) {
			return;
		}
		Shape newShape = ((Selectable) selectionNode).getHighlightShape();
		dispatchSelectionEvent(new SelectionEvent(getSelection(), SelectionEvent.SELECTION_CHANGED, newShape));
	}

	public void setSelection(Mark begin, Mark end) {
		TextNode node = begin.getTextNode();
		if (node != end.getTextNode()) {
			throw new RuntimeException("Markers not from same TextNode");
		}
		node.setSelection(begin, end);
		selectionNode = node;
		selectionNodeRoot = node.getRoot();
		Object selection = getSelection();
		Shape shape = node.getHighlightShape();
		dispatchSelectionEvent(new SelectionEvent(selection, SelectionEvent.SELECTION_DONE, shape));
	}

	public void clearSelection() {
		if (selectionNode == null) {
			return;
		}
		dispatchSelectionEvent(new SelectionEvent(null, SelectionEvent.SELECTION_CLEARED, null));
		selectionNode = null;
		selectionNodeRoot = null;
	}

	/*
	 * Checks the event to see if it is a selection gesture and processes it
	 * accordingly.
	 * 
	 * @param evt the GraphicsNodeEvent, which may be a "select gesture" Param evt
	 * is a GraphicsNodeEvent rather than a GraphicsNodeMouseEvent for future
	 * extension, so we can use Shift-arrow, etc.
	 */
	protected void checkSelectGesture(GraphicsNodeEvent evt) {

		GraphicsNodeMouseEvent mevt = null;
		if (evt instanceof GraphicsNodeMouseEvent) {
			mevt = (GraphicsNodeMouseEvent) evt;
		}

		GraphicsNode source = evt.getGraphicsNode();
		if (isDeselectGesture(evt)) {
			if (selectionNode != null) {
				selectionNodeRoot.removeTreeGraphicsNodeChangeListener(this);
			}
			clearSelection();
		} else if (mevt != null) {

			Point2D p = mevt.getPoint2D();

			if ((source instanceof Selectable) && (isSelectStartGesture(evt))) {
				if (selectionNode != source) {
					if (selectionNode != null) {
						selectionNodeRoot.removeTreeGraphicsNodeChangeListener(this);
					}
					selectionNode = source;
					if (source != null) {
						selectionNodeRoot = source.getRoot();
						selectionNodeRoot.addTreeGraphicsNodeChangeListener(this);
					}
				}

				((Selectable) source).selectAt(p.getX(), p.getY());
				dispatchSelectionEvent(new SelectionEvent(null, SelectionEvent.SELECTION_STARTED, null));

			} else if (isSelectEndGesture(evt)) {
				if (selectionNode == source) {
					((Selectable) source).selectTo(p.getX(), p.getY());
				}
				Object oldSelection = getSelection();
				if (selectionNode != null) {
					Shape newShape;
					newShape = ((Selectable) selectionNode).getHighlightShape();
					dispatchSelectionEvent(new SelectionEvent(oldSelection, SelectionEvent.SELECTION_DONE, newShape));
				}
			} else if (isSelectContinueGesture(evt)) {

				if (selectionNode == source) {
					boolean result = ((Selectable) source).selectTo(p.getX(), p.getY());
					if (result) {
						Shape newShape = ((Selectable) selectionNode).getHighlightShape();

						dispatchSelectionEvent(new SelectionEvent(null, SelectionEvent.SELECTION_CHANGED, newShape));
					}
				}
			} else if ((source instanceof Selectable) && (isSelectAllGesture(evt))) {
				if (selectionNode != source) {
					if (selectionNode != null) {
						selectionNodeRoot.removeTreeGraphicsNodeChangeListener(this);
					}
					selectionNode = source;
					if (source != null) {
						selectionNodeRoot = source.getRoot();
						selectionNodeRoot.addTreeGraphicsNodeChangeListener(this);
					}
				}
				((Selectable) source).selectAll(p.getX(), p.getY());
				Object oldSelection = getSelection();
				Shape newShape = ((Selectable) source).getHighlightShape();
				dispatchSelectionEvent(new SelectionEvent(oldSelection, SelectionEvent.SELECTION_DONE, newShape));
			}
		}
	}

	private boolean isDeselectGesture(GraphicsNodeEvent evt) {
		return ((evt.getID() == GraphicsNodeMouseEvent.MOUSE_CLICKED)
				&& (((GraphicsNodeMouseEvent) evt).getClickCount() == 1));
	}

	private boolean isSelectStartGesture(GraphicsNodeEvent evt) {
		return (evt.getID() == GraphicsNodeMouseEvent.MOUSE_PRESSED);
	}

	private boolean isSelectEndGesture(GraphicsNodeEvent evt) {
		return ((evt.getID() == GraphicsNodeMouseEvent.MOUSE_RELEASED));
	}

	private boolean isSelectContinueGesture(GraphicsNodeEvent evt) {
		return (evt.getID() == GraphicsNodeMouseEvent.MOUSE_DRAGGED);
	}

	private boolean isSelectAllGesture(GraphicsNodeEvent evt) {
		return ((evt.getID() == GraphicsNodeMouseEvent.MOUSE_CLICKED)
				&& (((GraphicsNodeMouseEvent) evt).getClickCount() == 2));
	}

	/*
	 * Get the contents of the current selection.
	 */
	@Override
	public Object getSelection() {
		Object value = null;
		if (selectionNode instanceof Selectable) {
			value = ((Selectable) selectionNode).getSelection();
		}
		return value;
	}

	/**
	 * Reports whether the current selection contains any objects.
	 */
	@Override
	public boolean isEmpty() {
		return (getSelection() == null);
	}

	/**
	 * Reports whether the current selection contains any objects.
	 */
	public void dispatchSelectionEvent(SelectionEvent e) {
		if (listeners != null) {
			Iterator<SelectionListener> iter = listeners.iterator();
			switch (e.getID()) {
			case SelectionEvent.SELECTION_DONE:
				while (iter.hasNext()) {
					iter.next().selectionDone(e);
				}
				break;
			case SelectionEvent.SELECTION_CHANGED:
				while (iter.hasNext()) {
					iter.next().selectionChanged(e);
				}
				break;
			case SelectionEvent.SELECTION_CLEARED:
				while (iter.hasNext()) {
					iter.next().selectionCleared(e);
				}
				break;
			case SelectionEvent.SELECTION_STARTED:
				while (iter.hasNext()) {
					iter.next().selectionStarted(e);
				}
				break;
			}
		}
	}

	/**
	 * Add a SelectionListener to this Selector's notification list.
	 * 
	 * @param l the SelectionListener to add.
	 */
	@Override
	public void addSelectionListener(SelectionListener l) {
		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(l);
	}

	/**
	 * Remove a SelectionListener from this Selector's notification list.
	 * 
	 * @param l the SelectionListener to be removed.
	 */
	@Override
	public void removeSelectionListener(SelectionListener l) {
		if (listeners != null) {
			listeners.remove(l);
		}
	}

	private void report(GraphicsNodeEvent evt, String message) {
		GraphicsNode source = evt.getGraphicsNode();
		String label = "(non-text node)";
		if (source instanceof TextNode) {
			char[] cbuff;
			java.text.CharacterIterator iter = ((TextNode) source).getAttributedCharacterIterator();
			cbuff = new char[iter.getEndIndex()];
			if (cbuff.length > 0)
				cbuff[0] = iter.first();
			for (int i = 1; i < cbuff.length; ++i) {
				cbuff[i] = iter.next();
			}
			label = new String(cbuff);
		}
		System.out.println("Mouse " + message + " in " + label);
	}

}
