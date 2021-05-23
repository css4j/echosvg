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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.util.ParsedURL;

/**
 * This test validates that SecurityExceptions are generated when the user is
 * trying the access external resources and the UserAgent disallows that.
 *
 * In the following, 'unsecure' means an external resource coming from a
 * different location than the file referencing it.
 *
 * This test works with an SVG file containing an unsecure stylesheet and a set
 * of unsecure elements of all kinds, such as &lt;image&gt; &lt;use&gt; or
 * &lt;feImage&gt;. All these elements are defined in a defs section. The test
 * tries to load the document and validates that a SecurityException is thrown
 * (because of the unsecure stylesheet). Then, the test iterates over the
 * various unsecure elements, inserting them into the document outside the defs
 * section, which should result in a SecurityException in each case.
 *
 * There is a property (secure) to have the test work the opposite way and check
 * that no SecurityException is thrown if access to external resources is
 * allowed.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */

public class ExternalResourcesTest implements ErrorConstants {
	/**
	 * Error when the input file cannot be loaded into a Document object {0} =
	 * IOException message
	 */
	public static final String ERROR_CANNOT_LOAD_SVG_DOCUMENT = "ExternalResourcesTest.error.cannot.load.svg.document";

	/**
	 * Error while processing the document {0} = BridgeException message
	 */
	public static final String ERROR_WHILE_PROCESSING_SVG_DOCUMENT = "ExternalResourcesTest.error.while.processing.svg.document";

	/**
	 * Error: an expected exception was not thrown {0} = List of ids for which the
	 * exception was not thrown
	 */
	public static final String ERROR_UNTHROWN_SECURITY_EXCEPTIONS = "ExternalResourcesTest.error.unthrown.security.exceptions";

	/**
	 * Error: an unexpected exception was thrown {0} = List of ids for which an
	 * exception was thrown
	 */
	public static final String ERROR_THROWN_SECURITY_EXCEPTIONS = "ExternalResourcesTest.error.thrown.security.exceptions";

	/**
	 * Error when the insertion point cannot be found in the test document {0} =
	 * insertion point id
	 */
	public static final String ERROR_NO_INSERTION_POINT_IN_DOCUMENT = "ExternalResourceTest.error.no.insertion.point.in.document";

	/**
	 * Error when the test could not find a list of ids for testing
	 */
	public static final String ERROR_NO_ID_LIST = "ExternalResourceTest.error.no.id.list";

	/**
	 * Error when one of the target id cannot be found {0} = id which was not found
	 */
	public static final String ERROR_TARGET_ID_NOT_FOUND = "ExternalResourcesTest.error.target.id.not.found";

	/**
	 * Entry describing the error
	 */
	public static final String ENTRY_KEY_ERROR_DESCRIPTION = "ExternalResourcesTest.entry.key.error.description";

	public static final String ENTRY_KEY_INSERTION_POINT_ID = "ExternalResourcesTest.entry.key.insertion.point.id";

	public static final String ENTRY_KEY_TARGET_ID = "ExternalResourcesTest.entry.target.id";

	public static final String ENTRY_KEY_EXPECTED_EXCEPTION_ON = "ExternalResourcesTest.entry.key.expected.exception.on";

	public static final String ENTRY_KEY_UNEXPECTED_EXCEPTION_ON = "ExternalResourcesTest.entry.key.unexpected.exception.on";

	/**
	 * Pseudo id for the external stylesheet test
	 */
	public static final String EXTERNAL_STYLESHEET_ID = "external-stylesheet";

	/**
	 * Test Namespace
	 */
	public static final String testNS = "https://css4j.github.io/echosvg/test";

	/**
	 * Id of the element where unsecure content is inserted
	 */
	public static final String INSERTION_POINT_ID = "insertionPoint";

	/**
	 * Location of bridge test files in filesystem.
	 */
	public static final String FILE_DIR = "test-resources/io/sf/carte/echosvg/bridge/";

	String svgURL;

	/**
	 * Resolves the input string. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	private String resolveURL(String basename) {
		final String resName = TestLocations.PROJECT_ROOT_URL + FILE_DIR + basename + ".svg";
		URL url;
		try {
			url = new URL(resName);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(resName);
		}
		return url.toExternalForm();
	}

	/**
	 * This test uses a list of ids found in the test document. These ids reference
	 * elements found in a defs section. For each such element, the test will
	 * attempt to insert the target id at a given insertion point. That insertion
	 * should cause a SecurityException. If so, the test passes. Otherwise, the test
	 * will fail
	 */
	@org.junit.Test
	public void testSecure() throws IOException {
		runTest("externalResourcesAccess", true);
	}

