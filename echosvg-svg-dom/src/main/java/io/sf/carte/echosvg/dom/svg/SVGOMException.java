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
package io.sf.carte.echosvg.dom.svg;

import org.w3c.dom.svg.SVGException;

/**
 * An implementation of the SVGException class.
 *
 * @author <a href="mailto:tkormann@ilog.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMException extends SVGException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>SVGOMException</code> with the specified parameters.
	 *
	 * @param code    the exception code
	 * @param message the error message
	 */
	public SVGOMException(short code, String message) {
		super(code, message);
	}
}
