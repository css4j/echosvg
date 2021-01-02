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
package io.sf.carte.echosvg.gvt;

import io.sf.carte.echosvg.gvt.event.GraphicsNodeChangeListener;
import io.sf.carte.echosvg.gvt.event.GraphicsNodeKeyListener;
import io.sf.carte.echosvg.gvt.event.GraphicsNodeMouseListener;
import io.sf.carte.echosvg.gvt.event.SelectionListener;

/**
 * Interface which allows selection of GraphicsNodes and their contents.
 *
 * @author <a href="mailto:bill.haneman@ireland.sun.com">Bill Haneman</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface Selector extends GraphicsNodeMouseListener, GraphicsNodeKeyListener, GraphicsNodeChangeListener {

	/**
	 * Get the contents of the current selection buffer.
	 */
	Object getSelection();

	/**
	 * Reports whether the current selection contains any objects.
	 */
	boolean isEmpty();

	/**
	 * Add a SelectionListener to this Selector's notification list.
	 * 
	 * @param l the SelectionListener to add.
	 */
	void addSelectionListener(SelectionListener l);

	/**
	 * Remove a SelectionListener from this Selector's notification list.
	 * 
	 * @param l the SelectionListener to be removed.
	 */
	void removeSelectionListener(SelectionListener l);

}
