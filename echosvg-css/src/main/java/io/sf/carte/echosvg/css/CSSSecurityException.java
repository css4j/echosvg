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
package io.sf.carte.echosvg.css;

/**
 * A CSS-related security exception.
 */
public class CSSSecurityException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@code CSSSecurityException} with no detail message.
	 */
	public CSSSecurityException() {
		super();
	}

	/**
	 * Creates a {@code CSSSecurityException} with the specified cause and a detail
	 * message which typically contains the class and detail message of cause.
	 * 
	 * @param cause the cause.
	 */
	public CSSSecurityException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@code CSSSecurityException} with a detail message.
	 * 
	 * @param message the message.
	 */
	public CSSSecurityException(String message) {
		super(message);
	}

	/**
	 * Creates a {@code CSSSecurityException} with the specified detail message and
	 * cause.
	 * 
	 * @param message the message.
	 * @param cause   the cause.
	 */
	public CSSSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

}
