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
package io.sf.carte.echosvg.dom.svg;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGMatrix;

/**
 * This class provides an abstract implementation of the {@link SVGMatrix}
 * interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractSVGMatrix implements SVGMatrix {

    /**
     * The transform used to implement flipX.
     */
    protected static final AffineTransform FLIP_X_TRANSFORM =
        new AffineTransform(-1, 0, 0, 1, 0, 0);

    /**
     * The transform used to implement flipX.
     */
    protected static final AffineTransform FLIP_Y_TRANSFORM =
        new AffineTransform(1, 0, 0, -1, 0, 0);

    /**
     * Returns the associated AffineTransform.
     */
    protected abstract AffineTransform getAffineTransform();

    /**
     * Implements {@link SVGMatrix#getA()}.
     */
    @Override
    public float getA() {
        return (float)getAffineTransform().getScaleX();
    }

    /**
     * Implements {@link SVGMatrix#setA(float)}.
     */
    @Override
    public void setA(float a) throws DOMException {
        AffineTransform at = getAffineTransform();
        at.setTransform(a,
                        at.getShearY(),
                        at.getShearX(),
                        at.getScaleY(),
                        at.getTranslateX(),
                        at.getTranslateY());
    }

    /**
     * Implements {@link SVGMatrix#getB()}.
     */
    @Override
    public float getB() {
        return (float)getAffineTransform().getShearY();
    }

    /**
     * Implements {@link SVGMatrix#setB(float)}.
     */
    @Override
    public void setB(float b) throws DOMException {
        AffineTransform at = getAffineTransform();
        at.setTransform(at.getScaleX(),
                        b,
                        at.getShearX(),
                        at.getScaleY(),
                        at.getTranslateX(),
                        at.getTranslateY());
    }

    /**
     * Implements {@link SVGMatrix#getC()}.
     */
    @Override
    public float getC() {
        return (float)getAffineTransform().getShearX();
    }

    /**
     * Implements {@link SVGMatrix#setC(float)}.
     */
    @Override
    public void setC(float c) throws DOMException {
        AffineTransform at = getAffineTransform();
        at.setTransform(at.getScaleX(),
                        at.getShearY(),
                        c,
                        at.getScaleY(),
                        at.getTranslateX(),
                        at.getTranslateY());
    }

    /**
     * Implements {@link SVGMatrix#getD()}.
     */
    @Override
    public float getD() {
        return (float)getAffineTransform().getScaleY();
    }

    /**
     * Implements {@link SVGMatrix#setD(float)}.
     */
    @Override
    public void setD(float d) throws DOMException {
        AffineTransform at = getAffineTransform();
        at.setTransform(at.getScaleX(),
                        at.getShearY(),
                        at.getShearX(),
                        d,
                        at.getTranslateX(),
                        at.getTranslateY());
    }

    /**
     * Implements {@link SVGMatrix#getE()}.
     */
    @Override
    public float getE() {
        return (float)getAffineTransform().getTranslateX();
    }

    /**
     * Implements {@link SVGMatrix#setE(float)}.
     */
    @Override
    public void setE(float e) throws DOMException {
        AffineTransform at = getAffineTransform();
        at.setTransform(at.getScaleX(),
                        at.getShearY(),
                        at.getShearX(),
                        at.getScaleY(),
                        e,
                        at.getTranslateY());
    }

    /**
     * Implements {@link SVGMatrix#getF()}.
     */
    @Override
    public float getF() {
        return (float)getAffineTransform().getTranslateY();
    }

    /**
     * Implements {@link SVGMatrix#setF(float)}.
     */
    @Override
    public void setF(float f) throws DOMException {
        AffineTransform at = getAffineTransform();
        at.setTransform(at.getScaleX(),
                        at.getShearY(),
                        at.getShearX(),
                        at.getScaleY(),
                        at.getTranslateX(),
                        f);
    }

    /**
     * Implements {@link SVGMatrix#multiply(SVGMatrix)}.
     */
    @Override
    public SVGMatrix multiply(SVGMatrix secondMatrix) {
        AffineTransform at = new AffineTransform(secondMatrix.getA(),
                                                 secondMatrix.getB(),
                                                 secondMatrix.getC(),
                                                 secondMatrix.getD(),
                                                 secondMatrix.getE(),
                                                 secondMatrix.getF());
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.concatenate(at);
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#inverse()}.
     */
    @Override
    public SVGMatrix inverse() throws SVGException {
        try {
            return new SVGOMMatrix(getAffineTransform().createInverse());
        } catch (NoninvertibleTransformException e) {
            throw new SVGOMException(SVGException.SVG_MATRIX_NOT_INVERTABLE,
                                     e.getMessage());
        }
    }

    /**
     * Implements {@link SVGMatrix#translate(float,float)}.
     */
    @Override
    public SVGMatrix translate(float x, float y) {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.translate(x, y);
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#scale(float)}.
     */
    @Override
    public SVGMatrix scale(float scaleFactor) {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.scale(scaleFactor, scaleFactor);
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#scaleNonUniform(float,float)}.
     */
    @Override
    public SVGMatrix scaleNonUniform (float scaleFactorX, float scaleFactorY) {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.scale(scaleFactorX, scaleFactorY);
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#rotate(float)}.
     */
    @Override
    public SVGMatrix rotate(float angle) {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.rotate( Math.toRadians( angle ) );
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#rotateFromVector(float,float)}.
     */
    @Override
    public SVGMatrix rotateFromVector(float x, float y) throws SVGException {
        if (x == 0 || y == 0) {
            throw new SVGOMException(SVGException.SVG_INVALID_VALUE_ERR, "");
        }
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.rotate(Math.atan2(y, x));
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#flipX()}.
     */
    @Override
    public SVGMatrix flipX() {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.concatenate(FLIP_X_TRANSFORM);
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#flipY()}.
     */
    @Override
    public SVGMatrix flipY() {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.concatenate(FLIP_Y_TRANSFORM);
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#skewX(float)}.
     */
    @Override
    public SVGMatrix skewX(float angleDeg) {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.concatenate
            (AffineTransform.getShearInstance( Math.tan( Math.toRadians( angleDeg )), 0));
        return new SVGOMMatrix(tr);
    }

    /**
     * Implements {@link SVGMatrix#skewY(float)}.
     */
    @Override
    public SVGMatrix skewY(float angleDeg ) {
        AffineTransform tr = (AffineTransform)getAffineTransform().clone();
        tr.concatenate
            (AffineTransform.getShearInstance(0,  Math.tan( Math.toRadians( angleDeg ) ) ));
        return new SVGOMMatrix(tr);
    }
}
