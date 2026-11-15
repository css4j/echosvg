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

import io.sf.carte.echosvg.test.misc.ScriptUtil;
import io.sf.carte.echosvg.test.misc.TestLocations;

/**
 * Convenience class for creating a SVGRenderingAccuracyTest with predefined
 * rules for the various configuration parameters.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class PreconfiguredRenderingTest extends SVGRenderingAccuracyTest {

	/**
	 * Generic constants
	 */
	public static final String PNG_EXTENSION = ".png";

	public static final String SVG_EXTENSION = ".svg";
	public static final String SVGZ_EXTENSION = ".svgz";
	private static final String HTML_EXTENSION = ".html";
	private static final String XHTML_EXTENSION = ".xhtml";

	public static final char PATH_SEPARATOR = '/';

	public static final String PLATFORM_VARIATION_SUFFIX = "_platform";
	public static final String[] DEFAULT_VARIATION_PLATFORMS = { "xfce", "windows-2025" };

	static {
		ScriptUtil.defaultRhinoShutter();
	}

	/**
	 * For preconfigured tests, the configuration has to be derived from the test
	 * identifier. The identifier should characterize the SVG file to be tested.
	 */
	@Override
	public void setInputAndRefURL(URI svgURL, URI refImgURL) {
		super.setInputAndRefURL(svgURL, refImgURL);

		String relSvgFile = TestLocations.PROJECT_ROOT_URI.relativize(getURI()).getPath();
		String[] dirNfile = breakSVGFile(relSvgFile);

		setVariations(dirNfile);
	}

	public void setFile(String id) throws IllegalArgumentException {
		String svgFile = id;

		String[] dirNfile = breakSVGFile(svgFile);

		super.setInputAndRefURL(resolveURI(buildSVGURL(dirNfile[0], dirNfile[1], dirNfile[2])),
				resolveURI(buildRefImgPath(dirNfile[0], dirNfile[1])));

		setVariations(dirNfile);
	}

	protected void setVariations(String[] dirNfile) {
		String[] variationPaths = buildVariationPaths(dirNfile[0], dirNfile[1]);
		for (String variationPath : variationPaths) {
			addVariationPath(variationPath);
		}
		String vpath = buildSaveVariationPath(dirNfile[0], dirNfile[1]);
		String rangeVariation = vpath + PNG_EXTENSION;
		String platformVariation = vpath + PLATFORM_VARIATION_SUFFIX + PNG_EXTENSION;
		setSaveRangeVariation(resolveURI(rangeVariation).getPath());
		setSavePlatformVariation(resolveURI(platformVariation).getPath());

		setCandidateReference(
				resolvePath(buildCandidateReferenceFile(dirNfile[0], dirNfile[1])));
	}

	/**
	 * Make the name as simple as possible. For preconfigured SVG files, we use the
	 * test id, which is the relevant identifier for the test user.
	 */
	public String getName() {
		return getURI().toString();
	}

	/**
	 * Gives a chance to the subclass to prepend a prefix to the svgFile name. The
	 * svgURL is built as: getSVGURLPrefix() + svgDir + svgFile
	 */
	protected String buildSVGURL(String svgDir, String svgFile, String svgExt) {
		return getSVGURLPrefix() + svgDir + svgFile + svgExt;
	}

	protected abstract String getSVGURLPrefix();

	/**
	 * Gives a chance to the subclass to control the construction of the reference
	 * PNG file from the svgFile name The refImgPath is built as: getRefImagePrefix()
	 * + svgDir + getRefImageSuffix() + svgFile
	 */
	protected String buildRefImgPath(String svgDir, String svgFile) {
		String refPrefix = getRefImagePrefix();
		String refSuffix = getRefImageSuffix();
		CharSequence imgSuffix = getImageSuffix();

		StringBuilder buf = new StringBuilder(refPrefix.length() + refSuffix.length() + imgSuffix.length()
				+ svgDir.length() + svgFile.length() + 4);

		buf.append(refPrefix).append(svgDir).append(refSuffix);

		buf.append(svgFile).append(imgSuffix).append(PNG_EXTENSION);

		return buf.toString();
	}

	protected abstract String getRefImagePrefix();

	protected abstract String getRefImageSuffix();

	/**
	 * Gives a chance to the subclass to control the construction of the variation
	 * paths, which are built as: getVariationPrefix() + svgDir +
	 * getVariationSuffix() + svgFile + PNG_EXTENSION
	 */
	public String[] buildVariationPaths(String svgDir, String svgFile) {
		String[] platforms = getVariationPlatforms();
		String[] paths = new String[platforms.length + 1];
		paths[0] = getVariationPrefix() + svgDir + getVariationSuffix() + svgFile + getImageSuffix()
				+ PNG_EXTENSION;
		for (int i = 0; i < platforms.length; i++) {
			paths[i + 1] = getVariationPrefix() + svgDir + getVariationSuffix() + svgFile
					+ getImageSuffix() + '_' + platforms[i] + PNG_EXTENSION;
		}
		return paths;
	}

	protected abstract String getVariationPrefix();

	protected abstract String getVariationSuffix();

	protected abstract String[] getVariationPlatforms();

	/**
	 * Gives a chance to the subclass to control the construction of the
	 * saveVariation path, which is built as: getSaveVariationPrefix() + svgDir +
	 * getSaveVariationSuffix() + svgFile, without PNG_EXTENSION
	 */
	protected String buildSaveVariationPath(String svgDir, String svgFile) {
		return getSaveVariationPrefix() + svgDir + getSaveVariationSuffix() + svgFile
				+ getImageSuffix();
	}

	protected abstract String getSaveVariationPrefix();

	protected abstract String getSaveVariationSuffix();

	/**
	 * Gives a chance to the subclass to control the construction of the
	 * candidateReference path, which is built as: getCandidatereferencePrefix() +
	 * svgDir + getCandidatereferenceSuffix() + svgFile + PNG_EXTENSION
	 */
	public String buildCandidateReferenceFile(String svgDir, String svgFile) {
		return getCandidateReferencePrefix() + svgDir + getCandidateReferenceSuffix() + svgFile
				+ getImageSuffix() + PNG_EXTENSION;
	}

	protected abstract String getCandidateReferencePrefix();

	protected abstract String getCandidateReferenceSuffix();

	protected String[] breakSVGFile(String svgFile) {
		if (svgFile == null) {
			throw new IllegalArgumentException(svgFile);
		}

		String[] ret = new String[3];

		if (svgFile.endsWith(SVG_EXTENSION)) {
			ret[2] = SVG_EXTENSION;
		} else if (svgFile.endsWith(SVGZ_EXTENSION)) {
			ret[2] = SVGZ_EXTENSION;
		} else if (svgFile.endsWith(HTML_EXTENSION)) {
			ret[2] = HTML_EXTENSION;
		} else if (svgFile.endsWith(XHTML_EXTENSION)) {
			ret[2] = XHTML_EXTENSION;
		} else {
			throw new IllegalArgumentException(svgFile);
		}

		svgFile = svgFile.substring(0, svgFile.length() - ret[2].length());

		int fileNameStart = svgFile.lastIndexOf(PATH_SEPARATOR);
		String svgDir = "";
		if (fileNameStart != -1) {
			if (svgFile.length() < fileNameStart + 2) {
				// Nothing after PATH_SEPARATOR
				throw new IllegalArgumentException(svgFile);
			}
			svgDir = svgFile.substring(0, fileNameStart + 1);
			svgFile = svgFile.substring(fileNameStart + 1);
		}
		ret[0] = svgDir;
		ret[1] = svgFile;
		return ret;
	}

}
