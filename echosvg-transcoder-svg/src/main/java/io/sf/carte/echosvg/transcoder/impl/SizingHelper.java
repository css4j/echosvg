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
package io.sf.carte.echosvg.transcoder.impl;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValue.CssType;
import io.sf.carte.doc.style.css.property.Evaluator;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.doc.style.css.property.ValueList;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * Helper methods for SVG sizing.
 */
public class SizingHelper {

	// Prevent instantiation
	private SizingHelper() {
		super();
	}

	/**
	 * Apply the CSS context rules.
	 * <p>
	 * See <a href="https://svgwg.org/specs/integration/#svg-css-sizing">Sizing SVG
	 * content in CSS context</a>.
	 * </p>
	 * <p>
	 * To resolve 'auto' value on ‘svg’ element if the ‘viewBox’ attribute is not
	 * specified:
	 * </p>
	 * <ul>
	 * <li>Use author specified width and height ‘svg’ element attributes.</li>
	 * <li>If any of the sizing attributes are missing, resolve the missing ‘svg’
	 * element width to '300px' and missing height to '150px' (using CSS 2.1
	 * replaced elements size calculation).</li>
	 * </ul>
	 * <p>
	 * To resolve 'auto' value on ‘svg’ element if the ‘viewBox’ attribute is
	 * specified:
	 * </p>
	 * <ul>
	 * <li>Use the ‘viewBox’ attribute to calculate the intrinsic aspect ratio of
	 * the ‘svg’ element.</li>
	 * <li>If the width or height attributes are not specified - keep not specified
	 * width or height as 'auto'.</li>
	 * </ul>
	 * 
	 * @param svgRoot the outer svg element.
	 */
	public static void defaultDimensions(Element svgRoot) {
		String width = svgRoot.getAttribute("width").trim();
		String height = svgRoot.getAttribute("height").trim();

		if (width.isEmpty() || CSSConstants.CSS_AUTO_VALUE.equalsIgnoreCase(width)) {
			defaultWidth(svgRoot, height);
		}

		if (height.isEmpty() || CSSConstants.CSS_AUTO_VALUE.equalsIgnoreCase(height)) {
			String viewBox = svgRoot.getAttribute("viewBox").trim();
			if (viewBox.isEmpty()) {
				svgRoot.setAttribute("height", "150px");
			}
		}
	}

	private static void defaultWidth(Element svgRoot, String height) {
		String viewBox = svgRoot.getAttribute("viewBox").trim();
		String width;
		if (viewBox.isEmpty()) {
			width = "300px";
		} else if (!height.isEmpty() && !CSSConstants.CSS_AUTO_VALUE.equalsIgnoreCase(height)) {
			try {
				float ratio = computeAspectRatio(viewBox);
				float fh = floatValue(height);
				if (!Float.isNaN(ratio) && !Float.isNaN(fh)) {
					width = Float.toString(ratio * fh);
				} else {
					width = "300px";
				}
			} catch (TranscoderException e) {
				// Leave as it is, maybe the value has to be computed
				return;
			} catch (Exception e) {
				// Any other exception: set to 300
				width = "300px";
			}
		} else {
			return;
		}

		svgRoot.setAttribute("width", width);
	}

	static float computeAspectRatio(String viewBox) throws DOMException {
		ValueFactory factory = new ValueFactory();
		StyleValue value = factory.parseProperty(viewBox);
		float[] numbers = new float[4];
		if (!computeRectangle(value, numbers)) {
			throw new DOMException(DOMException.SYNTAX_ERR, "Wrong viewBox attribute.");
		}
		return numbers[2] / numbers[3];
	}

	static boolean computeRectangle(StyleValue value, float[] numbers) throws DOMException {
		if (value.getCssValueType() != CssType.LIST) {
			return false;
		}
		ValueList list = (ValueList) value;
		if (list.getLength() != 4) {
			return false;
		}

		for (int i = 0; i < 4; i++) {
			StyleValue item = list.item(i);
			if (item.getCssValueType() != CssType.TYPED) {
				return false;
			}
			CSSTypedValue typed;
			switch (item.getPrimitiveType()) {
			case NUMERIC:
				typed = (CSSTypedValue) item;
				if (typed.getUnitType() != CSSUnit.CSS_NUMBER) {
					return false;
				}
				break;
			case EXPRESSION:
				Evaluator eval = new Evaluator(CSSUnit.CSS_NUMBER);
				typed = eval.evaluateExpression((CSSExpressionValue) item);
				if (typed.getUnitType() != CSSUnit.CSS_NUMBER) {
					return false;
				}
				break;
			default:
				return false;
			}
			numbers[i] = typed.getFloatValue(CSSUnit.CSS_NUMBER);
		}
		return true;
	}

	static float floatValue(String number) throws TranscoderException, DOMException {
		ValueFactory factory = new ValueFactory();
		StyleValue value = factory.parseProperty(number);

		if (value.getCssValueType() != CssType.TYPED) {
			throw new TranscoderException("Leave value unchanged.");
		}

		CSSTypedValue typed;
		switch (value.getPrimitiveType()) {
		case NUMERIC:
			typed = (CSSTypedValue) value;
			if (typed.getUnitType() != CSSUnit.CSS_NUMBER) {
				if (CSSUnit.isRelativeLengthUnitType(typed.getUnitType())) {
					throw new TranscoderException("Leave value unchanged.");
				}
				// Either an absolute length or a wrong value
				return typed.getFloatValue(CSSUnit.CSS_PX);
			}
			break;
		case EXPRESSION:
			Evaluator eval = new Evaluator(CSSUnit.CSS_NUMBER);
			typed = eval.evaluateExpression((CSSExpressionValue) value);
			if (typed.getUnitType() != CSSUnit.CSS_NUMBER) {
				if (CSSUnit.isRelativeLengthUnitType(typed.getUnitType())) {
					throw new TranscoderException("Leave value unchanged.");
				}
				// Either an absolute length or a wrong value
				return typed.getFloatValue(CSSUnit.CSS_PX);
			}
			break;
		default:
			throw new DOMException(DOMException.SYNTAX_ERR, "Wrong dimension.");
		}

		return typed.getFloatValue(CSSUnit.CSS_NUMBER);
	}

}
