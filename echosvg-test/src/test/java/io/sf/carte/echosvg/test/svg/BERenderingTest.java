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

/**
 * Preconfigured test for SVG files under the beSuite directory.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class BERenderingTest extends PreconfiguredRenderingTest {

	private static final String SVG_URL_PREFIX = "test-references/../../beSuite/";

	private static final String REF_IMAGE_PREFIX = "test-references/svgbe/";

	private static final String REF_IMAGE_SUFFIX = "";

	private static final String VARIATION_PREFIX = "test-references/svgbe/";

	private static final String VARIATION_SUFFIX = "accepted-variation/";

	private static final String SAVE_VARIATION_PREFIX = "test-references/svgbe/";

	private static final String SAVE_VARIATION_SUFFIX = "candidate-variation/";

	private static final String SAVE_CANDIDATE_REFERENCE_PREFIX = "test-references/svgbe/";

	private static final String SAVE_CANDIDATE_REFERENCE_SUFFIX = "candidate-reference/";

	@Override
	protected String getSVGURLPrefix() {
		return SVG_URL_PREFIX;
	}

	@Override
	protected String getRefImagePrefix() {
		return REF_IMAGE_PREFIX;
	}

	@Override
	protected String getRefImageSuffix() {
		return REF_IMAGE_SUFFIX;
	}

	@Override
	protected String getVariationPrefix() {
		return VARIATION_PREFIX;
	}

	@Override
	protected String getVariationSuffix() {
		return VARIATION_SUFFIX;
	}

	@Override
	protected String[] getVariationPlatforms() {
		return DEFAULT_VARIATION_PLATFORMS;
	}

	@Override
	protected String getSaveVariationPrefix() {
		return SAVE_VARIATION_PREFIX;
	}

	@Override
	protected String getSaveVariationSuffix() {
		return SAVE_VARIATION_SUFFIX;
	}

	@Override
	protected String getCandidateReferencePrefix() {
		return SAVE_CANDIDATE_REFERENCE_PREFIX;
	}

	@Override
	protected String getCandidateReferenceSuffix() {
		return SAVE_CANDIDATE_REFERENCE_SUFFIX;
	}

	public BERenderingTest() {
		setValidating(Boolean.TRUE);
	}
}
