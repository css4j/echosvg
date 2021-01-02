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
package io.sf.carte.echosvg.css.engine;

import java.util.EventObject;

import org.w3c.dom.Element;

/**
 * This class represents a CSS event fired by a CSSEngine.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class CSSEngineEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
	 * The event target.
	 */
	protected Element element;

	/**
	 * The changed properties indexes.
	 */
	protected int[] properties;

	/**
	 * Creates a new CSSEngineEvent.
	 */
	public CSSEngineEvent(CSSEngine source, Element elt, int[] props) {
		super(source);
		element = elt;
		properties = props;
	}

	/**
	 * Returns the target element.
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * Returns the changed properties.
	 */
	public int[] getProperties() {
		return properties;
	}
}
