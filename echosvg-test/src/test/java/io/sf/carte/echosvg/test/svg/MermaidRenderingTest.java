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

import java.awt.Color;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.ScriptUtil;
import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of Mermaid-generated SVG files under the {@code samples}
 * directory, and compares the result with a reference image.
 * <p>
 * It bypasses EchoSVG's style computations and uses CSS4J instead.
 * </p>
 */
public class MermaidRenderingTest extends AbstractBypassRenderingCheck {

	/**
	 * A standard Mermaid test.
	 * 
	 * @param file the SVG file to test.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testMermaid(String file) throws TranscoderException, IOException {
		testMermaid(file, 0);
	}

	/**
	 * A standard Mermaid test, with an expected error count.
	 * 
	 * @param file               the SVG file to test.
	 * @param expectedErrorCount the expected error count.
	 * @throws TranscoderException
	 * @throws IOException
	 */
	void testMermaid(String file, int expectedErrorCount)
			throws TranscoderException, IOException {
		test(file, SVGRenderingAccuracyTest.DEFAULT_MEDIUM, false, Color.white, null, false,
				expectedErrorCount);
	}

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
		ScriptUtil.defaultRhinoShutter();
	}

	@Test
	public void testMermaid() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid.svg");
	}

	@Test
	public void testWebsiteDiagram93() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-93.svg");
	}

	@Disabled
	@Test
	public void testBlock() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-block.svg");
	}

	@Test
	public void testC4Context() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-c4-context.svg");
	}

	@Test
	public void testC4Component() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-c4-component.svg");
	}

	@Test
	public void testC4Container() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-c4-container.svg");
	}

	@Test
	public void testC4Deployment() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-c4-deployment.svg");
	}

	@Test
	public void testC4Dynamic() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-c4-dynamic.svg");
	}

	@Test
	public void testClass() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-class.svg", 1);
	}

	@Test
	public void testEntityRelationship() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-erd.svg");
	}

	@Test
	public void testEntityRelationship2() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-erd2.svg");
	}

	@Test
	public void testFlowChart() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-flowchart.svg");
	}

	@Test
	public void testFlowChartCyrillic() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-flowchart-cyrillic.svg");
	}

	@Test
	public void testGantt() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-gantt.svg", 6);
	}

	@Test
	public void testGitGraph() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-git-graph.svg", 1);
	}

	@Test
	public void testJourney() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-journey.svg", 9);
	}

	@Test
	public void testMindmap() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-mindmap.svg", 1);
	}

	@Test
	public void testPie() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-pie.svg");
	}

	@Test
	public void testQuadrant() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-quadrant.svg", 6);
	}

	@Test
	public void testRequirement() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-requirement.svg", 1);
	}

	@Test
	public void testSankey() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-sankey.svg");
	}

	@Test
	public void testSequence() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-sequence.svg", 9);
	}

	@Test
	public void testSequenceBackground() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-sequence-background.svg", 5);
	}

	@Test
	public void testSequenceCritical() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-sequence-critical.svg", 5);
	}

	@Test
	public void testSequenceGrouping() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-sequence-grouping.svg", 11);
	}

	@Test
	public void testSequenceParallel() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-sequence-parallel.svg", 7);
	}

	@Test
	public void testState() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-state.svg");
	}

	@Test
	public void testTimeline() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-timeline.svg", 2);
	}

	@Test
	public void testXY() throws TranscoderException, IOException {
		testMermaid("samples/tests/spec2/foreign/mermaid-xy.svg");
	}

}
