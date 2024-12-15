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
import java.io.Reader;

/**
 * This class represents a reader which normalizes the line break: \n, \r, \r\n
 * are replaced by \n. The methods of this reader are not synchronized. The
 * input is buffered.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public abstract class NormalizingReader extends Reader {

	/**
	 * Read characters into a portion of an array.
	 * 
	 * @param cbuf Destination buffer
	 * @param off  Offset at which to start writing characters
	 * @param len  Maximum number of characters to read
	 * @return The number of characters read, or -1 if the end of the stream has
	 *         been reached
	 * @throws IOException If an I/O error occurs
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (len == 0) {
			return 0;
		}

		int c = read();
		if (c == -1) {
			return -1;
		}
		cbuf[off] = (char) c;
		int result = 1;
		while (result < len && (c = read()) != -1) {
			cbuf[result + off] = (char) c;
			result++;
		}
		return result;
	}

	/**
	 * Returns the current line in the stream.
	 */
	public abstract int getLine();

	/**
	 * Returns the current column in the stream.
	 */
	public abstract int getColumn();

}
