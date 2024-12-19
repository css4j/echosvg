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

package io.sf.carte.echosvg.apps.rasterizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.sf.carte.echosvg.test.TestUtil;
import io.sf.carte.echosvg.test.image.ImageCompareUtil;
import io.sf.carte.echosvg.test.image.TempImageFiles;
import io.sf.carte.echosvg.transcoder.Transcoder;
import io.sf.carte.echosvg.transcoder.TranscodingHints;
import io.sf.carte.echosvg.transcoder.TranscodingHints.Key;
import io.sf.carte.echosvg.transcoder.XMLAbstractTranscoder;
import io.sf.carte.echosvg.transcoder.image.ImageTranscoder;
import io.sf.carte.echosvg.transcoder.image.JPEGTranscoder;
import io.sf.carte.echosvg.transcoder.image.PNGTranscoder;
import io.sf.carte.echosvg.transcoder.svg.SVGAbstractTranscoder;

/**
 * Validates the operation of the <code>SVGRasterizer</code>. It validates the
 * option setting and the manipulation of source and destination sources.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGConverterTest {

	private static final String PROJECT_ROOT_URL = TestUtil.getRootProjectURL(SVGConverterTest.class,
			"echosvg-svgrasterizer");

	static String resolveRelURI(String relPath) {
		URL url = resolveURI(relPath);
		String f = url.getFile();
		String ref = url.getRef();
		if (ref == null) {
			return f;
		}
		return f + '#' + ref;
	}

	static URL resolveURI(String relPath) {
		URL url;
		try {
			url = new URL(PROJECT_ROOT_URL + relPath);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		return url;
	}

	static File resolveFile(String relPath) {
		URL url = resolveURI(relPath);
		return new File(url.getFile());
	}

	///////////////////////////////////////////////////////////////////////
	// Configuration tests
	///////////////////////////////////////////////////////////////////////
	@Test
	public void testTranscoderConfig() throws SVGConverterException, IOException {
		AbstractConfigTest t = null;

		//
		// Test Transcoder usage
		//
		// TranscoderConfigTest.PNG
		t = new TranscoderConfigTest(DestinationType.PNG, io.sf.carte.echosvg.transcoder.image.PNGTranscoder.class);
		t.runTest();

		// TranscoderConfigTest.JPEG
		t = new TranscoderConfigTest(DestinationType.JPEG, io.sf.carte.echosvg.transcoder.image.JPEGTranscoder.class);
		t.runTest();

		// TranscoderConfigTest.TIFF
		t = new TranscoderConfigTest(DestinationType.TIFF, io.sf.carte.echosvg.transcoder.image.TIFFTranscoder.class);
		t.runTest();
	}

	//
	// Checks that the proper hints are used
	//
	@Test
	public void testHintsConfig() throws SVGConverterException, IOException {
		// HintsConfigTest.KEY_AOI
		HintsConfigTest t = new HintsConfigTest(
				new Object[][] { { SVGAbstractTranscoder.KEY_AOI, new Rectangle(40, 50, 40, 80) } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setArea(new Rectangle(40, 50, 40, 80));
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_QUALITY
		t = new HintsConfigTest(new Object[][] { { JPEGTranscoder.KEY_QUALITY, .5f } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setQuality(.5f);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_INDEXED
		t = new HintsConfigTest(new Object[][] { { PNGTranscoder.KEY_INDEXED, 8 } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setIndexed(8);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_BACKGROUND_COLOR
		t = new HintsConfigTest(new Object[][] { { ImageTranscoder.KEY_BACKGROUND_COLOR, Color.pink } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setBackgroundColor(Color.pink);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_HEIGHT
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_HEIGHT, 50f } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setHeight(50);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_WIDTH
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_WIDTH, 50f } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setWidth(50);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_MAX_HEIGHT
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_MAX_HEIGHT, 50f } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setMaxHeight(50);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_MAX_WIDTH
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_MAX_WIDTH, 50f } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setMaxWidth(50);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_MEDIA
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_MEDIA, "print" } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setMediaType("print");
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_DEFAULT_FONT_FAMILY
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_DEFAULT_FONT_FAMILY, "Times" } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setDefaultFontFamily("Times");
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_ALTERNATE_STYLESHEET
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET, "myStyleSheet" } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setAlternateStylesheet("myStyleSheet");
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_USER_STYLESHEET_URI
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_USER_STYLESHEET_URI,
				new File(System.getProperty("user.dir"), "userStylesheet.css").toURI().toString() } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setUserStylesheet("userStylesheet.css");
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_LANGUAGE
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_LANGUAGE, "fr" } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setLanguage("fr");
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_PIXEL_UNIT_TO_MILLIMETER
		t = new HintsConfigTest(new Object[][] { { SVGAbstractTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, .5f } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setPixelUnitToMillimeter(.5f);
			}
		};

		t.runTest();

		// HintsConfigTest.KEY_XML_PARSER_VALIDATING
		t = new HintsConfigTest(new Object[][] { { XMLAbstractTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.TRUE } }) {
			@Override
			protected void deltaConfigure(SVGConverter c) {
				c.setValidate(true);
			}
		};

		t.runTest();
	}

	//
	// Check sources
	//
	@Test
	public void testSourcesConfig() throws SVGConverterException, IOException {
		// SourcesConfigTest.SimpleList
		SourcesConfigTest t = new SourcesConfigTest(
				new String[] { "samples/anne", "samples/batikFX", "samples/tests/spec/styling/smiley" }) {
			@Override
			protected void setSources(SVGConverter c) {
				String[] srcs = new String[3];
				srcs[0] = SVGConverterTest.resolveRelURI("samples/anne.svg");
				srcs[1] = SVGConverterTest.resolveRelURI("samples/batikFX.svg");
				srcs[2] = SVGConverterTest.resolveRelURI("samples/tests/spec/styling/smiley.svg");
				c.setSources(srcs);
			}
		};

		t.runTest();
	}

	@Test
	public void testDestConfig() throws SVGConverterException, IOException {
		//
		// Check destination
		//
		// DestConfigTest.DstFile
		DestConfigTest t = new DestConfigTest(new String[] { "samples/anne.svg" },
				new String[] { "test-reports/anne.png" }) {
			@Override
			protected void setDestination(SVGConverter c) {
				c.setDst(SVGConverterTest.resolveFile("test-reports/anne.png"));
			}
		};

		t.runTest();

		// DestConfigTest.DstDir
		t = new DestConfigTest(new String[] { "samples/anne.svg", "samples/tests/spec/styling/smiley.svg" },
				new String[] { "test-resources/anne.png", "test-resources/smiley.png" }) {
			@Override
			protected void setDestination(SVGConverter c) {
				c.setDst(SVGConverterTest.resolveFile("test-resources"));
			}
		};

		t.runTest();
	}

	@Test
	public void testOperation() throws SVGConverterException, IOException {
		//
		// Check that complete process goes without error
		//
		// OperationTest.Bug4888
		OperationTest t = new OperationTest() {
			@Override
			protected void configure(SVGConverter c) {
				String[] srcs = new String[1];
				srcs[0] = SVGConverterTest.resolveURI("samples/anne.svg").getFile();
				c.setSources(srcs);
				c.setDst(SVGConverterTest.resolveFile("samples/anne.png"));
				File file = SVGConverterTest.resolveFile("samples/anne.png");
				file.deleteOnExit();
			}
		};
		t.runTest();
	}

	///////////////////////////////////////////////////////////////////////
	// Configuration error test. These tests check that the expected
	// error gets reported for a given mis-configuration
	///////////////////////////////////////////////////////////////////////
	@Test
	public void testConfigError() throws SVGConverterException, IOException {
		// ConfigErrorTest.ERROR_NO_SOURCES_SPECIFIED
		ConfigErrorTest cet = new ConfigErrorTest(SVGConverter.ERROR_NO_SOURCES_SPECIFIED) {
			@Override
			protected void configure(SVGConverter c) {
				c.setSources(null);
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_CANNOT_COMPUTE_DESTINATION
		cet = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_COMPUTE_DESTINATION) {
			@Override
			protected void configure(SVGConverter c) {
				// Do not set destination file or destination directory
				c.setSources(
						new String[] { "https://raw.githubusercontent.com/css4j/echosvg/master/samples/dummy.svg" });
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_CANNOT_USE_DST_FILE
		cet = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_USE_DST_FILE) {
			@Override
			protected void configure(SVGConverter c) {
				File dummy = null;
				try {
					dummy = File.createTempFile("dummyPNG", ".png");
				} catch (IOException ioEx) {
					throw new RuntimeException(ioEx.getMessage());
				}
				String[] srcs = new String[2];
				srcs[0] = SVGConverterTest.resolveURI("samples/anne.svg").getFile();
				srcs[1] = SVGConverterTest.resolveURI("samples/batikFX.svg").getFile();
				c.setSources(srcs);
				c.setDst(dummy);
				dummy.deleteOnExit();
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_SOURCE_SAME_AS_DESTINATION
		cet = new ConfigErrorTest(SVGConverter.ERROR_SOURCE_SAME_AS_DESTINATION) {
			@Override
			protected void configure(SVGConverter c) {
				String[] srcs = new String[1];
				srcs[0] = SVGConverterTest.resolveURI("samples/anne.svg").getFile();
				c.setSources(srcs);
				c.setDst(SVGConverterTest.resolveFile("samples/anne.svg"));
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_CANNOT_READ_SOURCE
		cet = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_READ_SOURCE) {
			@Override
			protected void configure(SVGConverter c) {
				String[] srcs = new String[1];
				srcs[0] = SVGConverterTest
						.resolveURI("test-resources/io/sf/carte/echosvg/apps/rasterizer/notReadable.svg").getFile();
				c.setSources(srcs);
				c.setDst(SVGConverterTest.resolveFile("test-reports"));
			}

			@Override
			public boolean proceedWithSourceTranscoding(SVGConverterSource source, File dest) {
				// Big hack to simulate a non-readable SVG file
				File hackedFile = new File(((SVGConverterFileSource) source).getFile().getPath()) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean canRead() {
						System.out.println("Yahoooooooo! In canRead");
						return false;
					}
				};
				((SVGConverterFileSource) source).file = hackedFile;
				return true;
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_CANNOT_OPEN_SOURCE
		cet = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_OPEN_SOURCE) {
			@Override
			protected void configure(SVGConverter c) {
				String[] srcs = new String[1];
				srcs[0] = SVGConverterTest
						.resolveURI("test-resources/io/sf/carte/echosvg/apps/rasterizer/notReadable.svg").getFile();
				c.setSources(srcs);
			}

			@Override
			public boolean proceedWithComputedTask(Transcoder transcoder, Map<Key, ?> hints,
					List<SVGConverterSource> sources, List<File> dest) {
				System.out.println("==================> Hacked Starting to process Task <=========================");
				SVGConverterFileSource source = (SVGConverterFileSource) sources.get(0);
				source = new SVGConverterFileSource(source.getFile()) {
					@Override
					public InputStream openStream() throws FileNotFoundException {
						throw new FileNotFoundException("Simulated FileNotFoundException");
					}
				};

				sources.set(0, source);
				return true;
			}

		};
		cet.runTest();

		// ConfigErrorTest.ERROR_OUTPUT_NOT_WRITEABLE
		cet = new ConfigErrorTest(SVGConverter.ERROR_OUTPUT_NOT_WRITEABLE) {
			@Override
			protected void configure(SVGConverter c) {
				String[] srcs = new String[1];
				srcs[0] = SVGConverterTest.resolveURI("samples/anne.svg").getFile();
				c.setSources(srcs);
				File o = SVGConverterTest.resolveFile(
						"test-resources/io/sf/carte/echosvg/apps/rasterizer/readOnly.png");
				o.setReadOnly();
				c.setDst(o);
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_UNABLE_TO_CREATE_OUTPUT_DIR
		cet = new ConfigErrorTest(SVGConverter.ERROR_UNABLE_TO_CREATE_OUTPUT_DIR) {
			@Override
			protected void configure(SVGConverter c) {
				c.setDst(new File("ZYZ::/cannotCreate\000"));
			}
		};
		cet.runTest();

		// ConfigErrorTest.ERROR_WHILE_RASTERIZING_FILE
		cet = new ConfigErrorTest(SVGConverter.ERROR_WHILE_RASTERIZING_FILE) {
			@Override
			protected void configure(SVGConverter c) {
				String[] srcs = new String[1];
				srcs[0] = SVGConverterTest.resolveURI(
						"test-resources/io/sf/carte/echosvg/apps/rasterizer/invalidSVG.svg").getFile();
				c.setSources(srcs);
			}
		};
		cet.runTest();
	}

	//
	// Test that files are created as expected and are producing the
	// expected result.
	//
	@Test
	public void testOutput() throws SVGConverterException, IOException {
		// Plain file
		// OutputTest.plain
		new ConverterOutputTest().runTest("samples/anne.svg", // File to convert
				"test-reports/anne.png", // Output
				"test-references/samples/anne.png"); // reference

	}

	@Test
	public void testOutputView() throws SVGConverterException, IOException {
		// File with reference
		// OutputTest.reference
		new ConverterOutputTest().runTest("samples/anne.svg#svgView(viewBox(0,0,100,200))", // File to convert
				"test-reports/anneViewBox1.png", // Output
				"test-references/samples/anneViewBox1.png"); // reference

	}

}

/**
 * A ConfigTest builds an SVGConverter, configures it, sets itself as the
 * SVGConverterController and checks that the computed task is as expected
 * (i.e., right set of hints).
 */
