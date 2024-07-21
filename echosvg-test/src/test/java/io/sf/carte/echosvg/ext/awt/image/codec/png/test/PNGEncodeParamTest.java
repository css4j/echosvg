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
package io.sf.carte.echosvg.ext.awt.image.codec.png.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.ext.awt.image.codec.png.PNGEncodeParam;

/**
 * PNGEncodeParam tests.
 *
 * @version $Id$
 */
public class PNGEncodeParamTest {

	@Test
	public void testTextError() {
		PNGEncodeParam params = new PNGEncodeParam.RGB();

		assertFalse(params.isTextSet());
		assertFalse(params.isInternationalTextSet());
		assertFalse(params.isCompressedTextSet());

		assertThrows(IllegalStateException.class, () -> params.getText());
		assertThrows(IllegalStateException.class, () -> params.getInternationalText());
		assertThrows(IllegalStateException.class, () -> params.getCompressedText());
	}

}
