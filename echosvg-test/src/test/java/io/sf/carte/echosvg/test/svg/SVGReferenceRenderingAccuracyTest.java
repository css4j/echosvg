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

import java.io.File;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Test;

/**
 * Base class for tests which take an additional parameter in addition to the
 * SVG file.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGReferenceRenderingAccuracyTest extends ParameterizedRenderingAccuracyTest {
	protected String alias;

	@Test
	public void test1() throws MalformedURLException {
		runTest("samples/anne.svg", "svgView(viewBox(0,0,100,200))", "ViewBox1");
	}

	@Test
	public void test2() throws MalformedURLException {
		runTest("samples/anne.svg", "svgView(viewBox(100,50,100,200))", "ViewBox2");
	}

	@Test
	public void test3() throws MalformedURLException {
		runTest("samples/anne.svg", "svgView(transform(translate(-100,-50)))", "Transform1");
	}

	@Test
	public void test4() throws MalformedURLException {
		runTest("samples/anne.svg", "svgView(transform(translate(225,250)rotate(45)translate(-225,-250)))",
				"Transform2");
	}

	@Test
	public void test5() throws MalformedURLException {
		runTest("samples/anne.svg", "svgView(transform(rotate(45,225,250)))", "Transform3");
	}

	@Test
	public void testCandyScale() throws MalformedURLException {
		runTest("samples/batikCandy.svg", "svgView(transform(scale(0.01)))", "BumpMap1");
	}

	/**
	 * For this type of test, the id should be made as follows:<br>
	 * &lt;fileName&gt;#reference-alias <br>
	 * For example: <br>
	 * samples/anne.svg#svgView(viewBox(0,0,100,100))-viewBox1
	 * @throws MalformedURLException 
	 */
	public void runTest(String uri, String parameter, String alias) throws MalformedURLException {

		this.alias = alias;

		String svgFile = uri;

		String[] dirNfile = breakSVGFile(svgFile);

		setConfig(buildSVGURL(dirNfile[0], dirNfile[1]), buildRefImgURL(dirNfile[0], dirNfile[1]));

		String[] variationURLs = buildVariationURLs(dirNfile[0], dirNfile[1]);
		for (String variationURL : variationURLs) {
			addVariationURL(variationURL);
		}
		String vPath = buildSaveVariationPath(dirNfile[0], dirNfile[1]);
		String saveRangeVariation = vPath + PNG_EXTENSION;
		String savePlatformVariation = vPath + PLATFORM_VARIATION_SUFFIX + PNG_EXTENSION;
		setSaveRangeVariation(saveRangeVariation);
		setSavePlatformVariation(savePlatformVariation);
		setCandidateReference(new File(buildCandidateReferenceFile(dirNfile[0], dirNfile[1])));
	}

	/**
	 * Gives a chance to the subclass to prepend a prefix to the svgFile name. The
	 * svgURL is built as: getSVGURLPrefix() + svgDir + svgFile + SVG_EXTENSION +
	 * "#" + parameter
	 */
	protected String buildSVGURL(String svgDir, String svgFile) {
		return getSVGURLPrefix() + svgDir + svgFile + SVG_EXTENSION + "#" + parameter;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the reference
	 * PNG file from the svgFile name The refImgURL is built as: getRefImagePrefix()
	 * + svgDir + getRefImageSuffix() + svgFile
	 */
	@Override
	protected String buildRefImgURL(String svgDir, String svgFile) {
		return getRefImagePrefix() + svgDir + getRefImageSuffix() + svgFile + alias + PNG_EXTENSION;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the variation
	 * URL, which is built as: getVariationPrefix() + svgDir + getVariationSuffix()
	 * + svgFile + parameter + PNG_EXTENSION
	 */
	@Override
	public String[] buildVariationURLs(String svgDir, String svgFile) {
		String[] platforms = getVariationPlatforms();
		String[] urls = new String[platforms.length + 1];
		urls[0] = getVariationPrefix() + svgDir + getVariationSuffix() + svgFile + alias + PNG_EXTENSION;
		for (int i = 0; i < platforms.length; i++) {
			urls[i + 1] = getVariationPrefix() + svgDir + getVariationSuffix() + svgFile + alias + '_' + platforms[i]
					+ PNG_EXTENSION;
		}
		return urls;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the
	 * saveVariation URL, which is built as: getSaveVariationPrefix() + svgDir +
	 * getSaveVariationSuffix() + svgFile + parameter + PNG_EXTENSION
	 */
	@Override
	public String buildSaveVariationPath(String svgDir, String svgFile) {
		return getSaveVariationPrefix() + svgDir + getSaveVariationSuffix() + svgFile + alias + PNG_EXTENSION;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the
	 * candidateReference URL, which is built as: getSaveVariationPrefix() + svgDir
	 * + getSaveVariationSuffix() + svgFile + parameter + PNG_EXTENSION
	 */
	@Override
	public String buildCandidateReferenceFile(String svgDir, String svgFile) {
		return getCandidateReferencePrefix() + svgDir + getCandidateReferenceSuffix() + svgFile + alias + PNG_EXTENSION;
	}

}