abstract class AbstractConfigTest implements SVGConverterController {

	/**
	 * The 'proceedWithComputedTask' handler was not called
	 */
	private static final String ERROR_NO_COMPUTED_TASK = "ConfigTest.error.no.computed.task";

	/**
	 * Configuration Description
	 */
	static class Config {

		Class<?> transcoderClass;
		HashMap<TranscodingHints.Key, Object> hints;
		List<SVGConverterSource> sources;
		List<File> dest;

	}

	private Config expectedConfig;
	private Config computedConfig;

	AbstractConfigTest() {
	}

	void setExpectedConfig(Config expectedConfig) {
		this.expectedConfig = expectedConfig;
	}

	abstract void configure(SVGConverter c);

	private String makeSourceList(List<?> v) {
		int n = v.size();
		StringBuilder sb = new StringBuilder(n * 8);
		for (Object aV : v) {
			sb.append(aV.toString());
		}

		return sb.toString();
	}

	private String makeHintsString(Map<Object, Object> map) {
		StringBuilder sb = new StringBuilder();
		for (Object key : map.keySet()) {
			sb.append(key.toString());
			sb.append('(');
			sb.append(map.get(key).toString());
			sb.append(") -- ");
		}

		return sb.toString();
	}

	public void runTest() throws SVGConverterException, IOException {
		SVGConverter c = new SVGConverter(this);
		configure(c);
		c.execute();

		//
		// Now, check that the expectedConfig and the
		// computedConfig are identical
		//
		if (computedConfig == null) {
			throw new IllegalStateException(ERROR_NO_COMPUTED_TASK);
		}

		// ERROR_UNEXPECTED_TRANSCODER_CLASS
		assertEquals(expectedConfig.transcoderClass, computedConfig.transcoderClass);

		// Compare sources
		int en = expectedConfig.sources.size();
		int cn = computedConfig.sources.size();

		// ERROR_UNEXPECTED_SOURCES_LIST
		assertEquals(en, cn);

		for (int i = 0; i < en; i++) {
			SVGConverterSource es = expectedConfig.sources.get(i);
			SVGConverterSource cs = computedConfig.sources.get(i);
			if (!computedConfig.sources.contains(es)) {
				fail("Expected source [" + i + "] = -" + es.getURI() + "- (" + es.getClass().getName() + "), computed ["
						+ i + "] = -" + cs.getURI() + "- (" + cs.getClass().getName() + ")");
			}
		}

		// Compare dest
		en = expectedConfig.dest.size();
		cn = computedConfig.dest.size();

		// ERROR_UNEXPECTED_DEST_LIST
		assertEquals(en, cn);

		for (int i = 0; i < en; i++) {
			File es = expectedConfig.dest.get(i);
			File cs = computedConfig.dest.get(i);
			if (!computedConfig.dest.contains(cs)) {
				fail("Expected dest [" + i + "] = -" + es + ", computed [" + i + "] = -" + cs);
			}
		}

		//
		// Finally, check the hints
		//
		en = expectedConfig.hints.size();
		cn = computedConfig.hints.size();

		// ERROR_UNEXPECTED_NUMBER_OF_HINTS
		assertEquals(en, cn);

		for (Object hintKey : expectedConfig.hints.keySet()) {
			Object expectedHintValue = expectedConfig.hints.get(hintKey);

			Object computedHintValue = computedConfig.hints.get(hintKey);

			if (!expectedHintValue.equals(computedHintValue)) {
				fail("Unexpected hint [" + hintKey.toString() + ", " + expectedHintValue.toString() + "], found ["
						+ computedHintValue + "]");
			}
		}

	}

