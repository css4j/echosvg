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
package io.sf.carte.echosvg.css.engine.value.svg12;

import org.w3c.dom.DOMException;

import io.sf.carte.echosvg.css.engine.value.ColorValue;

/**
 * This class represents an device-specific color value.
 *
 * @version $Id$
 */
@Deprecated
public class DeviceColor extends ColorValue {

	public static final String DEVICE_GRAY_COLOR_FUNCTION = "device-gray";
	public static final String DEVICE_RGB_COLOR_FUNCTION = "device-rgb";
	public static final String DEVICE_CMYK_COLOR_FUNCTION = "device-cmyk";
	public static final String DEVICE_NCHANNEL_COLOR_FUNCTION = "device-nchannel";

	protected boolean nChannel;

	/**
	 * The color count.
	 */
	protected int count;

	/**
	 * The colors.
	 */
	protected float[] colors = new float[5];

	/**
	 * Creates a new DeviceColor.
	 * 
	 * @param nChannel true for a device-nchannel() color, false for Gray, RGB and
	 *                 CMYK
	 */
	public DeviceColor(boolean nChannel) {
		this.nChannel = nChannel;
	}

	/**
	 * Indicates whether this color uses an N-Channel color space.
	 * 
	 * @return true if N-Channel is used
	 */
	public boolean isNChannel() {
		return this.nChannel;
	}

	/**
	 * Returns the number of colors.
	 */
	public int getNumberOfColors() throws DOMException {
		return count;
	}

	/**
	 * Returns the color at the given index.
	 */
	public float getColor(int i) throws DOMException {
		return colors[i];
	}

	@Override
	public String getCSSColorSpace() {
		String cs;
		if (nChannel) {
			cs = DEVICE_NCHANNEL_COLOR_FUNCTION;
		} else {
			switch (count) {
			case 1:
				cs = DEVICE_GRAY_COLOR_FUNCTION;
				break;
			case 3:
				cs = DEVICE_RGB_COLOR_FUNCTION;
				break;
			case 4:
				cs = DEVICE_CMYK_COLOR_FUNCTION;
				break;
			default:
				throw new IllegalStateException("Invalid number of components encountered");
			}
		}
		return cs;
	}

	@Override
	public DeviceColor clone() {
		DeviceColor c = (DeviceColor) super.clone();
		c.nChannel = nChannel;
		c.count = count;
		c.colors = colors.clone();
		return c;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder(count * 8);
		if (nChannel) {
			sb.append(DEVICE_NCHANNEL_COLOR_FUNCTION);
		} else {
			switch (count) {
			case 1:
				sb.append(DEVICE_GRAY_COLOR_FUNCTION);
				break;
			case 3:
				sb.append(DEVICE_RGB_COLOR_FUNCTION);
				break;
			case 4:
				sb.append(DEVICE_CMYK_COLOR_FUNCTION);
				break;
			default:
				throw new IllegalStateException("Invalid number of components encountered");
			}
		}
		sb.append('(');
		for (int i = 0; i < count; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(colors[i]);
		}
		sb.append(')');
		return sb.toString();
	}

	/**
	 * Appends a color to the list.
	 */
	public void append(float c) {
		if (count == colors.length) {
			float[] t = new float[count * 2];
			System.arraycopy(colors, 0, t, 0, count);
			colors = t;
		}
		colors[count++] = c;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getCssText();
	}

}
