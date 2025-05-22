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

package io.sf.carte.echosvg.css.engine.value.css;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

import io.sf.carte.doc.style.css.CSSExpressionValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.CSSValueSyntax.Match;
import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.parser.CSSParser;
import io.sf.carte.doc.style.css.parser.SyntaxParser;
import io.sf.carte.doc.style.css.property.StyleValue;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStylableElement;
import io.sf.carte.echosvg.css.engine.StyleMap;
import io.sf.carte.echosvg.css.engine.value.AbstractValueFactory;
import io.sf.carte.echosvg.css.engine.value.CSSProxyValueException;
import io.sf.carte.echosvg.css.engine.value.CalcValue;
import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.IdentifierManager;
import io.sf.carte.echosvg.css.engine.value.PendingValue;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.StringMap;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class provides support for the CSS 'font' shorthand property.
 *
 * The form of this property is: [ [ &lt;font-style&gt; || &lt;font-variant&gt;
 * || &lt;font-weight&gt; ]? &lt;font-size&gt; [ / &lt;line-height&gt; ]?
 * &lt;font-family&gt; ] | caption | icon | menu | message-box | small-caption |
 * status-bar | inherit
 *
 * It is worth noting that there is a potential ambiguity between font-size and
 * font-weight since in SVG they can both be unitless. This is solved by
 * considering the 'last' number before an 'ident' or '/' to be font-size and
 * any preceding number to be font-weight.
 *
 * <p>
 * Original author: <a href="mailto:deweese@apache.org">deweese</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class FontShorthandManager extends AbstractValueFactory implements ShorthandManager {

	public FontShorthandManager() {
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_PROPERTY;
	}

	/**
	 * Implements {@link ShorthandManager#isAnimatableProperty()}.
	 */
	@Override
	public boolean isAnimatableProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#isAdditiveProperty()}.
	 */
	@Override
	public boolean isAdditiveProperty() {
		return false;
	}

	static LexicalUnit NORMAL_LU;

	static LexicalUnit BOLD_LU;

	static LexicalUnit MEDIUM_LU;

	static LexicalUnit SZ_10PT_LU;
	static LexicalUnit SZ_8PT_LU;

	static LexicalUnit FONT_FAMILY_LU;

	static {
		CSSParser parser = new CSSParser();
		parser.setFlag(Parser.Flag.VALUE_COMMENTS_IGNORE);
		try {
			NORMAL_LU = parser.parsePropertyValue(new StringReader(CSSConstants.CSS_NORMAL_VALUE));
			BOLD_LU = parser.parsePropertyValue(new StringReader(CSSConstants.CSS_BOLD_VALUE));
			MEDIUM_LU = parser.parsePropertyValue(new StringReader(CSSConstants.CSS_MEDIUM_VALUE));
			SZ_10PT_LU = parser.parsePropertyValue(new StringReader("10pt"));
			SZ_8PT_LU = parser.parsePropertyValue(new StringReader("8pt"));
			FONT_FAMILY_LU = parser.parsePropertyValue(
					new StringReader("\"Dialog\",\"Helvetica\"," + CSSConstants.CSS_SANS_SERIF_VALUE));
		} catch (CSSParseException | IOException e) {
		}
	}

	protected static final Set<String> values = new HashSet<>();
	static {
		values.add(CSSConstants.CSS_CAPTION_VALUE);
		values.add(CSSConstants.CSS_ICON_VALUE);
		values.add(CSSConstants.CSS_MENU_VALUE);
		values.add(CSSConstants.CSS_MESSAGE_BOX_VALUE);
		values.add(CSSConstants.CSS_SMALL_CAPTION_VALUE);
		values.add(CSSConstants.CSS_STATUS_BAR_VALUE);
	}

	public void handleSystemFont(CSSEngine eng, ShorthandManager.PropertyHandler ph, String s, boolean imp) {

		LexicalUnit fontStyle = NORMAL_LU;
		LexicalUnit fontVariant = NORMAL_LU;
		LexicalUnit fontWeight = NORMAL_LU;
		LexicalUnit lineHeight = NORMAL_LU;
		LexicalUnit fontFamily = FONT_FAMILY_LU;

		LexicalUnit fontSize;
		if (s.equals(CSSConstants.CSS_SMALL_CAPTION_VALUE)) {
			fontSize = SZ_8PT_LU;
		} else {
			fontSize = SZ_10PT_LU;
		}
		ph.property(CSSConstants.CSS_FONT_FAMILY_PROPERTY, fontFamily, imp);
		ph.property(CSSConstants.CSS_FONT_STYLE_PROPERTY, fontStyle, imp);
		ph.property(CSSConstants.CSS_FONT_VARIANT_PROPERTY, fontVariant, imp);
		ph.property(CSSConstants.CSS_FONT_WEIGHT_PROPERTY, fontWeight, imp);
		ph.property(CSSConstants.CSS_FONT_SIZE_PROPERTY, fontSize, imp);
		ph.property(CSSConstants.CSS_LINE_HEIGHT_PROPERTY, lineHeight, imp);
	}

	/**
	 * Implements
	 * {@link ShorthandManager#setValues(CSSEngine,ShorthandManager.PropertyHandler,LexicalUnit,boolean)}.
	 */
	@Override
	public void setValues(CSSEngine eng, ShorthandManager.PropertyHandler ph, final LexicalUnit lunit,
			boolean imp) {
		switch (lunit.getLexicalUnitType()) {
		case IDENT: {
			String s = lunit.getStringValue().toLowerCase(Locale.ROOT);
			if (values.contains(s)) {
				handleSystemFont(eng, ph, s, imp);
				return;
			}
			break;
		}

		case INHERIT:
		case UNSET:
		case REVERT:
			return;

		case VAR:
		case ATTR:
			setPendingLonghands(eng, ph, lunit, imp);
			return;

		default:
			break;
		}

		LexicalUnit fontStyle = null;
		LexicalUnit fontVariant = null;
		LexicalUnit fontWeight = null;
		LexicalUnit fontSize = null;
		LexicalUnit lineHeight = null;
		LexicalUnit fontFamily = null;

		ValueManager[] vMgrs = eng.getValueManagers();
		int fst, fv, fw, fsz;
		fst = eng.getPropertyIndex(CSSConstants.CSS_FONT_STYLE_PROPERTY);
		fv = eng.getPropertyIndex(CSSConstants.CSS_FONT_VARIANT_PROPERTY);
		fw = eng.getPropertyIndex(CSSConstants.CSS_FONT_WEIGHT_PROPERTY);
		fsz = eng.getFontSizeIndex();

		IdentifierManager fstVM = (IdentifierManager) vMgrs[fst];
		IdentifierManager fvVM = (IdentifierManager) vMgrs[fv];
		IdentifierManager fwVM = (IdentifierManager) vMgrs[fw];
		FontSizeManager fszVM = (FontSizeManager) vMgrs[fsz];

		StringMap<Value> fstSM = fstVM.getIdentifiers();
		StringMap<Value> fvSM = fvVM.getIdentifiers();
		StringMap<Value> fwSM = fwVM.getIdentifiers();
		StringMap<Value> fszSM = fszVM.getIdentifiers();

		// Check for font-style, font-variant, & font-weight
		// These are all optional.

		boolean svwDone = false;
		LexicalUnit lu = lunit;
		LexicalUnit intLU = null;
		while (!svwDone && (lu != null)) {
			switch (lu.getLexicalUnitType()) {
			case IDENT: {
				String s = lu.getStringValue().toLowerCase(Locale.ROOT).intern();
				if (fontStyle == null && fstSM.get(s) != null) {
					fontStyle = lu;
					if (intLU != null) {
						if (fontWeight == null) {
							fontWeight = intLU;
							intLU = null;
						} else {
							throw createInvalidLexicalUnitDOMException(intLU.getLexicalUnitType());
						}
					}
					break;
				}

				if (fontVariant == null && fvSM.get(s) != null) {
					fontVariant = lu;
					if (intLU != null) {
						if (fontWeight == null) {
							fontWeight = intLU;
							intLU = null;
						} else {
							throw createInvalidLexicalUnitDOMException(intLU.getLexicalUnitType());
						}
					}
					break;
				}

				if (intLU == null && fontWeight == null && fwSM.get(s) != null) {
					fontWeight = lu;
					break;
				}

				svwDone = true;
				break;
			}
			case INTEGER:
				if (intLU == null && fontWeight == null) {
					intLU = lu;
					break;
				}
				svwDone = true;
				break;

			case VAR:
			case ATTR:
				setPendingLonghands(eng, ph, lunit, imp);
				return;

			default: // All other must be size,'/line-height', family
				svwDone = true;
				break;
			}
			if (!svwDone)
				lu = lu.getNextLexicalUnit();
		}

		// Must have font-size.
		if (lu == null)
			throw createMalformedLexicalUnitDOMException();

		// Now we need to get font-size
		switch (lu.getLexicalUnitType()) {
		case IDENT: {
			String s = lu.getStringValue().toLowerCase(Locale.ROOT).intern();
			if (fszSM.get(s) != null) {
				fontSize = lu; // This is a font-size ident.
				lu = lu.getNextLexicalUnit();
			}
		}
			break;

		case DIMENSION:
			if (!CSSUnit.isLengthUnitType(lu.getCssUnit())) {
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}
		case INTEGER:
		case REAL:
		case PERCENTAGE:
			fontSize = lu;
			lu = lu.getNextLexicalUnit();
			break;

		case VAR:
		case ATTR:
			setPendingLonghands(eng, ph, lunit, imp);
			return;

		case CALC:
			Value calc;
			try {
				calc = createFontSizeCalc(lu);
			} catch (CSSProxyValueException e) {
				setPendingLonghands(eng, ph, lunit, imp);
				return;
			}
			if (calc.getPrimitiveType() != Type.EXPRESSION
					|| calc.matches(new SyntaxParser().parseSyntax("<length-percentage>")) != Match.TRUE) {
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}
			fontSize = lu;
			lu = lu.getNextLexicalUnit();

		default:
			break;
		}

		if (fontSize == null) {
			// We must have a font-size so see if we can use intLU...
			if (intLU != null) {
				fontSize = intLU; // Yup!
				intLU = null;
			} else {
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}
		}

		if (intLU != null) {
			// We have a intLU left see if we can use it as font-weight
			if (fontWeight == null) {
				fontWeight = intLU; // use intLU as font-weight.
			} else {
				// we have an 'extra' integer in property.
				throw createInvalidLexicalUnitDOMException(intLU.getLexicalUnitType());
			}
		}

		// Must have Font-Family, so if it's null now we are done!
		if (lu == null)
			throw createMalformedLexicalUnitDOMException();

		// Now at this point we want to look for
		// line-height.
		switch (lu.getLexicalUnitType()) {
		case OPERATOR_SLASH: // we have line-height
			lu = lu.getNextLexicalUnit();
			if (lu == null) // OOPS!
				throw createMalformedLexicalUnitDOMException();
			lineHeight = lu;
			lu = lu.getNextLexicalUnit();
			break;

		case VAR:
		case ATTR:
			setPendingLonghands(eng, ph, lunit, imp);
			return;

		default:
			break;
		}

		// Must have Font-Family, so if it's null now we are done!
		if (lu == null)
			throw createMalformedLexicalUnitDOMException();
		fontFamily = lu;

		if (fontStyle == null)
			fontStyle = NORMAL_LU;
		if (fontVariant == null)
			fontVariant = NORMAL_LU;
		if (fontWeight == null)
			fontWeight = NORMAL_LU;
		if (lineHeight == null)
			lineHeight = NORMAL_LU;

		try {
			ph.property(CSSConstants.CSS_FONT_FAMILY_PROPERTY, fontFamily, imp);
			ph.property(CSSConstants.CSS_FONT_STYLE_PROPERTY, fontStyle, imp);
			ph.property(CSSConstants.CSS_FONT_VARIANT_PROPERTY, fontVariant, imp);
			ph.property(CSSConstants.CSS_FONT_WEIGHT_PROPERTY, fontWeight, imp);
			ph.property(CSSConstants.CSS_FONT_SIZE_PROPERTY, fontSize, imp);
			ph.property(CSSConstants.CSS_LINE_HEIGHT_PROPERTY, lineHeight, imp);
		} catch (CSSProxyValueException e) {
			setPendingLonghands(eng, ph, lunit, imp);
		}
	}

	private Value createFontSizeCalc(LexicalUnit lu) throws DOMException {
		ValueFactory vf = new ValueFactory();
		StyleValue cssValue = vf.createCSSValue(lu.shallowClone());

		Type pType = cssValue.getPrimitiveType();
		if (pType != Type.EXPRESSION) {
			if (pType == Type.LEXICAL) {
				throw new CSSProxyValueException();
			}
			createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}

		CalcValue calc = new CalcValue((CSSExpressionValue) cssValue) {

			private static final long serialVersionUID = 1L;

			@Override
			protected FloatValue absoluteValue(CSSStylableElement elt, String pseudo, CSSEngine engine,
					int idx, StyleMap sm, FloatValue relative) {
				return (FloatValue) new FontSizeManager().computeValue(elt, pseudo, engine, idx, sm,
						relative);
			}

		};

		return calc;
	}

	private void setPendingLonghands(CSSEngine eng, PropertyHandler ph, LexicalUnit lunit, boolean imp) {
		PendingValue pending = new PendingValue(getPropertyName(), lunit);
		ph.pendingValue(CSSConstants.CSS_FONT_FAMILY_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_FONT_STYLE_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_FONT_VARIANT_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_FONT_WEIGHT_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_FONT_SIZE_PROPERTY, pending, imp);
		ph.pendingValue(CSSConstants.CSS_LINE_HEIGHT_PROPERTY, pending, imp);
	}

}
