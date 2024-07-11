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
package io.sf.carte.echosvg.ext.awt.image.codec.png;

/**
 * A PNG chunk.
 * <p>
 * See
 * <a href="https://w3c.github.io/PNG-spec/#4Concepts.FormatChunks">Chunks</a>
 * in the PNG specification.
 * </p>
 */
class PNGChunk {

	int length;
	int type;
	byte[] data;
	int crc;

	String typeString;

	public PNGChunk(int length, int type, byte[] data, int crc) {
		this.length = length;
		this.type = type;
		this.data = data;
		this.crc = crc;

		typeString = "" + (char) ((type >>> 24) & 0xff) + (char) ((type >>> 16) & 0xff) + (char) ((type >>> 8) & 0xff)
				+ (char) ((type) & 0xff);
	}

	public int getLength() {
		return length;
	}

	public int getType() {
		return type;
	}

	public String getTypeString() {
		return typeString;
	}

	public byte[] getData() {
		return data;
	}

	public byte getByte(int offset) {
		return data[offset];
	}

	public int getInt1(int offset) {
		return data[offset] & 0xff;
	}

	public int getInt2(int offset) {
		return ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff);
	}

	public int getInt4(int offset) {
		return ((data[offset] & 0xff) << 24) | ((data[offset + 1] & 0xff) << 16) | ((data[offset + 2] & 0xff) << 8)
				| (data[offset + 3] & 0xff);
	}

	public String getString4(int offset) {
		StringBuilder sb = new StringBuilder(4);
		sb.append((char) data[offset]);
		sb.append((char) data[offset + 1]);
		sb.append((char) data[offset + 2]);
		sb.append((char) data[offset + 3]);
		return sb.toString();
	}

	public boolean isType(String typeName) {
		return typeString.equals(typeName);
	}

}
