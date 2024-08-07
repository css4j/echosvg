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
package io.sf.carte.echosvg.dom.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractNode;

/**
 * Tests Node.setUserData and Node.getUserData.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NodeGetUserDataTest extends DOM3Test {

	@Test
	public void test() throws DOMException {
		UserHandler udh = new UserHandler();
		Document doc = newDoc();
		AbstractNode n = (AbstractNode) doc.createElementNS(null, "test");
		n.setUserData("key", "val", udh);
		((AbstractDocument) doc).renameNode(n, null, "abc");
		assertEquals(1, udh.getCount());
		assertEquals("val", n.getUserData("key"));
	}

	private static class UserHandler implements UserDataHandler {

		int count = 0;

		@Override
		public void handle(short op, String key, Object data, Node src, Node dest) {
			count++;
		}

		public int getCount() {
			return count;
		}

	}

}
