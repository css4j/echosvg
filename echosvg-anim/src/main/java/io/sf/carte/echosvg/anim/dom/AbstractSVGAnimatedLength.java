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
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLength;

import io.sf.carte.echosvg.anim.values.AnimatableLengthValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.svg.LiveAttributeException;
import io.sf.carte.echosvg.dom.svg.LiveAttributeValue;
import io.sf.carte.echosvg.parser.UnitProcessor;

/**
 * This class provides an implementation of the {@link
 * SVGAnimatedLength} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public abstract class AbstractSVGAnimatedLength
    extends    AbstractSVGAnimatedValue
    implements SVGAnimatedLength,
               LiveAttributeValue {

    /**
     * This constant represents horizontal lengths.
     */
    public static final short HORIZONTAL_LENGTH =
        UnitProcessor.HORIZONTAL_LENGTH;

    /**
     * This constant represents vertical lengths.
     */
    public static final short VERTICAL_LENGTH =
        UnitProcessor.VERTICAL_LENGTH;

    /**
     * This constant represents other lengths.
     */
    public static final short OTHER_LENGTH =
        UnitProcessor.OTHER_LENGTH;

    /**
     * This length's direction.
     */
    protected short direction;

    /**
     * The base value.
     */
    protected BaseSVGLength baseVal;

    /**
     * The current animated value.
     */
    protected AnimSVGLength animVal;

    /**
     * Whether the value is changing.
     */
    protected boolean changing;

    /**
     * Whether the value must be non-negative.
     */
    protected boolean nonNegative;

    /**
     * Creates a new SVGAnimatedLength.
     * @param elt The associated element.
     * @param ns The attribute's namespace URI.
     * @param ln The attribute's local name.
     * @param dir The length's direction.
     * @param nonneg Whether the length must be non-negative.
     */
    public AbstractSVGAnimatedLength(AbstractElement elt,
                                     String ns,
                                     String ln,
                                     short dir,
                                     boolean nonneg) {
        super(elt, ns, ln);
        direction = dir;
        nonNegative = nonneg;
    }

    /**
     * @return the default value to use when the associated attribute
     * was not specified.
     */
    protected abstract String getDefaultValue();

    /**
     * <b>DOM</b>: Implements {@link SVGAnimatedLength#getBaseVal()}.
     */
    @Override
    public SVGLength getBaseVal() {
        if (baseVal == null) {
            baseVal = new BaseSVGLength(direction);
        }
        return baseVal;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGAnimatedLength#getAnimVal()}.
     */
    @Override
    public SVGLength getAnimVal() {
        if (animVal == null) {
            animVal = new AnimSVGLength(direction);
        }
        return animVal;
    }

    /**
     * Gets the current animated length value.  If the attribute is missing
     * or malformed, an exception is thrown.
     */
    public float getCheckedValue() {
        if (hasAnimVal) {
            if (animVal == null) {
                animVal = new AnimSVGLength(direction);
            }
            if (nonNegative && animVal.value < 0) {
                throw new LiveAttributeException
                    (element, localName,
                     LiveAttributeException.ERR_ATTRIBUTE_NEGATIVE,
                     animVal.getValueAsString());
            }
            return animVal.getValue();
        } else {
            if (baseVal == null) {
                baseVal = new BaseSVGLength(direction);
            }
            baseVal.revalidate();
            if (baseVal.missing) {
                throw new LiveAttributeException
                    (element, localName,
                     LiveAttributeException.ERR_ATTRIBUTE_MISSING, null);
            } else if (baseVal.unitType ==
                        SVGLength.SVG_LENGTHTYPE_UNKNOWN) {
                throw new LiveAttributeException
                    (element, localName,
                     LiveAttributeException.ERR_ATTRIBUTE_MALFORMED,
                     baseVal.getValueAsString());
            }
            if (nonNegative && baseVal.value < 0) {
                throw new LiveAttributeException
                    (element, localName,
                     LiveAttributeException.ERR_ATTRIBUTE_NEGATIVE,
                     baseVal.getValueAsString());
            }
            return baseVal.getValue();
        }
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
            AnimatableLengthValue animLength = (AnimatableLengthValue) val;
            if (animVal == null) {
                animVal = new AnimSVGLength(direction);
            }
            animVal.setAnimatedValue(animLength.getLengthType(),
                                     animLength.getLengthValue());
        }
        fireAnimatedAttributeListeners();
    }

    /**
     * Returns the base value of the attribute as an {@link AnimatableValue}.
     */
    @Override
    public AnimatableValue getUnderlyingValue(AnimationTarget target) {
        SVGLength base = getBaseVal();
        return new AnimatableLengthValue
            (target, base.getUnitType(), base.getValueInSpecifiedUnits(),
             target.getPercentageInterpretation
                 (getNamespaceURI(), getLocalName(), false));
    }

    /**
     * Called when an Attr node has been added.
     */
    @Override
    public void attrAdded(Attr node, String newv) {
        attrChanged();
    }

    /**
     * Called when an Attr node has been modified.
     */
    @Override
    public void attrModified(Attr node, String oldv, String newv) {
        attrChanged();
    }

    /**
     * Called when an Attr node has been removed.
     */
    @Override
    public void attrRemoved(Attr node, String oldv) {
        attrChanged();
    }

    /**
     * Called when the attribute has changed in some way.
     */
    protected void attrChanged() {
        if (!changing && baseVal != null) {
            baseVal.invalidate();
        }
        fireBaseAttributeListeners();
        if (!hasAnimVal) {
            fireAnimatedAttributeListeners();
        }
    }

    /**
     * This class represents the SVGLength returned by {@link AbstractSVGAnimatedLength#getBaseVal() }.
     */
    protected class BaseSVGLength extends AbstractSVGLength {

        /**
         * Whether this length is valid.
         */
        protected boolean valid;

        /**
         * Whether the attribute is missing.
         */
        protected boolean missing;

        /**
         * Creates a new BaseSVGLength.
         * @param direction is one of HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
         */
        public BaseSVGLength(short direction) {
            super(direction);
        }

        /**
         * Invalidates this length.
         */
        public void invalidate() {
            valid = false;
        }

        /**
         * Resets the value of the associated attribute.
         */
        @Override
        protected void reset() {
            try {
                changing = true;
                valid = true;
                String value = getValueAsString();
                element.setAttributeNS(namespaceURI, localName, value);
            } finally {
                changing = false;
            }
        }

        /**
         * Initializes the length, if needed.
         */
        @Override
        protected void revalidate() {
            if (valid) {
                return;
            }

            missing = false;
            valid = true;

            Attr attr = element.getAttributeNodeNS(namespaceURI, localName);
            String s;
            if (attr == null) {
                s = getDefaultValue();
                if (s == null) {
                    missing = true;
                    return;
                }
            } else {
                s = attr.getValue();
            }

            parse(s);
        }

        /**
         * Returns the element this length is associated with.
         */
        @Override
        protected SVGOMElement getAssociatedElement() {
            return (SVGOMElement)element;
        }
    }

    /**
     * This class represents the SVGLength returned by {@link AbstractSVGAnimatedLength#getAnimVal()}.
     */
    protected class AnimSVGLength extends AbstractSVGLength {

        /**
         * Creates a new AnimSVGLength.
         * @param direction is one of HORIZONTAL_LENGTH, VERTICAL_LENGTH, or OTHER_LENGTH
         */
        public AnimSVGLength(short direction) {
            super(direction);
        }

        /**
         * <b>DOM</b>: Implements {@link SVGLength#getUnitType()}.
         */
        @Override
        public short getUnitType() {
            if (hasAnimVal) {
                return super.getUnitType();
            }
            return getBaseVal().getUnitType();
        }

        /**
         * <b>DOM</b>: Implements {@link SVGLength#getValue()}.
         */
        @Override
        public float getValue() {
            if (hasAnimVal) {
                return super.getValue();
            }
            return getBaseVal().getValue();
        }

        /**
         * <b>DOM</b>: Implements {@link SVGLength#getValueInSpecifiedUnits()}.
         */
        @Override
        public float getValueInSpecifiedUnits() {
            if (hasAnimVal) {
                return super.getValueInSpecifiedUnits();
            }
            return getBaseVal().getValueInSpecifiedUnits();
        }

        /**
         * <b>DOM</b>: Implements {@link SVGLength#getValueAsString()}.
         */
        @Override
        public String getValueAsString() {
            if (hasAnimVal) {
                return super.getValueAsString();
            }
            return getBaseVal().getValueAsString();
        }

        /**
         * <b>DOM</b>: Implements {@link SVGLength#setValue(float)}.
         */
        @Override
        public void setValue(float value) throws DOMException {
            throw element.createDOMException
                (DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length",
                 null);
        }

        /**
         * <b>DOM</b>: Implements {@link
         * SVGLength#setValueInSpecifiedUnits(float)}.
         */
        @Override
        public void setValueInSpecifiedUnits(float value) throws DOMException {
            throw element.createDOMException
                (DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length",
                 null);
        }

        /**
         * <b>DOM</b>: Implements {@link SVGLength#setValueAsString(String)}.
         */
        @Override
        public void setValueAsString(String value) throws DOMException {
            throw element.createDOMException
                (DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length",
                 null);
        }

        /**
         * <b>DOM</b>: Implements {@link
         * SVGLength#newValueSpecifiedUnits(short,float)}.
         */
        @Override
        public void newValueSpecifiedUnits(short unit, float value) {
            throw element.createDOMException
                (DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length",
                 null);
        }

        /**
         * <b>DOM</b>: Implements {@link
         * SVGLength#convertToSpecifiedUnits(short)}.
         */
        @Override
        public void convertToSpecifiedUnits(short unit) {
            throw element.createDOMException
                (DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.length",
                 null);
        }

        /**
         * Returns the element this length is associated with.
         */
        @Override
        protected SVGOMElement getAssociatedElement() {
            return (SVGOMElement) element;
        }

        /**
         * Sets the animated value.
         * @param type one of the values defines in org.w3c.dom.svg.SVGLength
         * @param val the length
         */
        protected void setAnimatedValue(int type, float val) {
            super.newValueSpecifiedUnits((short) type, val);
        }
    }
}
