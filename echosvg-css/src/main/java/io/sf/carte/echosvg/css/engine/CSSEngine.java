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

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.w3c.css.om.unit.CSSUnit;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

import io.sf.carte.doc.style.css.BooleanCondition;
import io.sf.carte.doc.style.css.CSSCanvas;
import io.sf.carte.doc.style.css.CSSDocument;
import io.sf.carte.doc.style.css.CSSRule;
import io.sf.carte.doc.style.css.CSSTypedValue;
import io.sf.carte.doc.style.css.CSSValue;
import io.sf.carte.doc.style.css.CSSValue.Type;
import io.sf.carte.doc.style.css.CSSValueSyntax;
import io.sf.carte.doc.style.css.CSSValueSyntax.Match;
import io.sf.carte.doc.style.css.MediaQueryList;
import io.sf.carte.doc.style.css.SelectorMatcher;
import io.sf.carte.doc.style.css.StyleDatabase;
import io.sf.carte.doc.style.css.UnitStringToId;
import io.sf.carte.doc.style.css.nsac.ArgumentCondition;
import io.sf.carte.doc.style.css.nsac.AttributeCondition;
import io.sf.carte.doc.style.css.nsac.CSSBudgetException;
import io.sf.carte.doc.style.css.nsac.CSSHandler;
import io.sf.carte.doc.style.css.nsac.CSSParseException;
import io.sf.carte.doc.style.css.nsac.CombinatorCondition;
import io.sf.carte.doc.style.css.nsac.CombinatorSelector;
import io.sf.carte.doc.style.css.nsac.Condition;
import io.sf.carte.doc.style.css.nsac.ConditionalSelector;
import io.sf.carte.doc.style.css.nsac.DeclarationCondition;
import io.sf.carte.doc.style.css.nsac.InputSource;
import io.sf.carte.doc.style.css.nsac.LexicalUnit;
import io.sf.carte.doc.style.css.nsac.LexicalUnit.LexicalType;
import io.sf.carte.doc.style.css.nsac.PageSelectorList;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.doc.style.css.nsac.ParserControl;
import io.sf.carte.doc.style.css.nsac.Selector;
import io.sf.carte.doc.style.css.nsac.SelectorFunction;
import io.sf.carte.doc.style.css.nsac.SelectorList;
import io.sf.carte.doc.style.css.om.AbstractCSSCanvas;
import io.sf.carte.doc.style.css.om.AbstractStyleDatabase;
import io.sf.carte.doc.style.css.om.CSSOMParser;
import io.sf.carte.doc.style.css.om.Specificity;
import io.sf.carte.doc.style.css.parser.AttributeConditionVisitor;
import io.sf.carte.doc.style.css.parser.ParseHelper;
import io.sf.carte.doc.style.css.parser.SyntaxParser;
import io.sf.carte.doc.style.css.property.ValueFactory;
import io.sf.carte.echosvg.css.CSSSecurityException;
import io.sf.carte.echosvg.css.engine.value.CSSProxyValueException;
import io.sf.carte.echosvg.css.engine.value.ComputedValue;
import io.sf.carte.echosvg.css.engine.value.InheritValue;
import io.sf.carte.echosvg.css.engine.value.LexicalValue;
import io.sf.carte.echosvg.css.engine.value.PendingValue;
import io.sf.carte.echosvg.css.engine.value.PropertyDefinition;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.util.CSSConstants;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This is the base class for all the CSS engines.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class CSSEngine {

	/**
	 * Returns the CSS parent node of the given node.
	 */
	public static Node getCSSParentNode(Node n) {
		if (n instanceof CSSNavigableNode) {
			return ((CSSNavigableNode) n).getCSSParentNode();
		}
		return n.getParentNode();
	}

	/**
	 * Returns the CSS first child node of the given node.
	 */
	protected static Node getCSSFirstChild(Node n) {
		if (n instanceof CSSNavigableNode) {
			return ((CSSNavigableNode) n).getCSSFirstChild();
		}
		return n.getFirstChild();
	}

	/**
	 * Returns the CSS next sibling node of the given node.
	 */
	protected static Node getCSSNextSibling(Node n) {
		if (n instanceof CSSNavigableNode) {
			return ((CSSNavigableNode) n).getCSSNextSibling();
		}
		return n.getNextSibling();
	}

	/**
	 * Returns the CSS previous sibling node of the given node.
	 */
	protected static Node getCSSPreviousSibling(Node n) {
		if (n instanceof CSSNavigableNode) {
			return ((CSSNavigableNode) n).getCSSPreviousSibling();
		}
		return n.getPreviousSibling();
	}

	/**
	 * Returns the next stylable parent of the given element.
	 */
	public static CSSStylableElement getParentCSSStylableElement(Element elt) {
		Node n = getCSSParentNode(elt);
		while (n != null) {
			if (n instanceof CSSStylableElement) {
				return (CSSStylableElement) n;
			}
			n = getCSSParentNode(n);
		}
		return null;
	}

	/**
	 * The user agent used for showing error messages.
	 */
	protected CSSEngineUserAgent userAgent;

	/**
	 * The CSS context.
	 */
	protected CSSContext cssContext;

	private EngineStyleDatabase styleDb;

	private CSSCanvas csscanvas;

	/**
	 * The associated document.
	 */
	protected Document document;

	/**
	 * The document URI.
	 */
	protected ParsedURL documentURI;

	/**
	 * Whether the document is a CSSNavigableDocument.
	 */
	protected boolean isCSSNavigableDocument;

	/**
	 * The property/int mappings.
	 */
	protected StringIntMap indexes;

	/**
	 * The shorthand-property/int mappings.
	 */
	protected StringIntMap shorthandIndexes;

	/**
	 * The value managers.
	 */
	protected ValueManager[] valueManagers;

	/**
	 * The shorthand managers.
	 */
	protected ShorthandManager[] shorthandManagers;

	/**
	 * The CSS parser.
	 */
	protected Parser parser;

	/**
	 * The pseudo-element names.
	 */
	protected String[] pseudoElementNames;

	/**
	 * The font-size property index.
	 */
	protected int fontSizeIndex = -1;

	/**
	 * The line-height property index.
	 */
	protected int lineHeightIndex = -1;

	/**
	 * The color property index.
	 */
	protected int colorIndex = -1;

	/**
	 * The user-agent style-sheet.
	 */
	protected StyleSheet userAgentStyleSheet;

	/**
	 * The user style-sheet.
	 */
	protected StyleSheet userStyleSheet;

	/**
	 * The media to use to cascade properties.
	 */
	private String medium;

	/**
	 * The DOM nodes which contains StyleSheets.
	 */
	protected List<Node> styleSheetNodes;

	/**
	 * List of StyleMap objects, one for each @font-face rule encountered by this
	 * CSSEngine.
	 */
	protected List<FontFaceRule> fontFaces = new LinkedList<>();

	/**
	 * The style attribute namespace URI.
	 */
	protected String styleNamespaceURI;

	/**
	 * The style attribute local name.
	 */
	protected String styleLocalName;

	/**
	 * The class attribute namespace URI.
	 */
	protected String classNamespaceURI;

	/**
	 * The class attribute local name.
	 */
	protected String classLocalName;

	/**
	 * The non CSS presentational hints.
	 */
	protected Set<String> nonCSSPresentationalHints;

	/**
	 * The non CSS presentational hints namespace URI.
	 */
	protected String nonCSSPresentationalHintsNamespaceURI;

	/**
	 * The style declaration document handler.
	 */
	protected StyleDeclarationDocumentHandler styleDeclarationDocumentHandler = new StyleDeclarationDocumentHandler();

	/**
	 * The style declaration update handler.
	 */
	protected StyleDeclarationUpdateHandler styleDeclarationUpdateHandler;

	/**
	 * The style sheet document handler.
	 */
	protected StyleSheetDocumentHandler styleSheetDocumentHandler = new StyleSheetDocumentHandler();

	/**
	 * The style declaration document handler used to build a StyleDeclaration
	 * object.
	 */
	protected StyleDeclarationBuilder styleDeclarationBuilder = new StyleDeclarationBuilder();

	/**
	 * The current element.
	 */
	protected CSSStylableElement element;

	/**
	 * The current base URI.
	 */
	protected ParsedURL cssBaseURI;

	/**
	 * The alternate stylesheet title.
	 */
	protected String alternateStyleSheet;

	/**
	 * Listener for CSSNavigableDocument events.
	 */
	protected CSSNavigableDocumentHandler cssNavigableDocumentListener;

	/**
	 * The DOMAttrModified event listener.
	 */
	protected EventListener domAttrModifiedListener;

	/**
	 * The DOMNodeInserted event listener.
	 */
	protected EventListener domNodeInsertedListener;

	/**
	 * The DOMNodeRemoved event listener.
	 */
	protected EventListener domNodeRemovedListener;

	/**
	 * The DOMSubtreeModified event listener.
	 */
	protected EventListener domSubtreeModifiedListener;

	/**
	 * The DOMCharacterDataModified event listener.
	 */
	protected EventListener domCharacterDataModifiedListener;

	/**
	 * Whether a style sheet as been removed from the document.
	 */
	protected boolean styleSheetRemoved;

	/**
	 * The right sibling of the last removed node.
	 */
	protected Node removedStylableElementSibling;

	/**
	 * The listeners.
	 */
	protected List<CSSEngineListener> listeners = Collections.synchronizedList(new LinkedList<>());

	/**
	 * The attributes found in stylesheets selectors.
	 */
	protected Set<String> selectorAttributes;

	/**
	 * The map from custom property names to their definitions.
	 */
	private HashMap<String, PropertyDefinition> propertyDefinitionMap = null;

	private static final int INITIAL_CUSTOM_PTY_SET_SIZE = 1; // Initial set is never used

	/**
	 * Used to fire a change event for all the properties.
	 */
	protected final int[] ALL_PROPERTIES;

	/**
	 * Creates a new CSSEngine.
	 * 
	 * @param doc     The associated document.
	 * @param uri     The document URI.
	 * @param p       The CSS parser.
	 * @param vm      The property value managers.
	 * @param sm      The shorthand properties managers.
	 * @param pe      The pseudo-element names supported by the associated XML
	 *                dialect. Must be null if no support for pseudo- elements is
	 *                required.
	 * @param sns     The namespace URI of the style attribute.
	 * @param sln     The local name of the style attribute.
	 * @param cns     The namespace URI of the class attribute.
	 * @param cln     The local name of the class attribute.
	 * @param hints   Whether the CSS engine should support non CSS presentational
	 *                hints.
	 * @param hintsNS The hints namespace URI.
	 * @param ctx     The CSS context.
	 */
	protected CSSEngine(Document doc, ParsedURL uri, Parser p, ValueManager[] vm, ShorthandManager[] sm, String[] pe,
			String sns, String sln, String cns, String cln, boolean hints, String hintsNS, CSSContext ctx) {
		document = doc;
		documentURI = uri;
		parser = p;
		pseudoElementNames = pe;
		styleNamespaceURI = sns;
		styleLocalName = sln;
		classNamespaceURI = cns;
		classLocalName = cln;
		cssContext = ctx;
		styleDb = new EngineStyleDatabase();
		csscanvas = new EngineCSSCanvas();

		isCSSNavigableDocument = doc instanceof CSSNavigableDocument;

		int len = vm.length;
		indexes = new StringIntMap(len);
		valueManagers = vm;

		for (int i = len - 1; i >= 0; --i) {
			String pn = vm[i].getPropertyName();
			indexes.put(pn, i);
			if (fontSizeIndex == -1 && pn.equals(CSSConstants.CSS_FONT_SIZE_PROPERTY)) {
				fontSizeIndex = i;
			}
			if (lineHeightIndex == -1 && pn.equals(CSSConstants.CSS_LINE_HEIGHT_PROPERTY)) {
				lineHeightIndex = i;
			}
			if (colorIndex == -1 && pn.equals(CSSConstants.CSS_COLOR_PROPERTY)) {
				colorIndex = i;
			}
		}

		len = sm.length;
		shorthandIndexes = new StringIntMap(len);
		shorthandManagers = sm;
		for (int i = len - 1; i >= 0; --i) {
			shorthandIndexes.put(sm[i].getPropertyName(), i);
		}

		if (hints) {
			nonCSSPresentationalHints = new HashSet<>(vm.length + sm.length);
			nonCSSPresentationalHintsNamespaceURI = hintsNS;
			len = vm.length;
			for (int i = 0; i < len; i++) {
				String pn = vm[i].getPropertyName();
				nonCSSPresentationalHints.add(pn);
			}
			len = sm.length;
			for (int i = 0; i < len; i++) {
				String pn = sm[i].getPropertyName();
				nonCSSPresentationalHints.add(pn);
			}
		}

		if (cssContext.isDynamic() && document instanceof EventTarget) {
			// Attach the mutation events listeners.
			addEventListeners((EventTarget) document);
			styleDeclarationUpdateHandler = new StyleDeclarationUpdateHandler();
		}

		ALL_PROPERTIES = new int[getNumberOfProperties()];
		for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
			ALL_PROPERTIES[i] = i;
		}
	}

	private class EngineStyleDatabase extends AbstractStyleDatabase {

		private static final long serialVersionUID = 1L;

		private final List<String> fonts = getAvailableFontList();

		private List<String> getAvailableFontList() {
			return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames());
		}

		@Override
		public String getDefaultGenericFontFamily() {
			return cssContext.getDefaultFontFamily().getStringValue();
		}

		@Override
		public String getDefaultGenericFontFamily(String genericFamily) {
			return genericFamily;
		}

		@Override
		public boolean isFontFaceName(String requestedFamily) {
			for (FontFaceRule ffRule : fontFaces) {
				StyleMap sm = ffRule.getStyleMap();
				int pidx = getPropertyIndex(CSSConstants.CSS_FONT_FAMILY_PROPERTY);
				Value fontFamily = sm.getValue(pidx);
				if (fontFamily != null && fontFamily.getStringValue().equalsIgnoreCase(requestedFamily)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public int getColorDepth() {
			// We do not have the actual Graphics2D here, but we try
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			java.awt.GraphicsConfiguration gConfiguration = genv.getDefaultScreenDevice()
					.getDefaultConfiguration();
			int bpc = 255;
			if (gConfiguration != null) {
				int[] comp = gConfiguration.getColorModel().getComponentSize();
				for (int i = 0; i < 3; i++) {
					if (bpc > comp[i]) {
						bpc = comp[i];
					}
				}
			}
			return bpc;
		}

		@Override
		public float getDeviceHeight() {
			return cssContext.getViewport(element).getHeight();
		}

		@Override
		public float getDeviceWidth() {
			return cssContext.getViewport(element).getWidth();
		}

		@Override
		protected boolean isFontFamilyAvailable(String fontFamily) {
			return fonts.contains(fontFamily);
		}

		@Override
		public CSSTypedValue getInitialColor() {
			String pcs = cssContext.getPrefersColorScheme();
			return "dark".equals(pcs) ?
					darkmodeInitialColor() : super.getInitialColor();
		}

		private CSSTypedValue darkmodeInitialColor() {
			return (CSSTypedValue) new ValueFactory().parseProperty("#fff");
		}

		@Override
		public boolean supports(SelectorList selectors) {
			for (Selector selector : selectors) {
				if (!supports(selector)) {
					return false;
				}
			}
			return true;
		}

		private boolean supports(Selector selector) {
			if (selector != null) {
				switch (selector.getSelectorType()) {
				case CHILD:
				case DESCENDANT:
				case DIRECT_ADJACENT:
				case SUBSEQUENT_SIBLING:
					CombinatorSelector combSel = (CombinatorSelector) selector;
					return supports(combSel.getSelector())
							&& supports(combSel.getSecondSelector());
				case CONDITIONAL:
					ConditionalSelector condSel = (ConditionalSelector) selector;
					return supports(condSel.getSimpleSelector())
							&& supports(condSel.getCondition());
				case COLUMN_COMBINATOR:
					return false;
				default:
				}
			}
			return true;
		}

		private boolean supports(Condition condition) {
			switch (condition.getConditionType()) {
			case AND:
				CombinatorCondition combCond = (CombinatorCondition) condition;
				return supports(combCond.getFirstCondition())
						&& supports(combCond.getSecondCondition());
			case SELECTOR_ARGUMENT:
				ArgumentCondition argCond = (ArgumentCondition) condition;
				SelectorList selist = argCond.getSelectors();
				return selist == null || supports(selist);
			default:
				return true;
			}
		}

	}

	private class EngineCSSCanvas extends AbstractCSSCanvas {

		@Override
		public CSSDocument getDocument() {
			return null;
		}

		@Override
		public StyleDatabase getStyleDatabase() {
			return styleDb;
		}

		@Override
		protected String getOverflowBlock() {
			return "none";
		}

		@Override
		protected String getOverflowInline() {
			return "none";
		}

		@Override
		protected String getPointerAccuracy() {
			return "none";
		}

		/**
		 * The desire for light or dark color schemes.
		 * 
		 * @return the {@code prefers-color-scheme} feature
		 */
		@Override
		protected String getPrefersColorScheme() {
			return cssContext.getPrefersColorScheme();
		}

		@Override
		protected float getResolution() {
			return cssContext.getResolution();
		}

	}

	/**
	 * Adds event listeners to the document to track CSS changes.
	 */
	protected void addEventListeners(EventTarget doc) {
		if (isCSSNavigableDocument) {
			cssNavigableDocumentListener = new CSSNavigableDocumentHandler();
			CSSNavigableDocument cnd = (CSSNavigableDocument) doc;
			cnd.addCSSNavigableDocumentListener(cssNavigableDocumentListener);
		} else {
			domAttrModifiedListener = new DOMAttrModifiedListener();
			doc.addEventListener("DOMAttrModified", domAttrModifiedListener, false);
			domNodeInsertedListener = new DOMNodeInsertedListener();
			doc.addEventListener("DOMNodeInserted", domNodeInsertedListener, false);
			domNodeRemovedListener = new DOMNodeRemovedListener();
			doc.addEventListener("DOMNodeRemoved", domNodeRemovedListener, false);
			domSubtreeModifiedListener = new DOMSubtreeModifiedListener();
			doc.addEventListener("DOMSubtreeModified", domSubtreeModifiedListener, false);
			domCharacterDataModifiedListener = new DOMCharacterDataModifiedListener();
			doc.addEventListener("DOMCharacterDataModified", domCharacterDataModifiedListener, false);
		}
	}

	/**
	 * Removes the event listeners from the document.
	 */
	protected void removeEventListeners(EventTarget doc) {
		if (isCSSNavigableDocument) {
			CSSNavigableDocument cnd = (CSSNavigableDocument) doc;
			cnd.removeCSSNavigableDocumentListener(cssNavigableDocumentListener);
		} else {
			doc.removeEventListener("DOMAttrModified", domAttrModifiedListener, false);
			doc.removeEventListener("DOMNodeInserted", domNodeInsertedListener, false);
			doc.removeEventListener("DOMNodeRemoved", domNodeRemovedListener, false);
			doc.removeEventListener("DOMSubtreeModified", domSubtreeModifiedListener, false);
			doc.removeEventListener("DOMCharacterDataModified", domCharacterDataModifiedListener, false);
		}
	}

	/**
	 * Disposes the CSSEngine and all the attached resources.
	 */
	public void dispose() {
		setCSSEngineUserAgent(null);
		disposeStyleMaps(document.getDocumentElement());
		if (document instanceof EventTarget) {
			// Detach the mutation events listeners.
			removeEventListeners((EventTarget) document);
		}
	}

	/**
	 * Removes the style maps from each CSSStylableElement in the document.
	 */
	protected void disposeStyleMaps(Node node) {
		if (node instanceof CSSStylableElement) {
			((CSSStylableElement) node).setComputedStyleMap(null, null);
		}
		for (Node n = getCSSFirstChild(node); n != null; n = getCSSNextSibling(n)) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				disposeStyleMaps(n);
			}
		}
	}

	/**
	 * Returns the CSS context.
	 */
	public CSSContext getCSSContext() {
		return cssContext;
	}

	/**
	 * Returns the document associated with this engine.
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Returns the font-size property index.
	 */
	public int getFontSizeIndex() {
		return fontSizeIndex;
	}

	/**
	 * Returns the line-height property index.
	 */
	public int getLineHeightIndex() {
		return lineHeightIndex;
	}

	/**
	 * Returns the color property index.
	 */
	public int getColorIndex() {
		return colorIndex;
	}

	/**
	 * Returns the number of properties.
	 */
	public int getNumberOfProperties() {
		return valueManagers.length;
	}

	/**
	 * Returns the property index, or -1.
	 */
	public int getPropertyIndex(String name) {
		return indexes.get(name);
	}

	/**
	 * Returns the shorthand property index, or -1.
	 */
	public int getShorthandIndex(String name) {
		return shorthandIndexes.get(name);
	}

	/**
	 * Returns the name of the property at the given index.
	 */
	public String getPropertyName(int idx) {
		return valueManagers[idx].getPropertyName();
	}

	public void setCSSEngineUserAgent(CSSEngineUserAgent userAgent) {
		this.userAgent = userAgent;
	}

	public CSSEngineUserAgent getCSSEngineUserAgent() {
		return userAgent;
	}

	/**
	 * Sets the user agent style-sheet.
	 */
	public void setUserAgentStyleSheet(StyleSheet ss) {
		userAgentStyleSheet = ss;
	}

	/**
	 * Sets the user style-sheet.
	 */
	public void setUserStyleSheet(StyleSheet ss) {
		userStyleSheet = ss;
	}

	/**
	 * Returns the ValueManagers.
	 */
	public ValueManager[] getValueManagers() {
		return valueManagers;
	}

	/**
	 * Returns the ShorthandManagers.
	 */
	public ShorthandManager[] getShorthandManagers() {
		return shorthandManagers;
	}

	/**
	 * Gets the StyleMaps generated by @font-face rules encountered by this
	 * CSSEngine thus far.
	 */
	public List<FontFaceRule> getFontFaces() {
		return fontFaces;
	}

	/**
	 * Sets the media to use to compute the styles.
	 */
	public void setMedia(String str) {
		medium = str != null ? str.toLowerCase(Locale.ROOT) : null;
	}

	/**
	 * Sets the alternate style-sheet title.
	 */
	public void setAlternateStyleSheet(String str) {
		alternateStyleSheet = str;
	}

	/**
	 * Recursively imports the cascaded style from a source element to an element of
	 * the current document.
	 */
	public void importCascadedStyleMaps(Element src, CSSEngine srceng, Element dest) {
		if (src instanceof CSSStylableElement) {
			CSSStylableElement csrc = (CSSStylableElement) src;
			CSSStylableElement cdest = (CSSStylableElement) dest;

			StyleMap sm = srceng.getCascadedStyleMap(csrc, null);
			sm.setFixedCascadedStyle(true);
			cdest.setComputedStyleMap(null, sm);

			if (pseudoElementNames != null) {
				for (String pe : pseudoElementNames) {
					sm = srceng.getCascadedStyleMap(csrc, pe);
					cdest.setComputedStyleMap(pe, sm);
				}
			}
		}

		for (Node dn = getCSSFirstChild(dest),
				sn = getCSSFirstChild(src); dn != null; dn = getCSSNextSibling(dn), sn = getCSSNextSibling(sn)) {
			if (sn.getNodeType() == Node.ELEMENT_NODE) {
				importCascadedStyleMaps((Element) sn, srceng, (Element) dn);
			}
		}
	}

	/**
	 * Returns the current base-url.
	 */
	public ParsedURL getCSSBaseURI() {
		if (cssBaseURI == null) {
			cssBaseURI = element.getCSSBase();
		}
		return cssBaseURI;
	}

	/**
	 * Returns the cascaded style of the given element/pseudo-element.
	 * 
	 * @param elt    The stylable element.
	 * @param pseudo Optional pseudo-element string (null if none).
	 */
	public StyleMap getCascadedStyleMap(CSSStylableElement elt, String pseudo) {
		int props = getNumberOfProperties();
		final StyleMap result = new StyleMap(props);

		SelectorMatcher matcher = new SVGSelectorMatcher(elt);
		if (pseudo != null) {
			Parser parser = createCSSParser();
			Condition pseCond = parser.parsePseudoElement(pseudo);
			matcher.setPseudoElement(pseCond);
		}

		// Apply the user-agent style-sheet to the result.
		if (userAgentStyleSheet != null) {
			ArrayList<Rule> rules = new ArrayList<>();
			addMatchingRules(rules, userAgentStyleSheet, matcher);
			addRules(matcher, result, rules, StyleMap.USER_AGENT_ORIGIN);
		}

		// Apply the user properties style-sheet to the result.
		if (userStyleSheet != null) {
			ArrayList<Rule> rules = new ArrayList<>();
			addMatchingRules(rules, userStyleSheet, matcher);
			addRules(matcher, result, rules, StyleMap.USER_ORIGIN);
		}

		element = elt;
		try {
			// Apply the non-CSS presentational hints to the result.
			if (nonCSSPresentationalHints != null) {
				ShorthandManager.PropertyHandler ph = new ShorthandManager.PropertyHandler() {
					@Override
					public void property(String pname, LexicalUnit lu, boolean important) {
						int idx = getPropertyIndex(pname);
						if (idx != -1) {
							ValueManager vm = valueManagers[idx];
							Value v;
							try {
								v = vm.createValue(lu, CSSEngine.this);
							} catch (CSSProxyValueException e) {
								v = new LexicalValue(lu);
							}
							putAuthorProperty(result, idx, v, important, StyleMap.NON_CSS_ORIGIN);
							return;
						}
						idx = getShorthandIndex(pname);
						if (idx == -1)
							return; // Unknown property...
						// Shorthand value
						shorthandManagers[idx].setValues(CSSEngine.this, this, lu, important);
					}

					@Override
					public void pendingValue(String pname, PendingValue v, boolean important) {
						int idx = getPropertyIndex(pname);
						if (idx != -1) { // line-height can be -1
							putAuthorProperty(result, idx, v, important, StyleMap.NON_CSS_ORIGIN);
						}
					}
				};

				NamedNodeMap attrs = elt.getAttributes();
				int len = attrs.getLength();
				for (int i = 0; i < len; i++) {
					Node attr = attrs.item(i);
					String an = attr.getNodeName();
					if (nonCSSPresentationalHints.contains(an)) {
						try {
							LexicalUnit lu;
							lu = parser.parsePropertyValue(new StringReader(attr.getNodeValue()));
							ph.property(an, lu, false);
						} catch (Exception e) {
							String m = e.getMessage();
							if (m == null)
								m = "";
							String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
							String s = Messages.formatMessage("property.syntax.error.at",
									new Object[] { u, an, attr.getNodeValue(), m });
							DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
							de.initCause(e);
							if (userAgent == null)
								throw de;
							userAgent.displayError(de);
						}
					}
				}
			}

			// Apply the document style-sheets to the result.
			CSSEngine eng = cssContext.getCSSEngineForElement(elt);
			List<Node> snodes = eng.getStyleSheetNodes();
			int slen = snodes.size();
			if (slen > 0) {
				ArrayList<Rule> rules = new ArrayList<>();
				for (Node snode : snodes) {
					CSSStyleSheetNode ssn = (CSSStyleSheetNode) snode;
					StyleSheet ss = ssn.getCSSStyleSheet();
					if (ss != null
							&& (!ss.isAlternate() || ss.getTitle() == null || ss.getTitle().equals(alternateStyleSheet))
							&& mediaMatch(ss.getMedia())) {
						addMatchingRules(rules, ss, matcher);
					}
				}
				addRules(matcher, result, rules, StyleMap.AUTHOR_ORIGIN);
			}

			// Apply the inline style to the result.
			if (styleLocalName != null) {
				String style = elt.getAttributeNS(styleNamespaceURI, styleLocalName);
				if (style.length() > 0) {
					try {
						styleDeclarationDocumentHandler.styleMap = result;
						parser.setDocumentHandler(styleDeclarationDocumentHandler);
						parser.parseStyleDeclaration(new StringReader(style));
						styleDeclarationDocumentHandler.styleMap = null;
					} catch (Exception e) {
						String m = e.getMessage();
						if (m == null)
							m = e.getClass().getName();
						String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
						String s = Messages.formatMessage("style.syntax.error.at",
								new Object[] { u, styleLocalName, style, m });
						DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
						de.initCause(e);
						if (userAgent == null)
							throw de;
						userAgent.displayError(de);
					}
				}
			}

			// Apply the override rules to the result.
			StyleDeclarationProvider p = elt.getOverrideStyleDeclarationProvider();
			if (p != null) {
				StyleDeclaration over = p.getStyleDeclaration();
				if (over != null) {
					int ol = over.size();
					for (int i = 0; i < ol; i++) {
						int idx = over.getIndex(i);
						Value value = over.getValue(i);
						boolean important = over.getPriority(i);
						if (!result.isImportant(idx) || important) {
							result.putValue(idx, value);
							result.putImportant(idx, important);
							result.putOrigin(idx, StyleMap.OVERRIDE_ORIGIN);
						}
					}

					// Custom properties
					Map<String, LexicalUnit> customProp = over.getCustomProperties();
					if (customProp != null) {
						for (Map.Entry<String, LexicalUnit> entry : customProp.entrySet()) {
							result.putCustomProperty(entry.getKey(), entry.getValue());
						}
					}
				}
			}
		} finally {
			element = null;
			cssBaseURI = null;
		}

		return result;
	}

	private Parser createCSSParser() {
		return new CSSOMParser();
	}

	/**
	 * Returns the computed style of the given element/pseudo for the property
	 * corresponding to the given index.
	 */
	public Value getComputedStyle(CSSStylableElement elt, String pseudo, int propidx)
			throws CSSSecurityException {
		StyleMap sm = elt.getComputedStyleMap(pseudo);
		if (sm == null) {
			sm = getCascadedStyleMap(elt, pseudo);
			elt.setComputedStyleMap(pseudo, sm);
		}

		Value value = sm.getValue(propidx);
		if (sm.isComputed(propidx))
			return value;

		Value result = value;
		ValueManager vm = valueManagers[propidx];
		CSSStylableElement p = getParentCSSStylableElement(elt);
		if (value == null) {
			if (p == null || !vm.isInheritedProperty()) {
				result = vm.getDefaultValue();
			}
		} else if (value.getPrimitiveType() == Type.LEXICAL) {
			LexicalValue var = (LexicalValue) value;
			LexicalUnit lunit = replaceLexicalValue(sm, var.getLexicalUnit(), elt, p, propidx);
			if (lunit != null) {
				result = vm.createValue(lunit, this);
				if (result == null || result.getCssValueType() == CSSValue.CssType.KEYWORD) {
					result = initialOrNull(p != null, vm, result);
				}
			} else {
				result = initialOrNull(p != null, vm, null);
			}
		} else if (value.getPrimitiveType() == Type.INTERNAL) {
			PendingValue pending = (PendingValue) value;
			if (substitutePendingShorthand(sm, pending, elt, p, propidx)) {
				result = sm.getValue(propidx);
			} else {
				result = initialOrNull(p != null, vm, null);
			}
		} else if (value.getCssValueType() == CSSValue.CssType.KEYWORD) {
			result = initialOrNull(p != null, vm, value);
		}

		if (result != null) {
			// Maybe it is a relative value.
			result = vm.computeValue(elt, pseudo, this, propidx, sm, result);
			if (result == null) {
				// calc() gave invalid result
				result = initialOrNull(p != null, vm, null);
			}
		}

		if (result == null) {
			// Value is 'inherit' and p != null.
			// The pseudo class is not propagated.
			result = getComputedStyle(p, null, propidx);
			sm.putParentRelative(propidx, true);
			sm.putInherited(propidx, true);
		}

		if (value == null) {
			sm.putValue(propidx, result);
			sm.putNullCascaded(propidx, true);
		} else if (result != value) {
			ComputedValue cv = new ComputedValue(value);
			cv.setComputedValue(result);
			sm.putValue(propidx, cv);
			result = cv;
		}

		sm.putComputed(propidx, true);
		return result;
	}

	private static Value initialOrNull(boolean hasParent, ValueManager vm, Value value) {
		Value result;
		if (hasParent && (vm.isInheritedProperty() || value == InheritValue.getInstance())) {
			result = null;
		} else {
			result = vm.getDefaultValue();
		}
		return result;
	}

	/**
	 * Substitute the {@code PROXY} values in a lexical value.
	 * 
	 * @param sm          the style map.
	 * @param lexicalUnit the lexical value.
	 * @param elt         the element for which the value is computed.
	 * @param parent      the parent element, or {@code null} if no parent.
	 * @param propIdx     the property index.
	 * @return the replaced lexical unit.
	 * @throws DOMException
	 * @throws CSSCircularityException   if a circularity was found while evaluating
	 *                                   custom properties.
	 * @throws CSSResourceLimitException if the limit of recursions or allowed
	 *                                   substitutions was exceeded.
	 */
	private LexicalUnit replaceLexicalValue(StyleMap sm, LexicalUnit lexicalUnit, CSSStylableElement elt,
			CSSStylableElement parent, int propIdx) throws CSSSecurityException {
		HashSet<String> customPropertySet = new HashSet<>(INITIAL_CUSTOM_PTY_SET_SIZE);

		CounterRef counter = new CounterRef();

		LexicalUnit lunit = lexicalUnit.clone();
		LexicalUnit replUnit;
		try {
			replUnit = replaceLexicalProxy(sm, lunit, elt, parent, counter, customPropertySet, propIdx);
		} catch (CSSSecurityException e) {
			throw e;
		} catch (DOMException e) {
			displayOrThrowError(e);
			return null;
		}

		if (replUnit != null && replUnit.getLexicalUnitType() == LexicalType.EMPTY) {
			replUnit = null;
		}

		return replUnit;
	}

	/**
	 * Given a lexical value, replace all occurrences of the {@code VAR} and
	 * {@code ATTR} lexical types with the values of the corresponding custom
	 * properties or attributes, and incrementing the supplied counter.
	 * 
	 * @param sm           the style map.
	 * @param lexval       the lexical value.
	 * @param elt          the element for which the value is computed.
	 * @param parent       the parent element, or {@code null} if no parent.
	 * @param counter      the substitution and recursion counter.
	 * @param customPtySet the set of custom property names, to prevent circular
	 *                     dependencies.
	 * @param propIdx      the property index.
	 * @return the replaced lexical unit.
	 * @throws DOMException
	 * @throws CSSCircularityException   if a circularity was found while evaluating
	 *                                   custom properties.
	 * @throws CSSResourceLimitException if the limit of recursions or allowed
	 *                                   substitutions was exceeded.
	 */
	private LexicalUnit replaceLexicalProxy(StyleMap sm, LexicalUnit lexval, CSSStylableElement elt,
			CSSStylableElement parent, CounterRef counter, Set<String> customPtySet, int propIdx)
					throws DOMException, CSSSecurityException {
		final int REPLACE_COUNT_LIMIT = 0x20000; // Number of allowed lexical substitutions

		/*
		 * Replace the PROXY (var(), attr()) values in the lexical chain
		 */
		LexicalUnit lu = lexval;
		topLoop: do {
			if (lu.getLexicalUnitType() == LexicalType.VAR) {
				sm.putCustomPtyRelative(propIdx, true);

				LexicalUnit newlu;
				LexicalUnit param = lu.getParameters();
				String propertyName = param.getStringValue(); // Property name
				param = param.getNextLexicalUnit(); // Comma?
				if (param != null) {
					param = param.getNextLexicalUnit(); // Fallback
				}

				/*
				 * Prepare a working set of traversed custom properties and attr()
				 */
				Set<String> ptySet = new HashSet<>(customPtySet.size() + 1);
				ptySet.addAll(customPtySet);

				/*
				 * Obtain a value and replace this var() in the lexical chain
				 */
				newlu = getCustomPropertyValueOrFallback(sm, propertyName, param, parent, counter,
						ptySet);

				if (newlu != null) {
					// We do not want to mess with a declared value, so clone it
					newlu = newlu.clone();
					// Verify whether we got another proxy value.
					newlu = replaceLexicalProxy(sm, newlu, elt, parent, counter, ptySet, propIdx);
				}

				boolean isLexval = lu == lexval;
				if (newlu == null) {
					// The current lexical unit can be removed
					lu = lu.remove();
					if (isLexval) {
						// We are processing the first in the lexical chain, re-assign
						lexval = lu;
					}
					ptySet.remove(propertyName);
					continue;
				}

				if (newlu.getLexicalUnitType() != LexicalType.EMPTY) {
					try {
						counter.replaceCounter += lu.countReplaceBy(newlu);
					} catch (CSSBudgetException e) {
						throw createVarResourceLimitException(propertyName, e);
					}
					if (counter.replaceCounter >= REPLACE_COUNT_LIMIT) {
						throw createVarResourceLimitException(propertyName);
					}
					lu = newlu;
					if (isLexval) {
						// We are processing the first in the lexical chain, re-assign
						lexval = newlu;
					}
				} else {
					// The current lexical unit can be removed
					LexicalUnit nextlu = lu.remove();
					if (nextlu != null) {
						lu = nextlu;
					} else {
						lu = newlu;
					}
					if (isLexval) {
						// We are processing the first in the lexical chain, re-assign
						lexval = lu;
					}
					ptySet.remove(propertyName);
				}
				continue;
			} else if (lu.getLexicalUnitType() == LexicalType.ATTR) {
				boolean isLexval = lu == lexval;

				/*
				 * Prepare a working set of traversed custom properties and attr()
				 */
				Set<String> ptySet = new HashSet<>(customPtySet.size() + 1);
				ptySet.addAll(customPtySet);

				LexicalUnit newlu = replacementAttrUnit(sm, lu, elt, parent, counter, ptySet, propIdx);

				if (newlu != null) {
					// Verify whether we got another proxy value.
					newlu = replaceLexicalProxy(sm, newlu, elt, parent, counter, ptySet, propIdx);
				}

				if (newlu == null) {
					// The current lexical unit can be removed
					lu = lu.remove();
					if (isLexval) {
						// We are processing the first in the lexical chain, re-assign
						lexval = lu;
					}
					continue;
				}

				sm.putAttrTainted(propIdx, true);

				// Handle EMPTY values
				while (newlu.getLexicalUnitType() == LexicalType.EMPTY) {
					LexicalUnit nextlu = newlu.remove();
					if (nextlu == null) {
						// The current lexical unit can be removed
						lu = lu.remove();
						if (lu == null) {
							lu = newlu;
						}
						if (isLexval) {
							// We are processing the first in the lexical chain, re-assign
							lexval = lu;
						}
						continue topLoop;
					} else {
						newlu = nextlu;
					}
				}

				try {
					counter.replaceCounter += lu.countReplaceBy(newlu);
				} catch (CSSBudgetException e) {
					throw createAttrResourceLimitException(e);
				}
				if (counter.replaceCounter >= REPLACE_COUNT_LIMIT) {
					throw createAttrResourceLimitException();
				}

				lu = newlu;
				if (isLexval) {
					// We are processing the first in the lexical chain, re-assign
					lexval = newlu;
				}
				continue;
			} else {
				LexicalUnit param = lu.getParameters();
				if (param != null || (param = lu.getSubValues()) != null) {
					// Ignore return value (it is a parameter or a sub-value)
					replaceLexicalProxy(sm, param, elt, parent, counter, customPtySet, propIdx);
				}
			}
			lu = lu.getNextLexicalUnit();
		} while (lu != null);

		return lexval;
	}

	private LexicalUnit replacementAttrUnit(StyleMap sm, LexicalUnit attr, CSSStylableElement elt,
			CSSStylableElement parent, CounterRef counter, Set<String> checkSet, int propIdx) {
		// Obtain attribute name and type (if set)
		LexicalUnit lu = attr.getParameters();
		if (lu.getLexicalUnitType() != LexicalType.IDENT) {
			valueSyntaxError("Unexpected attribute name (" + lu.getCssText() + ") in " + attr.getCssText());
			return null;
		}
		String attrname = lu.getStringValue();

		if (!checkSet.add(attrname)) {
			throw new CSSCircularityException(
					"Circularity evaluating attr() '" + attrname + "': " + checkSet.toString());
		}

		CSSValueSyntax syn = null;
		short unitConv = -1; // -1 means string
		lu = lu.getNextLexicalUnit();
		if (lu != null) {
			if (lu.getLexicalUnitType() != LexicalType.OPERATOR_COMMA) {
				switch (lu.getLexicalUnitType()) {
				case IDENT:
					String attrtype = lu.getStringValue().toLowerCase(Locale.ROOT);
					// TODO: remove "string" once Chrome 136 ships
					if (!"raw-string".equals(attrtype) && !"string".equals(attrtype)) {
						unitConv = UnitStringToId.unitFromString(attrtype);
						if (unitConv == CSSUnit.CSS_OTHER) {
							valueSyntaxError(
									"Unexpected attribute type (" + lu.getCssText() + ") in "
											+ attr.getCssText());
							return null;
						}
					}
					break;
				case OPERATOR_MOD:
					// %
					unitConv = CSSUnit.CSS_PERCENTAGE;
					break;
				case TYPE_FUNCTION:
					LexicalUnit param = lu.getParameters();
					if (param.getLexicalUnitType() == LexicalType.SYNTAX) {
						syn = param.getSyntax();
						// Set some unit so it is not processed as string
						unitConv = CSSUnit.CSS_NUMBER;
						break;
					}
				default:
					valueSyntaxError(
							"Unexpected attribute type (" + lu.getCssText() + ") in " + attr.getCssText());
					return lu == null ? null : lu.clone();
				}

				lu = lu.getNextLexicalUnit();
				if (lu != null) {
					if (lu.getLexicalUnitType() != LexicalType.OPERATOR_COMMA) {
						valueSyntaxError(
								"Expected comma, found: " + lu.getCssText() + " in " + attr.getCssText());
						return null;
					}
					lu = lu.getNextLexicalUnit();
					// Now lu contains the fallback
				}
			} else {
				lu = lu.getNextLexicalUnit();
				if (lu == null) {
					// Ending with comma is wrong syntax
					valueSyntaxError("Unexpected end after comma in value " + attr.getCssText());
					return null;
				}
			}
		}

		// Obtain the attribute value
		org.w3c.dom.Attr attrNode = elt.getAttributeNode(attrname);
		String attrvalue = attrNode != null ? attrNode.getValue() : "";

		Parser parser = createCSSParser();

		/*
		 * string type is a special case
		 */
		if (unitConv == -1) { // string
			LexicalUnit substValue;
			if (attrNode != null || lu == null) {
				String s = ParseHelper.quote(attrvalue, '"');
				try {
					substValue = parser.parsePropertyValue(new StringReader(s));
				} catch (IOException e) {
					// This won't happen
					substValue = null;
				} catch (CSSParseException e) {
					// Possibly a budget error
					valueSyntaxError("Unexpected error parsing: " + s.substring(0, Math.min(s.length(), 255)), e);
					// Process fallback
					if (lu != null) {
						substValue = lu.clone();
					} else {
						try {
							substValue = parser.parsePropertyValue(new StringReader("\"\""));
						} catch (CSSParseException | IOException e1) {
							substValue = null; // cannot happen
						}
					}
				}
			} else {
				// fallback cannot be null here
				substValue = lu.clone();
			}
			// No further processing required
			return substValue;
		}

		attrvalue = attrvalue.trim();

		if (!attrvalue.isEmpty()) {
			/*
			 * Non-string types
			 */

			LexicalUnit substValue;
			try {
				substValue = parser.parsePropertyValue(new StringReader(attrvalue));
			} catch (IOException e) {
				// This won't happen
				substValue = null;
			} catch (CSSParseException e) {
				valueSyntaxError("Error parsing attribute '" + attrname + "', value: " + attrvalue, e);
				// Return fallback
				if (lu != null) {
					return lu.clone();
				}
				return null;
			}

			// Substitute proxy values before checking type.
			substValue = replaceLexicalProxy(sm, substValue, elt, parent, counter, checkSet, propIdx);

			if (substValue != null) {
				// Now check that the value is of the correct type.
				//
				if (syn != null) {
					if (substValue.matches(syn) == Match.TRUE) {
						return substValue;
					} else {
						String message = "Attribute " + attrname + " with value '" + substValue
								+ "' does not match type '" + syn.toString() + "'.";
						if (lu == null) {
							// Throw an exception to break the chain replacement
							throw new DOMException(DOMException.TYPE_MISMATCH_ERR, message);
						}
						valueSyntaxError(message);
					}
				} else {
					// Unit-based types
					float f;
					LexicalType luType = substValue.getLexicalUnitType();
					if (luType == LexicalType.INTEGER) {
						f = substValue.getIntegerValue();
					} else if (luType == LexicalType.REAL) {
						f = substValue.getFloatValue();
					} else {
						String message = "Attribute unit is not a <number>, instead is: "
								+ CSSUnit.dimensionUnitString(substValue.getCssUnit());
						// Check for fallback
						if (lu != null) {
							valueSyntaxError(message);
							return lu.clone();
						}
						// Guaranteed-invalid
						throw new DOMException(DOMException.TYPE_MISMATCH_ERR, message);
					}
					if (unitConv != CSSUnit.CSS_NUMBER) {
						// Must convert to unit
						substValue = ParseHelper.createDimensionLexicalUnit(unitConv, f);
					}
					return substValue;
				}
			}
		}

		// Return fallback
		return lu == null ? null : lu.clone();
	}

	/**
	 * Obtain the (lexical) value of a custom property and replace any {@code VAR}
	 * unit in it, applying the fallback if necessary.
	 * 
	 * @param sm
	 * @param customProperty the custom property name.
	 * @param fallbackLU     the custom property fallback.
	 * @param parent         the parent element, or {@code null} if no parent.
	 * @param counter        the counter.
	 * @param customPtySet   check that the custom property name is not a member of
	 *                       this set of custom property names, to prevent circular
	 *                       dependencies.
	 * @return the value of {@code customProperty} or the fallback if there is no
	 *         value.
	 * @throws DOMException
	 * @throws CSSCircularityException   if a circularity was found while evaluating
	 *                                   custom properties.
	 * @throws CSSResourceLimitException if the limit of recursions or allowed
	 *                                   substitutions was exceeded.
	 */
	private LexicalUnit getCustomPropertyValueOrFallback(StyleMap sm, String customProperty, LexicalUnit fallbackLU,
			CSSStylableElement parent, CounterRef counter, Set<String> customPtySet)
			throws DOMException, CSSSecurityException {

		if (customPtySet.contains(customProperty)) {
			throw new CSSCircularityException(
					"Circularity evaluating custom property " + customProperty + ": "
							+ customPtySet.toString());
		}

		if (!counter.increment()) {
			throw createVarResourceLimitException(customProperty);
		}

		LexicalUnit custom = getCustomProperty(sm, customProperty, parent);

		if (custom != null) {
			customPtySet.add(customProperty);
			return custom;
		}

		// Fallback
		return fallbackLU;
	}

	private LexicalUnit getCustomProperty(StyleMap sm, String name, CSSStylableElement parent) {
		// First, try to obtain a possible property definition from a @property rule
		PropertyDefinition definition = getPropertyDefinition(name);
		boolean inherits = definition == null || definition.inherits();

		LexicalUnit custom;
		while ((custom = sm.getCustomProperty(name)) == null && inherits) {
			if (parent != null) {
				sm = parent.getComputedStyleMap(null);
				if (sm == null) {
					sm = getCascadedStyleMap(parent, null);
					parent.setComputedStyleMap(null, sm);
				}
				parent = getParentCSSStylableElement(parent);
			} else {
				break;
			}
		}

		if (definition != null) {
			if (custom == null) {
				custom = definition.getInitialValue();
			} else {
				CSSValueSyntax syntax = definition.getSyntax();
				// syntax is never null
				if (custom.matches(syntax) == Match.FALSE) {
					custom = definition.getInitialValue();
				}
			}
		}

		return custom;
	}

	private PropertyDefinition getPropertyDefinition(String name) {
		return propertyDefinitionMap == null ? null : propertyDefinitionMap.get(name);
	}

	/**
	 * Perform a lexical substitution on a pending shorthand value.
	 * 
	 * @param sm      the style map.
	 * @param pending the pending longhand value.
	 * @param elt     the element for which the value is computed.
	 * @param parent  the parent element, or {@code null} if no parent.
	 * @param propIdx the property index.
	 * @return {@code true} if the shorthand was replaced successfully.
	 * @throws DOMException
	 * @throws CSSCircularityException   if a circularity was found while evaluating
	 *                                   custom properties.
	 * @throws CSSResourceLimitException if the limit of recursions or allowed
	 *                                   substitutions was exceeded.
	 */
	private boolean substitutePendingShorthand(StyleMap sm, PendingValue pending, CSSStylableElement elt,
			CSSStylableElement parent, int propIdx) throws DOMException, CSSSecurityException {
		LexicalUnit lunit = replaceLexicalProxy(sm, pending.getLexicalUnit().clone(), elt, parent,
				new CounterRef(), new HashSet<>(INITIAL_CUSTOM_PTY_SET_SIZE), propIdx);
		boolean ret = lunit != null ?
				setShorthandLonghands(sm, pending.getShorthandName(), lunit, sm.isImportant(propIdx)) : false;
		return ret;
	}

	private boolean setShorthandLonghands(StyleMap sm, String propertyName, LexicalUnit value,
			boolean important) throws DOMException {
		try {
			int idx = getShorthandIndex(propertyName);
			if (idx == -1)
				return false; // Unknown property...
			// Shorthand value
			shorthandManagers[idx].setValues(CSSEngine.this, new ShorthandManager.PropertyHandler() {

				@Override
				public void property(String pname, LexicalUnit value, boolean important) {
					int idx = getPropertyIndex(pname);
					if (idx != -1) {
						Value oldv = sm.getValue(idx);
						if (oldv == null || oldv.getPrimitiveType() == Type.INTERNAL) {
							ValueManager vm = valueManagers[idx];
							Value v = vm.createValue(value, CSSEngine.this);
							sm.putValue(idx, v);
							// sm.putImportant(idx, important); // already done
						} // else the value was set later
					} else {
						// This can be removed
						throw new IllegalStateException("Unknown pending value.");
					}
				}

				@Override
				public void pendingValue(String name, PendingValue value, boolean important) {
					throw new IllegalStateException("Cannot set pending values after replacement.");
				}

			}, value, important);
			return true;
		} catch (DOMException e) {
			// Report error
			DOMException ex = new DOMException(e.code, "Error setting shorthand " + propertyName);
			ex.initCause(e);
			displayOrThrowError(ex);
			return false;
		}
	}

	private void valueSyntaxError(String message) {
		DOMException ex = new DOMException(DOMException.SYNTAX_ERR, message);
		displayOrThrowError(ex);
	}

	private void valueSyntaxError(String message, Throwable cause) {
		DOMException ex = new DOMException(DOMException.SYNTAX_ERR, message);
		ex.initCause(cause);
		displayOrThrowError(ex);
	}

	private CSSResourceLimitException createVarResourceLimitException(String propertyName) {
		return createResourceLimitException(
				"Resource limit hit while replacing custom property: " + propertyName);
	}

	private CSSResourceLimitException createVarResourceLimitException(String propertyName, Throwable cause) {
		return createResourceLimitException(
				"Resource limit hit while replacing custom property " + propertyName, cause);
	}

	private CSSResourceLimitException createAttrResourceLimitException() {
		return createResourceLimitException("Resource limit hit while replacing attr() property.");
	}

	private CSSResourceLimitException createAttrResourceLimitException(Throwable e) {
		return createResourceLimitException(
				"Resource limit hit while replacing attr() property.", e);
	}

	private CSSResourceLimitException createResourceLimitException(String message) {
		return new CSSResourceLimitException(message);
	}

	private CSSResourceLimitException createResourceLimitException(String message, Throwable e) {
		return new CSSResourceLimitException(message, e);
	}

	private void displayOrThrowError(RuntimeException ex) {
		if (userAgent == null) {
			throw ex;
		}
		userAgent.displayError(ex);
	}

	/**
	 * Returns the document CSSStyleSheetNodes in a list. This list is updated as
	 * the document is modified.
	 */
	public List<Node> getStyleSheetNodes() {
		if (styleSheetNodes == null) {
			styleSheetNodes = new ArrayList<>();
			selectorAttributes = new HashSet<>();
			// Create the attribute visitor
			AttributeVisitor visitor = new AttributeVisitor();
			// Find all the style-sheets in the document.
			findStyleSheetNodes(document);
			for (Node styleSheetNode : styleSheetNodes) {
				CSSStyleSheetNode ssn;
				ssn = (CSSStyleSheetNode) styleSheetNode;
				StyleSheet ss = ssn.getCSSStyleSheet();
				if (ss != null) {
					findSelectorAttributes(visitor, ss);
				}
			}
		}
		return styleSheetNodes;
	}

	/**
	 * An auxiliary method for getStyleSheets().
	 */
	protected void findStyleSheetNodes(Node n) {
		if (n instanceof CSSStyleSheetNode) {
			styleSheetNodes.add(n);
		}
		for (Node nd = getCSSFirstChild(n); nd != null; nd = getCSSNextSibling(nd)) {
			findStyleSheetNodes(nd);
		}
	}

	/**
	 * Finds the selector attributes in the given stylesheet.
	 */
	private void findSelectorAttributes(AttributeVisitor visitor, StyleSheet ss) {
		int len = ss.getSize();
		for (int i = 0; i < len; i++) {
			Rule r = ss.getRule(i);
			switch (r.getType()) {
			case StyleRule.TYPE:
				StyleRule style = (StyleRule) r;
				SelectorList sl = style.getSelectorList();
				visitor.visit(sl);
				break;

			case MediaRule.TYPE:
			case ImportRule.TYPE:
				MediaRule mr = (MediaRule) r;
				if (mediaMatch(mr.getMediaList())) {
					findSelectorAttributes(visitor, mr);
				}
				break;
			}
		}
	}

	private class AttributeVisitor extends AttributeConditionVisitor {

		AttributeVisitor() {
			super();
		}

		@Override
		public void visit(AttributeCondition condition) {
			switch (condition.getConditionType()) {
			case ATTRIBUTE:
			case ONE_OF_ATTRIBUTE:
			case BEGIN_HYPHEN_ATTRIBUTE:
			case BEGINS_ATTRIBUTE:
			case ENDS_ATTRIBUTE:
			case SUBSTRING_ATTRIBUTE:
				selectorAttributes.add(condition.getLocalName());
				break;
			default:
				break;
			}
		}

	}

	/**
	 * Interface for people interesting in having 'primary' properties set.
	 * Shorthand properties will be expanded "automatically".
	 */
	public interface MainPropertyReceiver {

		/**
		 * Called with a non-shorthand property name and it's value.
		 */
		void setMainProperty(String name, Value v, boolean important);

	}

	public void setMainProperties(CSSStylableElement elt, final MainPropertyReceiver dst, String pname, String value,
			boolean important) {
		try {
			element = elt;
			LexicalUnit lu = parser.parsePropertyValue(new StringReader(value));
			ShorthandManager.PropertyHandler ph = new ShorthandManager.PropertyHandler() {
				@Override
				public void property(String pname, LexicalUnit lu, boolean important) {
					int idx = getPropertyIndex(pname);
					if (idx != -1) {
						ValueManager vm = valueManagers[idx];
						Value v = vm.createValue(lu, CSSEngine.this);
						dst.setMainProperty(pname, v, important);
						return;
					}
					idx = getShorthandIndex(pname);
					if (idx == -1)
						return; // Unknown property...
					// Shorthand value
					shorthandManagers[idx].setValues(CSSEngine.this, this, lu, important);
				}

				@Override
				public void pendingValue(String name, PendingValue value, boolean important) {
					dst.setMainProperty(name, value, important);
				}
			};
			ph.property(pname, lu, important);
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = ""; // todo - better handling of NPE
			String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
			String s = Messages.formatMessage("property.syntax.error.at", new Object[] { u, pname, value, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
		} finally {
			element = null;
			cssBaseURI = null;
		}
	}

	/**
	 * Parses and creates a property value from elt.
	 * 
	 * @param elt   The element property is from.
	 * @param prop  The property name.
	 * @param value The property value.
	 */
	public Value parsePropertyValue(CSSStylableElement elt, String prop, String value) {
		int idx = getPropertyIndex(prop);
		if (idx == -1) {
			return null;
		}
		ValueManager vm = valueManagers[idx];
		try {
			element = elt;
			LexicalUnit lu;
			lu = parser.parsePropertyValue(new StringReader(value));
			return vm.createValue(lu, this);
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = "";
			String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
			String s = Messages.formatMessage("property.syntax.error.at", new Object[] { u, prop, value, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
		} finally {
			element = null;
			cssBaseURI = null;
		}
		return vm.getDefaultValue();
	}

	/**
	 * Parses and creates a style declaration.
	 * 
	 * @param value The style declaration text.
	 */
	public StyleDeclaration parseStyleDeclaration(CSSStylableElement elt, String value) {
		styleDeclarationBuilder.styleDeclaration = new StyleDeclaration();
		try {
			element = elt;
			parser.setDocumentHandler(styleDeclarationBuilder);
			parser.parseStyleDeclaration(new StringReader(value));
		} catch (SecurityException e) {
			// We catch security exceptions, in the unlikely case that the
			// creation of values produces one.
			throw e;
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = "";
			String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
			String s = Messages.formatMessage("syntax.error.at", new Object[] { u, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
		} finally {
			element = null;
			cssBaseURI = null;
		}
		return styleDeclarationBuilder.styleDeclaration;
	}

	/**
	 * Parses and creates a new style-sheet.
	 * 
	 * @param uri   The style-sheet URI.
	 * @param media The target media of the style-sheet.
	 */
	public StyleSheet parseStyleSheet(ParsedURL uri, String media) throws DOMException {
		StyleSheet ss = new StyleSheet();
		try {
			ss.setMedia(parser.parseMediaQueryList(media, null));
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = "";
			String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
			String s = Messages.formatMessage("syntax.error.at", new Object[] { u, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
			return ss;
		}
		parseStyleSheet(ss, uri);
		return ss;
	}

	/**
	 * Parses and creates a new style-sheet.
	 * 
	 * @param is    The input source used to read the document.
	 * @param uri   The base URI.
	 * @param media The target media of the style-sheet.
	 */
	public StyleSheet parseStyleSheet(InputSource is, ParsedURL uri, String media) throws DOMException {
		StyleSheet ss = new StyleSheet();
		try {
			ss.setMedia(parser.parseMediaQueryList(media, null));
			parseStyleSheet(ss, is, uri);
		} catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = "";
			String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
			String s = Messages.formatMessage("syntax.error.at", new Object[] { u, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
		}
		return ss;
	}

	/**
	 * Parses and fills the given style-sheet.
	 * 
	 * @param ss  The stylesheet to fill.
	 * @param uri The base URI.
	 */
	public void parseStyleSheet(StyleSheet ss, ParsedURL uri) throws DOMException {
		if (uri == null) {
			String s = Messages.formatMessage("syntax.error.at", new Object[] { "Null Document reference", "" });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
			return;
		}

		try {
			// Check that access to the uri is allowed
			cssContext.checkLoadExternalResource(uri, documentURI);
			parseStyleSheet(ss, new InputSource(uri.toString()), uri);
		} catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = e.getClass().getName();
			String s = Messages.formatMessage("syntax.error.at", new Object[] { uri.toString(), m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
		}
	}

	/**
	 * Parses and creates a new style-sheet.
	 * 
	 * @param rules The style-sheet rules to parse.
	 * @param uri   The style-sheet URI.
	 * @param media The target media of the style-sheet.
	 */
	public StyleSheet parseStyleSheet(String rules, ParsedURL uri, String media) throws DOMException {
		StyleSheet ss = new StyleSheet();
		try {
			ss.setMedia(parser.parseMediaQueryList(media, null));
		} catch (Exception e) {
			String m = e.getMessage();
			if (m == null)
				m = "";
			String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
			String s = Messages.formatMessage("syntax.error.at", new Object[] { u, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
			return ss;
		}
		parseStyleSheet(ss, rules, uri);
		return ss;
	}

	/**
	 * Parses and fills the given style-sheet.
	 * 
	 * @param ss    The stylesheet to fill.
	 * @param rules The style-sheet rules to parse.
	 * @param uri   The base URI.
	 */
	public void parseStyleSheet(StyleSheet ss, String rules, ParsedURL uri) throws DOMException {
		try {
			parseStyleSheet(ss, new InputSource(new StringReader(rules)), uri);
		} catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			// e.printStackTrace();
			String m = e.getMessage();
			if (m == null) {
				m = "";
			}
			final String strUri;
			if (uri != null) {
				strUri = uri.toString();
			} else {
				strUri = "";
			}
			String s = Messages.formatMessage("stylesheet.syntax.error",
					new Object[] { strUri, rules, m });
			DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
			de.initCause(e);
			if (userAgent == null)
				throw de;
			userAgent.displayError(de);
		}
	}

	/**
	 * Parses and fills the given style-sheet.
	 * 
	 * @param ss  The stylesheet to fill.
	 * @param uri The base URI.
	 */
	protected void parseStyleSheet(StyleSheet ss, InputSource is, ParsedURL uri) throws IOException {
		try {
			cssBaseURI = uri;
			styleSheetDocumentHandler.styleSheet = ss;
			parser.setDocumentHandler(styleSheetDocumentHandler);
			parser.parseStyleSheet(is);

			// Load the imported sheets.
			int len = ss.getSize();
			for (int i = 0; i < len; i++) {
				Rule r = ss.getRule(i);
				if (r.getType() != ImportRule.TYPE) {
					// @import rules must be the first rules.
					break;
				}
				ImportRule ir = (ImportRule) r;
				parseStyleSheet(ir, ir.getURI());
			}
		} finally {
			cssBaseURI = null;
		}
	}

	/**
	 * Puts an author property from a style-map in another style-map, if possible.
	 */
	protected void putAuthorProperty(StyleMap dest, int idx, Value sval, boolean imp, int origin) {
		Value dval = dest.getValue(idx);
		int dorg = dest.getOrigin(idx);
		boolean dimp = dest.isImportant(idx);

		boolean cond = dval == null;
		if (!cond) {
			switch (dorg) {
			case StyleMap.USER_ORIGIN:
				cond = !dimp;
				break;
			case StyleMap.AUTHOR_ORIGIN:
				cond = !dimp || imp;
				break;
			case StyleMap.OVERRIDE_ORIGIN:
				cond = false;
				break;
			default:
				cond = true;
			}
		}

		if (cond) {
			dest.putValue(idx, sval);
			dest.putImportant(idx, imp);
			dest.putOrigin(idx, origin);
		}
	}

	/**
	 * Adds the rules matching the element/pseudo-element of given style sheet to
	 * the list.
	 */
	protected void addMatchingRules(List<Rule> rules, StyleSheet ss, SelectorMatcher matcher) {
		int len = ss.getSize();
		for (int i = 0; i < len; i++) {
			Rule r = ss.getRule(i);
			switch (r.getType()) {
			case StyleRule.TYPE:
				StyleRule style = (StyleRule) r;
				SelectorList sl = style.getSelectorList();
				int slen = sl.getLength();
				for (int j = 0; j < slen; j++) {
					Selector s = sl.item(j);
					if (matcher.matches(s)) {
						rules.add(style);
					}
				}
				break;

			case MediaRule.TYPE:
			case ImportRule.TYPE:
				MediaRule mr = (MediaRule) r;
				if (mediaMatch(mr.getMediaList())) {
					addMatchingRules(rules, mr, matcher);
				}
				break;

			case SupportsRule.TYPE:
				SupportsRule sr = (SupportsRule) r;
				if (sr.supports) {
					addMatchingRules(rules, sr, matcher);
				}
				break;
			}
		}
	}

	/**
	 * Whether the given media list matches the media list of this CSSEngine object.
	 */
	protected boolean mediaMatch(MediaQueryList ml) {
		if (medium == null || ml == null || ml.isAllMedia()) {
			return true;
		}
		return ml.matches(medium, csscanvas);
	}

	private void setSupports(SupportsRule sr) {
		BooleanCondition condition = sr.getCondition();
		if (condition != null) {
			try {
				sr.supports = supports(condition);
				return;
			} catch (Exception e) {
			}
		}
		sr.supports = false;
	}

	private boolean supports(BooleanCondition condition) {
		switch (condition.getType()) {
		case PREDICATE:
			DeclarationCondition declCond = (DeclarationCondition) condition;
			return supports(declCond.getName(), declCond.getValue());
		case AND:
			List<BooleanCondition> subcond = condition.getSubConditions();
			if (subcond == null) {
				// No conditions inside and()
				DOMException ex = new DOMException(DOMException.SYNTAX_ERR, "No conditions inside and().");
				userAgent.displayError(ex);
				return false;
			}
			Iterator<BooleanCondition> it = subcond.iterator();
			while (it.hasNext()) {
				if (!supports(it.next())) {
					return false;
				}
			}
			return true;
		case NOT:
			BooleanCondition nested = condition.getNestedCondition();
			if (nested == null) {
				// No condition inside not()
				DOMException ex = new DOMException(DOMException.SYNTAX_ERR, "No condition inside not().");
				userAgent.displayError(ex);
				return false;
			}
			return !supports(nested);
		case OR:
			subcond = condition.getSubConditions();
			if (subcond == null) {
				// No conditions inside or()
				DOMException ex = new DOMException(DOMException.SYNTAX_ERR, "No conditions inside or().");
				userAgent.displayError(ex);
				return false;
			}
			it = subcond.iterator();
			while (it.hasNext()) {
				if (supports(it.next())) {
					return true;
				}
			}
			break;
		case SELECTOR_FUNCTION:
			SelectorFunction selCond = (SelectorFunction) condition;
			return styleDb.supports(selCond.getSelectors());
		case OTHER:
			break;
		}

		return false;
	}

	private boolean supports(String property, LexicalUnit value) {
		int idx = getPropertyIndex(property);
		if (idx != -1) {
			try {
				valueManagers[idx].createValue(value, CSSEngine.this);
				return true;
			} catch (Exception e) {
			}
			return false;
		}

		idx = getShorthandIndex(property);
		if (idx != -1) {
			try {
				shorthandManagers[idx].setValues(this, new ShorthandManager.PropertyHandler() {

					@Override
					public void property(String name, LexicalUnit value, boolean important) {
					}

					@Override
					public void pendingValue(String name, PendingValue value, boolean important) {
					}

				}, value, false);
				return true;
			} catch (Exception e) {
			}
		}

		return false;
	}

	/**
	 * Adds the rules contained in the given list to a stylemap.
	 */
	protected void addRules(SelectorMatcher matcher, StyleMap sm, ArrayList<Rule> rules, int origin) {
		sortRules(rules, matcher);

		if (origin == StyleMap.AUTHOR_ORIGIN) {
			for (Rule rule : rules) {
				StyleRule sr = (StyleRule) rule;
				StyleDeclaration sd = sr.getStyleDeclaration();
				int len = sd.size();
				for (int i = 0; i < len; i++) {
					putAuthorProperty(sm, sd.getIndex(i), sd.getValue(i), sd.getPriority(i), origin);
				}

				// Custom properties
				Map<String, LexicalUnit> customProp = sd.getCustomProperties();
				if (customProp != null) {
					for (Map.Entry<String, LexicalUnit> entry : customProp.entrySet()) {
						sm.putCustomProperty(entry.getKey(), entry.getValue());
					}
				}
			}
		} else {
			for (Rule rule : rules) {
				StyleRule sr = (StyleRule) rule;
				StyleDeclaration sd = sr.getStyleDeclaration();
				int len = sd.size();
				for (int i = 0; i < len; i++) {
					int idx = sd.getIndex(i);
					sm.putValue(idx, sd.getValue(i));
					sm.putImportant(idx, sd.getPriority(i));
					sm.putOrigin(idx, origin);
				}
			}
		}
	}

	/**
	 * Sorts the rules matching the element/pseudo-element of given style sheet to
	 * the list.
	 */
	protected void sortRules(ArrayList<Rule> rules, SelectorMatcher matcher) {
		int len = rules.size();
		Specificity[] specificities = new Specificity[len];
		for (int i = 0; i < len; i++) {
			StyleRule r = (StyleRule) rules.get(i);
			SelectorList sl = r.getSelectorList();
			Specificity mostSpecific = null;
			int slen = sl.getLength();
			for (int k = 0; k < slen; k++) {
				Selector s = sl.item(k);
				if (matcher.matches(s)) {
					Specificity specificity = new Specificity(s, matcher);
					if (mostSpecific == null) {
						mostSpecific = specificity;
					} else {
						int sp = Specificity.selectorCompare(specificity, mostSpecific);
						if (sp > 0) {
							mostSpecific = specificity;
						}
					}
				}
			}
			specificities[i] = mostSpecific;
		}
		for (int i = 1; i < len; i++) {
			Rule rule = rules.get(i);
			Specificity spec = specificities[i];
			int j = i - 1;
			while (j >= 0 && Specificity.selectorCompare(specificities[j], spec) > 0) {
				rules.set(j + 1, rules.get(j));
				specificities[j + 1] = specificities[j];
				j--;
			}
			rules.set(j + 1, rule);
			specificities[j + 1] = spec;
		}
	}

	/**
	 * To parse a style declaration.
	 */
	protected class StyleDeclarationDocumentHandler extends DocumentAdapter
			implements ShorthandManager.PropertyHandler {

		public StyleMap styleMap;

		@Override
		public void property(String name, LexicalUnit value, boolean important) {
			int i = getPropertyIndex(name);
			if (i == -1) {
				i = getShorthandIndex(name);
				if (i == -1) {
					// Unknown property
					return;
				}
				try {
					shorthandManagers[i].setValues(CSSEngine.this, this, value, important);
				} catch (Exception e) {
					if (userAgent == null)
						throw e;
					userAgent.displayError(e);
				}
			} else {
				Value v;
				try {
					v = valueManagers[i].createValue(value, CSSEngine.this);
				} catch (Exception e) {
					if (userAgent == null)
						throw e;
					userAgent.displayError(e);
					return;
				}
				putAuthorProperty(styleMap, i, v, important, StyleMap.INLINE_AUTHOR_ORIGIN);
			}
		}

		@Override
		public void lexicalProperty(String name, LexicalUnit value, boolean important) {
			styleMap.putCustomProperty(name, value);
		}

		@Override
		public void pendingValue(String name, PendingValue v, boolean important) {
			int idx = getPropertyIndex(name);
			if (idx != -1) { // line-height can be -1
				putAuthorProperty(styleMap, idx, v, important, StyleMap.INLINE_AUTHOR_ORIGIN);
			}
		}

	}

	/**
	 * To build a StyleDeclaration object.
	 */
	protected class StyleDeclarationBuilder extends DocumentAdapter implements ShorthandManager.PropertyHandler {

		public StyleDeclaration styleDeclaration;

		@Override
		public void property(String name, LexicalUnit value, boolean important) {
			int i = getPropertyIndex(name);
			if (i == -1) {
				i = getShorthandIndex(name);
				if (i == -1) {
					// Unknown property
					return;
				}
				shorthandManagers[i].setValues(CSSEngine.this, this, value, important);
			} else {
				Value v = valueManagers[i].createValue(value, CSSEngine.this);
				styleDeclaration.append(v, i, important);
			}
		}

		@Override
		public void lexicalProperty(String name, LexicalUnit value, boolean important) {
			styleDeclaration.setCustomProperty(name, value, important);
		}

		@Override
		public void pendingValue(String name, PendingValue value, boolean important) {
			int idx = getPropertyIndex(name);
			if (idx != -1) { // line-height can be -1
				styleDeclaration.append(value, idx, important);
			}
		}

	}

	/**
	 * To parse a style sheet.
	 */
	protected class StyleSheetDocumentHandler extends DocumentAdapter implements ShorthandManager.PropertyHandler {

		public StyleSheet styleSheet;
		protected StyleRule styleRule;
		protected StyleDeclaration styleDeclaration;

		private PropertyDefinitionImpl currentPropertyDefinition = null;

		private int ignoredForRule = 0;

		@Override
		public void parseStart(ParserControl parserctl) {
		}

		@Override
		public void endOfStream() {
		}

		@Override
		public void ignorableAtRule(String atRule) {
		}

		@Override
		public void namespaceDeclaration(String prefix, String uri) {
		}

		@Override
		public void startCounterStyle(String name) {
			if (ignoredForRule == 0) {
				ignoredForRule = CSSRule.COUNTER_STYLE_RULE;
			}
		}

		@Override
		public void endCounterStyle() {
			if (ignoredForRule == CSSRule.COUNTER_STYLE_RULE) {
				ignoredForRule = 0;
			}
		}

		@Override
		public void importStyle(String uri, MediaQueryList media, String defaultNamespaceURI) {
			if (ignoredForRule > 0) {
				return;
			}

			ImportRule ir = new ImportRule();
			ir.setMediaList(media);
			ir.setParent(styleSheet);
			ParsedURL base = getCSSBaseURI();
			ParsedURL url;
			if (base == null) {
				url = new ParsedURL(uri);
			} else {
				url = new ParsedURL(base, uri);
			}
			ir.setURI(url);
			styleSheet.append(ir);
		}

		@Override
		public void startMedia(MediaQueryList media) {
			if (ignoredForRule > 0) {
				return;
			}

			MediaRule mr = new MediaRule();
			mr.setMediaList(media);
			mr.setParent(styleSheet);
			styleSheet.append(mr);
			styleSheet = mr;
		}

		@Override
		public void endMedia(MediaQueryList media) {
			if (ignoredForRule > 0) {
				return;
			}

			styleSheet = styleSheet.getParent();
		}

		@Override
		public void startPage(PageSelectorList pageSelectorList) {
			if (ignoredForRule == 0) {
				ignoredForRule = CSSRule.PAGE_RULE;
			}
		}

		@Override
		public void endPage(PageSelectorList pageSelectorList) {
			if (ignoredForRule == CSSRule.PAGE_RULE) {
				ignoredForRule = 0;
			}
		}

		@Override
		public void startFontFace() {
			if (ignoredForRule > 0) {
				return;
			}

			styleDeclaration = new StyleDeclaration();
		}

		@Override
		public void endFontFace() {
			if (ignoredForRule > 0) {
				return;
			}

			StyleMap sm = new StyleMap(getNumberOfProperties());
			int len = styleDeclaration.size();
			for (int i = 0; i < len; i++) {
				int idx = styleDeclaration.getIndex(i);
				sm.putValue(idx, styleDeclaration.getValue(i));
				sm.putImportant(idx, styleDeclaration.getPriority(i));
				// Not sure on this..
				sm.putOrigin(idx, StyleMap.AUTHOR_ORIGIN);
			}
			styleDeclaration = null;

			int pidx = getPropertyIndex(CSSConstants.CSS_FONT_FAMILY_PROPERTY);
			Value fontFamily = sm.getValue(pidx);
			if (fontFamily == null)
				return;

			ParsedURL base = getCSSBaseURI();
			fontFaces.add(new FontFaceRule(sm, base));
		}

		@Override
		public void startMargin(String name) {
			// ignoredForRule = CSSRule.PAGE_RULE
		}

		@Override
		public void endMargin() {
			// ignoredForRule = CSSRule.PAGE_RULE
		}

		@Override
		public void startKeyframes(String name) {
			if (ignoredForRule == 0) {
				ignoredForRule = CSSRule.KEYFRAMES_RULE;
			}
		}

		@Override
		public void endKeyframes() {
			if (ignoredForRule == CSSRule.KEYFRAMES_RULE) {
				ignoredForRule = 0;
			}
		}

		@Override
		public void startKeyframe(LexicalUnit keyframeSelector) {
			// ignoredForRule = CSSRule.KEYFRAMES_RULE
		}

		@Override
		public void endKeyframe() {
			// ignoredForRule = CSSRule.KEYFRAMES_RULE
		}

		@Override
		public void startFontFeatures(String[] familyName) {
			if (ignoredForRule == 0) {
				ignoredForRule = CSSRule.FONT_FEATURE_VALUES_RULE;
			}
		}

		@Override
		public void endFontFeatures() {
			if (ignoredForRule == CSSRule.FONT_FEATURE_VALUES_RULE) {
				ignoredForRule = 0;
			}
		}

		@Override
		public void startFeatureMap(String mapName) {
			// ignoredForRule = CSSRule.FONT_FEATURE_VALUES_RULE
		}

		@Override
		public void endFeatureMap() {
			// ignoredForRule = CSSRule.FONT_FEATURE_VALUES_RULE
		}

		@Override
		public void startProperty(String name) {
			if (ignoredForRule == 0) {
				ignoredForRule = CSSRule.PROPERTY_RULE;
			} else {
				return;
			}

			if (propertyDefinitionMap == null) {
				propertyDefinitionMap = new HashMap<>();
			}

			currentPropertyDefinition = new PropertyDefinitionImpl(name);
		}

		@Override
		public void endProperty(boolean discard) {
			if (ignoredForRule != CSSRule.PROPERTY_RULE) {
				return;
			}

			if (!discard) {
				propertyDefinitionMap.put(currentPropertyDefinition.getName(), currentPropertyDefinition);
			}

			currentPropertyDefinition = null;
			ignoredForRule = 0;
		}

		@Override
		public void startSupports(BooleanCondition condition) {
			if (ignoredForRule > 0) {
				return;
			}

			SupportsRule sr = new SupportsRule(condition);

			setSupports(sr);

			sr.setParent(styleSheet);
			styleSheet.append(sr);
			styleSheet = sr;
		}

		@Override
		public void endSupports(BooleanCondition condition) {
			if (ignoredForRule > 0) {
				return;
			}

			styleSheet = styleSheet.getParent();
		}

		@Override
		public void startViewport() {
			if (ignoredForRule == 0) {
				ignoredForRule = CSSRule.VIEWPORT_RULE;
			}
		}

		@Override
		public void endViewport() {
			if (ignoredForRule == CSSRule.VIEWPORT_RULE) {
				ignoredForRule = 0;
			}
		}

		@Override
		public void startSelector(SelectorList selectors) {
			if (ignoredForRule > 0) {
				return;
			}

			styleRule = new StyleRule();
			styleRule.setSelectorList(selectors);
			styleDeclaration = new StyleDeclaration();
			styleRule.setStyleDeclaration(styleDeclaration);
			styleSheet.append(styleRule);
		}

		@Override
		public void endSelector(SelectorList selectors) {
			styleRule = null;
			styleDeclaration = null;
		}

		@Override
		public void property(String name, LexicalUnit value, boolean important) {
			if (ignoredForRule > 0) {
				return;
			}

			int i = getPropertyIndex(name);
			if (i == -1) {
				i = getShorthandIndex(name);
				if (i == -1) {
					// Unknown property
					return;
				}
				try {
					shorthandManagers[i].setValues(CSSEngine.this, this, value, important);
				} catch (Exception e) {
					if (userAgent == null)
						throw e;
					userAgent.displayError(e);
				}
			} else {
				Value v;
				try {
					v = valueManagers[i].createValue(value, CSSEngine.this);
				} catch (Exception e) {
					if (userAgent == null)
						throw e;
					userAgent.displayError(e);
					return;
				}
				styleDeclaration.append(v, i, important);
			}
		}

		@Override
		public void lexicalProperty(String name, LexicalUnit value, boolean important) {
			if (ignoredForRule == CSSRule.PROPERTY_RULE) {
				propertyRuleDescriptor(name, value, important);
				return;
			} else if (ignoredForRule > 0) {
				return;
			}

			styleDeclaration.setCustomProperty(name, value, important);
		}

		private void propertyRuleDescriptor(String name, LexicalUnit value, boolean important) {
			switch (name) {
			case "inherits":
				currentPropertyDefinition.setInherits(!"false".equalsIgnoreCase(value.getStringValue()));
				break;
			case "initial-value":
				currentPropertyDefinition.setInitialValue(value);
				break;
			case "syntax":
				String s = value.getStringValue();
				if (s == null) {
					s = "*";
				}
				SyntaxParser synParser = new SyntaxParser();
				CSSValueSyntax syn;
				try {
					syn = synParser.parseSyntax(s);
				} catch (Exception e) {
					syn = synParser.parseSyntax("*");
				}
				currentPropertyDefinition.setSyntax(syn);
				break;
			}
		}

		@Override
		public void pendingValue(String name, PendingValue v, boolean important) {
			if (ignoredForRule > 0) {
				return;
			}

			int i = getPropertyIndex(name);
			if (i != -1) { // line-height can be -1
				styleDeclaration.append(v, i, important);
			}
		}

	}

	/**
	 * Provides an (empty) adapter for the DocumentHandler interface. Most methods
	 * just throw an UnsupportedOperationException, so the subclasses <i>must</i>
	 * override them with 'real' methods.
	 */
	protected static class DocumentAdapter implements CSSHandler {

		@Override
		public void parseStart(ParserControl parserctl) {
		}

		@Override
		public void endOfStream() {
		}

		@Override
		public void comment(String text, boolean precededByLF) {
			// We always ignore the comments.
		}

		@Override
		public void ignorableAtRule(String atRule) {
			throwUnsupportedEx();
		}

		@Override
		public void namespaceDeclaration(String prefix, String uri) {
			throwUnsupportedEx();
		}

		@Override
		public void importStyle(String uri, MediaQueryList media, String defaultNamespaceURI) {
			throwUnsupportedEx();
		}

		@Override
		public void startMedia(MediaQueryList media) {
			throwUnsupportedEx();
		}

		@Override
		public void endMedia(MediaQueryList media) {
			throwUnsupportedEx();
		}

		@Override
		public void startPage(PageSelectorList pageSelectorList) {
			throwUnsupportedEx();
		}

		@Override
		public void endPage(PageSelectorList pageSelectorList) {
			throwUnsupportedEx();
		}

		@Override
		public void startFontFace() {
			throwUnsupportedEx();
		}

		@Override
		public void endFontFace() {
			throwUnsupportedEx();
		}

		@Override
		public void startMargin(String name) {
			throwUnsupportedEx();
		}

		@Override
		public void endMargin() {
			throwUnsupportedEx();
		}

		@Override
		public void startCounterStyle(String name) {
			throwUnsupportedEx();
		}

		@Override
		public void endCounterStyle() {
			throwUnsupportedEx();
		}

		@Override
		public void startKeyframes(String name) {
			throwUnsupportedEx();
		}

		@Override
		public void endKeyframes() {
			throwUnsupportedEx();
		}

		@Override
		public void startKeyframe(LexicalUnit keyframeSelector) {
			throwUnsupportedEx();
		}

		@Override
		public void endKeyframe() {
			throwUnsupportedEx();
		}

		@Override
		public void startFontFeatures(String[] familyName) {
			throwUnsupportedEx();
		}

		@Override
		public void endFontFeatures() {
			throwUnsupportedEx();
		}

		@Override
		public void startFeatureMap(String mapName) {
			throwUnsupportedEx();
		}

		@Override
		public void endFeatureMap() {
			throwUnsupportedEx();
		}

		@Override
		public void startProperty(String name) {
			throwUnsupportedEx();
		}

		@Override
		public void endProperty(boolean discard) {
			throwUnsupportedEx();
		}

		@Override
		public void startSupports(BooleanCondition condition) {
			throwUnsupportedEx();
		}

		@Override
		public void endSupports(BooleanCondition condition) {
			throwUnsupportedEx();
		}

		@Override
		public void startViewport() {
			throwUnsupportedEx();
		}

		@Override
		public void endViewport() {
			throwUnsupportedEx();
		}

		@Override
		public void startSelector(SelectorList selectors) {
			throwUnsupportedEx();
		}

		@Override
		public void endSelector(SelectorList selectors) {
			throwUnsupportedEx();
		}

		@Override
		public void property(String name, LexicalUnit value, boolean important) {
			throwUnsupportedEx();
		}

		@Override
		public void lexicalProperty(String name, LexicalUnit value, boolean important) {
			// Ignore custom properties
		}

		private void throwUnsupportedEx() {
			throw new UnsupportedOperationException("you try to use an empty method in Adapter-class");
		}

	}

	// CSS events /////////////////////////////////////////////////////////

	protected static final CSSEngineListener[] LISTENER_ARRAY = {};

	/**
	 * Adds a CSS engine listener.
	 */
	public void addCSSEngineListener(CSSEngineListener l) {
		listeners.add(l);
	}

	/**
	 * Removes a CSS engine listener.
	 */
	public void removeCSSEngineListener(CSSEngineListener l) {
		listeners.remove(l);
	}

	/**
	 * Fires a CSSEngineEvent, given a list of modified properties.
	 */
	protected void firePropertiesChangedEvent(Element target, int[] props) {
		CSSEngineListener[] ll = listeners.toArray(LISTENER_ARRAY);

		int len = ll.length;
		if (len > 0) {
			CSSEngineEvent evt = new CSSEngineEvent(this, target, props);
			for (CSSEngineListener aLl : ll) {
				aLl.propertiesChanged(evt);
			}
		}
	}

	// Dynamic updates ////////////////////////////////////////////////////

	/**
	 * Called when the inline style of the given element has been updated.
	 */
	protected void inlineStyleAttributeUpdated(CSSStylableElement elt, StyleMap style, short attrChange,
			String prevValue, String newValue) {
		boolean[] updated = styleDeclarationUpdateHandler.updatedProperties;
		for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
			updated[i] = false;
		}

		switch (attrChange) {
		case MutationEvent.ADDITION: // intentional fall-through
		case MutationEvent.MODIFICATION:
			if (newValue.length() > 0) {
				element = elt;
				try {
					styleDeclarationUpdateHandler.styleMap = style;
					parser.setDocumentHandler(styleDeclarationUpdateHandler);
					parser.parseStyleDeclaration(new StringReader(newValue));
					styleDeclarationUpdateHandler.styleMap = null;
				} catch (Exception e) {
					String m = e.getMessage();
					if (m == null)
						m = "";
					String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
					String s = Messages.formatMessage("style.syntax.error.at",
							new Object[] { u, styleLocalName, newValue, m });
					DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
					de.initCause(e);
					if (userAgent == null)
						throw de;
					userAgent.displayError(de);
				} finally {
					element = null;
					cssBaseURI = null;
				}
			}

			// Fall through
		case MutationEvent.REMOVAL:
			boolean removed = false;

			if (prevValue != null && prevValue.length() > 0) {
				// Check if the style map has cascaded styles which
				// come from the inline style attribute or override style.
				for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
					if (style.isComputed(i) && !updated[i]) {
						int origin = style.getOrigin(i);
						if (origin >= StyleMap.INLINE_AUTHOR_ORIGIN) { // ToDo Jlint says: always same result ??
							removed = true;
							updated[i] = true;
						}
					}
				}
			}

			if (removed) {
				invalidateProperties(elt, null, updated, true);
			} else {
				int count = 0;
				// Invalidate the relative values
				boolean fs = (fontSizeIndex == -1) ? false : updated[fontSizeIndex];
				boolean lh = (lineHeightIndex == -1) ? false : updated[lineHeightIndex];
				boolean cl = (colorIndex == -1) ? false : updated[colorIndex];
				boolean isRoot = elt.getOwnerDocument().getDocumentElement() == elt;
				boolean cp = styleDeclarationUpdateHandler.updatedCustomProperties;

				for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
					if (updated[i]) {
						count++;
					} else if ((fs && style.isFontSizeRelative(i)) || (lh && style.isLineHeightRelative(i))
							|| (cl && style.isColorRelative(i)) || (fs && isRoot && style.isRootFontSizeRelative(i))
							|| (lh && isRoot && style.isRootLineHeightRelative(i))
							|| (cp && style.isCustomPtyRelative(i))) {
						updated[i] = true;
						clearComputedValue(style, i);
						count++;
					}
				}

				if (count > 0) {
					int[] props = new int[count];
					count = 0;
					for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
						if (updated[i]) {
							props[count++] = i;
						}
					}
					invalidateProperties(elt, props, null, true);
				}
			}
			break;

		default:
			// Must not happen
			throw new IllegalStateException("Invalid attrChangeType");
		}
	}

	private static void clearComputedValue(StyleMap style, int n) {
		if (style.isNullCascaded(n)) {
			style.putValue(n, null);
		} else {
			Value v = style.getValue(n);
			if (v instanceof ComputedValue) {
				ComputedValue cv = (ComputedValue) v;
				v = cv.getCascadedValue();
				style.putValue(n, v);
			}
		}
		style.putComputed(n, false);
	}

	/**
	 * Invalidates all the properties of the given node.
	 */
	protected void invalidateProperties(Node node, int[] properties, boolean[] updated, boolean recascade) {

		if (!(node instanceof CSSStylableElement))
			return; // Not Stylable sub tree

		CSSStylableElement elt = (CSSStylableElement) node;
		StyleMap style = elt.getComputedStyleMap(null);
		if (style == null)
			return; // Nothing to invalidate.

		boolean[] diffs = new boolean[getNumberOfProperties()];
		if (updated != null) {
			System.arraycopy(updated, 0, diffs, 0, updated.length);
		}
		if (properties != null) {
			for (int property : properties) {
				diffs[property] = true;
			}
		}
		int count = 0;
		if (!recascade) {
			for (boolean diff : diffs) {
				if (diff) {
					count++;
				}
			}
		} else {
			StyleMap newStyle = getCascadedStyleMap(elt, null);
			elt.setComputedStyleMap(null, newStyle);
			for (int i = 0; i < diffs.length; i++) {
				if (diffs[i]) {
					count++;
					continue; // Already marked changed.
				}

				// Value nv = getComputedStyle(elt, null, i);
				Value nv = newStyle.getValue(i);
				Value ov = null;
				if (!style.isNullCascaded(i)) {
					ov = style.getValue(i);
					if (ov instanceof ComputedValue) {
						ov = ((ComputedValue) ov).getCascadedValue();
					}
				}

				if (nv == ov)
					continue;
				if ((nv != null) && (ov != null)) {
					if (nv.equals(ov))
						continue;
					String ovCssText = ov.getCssText();
					String nvCssText = nv.getCssText();
					if ((nvCssText == ovCssText) || ((nvCssText != null) && nvCssText.equals(ovCssText)))
						continue;
				}
				count++;
				diffs[i] = true;
			}
		}
		int[] props = null;
		if (count != 0) {
			props = new int[count];
			count = 0;
			for (int i = 0; i < diffs.length; i++) {
				if (diffs[i])
					props[count++] = i;
			}
		}
		propagateChanges(elt, props, recascade);
	}

	/**
	 * Propagates the changes that occurs on the parent of the given node. Props is
	 * a list of known 'changed' properties. If recascade is true then the
	 * stylesheets will be applied again to see if the any new rules apply (or old
	 * rules don't apply).
	 */
	protected void propagateChanges(Node node, int[] props, boolean recascade) {
		if (!(node instanceof CSSStylableElement))
			return;
		CSSStylableElement elt = (CSSStylableElement) node;
		StyleMap style = elt.getComputedStyleMap(null);
		if (style != null) {
			boolean[] updated = styleDeclarationUpdateHandler.updatedProperties;
			for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
				updated[i] = false;
			}
			if (props != null) {
				for (int i = props.length - 1; i >= 0; --i) {
					int idx = props[i];
					updated[idx] = true;
				}
			}

			// Invalidate the relative values
			boolean fs = (fontSizeIndex == -1) ? false : updated[fontSizeIndex];
			boolean lh = (lineHeightIndex == -1) ? false : updated[lineHeightIndex];
			boolean cl = (colorIndex == -1) ? false : updated[colorIndex];
			boolean isRootFs = fs && elt.getOwnerDocument().getDocumentElement() == elt;
			boolean cp = styleDeclarationUpdateHandler.updatedCustomProperties;

			int count = 0;
			for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
				if (updated[i]) {
					count++;
				} else if ((fs && style.isFontSizeRelative(i)) || (lh && style.isLineHeightRelative(i))
						|| (cl && style.isColorRelative(i)) || (isRootFs && style.isRootFontSizeRelative(i))
						|| (cp && style.isCustomPtyRelative(i))) {
					updated[i] = true;
					clearComputedValue(style, i);
					count++;
				}
			}

			if (count == 0) {
				props = null;
			} else {
				props = new int[count];
				count = 0;
				for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
					if (updated[i]) {
						props[count++] = i;
					}
				}
				firePropertiesChangedEvent(elt, props);
			}
		}

		int[] inherited = props;
		if (props != null) {
			// Filter out uninheritable properties when we
			// propogate to children.
			int count = 0;
			for (int i = 0; i < props.length; i++) {
				ValueManager vm = valueManagers[props[i]];
				if (vm.isInheritedProperty())
					count++;
				else
					props[i] = -1;
			}

			if (count == 0) {
				// nothing to propogate for sure
				inherited = null;
			} else {
				inherited = new int[count];
				count = 0;
				for (int prop : props)
					if (prop != -1)
						inherited[count++] = prop;
			}
		}

		for (Node n = getCSSFirstChild(node); n != null; n = getCSSNextSibling(n)) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				// XXX Should this invalidateProperties be called on eng?
				// In r216064 with CSSImportedElementRoot, the imported
				// element's CSSEngine was indeed used.
				// CSSEngine eng = cssContext.getCSSEngineForElement((Element) n);
				invalidateProperties(n, inherited, null, recascade);
			}
		}
	}

	/**
	 * To parse a style declaration and update a StyleMap.
	 */
	protected class StyleDeclarationUpdateHandler extends DocumentAdapter implements ShorthandManager.PropertyHandler {

		public StyleMap styleMap;
		public boolean[] updatedProperties = new boolean[getNumberOfProperties()];
		public boolean updatedCustomProperties;

		/**
		 * <b>SAC</b>: Implements
		 * {@link CSSHandler#property(String,LexicalUnit,boolean)}.
		 */
		@Override
		public void property(String name, LexicalUnit value, boolean important) {
			int i = getPropertyIndex(name);
			if (i == -1) {
				i = getShorthandIndex(name);
				if (i == -1) {
					// Unknown property
					return;
				}
				shorthandManagers[i].setValues(CSSEngine.this, this, value, important);
			} else {
				if (styleMap.isImportant(i)) {
					// The previous value is important, and a value
					// from a style attribute cannot be important...
					return;
				}

				updatedProperties[i] = true;

				Value v = valueManagers[i].createValue(value, CSSEngine.this);
				styleMap.putMask(i, 0);
				styleMap.putValue(i, v);
				styleMap.putOrigin(i, StyleMap.INLINE_AUTHOR_ORIGIN);
			}
		}

		@Override
		public void lexicalProperty(String name, LexicalUnit value, boolean important) {
			updatedCustomProperties = true;
		}

		@Override
		public void pendingValue(String name, PendingValue v, boolean important) {
			int i = getPropertyIndex(name);
			if (styleMap.isImportant(i)) {
				// The previous value is important, and a value
				// from a style attribute cannot be important...
				return;
			}

			updatedProperties[i] = true;

			styleMap.putMask(i, 0);
			styleMap.putValue(i, v);
			styleMap.putOrigin(i, StyleMap.INLINE_AUTHOR_ORIGIN);
		}

	}

	/**
	 * Called when a non-CSS presentational hint has been updated.
	 */
	protected void nonCSSPresentationalHintUpdated(CSSStylableElement elt, StyleMap style, String property,
			short attrChange, String newValue) {
		int idx = getPropertyIndex(property);

		if (style.isImportant(idx)) {
			// The current value is important, and a value
			// from an XML attribute cannot be important...
			return;
		}

		if (style.getOrigin(idx) >= StyleMap.AUTHOR_ORIGIN) {
			// The current value has a greater priority
			return;
		}

		switch (attrChange) {
		case MutationEvent.ADDITION: // intentional fall-through
		case MutationEvent.MODIFICATION:
			element = elt;
			try {
				LexicalUnit lu = parser.parsePropertyValue(new StringReader(newValue));
				ValueManager vm = valueManagers[idx];
				Value v = vm.createValue(lu, CSSEngine.this);
				style.putMask(idx, 0);
				style.putValue(idx, v);
				style.putOrigin(idx, StyleMap.NON_CSS_ORIGIN);
			} catch (Exception e) {
				String m = e.getMessage();
				if (m == null)
					m = "";
				String u = ((documentURI == null) ? "<unknown>" : documentURI.toString());
				String s = Messages.formatMessage("property.syntax.error.at",
						new Object[] { u, property, newValue, m });
				DOMException de = new DOMException(DOMException.SYNTAX_ERR, s);
				de.initCause(e);
				if (userAgent == null)
					throw de;
				userAgent.displayError(de);
			} finally {
				element = null;
				cssBaseURI = null;
			}
			break;

		case MutationEvent.REMOVAL: {
			int[] invalid = { idx };
			invalidateProperties(elt, invalid, null, true);
			return;
		}
		}

		boolean[] updated = styleDeclarationUpdateHandler.updatedProperties;
		for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
			updated[i] = false;
		}
		updated[idx] = true;

		// Invalidate the relative values
		boolean fs = idx == fontSizeIndex;
		boolean lh = idx == lineHeightIndex;
		boolean cl = idx == colorIndex;
		boolean isRootFs = fs && elt.getOwnerDocument().getDocumentElement() == elt;
		int count = 0;

		for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
			if (updated[i]) {
				count++;
			} else if ((fs && style.isFontSizeRelative(i)) || (lh && style.isLineHeightRelative(i))
					|| (cl && style.isColorRelative(i)) || (isRootFs && style.isRootFontSizeRelative(i))) {
				updated[i] = true;
				clearComputedValue(style, i);
				count++;
			}
		}

		int[] props = new int[count];
		count = 0;
		for (int i = getNumberOfProperties() - 1; i >= 0; --i) {
			if (updated[i]) {
				props[count++] = i;
			}
		}

		invalidateProperties(elt, props, null, true);
	}

	/**
	 * Returns whether the DOM subtree rooted at the specified node contains a
	 * {@link CSSStyleSheetNode}.
	 */
	protected boolean hasStyleSheetNode(Node n) {
		if (n instanceof CSSStyleSheetNode) {
			return true;
		}
		n = getCSSFirstChild(n);
		while (n != null) {
			if (hasStyleSheetNode(n)) {
				return true;
			}
			n = getCSSNextSibling(n);
		}
		return false;
	}

	/**
	 * Handles an attribute change in the document.
	 */
	protected void handleAttrModified(Element e, Attr attr, short attrChange, String prevValue, String newValue) {
		if (!(e instanceof CSSStylableElement) || newValue.equals(prevValue)) {
			return; // Either not a stylable element or no actual change
		}

		String attrNS = attr.getNamespaceURI();
		String name = attrNS == null ? attr.getNodeName() : attr.getLocalName();

		CSSStylableElement elt = (CSSStylableElement) e;
		StyleMap style = elt.getComputedStyleMap(null);
		if (style != null) {
			if ((attrNS == styleNamespaceURI || attrNS != null && attrNS.equals(styleNamespaceURI))
					&& name.equals(styleLocalName)) {
				// The style declaration attribute has been modified.
				inlineStyleAttributeUpdated(elt, style, attrChange, prevValue, newValue);
				return;
			}

			if ((nonCSSPresentationalHints != null) && (attrNS == nonCSSPresentationalHintsNamespaceURI
					|| attrNS != null && attrNS.equals(nonCSSPresentationalHintsNamespaceURI))) {
				if (nonCSSPresentationalHints.contains(name)) {
					// The 'name' attribute which represents a non CSS
					// presentational hint has been modified.
					nonCSSPresentationalHintUpdated(elt, style, name, attrChange, newValue);
					return;
				}
			}
		}

		if (selectorAttributes != null && selectorAttributes.contains(name)) {
			// An attribute has been modified, invalidate all the
			// properties to correctly match attribute selectors.
			invalidateProperties(elt, null, null, true);
			for (Node n = getCSSNextSibling(elt); n != null; n = getCSSNextSibling(n)) {
				invalidateProperties(n, null, null, true);
			}
		}
	}

	/**
	 * Handles a node insertion in the document.
	 */
	protected void handleNodeInserted(Node n) {
		if (hasStyleSheetNode(n)) {
			// Invalidate all the CSSStylableElements in the document.
			styleSheetNodes = null;
			invalidateProperties(document.getDocumentElement(), null, null, true);
		} else if (n instanceof CSSStylableElement) {
			// Invalidate the CSSStylableElement siblings, to correctly
			// match the adjacent selectors and first-child pseudo-class.
			n = getCSSNextSibling(n);
			while (n != null) {
				invalidateProperties(n, null, null, true);
				n = getCSSNextSibling(n);
			}
		}
	}

	/**
	 * Handles a node removal from the document.
	 */
	protected void handleNodeRemoved(Node n) {
		if (hasStyleSheetNode(n)) {
			// Wait for the DOMSubtreeModified to do the invalidations
			// because at this time the node is in the tree.
			styleSheetRemoved = true;
		} else if (n instanceof CSSStylableElement) {
			// Wait for the DOMSubtreeModified to do the invalidations
			// because at this time the node is in the tree.
			removedStylableElementSibling = getCSSNextSibling(n);
		}
		// Clears the computed styles in the removed tree.
		disposeStyleMaps(n);
	}

	/**
	 * Handles a subtree modification in the document. todo the incoming Node is
	 * actually ignored (not used) here, but it seems caller-sites assume that it is
	 * used - is this done right??
	 */
	protected void handleSubtreeModified(Node ignored) {
		if (styleSheetRemoved) {
			// Invalidate all the CSSStylableElements in the document.
			styleSheetRemoved = false;
			styleSheetNodes = null;
			invalidateProperties(document.getDocumentElement(), null, null, true);
		} else if (removedStylableElementSibling != null) {
			// Invalidate the CSSStylableElement siblings, to
			// correctly match the adjacent selectors and
			// first-child pseudo-class.
			Node n = removedStylableElementSibling;
			while (n != null) {
				invalidateProperties(n, null, null, true);
				n = getCSSNextSibling(n);
			}
			removedStylableElementSibling = null;
		}
	}

	/**
	 * Handles a character data modification in the document.
	 */
	protected void handleCharacterDataModified(Node n) {
		if (getCSSParentNode(n) instanceof CSSStyleSheetNode) {
			// Invalidate all the CSSStylableElements in the document.
			styleSheetNodes = null;
			invalidateProperties(document.getDocumentElement(), null, null, true);
		}
	}

	/**
	 * To handle mutations of a CSSNavigableDocument.
	 */
	protected class CSSNavigableDocumentHandler implements CSSNavigableDocumentListener, MainPropertyReceiver {

		/**
		 * Array to hold which properties have been changed by a call to
		 * setMainProperties.
		 */
		protected boolean[] mainPropertiesChanged;

		/**
		 * The StyleDeclaration to use from the MainPropertyReceiver.
		 */
		protected StyleDeclaration declaration;

		/**
		 * A node has been inserted into the CSSNavigableDocument tree.
		 */
		@Override
		public void nodeInserted(Node newNode) {
			handleNodeInserted(newNode);
		}

		/**
		 * A node is about to be removed from the CSSNavigableDocument tree.
		 */
		@Override
		public void nodeToBeRemoved(Node oldNode) {
			handleNodeRemoved(oldNode);
		}

		/**
		 * A subtree of the CSSNavigableDocument tree has been modified in some way.
		 */
		@Override
		public void subtreeModified(Node rootOfModifications) {
			handleSubtreeModified(rootOfModifications);
		}

		/**
		 * Character data in the CSSNavigableDocument tree has been modified.
		 */
		@Override
		public void characterDataModified(Node text) {
			handleCharacterDataModified(text);
		}

		/**
		 * An attribute has changed in the CSSNavigableDocument.
		 */
		@Override
		public void attrModified(Element e, Attr attr, short attrChange, String prevValue, String newValue) {
			handleAttrModified(e, attr, attrChange, prevValue, newValue);
		}

		/**
		 * The text of the override style declaration for this element has been
		 * modified.
		 */
		@Override
		public void overrideStyleTextChanged(CSSStylableElement elt, String text) {
			StyleDeclarationProvider p = elt.getOverrideStyleDeclarationProvider();
			StyleDeclaration declaration = p.getStyleDeclaration();
			int ds = declaration.size();
			boolean[] updated = new boolean[getNumberOfProperties()];
			for (int i = 0; i < ds; i++) {
				updated[declaration.getIndex(i)] = true;
			}
			declaration = parseStyleDeclaration(elt, text);
			p.setStyleDeclaration(declaration);
			ds = declaration.size();
			for (int i = 0; i < ds; i++) {
				updated[declaration.getIndex(i)] = true;
			}
			invalidateProperties(elt, null, updated, true);
		}

		/**
		 * A property in the override style declaration has been removed.
		 */
		@Override
		public void overrideStylePropertyRemoved(CSSStylableElement elt, String name) {
			StyleDeclarationProvider p = elt.getOverrideStyleDeclarationProvider();
			StyleDeclaration declaration = p.getStyleDeclaration();
			int idx = getPropertyIndex(name);
			int ds = declaration.size();
			for (int i = 0; i < ds; i++) {
				if (idx == declaration.getIndex(i)) {
					declaration.remove(i);
					StyleMap style = elt.getComputedStyleMap(null);
					if (style != null && style.getOrigin(idx) == StyleMap.OVERRIDE_ORIGIN
					/* && style.isComputed(idx) */) {
						invalidateProperties(elt, new int[] { idx }, null, true);
					}
					break;
				}
			}
		}

		/**
		 * A property in the override style declaration has been changed.
		 */
		@Override
		public void overrideStylePropertyChanged(CSSStylableElement elt, String name, String val, String prio) {
			boolean important = prio != null && prio.length() != 0;
			StyleDeclarationProvider p = elt.getOverrideStyleDeclarationProvider();
			declaration = p.getStyleDeclaration();
			setMainProperties(elt, this, name, val, important);
			declaration = null;
			invalidateProperties(elt, null, mainPropertiesChanged, true);
		}

		// MainPropertyReceiver //////////////////////////////////////////////

		/**
		 * Sets a main property value in response to a shorthand property being set.
		 */
		@Override
		public void setMainProperty(String name, Value v, boolean important) {
			int idx = getPropertyIndex(name);
			if (idx == -1) {
				return; // unknown property
			}

			int i;
			for (i = 0; i < declaration.size(); i++) {
				if (idx == declaration.getIndex(i)) {
					break;
				}
			}
			if (i < declaration.size()) {
				declaration.put(i, v, idx, important);
			} else {
				declaration.append(v, idx, important);
			}
		}

	}

	/**
	 * To handle the insertion of a CSSStyleSheetNode in the associated document.
	 */
	protected class DOMNodeInsertedListener implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			handleNodeInserted((Node) evt.getTarget());
		}

	}

	/**
	 * To handle the removal of a CSSStyleSheetNode from the associated document.
	 */
	protected class DOMNodeRemovedListener implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			handleNodeRemoved((Node) evt.getTarget());
		}

	}

	/**
	 * To handle the removal of a CSSStyleSheetNode from the associated document.
	 */
	protected class DOMSubtreeModifiedListener implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			handleSubtreeModified((Node) evt.getTarget());
		}

	}

	/**
	 * To handle the modification of a CSSStyleSheetNode.
	 */
	protected class DOMCharacterDataModifiedListener implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			handleCharacterDataModified((Node) evt.getTarget());
		}

	}

	/**
	 * To handle the element attributes modification in the associated document.
	 */
	protected class DOMAttrModifiedListener implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			MutationEvent mevt = (MutationEvent) evt;
			handleAttrModified((Element) evt.getTarget(), (Attr) mevt.getRelatedNode(), mevt.getAttrChange(),
					mevt.getPrevValue(), mevt.getNewValue());
		}

	}

}
