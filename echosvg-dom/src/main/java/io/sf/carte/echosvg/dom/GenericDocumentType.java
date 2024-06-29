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

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class implements the {@link org.w3c.dom.DocumentType} interface.
 *
 * @author <a href="mailto:thomas.deweese@kodak.com">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class GenericDocumentType extends AbstractChildNode implements DocumentType {

	private static final long serialVersionUID = 1L;

	/**
	 * The qualified name of the document element.
	 */
	protected String qualifiedName;

	/**
	 * The DTD public ID, if specified.
	 */
	protected String publicId;

	/**
	 * The DTD system ID, if specified.
	 */
	protected String systemId;

	/**
	 * Creates a new DocumentType object.
	 */
	public GenericDocumentType(String qualifiedName, String publicId, String systemId) {
		this.qualifiedName = qualifiedName;
		this.publicId = publicId;
		this.systemId = systemId;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.Node#getNodeName()}.
	 * 
	 * @return The name of the DTD.
	 */
	@Override
	public String getNodeName() {
		return qualifiedName;
	}

	@Override
	public short getNodeType() {
		return DOCUMENT_TYPE_NODE;
	}

	@Override
	public boolean isReadonly() {
		return true;
	}

	@Override
	public void setReadonly(boolean ro) {
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.DocumentType#getName()}.
	 * 
	 * @return The name of document element as specified in the DTD.
	 */
	@Override
	public String getName() {
		return qualifiedName;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.DocumentType#getEntities()}.
	 * 
	 * @return null.
	 */
	@Override
	public NamedNodeMap getEntities() {
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.DocumentType#getNotations()}.
	 * 
	 * @return null.
	 */
	@Override
	public NamedNodeMap getNotations() {
		return null;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.DocumentType#getPublicId()}.
	 * 
	 * @return The public id.
	 */
	@Override
	public String getPublicId() {
		return publicId;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.DocumentType#getSystemId()}.
	 * 
	 * @return The public id.
	 */
	@Override
	public String getSystemId() {
		return systemId;
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.DocumentType#getInternalSubset()}.
	 * 
	 * @return The public id.
	 */
	@Override
	public String getInternalSubset() {
		return null;
	}

	@Override
	protected Node newNode() {
		return new GenericDocumentType(qualifiedName, publicId, systemId);
	}

}
