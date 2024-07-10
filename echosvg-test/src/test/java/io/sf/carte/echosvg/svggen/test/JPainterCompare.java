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
package io.sf.carte.echosvg.svggen.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.svggen.CachedImageHandlerBase64Encoder;
import io.sf.carte.echosvg.svggen.GenericImageHandler;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext;
import io.sf.carte.echosvg.svggen.SVGGraphics2D;
import io.sf.carte.echosvg.swing.JSVGCanvas;
import io.sf.carte.echosvg.swing.svg.SVGDocumentLoaderAdapter;
import io.sf.carte.echosvg.swing.svg.SVGDocumentLoaderEvent;
import io.sf.carte.echosvg.util.SVGConstants;

/**
 * Simple component which displays, side by side, the drawing created by a
 * <code>Painter</code>, rendered in a <code>JPainterComponent</code> on the
 * left, and in a <code>JSVGCanvas</code> on the right, where the SVG displayed
 * is the one created by the <code>SVGGraphics2D</code>
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JPainterCompare extends JPanel implements SVGConstants {

	private static final long serialVersionUID = 1L;

	/**
	 * Canvas size for all tests
	 */
	public static final Dimension CANVAS_SIZE = new Dimension(300, 400);

	public static final String MESSAGES_USAGE = "JPainterCompare.messages.usage";

	public static final String MESSAGES_LOADING_CLASS = "JPainterCompare.messages.loading.class";

	public static final String MESSAGES_LOADED_CLASS = "JPainterCompare.messages.loaded.class";

	public static final String MESSAGES_INSTANCIATED_OBJECT = "JPainterCompare.messages.instanciated.object";

	public static final String ERROR_COULD_NOT_LOAD_CLASS = "JPainterCompare.error.could.not.load.class";

	public static final String ERROR_COULD_NOT_INSTANCIATE_OBJECT = "JPainterCompare.error.could.not.instanciate.object";

	public static final String ERROR_CLASS_NOT_PAINTER = "JPainterCompare.error.class.not.painter";

	public static final String ERROR_COULD_NOT_TRANSCODE_TO_SVG = "JPainterCompare.error.could.not.transcode.to.svg";

	public static final String ERROR_COULD_NOT_CONVERT_FILE_PATH_TO_URL = "JPainterCompare.error.could.not.convert.file.path.to.url";

	public static final String ERROR_COULD_NOT_RENDER_GENERATED_SVG = "JPainterCompare.error.could.not.render.generated.svg";

	public static final String CONFIG_TMP_FILE_PREFIX = "JPainterCompare.config.tmp.file.prefix";

	/**
	 * Builds an <code>SVGGraphics2D</code> with a default configuration.
	 */
	protected SVGGraphics2D buildSVGGraphics2D() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String namespaceURI = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document domFactory = impl.createDocument(namespaceURI, SVG_SVG_TAG, null);

		// Create a default context from our Document instance
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(domFactory);

		GenericImageHandler ihandler = new CachedImageHandlerBase64Encoder();
		ctx.setGenericImageHandler(ihandler);

		return new SVGGraphics2D(ctx, false);
	}

	private static class LoaderListener extends SVGDocumentLoaderAdapter {

		public final String sem = "sem-echo";
		boolean success = false;

		@Override
		public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
			synchronized (sem) {
				sem.notifyAll();
			}
		}

		@Override
		public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
			success = true;
			synchronized (sem) {
				sem.notifyAll();
			}
		}

	}

	/**
	 * Constructor
	 */
	public JPainterCompare(Painter painter) {
		// First, create the AWT reference.
		JPainterComponent ref = new JPainterComponent(painter);

		// Now, generate the SVG from this Painter
		SVGGraphics2D g2d = buildSVGGraphics2D();

		g2d.setSVGCanvasSize(CANVAS_SIZE);

		//
		// Generate SVG content
		//
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile(CONFIG_TMP_FILE_PREFIX, ".svg");

			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tmpFile),
					StandardCharsets.UTF_8);

			painter.paint(g2d);
			g2d.stream(osw);
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(Messages.formatMessage(
					ERROR_COULD_NOT_TRANSCODE_TO_SVG, new Object[] { e.getClass().getName() }));
		}

		//
		// Now, transcode SVG to a BufferedImage
		//
		JSVGCanvas svgCanvas = new JSVGCanvas();
		LoaderListener l = new LoaderListener();
		svgCanvas.addSVGDocumentLoaderListener(l);

		try {
			svgCanvas.setURI(tmpFile.toURI().toURL().toString());
			synchronized (l.sem) {
				l.sem.wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(Messages.formatMessage(
					ERROR_COULD_NOT_CONVERT_FILE_PATH_TO_URL, new Object[] { e.getMessage() }));
		}

		if (l.success) {
			setLayout(new GridLayout(1, 2));
			add(ref);
			add(svgCanvas);
		}

		else {
			throw new RuntimeException(Messages.formatMessage(ERROR_COULD_NOT_RENDER_GENERATED_SVG, null));
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(CANVAS_SIZE.width * 2, CANVAS_SIZE.height);
	}

	/*
	 * Debug application: shows the image created by a <code>Painter</code> on the
	 * left and the image created by a <code>JSVGComponent</code> from the SVG
	 * generated by <code>SVGGraphics2D</code> from the same <code>Painter</code> on
	 * the right.
	 *
	 */
	public static void main(String[] args) {
		if (args.length <= 0) {
			System.out.println(Messages.formatMessage(MESSAGES_USAGE, null));
			System.exit(0);
		}

		// Load class.
		String className = args[0];
		System.out.println(Messages.formatMessage(MESSAGES_LOADING_CLASS, new Object[] { className }));

		Class<?> cl = null;

		try {
			cl = Class.forName(className);
			System.out.println(Messages.formatMessage(MESSAGES_LOADED_CLASS, new Object[] { className }));
		} catch (Exception e) {
			System.out.println(Messages.formatMessage(ERROR_COULD_NOT_LOAD_CLASS,
					new Object[] { className, e.getClass().getName() }));
			System.exit(0);
		}

		// Instanciate object
		Object o = null;

		try {
			o = cl.getDeclaredConstructor().newInstance();
			System.out.println(Messages.formatMessage(MESSAGES_INSTANCIATED_OBJECT, null));
		} catch (Exception e) {
			System.out.println(Messages.formatMessage(ERROR_COULD_NOT_INSTANCIATE_OBJECT,
					new Object[] { className, e.getClass().getName() }));
			System.exit(0);
		}

		// Cast to Painter
		Painter p = null;

		try {
			p = (Painter) o;
		} catch (ClassCastException e) {
			System.out.println(Messages.formatMessage(ERROR_CLASS_NOT_PAINTER, new Object[] { className }));
			System.exit(0);
		}

		// Build frame
		JFrame f = new JFrame();
		JPainterCompare c = new JPainterCompare(p);
		c.setBackground(Color.white);
		c.setPreferredSize(new Dimension(300, 400));
		f.getContentPane().add(c);
		f.getContentPane().setBackground(Color.white);
		f.pack();
		f.setVisible(true);
	}

}
