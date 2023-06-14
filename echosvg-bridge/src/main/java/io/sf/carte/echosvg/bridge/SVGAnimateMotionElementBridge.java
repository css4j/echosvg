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
package io.sf.carte.echosvg.bridge;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAngle;

import io.sf.carte.echosvg.anim.AbstractAnimation;
import io.sf.carte.echosvg.anim.AnimationEngine;
import io.sf.carte.echosvg.anim.MotionAnimation;
import io.sf.carte.echosvg.anim.dom.AnimationTarget;
import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.anim.dom.SVGOMPathElement;
import io.sf.carte.echosvg.anim.values.AnimatableMotionPointValue;
import io.sf.carte.echosvg.anim.values.AnimatableValue;
import io.sf.carte.echosvg.dom.svg.SVGAnimatedPathDataSupport;
import io.sf.carte.echosvg.dom.util.XLinkSupport;
import io.sf.carte.echosvg.ext.awt.geom.ExtendedGeneralPath;
import io.sf.carte.echosvg.parser.AWTPathProducer;
import io.sf.carte.echosvg.parser.AngleHandler;
import io.sf.carte.echosvg.parser.AngleParser;
import io.sf.carte.echosvg.parser.LengthArrayProducer;
import io.sf.carte.echosvg.parser.LengthPairListParser;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.PathParser;

