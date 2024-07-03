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

package io.sf.carte.echosvg.transcoder.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import io.sf.carte.echosvg.transcoder.ErrorHandler;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * An error handler that ignores errors and warnings (but not fatal errors).
 *
 * @author github.com/carlosame
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DummyErrorHandler implements ErrorHandler {

	private List<TranscoderException> errors = null;

	private int warningCount = 0;

	@Override
	public void error(TranscoderException ex) throws TranscoderException {
		prepareErrorList();
		errors.add(ex);
	}

	private void prepareErrorList() {
		if (errors == null) {
			errors = new ArrayList<>();
		}
	}

	@Override
	public void fatalError(TranscoderException ex) throws TranscoderException {
		ex.printStackTrace();
		prepareErrorList();
		errors.add(ex);
	}

	@Override
	public void warning(TranscoderException ex) throws TranscoderException {
		warningCount++;
	}

	/**
	 * Get the list of errors, if any.
	 * 
	 * @return the list of errors, {@code null} if no error was reported.
	 */
	public List<TranscoderException> getErrors() {
		return errors;
	}

	/**
	 * Get the error count.
	 * 
	 * @return the error count.
	 */
	public int getErrorCount() {
		return errors == null ? 0 : errors.size();
	}

	/**
	 * Get the warning count.
	 * 
	 * @return the warning count.
	 */
	public int getWarningCount() {
		return warningCount;
	}

	public void assertErrorCount(int expectedErrorCount) {
		int errCount = getErrorCount();
		if (errCount != expectedErrorCount) {
			if (errors != null) {
				for (TranscoderException ex : errors) {
					ex.printStackTrace();
				}
				System.err.flush();
			}
			fail("Expected " + expectedErrorCount + " errors, found " + errCount);
		}
	}

}
