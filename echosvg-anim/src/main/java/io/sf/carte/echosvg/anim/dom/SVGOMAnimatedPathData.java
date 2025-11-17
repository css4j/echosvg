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
import org.w3c.dom.svg.SVGAnimatedPathData;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGPathSeg;
import org.w3c.dom.svg.SVGPathSegList;

import io.sf.carte.echosvg.anim.values.AnimatablePathDataValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.svg.AbstractSVGList;
import io.sf.carte.echosvg.dom.svg.AbstractSVGNormPathSegList;
import io.sf.carte.echosvg.dom.svg.AbstractSVGPathSegList;
import io.sf.carte.echosvg.dom.svg.ListBuilder;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.SVGAnimatedPathDataSupport;
import io.sf.carte.echosvg.dom.svg.SVGItem;
import io.sf.carte.echosvg.dom.svg.SVGPathSegItem;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.PathArrayProducer;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class is the implementation of the {@link SVGAnimatedPathData}
 * interface.
 *
 * @author <a href="mailto:nicolas.socheleau@bitflash.com">Nicolas Socheleau</a>
 * @author <a href="mailto:andrest@world-affair.com">Andres Toussaint</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMAnimatedPathData extends AbstractSVGAnimatedValue implements SVGAnimatedPathData {

	/**
	 * Whether the list is changing.
	 */
	protected boolean changing;

	/**
	 * The base path data value.
	 */
	protected BaseSVGPathSegList pathSegs;

	/**
	 * The normalized base path data value.
	 */
	protected NormalizedBaseSVGPathSegList normalizedPathSegs;

	/**
	 * The animated path data value.
	 */
	protected AnimSVGPathSegList animPathSegs;

//     /**
//      * The normalized animated base path data value.
//      */
//     protected NormalizedAnimSVGPathSegList normalizedPathSegs;

	/**
	 * Default value for the 'd' attribute.
	 */
	protected String defaultValue;

	/**
	 * Creates a new SVGOMAnimatedPathData.
	 * 
	 * @param elt          The associated element.
	 * @param ns           The attribute's namespace URI.
	 * @param ln           The attribute's local name.
	 * @param defaultValue The default value if the attribute is not specified.
	 */
	public SVGOMAnimatedPathData(AbstractElement elt, String ns, String ln, String defaultValue) {
		super(elt, ns, ln);
		this.defaultValue = defaultValue;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGAnimatedPathData#getAnimatedNormalizedPathSegList()}.
	 */
	@Override
	public SVGPathSegList getAnimatedNormalizedPathSegList() {
		throw new UnsupportedOperationException(
				"SVGAnimatedPathData.getAnimatedNormalizedPathSegList is not implemented"); // XXX
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedPathData#getAnimatedPathSegList()}.
	 */
	@Override
	public SVGPathSegList getAnimatedPathSegList() {
		if (animPathSegs == null) {
			animPathSegs = new AnimSVGPathSegList();
		}
		return animPathSegs;
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link SVGAnimatedPathData#getNormalizedPathSegList()}.
	 * <p>
	 * Returns the SVGPathSegList mapping the normalized static 'd' attribute of the
	 * element.
	 * </p>
	 * <p>
	 * A normalized path is composed only of absolute moveto, lineto and cubicto
	 * path segments (M, L and C). Using this subset, the path description can be
	 * represented with fewer segment types. Be aware that the normalized 'd'
	 * attribute will be a larger String that the original.
	 * </p>
	 * <p>
	 * Relative values are transformed into absolute, quadratic curves are promoted
	 * to cubic curves, and arcs are converted into one or more cubic curves (one
	 * per quadrant).
	 * </p>
	 * <p>
	 * Modifications to the normalized SVGPathSegList will result in substituting
	 * the original path with a set of normalized path segments.
	 * </p>
	 * 
	 * @return a path segment list containing the normalized version of the path.
	 */
	@Override
	public SVGPathSegList getNormalizedPathSegList() {
		if (normalizedPathSegs == null) {
			normalizedPathSegs = new NormalizedBaseSVGPathSegList();
		}
		return normalizedPathSegs;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedPathData#getPathSegList()}.
	 */
	@Override
	public SVGPathSegList getPathSegList() {
		if (pathSegs == null) {
			pathSegs = new BaseSVGPathSegList();
		}
		return pathSegs;
	}

	/**
	 * Check whether the attribute is missing, empty or malformed.
	 * 
	 * @return -1 if the value is present and is valid,
	 *         LiveAttributeException.ERR_ATTRIBUTE_MISSING if the attribute is
	 *         missing or {@code none}, ERR_ATTRIBUTE_MALFORMED if it is invalid,
	 *         -2 if it is invalid but that was already returned.
	 */
	public short check() {
		if (!hasAnimVal) {
			if (pathSegs == null) {
				pathSegs = new BaseSVGPathSegList();
			}

			boolean wasValid = pathSegs.isValid();

			pathSegs.revalidate();

			if (pathSegs.none) {
				return LiveAttributeException.ERR_ATTRIBUTE_MISSING;
			}
			if (pathSegs.malformed) {
				return wasValid ? -2 : LiveAttributeException.ERR_ATTRIBUTE_MALFORMED;
			}
		}
		return -1;
	}

	/**
	 * Returns the base value of the attribute as an {@link AnimatableValue}.
	 */
	@Override
	public AnimatableValue getUnderlyingValue(AnimationTarget target) {
		SVGPathSegList psl = getPathSegList();
		PathArrayProducer pp = new PathArrayProducer();
		SVGAnimatedPathDataSupport.handlePathSegList(psl, pp);
		return new AnimatablePathDataValue(target, pp.getPathCommands(), pp.getPathParameters());
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
			AnimatablePathDataValue animPath = (AnimatablePathDataValue) val;
			if (animPathSegs == null) {
				animPathSegs = new AnimSVGPathSegList();
			}
			animPathSegs.setAnimatedValue(animPath.getCommands(), animPath.getParameters());
		}
		fireAnimatedAttributeListeners();
	}

	/**
	 * Called when an Attr node has been added.
	 */
	@Override
	public void attrAdded(Attr node, String newv) {
		if (!changing) {
			if (pathSegs != null) {
				pathSegs.invalidate();
			}
			if (normalizedPathSegs != null) {
				normalizedPathSegs.invalidate();
			}
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
		if (!changing) {
			if (pathSegs != null) {
				pathSegs.invalidate();
			}
			if (normalizedPathSegs != null) {
				normalizedPathSegs.invalidate();
			}
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
		if (!changing) {
			if (pathSegs != null) {
				pathSegs.invalidate();
			}
			if (normalizedPathSegs != null) {
				normalizedPathSegs.invalidate();
			}
		}
		fireBaseAttributeListeners();
		if (!hasAnimVal) {
			fireAnimatedAttributeListeners();
		}
	}

	/**
	 * {@link SVGPathSegList} implementation for the base path data value.
	 */
	public class BaseSVGPathSegList extends AbstractSVGPathSegList {

		/**
		 * Whether the attribute is missing or 'none'.
		 */
		protected boolean none;

		/**
		 * Whether the attribute is malformed.
		 */
		protected boolean malformed;

		boolean isValid() {
			return valid;
		}

		@Override
		protected AbstractElement getElement() {
			return element;
		}

		/**
		 * Returns the value of the DOM attribute containing the path data.
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
		 * Sets the DOM attribute value containing the path data.
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

			String s = getValueAsString().trim(); // Default is 'none', not null
			if (CSSConstants.CSS_NONE_VALUE.equalsIgnoreCase(s)) {
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
			BaseSVGPathSegList b = (BaseSVGPathSegList) list;
			b.malformed = malformed;
			b.none = none;
		}

	}

	/**
	 * {@link SVGPathSegList} implementation for the normalized version of the base
	 * path data value.
	 */
	public class NormalizedBaseSVGPathSegList extends AbstractSVGNormPathSegList {

		/**
		 * Whether the attribute is missing or 'none'.
		 */
		protected boolean none;

		/**
		 * Whether the attribute is malformed.
		 */
		protected boolean malformed;

		@Override
		protected AbstractElement getElement() {
			return element;
		}

		/**
		 * Returns the value of the DOM attribute containing the path data.
		 */
		@Override
		protected String getValueAsString() throws SVGException {
			Attr attr = element.getAttributeNodeNS(namespaceURI, localName);
			if (attr == null) {
				return defaultValue;
			}
			return attr.getValue();
		}

		/**
		 * Sets the DOM attribute value containing the path data.
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

			String s = getValueAsString().trim(); // Default is 'none', not null
			if (CSSConstants.CSS_NONE_VALUE.equalsIgnoreCase(s)) {
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
			NormalizedBaseSVGPathSegList b = (NormalizedBaseSVGPathSegList) list;
			b.malformed = malformed;
			b.none = none;
		}

	}

	/**
	 * {@link SVGPathSegList} implementation for the animated path data value.
	 */
	public class AnimSVGPathSegList extends AbstractSVGPathSegList {

		/**
		 * Creates a new AnimSVGPathSegList.
		 */
		public AnimSVGPathSegList() {
			itemList = new ArrayList<>(1);
		}

		@Override
		protected AbstractElement getElement() {
			return element;
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPathSegList#getNumberOfItems()}.
		 */
		@Override
		public int getNumberOfItems() {
			if (hasAnimVal) {
				return super.getNumberOfItems();
			}
			return getPathSegList().getNumberOfItems();
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPathSegList#getItem(int)}.
		 */
		@Override
		public SVGPathSeg getItem(int index) throws DOMException {
			if (hasAnimVal) {
				return super.getItem(index);
			}
			return getPathSegList().getItem(index);
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
		 * <b>DOM</b>: Implements {@link SVGPathSegList#clear()}.
		 */
		@Override
		public void clear() throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.pathseg.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPathSegList#initialize(SVGPathSeg)}.
		 */
		@Override
		public SVGPathSeg initialize(SVGPathSeg newItem) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.pathseg.list", null);
		}

		/**
		 * <b>DOM</b>: Implements
		 * {@link SVGPathSegList#insertItemBefore(SVGPathSeg, int)}.
		 */
		@Override
		public SVGPathSeg insertItemBefore(SVGPathSeg newItem, int index) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.pathseg.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPathSegList#replaceItem(SVGPathSeg, int)}.
		 */
		@Override
		public SVGPathSeg replaceItem(SVGPathSeg newItem, int index) throws DOMException, SVGException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.pathseg.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPathSegList#removeItem(int)}.
		 */
		@Override
		public SVGPathSeg removeItem(int index) throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.pathseg.list", null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPathSegList#appendItem(SVGPathSeg)}.
		 */
		@Override
		public SVGPathSeg appendItem(SVGPathSeg newItem) throws DOMException {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.pathseg.list", null);
		}

		/**
		 * Pass by reference integer for use by newItem.
		 */
		private int[] parameterIndex = new int[1];

		/**
		 * Creates a new SVGPathSegItem from the given path command and array of
		 * parameter values.
		 */
		protected SVGPathSegItem newItem(short command, float[] parameters, int[] j) {
			switch (command) {
			case SVGPathSeg.PATHSEG_ARC_ABS:
			case SVGPathSeg.PATHSEG_ARC_REL:
				return new SVGPathSegArcItem(command, PATHSEG_LETTERS[command], parameters[j[0]++], parameters[j[0]++],
						parameters[j[0]++], parameters[j[0]++] != 0, parameters[j[0]++] != 0, parameters[j[0]++],
						parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_CLOSEPATH:
				return new SVGPathSegItem(command, PATHSEG_LETTERS[command]);
			case SVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS:
			case SVGPathSeg.PATHSEG_CURVETO_CUBIC_REL:
				return new SVGPathSegCurvetoCubicItem(command, PATHSEG_LETTERS[command], parameters[j[0]++],
						parameters[j[0]++], parameters[j[0]++], parameters[j[0]++], parameters[j[0]++],
						parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_ABS:
			case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_REL:
				return new SVGPathSegCurvetoCubicSmoothItem(command, PATHSEG_LETTERS[command], parameters[j[0]++],
						parameters[j[0]++], parameters[j[0]++], parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_ABS:
			case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_REL:
				return new SVGPathSegCurvetoQuadraticItem(command, PATHSEG_LETTERS[command], parameters[j[0]++],
						parameters[j[0]++], parameters[j[0]++], parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_ABS:
			case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_REL:
				return new SVGPathSegCurvetoQuadraticSmoothItem(command, PATHSEG_LETTERS[command], parameters[j[0]++],
						parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_LINETO_ABS:
			case SVGPathSeg.PATHSEG_LINETO_REL:
			case SVGPathSeg.PATHSEG_MOVETO_ABS:
			case SVGPathSeg.PATHSEG_MOVETO_REL:
				return new SVGPathSegMovetoLinetoItem(command, PATHSEG_LETTERS[command], parameters[j[0]++],
						parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_REL:
			case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_ABS:
				return new SVGPathSegLinetoHorizontalItem(command, PATHSEG_LETTERS[command], parameters[j[0]++]);
			case SVGPathSeg.PATHSEG_LINETO_VERTICAL_REL:
			case SVGPathSeg.PATHSEG_LINETO_VERTICAL_ABS:
				return new SVGPathSegLinetoVerticalItem(command, PATHSEG_LETTERS[command], parameters[j[0]++]);
			}
			return null;
		}

		/**
		 * Sets the animated value.
		 */
		protected void setAnimatedValue(short[] commands, float[] parameters) {
			int size = itemList.size();
			int i = 0;
			int[] j = parameterIndex;
			j[0] = 0;
			while (i < size && i < commands.length) {
				SVGPathSeg s = (SVGPathSeg) itemList.get(i);
				if (s.getPathSegType() != commands[i]) {
					s = newItem(commands[i], parameters, j);
				} else {
					switch (commands[i]) {
					case SVGPathSeg.PATHSEG_ARC_ABS:
					case SVGPathSeg.PATHSEG_ARC_REL: {
						SVGPathSegArcItem ps = (SVGPathSegArcItem) s;
						ps.setR1(parameters[j[0]++]);
						ps.setR2(parameters[j[0]++]);
						ps.setAngle(parameters[j[0]++]);
						ps.setLargeArcFlag(parameters[j[0]++] != 0);
						ps.setSweepFlag(parameters[j[0]++] != 0);
						ps.setX(parameters[j[0]++]);
						ps.setY(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_CLOSEPATH:
						// Nothing to update.
						break;
					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS:
					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_REL: {
						SVGPathSegCurvetoCubicItem ps = (SVGPathSegCurvetoCubicItem) s;
						ps.setX1(parameters[j[0]++]);
						ps.setY1(parameters[j[0]++]);
						ps.setX2(parameters[j[0]++]);
						ps.setY2(parameters[j[0]++]);
						ps.setX(parameters[j[0]++]);
						ps.setY(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_ABS:
					case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_REL: {
						SVGPathSegCurvetoCubicSmoothItem ps = (SVGPathSegCurvetoCubicSmoothItem) s;
						ps.setX2(parameters[j[0]++]);
						ps.setY2(parameters[j[0]++]);
						ps.setX(parameters[j[0]++]);
						ps.setY(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_ABS:
					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_REL: {
						SVGPathSegCurvetoQuadraticItem ps = (SVGPathSegCurvetoQuadraticItem) s;
						ps.setX1(parameters[j[0]++]);
						ps.setY1(parameters[j[0]++]);
						ps.setX(parameters[j[0]++]);
						ps.setY(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_ABS:
					case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_REL: {
						SVGPathSegCurvetoQuadraticSmoothItem ps = (SVGPathSegCurvetoQuadraticSmoothItem) s;
						ps.setX(parameters[j[0]++]);
						ps.setY(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_LINETO_ABS:
					case SVGPathSeg.PATHSEG_LINETO_REL:
					case SVGPathSeg.PATHSEG_MOVETO_ABS:
					case SVGPathSeg.PATHSEG_MOVETO_REL: {
						SVGPathSegMovetoLinetoItem ps = (SVGPathSegMovetoLinetoItem) s;
						ps.setX(parameters[j[0]++]);
						ps.setY(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_REL:
					case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_ABS: {
						SVGPathSegLinetoHorizontalItem ps = (SVGPathSegLinetoHorizontalItem) s;
						ps.setX(parameters[j[0]++]);
						break;
					}
					case SVGPathSeg.PATHSEG_LINETO_VERTICAL_REL:
					case SVGPathSeg.PATHSEG_LINETO_VERTICAL_ABS: {
						SVGPathSegLinetoVerticalItem ps = (SVGPathSegLinetoVerticalItem) s;
						ps.setY(parameters[j[0]++]);
						break;
					}
					}
				}
				i++;
			}
			while (i < commands.length) {
				appendItemImpl(newItem(commands[i], parameters, j));
				i++;
			}
			while (size > commands.length) {
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
