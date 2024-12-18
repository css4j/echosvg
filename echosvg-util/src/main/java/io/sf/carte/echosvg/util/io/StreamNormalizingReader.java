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
import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.sf.carte.echosvg.util.EncodingUtilities;

/**
 * This class represents a NormalizingReader which handles streams of bytes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class StreamNormalizingReader extends NormalizingReader {

	/**
	 * The char decoder.
	 */
	protected CharDecoder charDecoder;

	/**
	 * The next char.
	 */
	protected int nextChar = -1;

	/**
	 * The current line in the stream.
	 */
	protected int line = 1;

	/**
	 * The current column in the stream.
	 */
	protected int column;

	/**
	 * Creates a new NormalizingReader. The encoding is assumed to be ISO-8859-1.
	 * 
	 * @param is The input stream to decode.
	 */
	public StreamNormalizingReader(InputStream is) throws IOException {
		this(is, null);
	}

	/**
	 * Creates a new NormalizingReader.
	 * 
	 * @param is  The input stream to decode.
	 * @param enc The standard encoding name. A null encoding means ISO-8859-1.
	 */
	public StreamNormalizingReader(InputStream is, String enc) throws IOException {
		if (enc == null) {
			enc = "ISO-8859-1";
		}
		charDecoder = createCharDecoder(is, enc);
	}

	/**
	 * Creates a new NormalizingReader.
	 * 
	 * @param r The reader to wrap.
	 */
	public StreamNormalizingReader(Reader r) throws IOException {
		charDecoder = new GenericDecoder(r);
	}

	/**
	 * This constructor is intended for use by subclasses.
	 */
	protected StreamNormalizingReader() {
	}

	/**
	 * Read a single character. This method will block until a character is
	 * available, an I/O error occurs, or the end of the stream is reached.
	 */
	@Override
	public int read() throws IOException {
		int result = nextChar;
		if (result != -1) {
			nextChar = -1;
			if (result == 13) {
				column = 0;
				line++;
			} else {
				column++;
			}
			return result;
		}
		result = charDecoder.readChar();
		switch (result) {
		case 13:
			column = 0;
			line++;
			int c = charDecoder.readChar();
			if (c == 10) {
				return 10;
			}
			nextChar = c;
			return 10;

		case 10:
			column = 0;
			line++;
			break;

		default:
			column++;
		}
		return result;
	}

	/**
	 * Returns the current line in the stream.
	 */
	@Override
	public int getLine() {
		return line;
	}

	/**
	 * Returns the current column in the stream.
	 */
	@Override
	public int getColumn() {
		return column;
	}

	/**
	 * Close the stream.
	 */
	@Override
	public void close() throws IOException {
		charDecoder.dispose();
		charDecoder = null;
	}

	/**
	 * Creates the CharDecoder mapped with the given encoding name.
	 */
	protected CharDecoder createCharDecoder(InputStream is, String enc) throws IOException {
		CharDecoderFactory cdf = charDecoderFactories.get(enc.toUpperCase(Locale.ROOT));
		if (cdf != null) {
			return cdf.createCharDecoder(is);
		}
		String e = EncodingUtilities.javaEncoding(enc);
		if (e == null) {
			e = enc;
		}
		return new GenericDecoder(is, e);
	}

	/**
	 * The CharDecoder factories map.
	 */
	protected static final Map<String, CharDecoderFactory> charDecoderFactories = new HashMap<>(11);
	static {
		CharDecoderFactory cdf = new ASCIIDecoderFactory();
		charDecoderFactories.put("ASCII", cdf);
		charDecoderFactories.put("US-ASCII", cdf);
		charDecoderFactories.put("ISO-8859-1", new ISO_8859_1DecoderFactory());
		charDecoderFactories.put("UTF-8", new UTF8DecoderFactory());
		charDecoderFactories.put("UTF-16", new UTF16DecoderFactory());
	}

	/**
	 * Represents a CharDecoder factory.
	 */
	protected interface CharDecoderFactory {

		CharDecoder createCharDecoder(InputStream is) throws IOException;

	}

	/**
	 * To create an ASCIIDecoder.
	 */
	protected static class ASCIIDecoderFactory implements CharDecoderFactory {

		@Override
		public CharDecoder createCharDecoder(InputStream is) throws IOException {
			return new ASCIIDecoder(is);
		}

	}

	/**
	 * To create an ISO_8859_1Decoder.
	 */
	protected static class ISO_8859_1DecoderFactory implements CharDecoderFactory {

		@Override
		public CharDecoder createCharDecoder(InputStream is) throws IOException {
			return new ISO_8859_1Decoder(is);
		}

	}

	/**
	 * To create a UTF8Decoder.
	 */
	protected static class UTF8DecoderFactory implements CharDecoderFactory {

		@Override
		public CharDecoder createCharDecoder(InputStream is) throws IOException {
			return new UTF8Decoder(is);
		}

	}

	/**
	 * To create a UTF16Decoder.
	 */
	protected static class UTF16DecoderFactory implements CharDecoderFactory {

		@Override
		public CharDecoder createCharDecoder(InputStream is) throws IOException {
			return new UTF16Decoder(is);
		}

	}

}
