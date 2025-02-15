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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.DOMException;

/**
 * A Value made of components.
 * 
 * @version $Id$
 */
public abstract class ComponentValue extends AbstractValue {

	private static final long serialVersionUID = 1L;

	private ComponentModificationHandler componentHandler = new ComponentModificationHandler();

	protected ComponentValue() {
		super();
	}

	protected ComponentModificationHandler getComponentHandler() {
		return componentHandler;
	}

	protected void componentize(Value value) {
		value.setModificationHandler(componentHandler);
	}

	protected void componentChanged(Value v) {
		componentHandler.componentChanged(v);
	}

	protected void componentAdded(Value v) {
		componentHandler.componentAdded(v);
	}

	protected void componentRemoved(Value v) {
		componentHandler.componentRemoved(v);
	}

	class ComponentModificationHandler implements ComponentHandler {

		@Override
		public void componentChanged(Value c) {
			propertyChanged();
		}

		@Override
		public void componentAdded(Value c) {
			propertyChanged();
		}

		@Override
		public void componentRemoved(Value c) {
			propertyChanged();
		}

		@Override
		public void listValueChanged(int index, Value value) throws DOMException {
			propertyChanged();
		}

		@Override
		public void valueChanged(Value v) {
			propertyChanged();
		}

		private void propertyChanged() {
			if (handler != null) {
				handler.valueChanged(getValue());
			}
		}

		@Override
		public Value getValue() {
			return ComponentValue.this;
		}
		
	}

}
