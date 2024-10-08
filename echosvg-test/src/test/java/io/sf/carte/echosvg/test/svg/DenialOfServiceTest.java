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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.sf.carte.echosvg.css.engine.CSSCircularityException;
import io.sf.carte.echosvg.css.engine.CSSResourceLimitException;
import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * Test documents that attempt a Denial of Service attack.
 */
public class DenialOfServiceTest {

	private static String RES_PREFIX = "/io/sf/carte/echosvg/transcoder/security/";

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	private void testSecurity(String file, int expectedErrorCount) throws TranscoderException, IOException {
		TranscoderSecurityCheck runner = new TranscoderSecurityCheck(expectedErrorCount);
		runner.runTest(RES_PREFIX + file);
	}

	/*
	 * Tests
	 */

	/**
	 * Check the behaviour on attr() circularity.
	 * 
	 * <p>
	 * If the test runs for more than a few seconds, the test failed.
	 * </p>
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testAttrCircularity() throws TranscoderException, IOException {
		assertThrows(CSSCircularityException.class, () -> testSecurity("attrCircularity.svg", 4));
	}

	/**
	 * Test a Billion Laughs DoS attack against the var() implementation.
	 * 
	 * <p>
	 * If the test runs for more than 3 seconds, either the computer is really slow
	 * or the test failed.
	 * </p>
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	@Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
	public void testVarBLA() throws TranscoderException, IOException {
		assertThrows(CSSResourceLimitException.class, () -> testSecurity("varBillionLaughsAttack.svg", 0));
	}

	/**
	 * Test a Billion Laughs DoS attack against the var() implementation, fallback
	 * variant.
	 * 
	 * <p>
	 * If the test runs for more than 3 seconds, either the computer is really slow
	 * or the test failed.
	 * </p>
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	@Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
	public void testVarBLAFallback() throws TranscoderException, IOException {
		assertThrows(CSSResourceLimitException.class, () -> testSecurity("varBLAFallback.svg", 0));
	}

	/**
	 * Check the behaviour on var() circularity.
	 * 
	 * <p>
	 * If the test runs for more than a few seconds, the test failed.
	 * </p>
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testVarCircularity() throws TranscoderException, IOException {
		assertThrows(CSSCircularityException.class, () -> testSecurity("varCircularity.svg", 0));
	}

	/**
	 * Check the behaviour on var() fallback circularity.
	 * 
	 * <p>
	 * If the test runs for more than a few seconds, the test failed.
	 * </p>
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	@Timeout(value = 2500, unit = TimeUnit.MILLISECONDS)
	public void testVarFallbackCircularity() throws TranscoderException, IOException {
		assertThrows(CSSCircularityException.class, () -> testSecurity("varFallbackCircularity.svg", 0));
	}

}
