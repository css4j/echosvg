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
package io.sf.carte.echosvg.transcoder.svggen;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.XMLFilter;

import io.sf.carte.echosvg.svggen.SVGGraphics2D;
import io.sf.carte.echosvg.transcoder.AbstractTranscoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.keys.BooleanKey;
import io.sf.carte.echosvg.transcoder.keys.FloatKey;
import io.sf.carte.echosvg.transcoder.keys.IntegerKey;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * This class allows to simplify the creation of a transcoder which transcodes
 * to SVG content. To use this class, you just have to implement the
 * <i>transcode</i> method of the <i>AbstractTranscoder</i> class : first get
 * the associated Document from the <i>TranscoderOutput</i> :
 * {@link #createDocument(TranscoderOutput)}, then create a new
 * {@link io.sf.carte.echosvg.svggen.SVGGraphics2D} with this Document
 * 
 * <pre>
 * Document doc = this.createDocument(output);
 * svgGenerator = new SVGGraphics2D(doc);
 * </pre>
 * 
 * Perform the effective transcoding, using the
 * {@link io.sf.carte.echosvg.svggen.SVGGraphics2D} previously created then call
 * the {@link #writeSVGToOutput(SVGGraphics2D, Element, TranscoderOutput)} to
 * create the effective output file (if the output is set to be a File or URI)
 * 
 * <pre>
 * Element svgRoot = svgGenerator.getRoot();
 * writeSVGToOutput(svgGenerator, svgRoot, output);
 * </pre>
 *
 * <p>
 * Several transcoding hints are defined for this abstract transcoder, but no
 * default implementation is provided. Subclasses must implement which keys are
 * relevant to them :
 * </p>
 * <ul>
 * <li>KEY_INPUT_WIDTH, KEY_INPUT_HEIGHT, KEY_XOFFSET, KEY_YOFFSET : this
 * Integer keys allows to set the portion of the image to transcode, defined by
 * the width, height, and offset of this portion in Metafile units.</li>
 * <li>KEY_ESCAPED : this Boolean ley allow to escape XML characters in the
 * output</li>
 * </ul>
 * 
 * <pre>
 * transcoder.addTranscodingHint(ToSVGAbstractTranscoder.KEY_INPUT_WIDTH, Integer.valueOf(input_width));
 * </pre>
 *
 * KEY_WIDTH, KEY_HEIGHT : this Float values allows to force the width and
 * height of the output:
 *
 * <pre>
 * transcoder.addTranscodingHint(ToSVGAbstractTranscoder.KEY_WIDTH, Float.valueOf(width));
 * </pre>
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class ToSVGAbstractTranscoder extends AbstractTranscoder implements SVGConstants {

	public static final int TRANSCODER_ERROR_BASE = 0xff00;
	public static final int ERROR_NULL_INPUT = TRANSCODER_ERROR_BASE + 0;
	public static final int ERROR_INCOMPATIBLE_INPUT_TYPE = TRANSCODER_ERROR_BASE + 1;
	public static final int ERROR_INCOMPATIBLE_OUTPUT_TYPE = TRANSCODER_ERROR_BASE + 2;

	/*
	 * Keys definition : width value for the output (in pixels).
	 */
	public static final TranscodingHints.Key KEY_WIDTH = new FloatKey();

	/*
	 * Keys definition : height value for the output (in pixels).
	 */
	public static final TranscodingHints.Key KEY_HEIGHT = new FloatKey();

	/*
	 * Keys definition : width value for the input (in pixels).
	 */
	public static final TranscodingHints.Key KEY_INPUT_WIDTH = new IntegerKey();

	/*
	 * Keys definition : height value for the input (in pixels).
	 */
	public static final TranscodingHints.Key KEY_INPUT_HEIGHT = new IntegerKey();

	/*
	 * Keys definition : x offset value for the output (in pixels).
	 */
	public static final TranscodingHints.Key KEY_XOFFSET = new IntegerKey();

	/*
	 * Keys definition : y offset value for the output (in pixels).
	 */
	public static final TranscodingHints.Key KEY_YOFFSET = new IntegerKey();

	/*
	 * Keys definition : Define if the characters will be escaped in the output.
	 */
	public static final TranscodingHints.Key KEY_ESCAPED = new BooleanKey();

	protected SVGGraphics2D svgGenerator;

	/**
	 * Create an empty Document from a TranscoderOutput.
	 * <ul>
	 * <li>If the TranscoderOutput already contains an empty Document : returns this
	 * Document</li>
	 * <li>else create a new empty DOM Document</li>
	 * </ul>
	 */
	protected Document createDocument(TranscoderOutput output) {
		// Use SVGGraphics2D to generate SVG content
		Document doc;
		if (output.getDocument() == null) {
			doc = createDocument();
		} else {
			doc = output.getDocument();
		}

		return doc;
	}

	private static Document createDocument() {
		DOMImplementation domImpl;
		try {
			Class<?> domCls = Class.forName("io.sf.carte.echosvg.anim.dom.SVGDOMImplementation");
			Method m = domCls.getMethod("getDOMImplementation");
			domImpl = (DOMImplementation) m.invoke(null);
		} catch (Exception e) {
			domImpl = getJDKDOMImplementation();
		}

		return domImpl.createDocument(SVG_NAMESPACE_URI, SVG_SVG_TAG, null);
	}

	private static DOMImplementation getJDKDOMImplementation() {
		// First obtain a DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}

		return builder.getDOMImplementation();
	}

	/**
	 * Get the {@link io.sf.carte.echosvg.svggen.SVGGraphics2D} associated with this
	 * transcoder.
	 */
	public SVGGraphics2D getGraphics2D() {
		return svgGenerator;
	}

	/**
	 * Writes the SVG content held by the svgGenerator to the
	 * <code>TranscoderOutput</code>. This method does nothing if the output already
	 * contains a Document.
	 */
	protected void writeSVGToOutput(SVGGraphics2D svgGenerator, Element svgRoot, TranscoderOutput output)
			throws TranscoderException {

		Document doc = output.getDocument();

		if (doc != null)
			return;

		// XMLFilter
		XMLFilter xmlFilter = output.getXMLFilter();
		if (xmlFilter != null) {
			handler.fatalError(new TranscoderException("" + ERROR_INCOMPATIBLE_OUTPUT_TYPE));
		}

		try {
			boolean escaped = false;
			if (hints.containsKey(KEY_ESCAPED)) {
				escaped = (Boolean) hints.get(KEY_ESCAPED);
			}
			// Output stream
			OutputStream os = output.getOutputStream();
			if (os != null) {
				svgGenerator.stream(svgRoot, new OutputStreamWriter(os), false, escaped);
				return;
			}

			// Writer
			Writer wr = output.getWriter();
			if (wr != null) {
				svgGenerator.stream(svgRoot, wr, false, escaped);
				return;
			}

			// URI
			String uri = output.getURI();
			if (uri != null) {
				try {
					URL url = new URL(uri);
					URLConnection urlCnx = url.openConnection();
					os = urlCnx.getOutputStream();
					svgGenerator.stream(svgRoot, new OutputStreamWriter(os), false, escaped);
					return;
				} catch (IOException e) {
					handler.fatalError(new TranscoderException(e));
				}
			}
		} catch (IOException e) {
			throw new TranscoderException(e);
		}

		throw new TranscoderException("" + ERROR_INCOMPATIBLE_OUTPUT_TYPE);

	}
}
