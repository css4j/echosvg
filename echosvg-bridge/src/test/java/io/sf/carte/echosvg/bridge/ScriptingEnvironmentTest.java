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
package io.sf.carte.echosvg.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.script.Interpreter;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.RunnableQueue;

public class ScriptingEnvironmentTest {

	private static final String DOC_URL = "http://example.com/doc.svg";
	private static final String EVIL_URL = "file://etc/passwd";
	private static final String SVG = "<svg xmlns='http://www.w3.org/2000/svg' width='10' height='10'/>";

	private RunnableQueue runnableQueue;

	@BeforeEach
	public void setUp() {
		runnableQueue = RunnableQueue.createRunnableQueue();
		runnableQueue.resumeExecution();
	}

	@AfterEach
	public void tearDown() {
		if (runnableQueue != null && runnableQueue.getThread() != null) {
			runnableQueue.getThread().halt();
		}
	}

	private ScriptingEnvironment createEnvironment(RecordingUserAgent userAgent)
			throws IOException {
		return createEnvironment(userAgent, DOC_URL);
	}

	private ScriptingEnvironment createEnvironment(RecordingUserAgent userAgent, String docUrl)
			throws IOException {
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory();
		SVGDocument doc = factory.createSVGDocument(docUrl, new StringReader(SVG));

		UpdateManager updateManager = mock(UpdateManager.class);
		when(updateManager.getUpdateRunnableQueue()).thenReturn(runnableQueue);

		BridgeContext ctx = mock(BridgeContext.class);
		when(ctx.getUserAgent()).thenReturn(userAgent);
		when(ctx.getDocument()).thenReturn(doc);
		when(ctx.getUpdateManager()).thenReturn(updateManager);

		return new ScriptingEnvironment(ctx);
	}

	@Test
	public void testGetURLBlocksCrossOriginRequest() throws Exception {
		RecordingUserAgent userAgent = new RecordingUserAgent();
		ScriptingEnvironment scriptEnv = createEnvironment(userAgent);

		Interpreter mockInterpreter = mock(Interpreter.class);
		Window window = scriptEnv.getWindow(mockInterpreter, "text/ecmascript");

		final CountDownLatch done = new CountDownLatch(1);
		URLDoneInfo info = new URLDoneInfo();

		window.getURL(EVIL_URL, new Window.URLResponseHandler() {

			public void getURLDone(boolean success, String mime, String content) {
				info.setSuccess(success);
				info.setContent(content);
				done.countDown();
			}

		});

		assertTrue(done.await(10, TimeUnit.SECONDS), "getURL callback was never invoked");
		assertTrue(userAgent.checked, "checkLoadExternalResource must be called");
		assertEquals(EVIL_URL,
				userAgent.checkedResource == null ? null : userAgent.checkedResource.toString());
		assertFalse(info.success, "cross-origin getURL() must not succeed");
		assertNull(info.content, "no content should be returned for a blocked request");
	}

	@Test
	public void testGetURLAllowsSameOriginResource() throws Exception {
		RecordingUserAgent userAgent = new RecordingUserAgent();
		ScriptingEnvironment scriptingEnvironment = createEnvironment(userAgent);

		Interpreter mockInterpreter = mock(Interpreter.class);
		Window window = scriptingEnvironment.getWindow(mockInterpreter, "text/ecmascript");

		final CountDownLatch done = new CountDownLatch(1);
		URLDoneInfo info = new URLDoneInfo();

		// data: URIs are always permitted by DefaultExternalResourceSecurity.
		window.getURL("data:text/plain,hello", new Window.URLResponseHandler() {

			public void getURLDone(boolean success, String mime, String content) {
				info.setSuccess(success);
				info.setContent(content);
				done.countDown();
			}

		});

		assertTrue(done.await(10, TimeUnit.SECONDS), "getURL callback was never invoked");
		assertTrue(userAgent.checked, "checkLoadExternalResource must be called");
		assertTrue(info.success, "same-origin/data getURL() should succeed");
		assertEquals("hello", info.content,
				"data: resource content should be returned to the callback");
	}

	@Test
	public void testPostURLBlocksCrossOriginRequest() throws Exception {
		RecordingUserAgent userAgent = new RecordingUserAgent();
		ScriptingEnvironment scriptEnv = createEnvironment(userAgent);

		Interpreter mockInterpreter = mock(Interpreter.class);
		Window window = scriptEnv.getWindow(mockInterpreter, "text/ecmascript");

		final CountDownLatch done = new CountDownLatch(1);
		URLDoneInfo info = new URLDoneInfo();

		window.postURL(EVIL_URL, "attacker-controlled-body", new Window.URLResponseHandler() {

			public void getURLDone(boolean success, String mime, String content) {
				info.setSuccess(success);
				info.setContent(content);
				done.countDown();
			}

		}, "application/x-malicious", null);

		assertTrue(done.await(10, TimeUnit.SECONDS), "postURL callback was never invoked");
		assertTrue(userAgent.checked, "checkLoadExternalResource must be called");
		assertEquals(EVIL_URL,
				userAgent.checkedResource == null ? null : userAgent.checkedResource.toString());
		assertFalse(info.success, "cross-origin postURL() must not succeed");
		assertNull(info.content, "no content should be returned for a blocked request");
	}

	private static class URLDoneInfo {

		boolean success;
		String content;

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public void setContent(String content) {
			this.content = content;
		}

	}

	private static class RecordingUserAgent extends UserAgentAdapter {

		private boolean checked;
		private ParsedURL checkedResource;

		@Override
		public void checkLoadExternalResource(ParsedURL resourceURL, ParsedURL docURL)
				throws SecurityException {
			checked = true;
			checkedResource = resourceURL;
			super.checkLoadExternalResource(resourceURL, docURL);
		}

	}

}
