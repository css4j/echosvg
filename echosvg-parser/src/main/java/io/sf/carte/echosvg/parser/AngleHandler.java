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
 * This interface must be implemented and then registred as the handler of a
 * <code>AngleParser</code> instance in order to be notified of parsing events.
 *  <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public interface AngleHandler {

	/**
	 * Invoked when the angle attribute parsing starts.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void startAngle() throws ParseException;

	/**
	 * Invoked when a float value has been parsed.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void angleValue(float v) throws ParseException;

	/**
	 * Invoked when 'deg' has been parsed.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void deg() throws ParseException;

	/**
	 * Invoked when 'grad' has been parsed.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void grad() throws ParseException;

	/**
	 * Invoked when 'rad' has been parsed.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void rad() throws ParseException;

	/**
	 * Invoked when 'turn' has been parsed.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void turn() throws ParseException;

	/**
	 * Invoked when the angle attribute parsing ends.
	 * 
	 * @exception ParseException if an error occured while processing the angle
	 */
	void endAngle() throws ParseException;

}
