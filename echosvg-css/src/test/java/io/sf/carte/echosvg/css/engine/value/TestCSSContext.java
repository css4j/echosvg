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

package io.sf.carte.echosvg.css.engine.value;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.echosvg.css.Viewport;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.util.ParsedURL;

class TestCSSContext implements CSSContext {

	private CSSEngine engine;

	public TestCSSContext(Document doc, ParsedURL purl, Parser cssParser) {
		this.engine = new SVGCSSEngine(doc, purl, cssParser, this) {

			@Override
			public ParsedURL getCSSBaseURI() {
				return purl;
			}

		};
	}

	@Override
	public Value getSystemColor(String ident) {
		return null;
	}

	@Override
	public Value getDefaultFontFamily() {
		return null;
	}

	@Override
	public float getLighterFontWeight(float f) {
		return 0;
	}

	@Override
	public float getBolderFontWeight(float f) {
		return f * 1.2f;
	}

	@Override
	public float getResolution() {
		return 96f;
	}

	@Override
	public float getMediumFontSize() {
		return 12f;
	}

	@Override
	public float getBlockWidth(Element elt) {
		return 100;
	}

	@Override
	public float getBlockHeight(Element elt) {
		return 50;
	}

	@Override
	public Viewport getViewport(Element e) {
		return new Viewport() {

			@Override
			public float getWidth() {
				return 400f;
			}

			@Override
			public float getHeight() {
				return 300f;
			}

		};
	}

	@Override
	public void checkLoadExternalResource(ParsedURL resourceURL, ParsedURL docURL) throws SecurityException {
	}

	@Override
	public String getPrefersColorScheme() {
		return "light";
	}

	@Override
	public boolean isDynamic() {
		return false;
	}

	@Override
	public boolean isInteractive() {
		return false;
	}

	@Override
	public CSSEngine getCSSEngineForElement(Element e) {
		return engine;
	}

}
