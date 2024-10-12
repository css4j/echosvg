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

import org.w3c.css.om.unit.CSSUnit;

/**
 * This class provides an adapter for LengthHandler
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultLengthHandler implements LengthHandler {

	/**
	 * This class does not need to be instantiated.
	 */
	protected DefaultLengthHandler() {
	}

	/**
	 * Implements {@link LengthHandler#startLength()}.
	 */
	@Override
	public void startLength() throws ParseException {
	}

	/**
	 * Implements {@link LengthHandler#lengthValue(float)}.
	 */
	@Override
	public void lengthValue(float v) throws ParseException {
	}

	@Override
	public void em() throws ParseException {
		setUnit(CSSUnit.CSS_EM);
	}

	@Override
	public void ex() throws ParseException {
		setUnit(CSSUnit.CSS_EX);
	}

	@Override
	public void lh() throws ParseException {
		setUnit(CSSUnit.CSS_LH);
	}

	@Override
	public void in() throws ParseException {
		setUnit(CSSUnit.CSS_IN);
	}

	@Override
	public void cm() throws ParseException {
		setUnit(CSSUnit.CSS_CM);
	}

	@Override
	public void mm() throws ParseException {
		setUnit(CSSUnit.CSS_MM);
	}

	@Override
	public void pc() throws ParseException {
		setUnit(CSSUnit.CSS_PC);
	}

	@Override
	public void pt() throws ParseException {
		setUnit(CSSUnit.CSS_PT);
	}

	@Override
	public void px() throws ParseException {
		setUnit(CSSUnit.CSS_PX);
	}

	@Override
	public void percentage() throws ParseException {
		setUnit(CSSUnit.CSS_PERCENTAGE);
	}

	@Override
	public void vh() throws ParseException {
		setUnit(CSSUnit.CSS_VH);
	}

	@Override
	public void vw() throws ParseException {
		setUnit(CSSUnit.CSS_VW);
	}

	@Override
	public void vmax() throws ParseException {
		setUnit(CSSUnit.CSS_VMAX);
	}

	@Override
	public void vmin() throws ParseException {
		setUnit(CSSUnit.CSS_VMIN);
	}

	protected void setUnit(short unit) {
	}

	/**
	 * Implements {@link LengthHandler#endLength()}.
	 */
	@Override
	public void endLength() throws ParseException {
	}

}
