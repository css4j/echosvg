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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.mozilla.javascript.ClassShutter;

/**
 * Class shutter that restricts access to EchoSVG internals from script.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RhinoClassShutter implements ClassShutter {

	private static final List<Pattern> whitelist = new ArrayList<>();

	static {
		addToWhitelist("java.io.PrintStream");
		addToWhitelist("java.lang.System");
		addToWhitelist("java.net.URL");
		addToWhitelist(".*Permission");
		addToWhitelist("org.w3c.*");
		addToWhitelist("io.sf.carte.echosvg.w3c.*");
		addToWhitelist("io.sf.carte.echosvg.anim.*");
		addToWhitelist("io.sf.carte.echosvg.dom.*");
		addToWhitelist("io.sf.carte.echosvg.css.*");
		addToWhitelist("io.sf.carte.echosvg.util.*");
	}

	public RhinoClassShutter() {
		super();
	}

	/**
	 * Add the given regular expression to the whitelist.
	 * 
	 * @param regex the regular expression.
	 * @throws PatternSyntaxException - if the regular expression's syntax is
	 *                                invalid
	 */
	public static void addToWhitelist(String regex) throws PatternSyntaxException {
		if (regex == null || (regex = regex.trim()).length() == 0) {
			return;
		}

		for (Pattern p : whitelist) {
			if (regex.equals(p.pattern())) {
				// Already here
				return;
			}
		}

		Pattern p = Pattern.compile(regex);
		whitelist.add(p);
	}

	/**
	 * Remove the given regular expression from the whitelist.
	 * 
	 * @param regex the regular expression.
	 * @throws PatternSyntaxException - if the regular expression's syntax is
	 *                                invalid
	 */
	public static void removeFromWhitelist(String regex) throws PatternSyntaxException {
		if (regex == null || (regex = regex.trim()).length() == 0) {
			return;
		}
		for (Pattern p : whitelist) {
			if (regex.equals(p.pattern())) {
				whitelist.remove(p);
				break;
			}
		}
	}

	/**
	 * Loads a whitelist from the given reader.
	 * <p>
	 * Loading a whitelist does not reset the list, and only applies the new entries
	 * to it.
	 * </p>
	 * <ul>
	 * <li>If an entry begins with a {@code #}, the line will be ignored.</li>
	 * <li>If an entry starts with a {@code -}, the entry shall be removed.</li>
	 * <li>If begins with a {@code +} or with a character different from {@code -}
	 * and {@code #}, the entry shall be added.</li>
	 * </ul>
	 * <p>
	 * Example:
	 * </p>
	 * 
	 * <pre>
	 * # Comment
	 * +java.io.PrintWriter
	 * +com.example.*
	 * -java.lang.System
	 * </pre>
	 * 
	 * @param reader the reader.
	 * @throws IOException            - if an I/O problem occurs when reading the
	 *                                list
	 * @throws PatternSyntaxException - if a regular expression's syntax is invalid
	 */
	public static void loadWhitelist(Reader reader) throws IOException, PatternSyntaxException {
		BufferedReader re = new BufferedReader(reader);
		String s;
		while ((s = re.readLine()) != null) {
			int len = s.length();
			if (len > 0) {
				char c = s.charAt(0);
				if (c == '-') {
					removeFromWhitelist(s.substring(1, len));
				} else if (c == '+') {
					addToWhitelist(s.substring(1, len));
				} else if (c != '#') {
					addToWhitelist(s);
				}
			}
		}
	}

	/**
	 * Returns whether the given class is visible to scripts.
	 */
	@Override
	public boolean visibleToScripts(String fullClassName) {
		// Don't let them mess with script engine's internals.
		if (fullClassName.startsWith("org.mozilla.javascript"))
			return "org.mozilla.javascript.EcmaError".equals(fullClassName);

		if (fullClassName.startsWith("io.sf.carte.echosvg.")) {
			// Just get package within this implementation.
			String implPkg = fullClassName.substring(20);

			// Don't let them mess with this implementation's script internals.
			if (implPkg.startsWith("script"))
				return false;

			// Don't let them get global structures.
			if (implPkg.startsWith("apps"))
				return false;

			// Don't let them get scripting stuff from bridge, but specifically
			// allow access to:
			//
			// i.s.c.e.bridge.ScriptingEnvironment$Window$IntervalScriptTimerTask
			// i.s.c.e.bridge.ScriptingEnvironment$Window$IntervalRunnableTimerTask
			// i.s.c.e.bridge.ScriptingEnvironment$Window$TimeoutScriptTimerTask
			// i.s.c.e.bridge.ScriptingEnvironment$Window$TimeoutRunnableTimerTask
			//
			// since objects of these classes are returned by setInterval() and
			// setTimeout().
			if (implPkg.startsWith("bridge.")) {
				String implBridgeClass = implPkg.substring(7);
				if (implBridgeClass.startsWith("ScriptingEnvironment")) {
					if (implBridgeClass.startsWith("$Window$", 20)) {
						String c = implBridgeClass.substring(28);
						if (c.equals("IntervalScriptTimerTask")
								|| c.equals("IntervalRunnableTimerTask")
								|| c.equals("TimeoutScriptTimerTask")
								|| c.equals("TimeoutRunnableTimerTask")) {
							return true;
						}
					}
					return false;
				}
				if (implBridgeClass.startsWith("BaseScriptingEnvironment")) {
					return false;
				}
			}
		}

		for (Pattern p : whitelist) {
			if (p.matcher(fullClassName).matches()) {
				return true;
			}
		}

		return false;
	}

}
