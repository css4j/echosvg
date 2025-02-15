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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.w3c.api.DOMSyntaxException;
import org.w3c.css.om.typed.CSSColor;
import org.w3c.css.om.typed.CSSStyleValueList;
import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.DOMException;

/**
 * color() function.
 *
 * @author See Git history.
 * @version $Id$
 */
public class ColorFunction extends ColorValue implements CSSColor {

	private static final long serialVersionUID = 1L;

	private IdentValue colorSpace;

	/**
	 * The channels.
	 */
	private AbstractValueList<NumericValue> channels;

	private static final Set<String> predefinedSpaces;

	static {
		predefinedSpaces = new HashSet<>(9);
		predefinedSpaces.add(ColorValue.CS_SRGB);
		predefinedSpaces.add(ColorValue.CS_SRGB_LINEAR);
		predefinedSpaces.add(ColorValue.CS_A98_RGB);
		predefinedSpaces.add(ColorValue.CS_DISPLAY_P3);
		predefinedSpaces.add(ColorValue.CS_PROPHOTO_RGB);
		predefinedSpaces.add(ColorValue.CS_REC2020);
		predefinedSpaces.add(ColorValue.CS_XYZ);
		predefinedSpaces.add(ColorValue.CS_XYZ_D50);
		predefinedSpaces.add(ColorValue.CS_XYZ_D65);
	}

	/**
	 * Creates a new ColorFunction.
	 * 
	 * @param colorSpace the color space.
	 * @param channels a channel list to copy from.
	 * @throws DOMSyntaxException if a supplied component is invalid.
	 */
	public ColorFunction(String colorSpace, CSSStyleValueList<NumericValue> channels)
			throws DOMSyntaxException {
		super();
		this.colorSpace = new IdentValue(canonicalName(colorSpace));
		componentize(this.colorSpace);
		componentizeChannels(channels);
	}

	private void componentizeChannels(CSSStyleValueList<NumericValue> channels) {
		// Clone channels if the list belongs to some other value
		boolean clone = channels instanceof AbstractValue && ((AbstractValue) channels).handler != null;
		int len = channels.getLength();
		this.channels = new AbstractValueList<>(' ', len);
		for (NumericValue channel : channels) {
			if (clone) {
				channel = channel.clone();
			}
			this.channels.add(numericComponent(channel));
		}
		componentize(this.channels);
	}

	/**
	 * Get the color space as defined by the Typed OM specification.
	 * 
	 * @return the color space.
	 */
	@Override
	public IdentValue getColorSpace() {
		return colorSpace;
	}

	@Override
	public String getCSSColorSpace() {
		return colorSpace.getValue();
	}

	@Override
	public void setColorSpace(String cs) {
		colorSpace.setValue(canonicalName(cs));
		componentChanged(colorSpace);
	}

	static String canonicalName(String cs) throws DOMException {
		cs = cs.toLowerCase(Locale.ROOT).intern();
		if (!predefinedSpaces.contains(cs)) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
					"Unsupported color space: " + cs);
		}
		return cs;
	}

	@Override
	public CSSStyleValueList<NumericValue> getChannels() {
		return channels;
	}

	@Override
	public int getLength() throws DOMException {
		return channels.getLength() + 2;
	}

	@Override
	public Value item(int index) throws DOMException {
		if (index == 0) {
			return getColorSpace();
		} else if (index == channels.getLength() + 1) {
			return getAlpha();
		}
		return channels.item(index - 1);
	}

	@Override
	public ColorFunction getColorValue() {
		return this;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		StringBuilder buf = new StringBuilder(15 + colorSpace.value.length() + channels.getLength() * 8);
		buf.append("color(").append(colorSpace.getCssText());
		for (NumericValue ch : channels) {
			buf.append(' ').append(ch.getCssText());
		}
		if (!isOpaque()) {
			buf.append(" / ").append(getAlpha().getCssText());
		}
		buf.append(')');
		return buf.toString();
	}

	/**
	 * Initialize a numeric component of this value.
	 * 
	 * @param ch the component.
	 * @return the initialized component.
	 * @throws DOMSyntaxException if the value is inadequate for a component.
	 */
	private NumericValue numericComponent(NumericValue ch) throws DOMSyntaxException {
		if (ch.getUnitType() != CSSUnit.CSS_PERCENTAGE && ch.getUnitType() != CSSUnit.CSS_NUMBER) {
			throw new DOMSyntaxException("color() component must be a number or percentage.");
		}
		componentize(ch);
		return ch;
	}

	@Override
	public ColorFunction clone() {
		ColorFunction clon;
		try {
			clon = new ColorFunction(getColorSpace().getValue(), this.channels);
		} catch (DOMSyntaxException e) {
			clon = null;
		}
		return clon;
	}

}
