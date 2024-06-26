/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.sf.carte.echosvg.css.engine;

import org.w3c.dom.Element;

import io.sf.carte.doc.style.css.SelectorMatcher;
import io.sf.carte.doc.style.css.om.BaseSelectorMatcher;

/**
 * Used for CSS selector matching.
 */
public class SVGSelectorMatcher extends BaseSelectorMatcher<Element> {

	private static final long serialVersionUID = 2L;

	/**
	 * Construct a new matcher.
	 * 
	 * @param elt the element to check for matches.
	 */
	public SVGSelectorMatcher(Element elt) {
		super(elt);
	}

	@Override
	protected SelectorMatcher obtainSelectorMatcher(Element element) {
		return new SVGSelectorMatcher(element);
	}

	@Override
	protected boolean isVisitedURI(String href) {
		return false;
	}

}
