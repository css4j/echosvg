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
package io.sf.carte.echosvg.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents an object which decodes ASCII characters from a stream
 * of bytes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ASCIIDecoder extends AbstractCharDecoder {

	/**
	 * Creates a new ASCIIDecoder.
	 */
	public ASCIIDecoder(InputStream is) {
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
			return END_OF_STREAM;
		}
		int result = buffer[position++];
		if (result < 0) {
			charError("ASCII");
		}
		return result;
	}
}
