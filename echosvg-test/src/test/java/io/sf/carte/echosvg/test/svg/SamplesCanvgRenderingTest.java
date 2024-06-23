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

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.TestFonts;
import io.sf.carte.echosvg.transcoder.TranscoderException;

/**
 * This test renders a number of SVG files under the {@code samples/spec12}
 * directory, and compares the result with a reference image. See issue #92.
 * <p>
 * Beware that when a test references an issue number, in principle it will be
 * an issue in the Canvg project, not in EchoSVG.
 * </p>
 * <p>
 * Read the {@code IMAGE_COMPARISONS.md} file for details about the handling of
 * candidate and reference images.
 * </p>
 */
public class SamplesCanvgRenderingTest extends AbstractSamplesRendering {

	private static final String BROWSER_MEDIA = "screen";

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		TestFonts.loadTestFonts();
	}

	/* @formatter:off
	 * 
	 * Images obtained from https://github.com/canvg/canvg/tree/master/test/svgs
	 * 
	 * Test methods generated with:
	 * 
	 * for f in *;
	 *   do echo "@Test";
	 *      echo "public void test${f^}() throws TranscoderException, IOException {"|tr '-' '_'|sed 's/\.svg//';
	 *      echo "    test(\"samples/canvg/${f}\");";
	 *      echo "}";
	 *      echo;
	 * done
	 * 
	 * @formatter:on
	 */

	@Test
	public void test1() throws TranscoderException, IOException {
		float[] times = { 0f, 1f, 2f };
		testAnim("samples/canvg/1.svg", times, false);
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void test2() throws TranscoderException, IOException {
		testNV("samples/canvg/2.svg");
	}

	@Test
	public void test3() throws TranscoderException, IOException {
		test("samples/canvg/3.svg");
	}

	@Test
	public void test4() throws TranscoderException, IOException {
		test("samples/canvg/4.svg");
	}

	@Test
	public void test5() throws TranscoderException, IOException {
		test("samples/canvg/5.svg");
	}

	@Test
	public void test6() throws TranscoderException, IOException {
		test("samples/canvg/6.svg");
	}

	@Test
	public void test7() throws TranscoderException, IOException {
		test("samples/canvg/7.svg");
	}

	@Test
	public void test8() throws TranscoderException, IOException {
		test("samples/canvg/8.svg");
	}

	@Test
	public void test9() throws TranscoderException, IOException {
		test("samples/canvg/9.svg");
	}

	@Test
	public void test10() throws TranscoderException, IOException {
		test("samples/canvg/10.svg");
	}

	@Test
	public void test11() throws TranscoderException, IOException {
		test("samples/canvg/11.svg");
	}

	@Test
	public void test12() throws TranscoderException, IOException {
		test("samples/canvg/12.svg");
	}

	@Test
	public void test13() throws TranscoderException, IOException {
		test("samples/canvg/13.svg");
	}

	@Test
	public void test14() throws TranscoderException, IOException {
		test("samples/canvg/14.svg");
	}

	@Test
	public void test15() throws TranscoderException, IOException {
		test("samples/canvg/15.svg");
	}

	@Test
	public void test16() throws TranscoderException, IOException {
		test("samples/canvg/16.svg");
	}

	@Test
	public void test17() throws TranscoderException, IOException {
		test("samples/canvg/17.svg");
	}

	@Test
	public void test18() throws TranscoderException, IOException {
		test("samples/canvg/18.svg");
	}

	@Test
	public void test19() throws TranscoderException, IOException {
		test("samples/canvg/19.svg");
	}

	@Test
	public void test20() throws TranscoderException, IOException {
		float[] times = { 0f, 2f, 4f };
		testAnim("samples/canvg/20.svg", times, false);
	}

	@Test
	public void test21() throws TranscoderException, IOException {
		float[] times = { 0f, 2f, 4f };
		testAnim("samples/canvg/21.svg", times, false);
	}

	@Test
	public void test22() throws TranscoderException, IOException {
		testNV("samples/canvg/22.svg");
	}

	@Test
	public void test23() throws TranscoderException, IOException {
		test("samples/canvg/23.svg");
	}

	@Test
	public void test24() throws TranscoderException, IOException {
		test("samples/canvg/24.svg");
	}

	@Test
	public void test25() throws TranscoderException, IOException {
		test("samples/canvg/25.svg");
	}

	@Test
	public void test26() throws TranscoderException, IOException {
		testNV("samples/canvg/26.svg");
	}

	@Test
	public void test27() throws TranscoderException, IOException {
		test("samples/canvg/27.svg");
	}

	@Test
	public void test28() throws TranscoderException, IOException {
		test("samples/canvg/28.svg");
	}

	@Test
	public void test29() throws TranscoderException, IOException {
		test("samples/canvg/29.svg");
	}

	@Test
	public void test30() throws TranscoderException, IOException {
		test("samples/canvg/30.svg");
	}

	@Test
	public void test31() throws TranscoderException, IOException {
		test("samples/canvg/31.svg");
	}

	@Test
	public void test32() throws TranscoderException, IOException {
		testNV("samples/canvg/32.svg");
	}

	@Test
	public void test33() throws TranscoderException, IOException {
		test("samples/canvg/33.svg");
	}

	@Test
	public void test34() throws TranscoderException, IOException {
		testNV("samples/canvg/34.svg");
	}

	@Test
	public void test35() throws TranscoderException, IOException {
		testNV("samples/canvg/35.svg");
	}

	@Test
	public void test36() throws TranscoderException, IOException {
		test("samples/canvg/36.svg");
	}

	@Test
	public void test37() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/37.svg", BROWSER_MEDIA, 1);
	}

	@Test
	public void testIssue3() throws TranscoderException, IOException {
		testNV("samples/canvg/issue3.svg");
	}

	@Test
	public void testIssue8() throws TranscoderException, IOException {
		test("samples/canvg/issue8.svg");
	}

	@Test
	public void testIssue24() throws TranscoderException, IOException {
		testNV("samples/canvg/issue24.svg");
	}

	@Test
	public void testIssue25() throws TranscoderException, IOException {
		testNV("samples/canvg/issue25.svg");
	}

	@Test
	public void testIssue30() throws TranscoderException, IOException {
		testNV("samples/canvg/issue30.svg");
	}

	@Test
	public void testIssue31() throws TranscoderException, IOException {
		testNV("samples/canvg/issue31.svg");
	}

	@Test
	public void testIssue32() throws TranscoderException, IOException {
		testNV("samples/canvg/issue32.svg");
	}

	@Test
	public void testIssue33() throws TranscoderException, IOException {
		testNV("samples/canvg/issue33.svg");
	}

	@Test
	public void testIssue34() throws TranscoderException, IOException {
		testNV("samples/canvg/issue34.svg");
	}

	@Test
	public void testIssue35() throws TranscoderException, IOException {
		testNV("samples/canvg/issue35.svg");
	}

	@Test
	public void testIssue36() throws TranscoderException, IOException {
		testNV("samples/canvg/issue36.svg");
	}

	@Test
	public void testIssue38() throws TranscoderException, IOException {
		test("samples/canvg/issue38.svg");
	}

	@Test
	public void testIssue39() throws TranscoderException, IOException {
		test("samples/canvg/issue39.svg");
	}

	@Test
	public void testIssue40() throws TranscoderException, IOException {
		test("samples/canvg/issue40.svg");
	}

	@Test
	public void testIssue41() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue41.svg", BROWSER_MEDIA, 2);
	}

	@Test
	public void testIssue42() throws TranscoderException, IOException {
		testNV("samples/canvg/issue42.svg");
	}

	@Test
	public void testIssue44() throws TranscoderException, IOException {
		testNV("samples/canvg/issue44.svg");
	}

	@Test
	public void testIssue45() throws TranscoderException, IOException {
		testNV("samples/canvg/issue45.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue46() throws TranscoderException, IOException {
		testNV("samples/canvg/issue46.svg");
	}

	@Test
	public void testIssue48() throws TranscoderException, IOException {
		testNV("samples/canvg/issue48.svg");
	}

	@Test
	public void testIssue50() throws TranscoderException, IOException {
		testNV("samples/canvg/issue50.svg");
	}

	@Test
	public void testIssue52() throws TranscoderException, IOException {
		testNV("samples/canvg/issue52.svg");
	}

	@Test
	public void testIssue54() throws TranscoderException, IOException {
		testNV("samples/canvg/issue54.svg");
	}

	@Test
	public void testIssue55() throws TranscoderException, IOException {
		testNV("samples/canvg/issue55.svg");
	}

	@Test
	public void testIssue57() throws TranscoderException, IOException {
		testNV("samples/canvg/issue57.svg");
	}

	@Test
	public void testIssue57b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue57b.svg");
	}

	@Test
	public void testIssue66() throws TranscoderException, IOException {
		test("samples/canvg/issue66.svg");
	}

	@Test
	public void testIssue67() throws TranscoderException, IOException {
		testNV("samples/canvg/issue67.svg");
	}

	@Test
	public void testIssue70() throws TranscoderException, IOException {
		testNV("samples/canvg/issue70.svg");
	}

	@Test
	public void testIssue71() throws TranscoderException, IOException {
		testNV("samples/canvg/issue71.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue73() throws TranscoderException, IOException {
		float[] times = { 2.7f };
		testAnim("samples/canvg/issue73.svg", times, false);
	}

	@Test
	public void testIssue75() throws TranscoderException, IOException {
		testNV("samples/canvg/issue75.svg");
	}

	@Test
	public void testIssue76() throws TranscoderException, IOException {
		test("samples/canvg/issue76.svg");
	}

	@Test
	public void testIssue77() throws TranscoderException, IOException {
		testNV("samples/canvg/issue77.svg");
	}

	@Test
	public void testIssue79() throws TranscoderException, IOException {
		testNV("samples/canvg/issue79.svg");
	}

	/*
	 * TODO: investigate.
	 */
	@Disabled
	@Test
	public void testIssue82() throws TranscoderException, IOException {
		test("samples/canvg/issue82.svg");
	}

	@Test
	public void testIssue85() throws TranscoderException, IOException {
		testNV("samples/canvg/issue85.svg");
	}

	@Test
	public void testIssue88() throws TranscoderException, IOException {
		testNV("samples/canvg/issue88.svg");
	}

	@Test
	public void testIssue89() throws TranscoderException, IOException {
		testNV("samples/canvg/issue89.svg");
	}

	@Test
	public void testIssue91() throws TranscoderException, IOException {
		test("samples/canvg/issue91.svg");
	}

	/*
	 * TODO: investigate.
	 */
	@Disabled
	@Test
	public void testIssue94() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue94.svg", BROWSER_MEDIA, 1);
	}

	@Test
	public void testIssue97() throws TranscoderException, IOException {
		testNV("samples/canvg/issue97.svg");
	}

	@Test
	public void testIssue98() throws TranscoderException, IOException {
		testNV("samples/canvg/issue98.svg");
	}

	@Test
	public void testIssue99() throws TranscoderException, IOException {
		testNV("samples/canvg/issue99.svg");
	}

	@Test
	public void testIssue104() throws TranscoderException, IOException {
		testNV("samples/canvg/issue104.svg");
	}

	@Test
	public void testIssue106() throws TranscoderException, IOException {
		testNV("samples/canvg/issue106.svg");
	}

	@Test
	public void testIssue108() throws TranscoderException, IOException {
		testNV("samples/canvg/issue108.svg");
	}

	@Test
	public void testIssue112() throws TranscoderException, IOException {
		test("samples/canvg/issue112.svg");
	}

	@Test
	public void testIssue114() throws TranscoderException, IOException {
		testNV("samples/canvg/issue114.svg");
	}

	@Test
	public void testIssue115() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue115.svg", BROWSER_MEDIA, 1);
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue116() throws TranscoderException, IOException {
		testNV("samples/canvg/issue116.svg");
	}

	@Test
	public void testIssue117() throws TranscoderException, IOException {
		testNV("samples/canvg/issue117.svg");
	}

	@Test
	public void testIssue121() throws TranscoderException, IOException {
		float[] times = { 0f, 1f };
		testAnim("samples/canvg/issue121.svg", times, false);
	}

	@Test
	public void testIssue122() throws TranscoderException, IOException {
		testNV("samples/canvg/issue122.svg");
	}

	@Test
	public void testIssue125a() throws TranscoderException, IOException {
		testNV("samples/canvg/issue125a.svg");
	}

	@Test
	public void testIssue125b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue125b.svg");
	}

	@Test
	public void testIssue128() throws TranscoderException, IOException {
		testNV("samples/canvg/issue128.svg");
	}

	@Test
	public void testIssue132() throws TranscoderException, IOException {
		testNV("samples/canvg/issue132.svg");
	}

	/**
	 * Disabled due to security restrictions.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Disabled
	@Test
	public void testIssue134() throws TranscoderException, IOException {
		testNV("samples/canvg/issue134.svg");
	}

	@Test
	public void testIssue135() throws TranscoderException, IOException {
		testNV("samples/canvg/issue135.svg");
	}

	@Test
	public void testIssue137() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue137.svg", BROWSER_MEDIA, 1);
	}

	@Test
	public void testIssue138() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue138.svg", BROWSER_MEDIA, 1);
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue142() throws TranscoderException, IOException {
		testNV("samples/canvg/issue142.svg");
	}

	@Test
	public void testIssue144() throws TranscoderException, IOException {
		testNV("samples/canvg/issue144.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue145() throws TranscoderException, IOException {
		testNV("samples/canvg/issue145.svg");
	}

	@Test
	public void testIssue158() throws TranscoderException, IOException {
		testNV("samples/canvg/issue158.svg");
	}

	/**
	 * Disabled due to security restrictions.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Disabled
	@Test
	public void testIssue161() throws TranscoderException, IOException {
		testNV("samples/canvg/issue161.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue166() throws TranscoderException, IOException {
		testNV("samples/canvg/issue166.svg");
	}

	@Test
	public void testIssue172() throws TranscoderException, IOException {
		test("samples/canvg/issue172.svg");
	}

	@Test
	public void testIssue175() throws TranscoderException, IOException {
		testNV("samples/canvg/issue175.svg");
	}

	@Test
	public void testIssue176() throws TranscoderException, IOException {
		testNV("samples/canvg/issue176.svg");
	}

	@Test
	public void testIssue178() throws TranscoderException, IOException {
		testNV("samples/canvg/issue178.svg");
	}

	@Test
	public void testIssue179() throws TranscoderException, IOException {
		float[] times = { 0f, 0.5f };
		testAnim("samples/canvg/issue179.svg", times, false);
	}

	@Test
	public void testIssue180() throws TranscoderException, IOException {
		testNV("samples/canvg/issue180.svg");
	}

	@Test
	public void testIssue181() throws TranscoderException, IOException {
		testNV("samples/canvg/issue181.svg");
	}

	@Test
	public void testIssue182() throws TranscoderException, IOException {
		testNV("samples/canvg/issue182.svg");
	}

	@Test
	public void testIssue183() throws TranscoderException, IOException {
		testNV("samples/canvg/issue183.svg");
	}

	@Test
	public void testIssue184() throws TranscoderException, IOException {
		test("samples/canvg/issue184.svg");
	}

	@Test
	public void testIssue187() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue187.svg", BROWSER_MEDIA, 1);
	}

	@Test
	public void testIssue195() throws TranscoderException, IOException {
		testNV("samples/canvg/issue195.svg");
	}

	@Test
	public void testIssue196() throws TranscoderException, IOException {
		testNV("samples/canvg/issue196.svg");
	}

	@Test
	public void testIssue197() throws TranscoderException, IOException {
		testNV("samples/canvg/issue197.svg");
	}

	@Test
	public void testIssue202() throws TranscoderException, IOException {
		testNV("samples/canvg/issue202.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue202b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue202b.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue206() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue206.svg", BROWSER_MEDIA, 66);
	}

	/*
	 * TODO: investigate.
	 */
	@Disabled
	@Test
	public void testIssue211() throws TranscoderException, IOException {
		testNV("samples/canvg/issue211.svg");
	}

	@Test
	public void testIssue212() throws TranscoderException, IOException {
		test("samples/canvg/issue212.svg");
	}

	@Test
	public void testIssue217() throws TranscoderException, IOException {
		testNV("samples/canvg/issue217.svg");
	}

	@Test
	public void testIssue227() throws TranscoderException, IOException {
		testNV("samples/canvg/issue227.svg");
	}

	/**
	 * File was edited to remove reference to gnuplot_svg.js.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue229() throws TranscoderException, IOException {
		test("samples/canvg/issue229.svg");
	}

	@Test
	public void testIssue231() throws TranscoderException, IOException {
		testNV("samples/canvg/issue231.svg");
	}

	@Test
	public void testIssue234() throws TranscoderException, IOException {
		test("samples/canvg/issue234.svg");
	}

	@Test
	public void testIssue234b() throws TranscoderException, IOException {
		test("samples/canvg/issue234b.svg");
	}

	@Test
	public void testIssue234c() throws TranscoderException, IOException {
		test("samples/canvg/issue234c.svg");
	}

	@Test
	public void testIssue234d() throws TranscoderException, IOException {
		testNV("samples/canvg/issue234d.svg");
	}

	@Test
	public void testIssue234e() throws TranscoderException, IOException {
		testNV("samples/canvg/issue234e.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue238() throws TranscoderException, IOException {
		testNV("samples/canvg/issue238.svg");
	}

	@Test
	public void testIssue241() throws TranscoderException, IOException {
		test("samples/canvg/issue241.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue244() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue244.svg", BROWSER_MEDIA, 1);
	}

	@Test
	public void testIssue255() throws TranscoderException, IOException {
		testNV("samples/canvg/issue255.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue268() throws TranscoderException, IOException {
		testNV("samples/canvg/issue268.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue269() throws TranscoderException, IOException {
		testNV("samples/canvg/issue269.svg");
	}

	@Test
	public void testIssue273() throws TranscoderException, IOException {
		float[] times = { 0f, 1f, 2f };
		testAnim("samples/canvg/issue273.svg", times, false);
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue277() throws TranscoderException, IOException {
		testNV("samples/canvg/issue277.svg");
	}

	@Test
	public void testIssue282() throws TranscoderException, IOException {
		testNV("samples/canvg/issue282.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue289() throws TranscoderException, IOException {
		testNV("samples/canvg/issue289.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue320() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue320.svg", BROWSER_MEDIA, 1);
	}

	@Test
	public void testIssue322() throws TranscoderException, IOException {
		testNV("samples/canvg/issue322.svg");
	}

	@Test
	public void testIssue342() throws TranscoderException, IOException {
		testNV("samples/canvg/issue342.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue352() throws TranscoderException, IOException {
		testNV("samples/canvg/issue352.svg");
	}

	@Test
	public void testIssue358() throws TranscoderException, IOException {
		testNV("samples/canvg/issue358.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue362() throws TranscoderException, IOException {
		testNV("samples/canvg/issue362.svg");
	}

	@Test
	public void testIssue366() throws TranscoderException, IOException {
		testNV("samples/canvg/issue366.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue372() throws TranscoderException, IOException {
		testNV("samples/canvg/issue372.svg");
	}

	@Test
	public void testIssue376() throws TranscoderException, IOException {
		testNV("samples/canvg/issue376.svg");
	}

	@Test
	public void testIssue406() throws TranscoderException, IOException {
		testNV("samples/canvg/issue406.svg");
	}

	@Test
	public void testIssue454() throws TranscoderException, IOException {
		testNV("samples/canvg/issue454.svg");
	}

	@Test
	public void testIssue454b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue454b.svg");
	}

	@Test
	public void testIssue473() throws TranscoderException, IOException {
		testNV("samples/canvg/issue473.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue510() throws TranscoderException, IOException {
		testNV("samples/canvg/issue510.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue556() throws TranscoderException, IOException {
		testNV("samples/canvg/issue556.svg");
	}

	@Test
	public void testIssue564() throws TranscoderException, IOException {
		testNV("samples/canvg/issue564.svg");
	}

	@Test
	public void testIssue593() throws TranscoderException, IOException {
		testNV("samples/canvg/issue593.svg");
	}

	@Test
	public void testIssue610() throws TranscoderException, IOException {
		testNV("samples/canvg/issue610.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue615() throws TranscoderException, IOException {
		testNV("samples/canvg/issue615.svg");
	}

	@Test
	public void testIssue620() throws TranscoderException, IOException {
		testNV("samples/canvg/issue620.svg");
	}

	@Test
	public void testIssue690() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue690.svg", BROWSER_MEDIA, 4);
	}

	/*
	 * TODO: investigate.
	 */
	@Disabled
	@Test
	public void testIssue694() throws TranscoderException, IOException {
		testNV("samples/canvg/issue694.svg");
	}

	/*
	 * TODO: investigate.
	 */
	@Disabled
	@Test
	public void testIssue713() throws TranscoderException, IOException {
		testNV("samples/canvg/issue713.svg");
	}

	@Test
	public void testIssue717() throws TranscoderException, IOException {
		testNV("samples/canvg/issue717.svg");
	}

	@Test
	public void testIssue735() throws TranscoderException, IOException {
		testNV("samples/canvg/issue735.svg");
	}

	@Test
	public void testIssue747() throws TranscoderException, IOException {
		testNV("samples/canvg/issue747.svg");
	}

	@Test
	public void testIssue748() throws TranscoderException, IOException {
		testNV("samples/canvg/issue748.svg");
	}

	@Test
	public void testIssue750() throws TranscoderException, IOException {
		testNV("samples/canvg/issue750.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue757() throws TranscoderException, IOException {
		testNV("samples/canvg/issue757.svg");
	}

	@Test
	public void testIssue782a() throws TranscoderException, IOException {
		testNV("samples/canvg/issue782a.svg");
	}

	@Test
	public void testIssue782b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue782b.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue816() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue816.svg", BROWSER_MEDIA, 14);
	}

	@Test
	public void testIssue832() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue832.svg", BROWSER_MEDIA, 2);
	}

	/*
	 * TODO: investigate possible enhancement.
	 */
	@Disabled
	@Test
	public void testIssue869() throws TranscoderException, IOException {
		testNV("samples/canvg/issue869.svg");
	}

	/*
	 * TODO: investigate possible enhancement.
	 */
	@Disabled
	@Test
	public void testIssue870() throws TranscoderException, IOException {
		testNV("samples/canvg/issue870.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue897a() throws TranscoderException, IOException {
		testNV("samples/canvg/issue897a.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue897b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue897b.svg");
	}

	/**
	 * Disabled due to security restrictions.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Disabled
	@Test
	public void testIssue908() throws TranscoderException, IOException {
		testNV("samples/canvg/issue908.svg");
	}

	@Test
	public void testIssue941() throws TranscoderException, IOException {
		testNV("samples/canvg/issue941.svg");
	}

	@Test
	public void testIssue945() throws TranscoderException, IOException {
		testNVErrIgnore("samples/canvg/issue945.svg", BROWSER_MEDIA, 11);
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue946() throws TranscoderException, IOException {
		testNV("samples/canvg/issue946.svg");
	}

	/*
	 * FIXME: investigate possible bug.
	 */
	@Disabled
	@Test
	public void testIssue977() throws TranscoderException, IOException {
		testNV("samples/canvg/issue977.svg");
	}

	/*
	 * TODO: investigate possible enhancement.
	 */
	@Disabled
	@Test
	public void testIssue1001() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1001.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue1045() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1045.svg");
	}

	@Disabled
	@Test
	public void testIssue1063() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1063.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue1111() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1111.svg");
	}

	@Test
	public void testIssue1161() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1161.svg");
	}

	@Test
	public void testIssue1196() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1196.svg");
	}

	/**
	 * Document was non-conformant (no SVG namespace) and was edited.
	 * 
	 * @throws TranscoderException
	 * @throws IOException
	 */
	@Test
	public void testIssue1364() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1364.svg");
	}

	/*
	 * TODO: investigate possible enhancement.
	 */
	@Disabled
	@Test
	public void testIssue1439() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1439.svg");
	}

	@Test
	public void testIssue1460() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1460.svg");
	}

	@Test
	public void testIssue1516() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1516.svg");
	}

	@Test
	public void testIssue1548a() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1548a.svg");
	}

	@Test
	public void testIssue1548b() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1548b.svg");
	}

	@Test
	public void testIssue1548c() throws TranscoderException, IOException {
		testNV("samples/canvg/issue1548c.svg");
	}

	@Test
	public void testFavicon() throws TranscoderException, IOException {
		testNV("samples/canvg/favicon.svg");
	}

	@Disabled
	@Test
	public void testLydianv_webfont() throws TranscoderException, IOException {
		testNV("samples/canvg/lydianv-webfont.svg");
	}

	@Disabled
	@Test
	public void testSwz721b_webfont() throws TranscoderException, IOException {
		testNV("samples/canvg/swz721b-webfont.svg");
	}

	/*
	 * Put larger thresholds due to smaller file sizes.
	 */
	@Override
	float getBelowThresholdAllowed() {
		return 0.5f;
	}

	@Override
	float getOverThresholdAllowed() {
		return 0.1f;
	}

}
