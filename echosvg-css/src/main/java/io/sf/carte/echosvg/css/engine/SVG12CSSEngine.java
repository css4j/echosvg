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
package io.sf.carte.echosvg.css.engine;

import org.w3c.dom.Document;

import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.css.engine.value.svg.OpacityManager;
import io.sf.carte.echosvg.css.engine.value.svg.SVGColorManager;
import io.sf.carte.echosvg.css.engine.value.svg12.MarginLengthManager;
import io.sf.carte.echosvg.css.engine.value.svg12.TextAlignManager;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVG12CSSConstants;

/**
 * This class provides a CSS engine initialized for SVG 1.2.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVG12CSSEngine extends SVGCSSEngine {

	/**
	 * Creates a new SVG12CSSEngine.
	 * 
	 * @param doc The associated document.
	 * @param uri The document URI.
	 * @param p   The CSS parser to use.
	 * @param ctx The CSS context.
	 */
	public SVG12CSSEngine(Document doc, ParsedURL uri, Parser p, CSSContext ctx) {
		super(doc, uri, p, SVG_VALUE_MANAGERS, SVG_SHORTHAND_MANAGERS, ctx);
	}

	/**
	 * Creates a new SVG12CSSEngine.
	 * 
	 * @param doc The associated document.
	 * @param uri The document URI.
	 * @param p   The CSS parser to use.
	 * @param vms Extension value managers.
	 * @param sms Extension shorthand managers.
	 * @param ctx The CSS context.
	 */
	public SVG12CSSEngine(Document doc, ParsedURL uri, Parser p, ValueManager[] vms, ShorthandManager[] sms,
			CSSContext ctx) {
		super(doc, uri, p, mergeArrays(SVG_VALUE_MANAGERS, vms), mergeArrays(SVG_SHORTHAND_MANAGERS, sms), ctx);
	}

	/**
	 * The value managers for SVG.
	 */
	public static final ValueManager[] SVG_VALUE_MANAGERS = {
			new MarginLengthManager(SVG12CSSConstants.CSS_INDENT_PROPERTY),
			new SVGColorManager(SVG12CSSConstants.CSS_SOLID_COLOR_PROPERTY),
			new OpacityManager(SVG12CSSConstants.CSS_SOLID_OPACITY_PROPERTY, true), new TextAlignManager(), };

	/**
	 * The shorthand managers for SVG.
	 */
	public static final ShorthandManager[] SVG_SHORTHAND_MANAGERS = { };

	//
	// The property indexes.
	//
	public static final int INDENT_INDEX = SVGCSSEngine.FINAL_INDEX + 1;
	public static final int SOLID_COLOR_INDEX = INDENT_INDEX + 1;
	public static final int SOLID_OPACITY_INDEX = SOLID_COLOR_INDEX + 1;
	public static final int TEXT_ALIGN_INDEX = SOLID_OPACITY_INDEX + 1;
	public static final int FINAL_INDEX = TEXT_ALIGN_INDEX;

}
