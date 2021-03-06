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

/**
 * An adapter class that represents a listener to the
 * <code>GVTTreeRendererEvent</code> events.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class GVTTreeRendererAdapter implements GVTTreeRendererListener {

	/**
	 * Called when a rendering is in its preparing phase.
	 */
	@Override
	public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
	}

	/**
	 * Called when a rendering started.
	 */
	@Override
	public void gvtRenderingStarted(GVTTreeRendererEvent e) {
	}

	/**
	 * Called when a rendering was completed.
	 */
	@Override
	public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
	}

	/**
	 * Called when a rendering was cancelled.
	 */
	@Override
	public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
	}

	/**
	 * Called when a rendering failed.
	 */
	@Override
	public void gvtRenderingFailed(GVTTreeRendererEvent e) {
	}

}
