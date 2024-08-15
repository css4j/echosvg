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
package io.sf.carte.echosvg.css.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGPaint;

import io.sf.carte.echosvg.css.engine.value.AbstractValueModificationHandler;
import io.sf.carte.echosvg.css.engine.value.ColorValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueModificationHandler;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This class implements the {@link SVGPaint} interface.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
@SuppressWarnings("deprecation")
public class CSSOMSVGPaint extends CSSOMSVGColor {

	/**
	 * Creates a new CSSOMSVGPaint.
	 */
	public CSSOMSVGPaint(ValueProvider vp) {
		super(vp);
	}

	/**
	 * Sets the modification handler of this value.
	 */
	@Override
	public void setModificationHandler(ValueModificationHandler h) {
		if (!(h instanceof PaintModificationHandler)) {
			throw new IllegalArgumentException();
		}
		super.setModificationHandler(h);
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGColor#getColorType()}.
	 */
	@Override
	public short getColorType() {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGPaint#getPaintType()}.
	 */
	public short getPaintType() {
		Value value = valueProvider.getValue();
		switch (value.getCssValueType()) {
		case TYPED:
			switch (value.getPrimitiveType()) {
			case IDENT:
				String str = value.getIdentifierValue();
				if (str.equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
					return SVGPaint.SVG_PAINTTYPE_NONE;
				} else if (str.equalsIgnoreCase(CSSConstants.CSS_CURRENTCOLOR_VALUE)) {
					return SVGPaint.SVG_PAINTTYPE_CURRENTCOLOR;
				}
				return SVGPaint.SVG_PAINTTYPE_RGBCOLOR;
			case COLOR:
				ColorValue color = value.getColorValue();
				switch (color.getCSSColorSpace()) {
				case ColorValue.RGB_FUNCTION:
					return SVGPaint.SVG_PAINTTYPE_RGBCOLOR;
				default:
					break;
				}
				break;
			case URI:
				return SVGPaint.SVG_PAINTTYPE_URI;
			default:
				break;
			}
			break;

		case LIST:
			Value v0 = value.item(0);
			Value v1 = value.item(1);
			switch (v0.getPrimitiveType()) {
			case IDENT:
				return SVGPaint.SVG_PAINTTYPE_RGBCOLOR_ICCCOLOR;
			case URI:
				if (v1.getCssValueType() == Value.CssType.LIST)
					// FIXME: Should probably check this more deeply...
					return SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR_ICCCOLOR;

				switch (v1.getPrimitiveType()) {
				case IDENT:
					String str = v1.getIdentifierValue();
					if (str.equalsIgnoreCase(CSSConstants.CSS_NONE_VALUE)) {
						return SVGPaint.SVG_PAINTTYPE_URI_NONE;
					} else if (str.equalsIgnoreCase(CSSConstants.CSS_CURRENTCOLOR_VALUE)) {
						return SVGPaint.SVG_PAINTTYPE_URI_CURRENTCOLOR;
					}
					return SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR;
				case COLOR:
					ColorValue color = v1.getColorValue();
					switch (color.getCSSColorSpace()) {
					case ColorValue.RGB_FUNCTION:
						return SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR;
					default:
						break;
					}
				default:
					break;
				}

				break;
			case COLOR:
				ColorValue color = v0.getColorValue();
				switch (color.getCSSColorSpace()) {
				case ColorValue.RGB_FUNCTION:
					return SVGPaint.SVG_PAINTTYPE_RGBCOLOR_ICCCOLOR;
				default:
					break;
				}
			default:
				break;
			}
		default:
			break;

		}

		return SVGPaint.SVG_PAINTTYPE_UNKNOWN;
	}

	@Override
	public String getURIValue() throws DOMException {
		return getUri();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGPaint#getUri()}.
	 */
	public String getUri() {
		switch (getPaintType()) {
		case SVGPaint.SVG_PAINTTYPE_URI:
			return valueProvider.getValue().getURIValue();

		case SVGPaint.SVG_PAINTTYPE_URI_NONE:
		case SVGPaint.SVG_PAINTTYPE_URI_CURRENTCOLOR:
		case SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR:
		case SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR_ICCCOLOR:
			return valueProvider.getValue().item(0).getStringValue();
		}
		throw new InternalError();
	}

	/**
	 * <b>DOM</b>: Implements {@link org.w3c.dom.svg.SVGPaint#setUri(String)}.
	 */
	public void setUri(String uri) {
		if (handler == null) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		} else {
			((PaintModificationHandler) handler).uriValueChanged(uri);
		}
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.svg.SVGPaint#setPaint(short,String,String,String)}.
	 */
	public void setPaint(short paintType, String uri, String rgbColor, String iccColor) {
		if (handler == null) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "");
		} else {
			((PaintModificationHandler) handler).paintChanged(paintType, uri, rgbColor, iccColor);
		}
	}

	/**
	 * To manage the modifications on a SVGPaint value.
	 */
	public interface PaintModificationHandler extends ValueModificationHandler {

		/**
		 * Called when the URI has been modified.
		 */
		void uriValueChanged(String uri);

		/**
		 * Called when the paint value has beem modified.
		 */
		void paintChanged(short type, String uri, String rgb, String icc);

	}

	/**
	 * Provides an abstract implementation of a PaintModificationHandler.
	 */
	public abstract class AbstractModificationHandler extends AbstractValueModificationHandler
			implements PaintModificationHandler {

		/**
		 * Called when the URI has been modified.
		 */
		@Override
		public void uriValueChanged(String uri) {
			setPropertyText("url(" + uri + ") none");
		}

		/**
		 * Called when the paint value has beem modified.
		 */
		@Override
		public void paintChanged(short type, String uri, String rgb, String icc) {
			switch (type) {
			case SVGPaint.SVG_PAINTTYPE_NONE:
				setPropertyText("none");
				break;

			case SVGPaint.SVG_PAINTTYPE_CURRENTCOLOR:
				setPropertyText("currentcolor");
				break;

			case SVGPaint.SVG_PAINTTYPE_RGBCOLOR:
				setPropertyText(rgb);
				break;

			case SVGPaint.SVG_PAINTTYPE_RGBCOLOR_ICCCOLOR:
				setPropertyText(rgb + ' ' + icc);
				break;

			case SVGPaint.SVG_PAINTTYPE_URI:
				setPropertyText("url(" + uri + ')');
				break;

			case SVGPaint.SVG_PAINTTYPE_URI_NONE:
				setPropertyText("url(" + uri + ") none");
				break;

			case SVGPaint.SVG_PAINTTYPE_URI_CURRENTCOLOR:
				setPropertyText("url(" + uri + ") currentcolor");
				break;

			case SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR:
				setPropertyText("url(" + uri + ") " + rgb);
				break;

			case SVGPaint.SVG_PAINTTYPE_URI_RGBCOLOR_ICCCOLOR:
				setPropertyText("url(" + uri + ") " + rgb + ' ' + icc);
			}
		}

	}

}
