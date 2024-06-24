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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

/**
 * This test validates the operation of SelfContainedSVGOnLoadTest. It is a test
 * agregation which points to several tests which correspond to expected error
 * conditions. The test simply validates that the expected error conditions are
 * properly detected and reported.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SelfContainedSVGOnLoadTestValidator {
	/**
	 * Error codes
	 */
	public static final String ERROR_UNEXPECTED_TEST_RESULT = "SelfContainedSVGOnLoadTestValidator.error.unexpected.test.result";

	public static final String ERROR_UNEXPECTED_ERROR_CODE = "SelfContainedSVGOnLoadTestValidator.error.unexpected.error.code";

	public static final String ERROR_UNEXPECTED_NUMBER_OF_DESCRIPTION_ENTRIES = "SelfContainedSVGOnLoadTestValidator.error.unexpected.number.of.description.entries";

	public static final String ERROR_UNEXPECTED_TEST_FAILURE = "SelfContainedSVGOnLoadTestValidator.error.unexpected.test.failure";

	public static final String ERROR_UNEXPECTED_DESCRIPTION_ENTRY = "SelfContainedSVGOnLoadTestValidator.error.unexpected.description.entry";

	public static final String ENTRY_ERROR_CODE = "SelfContainedSVGOnLoadTestValidator.entry.error.code";

	public static final String ENTRY_EXPECTED_ERROR_CODE = "SelfContainedSVGOnLoadTestValidator.entry.expected.error.code";

	public static final String ENTRY_NUMBER_OF_DESCRIPTION = "SelfContainedSVGOnLoadTestValidator.entry.number.of.description";

	public static final String ENTRY_EXPECTED_NUMBER_OF_DESCRIPTION = "SelfContainedSVGOnLoadTestValidator.entry.expected.number.of.description";

	public static final String ENTRY_KEY = "SelfContainedSVGOnLoadTestValidator.entry.key";

	public static final String ENTRY_EXPECTED_KEY = "SelfContainedSVGOnLoadTestValidator.entry.expected.key";

	/**
	 * URLs for children tests
	 */
	public static final String invalidURL = "invalidURL";
	public static final String invalidRectRxUnitURL = "io/sf/carte/echosvg/test/svg/invalidRectRxUnit.svg";
	public static final String invalidTestResultElementsNumberURL = "io/sf/carte/echosvg/test/svg/invalidTestResultElementsNumber.svg";
	public static final String unexpectedResultValueURL = "io/sf/carte/echosvg/test/svg/unexpectedResultValue.svg";
	public static final String missingOrEmptyErrorCodeURL = "io/sf/carte/echosvg/test/svg/missingOrEmptyErrorCode.svg";
	public static final String errorURL = "io/sf/carte/echosvg/test/svg/error.svg";
	public static final String errorAndEntriesURL = "io/sf/carte/echosvg/test/svg/errorAndEntries.svg";
	public static final String successURL = "io/sf/carte/echosvg/test/svg/success.svg";

	@Test
	public void testSuccess() throws Exception {
		new SuccessTest().runTest();
	}

	@Test
	public void testCannotLoadSVGDocument() throws Exception {
		new CannotLoadSVGDocument().runTest();
	}

	@Test
	public void testInvalidRectRxUnit() throws Exception {
		new InvalidRectRxUnit().runTest();
	}

	@Test
	public void testInvalidTestResultElementsNumber() throws Exception {
		new InvalidTestResultElementsNumber().runTest();
	}

	@Test
	public void testMissingOrEmptyErrorCode() throws Exception {
		new MissingOrEmptyErrorCode().runTest();
	}

	@Test
	public void testReportError() throws Exception {
		new ReportError().runTest();
	}

	@Test
	public void testReportErrorAndEntries() throws Exception {
		new ReportErrorAndEntries().runTest();
	}

	static class SuccessTest {
		public void runTest() throws Exception {
			SelfContainedSVGOnLoadTest t = new SelfContainedSVGOnLoadTest();
			t.testSVGOnLoad(successURL, "");
		}
	}

	static class DefaultErrorTest extends SuccessTest {
		String svgURL;
		String expectedErrorCode;
		String expectedException;

		public DefaultErrorTest(String svgURL, String expectedErrorCode, String expectedException) {
			this.svgURL = svgURL;
			this.expectedErrorCode = expectedErrorCode;
			this.expectedException = expectedException;
		}

		@Override
		public void runTest() throws Exception {
			SelfContainedSVGOnLoadTest t = new SelfContainedSVGOnLoadTest();
			try {
				t.testSVGOnLoad(svgURL, expectedErrorCode);
			} catch (FileNotFoundException e) {
				assertEquals("SelfContainedSVGOnLoadTest.error.cannot.load.svg.document", expectedErrorCode);
			} catch (RuntimeException e) {
				assertEquals(expectedException, e.getClass().getName());
			}
		}
	}

	static class ReportErrorAndEntries extends DefaultErrorTest {
		public ReportErrorAndEntries() {
			super(errorAndEntriesURL, "can.you.read.this.error.code", null);
		}
	}

	static class ReportError extends DefaultErrorTest {
		public ReportError() {
			super(errorURL, "can.you.read.this.error.code", null);
		}
	}

	static class MissingOrEmptyErrorCode extends DefaultErrorTest {
		public MissingOrEmptyErrorCode() {
			super(missingOrEmptyErrorCodeURL, "", null);
		}
	}

	static class InvalidTestResultElementsNumber extends DefaultErrorTest {
		public InvalidTestResultElementsNumber() {
			super(invalidTestResultElementsNumberURL,
					SelfContainedSVGOnLoadTest.ERROR_UNEXPECTED_NUMBER_OF_TEST_RESULT_ELEMENTS,
					null);
		}
	}

	static class InvalidRectRxUnit extends DefaultErrorTest {
		public InvalidRectRxUnit() {
			super(invalidRectRxUnitURL, SelfContainedSVGOnLoadTest.ERROR_WHILE_PROCESSING_SVG_DOCUMENT,
					"io.sf.carte.echosvg.bridge.BridgeException");
		}
	}

	static class CannotLoadSVGDocument extends DefaultErrorTest {
		public CannotLoadSVGDocument() {
			super(invalidURL, SelfContainedSVGOnLoadTest.ERROR_CANNOT_LOAD_SVG_DOCUMENT,
					null);
		}
	}

}
