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
package io.sf.carte.echosvg.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents an object which decodes ISO-8859-1 characters from a
 * stream of bytes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ISO_8859_1Decoder extends AbstractCharDecoder {

	/**
	 * Creates a new ISO_8859_1Decoder.
	 */
	public ISO_8859_1Decoder(InputStream is) {
		super(is);
	}

	/**
	 * Reads the next character.
	 * 
	 * @return a character or END_OF_STREAM.
	 */
	@Override
	public int readChar() throws IOException {
		if (position == count) {
			fillBuffer();
		}
		if (count == -1) {
			return -1;
		}
		return buffer[position++] & 0xff;
	}
}
