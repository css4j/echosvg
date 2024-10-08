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
package io.sf.carte.echosvg.css.engine;

/**
 * A counter to prevent excessive recursions and replacements.
 */
class CounterRef {

	public CounterRef() {
		super();
	}


	private static final int MAX_RECURSION = 512;

	// Recursion counter
	private int counter = 0;

	// Counter for replaceBy()
	int replaceCounter = 0;

	boolean increment() {
		counter++;
		if (isInRange()) {
			return true;
		}
		// Give a small margin for further operations
		counter -= 8;
		return false;
	}

	private boolean isInRange() {
		return counter < MAX_RECURSION;
	}

}
