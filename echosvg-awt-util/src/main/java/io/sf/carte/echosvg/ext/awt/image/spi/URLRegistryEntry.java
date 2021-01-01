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

package io.sf.carte.echosvg.ext.awt.image.spi;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This type of Image tag registy entry is used for 'odd' URL types.
 * Ussually this means that the URL uses a non-standard protocol.  In
 * these cases you should be aware that in order for the construction
 * of the URL object to succeed you must register a @see
 * URLStreamHandler using one of the methods listed in
 * @see java.net.URL#URL(java.lang.String, java.lang.String, int, java.lang.String)
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface URLRegistryEntry extends RegistryEntry {
    /**
     * Check if the URL references an image that can be
     * handled by this format handler.  Generally speaking
     * this should not open the URL.  The decision should
     * be based on the structure of the URL (such as
     * the protocol in use).<p>
     *
     * If you don't care about the structure of the URL and only about
     * the contents of the URL you should register as a
     * StreamRegistryEntry, so the URL "connection" will be made
     * only once.
     *
     * @param url The URL to inspect.
     */
    boolean isCompatibleURL(ParsedURL url);

    /**
     * Decode the URL into a RenderableImage, here you should feel
     * free to open the URL yourself.<P>
     *
     * This should only return a broken link image if the image
     * is clearly of this format, but is unreadable for some reason.
     * otherwise return null.<p>
     *
     * If all entries refuse the url or return null then the registry
     * will automatically return a broken link image for you.
     *
     * @param url The url that reference the image.
     * @param needRawData If true the image returned should not have
     *                    any default color correction the file may
     *                    specify applied.
     */
    Filter handleURL(ParsedURL url, boolean needRawData);
}
