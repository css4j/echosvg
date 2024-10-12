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
 * This interface must be implemented and then registered as the handler of a
 * <code>LengthParser</code> instance in order to be notified of parsing events.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public interface LengthHandler {

	/**
	 * Invoked when the length attribute starts.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void startLength() throws ParseException;

	/**
	 * Invoked when a float value has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void lengthValue(float v) throws ParseException;

	/**
	 * Invoked when 'em' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void em() throws ParseException;

	/**
	 * Invoked when 'ex' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void ex() throws ParseException;

	/**
	 * Invoked when 'lh' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void lh() throws ParseException;

	/**
	 * Invoked when 'in' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void in() throws ParseException;

	/**
	 * Invoked when 'cm' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void cm() throws ParseException;

	/**
	 * Invoked when 'mm' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void mm() throws ParseException;

	/**
	 * Invoked when 'pc' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void pc() throws ParseException;

	/**
	 * Invoked when 'pt' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void pt() throws ParseException;

	/**
	 * Invoked when 'px' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void px() throws ParseException;

	/**
	 * Invoked when '%' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void percentage() throws ParseException;

	/**
	 * Invoked when 'vh' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void vh() throws ParseException;

	/**
	 * Invoked when 'vw' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void vw() throws ParseException;

	/**
	 * Invoked when 'vmax' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void vmax() throws ParseException;

	/**
	 * Invoked when 'vmin' has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void vmin() throws ParseException;

	/**
	 * Invoked when the length attribute ends.
	 * 
	 * @exception ParseException if an error occurs while processing the length
	 */
	void endLength() throws ParseException;

}
