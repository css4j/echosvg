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

/**
 * Tests Node.compareDocumentPosition.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class NodeCompareDocumentPositionTest extends DOM3Test {
    @Override
    public boolean runImplBasic() throws Exception {
        Document doc = newSVGDoc();
        AbstractNode e = (AbstractNode) doc.createElementNS(null, "test");
        doc.getDocumentElement().appendChild(e);
        AbstractNode e2 = (AbstractNode) doc.createElementNS(null, "two");
        e.appendChild(e2);
        AbstractNode e3 = (AbstractNode) doc.createElementNS(null, "three");
        e.appendChild(e3);
        AbstractNode e4 = (AbstractNode) doc.createElementNS(null, "four");
        doc.getDocumentElement().appendChild(e4);

        return e.compareDocumentPosition(e2) == (AbstractNode.DOCUMENT_POSITION_CONTAINS | AbstractNode.DOCUMENT_POSITION_PRECEDING)
                && e2.compareDocumentPosition(e) == (AbstractNode.DOCUMENT_POSITION_CONTAINED_BY | AbstractNode.DOCUMENT_POSITION_FOLLOWING)
                && e.compareDocumentPosition(e) == 0
                && e2.compareDocumentPosition(e3) == AbstractNode.DOCUMENT_POSITION_PRECEDING
                && e3.compareDocumentPosition(e2) == AbstractNode.DOCUMENT_POSITION_FOLLOWING
                && e3.compareDocumentPosition(e4) == AbstractNode.DOCUMENT_POSITION_PRECEDING
                && e4.compareDocumentPosition(e3) == AbstractNode.DOCUMENT_POSITION_FOLLOWING;
    }
}
