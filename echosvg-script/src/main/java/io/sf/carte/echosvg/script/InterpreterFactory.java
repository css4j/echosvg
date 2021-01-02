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
package io.sf.carte.echosvg.script;

import java.net.URL;

/**
 * An interface for factory objects than can create {@link Interpreter}
 * instances for a particular script language.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface InterpreterFactory {

	/**
	 * Returns the mime-types to register this interpereter with.
	 */
	String[] getMimeTypes();

	/**
	 * This method should create an instance of <code>Interpreter</code> interface
	 * implementation.
	 *
	 * @param documentURL the url for the document which will be scripted
	 * @param svg12       whether the document is an SVG 1.2 document
	 * @param imports     The set of classes/packages to import (if the interpreter
	 *                    supports that), may be null.
	 */
	Interpreter createInterpreter(URL documentURL, boolean svg12, ImportInfo imports);

	/**
	 * This method should create an instance of <code>Interpreter</code> interface
	 * implementation.
	 *
	 * @param documentURL the url for the document which will be scripted
	 * @param svg12       whether the document is an SVG 1.2 document
	 */
	Interpreter createInterpreter(URL documentURL, boolean svg12);
}
