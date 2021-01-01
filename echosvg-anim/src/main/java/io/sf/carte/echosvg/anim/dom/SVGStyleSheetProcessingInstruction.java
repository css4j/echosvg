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
package io.sf.carte.echosvg.anim.dom;

import java.util.HashMap;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.css.engine.CSSEngine;
import io.sf.carte.echosvg.css.engine.CSSStyleSheetNode;
import io.sf.carte.echosvg.css.engine.StyleSheet;
import io.sf.carte.echosvg.dom.AbstractDocument;
import io.sf.carte.echosvg.dom.StyleSheetFactory;
import io.sf.carte.echosvg.dom.StyleSheetProcessingInstruction;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This class provides an implementation of the 'xml-stylesheet' processing
 * instructions.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGStyleSheetProcessingInstruction
    extends StyleSheetProcessingInstruction
    implements CSSStyleSheetNode {
    
    private static final long serialVersionUID = 1L;
    /**
     * The style-sheet.
     */
    protected StyleSheet styleSheet;

    /**
     * Creates a new ProcessingInstruction object.
     */
    protected SVGStyleSheetProcessingInstruction() {
    }

    /**
     * Creates a new ProcessingInstruction object.
     */
    public SVGStyleSheetProcessingInstruction(String            data,
                                              AbstractDocument  owner,
                                              StyleSheetFactory f) {
        super(data, owner, f);
    }

    /**
     * Returns the URI of the referenced stylesheet.
     */
    public String getStyleSheetURI() {
        SVGOMDocument svgDoc = (SVGOMDocument) getOwnerDocument();
        ParsedURL url = svgDoc.getParsedURL();
        String href = getPseudoAttributes().get("href");
        if (url != null) {
            return new ParsedURL(url, href).toString();
        }
        return href;
    }

    /**
     * Returns the associated style-sheet.
     */
    @Override
    public StyleSheet getCSSStyleSheet() {
        if (styleSheet == null) {
            HashMap<String, String> attrs = getPseudoAttributes();
            String type = attrs.get("type");

            if ("text/css".equals(type)) {
                String title     = attrs.get("title");
                String media     = attrs.get("media");
                String href      = attrs.get("href");
                String alternate = attrs.get("alternate");
                SVGOMDocument doc = (SVGOMDocument)getOwnerDocument();
                ParsedURL durl = doc.getParsedURL();
                ParsedURL burl = new ParsedURL(durl, href);
                CSSEngine e = doc.getCSSEngine();
                
                styleSheet = e.parseStyleSheet(burl, media);
                styleSheet.setAlternate("yes".equals(alternate));
                styleSheet.setTitle(title);
            }
        }
        return styleSheet;
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.ProcessingInstruction#setData(String)}.
     */
    @Override
    public void setData(String data) throws DOMException {
        super.setData(data);
        styleSheet = null;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    @Override
    protected Node newNode() {
        return new SVGStyleSheetProcessingInstruction();
    }
}
