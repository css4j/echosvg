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
import org.w3c.dom.svg.SVGAnimatedPoints;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;

import io.sf.carte.echosvg.anim.values.AnimatablePointListValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.svg.AbstractSVGList;
import io.sf.carte.echosvg.dom.svg.AbstractSVGPointList;
import io.sf.carte.echosvg.dom.svg.ListBuilder;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.SVGItem;
import io.sf.carte.echosvg.dom.svg.SVGPointItem;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * This class is the implementation of the SVGAnimatedPoints interface.
 *
 * <p>
 * Original author: <a href="mailto:nicolas.socheleau@bitflash.com">Nicolas Socheleau</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class SVGOMAnimatedPoints extends AbstractSVGAnimatedValue implements SVGAnimatedPoints {

	/**
	 * The base value.
	 */
	protected BaseSVGPointList baseVal;

	/**
	 * The animated value.
	 */
	protected AnimSVGPointList animVal;

	/**
	 * Whether the list is changing.
	 */
	protected boolean changing;

	/**
	 * Default value for the point list.
	 */
	protected String defaultValue;

	/**
	 * Creates a new SVGOMAnimatedPoints.
	 * 
	 * @param elt          The associated element.
	 * @param ns           The attribute's namespace URI.
	 * @param ln           The attribute's local name.
	 * @param defaultValue The default value if the attribute is not specified.
	 */
	public SVGOMAnimatedPoints(AbstractElement elt, String ns, String ln, String defaultValue) {
		super(elt, ns, ln);
		this.defaultValue = defaultValue;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedPoints#getPoints()}.
	 */
	@Override
	public SVGPointList getPoints() {
		if (baseVal == null) {
			baseVal = new BaseSVGPointList();
		}
		return baseVal;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedPoints#getAnimatedPoints()}.
	 */
	@Override
	public SVGPointList getAnimatedPoints() {
		if (animVal == null) {
			animVal = new AnimSVGPointList();
		}
		return animVal;
	}

	/**
	 * Checks whether the points list value is malformed.
	 * 
	 * @return LiveAttributeException.ERR_ATTRIBUTE_MISSING if the points list value is missing,
	 * ERR_ATTRIBUTE_MALFORMED if is invalid, or {@code -1}
	 *         otherwise.
	 */
	public short check() {
		if (!hasAnimVal) {
			if (baseVal == null) {
				baseVal = new BaseSVGPointList();
			}

			baseVal.revalidate();

			if (baseVal.none) {
				return LiveAttributeException.ERR_ATTRIBUTE_MISSING;
			}
			if (baseVal.malformed) {
				return LiveAttributeException.ERR_ATTRIBUTE_MALFORMED;
			}
		}
		return -1;
	}

	/**
	 * Returns the base value of the attribute as an {@link AnimatableValue}.
	 */
	@Override
	public AnimatableValue getUnderlyingValue(AnimationTarget target) {
		SVGPointList pl = getPoints();
		int n = pl.getNumberOfItems();
		float[] points = new float[n * 2];
		for (int i = 0; i < n; i++) {
			SVGPoint p = pl.getItem(i);
			points[i * 2] = p.getX();
			points[i * 2 + 1] = p.getY();
		}
		return new AnimatablePointListValue(target, points);
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
			AnimatablePointListValue animPointList = (AnimatablePointListValue) val;
			if (animVal == null) {
				animVal = new AnimSVGPointList();
			}
			animVal.setAnimatedValue(animPointList.getNumbers());
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
	 * {@link SVGPointList} implementation for the base point list value.
	 */
	protected class BaseSVGPointList extends AbstractSVGPointList {

		/**
		 * Whether the attribute is missing or no value.
		 */
		protected boolean none;

		/**
		 * Whether the attribute is malformed.
		 */
		protected boolean malformed;

		boolean isValid() {
			return valid;
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
		 * Returns the value of the DOM attribute containing the point list.
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
		 * Sets the DOM attribute value containing the point list.
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
			none = false;
			malformed = false;
		}

		/**
		 * Appends the string representation of the given {@link SVGItem} to the DOM
		 * attribute. This is called in response to an append to the list.
		 */
		@Override
		protected void resetAttribute(SVGItem item) {
			super.resetAttribute(item);
			none = false;
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
			none = false;
			malformed = false;

			String s = getValueAsString().trim(); // Default is ''
			if (s.isEmpty()) {
				none = true;
				clear(itemList);
				itemList = new ArrayList<>(1);
				return;
			}

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

		@Override
		public void copyTo(AbstractSVGList list) {
			super.copyTo(list);
			BaseSVGPointList other = (BaseSVGPointList) list;
			other.malformed = malformed;
			other.none = none;
		}

	}

	/**
	 * {@link SVGPointList} implementation for the animated point list value.
	 */
	protected class AnimSVGPointList extends AbstractSVGPointList {

		/**
		 * Creates a new AnimSVGPointList.
		 */
		public AnimSVGPointList() {
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
		 * <b>DOM</b>: Implements {@link SVGPointList#getNumberOfItems()}.
		 */
		@Override
		public int getNumberOfItems() {
			if (hasAnimVal) {
				return super.getNumberOfItems();
			}
			return getPoints().getNumberOfItems();
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPointList#getItem(int)}.
		 */
		@Override
		public SVGPoint getItem(int index) throws DOMException {
			if (hasAnimVal) {
				return super.getItem(index);
			}
			return getPoints().getItem(index);
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
		 * <b>DOM</b>: Implements {@link SVGPointList#clear()}.
		 */
		@Override
		public void clear() throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPointList#initialize(SVGPoint)}.
		 */
		@Override
		public SVGPoint initialize(SVGPoint newItem) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPointList#insertItemBefore(SVGPoint, int)}.
		 */
		@Override
		public SVGPoint insertItemBefore(SVGPoint newItem, int index) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPointList#replaceItem(SVGPoint, int)}.
		 */
		@Override
		public SVGPoint replaceItem(SVGPoint newItem, int index) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPointList#removeItem(int)}.
		 */
		@Override
		public SVGPoint removeItem(int index) throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPointList#appendItem(SVGPoint)}.
		 */
		@Override
		public SVGPoint appendItem(SVGPoint newItem) throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point.list", null);
		}

		/**
		 * Sets the animated value.
		 */
		protected void setAnimatedValue(float[] pts) {
			int size = itemList.size();
			int i = 0;
			while (i < size && i < pts.length / 2) {
				SVGPointItem p = (SVGPointItem) itemList.get(i);
				p.setX(pts[i * 2]);
				p.setY(pts[i * 2 + 1]);
				i++;
			}
			while (i < pts.length / 2) {
				appendItemImpl(new SVGPointItem(pts[i * 2], pts[i * 2 + 1]));
				i++;
			}
			while (size > pts.length / 2) {
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
