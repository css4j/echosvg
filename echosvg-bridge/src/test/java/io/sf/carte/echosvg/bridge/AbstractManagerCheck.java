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

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

import org.junit.jupiter.api.BeforeEach;

public class AbstractManagerCheck {

	protected BridgeContext context;
	protected int errorCount;
	protected int messageCount;

	@BeforeEach
	public void setupBeforeEach() {
		context = createBridgeContext();
		errorCount = 0;
		messageCount = 0;
	}

	protected BridgeContext createBridgeContext() {
		return new BridgeContext(new UserAgentAdapter() {

			@Override
			public Dimension2D getViewportSize() {
				return new Dimension(200, 200);
			}

			@Override
			public void displayError(String message) {
				errorCount++;
			}

			@Override
			public void displayMessage(String message) {
				messageCount++;
			}

		});
	}

}
