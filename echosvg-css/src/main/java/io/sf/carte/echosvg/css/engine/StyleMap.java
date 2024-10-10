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
package io.sf.carte.echosvg.css.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.echosvg.css.engine.value.PendingValue;
import io.sf.carte.echosvg.css.engine.value.Value;

/**
 * This class represents objects which contains property/value mappings.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class StyleMap {

	public static final int IMPORTANT_MASK = 0x0001;
	public static final int COMPUTED_MASK = 0x0002;
	public static final int NULL_CASCADED_MASK = 0x0004;
	public static final int INHERITED_MASK = 0x0008;

	public static final int LINE_HEIGHT_RELATIVE_MASK = 0x0010;
	public static final int FONT_SIZE_RELATIVE_MASK = 0x0020;
	public static final int COLOR_RELATIVE_MASK = 0x0040;
	public static final int PARENT_RELATIVE_MASK = 0x0080;
	public static final int BLOCK_WIDTH_RELATIVE_MASK = 0x0100;
	public static final int BLOCK_HEIGHT_RELATIVE_MASK = 0x0200;
	public static final int BOX_RELATIVE_MASK = 0x0400;
	public static final int ROOT_LINE_HEIGHT_RELATIVE_MASK = 0x0800;
	public static final int ROOT_FONT_SIZE_RELATIVE_MASK = 0x1000;
	public static final int VIEWPORT_RELATIVE_MASK = 0x2000;
	public static final int CUSTOM_PTY_RELATIVE_MASK = 0x4000;
	public static final int ATTR_TAINTED_MASK = 0x8000;

	public static final int ORIGIN_MASK = 0xE0000000; // 3 last bits

	//
	// The origin values.
	//
	public static final int USER_AGENT_ORIGIN = 0;
	public static final int USER_ORIGIN = 0x20000000; // 0010
	public static final int NON_CSS_ORIGIN = 0x40000000; // 0100
	public static final int AUTHOR_ORIGIN = 0x60000000; // 0110
	public static final int INLINE_AUTHOR_ORIGIN = 0x80000000; // 1000
	public static final int OVERRIDE_ORIGIN = 0xA0000000; // 1010

	/**
	 * The values.
	 */
	protected Value[] values;

	/**
	 * To store the value masks.
	 */
	protected int[] masks;

	/**
	 * Custom property map.
	 */
	private Map<String, LexicalUnit> customProperties = null;

	/**
	 * Whether the values of this map cannot be re-cascaded.
	 */
	protected boolean fixedCascadedValues;

	/**
	 * Creates a new StyleMap.
	 */
	public StyleMap(int size) {
		values = new Value[size];
		masks = new int[size];
	}

	/**
	 * Whether this map has fixed cascaded value.
	 */
	public boolean hasFixedCascadedValues() {
		return fixedCascadedValues;
	}

	/**
	 * Sets the fixedCascadedValues property.
	 */
	public void setFixedCascadedStyle(boolean b) {
		fixedCascadedValues = b;
	}

	/**
	 * Returns the value at the given index, null if unspecified.
	 * 
	 * @param i the property index.
	 */
	public Value getValue(int i) {
		return values[i];
	}

	/**
	 * Returns the mask of the given property value.
	 * 
	 * @param i the property index.
	 */
	public int getMask(int i) {
		return masks[i];
	}

	/**
	 * Tells whether the given property value is important.
	 * 
	 * @param i the property index.
	 */
	public boolean isImportant(int i) {
		return (masks[i] & IMPORTANT_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is computed.
	 * 
	 * @param i the property index.
	 */
	public boolean isComputed(int i) {
		return (masks[i] & COMPUTED_MASK) != 0;
	}

	/**
	 * Tells whether the given cascaded property value is null.
	 * 
	 * @param i the property index.
	 */
	public boolean isNullCascaded(int i) {
		return (masks[i] & NULL_CASCADED_MASK) != 0;
	}

	/**
	 * Tells whether the given cascaded property value was inherited from it's
	 * parent or set locally.
	 * 
	 * @param i the property index.
	 */
	public boolean isInherited(int i) {
		return (masks[i] & INHERITED_MASK) != 0;
	}

	/**
	 * Returns the origin value.
	 * 
	 * @param i the property index.
	 */
	public int getOrigin(int i) {
		return (masks[i] & ORIGIN_MASK);
	}

	/**
	 * Tells whether the given property value is relative to 'color'.
	 * 
	 * @param i the property index.
	 */
	public boolean isColorRelative(int i) {
		return (masks[i] & COLOR_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to the parent's property
	 * value.
	 * 
	 * @param i the property index.
	 */
	public boolean isParentRelative(int i) {
		return (masks[i] & PARENT_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to 'line-height'.
	 * 
	 * @param i the property index.
	 */
	public boolean isLineHeightRelative(int i) {
		return (masks[i] & LINE_HEIGHT_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to 'font-size'.
	 * 
	 * @param i the property index.
	 */
	public boolean isFontSizeRelative(int i) {
		return (masks[i] & FONT_SIZE_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to 'line-height' of the
	 * {@code :root} element.
	 * 
	 * @param i the property index.
	 */
	public boolean isRootLineHeightRelative(int i) {
		return (masks[i] & ROOT_LINE_HEIGHT_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to 'font-size' of the
	 * {@code :root} element.
	 * 
	 * @param i the property index.
	 */
	public boolean isRootFontSizeRelative(int i) {
		return (masks[i] & ROOT_FONT_SIZE_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to the width of the
	 * containing block.
	 * 
	 * @param i the property index.
	 */
	public boolean isBlockWidthRelative(int i) {
		return (masks[i] & BLOCK_WIDTH_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to the height of the
	 * containing block.
	 * 
	 * @param i the property index.
	 */
	public boolean isBlockHeightRelative(int i) {
		return (masks[i] & BLOCK_HEIGHT_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to the viewport.
	 * 
	 * @param i the property index.
	 */
	public boolean isViewportRelative(int i) {
		return (masks[i] & VIEWPORT_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is relative to a custom property.
	 * 
	 * @param i the property index.
	 */
	public boolean isCustomPtyRelative(int i) {
		return (masks[i] & CUSTOM_PTY_RELATIVE_MASK) != 0;
	}

	/**
	 * Tells whether the given property value is attr()-tainted.
	 * 
	 * @param i the property index.
	 */
	public boolean isAttrTainted(int i) {
		return (masks[i] & ATTR_TAINTED_MASK) != 0;
	}

	/**
	 * Puts a property value, given the property index.
	 * 
	 * @param i The property index.
	 * @param v The property value.
	 */
	public void putValue(int i, Value v) {
		values[i] = v;
	}

	/**
	 * Puts a property mask, given the property index.
	 * 
	 * @param i The property index.
	 * @param m The property mask.
	 */
	public void putMask(int i, int m) {
		masks[i] = m;
	}

	/**
	 * Sets the priority of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putImportant(int i, boolean b) {
		if (b)
			masks[i] |= IMPORTANT_MASK;
		else
			masks[i] &= ~IMPORTANT_MASK;
	}

	/**
	 * Sets the origin of the given value.
	 * 
	 * @param i   the property index.
	 * @param val the origin.
	 */
	public void putOrigin(int i, int val) {
		masks[i] &= ~ORIGIN_MASK;
		masks[i] |= (val & ORIGIN_MASK);
	}

	/**
	 * Sets the computed flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putComputed(int i, boolean b) {
		if (b)
			masks[i] |= COMPUTED_MASK;
		else
			masks[i] &= ~COMPUTED_MASK;
	}

	/**
	 * Sets the null-cascaded flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putNullCascaded(int i, boolean b) {
		if (b)
			masks[i] |= NULL_CASCADED_MASK;
		else
			masks[i] &= ~NULL_CASCADED_MASK;
	}

	/**
	 * Sets the inherited flag of a property value. If true this computed value was
	 * inherited from it's parent.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putInherited(int i, boolean b) {
		if (b)
			masks[i] |= INHERITED_MASK;
		else
			masks[i] &= ~INHERITED_MASK;
	}

	/**
	 * Sets the color-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putColorRelative(int i, boolean b) {
		if (b)
			masks[i] |= COLOR_RELATIVE_MASK;
		else
			masks[i] &= ~COLOR_RELATIVE_MASK;
	}

	/**
	 * Sets the parent-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putParentRelative(int i, boolean b) {
		if (b)
			masks[i] |= PARENT_RELATIVE_MASK;
		else
			masks[i] &= ~PARENT_RELATIVE_MASK;
	}

	/**
	 * Sets the line-height-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putLineHeightRelative(int i, boolean b) {
		if (b)
			masks[i] |= LINE_HEIGHT_RELATIVE_MASK;
		else
			masks[i] &= ~LINE_HEIGHT_RELATIVE_MASK;
	}

	/**
	 * Sets the font-size-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putFontSizeRelative(int i, boolean b) {
		if (b)
			masks[i] |= FONT_SIZE_RELATIVE_MASK;
		else
			masks[i] &= ~FONT_SIZE_RELATIVE_MASK;
	}

	/**
	 * Sets the root-line-height-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putRootLineHeightRelative(int i, boolean b) {
		if (b)
			masks[i] |= ROOT_LINE_HEIGHT_RELATIVE_MASK;
		else
			masks[i] &= ~ROOT_LINE_HEIGHT_RELATIVE_MASK;
	}

	/**
	 * Sets the root-font-size-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putRootFontSizeRelative(int i, boolean b) {
		if (b)
			masks[i] |= ROOT_FONT_SIZE_RELATIVE_MASK;
		else
			masks[i] &= ~ROOT_FONT_SIZE_RELATIVE_MASK;
	}

	/**
	 * Sets the block-width-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putBlockWidthRelative(int i, boolean b) {
		if (b)
			masks[i] |= BLOCK_WIDTH_RELATIVE_MASK;
		else
			masks[i] &= ~BLOCK_WIDTH_RELATIVE_MASK;
	}

	/**
	 * Sets the block-height-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putBlockHeightRelative(int i, boolean b) {
		if (b)
			masks[i] |= BLOCK_HEIGHT_RELATIVE_MASK;
		else
			masks[i] &= ~BLOCK_HEIGHT_RELATIVE_MASK;
	}

	/**
	 * Sets the viewport-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putViewportRelative(int i, boolean b) {
		if (b)
			masks[i] |= VIEWPORT_RELATIVE_MASK;
		else
			masks[i] &= ~VIEWPORT_RELATIVE_MASK;
	}

	/**
	 * Sets the custom-property-relative flag of a property value.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} sets the flag, unsets otherwise.
	 */
	public void putCustomPtyRelative(int i, boolean b) {
		if (b)
			masks[i] |= CUSTOM_PTY_RELATIVE_MASK;
		else
			masks[i] &= ~CUSTOM_PTY_RELATIVE_MASK;
	}

	/**
	 * Marks or clears a property as attr()-tainted.
	 * 
	 * @param i the property index.
	 * @param b if {@code true} marks the property as attr()-tainted.
	 */
	public void putAttrTainted(int i, boolean b) {
		if (b)
			masks[i] |= ATTR_TAINTED_MASK;
		else
			masks[i] &= ~ATTR_TAINTED_MASK;
	}

	/**
	 * Set a custom property value.
	 * 
	 * @param name  the custom property name.
	 * @param value the custom property value.
	 */
	public void putCustomProperty(String name, LexicalUnit value) {
		if (customProperties == null) {
			customProperties = new HashMap<>();
		}

		customProperties.put(name, value);
	}

	/**
	 * Get the value of a custom property.
	 * 
	 * @param name the custom property name.
	 * @return the custom property value, or {@code null} if there is no value
	 *         defined for that custom property.
	 */
	public LexicalUnit getCustomProperty(String name) {
		return customProperties != null ? customProperties.get(name) : null;
	}

	/**
	 * Returns a printable representation of this style map.
	 */
	public String toString(CSSEngine eng) {
		Set<String> pendingShorthands = null;
		// Note that values.length should always be equal to
		// eng.getNumberOfProperties() for StyleMaps that were created
		// by that CSSEngine.
		int nSlots = values.length;
		StringBuilder sb = new StringBuilder(nSlots * 8 + 32);
		for (int i = 0; i < nSlots; i++) {
			Value v = values[i];
			if (v == null)
				continue;

			if (v.getPrimitiveType() != Type.INTERNAL) {
				sb.append(eng.getPropertyName(i));
				sb.append(": ");
				sb.append(v);
				if (isImportant(i))
					sb.append(" !important");
				sb.append(";\n");
			} else {
				if (pendingShorthands == null) {
					pendingShorthands = new HashSet<>();
				}
				PendingValue pending = (PendingValue) v;
				String name = pending.getShorthandName();
				if (pendingShorthands.add(name)) {
					sb.append(name);
					sb.append(": ");
					sb.append(pending.getLexicalUnit().toString());
					if (isImportant(i))
						sb.append(" !important");
					sb.append(";\n");
				}
			}
		}

		if (customProperties != null) {
			for (Map.Entry<String, LexicalUnit> entry : customProperties.entrySet()) {
				sb.append(entry.getKey());
				sb.append(": ");
				sb.append(entry.getValue().toString());
				sb.append(";\n");
			}
		}

		return sb.toString();
	}

}
