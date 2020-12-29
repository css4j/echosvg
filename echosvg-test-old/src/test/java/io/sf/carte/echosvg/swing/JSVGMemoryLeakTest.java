/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.swing;

import java.io.File;
import java.net.MalformedURLException;
import java.lang.ref.WeakReference;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.w3c.dom.Element;

import io.sf.carte.echosvg.anim.dom.SVGOMDocument;
import io.sf.carte.echosvg.anim.dom.SVGOMElement;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.UpdateManager;
import io.sf.carte.echosvg.dom.svg.SVGContext;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.MemoryLeakTest;
import io.sf.carte.echosvg.test.TestReport;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @version $Id$
 */
public class JSVGMemoryLeakTest extends MemoryLeakTest
    implements JSVGCanvasHandler.Delegate {
    public JSVGMemoryLeakTest() {
    }

    public String getName() { return "JSVGMemoryLeakTest."+getId(); }

    TestReport failReport = null;
    boolean done;
    JSVGCanvasHandler handler;
    JFrame theFrame;

    /**
     * A WeakReference to the JSVGCanvas.
     */
    WeakReference theCanvas;

    protected void setTheCanvas(JSVGCanvas c) {
        theCanvas = new WeakReference(c);
    }

    protected JSVGCanvas getTheCanvas() {
        return (JSVGCanvas) theCanvas.get();
    }

    public static String fmt(String key, Object []args) {
        return TestMessages.formatMessage(key, args);
    }

    public JSVGCanvasHandler createHandler() {
        return new JSVGCanvasHandler(this, this);
    }

    public TestReport doSomething() throws Exception {
        handler = createHandler();
        registerObjectDesc(handler, "Handler");
        done = false;
        handler.runCanvas(getId());

        SwingUtilities.invokeAndWait( new Runnable() {
                public void run() {
                    // System.out.println("In Invoke");
                    theFrame.remove(getTheCanvas());
                    getTheCanvas().dispose();

                    theFrame.dispose();
                    theFrame=null;
                    theCanvas=null;
                }
            });

        try  { Thread.sleep(100); } catch (InterruptedException ie) { }

        SwingUtilities.invokeAndWait( new Runnable() {
                public void run() {
                    // Create a new Frame to take focus for Swing so old one
                    // can be GC'd.
                    theFrame = new JFrame("FocusFrame"); 
                    // registerObjectDesc(jframe, "FocusFrame");
                    theFrame.setSize(new java.awt.Dimension(40, 50));
                    theFrame.setVisible(true);
                }});

        try  { Thread.sleep(100); } catch (InterruptedException ie) { }
        
        SwingUtilities.invokeAndWait( new Runnable() {
                public void run() {
                    theFrame.setVisible(false);
                    theFrame.dispose();
                }});
        
        handler = null;
        if (failReport != null) return failReport;
        DefaultTestReport report = new DefaultTestReport(this);
        report.setPassed(true);
        return report;
    }

    public void scriptDone() {
        synchronized (this) {
            done = true;
            handler.scriptDone();
        }
    }

    public void registerElement(Element e, String desc) {
        registerObjectDesc(e, desc);
        UpdateManager um = getTheCanvas().getUpdateManager();
        BridgeContext bc = um.getBridgeContext();
        GraphicsNode gn = bc.getGraphicsNode(e);
        if (gn != null)
            registerObjectDesc(gn, desc+"_GN");
        if (e instanceof SVGOMElement) {
            SVGOMElement svge = (SVGOMElement)e;
            SVGContext svgctx = svge.getSVGContext();
            if (svgctx != null) {
                registerObjectDesc(svgctx, desc+"_CTX");
            }
        }
    }

    public void registerResourceContext(String uriSubstring, String desc) {
        UpdateManager um = getTheCanvas().getUpdateManager();
        BridgeContext bc = um.getBridgeContext();
        BridgeContext[] ctxs = bc.getChildContexts();
        for (BridgeContext ctx : ctxs) {
            bc = ctx;
            if (bc == null) {
                continue;
            }
            String url = ((SVGOMDocument) bc.getDocument()).getURL();
            if (url.indexOf(uriSubstring) != -1) {
                registerObjectDesc(ctx, desc);
            }
        }
    }

    /* JSVGCanvasHandler.Delegate Interface */
    public boolean canvasInit(JSVGCanvas canvas) {
        // System.err.println("In Init");
        setTheCanvas(canvas);
        theFrame  = handler.getFrame();

        File f = new File(getId());
        try {
            canvas.setURI(f.toURI().toURL().toString());
        } catch (MalformedURLException mue) {
        }
        registerObjectDesc(canvas, "JSVGCanvas");
        registerObjectDesc(handler.getFrame(), "JFrame");

        return true;
    }

    public void canvasLoaded(JSVGCanvas canvas) {
        // System.err.println("Loaded");
        registerObjectDesc(canvas.getSVGDocument(), "SVGDoc");
    }

    public void canvasRendered(JSVGCanvas canvas) {
        // System.err.println("Rendered");
        registerObjectDesc(canvas.getGraphicsNode(), "GVT");
        UpdateManager um = canvas.getUpdateManager();
        if (um == null) {
            return;
        }
        BridgeContext bc = um.getBridgeContext();
        registerObjectDesc(um, "updateManager");
        registerObjectDesc(bc, "bridgeContext");
        BridgeContext[] subCtxs = bc.getChildContexts();
        for (BridgeContext subCtx : subCtxs) {
            if (subCtx != null) {
                SVGOMDocument doc = (SVGOMDocument) subCtx.getDocument();
                registerObjectDesc(subCtx, "BridgeContext_" + doc.getURL());
            }
        }
    }

    public boolean canvasUpdated(JSVGCanvas canvas) {
        // System.err.println("Updated");
        synchronized (this) {
            return done;
        }
    }
    public void canvasDone(final JSVGCanvas canvas) {
        // System.err.println("Done");
    }

    public void failure(TestReport report) {
        synchronized (this) {
            done = true;
            failReport = report;
        }
    }
}
