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
package io.sf.carte.echosvg.script.rhino;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.GeneratedClassLoader;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrappedException;

/**
 * This implementation of the Rhino <code>SecurityController</code> interface is
 * meant for use within the context of EchoSVG only. It is a partial
 * implementation of the interface that does what is needed by EchoSVG and no
 * more.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@SuppressWarnings("removal")
class SMSecurityController extends EchoSVGSecurityController {

	/**
	 * Default constructor
	 */
	public SMSecurityController() {
		super();
	}

	@Override
	public GeneratedClassLoader createClassLoader(final ClassLoader parentLoader, Object securityDomain) {

		if (securityDomain instanceof RhinoClassLoader) {
			return (RhinoClassLoader) securityDomain;
		}

		// FIXX: This should be supported by intersecting perms.
		// Calling var script = Script(source); script(); is not supported
		throw new SecurityException("Script() objects are not supported");
	}

	/**
	 * Get dynamic security domain that allows an action only if it is allowed by
	 * the current Java stack and <i>securityDomain</i>. If <i>securityDomain</i> is
	 * null, return domain representing permissions allowed by the current stack.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Object getDynamicSecurityDomain(Object securityDomain) {

		ClassLoader loader = (RhinoClassLoader) securityDomain;
		// Already have a rhino loader in place no need to
		// do anything (normally you would want to union the
		// the current stack with the loader's context but
		// in our case no one has lower privledges than a
		// rhino class loader).
		if (loader != null)
			return loader;

		return AccessController.getContext();
	}

	/**
	 * Calls {@link Callable#call(Context, Scriptable, Scriptable, Object[])} of
	 * <code>callable</code> under restricted security domain where an action is
	 * allowed only if it is allowed according to the Java stack on the moment of
	 * the <code>callWithDomain</code> call and <code>securityDomain</code>. Any
	 * call to {@link #getDynamicSecurityDomain(Object)} during execution of
	 * {@link Callable#call(Context, Scriptable, Scriptable, Object[])} should
	 * return a domain incorporate restrictions imposed by
	 * <code>securityDomain</code>.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Object callWithDomain(Object securityDomain, final Context cx, final Callable callable,
			final Scriptable scope, final Scriptable thisObj, final Object[] args) {
		AccessControlContext acc;
		if (securityDomain instanceof AccessControlContext)
			acc = (AccessControlContext) securityDomain;
		else {
			RhinoClassLoader loader = (RhinoClassLoader) securityDomain;
			acc = (AccessControlContext) loader.getAccessControlObject();
		}

		PrivilegedExceptionAction<Object> execAction = new PrivilegedExceptionAction<Object>() {
			@Override
			public Object run() {
				return callable.call(cx, scope, thisObj, args);
			}
		};
		try {
			return AccessController.doPrivileged(execAction, acc);
		} catch (Exception e) {
			throw new WrappedException(e);
		}
	}

}
