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
package io.sf.carte.echosvg.ext.awt.image.codec.impl;

/**
 * Methods useful for codecs.
 *
 * @version $Id$
 */
public class CodecUtil {

	/**
	 * Avoid instantiation.
	 */
	CodecUtil() {
		super();
	}

	/**
	 * Checks whether {@code checkMe} starts with the contents of {@code expect}.
	 * 
	 * @param expect    the array with the expected elements.
	 * @param checkMe   the array to check.
	 * @param fromIndex the index of {@code checkMe} at which the check should
	 *                  start.
	 * @return {@code true} if {@code checkMe} starts with {@code expect}.
	 */
	public static boolean arrayStartsWith(byte[] expect, byte[] checkMe, int fromIndex) {
		if (checkMe.length - fromIndex < expect.length) {
			return false;
		}
		for (int i = 0; i < expect.length; i++) {
			if (expect[i] != checkMe[fromIndex + i]) {
				return false;
			}
		}
		return true;
	}

}
