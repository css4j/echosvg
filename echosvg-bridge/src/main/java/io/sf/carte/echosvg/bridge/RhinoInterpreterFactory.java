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
package io.sf.carte.echosvg.bridge;

import java.net.URL;

import io.sf.carte.echosvg.script.ImportInfo;
import io.sf.carte.echosvg.script.Interpreter;
import io.sf.carte.echosvg.script.InterpreterFactory;

/**
 * Allows to create instances of <code>RhinoInterpreter</code> class.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RhinoInterpreterFactory implements InterpreterFactory {

	/**
	 * The MIME types that Rhino can handle.
	 */
	public static final String[] RHINO_MIMETYPES = { "application/ecmascript", "application/javascript",
			"text/ecmascript", "text/javascript", };

	/**
	 * Builds a <code>RhinoInterpreterFactory</code>.
	 */
	public RhinoInterpreterFactory() {
	}

	/**
	 * Returns the mime-types to register this interpereter with.
	 */
	@Override
	public String[] getMimeTypes() {
		return RHINO_MIMETYPES;
	}

	/**
	 * Creates an instance of <code>RhinoInterpreter</code> class.
	 *
	 * @param documentURL the url for the document which will be scripted
	 * @param svg12       whether the document is an SVG 1.2 document
	 */
	@Override
	public Interpreter createInterpreter(URL documentURL, boolean svg12) {
		return createInterpreter(documentURL, svg12, null);
	}

	/**
	 * Creates an instance of <code>RhinoInterpreter</code> class.
	 *
	 * @param documentURL the url for the document which will be scripted
	 * @param svg12       whether the document is an SVG 1.2 document
	 * @param imports     The set of classes/packages to import (if the interpreter
	 *                    supports that), may be null.
	 */
	@Override
	public Interpreter createInterpreter(URL documentURL, boolean svg12, ImportInfo imports) {
		if (svg12) {
			return new SVG12RhinoInterpreter(documentURL, imports);
		}
		return new RhinoInterpreter(documentURL, imports);
	}
}
