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
package io.sf.carte.echosvg.anim.dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.w3c.css.om.CSSStyleDeclaration;
import org.w3c.css.om.CSSStyleSheet;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.view.ViewCSS;

import io.sf.carte.doc.style.css.nsac.InputSource;
import io.sf.carte.doc.style.css.nsac.Parser;
import io.sf.carte.echosvg.css.dom.CSSOMSVGViewCSS;
import io.sf.carte.echosvg.css.engine.CSSContext;
import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.SVGCSSEngine;
import io.sf.carte.echosvg.css.engine.value.ShorthandManager;
import io.sf.carte.echosvg.css.engine.value.ValueManager;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.AbstractStylableDocument;
import io.sf.carte.echosvg.dom.ExtensibleDOMImplementation;
import io.sf.carte.echosvg.dom.GenericElement;
import io.sf.carte.echosvg.dom.events.DOMTimeEvent;
import io.sf.carte.echosvg.dom.events.DocumentEventSupport;
import io.sf.carte.echosvg.dom.svg.SVGOMEvent;
import io.sf.carte.echosvg.dom.util.CSSStyleDeclarationFactory;
import io.sf.carte.echosvg.dom.util.DOMUtilities;
import io.sf.carte.echosvg.i18n.LocalizableSupport;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This class implements the {@link DOMImplementation} interface. It provides
 * support the SVG 1.1 documents.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGDOMImplementation extends ExtensibleDOMImplementation implements CSSStyleDeclarationFactory {

	private static final long serialVersionUID = 1L;

	/**
	 * The SVG namespace uri.
	 */
	public static final String SVG_NAMESPACE_URI = SVGConstants.SVG_NAMESPACE_URI;

	/**
	 * The error messages bundle class name.
	 */
	protected static final String RESOURCES = "io.sf.carte.echosvg.dom.svg.resources.Messages";

	protected HashMap<String, ElementFactory> factories;

	/**
	 * Returns the default instance of this class.
	 */
	public static DOMImplementation getDOMImplementation() {
		return DOM_IMPLEMENTATION;
	}

	/**
	 * Creates a new SVGDOMImplementation object.
	 */
	public SVGDOMImplementation() {
		factories = svgFactories;
		registerFeature("CSS", "2.0");
		registerFeature("StyleSheets", "2.0");
		registerFeature("SVG", new String[] { "1.0", "1.1" });
		registerFeature("SVGEvents", new String[] { "1.0", "1.1" });
	}

	@Override
	protected void initLocalizable() {
		localizableSupport = new LocalizableSupport(RESOURCES, getClass().getClassLoader());
	}

	@Override
	public CSSEngine createCSSEngine(AbstractStylableDocument doc, CSSContext ctx, Parser p, ValueManager[] vms,
			ShorthandManager[] sms) {

		ParsedURL durl = ((SVGOMDocument) doc).getParsedURL();
		CSSEngine result = createCSSEngine(doc, durl, p, vms, sms, ctx);

		URL url = getResource("resources/UserAgentStyleSheet.css");
		if (url != null) {
			ParsedURL purl = new ParsedURL(url);
			InputStream is;
			try {
				is = openStream(purl);
			} catch (IOException e) {
				e.printStackTrace();
				return result;
			}
			try (InputStreamReader re = new InputStreamReader(is, StandardCharsets.UTF_8)) {
				InputSource source = new InputSource(re);
				result.setUserAgentStyleSheet(result.parseStyleSheet(source, purl, "all"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	CSSEngine createCSSEngine(AbstractStylableDocument doc, ParsedURL durl, Parser p, ValueManager[] vms,
			ShorthandManager[] sms, CSSContext ctx) {
		return new SVGCSSEngine(doc, durl, p, vms, sms, ctx);
	}

	protected URL getResource(String resourceName) {
		return ResourceLoader.getInstance().getResource(getClass(), resourceName);
	}

	protected InputStream openStream(ParsedURL purl) throws IOException {
		return ResourceLoader.getInstance().openStream(purl);
	}

	/**
	 * Creates a ViewCSS.
	 */
	@Override
	public ViewCSS createViewCSS(AbstractStylableDocument doc) {
		return new CSSOMSVGViewCSS(doc.getCSSEngine());
	}

	/**
	 * <b>DOM</b>: Implements
	 * {@link DOMImplementation#createDocument(String,String,DocumentType)}.
	 */
	@Override
	public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype)
			throws DOMException {
		Document result = new SVGOMDocument(doctype, this);
		// BUG 32108: return empty document if qualifiedName is null.
		if (qualifiedName != null)
			result.appendChild(result.createElementNS(namespaceURI, qualifiedName));
		return result;
	}

	// DOMImplementationCSS /////////////////////////////////////////////////

	/**
	 * <b>DOM</b>: Implements
	 * {@link org.w3c.dom.css.DOMImplementationCSS#createCSSStyleSheet(String,String)}.
	 */
	@Override
	public CSSStyleSheet createCSSStyleSheet(String title, String media) {
		throw new UnsupportedOperationException("DOMImplementationCSS.createCSSStyleSheet is not implemented"); // XXX
	}

	// CSSStyleDeclarationFactory ///////////////////////////////////////////

	/**
	 * Creates a style declaration.
	 * 
	 * @return a CSSOMStyleDeclaration instance.
	 */
	@Override
	public CSSStyleDeclaration createCSSStyleDeclaration() {
		throw new UnsupportedOperationException(
				"CSSStyleDeclarationFactory.createCSSStyleDeclaration is not implemented"); // XXX
	}

	// StyleSheetFactory /////////////////////////////////////////////

	/**
	 * Creates a stylesheet from the data of an xml-stylesheet processing
	 * instruction or return null.
	 */
	@Override
	public StyleSheet createStyleSheet(Node n, HashMap<String, String> attrs) {
		throw new UnsupportedOperationException("StyleSheetFactory.createStyleSheet is not implemented"); // XXX
	}

	/**
	 * Returns the user-agent stylesheet.
	 */
	public CSSStyleSheet getUserAgentStyleSheet() {
		throw new UnsupportedOperationException("StyleSheetFactory.getUserAgentStyleSheet is not implemented"); // XXX
	}

	/**
	 * Implements the behavior of Document.createElementNS() for this DOM
	 * implementation.
	 */
	@Override
	public Element createElementNS(AbstractDocument document, String namespaceURI, String qualifiedName) {
		if (namespaceURI == null)
			return new GenericElement(qualifiedName.intern(), document);

		String name = DOMUtilities.getLocalName(qualifiedName);

		if (SVGConstants.SVG_NAMESPACE_URI.equals(namespaceURI)) {
			ElementFactory ef = factories.get(name);
			if (ef != null)
				return ef.create(DOMUtilities.getPrefix(qualifiedName), document);
			// Typically a div inside SVG
			if ("div".equals(name)) {
				namespaceURI = "http://www.w3.org/1999/xhtml";
			}
		}
		if (customFactories != null) {
			ElementFactory cef;
			cef = (ElementFactory) customFactories.get(namespaceURI, name);
			if (cef != null) {
				String prefix = DOMUtilities.getPrefix(qualifiedName);
				return cef.create(prefix, document);
			}
		}

		return super.createElementNS(document, namespaceURI, qualifiedName);
	}

	/**
	 * Creates an DocumentEventSupport object suitable for use with this
	 * implementation.
	 */
	@Override
	public DocumentEventSupport createDocumentEventSupport() {
		DocumentEventSupport result = new DocumentEventSupport();
		result.registerEventFactory("SVGEvents", new DocumentEventSupport.EventFactory() {
			@Override
			public Event createEvent() {
				return new SVGOMEvent();
			}
		});
		result.registerEventFactory("TimeEvent", new DocumentEventSupport.EventFactory() {
			@Override
			public Event createEvent() {
				return new DOMTimeEvent();
			}
		});
		return result;
	}

	// The element factories /////////////////////////////////////////////////

	/**
	 * The SVG element factories.
	 */
	protected static HashMap<String, ElementFactory> svgFactories = new HashMap<>(85);

	static {
		svgFactories.put(SVGConstants.SVG_A_TAG, new AElementFactory());

		svgFactories.put(SVGConstants.SVG_ALT_GLYPH_TAG, new AltGlyphElementFactory());

		svgFactories.put(SVGConstants.SVG_ALT_GLYPH_DEF_TAG, new AltGlyphDefElementFactory());

		svgFactories.put(SVGConstants.SVG_ALT_GLYPH_ITEM_TAG, new AltGlyphItemElementFactory());

		svgFactories.put(SVGConstants.SVG_ANIMATE_TAG, new AnimateElementFactory());

		svgFactories.put(SVGConstants.SVG_ANIMATE_COLOR_TAG, new AnimateColorElementFactory());

		svgFactories.put(SVGConstants.SVG_ANIMATE_MOTION_TAG, new AnimateMotionElementFactory());

		svgFactories.put(SVGConstants.SVG_ANIMATE_TRANSFORM_TAG, new AnimateTransformElementFactory());

		svgFactories.put(SVGConstants.SVG_CIRCLE_TAG, new CircleElementFactory());

		svgFactories.put(SVGConstants.SVG_CLIP_PATH_TAG, new ClipPathElementFactory());

		svgFactories.put(SVGConstants.SVG_CURSOR_TAG, new CursorElementFactory());

		svgFactories.put(SVGConstants.SVG_DEFINITION_SRC_TAG, new DefinitionSrcElementFactory());

		svgFactories.put(SVGConstants.SVG_DEFS_TAG, new DefsElementFactory());

		svgFactories.put(SVGConstants.SVG_DESC_TAG, new DescElementFactory());

		svgFactories.put(SVGConstants.SVG_DISCARD_TAG, new DiscardElementFactory());

		svgFactories.put(SVGConstants.SVG_ELLIPSE_TAG, new EllipseElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_BLEND_TAG, new FeBlendElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_COLOR_MATRIX_TAG, new FeColorMatrixElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_COMPONENT_TRANSFER_TAG, new FeComponentTransferElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_COMPOSITE_TAG, new FeCompositeElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_CONVOLVE_MATRIX_TAG, new FeConvolveMatrixElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_DIFFUSE_LIGHTING_TAG, new FeDiffuseLightingElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_DISPLACEMENT_MAP_TAG, new FeDisplacementMapElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_DISTANT_LIGHT_TAG, new FeDistantLightElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_FLOOD_TAG, new FeFloodElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_FUNC_A_TAG, new FeFuncAElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_FUNC_R_TAG, new FeFuncRElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_FUNC_G_TAG, new FeFuncGElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_FUNC_B_TAG, new FeFuncBElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_GAUSSIAN_BLUR_TAG, new FeGaussianBlurElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_IMAGE_TAG, new FeImageElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_MERGE_TAG, new FeMergeElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_MERGE_NODE_TAG, new FeMergeNodeElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_MORPHOLOGY_TAG, new FeMorphologyElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_OFFSET_TAG, new FeOffsetElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_POINT_LIGHT_TAG, new FePointLightElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_SPECULAR_LIGHTING_TAG, new FeSpecularLightingElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_SPOT_LIGHT_TAG, new FeSpotLightElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_TILE_TAG, new FeTileElementFactory());

		svgFactories.put(SVGConstants.SVG_FE_TURBULENCE_TAG, new FeTurbulenceElementFactory());

		svgFactories.put(SVGConstants.SVG_FILTER_TAG, new FilterElementFactory());

		svgFactories.put(SVGConstants.SVG_FONT_TAG, new FontElementFactory());

		svgFactories.put(SVGConstants.SVG_FONT_FACE_TAG, new FontFaceElementFactory());

		svgFactories.put(SVGConstants.SVG_FONT_FACE_FORMAT_TAG, new FontFaceFormatElementFactory());

		svgFactories.put(SVGConstants.SVG_FONT_FACE_NAME_TAG, new FontFaceNameElementFactory());

		svgFactories.put(SVGConstants.SVG_FONT_FACE_SRC_TAG, new FontFaceSrcElementFactory());

		svgFactories.put(SVGConstants.SVG_FONT_FACE_URI_TAG, new FontFaceUriElementFactory());

		svgFactories.put(SVGConstants.SVG_FOREIGN_OBJECT_TAG, new ForeignObjectElementFactory());

		svgFactories.put(SVGConstants.SVG_G_TAG, new GElementFactory());

		svgFactories.put(SVGConstants.SVG_GLYPH_TAG, new GlyphElementFactory());

		svgFactories.put(SVGConstants.SVG_GLYPH_REF_TAG, new GlyphRefElementFactory());

		svgFactories.put(SVGConstants.SVG_HKERN_TAG, new HkernElementFactory());

		svgFactories.put(SVGConstants.SVG_IMAGE_TAG, new ImageElementFactory());

		svgFactories.put(SVGConstants.SVG_LINE_TAG, new LineElementFactory());

		svgFactories.put(SVGConstants.SVG_LINEAR_GRADIENT_TAG, new LinearGradientElementFactory());

		svgFactories.put(SVGConstants.SVG_MARKER_TAG, new MarkerElementFactory());

		svgFactories.put(SVGConstants.SVG_MASK_TAG, new MaskElementFactory());

		svgFactories.put(SVGConstants.SVG_METADATA_TAG, new MetadataElementFactory());

		svgFactories.put(SVGConstants.SVG_MISSING_GLYPH_TAG, new MissingGlyphElementFactory());

		svgFactories.put(SVGConstants.SVG_MPATH_TAG, new MpathElementFactory());

		svgFactories.put(SVGConstants.SVG_PATH_TAG, new PathElementFactory());

		svgFactories.put(SVGConstants.SVG_PATTERN_TAG, new PatternElementFactory());

		svgFactories.put(SVGConstants.SVG_POLYGON_TAG, new PolygonElementFactory());

		svgFactories.put(SVGConstants.SVG_POLYLINE_TAG, new PolylineElementFactory());

		svgFactories.put(SVGConstants.SVG_RADIAL_GRADIENT_TAG, new RadialGradientElementFactory());

		svgFactories.put(SVGConstants.SVG_RECT_TAG, new RectElementFactory());

		svgFactories.put(SVGConstants.SVG_SET_TAG, new SetElementFactory());

		svgFactories.put(SVGConstants.SVG_SCRIPT_TAG, new ScriptElementFactory());

		svgFactories.put(SVGConstants.SVG_STOP_TAG, new StopElementFactory());

		svgFactories.put(SVGConstants.SVG_STYLE_TAG, new StyleElementFactory());

		svgFactories.put(SVGConstants.SVG_SVG_TAG, new SvgElementFactory());

		svgFactories.put(SVGConstants.SVG_SWITCH_TAG, new SwitchElementFactory());

		svgFactories.put(SVGConstants.SVG_SYMBOL_TAG, new SymbolElementFactory());

		svgFactories.put(SVGConstants.SVG_TEXT_TAG, new TextElementFactory());

		svgFactories.put(SVGConstants.SVG_TEXT_PATH_TAG, new TextPathElementFactory());

		svgFactories.put(SVGConstants.SVG_TITLE_TAG, new TitleElementFactory());

		svgFactories.put(SVGConstants.SVG_TREF_TAG, new TrefElementFactory());

		svgFactories.put(SVGConstants.SVG_TSPAN_TAG, new TspanElementFactory());

		svgFactories.put(SVGConstants.SVG_USE_TAG, new UseElementFactory());

		svgFactories.put(SVGConstants.SVG_VIEW_TAG, new ViewElementFactory());

		svgFactories.put(SVGConstants.SVG_VKERN_TAG, new VkernElementFactory());
	}

	/**
	 * To create a 'a' element.
	 */
	protected static class AElementFactory implements ElementFactory {

		public AElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'altGlyph' element.
	 */
	protected static class AltGlyphElementFactory implements ElementFactory {

		public AltGlyphElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAltGlyphElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'altGlyphDef' element.
	 */
	protected static class AltGlyphDefElementFactory implements ElementFactory {

		public AltGlyphDefElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAltGlyphDefElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'altGlyphItem' element.
	 */
	protected static class AltGlyphItemElementFactory implements ElementFactory {

		public AltGlyphItemElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAltGlyphItemElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'animate' element.
	 */
	protected static class AnimateElementFactory implements ElementFactory {

		public AnimateElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAnimateElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'animateColor' element.
	 */
	protected static class AnimateColorElementFactory implements ElementFactory {

		public AnimateColorElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAnimateColorElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'animateMotion' element.
	 */
	protected static class AnimateMotionElementFactory implements ElementFactory {

		public AnimateMotionElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAnimateMotionElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'animateTransform' element.
	 */
	protected static class AnimateTransformElementFactory implements ElementFactory {

		public AnimateTransformElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMAnimateTransformElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'circle' element.
	 */
	protected static class CircleElementFactory implements ElementFactory {

		public CircleElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMCircleElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'clip-path' element.
	 */
	protected static class ClipPathElementFactory implements ElementFactory {

		public ClipPathElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMClipPathElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'cursor' element.
	 */
	protected static class CursorElementFactory implements ElementFactory {

		public CursorElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMCursorElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'definition-src' element.
	 */
	protected static class DefinitionSrcElementFactory implements ElementFactory {

		public DefinitionSrcElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMDefinitionSrcElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'defs' element.
	 */
	protected static class DefsElementFactory implements ElementFactory {

		public DefsElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMDefsElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'desc' element.
	 */
	protected static class DescElementFactory implements ElementFactory {

		public DescElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMDescElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'discard' element.
	 */
	protected static class DiscardElementFactory implements ElementFactory {

		public DiscardElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMDiscardElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create an 'ellipse' element.
	 */
	protected static class EllipseElementFactory implements ElementFactory {

		public EllipseElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMEllipseElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feBlend' element.
	 */
	protected static class FeBlendElementFactory implements ElementFactory {

		public FeBlendElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEBlendElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feColorMatrix' element.
	 */
	protected static class FeColorMatrixElementFactory implements ElementFactory {

		public FeColorMatrixElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEColorMatrixElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feComponentTransfer' element.
	 */
	protected static class FeComponentTransferElementFactory implements ElementFactory {

		public FeComponentTransferElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEComponentTransferElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feComposite' element.
	 */
	protected static class FeCompositeElementFactory implements ElementFactory {

		public FeCompositeElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFECompositeElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feConvolveMatrix' element.
	 */
	protected static class FeConvolveMatrixElementFactory implements ElementFactory {

		public FeConvolveMatrixElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEConvolveMatrixElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feDiffuseLighting' element.
	 */
	protected static class FeDiffuseLightingElementFactory implements ElementFactory {

		public FeDiffuseLightingElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEDiffuseLightingElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feDisplacementMap' element.
	 */
	protected static class FeDisplacementMapElementFactory implements ElementFactory {

		public FeDisplacementMapElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEDisplacementMapElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feDistantLight' element.
	 */
	protected static class FeDistantLightElementFactory implements ElementFactory {

		public FeDistantLightElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEDistantLightElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feFlood' element.
	 */
	protected static class FeFloodElementFactory implements ElementFactory {

		public FeFloodElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEFloodElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feFuncA' element.
	 */
	protected static class FeFuncAElementFactory implements ElementFactory {

		public FeFuncAElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEFuncAElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feFuncR' element.
	 */
	protected static class FeFuncRElementFactory implements ElementFactory {

		public FeFuncRElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEFuncRElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feFuncG' element.
	 */
	protected static class FeFuncGElementFactory implements ElementFactory {

		public FeFuncGElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEFuncGElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feFuncB' element.
	 */
	protected static class FeFuncBElementFactory implements ElementFactory {

		public FeFuncBElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEFuncBElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feGaussianBlur' element.
	 */
	protected static class FeGaussianBlurElementFactory implements ElementFactory {

		public FeGaussianBlurElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEGaussianBlurElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feImage' element.
	 */
	protected static class FeImageElementFactory implements ElementFactory {

		public FeImageElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEImageElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feMerge' element.
	 */
	protected static class FeMergeElementFactory implements ElementFactory {

		public FeMergeElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEMergeElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feMergeNode' element.
	 */
	protected static class FeMergeNodeElementFactory implements ElementFactory {

		public FeMergeNodeElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEMergeNodeElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feMorphology' element.
	 */
	protected static class FeMorphologyElementFactory implements ElementFactory {

		public FeMorphologyElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEMorphologyElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feOffset' element.
	 */
	protected static class FeOffsetElementFactory implements ElementFactory {

		public FeOffsetElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEOffsetElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'fePointLight' element.
	 */
	protected static class FePointLightElementFactory implements ElementFactory {

		public FePointLightElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFEPointLightElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feSpecularLighting' element.
	 */
	protected static class FeSpecularLightingElementFactory implements ElementFactory {

		public FeSpecularLightingElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFESpecularLightingElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feSpotLight' element.
	 */
	protected static class FeSpotLightElementFactory implements ElementFactory {

		public FeSpotLightElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFESpotLightElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feTile' element.
	 */
	protected static class FeTileElementFactory implements ElementFactory {

		public FeTileElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFETileElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'feTurbulence' element
	 */
	protected static class FeTurbulenceElementFactory implements ElementFactory {

		public FeTurbulenceElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFETurbulenceElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'filter' element.
	 */
	protected static class FilterElementFactory implements ElementFactory {

		public FilterElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFilterElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'font' element.
	 */
	protected static class FontElementFactory implements ElementFactory {

		public FontElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFontElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'font-face' element.
	 */
	protected static class FontFaceElementFactory implements ElementFactory {

		public FontFaceElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFontFaceElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'font-face-format' element.
	 */
	protected static class FontFaceFormatElementFactory implements ElementFactory {

		public FontFaceFormatElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFontFaceFormatElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'font-face-name' element.
	 */
	protected static class FontFaceNameElementFactory implements ElementFactory {

		public FontFaceNameElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFontFaceNameElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'font-face-src' element.
	 */
	protected static class FontFaceSrcElementFactory implements ElementFactory {

		public FontFaceSrcElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFontFaceSrcElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'font-face-uri' element.
	 */
	protected static class FontFaceUriElementFactory implements ElementFactory {

		public FontFaceUriElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMFontFaceUriElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'foreignObject' element.
	 */
	protected static class ForeignObjectElementFactory implements ElementFactory {

		public ForeignObjectElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMForeignObjectElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'g' element.
	 */
	protected static class GElementFactory implements ElementFactory {

		public GElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMGElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'glyph' element.
	 */
	protected static class GlyphElementFactory implements ElementFactory {

		public GlyphElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMGlyphElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'glyphRef' element.
	 */
	protected static class GlyphRefElementFactory implements ElementFactory {

		public GlyphRefElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMGlyphRefElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'hkern' element.
	 */
	protected static class HkernElementFactory implements ElementFactory {

		public HkernElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMHKernElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'image' element.
	 */
	protected static class ImageElementFactory implements ElementFactory {

		public ImageElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMImageElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'line' element.
	 */
	protected static class LineElementFactory implements ElementFactory {

		public LineElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMLineElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'linearGradient' element.
	 */
	protected static class LinearGradientElementFactory implements ElementFactory {

		public LinearGradientElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMLinearGradientElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'marker' element.
	 */
	protected static class MarkerElementFactory implements ElementFactory {

		public MarkerElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMMarkerElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'mask' element.
	 */
	protected static class MaskElementFactory implements ElementFactory {

		public MaskElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMMaskElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'metadata' element.
	 */
	protected static class MetadataElementFactory implements ElementFactory {

		public MetadataElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMMetadataElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'missing-glyph' element.
	 */
	protected static class MissingGlyphElementFactory implements ElementFactory {

		public MissingGlyphElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMMissingGlyphElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'mpath' element.
	 */
	protected static class MpathElementFactory implements ElementFactory {

		public MpathElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMMPathElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'path' element.
	 */
	protected static class PathElementFactory implements ElementFactory {

		public PathElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMPathElement(prefix, (AbstractDocument) doc);
		}

		@Override
		public void importAttributes(Element imported, Node toImport, boolean trimId) {
			ElementFactory.super.importAttributes(imported, toImport, trimId);
			if (toImport instanceof SVGOMPathElement) {
				SVGOMPathElement path = (SVGOMPathElement) imported;
				SVGOMPathElement otherPath = (SVGOMPathElement) toImport;
				SVGOMAnimatedPathData d = path.d;
				// Make sure pathSegs exists
				otherPath.d.getPathSegList();
				boolean wasValid = otherPath.d.pathSegs.isValid();
				if (otherPath.d.check() <= 0) {
					// Copy only if check was successful, so the errors will be reported
					d.getPathSegList();
					otherPath.d.pathSegs.copyTo(d.pathSegs);
				} else if (!wasValid) {
					// invalidate, so the errors can be reported
					otherPath.d.pathSegs.invalidate();
				}
			}
		}

	}

	/**
	 * To create a 'pattern' element.
	 */
	protected static class PatternElementFactory implements ElementFactory {

		public PatternElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMPatternElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'polygon' element.
	 */
	protected static class PolygonElementFactory implements ElementFactory {

		public PolygonElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMPolygonElement(prefix, (AbstractDocument) doc);
		}

		@Override
		public void importAttributes(Element imported, Node toImport, boolean trimId) {
			ElementFactory.super.importAttributes(imported, toImport, trimId);
			if (toImport instanceof SVGPointShapeElement) {
				SVGPointShapeElement poly = (SVGPointShapeElement) imported;
				SVGPointShapeElement otherPoly = (SVGPointShapeElement) toImport;
				SVGOMAnimatedPoints points = poly.points;
				otherPoly.points.getPoints();
				boolean wasValid = otherPoly.points.baseVal.isValid();
				if (otherPoly.points.check() <= 0) {
					// Copy only if check was successful, so the errors will be reported
					points.getPoints();
					otherPoly.points.baseVal.copyTo(points.baseVal);
				} else if (!wasValid) {
					// invalidate, so the errors can be reported
					otherPoly.points.baseVal.invalidate();
				}
			}
		}

	}

	/**
	 * To create a 'polyline' element.
	 */
	protected static class PolylineElementFactory implements ElementFactory {

		public PolylineElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMPolylineElement(prefix, (AbstractDocument) doc);
		}

		@Override
		public void importAttributes(Element imported, Node toImport, boolean trimId) {
			ElementFactory.super.importAttributes(imported, toImport, trimId);
			if (toImport instanceof SVGPointShapeElement) {
				SVGPointShapeElement poly = (SVGPointShapeElement) imported;
				SVGPointShapeElement otherPoly = (SVGPointShapeElement) toImport;
				SVGOMAnimatedPoints points = poly.points;
				otherPoly.points.getPoints();
				boolean wasValid = otherPoly.points.baseVal.isValid();
				if (otherPoly.points.check() <= 0) {
					// Copy only if check was successful, so the errors will be reported
					points.getPoints();
					otherPoly.points.baseVal.copyTo(points.baseVal);
				} else if (!wasValid) {
					// invalidate, so the errors can be reported
					otherPoly.points.baseVal.invalidate();
				}
			}
		}

	}

	/**
	 * To create a 'radialGradient' element.
	 */
	protected static class RadialGradientElementFactory implements ElementFactory {

		public RadialGradientElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMRadialGradientElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'rect' element.
	 */
	protected static class RectElementFactory implements ElementFactory {

		public RectElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMRectElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'script' element.
	 */
	protected static class ScriptElementFactory implements ElementFactory {

		public ScriptElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMScriptElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'set' element.
	 */
	protected static class SetElementFactory implements ElementFactory {

		public SetElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMSetElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'stop' element.
	 */
	protected static class StopElementFactory implements ElementFactory {

		public StopElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMStopElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'style' element.
	 */
	protected static class StyleElementFactory implements ElementFactory {

		public StyleElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMStyleElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create an 'svg' element.
	 */
	protected static class SvgElementFactory implements ElementFactory {

		public SvgElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMSVGElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'switch' element.
	 */
	protected static class SwitchElementFactory implements ElementFactory {

		public SwitchElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMSwitchElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'symbol' element.
	 */
	protected static class SymbolElementFactory implements ElementFactory {

		public SymbolElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMSymbolElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'text' element.
	 */
	protected static class TextElementFactory implements ElementFactory {

		public TextElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMTextElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'textPath' element.
	 */
	protected static class TextPathElementFactory implements ElementFactory {

		public TextPathElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMTextPathElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'title' element.
	 */
	protected static class TitleElementFactory implements ElementFactory {

		public TitleElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMTitleElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'tref' element.
	 */
	protected static class TrefElementFactory implements ElementFactory {

		public TrefElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMTRefElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'tspan' element.
	 */
	protected static class TspanElementFactory implements ElementFactory {

		public TspanElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMTSpanElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'use' element.
	 */
	protected static class UseElementFactory implements ElementFactory {

		public UseElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMUseElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'view' element.
	 */
	protected static class ViewElementFactory implements ElementFactory {

		public ViewElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMViewElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * To create a 'vkern' element.
	 */
	protected static class VkernElementFactory implements ElementFactory {

		public VkernElementFactory() {
		}

		/**
		 * Creates an instance of the associated element type.
		 */
		@Override
		public Element create(String prefix, Document doc) {
			return new SVGOMVKernElement(prefix, (AbstractDocument) doc);
		}

	}

	/**
	 * The default instance of this class.
	 */
	protected static final DOMImplementation DOM_IMPLEMENTATION = new SVGDOMImplementation();

}
