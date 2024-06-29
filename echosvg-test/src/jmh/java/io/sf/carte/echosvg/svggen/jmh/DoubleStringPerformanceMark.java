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
package io.sf.carte.echosvg.svggen.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import io.sf.carte.echosvg.anim.dom.SVGDOMImplementation;
import io.sf.carte.echosvg.svggen.SVGGeneratorContext;

/**
 * This test checks that there is no performance degradation in the doubleString
 * utility method.
 *
 * Based on DoubleStringPerformanceTest by Vincent Hardy (vincent.hardy at sun-dot-com).
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Threads(4)
@Fork(value = 2)
@Measurement(iterations = 4, time = 10)
@Warmup(iterations = 4, time = 10)
public class DoubleStringPerformanceMark {

	static final double[] testValues = { 0, 0.00000000001, 0.2e-14, 0.45, 123412341234e14, 987654321e-12, 234143,
			2.3333444000044e56, 45.3456 };

	@Benchmark
	public void markDoubleString() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);
		final SVGGeneratorContext gc = new MarkGeneratorContext(doc);

		int maxLength = 0;
		for (int i = 0; i < 1000; i++) {
			for (double testValue : testValues) {
				maxLength = Math.max((gc.doubleString(testValue)).length(), maxLength);
			}
		}
	}

	static class MarkGeneratorContext extends SVGGeneratorContext {

		MarkGeneratorContext(Document doc) {
			super(doc);
		}

	}

}
