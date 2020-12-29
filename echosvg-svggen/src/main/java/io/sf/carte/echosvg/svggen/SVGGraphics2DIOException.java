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
package io.sf.carte.echosvg.svggen;

import java.io.IOException;

/**
 * Thrown when an SVG Generator method receives an illegal argument in parameter.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @version $Id$
 */
public class SVGGraphics2DIOException extends IOException {
    /** The enclosed exception. */
    private IOException embedded;

    /**
     * Constructs a new <code>SVGGraphics2DIOException</code> with the
     * specified detail message.
     * @param s the detail message of this exception
     */
    public SVGGraphics2DIOException(String s) {
        this(s, null);
    }

    /**
     * Constructs a new <code>SVGGraphics2DIOException</code> with the
     * specified detail message.
     * @param ex the enclosed exception
     */
    public SVGGraphics2DIOException(IOException ex) {
        this(null, ex);
    }

    /**
     * Constructs a new <code>SVGGraphics2DIOException</code> with the
     * specified detail message.
     * @param s the detail message of this exception
     * @param ex the original exception
     */
    public SVGGraphics2DIOException(String s, IOException ex) {
        super(s);
        embedded = ex;
    }

    /**
     * Returns the message of this exception. If an error message has
     * been specified, returns that one. Otherwise, return the error message
     * of enclosed exception or null if any.
     */
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) {
            return msg;
        } else if (embedded != null) {
            return embedded.getMessage();
        } else {
            return null;
        }
    }

    /**
     * Returns the original enclosed exception or null if any.
     */
    public IOException getException() {
        return embedded;
    }
}
