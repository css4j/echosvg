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
package io.sf.carte.echosvg.parser;

import java.io.StringReader;

import io.sf.carte.echosvg.test.AbstractTest;
import io.sf.carte.echosvg.test.DefaultTestReport;
import io.sf.carte.echosvg.test.TestReport;

/**
 * To test the length parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LengthParserTest extends AbstractTest {

	protected String sourceLength;
	protected String destinationLength;

	protected StringBuffer buffer;
	protected String resultLength;

	/**
	 * Creates a new LengthParserTest.
	 * 
	 * @param slength The length to parse.
	 * @param dlength The length after serialization.
	 */
	public LengthParserTest(String slength, String dlength) {
		sourceLength = slength;
		destinationLength = dlength;
	}

	@Override
	public TestReport runImpl() throws Exception {
		LengthParser pp = new LengthParser();
		pp.setLengthHandler(new TestHandler());

		try {
			pp.parse(new StringReader(sourceLength));
		} catch (ParseException e) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode("parse.error");
			report.addDescriptionEntry("exception.text", e.getMessage());
			report.setPassed(false);
			return report;
		}

		if (!destinationLength.equals(resultLength)) {
			DefaultTestReport report = new DefaultTestReport(this);
			report.setErrorCode("invalid.parsing.events");
			report.addDescriptionEntry("expected.text", destinationLength);
			report.addDescriptionEntry("generated.text", resultLength);
			report.setPassed(false);
			return report;
		}

		return reportSuccess();
	}

	class TestHandler extends DefaultLengthHandler {
		public TestHandler() {
		}

		@Override
		public void startLength() throws ParseException {
			buffer = new StringBuffer();
		}

		@Override
		public void lengthValue(float v) throws ParseException {
			buffer.append(v);
		}

		@Override
		public void em() throws ParseException {
			buffer.append("em");
		}

		@Override
		public void ex() throws ParseException {
			buffer.append("ex");
		}

		@Override
		public void in() throws ParseException {
			buffer.append("in");
		}

		@Override
		public void cm() throws ParseException {
			buffer.append("cm");
		}

		@Override
		public void mm() throws ParseException {
			buffer.append("mm");
		}

		@Override
		public void pc() throws ParseException {
			buffer.append("pc");
		}

		@Override
		public void pt() throws ParseException {
			buffer.append("pt");
		}

		@Override
		public void px() throws ParseException {
			buffer.append("px");
		}

		@Override
		public void percentage() throws ParseException {
			buffer.append("%");
		}

		@Override
		public void endLength() throws ParseException {
			resultLength = buffer.toString();
		}
	}
}
