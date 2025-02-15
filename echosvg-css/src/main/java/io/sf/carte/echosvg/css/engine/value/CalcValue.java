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

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.property.Evaluator;

/**
 * {@code calc()} value.
 *
 * @author See Git history.
 * @version $Id$
 */
public class CalcValue extends NumericDelegateValue<CSSExpressionValue> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new value.
	 */
	public CalcValue(CSSExpressionValue expr) {
		super(expr);
	}

	@Override
	public Type getPrimitiveType() {
		return Type.EXPRESSION;
	}

	@Override
	protected CSSTypedValue evaluate(Evaluator eval) {
		return eval.evaluateExpression(getNumericDelegate());
	}

}
