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

/**
 * An adapter class that represents a listener to the GVTTreeBuilderEvent
 * events.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class GVTTreeBuilderAdapter implements GVTTreeBuilderListener {

	/**
	 * Called when a build started. The data of the event is initialized to the old
	 * document.
	 */
	@Override
	public void gvtBuildStarted(GVTTreeBuilderEvent e) {
	}

	/**
	 * Called when a build was completed.
	 */
	@Override
	public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
	}

	/**
	 * Called when a build was cancelled.
	 */
	@Override
	public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
	}

	/**
	 * Called when a build failed.
	 */
	@Override
	public void gvtBuildFailed(GVTTreeBuilderEvent e) {
	}

}
