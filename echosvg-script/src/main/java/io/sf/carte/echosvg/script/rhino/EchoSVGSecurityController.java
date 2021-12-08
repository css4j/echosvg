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

import java.lang.reflect.Constructor;
import java.security.ProtectionDomain;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.GeneratedClassLoader;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.SecurityController;

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
public class EchoSVGSecurityController extends SecurityController {

	private static final EchoSVGSecurityController singleton = createSecurityController();

	private static EchoSVGSecurityController createSecurityController() {
		EchoSVGSecurityController sc;
		try {
			Class<?> cl = Class.forName("io.sf.carte.echosvg.script.rhino.SMSecurityController");
			Constructor<?> ctor = cl.getConstructor();
			sc = (EchoSVGSecurityController) ctor.newInstance();
		} catch (Exception e) {
			sc = new EchoSVGSecurityController();
		}
		return sc;
	}

	public static EchoSVGSecurityController getInstance() {
		return singleton;
	}

	/**
	 * Default constructor
	 */
	EchoSVGSecurityController() {
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
	 * Get the access control object (an {@code AccessControlContext}, if the JVM
	 * supports the SecurityManager API) which can be associated with the given
	 * {@code ProtectionDomain}.
	 * 
	 * @param protectionDomain the protection domain.
	 * @return the access control object, or {@code null} if the JVM knows no access
	 *         control context.
	 */
	Object getAccessControlObject(ProtectionDomain protectionDomain) {
		return null;
	}

	/**
	 * Get dynamic security domain that allows an action only if it is allowed by
	 * the current Java stack and <i>securityDomain</i>. If <i>securityDomain</i> is
	 * null, return domain representing permissions allowed by the current stack.
	 */
	@Override
	public Object getDynamicSecurityDomain(Object securityDomain) {
		return securityDomain;
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
	@Override
	public Object callWithDomain(Object securityDomain, final Context cx, final Callable callable,
			final Scriptable scope, final Scriptable thisObj, final Object[] args) {
		return callable.call(cx, scope, thisObj, args);
	}

}
