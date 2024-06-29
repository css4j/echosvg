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
package io.sf.carte.echosvg.script;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import org.w3c.dom.Document;

import io.sf.carte.echosvg.anim.dom.SVGOMDocument;

/**
 * A class allowing to create/query an
 * {@link io.sf.carte.echosvg.script.Interpreter} corresponding to a particular
 * <code>Document</code> and scripting language.
 *
 * <p>
 * By default, it is able to create interpreters for ECMAScript, Python and Tcl
 * scripting languages if you provide the right jar files in your CLASSPATH
 * (i.e. Rhino, JPython and Jacl jar files).
 * </p>
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class InterpreterPool {

	/**
	 * Name of the "document" object when referenced by scripts
	 */
	public static final String BIND_NAME_DOCUMENT = "document";

	/**
	 * The default InterpreterFactory map.
	 */
	protected static Map<String, InterpreterFactory> defaultFactories = new HashMap<>(7);

	/**
	 * The InterpreterFactory map.
	 */
	protected Map<String, InterpreterFactory> factories = new HashMap<>(7);

	static {
		ServiceLoader<InterpreterFactory> loader = ServiceLoader.load(InterpreterFactory.class);

		Iterator<InterpreterFactory> iter = loader.iterator();
		while (iter.hasNext()) {
			InterpreterFactory factory = null;
			factory = iter.next();
			String[] mimeTypes = factory.getMimeTypes();
			for (String mimeType : mimeTypes) {
				defaultFactories.put(mimeType, factory);
			}
		}
	}

	/**
	 * Constructs a new <code>InterpreterPool</code>.
	 */
	public InterpreterPool() {
		factories.putAll(defaultFactories);
	}

	/**
	 * Creates a new interpreter for the specified document and according to the
	 * specified language. This method can return null if no interpreter has been
	 * found for the specified language.
	 *
	 * @param document the document that needs the interpreter
	 * @param language the scripting language
	 */
	public Interpreter createInterpreter(Document document, String language) {
		return createInterpreter(document, language, null);
	}

	/**
	 * Creates a new interpreter for the specified document and according to the
	 * specified language. This method can return null if no interpreter has been
	 * found for the specified language.
	 *
	 * @param document the document that needs the interpreter. Cannot be null.
	 * @param language the scripting language
	 * @param imports  The set of classes/packages to import (if the interpreter
	 *                 supports that).
	 */
	public Interpreter createInterpreter(Document document, String language, ImportInfo imports) {
		InterpreterFactory factory = factories.get(language);

		if (factory == null)
			return null;

		if (imports == null)
			imports = ImportInfo.getImports();

		Interpreter interpreter = null;
		SVGOMDocument svgDoc = (SVGOMDocument) document;
		URL url = null;
		try {
			url = new URL(svgDoc.getDocumentURI());
		} catch (MalformedURLException e) {
		}
		interpreter = createInterpreter(factory, url, svgDoc.isSVG12(), imports);

		if (interpreter == null)
			return null;

		interpreter.bindObject(BIND_NAME_DOCUMENT, document);

		return interpreter;
	}

	private static Interpreter createInterpreter(InterpreterFactory factory, URL documentURL, boolean svg12,
			ImportInfo imports) {
		PrivilegedExceptionAction<?> pea = new PrivilegedExceptionAction<Interpreter>() {
			@Override
			public Interpreter run() throws Exception {
				return factory.createInterpreter(documentURL, svg12, imports);
			}
		};
		try {
			return (Interpreter) SecurityHelper.getInstance().runPrivilegedExceptionAction(pea);
		} catch (PrivilegedActionException pae) {
			Exception ex = pae.getException();
			ex.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Adds for the specified language, the specified Interpreter factory.
	 *
	 * @param language the language for which the factory is registered
	 * @param factory  the <code>InterpreterFactory</code> to register
	 */
	public void putInterpreterFactory(String language, InterpreterFactory factory) {
		factories.put(language, factory);
	}

	/**
	 * Removes the InterpreterFactory associated to the specified language.
	 *
	 * @param language the language for which the factory should be removed.
	 */
	public void removeInterpreterFactory(String language) {
		factories.remove(language);
	}

}
