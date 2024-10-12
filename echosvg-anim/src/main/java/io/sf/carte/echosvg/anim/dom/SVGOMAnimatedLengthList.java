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
package io.sf.carte.echosvg.anim.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGLengthList;

import io.sf.carte.echosvg.anim.values.AnimatableLengthListValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.svg.AbstractSVGList;
import io.sf.carte.echosvg.dom.svg.ListBuilder;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.SVGItem;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * This class is the implementation of the {@link SVGAnimatedLengthList}
 * interface.
 *
 * @author <a href="mailto:nicolas.socheleau@bitflash.com">Nicolas Socheleau</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMAnimatedLengthList extends AbstractSVGAnimatedValue implements SVGAnimatedLengthList {

	/**
	 * The base value.
	 */
	protected BaseSVGLengthList baseVal;

	/**
	 * The animated value.
	 */
	protected AnimSVGLengthList animVal;

	/**
	 * Whether the list is changing.
	 */
	protected boolean changing;

	/**
	 * Default value for the length list.
	 */
	protected String defaultValue;

	/**
	 * Whether empty length lists are allowed.
	 */
	protected boolean emptyAllowed;

	/**
	 * The direction of the lengths in this list.
	 */
	protected short direction;

	/**
	 * Creates a new SVGOMAnimatedLengthList.
	 * 
	 * @param elt          The associated element.
	 * @param ns           The attribute's namespace URI.
	 * @param ln           The attribute's local name.
	 * @param defaultValue The default value if the attribute is not specified.
	 * @param emptyAllowed Whether a list with no items is allowed.
	 * @param direction    The direction of the lengths in the list.
	 */
	public SVGOMAnimatedLengthList(AbstractElement elt, String ns, String ln, String defaultValue, boolean emptyAllowed,
			short direction) {
		super(elt, ns, ln);
		this.defaultValue = defaultValue;
		this.emptyAllowed = emptyAllowed;
		this.direction = direction;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedLengthList#getBaseVal()}.
	 */
	@Override
	public SVGLengthList getBaseVal() {
		if (baseVal == null) {
			baseVal = new BaseSVGLengthList();
		}
		return baseVal;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedLengthList#getAnimVal()}.
	 */
	@Override
	public SVGLengthList getAnimVal() {
		if (animVal == null) {
			animVal = new AnimSVGLengthList();
		}
		return animVal;
	}

	/**
	 * Throws an exception if the length list value is malformed.
	 */
	public void check() {
		if (!hasAnimVal) {
			if (baseVal == null) {
				baseVal = new BaseSVGLengthList();
			}
			baseVal.revalidate();
			if (baseVal.missing) {
				throw new LiveAttributeException(element, localName, LiveAttributeException.ERR_ATTRIBUTE_MISSING,
						null);
			}
			if (baseVal.malformed) {
				throw new LiveAttributeException(element, localName, LiveAttributeException.ERR_ATTRIBUTE_MALFORMED,
						baseVal.getValueAsString());
			}
		}
	}

	/**
	 * Returns the base value of the attribute as an {@link AnimatableValue}.
	 */
	@Override
	public AnimatableValue getUnderlyingValue(AnimationTarget target) {
		SVGLengthList ll = getBaseVal();
		int n = ll.getNumberOfItems();
		short[] types = new short[n];
		float[] values = new float[n];
		for (int i = 0; i < n; i++) {
			SVGLength l = ll.getItem(i);
			types[i] = l.getCSSUnitType();
			values[i] = l.getValueInSpecifiedUnits();
		}
		return new AnimatableLengthListValue(target, types, values,
				target.getPercentageInterpretation(getNamespaceURI(), getLocalName(), false));
	}

	/**
	 * Updates the animated value with the given {@link AnimatableValue}.
	 */
	@Override
	protected void updateAnimatedValue(AnimatableValue val) {
		if (val == null) {
			hasAnimVal = false;
		} else {
			hasAnimVal = true;
			AnimatableLengthListValue animLengths = (AnimatableLengthListValue) val;
			if (animVal == null) {
				animVal = new AnimSVGLengthList();
			}
			animVal.setAnimatedValue(animLengths.getLengthTypes(), animLengths.getLengthValues());
		}
		fireAnimatedAttributeListeners();
	}

	/**
	 * Called when an Attr node has been added.
	 */
	@Override
	public void attrAdded(Attr node, String newv) {
		if (!changing && baseVal != null) {
			baseVal.invalidate();
		}
		fireBaseAttributeListeners();
		if (!hasAnimVal) {
			fireAnimatedAttributeListeners();
		}
	}

	/**
	 * Called when an Attr node has been modified.
	 */
	@Override
	public void attrModified(Attr node, String oldv, String newv) {
		if (!changing && baseVal != null) {
			baseVal.invalidate();
		}
		fireBaseAttributeListeners();
		if (!hasAnimVal) {
			fireAnimatedAttributeListeners();
		}
	}

	/**
	 * Called when an Attr node has been removed.
	 */
	@Override
	public void attrRemoved(Attr node, String oldv) {
		if (!changing && baseVal != null) {
			baseVal.invalidate();
		}
		fireBaseAttributeListeners();
		if (!hasAnimVal) {
			fireAnimatedAttributeListeners();
		}
	}

	/**
	 * {@link SVGLengthList} implementation for the base length list value.
	 */
	public class BaseSVGLengthList extends AbstractSVGLengthList {

		/**
		 * Whether the value is missing.
		 */
		protected boolean missing;

		/**
		 * Whether the value is malformed.
		 */
		protected boolean malformed;

		/**
		 * Creates a new BaseSVGLengthList.
		 */
		public BaseSVGLengthList() {
			super(SVGOMAnimatedLengthList.this.direction);
		}

		/**
		 * Create a DOMException.
		 */
		@Override
		protected DOMException createDOMException(short type, String key, Object[] args) {
			return element.createDOMException(type, key, args);
		}

		/**
		 * Create a SVGException.
		 */
		@Override
		protected SVGException createSVGException(short type, String key, Object[] args) {

			return ((SVGOMElement) element).createSVGException(type, key, args);
		}

		/**
		 * Returns the element owning the attribute with which this length list is
		 * associated.
		 */
		@Override
		protected Element getElement() {
			return element;
		}

		/**
		 * Returns the value of the DOM attribute containing the length list.
		 */
		@Override
		protected String getValueAsString() {
			Attr attr = element.getAttributeNodeNS(namespaceURI, localName);
			if (attr == null) {
				return defaultValue;
			}
			return attr.getValue();
		}

		/**
		 * Sets the DOM attribute value containing the length list.
		 */
		@Override
		protected void setAttributeValue(String value) {
			try {
				changing = true;
				element.setAttributeNS(namespaceURI, localName, value);
			} finally {
				changing = false;
			}
		}

		/**
		 * Resets the value of the associated attribute.
		 */
		@Override
		protected void resetAttribute() {
			super.resetAttribute();
			missing = false;
			malformed = false;
		}

		/**
		 * Appends the string representation of the given {@link SVGItem} to the DOM
		 * attribute. This is called in response to an append to the list.
		 */
		@Override
		protected void resetAttribute(SVGItem item) {
			super.resetAttribute(item);
			missing = false;
			malformed = false;
		}

		/**
		 * Initializes the list, if needed.
		 */
		@Override
		protected void revalidate() {
			if (valid) {
				return;
			}

			valid = true;
			missing = false;
			malformed = false;

			String s = getValueAsString();
			boolean isEmpty = s != null && (s = s.trim()).isEmpty();
			if (s == null || isEmpty && !emptyAllowed) {
				missing = true;
				clear(itemList);
				itemList = new ArrayList<>(1);
				return;
			}
			if (isEmpty) {
				itemList = new ArrayList<>(1);
			} else {
				ListBuilder builder = new ListBuilder(this);
				try {
					doParse(s, builder);
				} catch (ParseException e) {
					malformed = true;
				} finally {
					clear(itemList);
					List<SVGItem> parsedList = builder.getList();
					if (parsedList != null) {
						itemList = parsedList;
					} else {
						itemList = new ArrayList<>(1);
					}
				}
			}
		}

		@Override
		public void copyTo(AbstractSVGList list) {
			super.copyTo(list);
			BaseSVGLengthList other = (BaseSVGLengthList) list;
			other.malformed = malformed;
			other.missing = missing;
		}

	}

	/**
	 * {@link SVGLengthList} implementation for the animated length list value.
	 */
	protected class AnimSVGLengthList extends AbstractSVGLengthList {

		/**
		 * Creates a new AnimSVGLengthList.
		 */
		public AnimSVGLengthList() {
			super(SVGOMAnimatedLengthList.this.direction);
			itemList = new ArrayList<>(1);
		}

		/**
		 * Create a DOMException.
		 */
		@Override
		protected DOMException createDOMException(short type, String key, Object[] args) {
			return element.createDOMException(type, key, args);
		}

		/**
		 * Create a SVGException.
		 */
		@Override
		protected SVGException createSVGException(short type, String key, Object[] args) {

			return ((SVGOMElement) element).createSVGException(type, key, args);
		}

		/**
		 * Returns the element owning this SVGLengthList.
		 */
		@Override
		protected Element getElement() {
			return element;
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#getNumberOfItems()}.
		 */
		@Override
		public int getNumberOfItems() {
			if (hasAnimVal) {
				return super.getNumberOfItems();
			}
			return getBaseVal().getNumberOfItems();
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#getItem(int)}.
		 */
		@Override
		public SVGLength getItem(int index) throws DOMException {
			if (hasAnimVal) {
				return super.getItem(index);
			}
			return getBaseVal().getItem(index);
		}

		/**
		 * Returns the value of the DOM attribute containing the point list.
		 */
		@Override
		protected String getValueAsString() {
			if (itemList.isEmpty()) {
				return "";
			}
			StringBuilder sb = new StringBuilder(itemList.size() * 8);
			Iterator<SVGItem> i = itemList.iterator();
			if (i.hasNext()) {
				sb.append(i.next().getValueAsString());
			}
			while (i.hasNext()) {
				sb.append(getItemSeparator());
				sb.append(i.next().getValueAsString());
			}
			return sb.toString();
		}

		/**
		 * Sets the DOM attribute value containing the point list.
		 */
		@Override
		protected void setAttributeValue(String value) {
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#clear()}.
		 */
		@Override
		public void clear() throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#initialize(SVGLength)}.
		 */
		@Override
		public SVGLength initialize(SVGLength newItem) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length.list", null);
		}

		/**
		 * <b>DOM</b>: Implements
		 * {@link SVGLengthList#insertItemBefore(SVGLength, int)}.
		 */
		@Override
		public SVGLength insertItemBefore(SVGLength newItem, int index) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#replaceItem(SVGLength, int)}.
		 */
		@Override
		public SVGLength replaceItem(SVGLength newItem, int index) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#removeItem(int)}.
		 */
		@Override
		public SVGLength removeItem(int index) throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGLengthList#appendItem(SVGLength)}.
		 */
		@Override
		public SVGLength appendItem(SVGLength newItem) throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length.list", null);
		}

		/**
		 * Sets the animated value.
		 */
		protected void setAnimatedValue(short[] types, float[] values) {
			int size = itemList.size();
			int i = 0;
			while (i < size && i < types.length) {
				SVGLengthItem l = (SVGLengthItem) itemList.get(i);
				l.unitType = types[i];
				l.value = values[i];
				l.direction = this.direction;
				i++;
			}
			while (i < types.length) {
				appendItemImpl(new SVGLengthItem(types[i], values[i], this.direction));
				i++;
			}
			while (size > types.length) {
				removeItemImpl(--size);
			}
		}

		/**
		 * Resets the value of the associated attribute. Does nothing, since there is no
		 * attribute for an animated value.
		 */
		@Override
		protected void resetAttribute() {
		}

		/**
		 * Resets the value of the associated attribute. Does nothing, since there is no
		 * attribute for an animated value.
		 */
		@Override
		protected void resetAttribute(SVGItem item) {
		}

		/**
		 * Initializes the list, if needed. Does nothing, since there is no attribute to
		 * read the list from.
		 */
		@Override
		protected void revalidate() {
			valid = true;
		}

	}

}
