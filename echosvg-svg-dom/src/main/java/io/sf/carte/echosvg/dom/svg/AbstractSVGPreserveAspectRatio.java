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
package io.sf.carte.echosvg.dom.svg;

import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

import io.sf.carte.echosvg.parser.DefaultPreserveAspectRatioHandler;
import io.sf.carte.echosvg.parser.ParseException;
import io.sf.carte.echosvg.parser.PreserveAspectRatioParser;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Abstract implementation for SVGPreservAspectRatio
 *
 * This is the base implementation for SVGPreservAspectRatio
 *
 * @author Tonny Kohar
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class AbstractSVGPreserveAspectRatio implements SVGPreserveAspectRatio, SVGConstants {

	/**
	 * Strings for the 'align' values.
	 */
	protected static final String[] ALIGN_VALUES = { null, SVG_NONE_VALUE, SVG_XMINYMIN_VALUE, SVG_XMIDYMIN_VALUE,
			SVG_XMAXYMIN_VALUE, SVG_XMINYMID_VALUE, SVG_XMIDYMID_VALUE, SVG_XMAXYMID_VALUE, SVG_XMINYMAX_VALUE,
			SVG_XMIDYMAX_VALUE, SVG_XMAXYMAX_VALUE };

	/**
	 * Strings for the 'meet-or-slice' values.
	 */
	protected static final String[] MEET_OR_SLICE_VALUES = { null, SVG_MEET_VALUE, SVG_SLICE_VALUE };

	/**
	 * Returns a string representation of a preserve aspect ratio value specified
	 * numerically.
	 * 
	 * @param align       the align value, one of the
	 *                    SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_* constants
	 * @param meetOrSlice the meet-or-slice value, one of the
	 *                    SVGPreserveAspectRatio.SVG_MEETORSLICE_* constants
	 */
	public static String getValueAsString(short align, short meetOrSlice) {
		if (align < 1 || align > 10) {
			return null;
		}
		String value = ALIGN_VALUES[align];
		if (align == SVG_PRESERVEASPECTRATIO_NONE) {
			return value;
		}
		if (meetOrSlice < 1 || meetOrSlice > 2) {
			return null;
		}
		return value + ' ' + MEET_OR_SLICE_VALUES[meetOrSlice];
	}

	/**
	 * align property by default the value is
	 * SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID
	 */
	protected short align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;

	/**
	 * meetOrSlice property by default the value is
	 * SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
	 */
	protected short meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;

	/** Creates a new instance of AbstractSVGPreserveAspectRatio */
	public AbstractSVGPreserveAspectRatio() {
	}

	@Override
	public short getAlign() {
		return this.align;
	}

	@Override
	public short getMeetOrSlice() {
		return this.meetOrSlice;
	}

	@Override
	public void setAlign(short align) {
		this.align = align;
		setAttributeValue(getValueAsString());
	}

	@Override
	public void setMeetOrSlice(short meetOrSlice) {
		this.meetOrSlice = meetOrSlice;
		setAttributeValue(getValueAsString());
	}

	public void reset() {
		align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;
		meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
		// setAttributeValue(getValueAsString());
	}

	protected abstract void setAttributeValue(String value) throws DOMException;

	protected abstract DOMException createDOMException(short type, String key, Object[] args);

	protected void setValueAsString(String value) throws DOMException {
		PreserveAspectRatioParserHandler ph = new PreserveAspectRatioParserHandler();
		PreserveAspectRatioParser p = new PreserveAspectRatioParser(ph);
		try {
			p.parse(value);
			align = ph.getAlign();
			meetOrSlice = ph.getMeetOrSlice();
		} catch (ParseException ex) {
			throw createDOMException(DOMException.INVALID_MODIFICATION_ERR, "preserve.aspect.ratio",
					new Object[] { value });
		}
	}

	/**
	 * Returns the string representation of the preserve aspect ratio value.
	 */
	public String getValueAsString() {
		if (align < 1 || align > 10) {
			throw createDOMException(DOMException.INVALID_MODIFICATION_ERR, "preserve.aspect.ratio.align",
					new Object[] { (int) align });
		}
		String value = ALIGN_VALUES[align];
		if (align == SVG_PRESERVEASPECTRATIO_NONE) {
			return value;
		}

		if (meetOrSlice < 1 || meetOrSlice > 2) {
			throw createDOMException(DOMException.INVALID_MODIFICATION_ERR, "preserve.aspect.ratio.meet.or.slice",
					new Object[] { (int) meetOrSlice });
		}
		return value + ' ' + MEET_OR_SLICE_VALUES[meetOrSlice];
	}

	protected static class PreserveAspectRatioParserHandler extends DefaultPreserveAspectRatioHandler {

		public short align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;
		public short meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;

		public short getAlign() {
			return align;
		}

		public short getMeetOrSlice() {
			return meetOrSlice;
		}

		/**
		 * Invoked when 'none' been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void none() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE;
		}

		/**
		 * Invoked when 'xMaxYMax' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMaxYMax() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMAX;
		}

		/**
		 * Invoked when 'xMaxYMid' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMaxYMid() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMID;
		}

		/**
		 * Invoked when 'xMaxYMin' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMaxYMin() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMIN;
		}

		/**
		 * Invoked when 'xMidYMax' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMidYMax() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMAX;
		}

		/**
		 * Invoked when 'xMidYMid' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMidYMid() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;
		}

		/**
		 * Invoked when 'xMidYMin' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMidYMin() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMIN;
		}

		/**
		 * Invoked when 'xMinYMax' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMinYMax() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMAX;
		}

		/**
		 * Invoked when 'xMinYMid' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMinYMid() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMID;
		}

		/**
		 * Invoked when 'xMinYMin' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void xMinYMin() throws ParseException {
			align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMIN;
		}

		/**
		 * Invoked when 'meet' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void meet() throws ParseException {
			meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
		}

		/**
		 * Invoked when 'slice' has been parsed.
		 * 
		 * @exception ParseException if an error occured while processing the transform
		 */
		@Override
		public void slice() throws ParseException {
			meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_SLICE;
		}

	}

}