	@org.junit.Test
	public void testUnsecure() throws IOException {
		runTest("externalResourcesAccess", false);
	}

	public void runTest(String file, boolean secure) throws BridgeException, IOException {

		int idx = file.indexOf('.');
		if (idx != -1) {
			file = file.substring(0, idx);
		}
		svgURL = resolveURL(file);
		//
		// First step:
		//
		// Load the input SVG into a Document object
		//
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory();
		Document doc = f.createDocument(svgURL);

		//
		// Do an initial processing to validate that the external
		// stylesheet causes a SecurityException
		//
		MyUserAgent userAgent = buildUserAgent(secure);
		GVTBuilder builder = new GVTBuilder();
		BridgeContext ctx = new BridgeContext(userAgent);
		ctx.setDynamic(true);

		// We expect either a SecurityException or a BridgeException
		// with ERR_URI_UNSECURE.
		Exception th = null;
		try {
			GraphicsNode gn = builder.build(ctx, doc);
			gn.getBounds();
			th = userAgent.getDisplayError();
		} catch (BridgeException e) {
			th = e;
		} catch (SecurityException e) {
			th = e;
		}
		if (th == null) {
			if (secure)
				fail("Expected either a SecurityException or a BridgeException");
		} else if (th instanceof SecurityException) {
			if (!secure)
				throw (SecurityException) th;
		} else if (th instanceof BridgeException) {
			BridgeException be = (BridgeException) th;
			if (!secure || (secure && !ERR_URI_UNSECURE.equals(be.getCode()))) {
				throw be;
			}
		}

		//
		// Remove the stylesheet from the document
		//
		Node child = doc.getFirstChild();
		Node next = null;
		while (child != null) {
			next = child.getNextSibling();
			if (child.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
				doc.removeChild(child);
			}
			child = next;
		}

		//
		// Now, get the list of ids to be checked
		//
		Element root = doc.getDocumentElement();
		String idList = root.getAttributeNS(testNS, "targetids");
		assertNotNull(idList);
		assertNotEquals(0, idList.length());

		StringTokenizer st = new StringTokenizer(idList, ",");
		String[] ids = new String[st.countTokens()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = st.nextToken().trim();
		}

		for (String id : ids) {
			userAgent = buildUserAgent(secure);
			builder = new GVTBuilder();
			ctx = new BridgeContext(userAgent);
			ctx.setDynamic(true);

			Document cloneDoc = (Document) doc.cloneNode(true);
			Element insertionPoint = cloneDoc.getElementById(INSERTION_POINT_ID);

			assertNotNull(insertionPoint);

			Element target = cloneDoc.getElementById(id);

			assertNotNull(target);

			insertionPoint.appendChild(target);
			th = null;
			try {
				GraphicsNode gn = builder.build(ctx, cloneDoc);
				gn.getBounds();
				th = userAgent.getDisplayError();
			} catch (BridgeException e) {
				th = e;
			} catch (SecurityException e) {
				th = e;
			}
			if (th == null) {
				if (secure)
					fail("Expected either a SecurityException or a BridgeException");
			} else if (th instanceof SecurityException) {
				if (!secure)
					throw (SecurityException) th;
			} else if (th instanceof BridgeException) {
				BridgeException be = (BridgeException) th;
				if (!secure || (secure && !ERR_URI_UNSECURE.equals(be.getCode()))) {
					throw be;
				}
			}

		}

	}

	protected interface MyUserAgent extends UserAgent {
		Exception getDisplayError();
	}

	private MyUserAgent buildUserAgent(boolean secure) {
		if (secure) {
			return new SecureUserAgent();
		} else {
			return new RelaxedUserAgent();
		}
	}

	static class MyUserAgentAdapter extends UserAgentAdapter implements MyUserAgent {
		Exception ex = null;

		@Override
		public void displayError(Exception ex) {
			this.ex = ex;
			super.displayError(ex);
		}

		@Override
		public Exception getDisplayError() {
			return ex;
		}
	}

	static class SecureUserAgent extends MyUserAgentAdapter {
		@Override
		public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL resourcePURL, ParsedURL docPURL) {
			return new NoLoadExternalResourceSecurity();

		}
	}

	static class RelaxedUserAgent extends MyUserAgentAdapter {
		@Override
		public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL resourcePURL, ParsedURL docPURL) {
			return new RelaxedExternalResourceSecurity(resourcePURL, docPURL);

		}
	}

}
