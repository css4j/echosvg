/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.bridge;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.carte.echosvg.gvt.GraphicsNode;
import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;
import io.sf.carte.echosvg.util.ParsedURL;
import io.sf.carte.echosvg.util.XMLResourceDescriptor;

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

public class ExternalResourcesTest extends AbstractTest implements ErrorConstants {
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
	public static final String testNS = "http://xml.apache.org/batik/test";

	/**
	 * Id of the element where unsecure content is inserted
	 */
	public static final String INSERTION_POINT_ID = "insertionPoint";

	/**
	 * Location of test files in filesystem.
	 */
	public static final String FILE_DIR = "test-resources/io/sf/carte/echosvg/bridge/";
	/**
	 * Controls whether the test works in secure mode or not
	 */
	protected boolean secure = true;

	String svgURL;

	@Override
	public void setId(String id) {
		super.setId(id);
		String file = id;
		int idx = file.indexOf('.');
		if (idx != -1) {
			file = file.substring(0, idx);
		}
		svgURL = resolveURL(FILE_DIR + file + ".svg");
	}

	public Boolean getSecure() {
		return secure ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setSecure(Boolean secure) {
		this.secure = secure;
	}

	/**
	 * Resolves the input string as follows. + First, the string is interpreted as a
	 * file description. If the file exists, then the file name is turned into a
	 * URL. + Otherwise, the string is supposed to be a URL. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	protected String resolveURL(String url) {
		// Is url a file?
		File f = (new File(url)).getAbsoluteFile();
		if (f.getParentFile().exists()) {
			try {
				return f.toURI().toURL().toString();
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException();
			}
		}

		// url is not a file. It must be a regular URL...
		try {
			return (new URL(url)).toString();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(url);
		}
	}

	/**
	 * This test uses a list of ids found in the test document. These ids reference
	 * elements found in a defs section. For each such element, the test will
	 * attempt to insert the target id at a given insertion point. That insertion
	 * should cause a SecurityException. If so, the test passes. Otherwise, the test
	 * will fail
	 */
	@Override
	public TestReport runImpl() throws Exception {
		DefaultTestReport report = new DefaultTestReport(this);

		//
		// First step:
		//
		// Load the input SVG into a Document object
		//
		String parserClassName = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parserClassName);
		Document doc = null;

		try {
			doc = f.createDocument(svgURL);
		} catch (IOException e) {
			report.setErrorCode(ERROR_CANNOT_LOAD_SVG_DOCUMENT);
			report.addDescriptionEntry(ENTRY_KEY_ERROR_DESCRIPTION, e.getMessage());
			report.setPassed(false);
			return report;
		} catch (Exception e) {
			report.setErrorCode(ERROR_CANNOT_LOAD_SVG_DOCUMENT);
			report.addDescriptionEntry(ENTRY_KEY_ERROR_DESCRIPTION, e.getMessage());
			report.setPassed(false);
			return report;
		}

		List<String> failures = new ArrayList<>();

		//
		// Do an initial processing to validate that the external
		// stylesheet causes a SecurityException
		//
		MyUserAgent userAgent = buildUserAgent();
		GVTBuilder builder = new GVTBuilder();
		BridgeContext ctx = new BridgeContext(userAgent);
		ctx.setDynamic(true);

		// We expect either a SecurityException or a BridgeException
		// with ERR_URI_UNSECURE.
		Throwable th = null;
		try {
			GraphicsNode gn = builder.build(ctx, doc);
			gn.getBounds();
			th = userAgent.getDisplayError();
		} catch (BridgeException e) {
			th = e;
		} catch (SecurityException e) {
			th = e;
		} catch (Throwable t) {
			th = t;
		}
		if (th == null) {
			if (secure)
				failures.add(EXTERNAL_STYLESHEET_ID);
		} else if (th instanceof SecurityException) {
			if (!secure)
				failures.add(EXTERNAL_STYLESHEET_ID);
		} else if (th instanceof BridgeException) {
			BridgeException be = (BridgeException) th;
			if (!secure || (secure && !ERR_URI_UNSECURE.equals(be.getCode()))) {
				report.setErrorCode(ERROR_WHILE_PROCESSING_SVG_DOCUMENT);
				report.addDescriptionEntry(ENTRY_KEY_ERROR_DESCRIPTION, be.getMessage());
				report.setPassed(false);
				return report;
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
		if (idList == null || "".equals(idList)) {
			report.setErrorCode(ERROR_NO_ID_LIST);
			report.setPassed(false);
			return report;
		}

		StringTokenizer st = new StringTokenizer(idList, ",");
		String[] ids = new String[st.countTokens()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = st.nextToken().trim();
		}

		for (String id : ids) {
			userAgent = buildUserAgent();
			builder = new GVTBuilder();
			ctx = new BridgeContext(userAgent);
			ctx.setDynamic(true);

			Document cloneDoc = (Document) doc.cloneNode(true);
			Element insertionPoint = cloneDoc.getElementById(INSERTION_POINT_ID);

			if (insertionPoint == null) {
				report.setErrorCode(ERROR_NO_INSERTION_POINT_IN_DOCUMENT);
				report.addDescriptionEntry(ENTRY_KEY_INSERTION_POINT_ID, INSERTION_POINT_ID);
				report.setPassed(false);
				return report;
			}

			Element target = cloneDoc.getElementById(id);

			if (target == null) {
				report.setErrorCode(ERROR_TARGET_ID_NOT_FOUND);
				report.addDescriptionEntry(ENTRY_KEY_TARGET_ID, id);
				report.setPassed(false);
				return report;
			}

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
			} catch (Throwable t) {
				th = t;
			}
			if (th == null) {
				if (secure)
					failures.add(id);
			} else if (th instanceof SecurityException) {
				if (!secure)
					failures.add(id);
			} else if (th instanceof BridgeException) {
				BridgeException be = (BridgeException) th;
				if (!secure || (secure && !ERR_URI_UNSECURE.equals(be.getCode()))) {
					report.setErrorCode(ERROR_WHILE_PROCESSING_SVG_DOCUMENT);
					report.addDescriptionEntry(ENTRY_KEY_ERROR_DESCRIPTION, be.getMessage());
					report.setPassed(false);
					return report;
				}
			} else {
				// Some unknown exception was displayed...
				report.setErrorCode(ERROR_WHILE_PROCESSING_SVG_DOCUMENT);
				report.addDescriptionEntry(ENTRY_KEY_ERROR_DESCRIPTION, th.getMessage());
				report.setPassed(false);
				return report;
			}

		}

		if (failures.size() == 0) {
			return reportSuccess();
		}

		if (secure) {
			report.setErrorCode(ERROR_UNTHROWN_SECURITY_EXCEPTIONS);
			for (String failure : failures) {
				report.addDescriptionEntry(ENTRY_KEY_EXPECTED_EXCEPTION_ON, failure);
			}
		} else {
			report.setErrorCode(ERROR_THROWN_SECURITY_EXCEPTIONS);
			for (String failure : failures) {
				report.addDescriptionEntry(ENTRY_KEY_UNEXPECTED_EXCEPTION_ON, failure);
			}
		}

		report.setPassed(false);
		return report;
	}

	protected interface MyUserAgent extends UserAgent {
		Exception getDisplayError();
	}

	protected MyUserAgent buildUserAgent() {
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
