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

package io.sf.carte.echosvg.dom.util;

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * Wrapper for SAX Exceptions which would make it possible to
 * include line and column information with SAX parse errors.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @version $Id$
 */
public class SAXIOException extends IOException {

    protected SAXException saxe;

    public SAXIOException( SAXException saxe) {
        super(saxe.getMessage());
        this.saxe = saxe;
    }

    public SAXException getSAXException() { return saxe; }
    public Throwable    getCause() { return saxe; }
}