	@Override
	public boolean proceedWithComputedTask(Transcoder transcoder, Map<Key, ?> hints, List<SVGConverterSource> sources,
			List<File> dest) {
		computedConfig = new Config();
		computedConfig.transcoderClass = transcoder.getClass();
		computedConfig.sources = new ArrayList<>(sources);
		computedConfig.dest = new ArrayList<>(dest);
		computedConfig.hints = new HashMap<>(hints);
		return false; // Do not proceed with the convertion process,
		// we are only checking the config in this test.
	}

	@Override
	public boolean proceedWithSourceTranscoding(SVGConverterSource source, File dest) {
		return true;
	}

	@Override
	public boolean proceedOnSourceTranscodingFailure(SVGConverterSource source, File dest, String errorCode) {
		return true;
	}

	@Override
	public void onSourceTranscodingSuccess(SVGConverterSource source, File dest) {
	}

}

/**
 * Tests that a convertion task goes without exception.
 */
class OperationTest extends AbstractConfigTest {

	@Override
	public void runTest() throws SVGConverterException {
		SVGConverter c = new SVGConverter();
		configure(c);
		try {
			c.execute();
		} catch (SVGConverterException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Override
	protected void configure(SVGConverter c) {
		// Should be overridden by subclasses.
	}

}

/**
 * Provides a simple string constructor which allows the user to create a given
 * test to check that a specific transcoder class is used for a given mime type.
 */
class TranscoderConfigTest extends AbstractConfigTest {

	private static final String SOURCE_FILE = "samples/anne.svg";
	private static final String DEST_FILE_NAME = "samples/anne";

	private DestinationType dstType;

	/**
	 * @param dstType                 type of result image
	 * @param expectedTranscoderClass class for the Transcoder expected to perform
	 *                                the convertion.
	 */
	public TranscoderConfigTest(DestinationType dstType, Class<?> expectedTranscoderClass) {
		this.dstType = dstType;

		Config config = new Config();
		config.transcoderClass = expectedTranscoderClass;

		List<SVGConverterSource> sources = new ArrayList<>();
		sources.add(new SVGConverterFileSource(SVGConverterTest.resolveFile(SOURCE_FILE)));
		config.sources = sources;

		List<File> dest = new ArrayList<>();
		dest.add(SVGConverterTest.resolveFile(DEST_FILE_NAME + dstType.getExtension()));
		config.dest = dest;

		HashMap<TranscodingHints.Key, Object> hints = new HashMap<>();
		config.hints = hints;

		setExpectedConfig(config);
	}

	/**
	 * Configures the test with the given mime type
	 */
	@Override
	public void configure(SVGConverter c) {
		String[] srcs = new String[1];
		srcs[0] = SVGConverterTest.resolveRelURI(SOURCE_FILE);
		c.setSources(srcs);
		c.setDst(SVGConverterTest.resolveFile(DEST_FILE_NAME + dstType.getExtension()));
		c.setDestinationType(dstType);
	}

}

/**
 * Provides a simple string array constructor which allows the user to create a
 * test checking for a specific hint configuration.
 */
class HintsConfigTest extends AbstractConfigTest {

	private static final String SOURCE_FILE = "samples/anne.svg";
	private static final String DEST_FILE_NAME = "samples/anne";
	private static final Class<?> EXPECTED_TRANSCODER_CLASS = io.sf.carte.echosvg.transcoder.image.PNGTranscoder.class;
	private static final DestinationType DST_TYPE = DestinationType.PNG;

	/**
	 */
	public HintsConfigTest(Object[][] hintsMap) {
		Config config = new Config();
		config.transcoderClass = EXPECTED_TRANSCODER_CLASS;

		List<SVGConverterSource> sources = new ArrayList<>();
		sources.add(new SVGConverterFileSource(SVGConverterTest.resolveFile(SOURCE_FILE)));
		config.sources = sources;

		List<File> dest = new ArrayList<>();
		dest.add(SVGConverterTest.resolveFile(DEST_FILE_NAME + DST_TYPE.getExtension()));
		config.dest = dest;

		HashMap<TranscodingHints.Key, Object> hints = new HashMap<>();

		//
		// Add hints from constructor argument
		//
		for (Object[] aHintsMap : hintsMap) {
			hints.put((Key) aHintsMap[0], aHintsMap[1]);
		}
		config.hints = hints;

		setExpectedConfig(config);
	}

	/**
	 * Configures the test with the given mime type
	 */
	@Override
	public void configure(SVGConverter c) {
		String[] srcs = new String[1];
		srcs[0] = SVGConverterTest.resolveRelURI(SOURCE_FILE);
		c.setSources(srcs);
		c.setDst(SVGConverterTest.resolveFile(DEST_FILE_NAME + DST_TYPE.getExtension()));
		c.setDestinationType(DST_TYPE);
		deltaConfigure(c);
	}

	protected void deltaConfigure(SVGConverter c) {
	}

}

/**
 * Provides a simple string array constructor which allows the user to create a
 * test checking for a specific source configuration. The constructor argument
 * takes the list of expected files and the deltaConfigure method should set the
 * sources which is expected to produce that list of sources. The sources should
 * be file names which ommit the ".svg" extension.
 */
class SourcesConfigTest extends AbstractConfigTest {

	private static final Class<?> EXPECTED_TRANSCODER_CLASS = io.sf.carte.echosvg.transcoder.image.PNGTranscoder.class;
	private static final DestinationType DST_TYPE = DestinationType.PNG;
	private static final String SVG_EXTENSION = ".svg";

	/**
	 */
	public SourcesConfigTest(String[] expectedSources) {
		Config config = new Config();
		config.transcoderClass = EXPECTED_TRANSCODER_CLASS;

		List<SVGConverterSource> sources = new ArrayList<>();
		List<File> dest = new ArrayList<>();
		for (String expectedSource : expectedSources) {
			sources.add(new SVGConverterFileSource(
					SVGConverterTest.resolveFile(expectedSource + SVG_EXTENSION)));
			dest.add(SVGConverterTest.resolveFile(expectedSource + DST_TYPE.getExtension()));
		}
		config.sources = sources;
		config.dest = dest;

		HashMap<TranscodingHints.Key, Object> hints = new HashMap<>();
		config.hints = hints;

		setExpectedConfig(config);
	}

	/**
	 * Configures the test with the given mime type
	 */
	@Override
	public void configure(SVGConverter c) {
		c.setDestinationType(DST_TYPE);
		setSources(c);
	}

	protected void setSources(SVGConverter c) {
	}

}

/**
 * Provides a simple string array constructor which allows the user to create a
 * test checking for a specific destination configuration. The constructor
 * argument takes the list of sources and the list of expected configuration
 * which is influenced by the 'setDestination' content.
 */
class DestConfigTest extends AbstractConfigTest {

	private static final Class<?> EXPECTED_TRANSCODER_CLASS = io.sf.carte.echosvg.transcoder.image.PNGTranscoder.class;
	private static final DestinationType DST_TYPE = DestinationType.PNG;
	private String[] sourcesStrings;

	/**
	 */
	public DestConfigTest(String[] sourcesStrings, String[] expectedDest) {
		this.sourcesStrings = sourcesStrings;
		Config config = new Config();
		config.transcoderClass = EXPECTED_TRANSCODER_CLASS;

		List<SVGConverterSource> sources = new ArrayList<>();
		List<File> dest = new ArrayList<>();
		for (int i = 0; i < sourcesStrings.length; i++) {
			String sourcesString = sourcesStrings[i];
			File f = SVGConverterTest.resolveFile(sourcesString);
			this.sourcesStrings[i] = f.getAbsolutePath();
			sources.add(new SVGConverterFileSource(f));
		}

		for (String anExpectedDest : expectedDest) {
			dest.add(SVGConverterTest.resolveFile(anExpectedDest));
		}

		config.sources = sources;
		config.dest = dest;

		HashMap<TranscodingHints.Key, Object> hints = new HashMap<>();
		config.hints = hints;

		setExpectedConfig(config);
	}

	/**
	 * Configures the test with the given mime type
	 */
	@Override
	public void configure(SVGConverter c) {
		c.setDestinationType(DST_TYPE);
		c.setSources(sourcesStrings);
		setDestination(c);
	}

	protected void setDestination(SVGConverter c) {
	}

}

/**
 * This test lously checks that errors are reported as expected. It checks that
 * the error code given at construction time is reported either my an exception
 * thrown from the execute method or during the processing of single files in
 * the SVGConverterController handler.
 */
class ConfigErrorTest implements SVGConverterController {

	String errorCode;

	String foundErrorCode = null;

	public ConfigErrorTest(String expectedErrorCode) {
		this.errorCode = expectedErrorCode;
	}

	public void runTest() {
		SVGConverter c = new SVGConverter(this);

		c.setDestinationType(DestinationType.PNG);

		String[] srcs = new String[1];
		srcs[0] = SVGConverterTest.resolveURI("samples/anne.svg").getFile();
		c.setSources(srcs);

		configure(c);

		try {
			c.execute();
		} catch (SVGConverterException e) {
			foundErrorCode = e.getErrorCode();
		}

		assertEquals(errorCode, foundErrorCode, "Expected error code " + errorCode);
	}

	protected void configure(SVGConverter c) {
	}

	@Override
	public boolean proceedWithComputedTask(Transcoder transcoder, Map<Key, ?> hints, List<SVGConverterSource> sources,
			List<File> dest) {
		// Starting to process Task
		return true;
	}

	@Override
	public boolean proceedWithSourceTranscoding(SVGConverterSource source, File dest) {
		// Transcoding source to dest
		return true;
	}

	@Override
	public boolean proceedOnSourceTranscodingFailure(SVGConverterSource source, File dest, String errorCode) {
		// FAILURE
		foundErrorCode = errorCode;
		return true;
	}

	@Override
	public void onSourceTranscodingSuccess(SVGConverterSource source, File dest) {
		// SUCCESS
	}

}

/**
 * This test checks that a file is indeed created and that it is identical to an
 * expected reference.
 */
class ConverterOutputTest {

	private static final String PROJECT_NAME = "echosvg-svgrasterizer";

	public void runTest(String svgSource, String pngDest, String pngRef) throws SVGConverterException,
			IOException {
		SVGConverter c = new SVGConverter();

		String[] srcs = new String[1];
		srcs[0] = SVGConverterTest.resolveRelURI(svgSource);
		c.setSources(srcs);
		File destFile = SVGConverterTest.resolveFile(pngDest);
		c.setDst(destFile);

		c.setDestinationType(DestinationType.PNG);

		c.execute();

		URL urlRef = new URL(TestUtil.getRootProjectURL(SVGConverterTest.class, PROJECT_NAME) + pngRef);
		pngRef = urlRef.toExternalForm();

		pngDest = destFile.toURI().toURL().toExternalForm();

		TempImageFiles fileUtil = new TempImageFiles(TestUtil.getProjectBuildURL(SVGConverterTest.class,
				PROJECT_NAME));
		ImageCompareUtil t = new ImageCompareUtil(fileUtil, 8, pngDest, pngRef);
		String err = t.compare(0.9f, 0.06f);

		if (err != null) {
			fail(err);
		} else {
			destFile.delete();
		}
	}

}
