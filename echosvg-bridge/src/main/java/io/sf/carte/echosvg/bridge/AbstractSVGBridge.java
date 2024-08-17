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

import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * The base bridge class for SVG elements.
 *
 * <p>
 * Original author: <a href="mailto:tkormann@apache.org">Thierry Kormann</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class AbstractSVGBridge implements Bridge, SVGConstants {

	/**
	 * Constructs a new abstract bridge for SVG elements.
	 */
	protected AbstractSVGBridge() {
	}

	/**
	 * Returns the SVG namespace URI.
	 */
	@Override
	public String getNamespaceURI() {
		return SVG_NAMESPACE_URI;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		// <!> FIXME: temporary fix for progressive implementation
		// System.out.println("use static bridge for: "+getLocalName());
		return this;
	}

	static void reportLiveAttributeException(BridgeContext ctx, LiveAttributeException ex)
			throws RuntimeException {
		BridgeException be = new BridgeException(ctx, ex);
		if (ex.getCode() == LiveAttributeException.ERR_ATTRIBUTE_MISSING) {
			if (ctx.userAgent != null) {
				ctx.userAgent.displayWarning(be);
			}
		} else {
			displayErrorOrThrow(ctx, be);
		}

	}

	static void displayErrorOrThrow(BridgeContext ctx, RuntimeException ex) throws RuntimeException {
		if (ctx.userAgent != null) {
			ctx.userAgent.displayError(ex);
		} else {
			throw ex;
		}
	}

}
