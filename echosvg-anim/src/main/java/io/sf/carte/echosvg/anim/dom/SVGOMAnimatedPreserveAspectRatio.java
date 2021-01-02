/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.anim.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

import io.sf.carte.echosvg.anim.values.AnimatablePreserveAspectRatioValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.svg.AbstractSVGPreserveAspectRatio;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This class implements the {@link SVGAnimatedPreserveAspectRatio} interface.
 *
 * @author <a href="mailto:tonny@kiyut.com">Tonny Kohar</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMAnimatedPreserveAspectRatio extends AbstractSVGAnimatedValue
		implements SVGAnimatedPreserveAspectRatio {

	/**
	 * The base value.
	 */
	protected BaseSVGPARValue baseVal;

	/**
	 * The animated value.
	 */
	protected AnimSVGPARValue animVal;

	/**
	 * Whether the value is changing.
	 */
	protected boolean changing;

	/**
	 * Creates a new SVGOMAnimatedPreserveAspectRatio.
	 * 
	 * @param elt The associated element.
	 */
	public SVGOMAnimatedPreserveAspectRatio(AbstractElement elt) {
		super(elt, null, SVGConstants.SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedPreserveAspectRatio#getBaseVal()}.
	 */
	@Override
	public SVGPreserveAspectRatio getBaseVal() {
		if (baseVal == null) {
			baseVal = new BaseSVGPARValue();
		}
		return baseVal;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedPreserveAspectRatio#getAnimVal()}.
	 */
	@Override
	public SVGPreserveAspectRatio getAnimVal() {
		if (animVal == null) {
			animVal = new AnimSVGPARValue();
		}
		return animVal;
	}

	/**
	 * Throws an exception if the points list value is malformed.
	 */
	public void check() {
		if (!hasAnimVal) {
			if (baseVal == null) {
				baseVal = new BaseSVGPARValue();
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
		SVGPreserveAspectRatio par = getBaseVal();
		return new AnimatablePreserveAspectRatioValue(target, par.getAlign(), par.getMeetOrSlice());
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
			if (animVal == null) {
				animVal = new AnimSVGPARValue();
			}
			AnimatablePreserveAspectRatioValue animPAR = (AnimatablePreserveAspectRatioValue) val;
			animVal.setAnimatedValue(animPAR.getAlign(), animPAR.getMeetOrSlice());
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
	 * This class represents the SVGPreserveAspectRatio returned by
	 * {@link #getBaseVal()}.
	 */
	public class BaseSVGPARValue extends AbstractSVGPreserveAspectRatio {

		/**
		 * Whether the attribute is malformed.
		 */
		protected boolean malformed;

		/**
		 * Creates a new BaseSVGPARValue.
		 */
		public BaseSVGPARValue() {
			invalidate();
		}

		/**
		 * Create a DOMException.
		 */
		@Override
		protected DOMException createDOMException(short type, String key, Object[] args) {
			return element.createDOMException(type, key, args);
		}

		/**
		 * Sets the associated DOM attribute.
		 */
		@Override
		protected void setAttributeValue(String value) throws DOMException {
			try {
				changing = true;
				element.setAttributeNS(null, SVGConstants.SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE, value);
				malformed = false;
			} finally {
				changing = false;
			}
		}

		/**
		 * Re-reads the DOM attribute value.
		 */
		protected void invalidate() {
			String s = element.getAttributeNS(null, SVGConstants.SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);
			setValueAsString(s);
		}
	}

	/**
	 * This class represents the SVGPreserveAspectRatio returned by
	 * {@link #getAnimVal()}.
	 */
	public class AnimSVGPARValue extends AbstractSVGPreserveAspectRatio {

		/**
		 * Create a DOMException.
		 */
		@Override
		protected DOMException createDOMException(short type, String key, Object[] args) {
			return element.createDOMException(type, key, args);
		}

		/**
		 * Sets the associated DOM attribute. Does nothing, since animated values aren't
		 * reflected in the DOM.
		 */
		@Override
		protected void setAttributeValue(String value) throws DOMException {
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPreserveAspectRatio#getAlign()}.
		 */
		@Override
		public short getAlign() {
			if (hasAnimVal) {
				return super.getAlign();
			}
			return getBaseVal().getAlign();
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPreserveAspectRatio#getMeetOrSlice()}.
		 */
		@Override
		public short getMeetOrSlice() {
			if (hasAnimVal) {
				return super.getMeetOrSlice();
			}
			return getBaseVal().getMeetOrSlice();
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPreserveAspectRatio#setAlign(short)}.
		 */
		@Override
		public void setAlign(short align) {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.preserve.aspect.ratio",
					null);
		}

		/**
		 * <b>DOM</b>: Implements {@link SVGPreserveAspectRatio#setMeetOrSlice(short)}.
		 */
		@Override
		public void setMeetOrSlice(short meetOrSlice) {
			throw element.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.preserve.aspect.ratio",
					null);
		}

		/**
		 * Updates the animated value.
		 */
		protected void setAnimatedValue(short align, short meetOrSlice) {
			this.align = align;
			this.meetOrSlice = meetOrSlice;
		}
	}
}
