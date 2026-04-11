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
package io.sf.carte.echosvg.svggen.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.sf.carte.echosvg.constants.XMLConstants;

/**
 * Benchmarks the alternative implementations of attribute escape methods.
 * <p>
 * See io.sf.carte.echosvg.svggen.font.SVGFont#encodeEntities
 * </p>
 *
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class AttributeEscapeMark {

	private static final int ITERATIONS = 1000;

	private static final String[] testIn = { "id1", "abcdefghijklm", "abcde&", "ab>cdefg",
			"abcdefg<hijk", "abc''''def", "a\"\"bcdef\"", "\"\"\"\"\"\"\"\"z" };

	private static final String[] testOut = { "id1", "abcdefghijklm", "abcde&amp;", "ab&gt;cdefg",
			"abcdefg&lt;hijk", "abc&apos;&apos;&apos;&apos;def", "a&quot;&quot;bcdef&quot;",
			"&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;z" };

	@Benchmark
	public void markXmlAttributeEscape() {
		for (int i = 0; i < ITERATIONS; i++) {
			for (int j = 0; j < testIn.length; j++) {
				assertEquals(testOut[j], escapeXmlAttribute(testIn[j]));
			}
		}
	}

	@Benchmark
	public void markXmlAttributeEscapeBatik() {
		for (int i = 0; i < ITERATIONS; i++) {
			for (int j = 0; j < testIn.length; j++) {
				assertEquals(testOut[j], encodeEntities(testIn[j]));
			}
		}
	}

	private static void assertEquals(String expected, String actual) {
		if (!expected.equals(actual)) {
			throw new IllegalArgumentException("Expected " + expected + ", found " + actual);
		}
	}

	// New EchoSVG implementation

	private static String escapeXmlAttribute(String s) {
		StringBuilder sb = null;
		int len = s.length();
		for (int i = 0; i < len; i = s.offsetByCodePoints(i, 1)) {
			int cp = s.codePointAt(i);
			switch (cp) {
			case XMLConstants.XML_CHAR_LT:
				sb = appendEntityToBuffer(sb, XMLConstants.XML_ENTITY_LT, s, i, len);
				break;
			case XMLConstants.XML_CHAR_GT:
				sb = appendEntityToBuffer(sb, XMLConstants.XML_ENTITY_GT, s, i, len);
				break;
			case XMLConstants.XML_CHAR_AMP:
				sb = appendEntityToBuffer(sb, XMLConstants.XML_ENTITY_AMP, s, i, len);
				break;
			case XMLConstants.XML_CHAR_APOS:
				sb = appendEntityToBuffer(sb, XMLConstants.XML_ENTITY_APOS, s, i, len);
				break;
			case XMLConstants.XML_CHAR_QUOT:
				sb = appendEntityToBuffer(sb, XMLConstants.XML_ENTITY_QUOT, s, i, len);
				break;
			default:
				if (sb != null) {
					sb.appendCodePoint(cp);
				}
			}
		}
		return sb == null ? s : sb.toString();
	}

	private static StringBuilder appendEntityToBuffer(StringBuilder buf, String string, String text,
			int index, int inilen) {
		if (buf == null) {
			// Make room for a few entities in the buffer
			buf = new StringBuilder(inilen + 16);
			buf.append(text.subSequence(0, index));
		}
		buf.append(string);
		return buf;
	}

	// Original Batik implementation

	private static String encodeEntities(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == XMLConstants.XML_CHAR_LT) {
				sb.append(XMLConstants.XML_ENTITY_LT);
			} else if (s.charAt(i) == XMLConstants.XML_CHAR_GT) {
				sb.append(XMLConstants.XML_ENTITY_GT);
			} else if (s.charAt(i) == XMLConstants.XML_CHAR_AMP) {
				sb.append(XMLConstants.XML_ENTITY_AMP);
			} else if (s.charAt(i) == XMLConstants.XML_CHAR_APOS) {
				sb.append(XMLConstants.XML_ENTITY_APOS);
			} else if (s.charAt(i) == XMLConstants.XML_CHAR_QUOT) {
				sb.append(XMLConstants.XML_ENTITY_QUOT);
			} else {
				sb.append(s.charAt(i));
			}
		}
		return sb.toString();
	}

}
