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
package io.sf.carte.echosvg.dom;

import org.w3c.dom.Comment;

/**
 * This class implements the {@link org.w3c.dom.Comment} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public abstract class AbstractComment extends AbstractCharacterData implements Comment {

	private static final long serialVersionUID = 1L;

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
	 * 
	 * @return "#comment".
	 */
	@Override
	public String getNodeName() {
		return "#comment";
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeType()}.
	 * 
	 * @return {@link org.w3c.dom.Node#COMMENT_NODE}
	 */
	@Override
	public short getNodeType() {
		return COMMENT_NODE;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getTextContent()}.
	 */
	@Override
	public String getTextContent() {
		return getNodeValue();
	}

}
