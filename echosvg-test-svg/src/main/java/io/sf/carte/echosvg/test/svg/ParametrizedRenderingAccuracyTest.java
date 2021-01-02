/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.test.svg;

import java.io.File;

/**
 * Base class for tests which take an additional parameter in addition to the
 * SVG file.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ParametrizedRenderingAccuracyTest extends SamplesRenderingTest {
	public static final char PARAMETER_SEPARATOR = '-';

	/**
	 * Parameter which was passed appended to the SVG file
	 */
	protected String parameter;

	/**
	 * Constructor.
	 */
	public ParametrizedRenderingAccuracyTest() {
		super();
	}

	public char getParameterSeparator() {
		return PARAMETER_SEPARATOR;
	}

	@Override
	public void setId(String id) {
		this.id = id;

		String svgFile = id;

		int n = svgFile.lastIndexOf(getParameterSeparator());
		if (n == -1 || n + 1 >= svgFile.length()) {
			throw new IllegalArgumentException(id);
		}

		parameter = svgFile.substring(n + 1, svgFile.length());
		svgFile = svgFile.substring(0, n);

		String[] dirNfile = breakSVGFile(svgFile);

		setConfig(buildSVGURL(dirNfile[0], dirNfile[1], dirNfile[2]), buildRefImgURL(dirNfile[0], dirNfile[1]));

		String[] variationURLs = buildVariationURLs(dirNfile[0], dirNfile[1]);
		for (String variationURL : variationURLs) {
			addVariationURL(variationURL);
		}
		setSaveVariation(new File(buildSaveVariationFile(dirNfile[0], dirNfile[1])));
		setCandidateReference(new File(buildCandidateReferenceFile(dirNfile[0], dirNfile[1])));
	}

	/**
	 * Gives a chance to the subclass to control the construction of the reference
	 * PNG file from the svgFile name The refImgURL is built as: getRefImagePrefix()
	 * + svgDir + getRefImageSuffix() + svgFile
	 */
	@Override
	protected String buildRefImgURL(String svgDir, String svgFile) {
		return getRefImagePrefix() + svgDir + getRefImageSuffix() + svgFile + parameter + PNG_EXTENSION;
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
		urls[0] = getVariationPrefix() + svgDir + getVariationSuffix() + svgFile + parameter + PNG_EXTENSION;
		for (int i = 0; i < platforms.length; i++) {
			urls[i + 1] = getVariationPrefix() + svgDir + getVariationSuffix() + svgFile + parameter + '_'
					+ platforms[i] + PNG_EXTENSION;
		}
		return urls;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the
	 * saveVariation URL, which is built as: getSaveVariationPrefix() + svgDir +
	 * getSaveVariationSuffix() + svgFile + parameter + PNG_EXTENSION
	 */
	@Override
	public String buildSaveVariationFile(String svgDir, String svgFile) {
		return getSaveVariationPrefix() + svgDir + getSaveVariationSuffix() + svgFile + parameter + PNG_EXTENSION;
	}

	/**
	 * Gives a chance to the subclass to control the construction of the
	 * candidateReference URL, which is built as: getSaveVariationPrefix() + svgDir
	 * + getSaveVariationSuffix() + svgFile + parameter + PNG_EXTENSION
	 */
	@Override
	public String buildCandidateReferenceFile(String svgDir, String svgFile) {
		return getCandidateReferencePrefix() + svgDir + getCandidateReferenceSuffix() + svgFile + parameter
				+ PNG_EXTENSION;
	}

}
