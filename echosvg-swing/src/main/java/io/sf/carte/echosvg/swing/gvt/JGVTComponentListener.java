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
package io.sf.carte.echosvg.swing.gvt;

import java.awt.event.ComponentEvent;

/**
 * An interface for listeners of {@link JGVTComponent} events.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface JGVTComponentListener {
	int COMPONENT_TRANSFORM_CHANGED = ComponentEvent.COMPONENT_LAST + 1234;

	/**
	 * Called when the rendering transform changes on the JGVTComponentListener
	 */
	void componentTransformChanged(ComponentEvent event);
}
