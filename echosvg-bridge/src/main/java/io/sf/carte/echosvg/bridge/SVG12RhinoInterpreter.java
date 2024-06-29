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
package io.sf.carte.echosvg.bridge;

import java.net.URL;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import io.sf.carte.echosvg.script.ImportInfo;

/**
 * A RhinoInterpreter for SVG 1.2 documents.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVG12RhinoInterpreter extends RhinoInterpreter {

	/**
	 * Creates an SVG12RhinoInterpreter object.
	 */
	public SVG12RhinoInterpreter(URL documentURL) {
		super(documentURL);
	}

	/**
	 * Creates an SVG12RhinoInterpreter object.
	 */
	public SVG12RhinoInterpreter(URL documentURL, ImportInfo imports) {
		super(documentURL, imports);
	}

	/**
	 * Defines the class for the global object.
	 */
	@Override
	protected void defineGlobalWrapperClass(Scriptable global) {
		try {
			ScriptableObject.defineClass(global, GlobalWrapper.class);
		} catch (Exception ex) {
			// cannot happen
		}
	}

	/**
	 * Creates the global object.
	 */
	@Override
	protected ScriptableObject createGlobalObject(Context ctx) {
		return new GlobalWrapper(ctx);
	}

}
