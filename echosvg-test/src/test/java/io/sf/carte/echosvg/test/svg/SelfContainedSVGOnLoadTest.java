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
package io.sf.carte.echosvg.test.svg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.bridge.BaseScriptingEnvironment;
import io.sf.carte.echosvg.bridge.BridgeContext;
import io.sf.carte.echosvg.bridge.GVTBuilder;
import io.sf.carte.echosvg.bridge.UserAgent;
import io.sf.carte.echosvg.bridge.UserAgentAdapter;
import io.sf.carte.echosvg.test.TestLocations;

/**
 * This test takes an SVG file as an input. It processes the input SVG (meaning
 * it turns it into a GVT tree) and then dispatches the 'onload' event.
 * 
 * The SVG input file should contain script that will position the result of the
 * test in the DOM using the following namespace: <br>
 * xmlns:test="https://css4j.github.io/echosvg/test" <br>
 * 
 * The result should be set in the <code>result</code> attribute of the
 * &lt;testResult&gt; element by the script embeded in the input SVG test file.
 * <br>
 * 
 * Sample input SVG file:<br>
 * <code>
 * &lt;svg ... onload="runTest(evt)" xmlns:test="https://css4j.github.io/echosvg/test" &gt;
 *   &lt;script type="text/ecmascript"&gt;
 *   function runTest(evt) {
 *      ...; // do some test
 *      var rootSvg = document.getDocumentElement();
 *      var result = document.createElementNS("https://css4j.github.io/echosvg/test",
 *                                            "testResult");
 *      result.setAttributeNS(null, "result", "failed");
 *      result.setAttributeNS(null, "errorCode", "io.sf.carte.echosvg.css.dom.wrong.computed.value");
 *      rootSvg.appendChild(result);
 *   }
 * &lt;/script&gt;
 * &lt;/svg&gt;</code>
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@SuppressWarnings({ "removal" })
public class SelfContainedSVGOnLoadTest {
	/**
	 * Error when the input file cannot be loaded into a Document object {0} =
	 * IOException message
	 */
	public static final String ERROR_CANNOT_LOAD_SVG_DOCUMENT = "SelfContainedSVGOnLoadTest.error.cannot.load.svg.document";

	/**
	 * Error while building the GVT tree or dispatching the 'onload' event.
	 */
	public static final String ERROR_WHILE_PROCESSING_SVG_DOCUMENT = "SelfContainedSVGOnLoadTest.error.while.processing.svg.document";

	/**
	 * There is either 0 or more than one &lt;testResult&gt; elements in the
	 * document after dispatching the onload event.
	 */
	public static final String ERROR_UNEXPECTED_NUMBER_OF_TEST_RESULT_ELEMENTS = "SelfContainedSVGOnLoadTest.error.unexpected.number.of.test.result.elements";

	/**
	 * The 'result' attribute value is neither 'passed' nor 'failed'
	 */
	public static final String ERROR_UNEXPECTED_RESULT_VALUE = "SelfContainedSVGOnLoadTest.error.unexpected.result.value";

	/**
	 * The result was 'failed' but there was no 'errorCode' attribute or it was the
	 * empty string
	 */
	public static final String ERROR_MISSING_OR_EMPTY_ERROR_CODE_ON_FAILED_TEST = "SelfContainedSVGOnLoadTest.error.missing.or.empty.error.code.on.failed.test";

	/**
	 * Entry describing the error
	 */
	public static final String ENTRY_KEY_ERROR_DESCRIPTION = "SelfContainedSVGOnLoadTest.entry.key.error.description";

	/**
	 * Entry describing the number of testResult elements found in the document
	 * after dispatching onload.
	 */
	public static final String ENTRY_KEY_NUMBER_OF_TEST_RESULT_ELEMENTS = "SelfContainedSVGOnLoadTest.entry.key.number.of.test.result.elements";

	/**
	 * Entry describing the result value (different from 'passed' or 'failed' found
	 * in the 'result' attribute.
	 */
	public static final String ENTRY_KEY_RESULT_VALUE = "SelfContainedSVGOnLoadTest.entry.key.result.value";

	/**
	 * Test Namespace
	 */
	public static final String testNS = "https://css4j.github.io/echosvg/test";

	/**
	 * Test Constants
	 */
	public static final String TAG_TEST_RESULT = "testResult";
	public static final String TAG_ERROR_DESCRIPTION_ENTRY = "errorDescriptionEntry";
	public static final String ATTRIBUTE_RESULT = "result";
	public static final String ATTRIBUTE_KEY = "id";
	public static final String ATTRIBUTE_VALUE = "value";
	public static final String TEST_RESULT_PASSED = "passed";
	public static final String TEST_RESULT_FAILED = "failed";

	/**
	 * Default constructor
	 */
	protected SelfContainedSVGOnLoadTest() {
	}

	/**
	 * Resolves the input string as a URL. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	protected String resolveURL(String url) {
		final String resName = TestLocations.PROJECT_ROOT_URL + url;
		URL urlRes;
		try {
			urlRes = new URL(resName);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(url);
		}
		return urlRes.toExternalForm();
	}

	/**
	 * Load the SVG and run this test. The test goes through the following
	 * steps:
	 * <ul>
	 * <li>load the input SVG into a Document</li>
	 * <li>build the GVT tree corresponding to the Document and dispatch the
	 * 'onload' event</li>
	 * <li>looks for one and only one &lt;testResult&gt; element in the Document.
	 * This is used to build the returned TestReport</li>
	 * </ul>
	 *
	 * @param svgURL the URL string for the SVG document being tested
	 */
	public void testSVGOnLoad(String svgURL) throws Exception {
		testSVGOnLoad(svgURL, "");
	}

	/**
	 * Load the SVG and run this test. The test goes through the following
	 * steps:
	 * <ul>
	 * <li>load the input SVG into a Document</li>
	 * <li>build the GVT tree corresponding to the Document and dispatch the
	 * 'onload' event</li>
	 * <li>looks for one and only one &lt;testResult&gt; element in the Document.
	 * This is used to build the returned TestReport</li>
	 * </ul>
	 *
	 * @param svgURL the URL string for the SVG document being tested
	 * @param expectedError the expected error, if any.
	 * @throws Exception 
	 */
	public void testSVGOnLoad(String svgURL, String expectedError) throws Exception {

		//
		// First step:
		//
		// Load the input SVG into a Document object
		//
		Document doc = createDocument(svgURL);

		//
		// Second step:
		//
		// Now that the SVG file has been loaded, build
		// a GVT Tree from it
		//
		UserAgent userAgent = buildUserAgent();
		GVTBuilder builder = new GVTBuilder();
		BridgeContext ctx = new BridgeContext(userAgent);
		ctx.setDynamic(true);

		builder.build(ctx, doc);
		BaseScriptingEnvironment scriptEnvironment = new BaseScriptingEnvironment(ctx);
		scriptEnvironment.loadScripts();
		scriptEnvironment.dispatchSVGLoadEvent();

		//
		// Final step:
		//
		// Look for one and only one <testResult> element
		//
		NodeList testResultList = doc.getElementsByTagNameNS(testNS, TAG_TEST_RESULT);

		if (!SelfContainedSVGOnLoadTest.ERROR_UNEXPECTED_NUMBER_OF_TEST_RESULT_ELEMENTS.equals(expectedError)) {
			// Check that there is one and only one testResult element
			assertEquals(1, testResultList.getLength());
		} else {
			assertNotEquals(1, testResultList.getLength());
			return;
		}

		Element testResult = (Element) testResultList.item(0);

		// Now, get the result attribute. Would be either "passed" or "failed"
		String result = testResult.getAttributeNS(null, ATTRIBUTE_RESULT);

		if (!TEST_RESULT_PASSED.equals(result)) {
			String errorCode = testResult.getAttributeNS(null, "errorCode");
			assertEquals("Failed with unexpected error code,", expectedError, errorCode);
		} else if (expectedError.length() > 0) {
			fail("Script was loaded, but expected error: " + expectedError);
		}

	}

	@SuppressWarnings({ "deprecation" })
	private Document createDocument(String svgURL) throws Exception {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Document>() {
				@Override
				public Document run() throws Exception {
					SAXSVGDocumentFactory f = new SAXSVGDocumentFactory();

					String uri = svgURL;
					if (svgURL.startsWith("io/sf")) {
						URL url = getClass().getClassLoader().getResource(svgURL);

						if (url == null) {
							fail("Failed to load: " + svgURL);
						}

						uri = url.toExternalForm();
					}
					Document doc = f.createDocument(uri);
					return doc;
				}
			});
		} catch (PrivilegedActionException pae) {
			throw pae.getException();
		}
	}

	/**
	 * Give subclasses a chance to build their own UserAgent
	 */
	protected UserAgent buildUserAgent() {
		return new UserAgentAdapter();
	}

}
