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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.misc.TestLocations;

/**
 * Base class for tests which take an additional parameter in addition to the
 * SVG file.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGReferenceRenderingAccuracyTest extends ParameterizedRenderingAccuracyTest {

	private String alias;

	private String fragment;

	protected String getAlias() {
		return alias;
	}

	protected String getFragment() {
		return fragment;
	}

	@Test
	public void test1() throws URISyntaxException {
		runTest("samples/anne.svg", "svgView(viewBox(0,0,100,200))", "ViewBox1");
	}

	@Test
	public void test2() throws URISyntaxException {
		runTest("samples/anne.svg", "svgView(viewBox(100,50,100,200))", "ViewBox2");
	}

	@Test
	public void test3() throws URISyntaxException {
		runTest("samples/anne.svg", "svgView(transform(translate(-100,-50)))", "Transform1");
	}

	@Test
	public void test4() throws URISyntaxException {
		runTest("samples/anne.svg", "svgView(transform(translate(225,250)rotate(45)translate(-225,-250)))",
				"Transform2");
	}

	@Test
	public void test5() throws URISyntaxException {
		runTest("samples/anne.svg", "svgView(transform(rotate(45,225,250)))", "Transform3");
	}

	@Test
	public void testCandyScale() throws URISyntaxException {
		runTest("samples/batikCandy.svg", "svgView(transform(scale(0.01)))", "BumpMap1");
	}

	/**
	 * For this type of test, the id should be made as follows:<br>
	 * &lt;fileName&gt;#reference-alias <br>
	 * For example: <br>
	 * samples/anne.svg#svgView(viewBox(0,0,100,100))-viewBox1
	 * 
	 * @throws URISyntaxException
	 */
	public void runTest(String uri, String fragment, String alias) throws URISyntaxException {

		this.alias = alias;

		this.fragment = fragment;

		String svgFile = uri;

		String[] dirNfile = breakSVGFile(svgFile);

		setInputAndRefURL(buildSVGURL(dirNfile[0], dirNfile[1]),
				resolveURI(buildRefImgPath(dirNfile[0], dirNfile[1])));
	}

	@Override
	public void setFile(String id) throws IllegalArgumentException {
		throw new IllegalStateException("Must use runTest()");
	}

	/**
	 * Gives a chance to the subclass to prepend a prefix to the svgFile name. The
	 * svgURL is built as: getSVGURLPrefix() + svgDir + svgFile + SVG_EXTENSION +
	 * "#" + parameter
	 */
	protected URI buildSVGURL(String svgDir, String svgFile) {
		String path = TestLocations.PROJECT_ROOT_URI.getPath() + getSVGURLPrefix() + svgDir
				+ svgFile + SVG_EXTENSION;
		URI uri;
		try {
			uri = new URI("file", null, path, getFragment());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Could not obtain URI from " + svgFile);
		}
		return uri;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the reference
	 * PNG file from the svgFile name The refImgPath is built as: getRefImagePrefix()
	 * + svgDir + getRefImageSuffix() + svgFile
	 */
	@Override
	protected String buildRefImgPath(String svgDir, String svgFile) {
		return getRefImagePrefix() + svgDir + getRefImageSuffix()
				+ URLEncoder.encode(svgFile, StandardCharsets.UTF_8) + alias + PNG_EXTENSION;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the variation
	 * path, which is built as: getVariationPrefix() + svgDir + getVariationSuffix()
	 * + svgFile + parameter + PNG_EXTENSION
	 */
	@Override
	public String[] buildVariationPaths(String svgDir, String svgFile) {
		String[] platforms = getVariationPlatforms();
		String[] paths = new String[platforms.length + 1];
		paths[0] = getVariationPrefix() + svgDir + getVariationSuffix()
				+ URLEncoder.encode(svgFile, StandardCharsets.UTF_8) + alias + PNG_EXTENSION;
		for (int i = 0; i < platforms.length; i++) {
			paths[i + 1] = getVariationPrefix() + svgDir + getVariationSuffix()
					+ URLEncoder.encode(svgFile, StandardCharsets.UTF_8) + alias + '_'
					+ platforms[i] + PNG_EXTENSION;
		}
		return paths;
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
