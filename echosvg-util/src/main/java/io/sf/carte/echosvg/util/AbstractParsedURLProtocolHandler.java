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
package io.sf.carte.echosvg.util;

/**
 * Very simple abstract base class for ParsedURLProtocolHandlers. Just handles
 * the 'what protocol part'.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractParsedURLProtocolHandler implements ParsedURLProtocolHandler {

	protected String protocol;

	/**
	 * Constrcut a ProtocolHandler for <code>protocol</code>
	 */
	public AbstractParsedURLProtocolHandler(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * Returns the protocol to be handled by this class. The protocol must _always_
	 * be the part of the URL before the first ':'.
	 */
	@Override
	public String getProtocolHandled() {
		return protocol;
	}
}
