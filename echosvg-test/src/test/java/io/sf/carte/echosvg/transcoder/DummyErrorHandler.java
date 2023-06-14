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

package io.sf.carte.echosvg.transcoder;

/**
 * An error handler that ignores errors and warnings (but not fatal errors).
 *
 * @author github.com/carlosame
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DummyErrorHandler implements ErrorHandler {

	private int errorCount = 0;

	private int warningCount = 0;

	@Override
	public void error(TranscoderException ex) throws TranscoderException {
		errorCount++;
	}

	@Override
	public void fatalError(TranscoderException ex) throws TranscoderException {
		ex.printStackTrace();
		errorCount++;
	}

	@Override
	public void warning(TranscoderException ex) throws TranscoderException {
		warningCount++;
	}

	/**
	 * Get the error count.
	 * 
	 * @return the error count.
	 */
	public int getErrorCount() {
		return errorCount;
	}

	/**
	 * Get the warning count.
	 * 
	 * @return the warning count.
	 */
	public int getWarningCount() {
		return warningCount;
	}

}