/**
 * Bridge class for the 'animateMotion' animation element.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGAnimateMotionElementBridge extends SVGAnimateElementBridge {

	/**
	 * Returns 'animateMotion'.
	 */
	@Override
	public String getLocalName() {
		return SVG_ANIMATE_MOTION_TAG;
	}

	/**
	 * Returns a new instance of this bridge.
	 */
	@Override
	public Bridge getInstance() {
		return new SVGAnimateMotionElementBridge();
	}

	/**
	 * Creates the animation object for the animation element.
	 */
	@Override
	protected AbstractAnimation createAnimation(AnimationTarget target) {
		animationType = AnimationEngine.ANIM_TYPE_OTHER;
		attributeLocalName = "motion";

		AnimatableValue from = parseLengthPair(SVG_FROM_ATTRIBUTE);
		AnimatableValue to = parseLengthPair(SVG_TO_ATTRIBUTE);
		AnimatableValue by = parseLengthPair(SVG_BY_ATTRIBUTE);

		boolean rotateAuto = false, rotateAutoReverse = false;
		float rotateAngle = 0;
		short rotateAngleUnit = SVGAngle.SVG_ANGLETYPE_UNKNOWN;
		String rotateString = element.getAttributeNS(null, SVG_ROTATE_ATTRIBUTE);
		if (rotateString.length() != 0) {
			if (rotateString.equals("auto")) {
				rotateAuto = true;
			} else if (rotateString.equals("auto-reverse")) {
				rotateAuto = true;
				rotateAutoReverse = true;
			} else {
				class Handler implements AngleHandler {
					float theAngle;
					short theUnit = SVGAngle.SVG_ANGLETYPE_UNSPECIFIED;

					@Override
					public void startAngle() throws ParseException {
					}

					@Override
					public void angleValue(float v) throws ParseException {
						theAngle = v;
					}

					@Override
					public void deg() throws ParseException {
						theUnit = SVGAngle.SVG_ANGLETYPE_DEG;
					}

					@Override
					public void grad() throws ParseException {
						theUnit = SVGAngle.SVG_ANGLETYPE_GRAD;
					}

					@Override
					public void rad() throws ParseException {
						theUnit = SVGAngle.SVG_ANGLETYPE_RAD;
					}

					@Override
					public void endAngle() throws ParseException {
					}
				}
				AngleParser ap = new AngleParser();
				Handler h = new Handler();
				ap.setAngleHandler(h);
				try {
					ap.parse(rotateString);
				} catch (ParseException pEx) {
					throw new BridgeException(ctx, element, pEx, ErrorConstants.ERR_ATTRIBUTE_VALUE_MALFORMED,
							new Object[] { SVG_ROTATE_ATTRIBUTE, rotateString });
				}
				rotateAngle = h.theAngle;
				rotateAngleUnit = h.theUnit;
			}
		}
		return new MotionAnimation(timedElement, this, parseCalcMode(), parseKeyTimes(), parseKeySplines(),
				parseAdditive(), parseAccumulate(), parseValues(), from, to, by, parsePath(), parseKeyPoints(),
				rotateAuto, rotateAutoReverse, rotateAngle, rotateAngleUnit);
	}

	/**
	 * Returns the parsed 'path' attribute (or the path from a referencing 'mpath')
	 * from the animation element.
	 */
	protected ExtendedGeneralPath parsePath() {
		Node n = element.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE && SVG_NAMESPACE_URI.equals(n.getNamespaceURI())
					&& SVG_MPATH_TAG.equals(n.getLocalName())) {
				String uri = XLinkSupport.getXLinkHref((Element) n);
				Element path = ctx.getReferencedElement(element, uri);
				if (path == null || !SVG_NAMESPACE_URI.equals(path.getNamespaceURI())
						|| !SVG_PATH_TAG.equals(path.getLocalName())) {
					throw new BridgeException(ctx, element, ErrorConstants.ERR_URI_BAD_TARGET,
							new Object[] { uri });
				}
				SVGOMPathElement pathElt = (SVGOMPathElement) path;
				AWTPathProducer app = new AWTPathProducer();
				SVGAnimatedPathDataSupport.handlePathSegList(pathElt.getPathSegList(), app);
				return (ExtendedGeneralPath) app.getShape();
			}
			n = n.getNextSibling();
		}
		String pathString = element.getAttributeNS(null, SVG_PATH_ATTRIBUTE);
		if (pathString.length() == 0) {
			return null;
		}
		try {
			AWTPathProducer app = new AWTPathProducer();
			PathParser pp = new PathParser();
			pp.setPathHandler(app);
			pp.parse(pathString);
			return (ExtendedGeneralPath) app.getShape();
		} catch (ParseException pEx) {
			throw new BridgeException(ctx, element, pEx, ErrorConstants.ERR_ATTRIBUTE_VALUE_MALFORMED,
					new Object[] { SVG_PATH_ATTRIBUTE, pathString });
		}
	}

	/**
	 * Returns the parsed 'keyPoints' attribute from the animation element.
	 */
	protected float[] parseKeyPoints() {
		String keyPointsString = element.getAttributeNS(null, SVG_KEY_POINTS_ATTRIBUTE);
		int len = keyPointsString.length();
		if (len == 0) {
			return null;
		}
		List<Float> keyPoints = new ArrayList<>(7);
		int i = 0, start = 0, end;
		char c;
		outer: while (i < len) {
			while (keyPointsString.charAt(i) == ' ') {
				i++;
				if (i == len) {
					break outer;
				}
			}
			start = i++;
			if (i != len) {
				c = keyPointsString.charAt(i);
				while (c != ' ' && c != ';' && c != ',') {
					i++;
					if (i == len) {
						break;
					}
					c = keyPointsString.charAt(i);
				}
			}
			end = i++;
			try {
				float keyPointCoord = Float.parseFloat(keyPointsString.substring(start, end));
				keyPoints.add(keyPointCoord);
			} catch (NumberFormatException nfEx) {
				throw new BridgeException(ctx, element, nfEx, ErrorConstants.ERR_ATTRIBUTE_VALUE_MALFORMED,
						new Object[] { SVG_KEY_POINTS_ATTRIBUTE, keyPointsString });
			}
		}
		len = keyPoints.size();
		float[] ret = new float[len];
		for (int j = 0; j < len; j++) {
			ret[j] = keyPoints.get(j);
		}
		return ret;
	}

	/**
	 * Returns the calcMode that the animation defaults to if none is specified.
	 */
	@Override
	protected int getDefaultCalcMode() {
		return AbstractAnimation.CALC_MODE_PACED;
	}

	/**
	 * Returns the parsed 'values' attribute from the animation element.
	 */
	@Override
	protected AnimatableValue[] parseValues() {
		String valuesString = element.getAttributeNS(null, SVG_VALUES_ATTRIBUTE);
		int len = valuesString.length();
		if (len == 0) {
			return null;
		}
		return parseValues(valuesString);
	}

	protected AnimatableValue[] parseValues(String s) {
		try {
			LengthPairListParser lplp = new LengthPairListParser();
			LengthArrayProducer lap = new LengthArrayProducer();
			lplp.setLengthListHandler(lap);
			lplp.parse(s);
			short[] types = lap.getLengthTypeArray();
			float[] values = lap.getLengthValueArray();
			AnimatableValue[] ret = new AnimatableValue[types.length / 2];
			for (int i = 0; i < types.length; i += 2) {
				float x = animationTarget.svgToUserSpace(values[i], types[i],
						AnimationTarget.PERCENTAGE_VIEWPORT_WIDTH);
				float y = animationTarget.svgToUserSpace(values[i + 1], types[i + 1],
						AnimationTarget.PERCENTAGE_VIEWPORT_HEIGHT);
				ret[i / 2] = new AnimatableMotionPointValue(animationTarget, x, y, 0);
			}
			return ret;
		} catch (ParseException pEx) {
			throw new BridgeException(ctx, element, pEx, ErrorConstants.ERR_ATTRIBUTE_VALUE_MALFORMED,
					new Object[] { SVG_VALUES_ATTRIBUTE, s });
		}
	}

	/**
	 * Parses a single comma-separated length pair.
	 */
	protected AnimatableValue parseLengthPair(String ln) {
		String s = element.getAttributeNS(null, ln);
		if (s.length() == 0) {
			return null;
		}
		return parseValues(s)[0];
	}

	// AnimatableElement /////////////////////////////////////////////////////

	/**
	 * Returns the underlying value of the animated attribute. Used for composition
	 * of additive animations.
	 */
	@Override
	public AnimatableValue getUnderlyingValue() {
		return new AnimatableMotionPointValue(animationTarget, 0f, 0f, 0f);
	}

	/**
	 * Parses the animation element's target attributes and adds it to the
	 * document's AnimationEngine.
	 */
	@Override
	protected void initializeAnimation() {
		// Determine the target element.
		String uri = XLinkSupport.getXLinkHref(element);
		Node t;
		if (uri.length() == 0) {
			t = element.getParentNode();
		} else {
			t = ctx.getReferencedElement(element, uri);
			if (t == null || t.getOwnerDocument() != element.getOwnerDocument()) {
				throw new BridgeException(ctx, element, ErrorConstants.ERR_URI_BAD_TARGET, new Object[] { uri });
			}
		}
		animationTarget = null;
		if (t instanceof SVGOMElement) {
			targetElement = (SVGOMElement) t;
			animationTarget = targetElement;
		}
		if (animationTarget == null) {
			throw new BridgeException(ctx, element, ErrorConstants.ERR_URI_BAD_TARGET, new Object[] { uri });
		}

		// Add the animation.
		timedElement = createTimedElement();
		animation = createAnimation(animationTarget);
		eng.addAnimation(animationTarget, AnimationEngine.ANIM_TYPE_OTHER, attributeNamespaceURI, attributeLocalName,
				animation);
	}
}
