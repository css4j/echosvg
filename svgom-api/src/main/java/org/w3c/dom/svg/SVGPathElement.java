/*
 * Copyright (c) 2010 World Wide Web Consortium,
 *
 * (Massachusetts Institute of Technology, European Research Consortium for
 * Informatics and Mathematics, Keio University). All Rights Reserved. This
 * work is distributed under the W3C(r) Software License [1] in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231
 */

package org.w3c.dom.svg;

import org.w3c.dom.events.EventTarget;

public interface SVGPathElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable,
		SVGTransformable, EventTarget, SVGAnimatedPathData {
	public SVGAnimatedNumber getPathLength();

	public float getTotalLength();

	public SVGPoint getPointAtLength(float distance);

	public int getPathSegAtLength(float distance);

	public SVGPathSegClosePath createSVGPathSegClosePath();

	public SVGPathSegMovetoAbs createSVGPathSegMovetoAbs(float x, float y);

	public SVGPathSegMovetoRel createSVGPathSegMovetoRel(float x, float y);

	public SVGPathSegLinetoAbs createSVGPathSegLinetoAbs(float x, float y);

	public SVGPathSegLinetoRel createSVGPathSegLinetoRel(float x, float y);

	public SVGPathSegCurvetoCubicAbs createSVGPathSegCurvetoCubicAbs(float x, float y, float x1, float y1, float x2,
			float y2);

	public SVGPathSegCurvetoCubicRel createSVGPathSegCurvetoCubicRel(float x, float y, float x1, float y1, float x2,
			float y2);

	public SVGPathSegCurvetoQuadraticAbs createSVGPathSegCurvetoQuadraticAbs(float x, float y, float x1, float y1);

	public SVGPathSegCurvetoQuadraticRel createSVGPathSegCurvetoQuadraticRel(float x, float y, float x1, float y1);

	public SVGPathSegArcAbs createSVGPathSegArcAbs(float x, float y, float r1, float r2, float angle,
			boolean largeArcFlag, boolean sweepFlag);

	public SVGPathSegArcRel createSVGPathSegArcRel(float x, float y, float r1, float r2, float angle,
			boolean largeArcFlag, boolean sweepFlag);

	public SVGPathSegLinetoHorizontalAbs createSVGPathSegLinetoHorizontalAbs(float x);

	public SVGPathSegLinetoHorizontalRel createSVGPathSegLinetoHorizontalRel(float x);

	public SVGPathSegLinetoVerticalAbs createSVGPathSegLinetoVerticalAbs(float y);

	public SVGPathSegLinetoVerticalRel createSVGPathSegLinetoVerticalRel(float y);

	public SVGPathSegCurvetoCubicSmoothAbs createSVGPathSegCurvetoCubicSmoothAbs(float x, float y, float x2, float y2);

	public SVGPathSegCurvetoCubicSmoothRel createSVGPathSegCurvetoCubicSmoothRel(float x, float y, float x2, float y2);

	public SVGPathSegCurvetoQuadraticSmoothAbs createSVGPathSegCurvetoQuadraticSmoothAbs(float x, float y);

	public SVGPathSegCurvetoQuadraticSmoothRel createSVGPathSegCurvetoQuadraticSmoothRel(float x, float y);
}
