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
package io.sf.carte.echosvg.ext.awt.image.renderable;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;

import io.sf.carte.echosvg.ext.awt.image.GraphicsUtil;
import io.sf.carte.echosvg.ext.awt.image.Light;
import io.sf.carte.echosvg.ext.awt.image.PadMode;
import io.sf.carte.echosvg.ext.awt.image.rendered.AffineRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.BumpMap;
import io.sf.carte.echosvg.ext.awt.image.rendered.CachableRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.PadRed;
import io.sf.carte.echosvg.ext.awt.image.rendered.SpecularLightingRed;

/**
 * Implementation of the SpecularLightRable interface.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SpecularLightingRable8Bit extends AbstractColorInterpolationRable implements SpecularLightingRable {
	/**
	 * Surface Scale
	 */
	private double surfaceScale;

	/**
	 * Specular constant
	 */
	private double ks;

	/**
	 * Specular exponent
	 */
	private double specularExponent;

	/**
	 * Light used for the specular lighting computations
	 */
	private Light light;

	/**
	 * Lit Area
	 */
	private Rectangle2D litRegion;

	/**
	 * The dx/dy to use in user space for the sobel gradient.
	 */
	private float[] kernelUnitLength = null;

	public SpecularLightingRable8Bit(Filter src, Rectangle2D litRegion, Light light, double ks, double specularExponent,
			double surfaceScale, double[] kernelUnitLength) {
		super(src, null);
		setLight(light);
		setKs(ks);
		setSpecularExponent(specularExponent);
		setSurfaceScale(surfaceScale);
		setLitRegion(litRegion);
		setKernelUnitLength(kernelUnitLength);
	}

	/**
	 * Returns the source to be filtered
	 */
	@Override
	public Filter getSource() {
		return (Filter) getSources().get(0);
	}

	/**
	 * Sets the source to be filtered
	 */
	@Override
	public void setSource(Filter src) {
		init(src, null);
	}

	/**
	 * Returns this filter's bounds
	 */
	@Override
	public Rectangle2D getBounds2D() {
		return (Rectangle2D) (litRegion.clone());
	}

	/**
	 * Returns this filter's litRegion
	 */
	@Override
	public Rectangle2D getLitRegion() {
		return getBounds2D();
	}

	/**
	 * Set this filter's litRegion
	 */
	@Override
	public void setLitRegion(Rectangle2D litRegion) {
		touch();
		this.litRegion = litRegion;
	}

	/**
	 * @return Light object used for the specular lighting
	 */
	@Override
	public Light getLight() {
		return light;
	}

	/**
	 * @param light New Light object
	 */
	@Override
	public void setLight(Light light) {
		touch();
		this.light = light;
	}

	/**
	 * @return surfaceScale
	 */
	@Override
	public double getSurfaceScale() {
		return surfaceScale;
	}

	/**
	 * Sets the surface scale
	 */
	@Override
	public void setSurfaceScale(double surfaceScale) {
		touch();
		this.surfaceScale = surfaceScale;
	}

	/**
	 * @return specular constant, or ks.
	 */
	@Override
	public double getKs() {
		return ks;
	}

	/**
	 * Sets the specular constant, or ks
	 */
	@Override
	public void setKs(double ks) {
		touch();
		this.ks = ks;
	}

	/**
	 * @return specular exponent
	 */
	@Override
	public double getSpecularExponent() {
		return specularExponent;
	}

	/**
	 * Sets the specular exponent
	 */
	@Override
	public void setSpecularExponent(double specularExponent) {
		touch();
		this.specularExponent = specularExponent;
	}

	/**
	 * Returns the min [dx,dy] distance in user space for evalutation of the sobel
	 * gradient.
	 */
	@Override
	public double[] getKernelUnitLength() {
		if (kernelUnitLength == null)
			return null;

		double[] ret = new double[2];
		ret[0] = kernelUnitLength[0];
		ret[1] = kernelUnitLength[1];
		return ret;
	}

	/**
	 * Sets the min [dx,dy] distance in user space for evaluation of the sobel
	 * gradient. If set to zero or null then device space will be used.
	 */
	@Override
	public void setKernelUnitLength(double[] kernelUnitLength) {
		touch();
		if (kernelUnitLength == null) {
			this.kernelUnitLength = null;
			return;
		}

		if (this.kernelUnitLength == null)
			this.kernelUnitLength = new float[2];

		this.kernelUnitLength[0] = (float) kernelUnitLength[0];
		this.kernelUnitLength[1] = (float) kernelUnitLength[1];
	}

	@Override
	public RenderedImage createRendering(RenderContext rc) {
		Shape aoi = rc.getAreaOfInterest();
		if (aoi == null)
			aoi = getBounds2D();

		Rectangle2D aoiR = aoi.getBounds2D();
		Rectangle2D.intersect(aoiR, getBounds2D(), aoiR);

		AffineTransform at = rc.getTransform();
		Rectangle devRect = at.createTransformedShape(aoiR).getBounds();

		if (devRect.width == 0 || devRect.height == 0) {
			return null;
		}

		//
		// SpecularLightingRed only operates on a scaled space.
		// The following extracts the scale portion of the
		// user to device transform
		//
		// The source is rendered with the scale-only transform
		// and the rendered result is used as a bumpMap for the
		// SpecularLightingRed filter.
		//
		double sx = at.getScaleX();
		double sy = at.getScaleY();

		double shx = at.getShearX();
		double shy = at.getShearY();

		double tx = at.getTranslateX();
		double ty = at.getTranslateY();

		// The Scale is the "hypotonose" of the matrix vectors.
		double scaleX = Math.sqrt(sx * sx + shy * shy);
		double scaleY = Math.sqrt(sy * sy + shx * shx);

		if (scaleX == 0 || scaleY == 0) {
			// Non invertible transform
			return null;
		}

		// These values represent the scale factor to the intermediate
		// coordinate system where we will apply our convolution.
		if (kernelUnitLength != null) {
			if (scaleX >= 1 / kernelUnitLength[0])
				scaleX = 1 / kernelUnitLength[0];

			if (scaleY >= 1 / kernelUnitLength[1])
				scaleY = 1 / kernelUnitLength[1];
		}

		AffineTransform scale = AffineTransform.getScaleInstance(scaleX, scaleY);

		devRect = scale.createTransformedShape(aoiR).getBounds();

		// Grow for surround needs.
		aoiR.setRect(aoiR.getX() - (2 / scaleX), aoiR.getY() - (2 / scaleY), aoiR.getWidth() + (4 / scaleX),
				aoiR.getHeight() + (4 / scaleY));

		// Build texture from the source
		rc = (RenderContext) rc.clone();
		rc.setAreaOfInterest(aoiR);
		rc.setTransform(scale);

		// System.out.println("scaleX / scaleY : " + scaleX + "/" + scaleY);

		CachableRed cr;
		cr = GraphicsUtil.wrap(getSource().createRendering(rc));

		BumpMap bumpMap = new BumpMap(cr, surfaceScale, scaleX, scaleY);

		cr = new SpecularLightingRed(ks, specularExponent, light, bumpMap, devRect, 1 / scaleX, 1 / scaleY,
				isColorSpaceLinear());

		// Return sheared/rotated tiled image
		AffineTransform shearAt = new AffineTransform(sx / scaleX, shy / scaleX, shx / scaleY, sy / scaleY, tx, ty);

		if (!shearAt.isIdentity()) {
			RenderingHints rh = rc.getRenderingHints();
			Rectangle padRect = new Rectangle(devRect.x - 1, devRect.y - 1, devRect.width + 2, devRect.height + 2);
			cr = new PadRed(cr, padRect, PadMode.REPLICATE, rh);

			cr = new AffineRed(cr, shearAt, rh);
		}

		return cr;
	}
}
