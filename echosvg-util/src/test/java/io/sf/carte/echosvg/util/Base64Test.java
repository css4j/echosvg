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
package io.sf.carte.echosvg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * This test validates that the Base64 encoder/decoders work properly.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Base64Test {

	@Test
	public void testB64_1() throws Exception {
		performTest("B64.1", "ENCODE", "zeroByte", "zeroByte.64");
	}

	@Test
	public void testB64_2() throws Exception {
		performTest("B64.2", "DECODE", "zeroByte.64", "zeroByte");
	}

	@Test
	public void testB64_3() throws Exception {
		performTest("B64.3", "ROUND", "zeroByte", null);
	}

	@Test
	public void testB64_4() throws Exception {
		performTest("B64.4", "ENCODE", "oneByte", "oneByte.64");
	}

	@Test
	public void testB64_5() throws Exception {
		performTest("B64.5", "DECODE", "oneByte.64", "oneByte");
	}

	@Test
	public void testB64_6() throws Exception {
		performTest("B64.6", "ROUND", "oneByte", null);
	}

	@Test
	public void testB64_7() throws Exception {
		performTest("B64.4", "ENCODE", "twoByte", "twoByte.64");
	}

	@Test
	public void testB64_8() throws Exception {
		performTest("B64.4", "DECODE", "twoByte.64", "twoByte");
	}

	@Test
	public void testB64_9() throws Exception {
		performTest("B64.9", "ROUND", "twoByte", null);
	}

	@Test
	public void testB64_10() throws Exception {
		performTest("B64.10", "ENCODE", "threeByte", "threeByte.64");
	}

	@Test
	public void testB64_11() throws Exception {
		performTest("B64.11", "DECODE", "threeByte.64", "threeByte");
	}

	@Test
	public void testB64_12() throws Exception {
		performTest("B64.12", "ROUND", "threeByte", null);
	}

	@Test
	public void testB64_13() throws Exception {
		performTest("B64.13", "ENCODE", "fourByte", "fourByte.64");
	}

	@Test
	public void testB64_14() throws Exception {
		performTest("B64.14", "DECODE", "fourByte.64", "fourByte");
	}

	@Test
	public void testB64_15() throws Exception {
		performTest("B64.15", "ROUND", "fourByte", null);
	}

	@Test
	public void testB64_16() throws Exception {
		performTest("B64.16", "ENCODE", "tenByte", "tenByte.64");
	}

	@Test
	public void testB64_17() throws Exception {
		performTest("B64.17", "DECODE", "tenByte.64", "tenByte");
	}

	@Test
	public void testB64_18() throws Exception {
		performTest("B64.18", "ROUND", "tenByte", null);
	}

	@Disabled
	@Test
	public void testB64_19() throws Exception {
		performTest("B64.19", "ENCODE", "small", "small.64");
	}

	@Disabled
	@Test
	public void testB64_20() throws Exception {
		performTest("B64.20", "DECODE", "small.64", "small");
	}

	@Test
	public void testB64_21() throws Exception {
		performTest("B64.21", "ROUND", "small", null);
	}

	@Test
	public void testB64_22() throws Exception {
		performTest("B64.22", "ENCODE", "medium", "medium.64");
	}

	@Test
	public void testB64_23() throws Exception {
		performTest("B64.23", "DECODE", "medium.64", "medium");
	}

	@Test
	public void testB64_24() throws Exception {
		performTest("B64.24", "DECODE", "medium.pc.64", "medium");
	}

	@Test
	public void testB64_25() throws Exception {
		performTest("B64.25", "ROUND", "medium", null);
	}

	@Test
	public void testB64_26() throws Exception {
		performTest("B64.26", "ROUND", "large", null);
	}

	private void performTest(String id, String action, String in, String ref) {
		performTestCont(id, action, in != null ? getResource(in) : null, ref != null ? getResource(ref) : null);
	}

	private void performTestCont(String id, String action, URL in, URL ref) {
		try {
			runTestCase(id, action, in, ref);
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
	}

	private URL getResource(String name) {
		return Base64Test.class.getResource(name);
	}

	private void runTestCase(String id, String action, URL in, URL ref) throws IOException {
		InputStream inIS = in.openStream();

		if (action.equals("ROUND"))
			ref = in;
		else if (!action.equals("ENCODE") && !action.equals("DECODE")) {
			fail("Error bad action string for test: " + id);
		}

		InputStream refIS = ref.openStream();

		if (action.equals("ENCODE") || action.equals("ROUND")) {
			// We need to encode the incomming data
			PipedOutputStream pos = new PipedOutputStream();
			OutputStream os = new Base64EncoderStream(pos);

			// Copy the input to the Base64 Encoder (in a seperate thread).
			Thread t = new StreamCopier(inIS, os);

			// Read that from the piped output stream.
			inIS = new PipedInputStream(pos);
			t.start();
		}

		if (action.equals("DECODE") || action.equals("ROUND")) {
			inIS = new Base64DecodeStream(inIS);
		}

		int mismatch = compareStreams(inIS, refIS, action.equals("ENCODE"));

		assertEquals(-1, mismatch, "Computed answer differed from reference at byte: " + mismatch);
	}

	/**
	 * Returns true if the contents of <code>is1</code> match the contents of
	 * <code>is2</code>
	 */
	public static int compareStreams(InputStream is1, InputStream is2, boolean skipws) {
		return Base64Util.compareStreams(is1, is2, skipws);
	}

	static class StreamCopier extends Thread {

		InputStream src;
		OutputStream dst;

		public StreamCopier(InputStream src, OutputStream dst) {
			this.src = src;
			this.dst = dst;
		}

		@Override
		public void run() {
			try {
				byte[] data = new byte[1000];
				while (true) {
					int len = src.read(data, 0, data.length);
					if (len == -1)
						break;

					dst.write(data, 0, len);
				}
			} catch (IOException ioe) {
				// Nothing
			}
			try {
				dst.close();
			} catch (IOException ioe) {
				// Nothing
			}
		}

	}

}
