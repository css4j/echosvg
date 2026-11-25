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
package io.sf.carte.echosvg.latest.bridge;

import static io.sf.carte.echosvg.util.SVGConstants.SVG_SCRIPT_TYPE_JAVA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.svg.SVGDocument;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.bridge.BaseScriptingEnvironment;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.util.ParsedURL;

public class BaseScriptingEnvironmentTest {

	private static final String SVG = "<svg xmlns='http://www.w3.org/2000/svg' width='10' height='10'/>";
	private static final int[] REDIRECT_CODES = { 300, 301, 302, 303, 305, 307, 308 };

	private HttpServer origin;
	private HttpServer other;
	private final AtomicInteger otherHits = new AtomicInteger();
	private ScriptPolicyUserAgent userAgent;
	private TestBaseScriptingEnvironment scriptEnv;

	@BeforeEach
	public void setUp() throws IOException {
		InetAddress loopback = InetAddress.getByName("127.0.0.1");
		other = HttpServer.create(new InetSocketAddress(loopback, 0), 0);
		other.createContext("/", new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				otherHits.incrementAndGet();
				respond(exchange, 200, null);
			}

		});
		other.start();

		origin = HttpServer.create(new InetSocketAddress(loopback, 0), 0);
		origin.createContext("/script.jar", new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				respond(exchange, 200, null);
			}

		});
		// /same/<code> and /cross/<code> answer with the given status code
		// and a Location on the same origin or on the other server.
		origin.createContext("/same/", new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				respond(exchange, statusCode(exchange), "/script.jar");
			}

		});
		origin.createContext("/cross/", new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				respond(exchange, statusCode(exchange), otherUrl("/evil.jar"));
			}

		});
		origin.createContext("/loop", new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				respond(exchange, 302, "/loop");
			}

		});
		origin.start();

		userAgent = new ScriptPolicyUserAgent(other.getAddress().getPort());
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory();
		SVGDocument doc = factory.createSVGDocument(originUrl("/doc.svg"), new StringReader(SVG));
		BridgeContext mockContext = mock(BridgeContext.class);
		when(mockContext.getUserAgent()).thenReturn(userAgent);
		when(mockContext.getDocument()).thenReturn(doc);
		scriptEnv = new TestBaseScriptingEnvironment(mockContext);
	}

	@AfterEach
	public void tearDown() {
		origin.stop(0);
		other.stop(0);
	}

	private String originUrl(String path) {
		return "http://127.0.0.1:" + origin.getAddress().getPort() + path;
	}

	private String otherUrl(String path) {
		return "http://127.0.0.1:" + other.getAddress().getPort() + path;
	}

	private static int statusCode(HttpExchange exchange) {
		String path = exchange.getRequestURI().getPath();
		return Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
	}

	private static void respond(HttpExchange exchange, int code, String location)
			throws IOException {
		if (location != null) {
			exchange.getResponseHeaders().set("Location", location);
		}
		exchange.sendResponseHeaders(code, -1);
		exchange.close();
	}

	@Test
	public void testNoRedirectDoesNotCheckPolicy() throws IOException {
		scriptEnv.checkScriptRedirects(SVG_SCRIPT_TYPE_JAVA, new URL(originUrl("/script.jar")));
		assertEquals(Collections.emptyList(), userAgent.checked,
				"A response without redirect should not check the policy");
	}

	@Test
	public void testAllowedRedirectTargetIsChecked() throws IOException {
		for (int code : REDIRECT_CODES) {
			userAgent.checked.clear();
			scriptEnv.checkScriptRedirects(SVG_SCRIPT_TYPE_JAVA,
					new URL(originUrl("/same/" + code)));
			assertEquals(Collections.singletonList(originUrl("/script.jar")), userAgent.checked,
					"Target of the " + code + " redirect should be checked");
		}
	}

	@Test
	public void testRedirectRejectedByPolicy() throws IOException {
		for (int code : REDIRECT_CODES) {
			userAgent.checked.clear();
			final URL url = new URL(originUrl("/cross/" + code));
			assertThrows(SecurityException.class,
					() -> scriptEnv.checkScriptRedirects(SVG_SCRIPT_TYPE_JAVA, url),
					"Target of the " + code + " redirect rejected by the script policy must fail");
			assertEquals(Collections.singletonList(otherUrl("/evil.jar")), userAgent.checked,
					"Target of the " + code + " redirect should be checked");
			assertEquals(0, otherHits.get(),
					"Rejected target of the " + code + " redirect must not be contacted");
		}
	}

	@Test
	public void testNotModifiedIsNotARedirect() throws IOException {
		scriptEnv.checkScriptRedirects(SVG_SCRIPT_TYPE_JAVA, new URL(originUrl("/cross/304")));
		assertEquals(Collections.emptyList(), userAgent.checked,
				"304 Not Modified is not a redirect and should not check the policy");
	}

	@Test
	public void testTooManyRedirects() {
		IOException e = assertThrows(IOException.class, () -> scriptEnv
				.checkScriptRedirects(SVG_SCRIPT_TYPE_JAVA, new URL(originUrl("/loop"))),
				"Redirect loop must not be followed forever");
		assertTrue(e.getMessage().contains("Too many redirects"),
				"Unexpected exception for redirect loop: " + e.getMessage());
		assertEquals(TestBaseScriptingEnvironment.getMaxScriptRedirects(), userAgent.checked.size(),
				"Every redirect up to the limit should be checked");
	}

	@Test
	public void testNonHttpUrlIsIgnored() throws IOException {
		scriptEnv.checkScriptRedirects(SVG_SCRIPT_TYPE_JAVA,
				new URL("file:/nonexistent/script.jar"));
		assertEquals(Collections.emptyList(), userAgent.checked,
				"A non HTTP URL should not check the policy");
	}

	private static class ScriptPolicyUserAgent extends UserAgentAdapter {

		final List<String> checked = new CopyOnWriteArrayList<>();
		private final int rejectedPort;

		ScriptPolicyUserAgent(int rejectedPort) {
			this.rejectedPort = rejectedPort;
		}

		@Override
		public void checkLoadScript(String scriptType, ParsedURL scriptURL, ParsedURL docURL) {
			checked.add(scriptURL.toString());
			if (scriptURL.getPort() == rejectedPort) {
				throw new SecurityException("Script from " + scriptURL + " is not allowed");
			}
		}

	}

	private static class TestBaseScriptingEnvironment extends BaseScriptingEnvironment {

		public TestBaseScriptingEnvironment(BridgeContext ctx) {
			super(ctx);
		}

		public static int getMaxScriptRedirects() {
			return BaseScriptingEnvironment.MAX_SCRIPT_REDIRECTS;
		}

		@Override
		public URL checkScriptRedirects(String scriptType, URL url) throws IOException {
			return super.checkScriptRedirects(scriptType, url);
		}

	}

}
