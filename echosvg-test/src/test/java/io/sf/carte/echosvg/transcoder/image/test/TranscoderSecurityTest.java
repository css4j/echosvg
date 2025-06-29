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
package io.sf.carte.echosvg.transcoder.image.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.bridge.BridgeException;
import io.sf.carte.echosvg.test.misc.ScriptUtil;
import io.sf.carte.echosvg.test.misc.TestLocations;
import io.sf.carte.echosvg.transcoder.Transcoder;
import io.sf.carte.echosvg.transcoder.TranscoderException;
import io.sf.carte.echosvg.transcoder.TranscoderInput;
import io.sf.carte.echosvg.transcoder.TranscoderOutput;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;

class TranscoderSecurityTest {

	@BeforeAll
	public static void setUpBeforeAll() {
		ScriptUtil.defaultRhinoShutter();
	}

	@Test
	void testLinkStyleJar() {
		testResource("linkStyleJar.svg", SecurityException.class, null);
	}

	@Test
	void testImageJar() {
		testResource("imageJar.svg", BridgeException.class,
			"The document references an external resource (jar:https://github.com/"
			+ "css4j/echosvg/blob/master/test-resources/io/sf/carte/echosvg/bridge/"
			+ "IWasLoadedToo.jar!/META-INF/MANIFEST.MF) which comes from different "
			+ "location than the document itself. This is not allowed for security "
			+ "reasons and that resource will not be loaded.");
	}

	private void testResource(String fileName, Class<? extends Throwable> exClass, String message) {
		final String resPath = "io/sf/carte/echosvg/transcoder/image/test/" + fileName;
		TestTranscoderSecurity test = new TestTranscoderSecurity();

		try {
			test.test(resPath);
			if (exClass != null) {
				fail("Expected an exception of type: " + exClass.getName());
			}
		} catch (TranscoderException e) {
			Throwable t;
			if (!exClass.equals(e.getClass())) {
				t = e.getCause();
				if (t == null) {
					t = e;
				}
			} else {
				t = e;
			}
			assertEquals(exClass, t.getClass());
			if (message != null) {
				assertEquals(message, t.getMessage());
			}
		} catch (MalformedURLException | RuntimeException e) {
			assertEquals(exClass, e.getClass());
			if (message != null) {
				assertEquals(message, e.getMessage());
			}
		}
	}

	static class TestTranscoderSecurity {

		public void test(String fileName) throws TranscoderException, MalformedURLException {
			URL url = resolveURI(fileName);
			Transcoder transcoder = getTranscoder();
			TranscoderInput input = new TranscoderInput(url.toExternalForm());
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(os);
			transcoder.transcode(input, output);
		}

		Transcoder getTranscoder() {
			return new PNGTranscoder();
		}

		/**
		 * Resolves the input string as a URL.
		 * 
		 * @throws MalformedURLException
		 */
		URL resolveURI(String fileName) throws MalformedURLException {
			String uri;
			if (fileName.startsWith("io/sf")) {
				URL url = getClass().getClassLoader().getResource(fileName);

				if (url == null) {
					fail("Failed to load: " + fileName);
				}

				uri = url.toExternalForm();
			} else {
				uri = TestLocations.PROJECT_ROOT_URL + fileName;
			}
			return new URL(uri);
		}

	}

}
