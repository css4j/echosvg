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
package io.sf.carte.echosvg.util.gui.resource;

/**
 * Signals a missing listener
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class MissingListenerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * The class name of the listener bundle requested
	 * 
	 * @serial
	 */
	private String className;

	/**
	 * The name of the specific listener requested by the user
	 * 
	 * @serial
	 */
	private String key;

	/**
	 * Constructs a MissingListenerException with the specified information. A
	 * detail message is a String that describes this particular exception.
	 * 
	 * @param s         the detail message
	 * @param className the name of the listener class
	 * @param key       the key for the missing listener.
	 */
	public MissingListenerException(String s, String className, String key) {
		super(s);
		this.className = className;
		this.key = key;
	}

	/**
	 * Gets parameter passed by constructor.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets parameter passed by constructor.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns a printable representation of this object
	 */
	@Override
	public String toString() {
		return super.toString() + " (" + getKey() + ", bundle: " + getClassName() + ")";
	}

}
