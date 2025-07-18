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
 * This class represents a list of rules.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class StyleSheet extends MediaGroup {

	/**
	 * Whether or not this stylesheet is alternate.
	 */
	protected boolean alternate;

	/**
	 * The style sheet title.
	 */
	protected String title;

	static final short TYPE = -1;

	@Override
	short getType() {
		return TYPE;
	}

	/**
	 * Sets the 'alternate' attribute of this style-sheet.
	 */
	public void setAlternate(boolean b) {
		alternate = b;
	}

	/**
	 * Tells whether or not this stylesheet is alternate.
	 */
	public boolean isAlternate() {
		return alternate;
	}

	/**
	 * Sets the 'title' attribute of this style-sheet.
	 */
	public void setTitle(String t) {
		title = t;
	}

	/**
	 * Returns the title of this style-sheet.
	 */
	public String getTitle() {
		return title;
	}

}
