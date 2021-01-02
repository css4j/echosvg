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

import java.io.InputStream;
import java.io.StreamCorruptedException;

import io.sf.carte.echosvg.ext.awt.image.renderable.Filter;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This type of Image tag registy entry is used for most normal image file
 * formats. You are given a markable stream and an opportunity to check if it is
 * "compatible" if you return true then you will likely be asked to provide the
 * decoded image next.
 * 
 * @see MagicNumberRegistryEntry
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface StreamRegistryEntry extends RegistryEntry {

	/**
	 * returns the number of bytes that need to be supported by mark on the
	 * InputStream for this to check the stream for compatibility.
	 */
	int getReadlimit();

	/**
	 * Check if the Stream references an image that can be handled by this format
	 * handler. The input stream passed in should be assumed to support mark and
	 * reset.
	 *
	 * If this method throws a StreamCorruptedException then the InputStream will be
	 * closed and a new one opened (if possible).
	 *
	 * This method should only throw a StreamCorruptedException if it is unable to
	 * restore the state of the InputStream (i.e. mark/reset fails basically).
	 */
	boolean isCompatibleStream(InputStream is) throws StreamCorruptedException;

	/**
	 * Decode the Stream into a Filter. If the stream turns out not to be of a
	 * format this RegistryEntry can handle you should attempt to reset the stream,
	 * then return null.
	 * <P>
	 *
	 * This should only return a broken link image when the image is clearly of this
	 * format, but is unreadable for some reason.
	 *
	 * @param is          The input stream that contains the image.
	 * @param origURL     The original URL, if any, for documentation purposes only.
	 *                    This may be null.
	 * @param needRawData If true the image returned should not have any default
	 *                    color correction the file may specify applied.
	 */
	Filter handleStream(InputStream is, ParsedURL origURL, boolean needRawData);
}
