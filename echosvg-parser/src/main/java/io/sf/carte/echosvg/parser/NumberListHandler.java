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
package io.sf.carte.echosvg.parser;

/**
 * An handler interface for parsing NumberLists.
 *
 * @author tonny@kiyut.com
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface NumberListHandler {
	/**
	 * Invoked when the number list attribute starts.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 *                           list.
	 */
	void startNumberList() throws ParseException;

	/**
	 * Invoked when the number list attribute ends.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 *                           list.
	 */
	void endNumberList() throws ParseException;

	/**
	 * Invoked when the number attribute starts.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 */
	void startNumber() throws ParseException;

	/**
	 * Invoked when the number attribute ends.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 */
	void endNumber() throws ParseException;

	/**
	 * Invoked when a float value has been parsed.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 */
	void numberValue(float v) throws ParseException;
}
