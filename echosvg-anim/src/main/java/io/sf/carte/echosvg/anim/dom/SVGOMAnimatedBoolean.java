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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedBoolean;

import io.sf.carte.echosvg.anim.values.AnimatableBooleanValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;

/**
 * This class implements the {@link SVGAnimatedBoolean} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGOMAnimatedBoolean extends AbstractSVGAnimatedValue implements SVGAnimatedBoolean {

	/**
	 * The default value.
	 */
	protected boolean defaultValue;

	/**
	 * Whether the base value is valid.
	 */
	protected boolean valid;

	/**
	 * The current base value.
	 */
	protected boolean baseVal;

	/**
	 * The current animated value.
	 */
	protected boolean animVal;

	/**
	 * Whether the value is changing.
	 */
	protected boolean changing;

	/**
	 * Creates a new SVGOMAnimatedBoolean.
	 * 
	 * @param elt The associated element.
	 * @param ns  The attribute's namespace URI.
	 * @param ln  The attribute's local name.
	 * @param val The default value, if the attribute is not specified.
	 */
	public SVGOMAnimatedBoolean(AbstractElement elt, String ns, String ln, boolean val) {
		super(elt, ns, ln);
		defaultValue = val;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedBoolean#getBaseVal()}.
	 */
	@Override
	public boolean getBaseVal() {
		if (!valid) {
			update();
		}
		return baseVal;
	}

	/**
	 * Updates the base value from the attribute.
	 */
	protected void update() {
		Attr attr = element.getAttributeNodeNS(namespaceURI, localName);
		if (attr == null) {
			baseVal = defaultValue;
		} else {
			baseVal = attr.getValue().equals("true");
		}
		valid = true;
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedBoolean#setBaseVal(boolean)}.
	 */
	@Override
	public void setBaseVal(boolean baseVal) throws DOMException {
		try {
			this.baseVal = baseVal;
			valid = true;
			changing = true;
			element.setAttributeNS(namespaceURI, localName, String.valueOf(baseVal));
		} finally {
			changing = false;
		}
	}

	/**
	 * <b>DOM</b>: Implements {@link SVGAnimatedBoolean#getAnimVal()}.
	 */
	@Override
	public boolean getAnimVal() {
		if (hasAnimVal) {
			return animVal;
		}
		if (!valid) {
			update();
		}
		return baseVal;
	}

	/**
	 * Sets the animated value.
	 */
	public void setAnimatedValue(boolean animVal) {
		hasAnimVal = true;
		this.animVal = animVal;
		fireAnimatedAttributeListeners();
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
			this.animVal = ((AnimatableBooleanValue) val).getValue();
		}
		fireAnimatedAttributeListeners();
	}

	/**
	 * Returns the base value of the attribute as an {@link AnimatableValue}.
	 */
	@Override
	public AnimatableValue getUnderlyingValue(AnimationTarget target) {
		return new AnimatableBooleanValue(target, getBaseVal());
	}

	/**
	 * Called when an Attr node has been added.
	 */
	@Override
	public void attrAdded(Attr node, String newv) {
		if (!changing) {
			valid = false;
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
			valid = false;
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
			valid = false;
		}
		fireBaseAttributeListeners();
		if (!hasAnimVal) {
			fireAnimatedAttributeListeners();
		}
	}
}
