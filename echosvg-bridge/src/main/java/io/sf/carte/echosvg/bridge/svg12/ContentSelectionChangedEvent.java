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
package io.sf.carte.echosvg.bridge.svg12;

import java.util.EventObject;

import io.sf.carte.echosvg.anim.dom.XBLOMContentElement;

/**
 * An event to signify a change to the list of selected nodes for an xbl;content
 * element.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ContentSelectionChangedEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new ContentSelectionChangedEvent object.
	 * 
	 * @param c the xbl:content element whose selection is changing
	 */
	public ContentSelectionChangedEvent(XBLOMContentElement c) {
		super(c);
	}

	/**
	 * Returns the xbl:content element where the event originated.
	 */
	public XBLOMContentElement getContentElement() {
		return (XBLOMContentElement) source;
	}
}
