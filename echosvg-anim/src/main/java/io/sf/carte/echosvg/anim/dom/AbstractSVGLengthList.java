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

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGLengthList;

import io.sf.carte.echosvg.dom.svg.AbstractSVGList;
import io.sf.carte.echosvg.dom.svg.ListHandler;
import io.sf.carte.echosvg.dom.svg.SVGItem;
import io.sf.carte.echosvg.parser.LengthListHandler;
import io.sf.carte.echosvg.parser.LengthListParser;
import io.sf.carte.echosvg.parser.ParseException;

/**
 * This class is the implementation of <code>SVGLengthList</code>.
 *
 * @author <a href="mailto:nicolas.socheleau@bitflash.com">Nicolas Socheleau</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractSVGLengthList extends AbstractSVGList implements SVGLengthList {

	/**
	 * This length list's direction.
	 */
	protected short direction;

	/**
	 * Separator for a length list.
	 */
	public static final String SVG_LENGTH_LIST_SEPARATOR = " ";

	/**
	 * Return the separator between values in the list.
	 */
	@Override
	protected String getItemSeparator() {
		return SVG_LENGTH_LIST_SEPARATOR;
	}

	/**
	 * Create an SVGException when the checkItemType fails.
	 * 
	 * @return SVGException
	 */
	protected abstract SVGException createSVGException(short type, String key, Object[] args);

	/**
	 * Returns the element owning this SVGLengthList.
	 */
	protected abstract Element getElement();

	/**
	 * Creates a new SVGLengthList.
	 */
	protected AbstractSVGLengthList(short direction) {
		this.direction = direction;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLengthList#initialize(SVGLength)}.
	 */
	@Override
	public SVGLength initialize(SVGLength newItem) throws DOMException, SVGException {
		return (SVGLength) initializeImpl(newItem);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLengthList#getItem(int)}.
	 */
	@Override
	public SVGLength getItem(int index) throws DOMException {
		return (SVGLength) getItemImpl(index);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLengthList#insertItemBefore(SVGLength,int)}.
	 */
	@Override
	public SVGLength insertItemBefore(SVGLength newItem, int index) throws DOMException, SVGException {
		return (SVGLength) insertItemBeforeImpl(newItem, index);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLengthList#replaceItem(SVGLength,int)}.
	 */
	@Override
	public SVGLength replaceItem(SVGLength newItem, int index) throws DOMException, SVGException {
		return (SVGLength) replaceItemImpl(newItem, index);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLengthList#removeItem(int)}.
	 */
	@Override
	public SVGLength removeItem(int index) throws DOMException {
		return (SVGLength) removeItemImpl(index);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGLengthList#appendItem(SVGLength)}.
	 */
	@Override
	public SVGLength appendItem(SVGLength newItem) throws DOMException, SVGException {
		return (SVGLength) appendItemImpl(newItem);
	}

	/**
	 * Creates a new {@link SVGItem} object from the given {@link SVGLength}.
	 */
	@Override
	protected SVGItem createSVGItem(Object newItem) {
		SVGLength l = (SVGLength) newItem;
		return new SVGLengthItem(l.getUnitType(), l.getValueInSpecifiedUnits(), direction);
	}

	/**
	 * Parses the attribute associated with this SVGLengthList.
	 *
	 * @param value   attribute value
	 * @param handler length list handler
	 */
	@Override
	protected void doParse(String value, ListHandler handler) throws ParseException {

		LengthListParser lengthListParser = new LengthListParser();

		LengthListBuilder builder = new LengthListBuilder(handler);

		lengthListParser.setLengthListHandler(builder);
		lengthListParser.parse(value);
	}

	/**
	 * Asserts that the given item is an {@link SVGLengthList}.
	 */
	@Override
	protected void checkItemType(Object newItem) throws SVGException {
		if (!(newItem instanceof SVGLength)) {
			createSVGException(SVGException.SVG_WRONG_TYPE_ERR, "expected.length", null);
		}
	}

	/**
	 * An {@link SVGLength} in the list.
	 */
	protected class SVGLengthItem extends AbstractSVGLength implements SVGItem {

		/**
		 * Creates a new SVGLengthItem.
		 */
		public SVGLengthItem(short type, float value, short direction) {
			super(direction);
			this.unitType = type;
			this.value = value;
		}

		/**
		 * Returns the element this length is associated with.
		 */
		@Override
		protected SVGOMElement getAssociatedElement() {
			return (SVGOMElement) AbstractSVGLengthList.this.getElement();
		}

		/**
		 * List the item belongs to.
		 */
		protected AbstractSVGList parentList;

		/**
		 * Assigns a parent list to this item.
		 * 
		 * @param list The list the item belongs.
		 */
		@Override
		public void setParent(AbstractSVGList list) {
			parentList = list;
		}

		/**
		 * Returns the parent list of this item.
		 */
		@Override
		public AbstractSVGList getParent() {
			return parentList;
		}

		/**
		 * Notifies the parent list that this item has changed.
		 */
		@Override
		protected void reset() {
			if (parentList != null) {
				parentList.itemChanged();
			}
		}

	}

	/**
	 * Helper class to interface the {@link LengthListParser} and the
	 * {@link ListHandler}.
	 */
	protected class LengthListBuilder implements LengthListHandler {

		/**
		 * The ListHandler to pass newly created {@link SVGLengthItem} objects to.
		 */
		protected ListHandler listHandler;

		/**
		 * The the length value just parsed.
		 */
		protected float currentValue;

		/**
		 * The length unit just parsed.
		 */
		protected short currentType;

		/**
		 * Creates a new LengthListBuilder.
		 */
		public LengthListBuilder(ListHandler listHandler) {
			this.listHandler = listHandler;
		}

		/**
		 * Implements {@link LengthListHandler#startLengthList()}.
		 */
		@Override
		public void startLengthList() throws ParseException {
			listHandler.startList();
		}

		/**
		 * Implements {@link LengthListHandler#startLength()}.
		 */
		@Override
		public void startLength() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_NUMBER;
			currentValue = 0.0f;
		}

		/**
		 * Implements {@link LengthListHandler#lengthValue(float)}.
		 */
		@Override
		public void lengthValue(float v) throws ParseException {
			currentValue = v;
		}

		/**
		 * Implements {@link LengthListHandler#em()}.
		 */
		@Override
		public void em() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_EMS;
		}

		/**
		 * Implements {@link LengthListHandler#ex()}.
		 */
		@Override
		public void ex() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_EXS;
		}

		/**
		 * Implements {@link LengthListHandler#in()}.
		 */
		@Override
		public void in() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_IN;
		}

		/**
		 * Implements {@link LengthListHandler#cm()}.
		 */
		@Override
		public void cm() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_CM;
		}

		/**
		 * Implements {@link LengthListHandler#mm()}.
		 */
		@Override
		public void mm() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_MM;
		}

		/**
		 * Implements {@link LengthListHandler#pc()}.
		 */
		@Override
		public void pc() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_PC;
		}

		/**
		 * Implements {@link LengthListHandler#pt()}.
		 */
		@Override
		public void pt() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_EMS;
		}

		/**
		 * Implements {@link LengthListHandler#px()}.
		 */
		@Override
		public void px() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_PX;
		}

		/**
		 * Implements {@link LengthListHandler#percentage()}.
		 */
		@Override
		public void percentage() throws ParseException {
			currentType = SVGLength.SVG_LENGTHTYPE_PERCENTAGE;
		}

		/**
		 * Implements {@link LengthListHandler#endLength()}.
		 */
		@Override
		public void endLength() throws ParseException {
			listHandler.item(new SVGLengthItem(currentType, currentValue, direction));
		}

		/**
		 * Implements {@link LengthListHandler#endLengthList()}.
		 */
		@Override
		public void endLengthList() throws ParseException {
			listHandler.endList();
		}

	}

}
