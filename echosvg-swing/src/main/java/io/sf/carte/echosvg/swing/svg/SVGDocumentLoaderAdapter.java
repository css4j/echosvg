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
 * An adapter class that represents a listener to the
 * <code>SVGDocumentLoaderEvent</code> events.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class SVGDocumentLoaderAdapter implements SVGDocumentLoaderListener {

	/**
	 * Called when the loading of a document was started.
	 */
	@Override
	public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
	}

	/**
	 * Called when the loading of a document was completed.
	 */
	@Override
	public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
	}

	/**
	 * Called when the loading of a document was cancelled.
	 */
	@Override
	public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
	}

	/**
	 * Called when the loading of a document has failed.
	 */
	@Override
	public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
	}
}
