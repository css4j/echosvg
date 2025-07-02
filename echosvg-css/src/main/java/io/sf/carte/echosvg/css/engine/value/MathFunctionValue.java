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
package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSMathFunctionValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.property.Evaluator;

/**
 * Mathematical function value.
 *
 * @author See Git history.
 * @version $Id$
 */
public class MathFunctionValue extends NumericDelegateValue<CSSMathFunctionValue> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new value.
	 */
	public MathFunctionValue(CSSMathFunctionValue func) {
		super(func);
	}

	@Override
	public Type getPrimitiveType() {
		return Type.MATH_FUNCTION;
	}

	@Override
	protected CSSTypedValue evaluate(Evaluator eval) throws DOMException {
		return eval.evaluateFunction(getNumericDelegate());
	}

}
