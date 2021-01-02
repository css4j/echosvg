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
package io.sf.carte.echosvg.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

/**
 * Tests Node.setUserData and Node.getUserData.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NodeGetUserDataTest extends DOM3Test {
	static class UserHandler implements UserDataHandler {
		int count = 0;

		@Override
		public void handle(short op, String key, Object data, Node src, Node dest) {
			count++;
		}

		public int getCount() {
			return count;
		}
	}

	@Override
	public boolean runImplBasic() throws Exception {
		UserHandler udh = new UserHandler();
		Document doc = newDoc();
		AbstractNode n = (AbstractNode) doc.createElementNS(null, "test");
		n.setUserData("key", "val", udh);
		((AbstractDocument) doc).renameNode(n, null, "abc");
		return udh.getCount() == 1 && n.getUserData("key").equals("val");
	}
}
