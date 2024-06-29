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
 * This <code>SecurityManager</code> extension exposes the
 * <code>getClassContext</code> method so that it can be used by the
 * <code>EchoSVGSecuritySupport</code> or other security related class.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Deprecated(forRemoval = true)
public class EchoSVGSecurityManager extends SecurityManager {

	/**
	 * Returns the current execution stack as an array of classes.
	 * <p>
	 * The length of the array is the number of methods on the execution stack. The
	 * element at index <code>0</code> is the class of the currently executing
	 * method, the element at index <code>1</code> is the class of that method's
	 * caller, and so on.
	 *
	 * @return the execution stack.
	 */
	@Override
	public Class<?>[] getClassContext() {
		return super.getClassContext();
	}

}
