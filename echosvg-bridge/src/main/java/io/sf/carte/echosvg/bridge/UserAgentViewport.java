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
package io.sf.carte.echosvg.bridge;

/**
 * Defines a viewport for a <code>UserAgent</code>.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class UserAgentViewport implements Viewport {

	private UserAgent userAgent;

	/**
	 * Constructs a new viewport for the specified user agent.
	 * 
	 * @param userAgent the user agent that defines the viewport
	 */
	public UserAgentViewport(UserAgent userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Returns the width of this viewport.
	 */
	@Override
	public float getWidth() {
		return (float) userAgent.getViewportSize().getWidth();
	}

	/**
	 * Returns the height of this viewport.
	 */
	@Override
	public float getHeight() {
		return (float) userAgent.getViewportSize().getHeight();
	}
}
