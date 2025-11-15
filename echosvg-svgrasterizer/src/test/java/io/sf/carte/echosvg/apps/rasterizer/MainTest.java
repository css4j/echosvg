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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Validates the operation of the <code>Main</code> class.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
@Tag("SecMan")
@SuppressWarnings({ "deprecation", "removal" })
public class MainTest {

	@AfterEach
	public void tearDown() throws Exception {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
				@Override
				public Void run() throws Exception {
					System.setSecurityManager(null);
					System.setProperty("java.security.policy", "");
					return null;
				}
			});
		} catch (PrivilegedActionException pae) {
			throw pae.getException();
		}
	}

	private static String getRootProjectDirectory() {
		String resName = MainTest.class.getName().replace(".", "/") + ".class";
		URL url = MainTest.class.getResource(resName);
		if (url == null) {
			url = cwdURL();
		}
		String s = url.getFile();
		final String PROJECT_DIRNAME = "echosvg-svgrasterizer";
		int testDirIdx = s.lastIndexOf(PROJECT_DIRNAME);
		if (testDirIdx != -1) {
			s = s.substring(0, testDirIdx);
		} // If no PROJECT_DIRNAME, we probably got the root via CWD
		return s;
	}

	private static URL cwdURL() {
		try {
			return Paths.get(".").toAbsolutePath().normalize().toUri().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	// This test interferes with Gradle
	// Run it from IDE only
	@Test
	public void testMain() {
		String rootDir = getRootProjectDirectory();

		// MainConfigTest.output
		MainConfigTest t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				File dst = c.getDst();
				assertNotNull(dst);
				assertEquals("samples", dst.getName(), "-d");
			}

			@Override
			void onError(String errorCode, Object[] errorArgs) {
				fail("Unexpected error: " + errorCode);
			}

		};
		t.runTest("-d " + rootDir + "samples");

		// MainConfigTest.source

		t = new MainConfigTest() {
			String ERROR_UNEXPECTED_SOURCES = "MainConfigTest.error.unexpected.sources";

			@Override
			public void validate(SVGConverter c) {
				List<String> sources = c.getSources();
				assertEquals(1, sources.size());
				String src = sources.get(0);
				assertEquals(rootDir + "samples/anne.svg", src, ERROR_UNEXPECTED_SOURCES);
			}

		};
		t.runTest(rootDir + "samples/anne.svg");

		File file = new File(rootDir + "samples/anne.png");
		file.deleteOnExit();

		// MainConfigTest.mimeType.jpegA

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				DestinationType type = c.getDestinationType();
				assertEquals(DestinationType.JPEG, type);
				assertEquals(DestinationType.JPEG.toString(), type.toString(), "-m");
			}

		};
		t.runTest("-m image/jpeg");

		// MainConfigTest.mimeType.jpegB

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				DestinationType type = c.getDestinationType();
				assertEquals(DestinationType.JPEG, type);
				assertEquals(DestinationType.JPEG.toString(), type.toString(), "-m");
			}

		};
		t.runTest("-m image/jpg");

		// MainConfigTest.mimeType.jpegC

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				DestinationType type = c.getDestinationType();
				assertEquals(DestinationType.JPEG, type);
				assertEquals(DestinationType.JPEG.toString(), type.toString(), "-m");
			}

		};
		t.runTest("-m image/jpe");

		// MainConfigTest.mimeType.png

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				DestinationType type = c.getDestinationType();
				assertEquals(DestinationType.PNG, type);
				assertEquals(DestinationType.PNG.toString(), type.toString(), "-m");
			}

		};
		t.runTest("-m image/png");

		// MainConfigTest.mimeType.tiff

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				DestinationType type = c.getDestinationType();
				assertEquals(DestinationType.TIFF, type);
				assertEquals(DestinationType.TIFF.toString(), type.toString(), "-m");
			}

		};
		t.runTest("-m image/tiff");

		// MainConfigTest.width

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				float width = c.getWidth();
				assertEquals(467.69f, width, 1e-3, "-w");
			}

		};
		t.runTest("-w 467.69");

		// MainConfigTest.height

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				float height = c.getHeight();
				assertEquals(345.67, height, 1e-3, "-h");
			}

		};
		t.runTest("-h 345.67");

		// MainConfigTest.maxWidth
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				float maxWidth = c.getMaxWidth();
				assertEquals(467.69f, maxWidth, 1e-3, "-maxw");
			}

		};
		t.runTest("-maxw 467.69");

		// MainConfigTest.maxHeight
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				float maxHeight = c.getMaxHeight();
				assertEquals(345.67, maxHeight, 1e-3, "-maxh");
			}
		};
		t.runTest("-maxh 345.67");

		// MainConfigTest.aoi

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				Rectangle2D aoi = c.getArea();
				Rectangle2D.Float eAoi = new Rectangle2D.Float(5, 10, 20, 30);
				assertEquals(eAoi, aoi, "-a");
				assertEquals(toString(eAoi), toString(aoi), "-a");
			}

			public String toString(Rectangle2D r) {
				if (r == null) {
					return "null";
				} else {
					return r.getX() + "," + r.getY() + "," + r.getWidth() + "," + r.getHeight();
				}
			}

		};
		t.runTest("-a 5,10,20,30");

		// MainConfigTest.backgroundColor

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				Color bg = c.getBackgroundColor();
				Color eBg = new Color(200, 100, 50, 128); // Alpha is last
				assertEquals(eBg, bg, "-bg");
				assertEquals(toString(eBg), toString(bg), "-bg");
			}

			public String toString(Color c) {
				if (c == null) {
					return "null";
				} else {
					return c.getAlpha() + "." + c.getRed() + "." + c.getGreen() + "." + c.getBlue();
				}
			}

		};
		t.runTest("-bg 128.200.100.50");

		// MainConfigTest.cssMedia

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				String cssMedia = c.getMediaType();
				String eCssMedia = "projection";
				assertEquals(eCssMedia, cssMedia, "-cssMedia");
			}
		};
		t.runTest("-cssMedia projection");

		// MainConfigTest.fontFamily

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				String fontFamily = c.getDefaultFontFamily();
				String eFontFamily = "Arial, Comic Sans MS";
				assertEquals(eFontFamily, fontFamily, "-font-family");
			}

			@Override
			String[] makeArgsArray(String args) {
				return new String[] { "-font-family", "Arial, Comic Sans MS" };
			}
		};
		t.runTest("-font-family Arial, Comic Sans MS");

		// MainConfigTest.cssAlternate
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				String alternate = c.getAlternateStylesheet();
				String eAlternate = "myAlternateStylesheet";
				assertEquals(eAlternate, alternate, "-cssAlternate");
			}
		};
		t.runTest("-cssAlternate myAlternateStylesheet");

		// MainConfigTest.validate

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertTrue(c.getValidate(), "-validate");
			}
		};
		t.runTest("-validate");

		// MainConfigTest.onload

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertTrue(c.getExecuteOnload(), "-onload");
			}
		};
		t.runTest("-onload");

		// MainConfigTest.scripts

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertEquals("text/jpython", c.getAllowedScriptTypes(), "-scripts");
			}
		};
		t.runTest("-scripts text/jpython");

		// MainConfigTest.anyScriptOrigin

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertFalse(c.getConstrainScriptOrigin(), "-anyScriptOrigin");
			}
		};
		t.runTest("-anyScriptOrigin");

		// MainConfigTest.scriptSecurityOff

		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertTrue(c.getSecurityOff(), "-scriptSecurityOff");
			}
		};
		t.runTest("-scriptSecurityOff");

		// MainConfigTest.lang
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertEquals("fr", c.getLanguage(), "-lang");
			}
		};
		t.runTest("-lang fr");

		// MainConfigTest.cssUser
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertEquals("myStylesheet.css", c.getUserStylesheet(), "-cssUser");
			}
		};
		t.runTest("-cssUser myStylesheet.css");

		// MainConfigTest.dpi
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertEquals(5f, c.getPixelUnitToMillimeter(), 1e-3, "-dpi");
			}
		};
		t.runTest("-dpi 5.08");

		// MainConfigTest.quality
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertEquals(.5f, c.getQuality(), 1e-3, "-q");
			}
		};
		t.runTest("-q .5");

		// MainConfigTest.indexed
		t = new MainConfigTest() {
			@Override
			public void validate(SVGConverter c) {
				assertEquals(8, c.getIndexed(), "-indexed");
			}
		};
		t.runTest("-indexed 8");

		new MainConfigErrorTest().runTest("-d", "hello.svg -d");
		// MainConfigErrorTest.output

		new MainConfigErrorTest().runTest("-m", "hello.svg -m");
		// MainConfigErrorTest.mimeType

		new MainConfigErrorTest().runTest("-w", "hello.svg -w");
		// MainConfigErrorTest.width

		new MainConfigErrorTest().runTest("-h", "hello.svg -h");
		// MainConfigErrorTest.height

		new MainConfigErrorTest().runTest("-maxw", "hello.svg -maxw");
		// MainConfigErrorTest.maxWidth

		new MainConfigErrorTest().runTest("-maxh", "hello.svg -maxh");
		// MainConfigErrorTest.maxHeight

		new MainConfigErrorTest().runTest("-a", "hello.svg -a");
		// MainConfigErrorTest.area

		new MainConfigErrorTest().runTest("-bg", "hello.svg -bg");
		// MainConfigErrorTest.backgroundColor

		new MainConfigErrorTest().runTest("-cssMedia", "hello.svg -cssMedia");
		// MainConfigErrorTest.mediaType

		new MainConfigErrorTest().runTest("-font-family", "hello.svg -font-family");
		// MainConfigErrorTest.font-family

		new MainConfigErrorTest().runTest("-cssAlternate", "hello.svg -cssAlternate");
		// MainConfigErrorTest.cssAlternate

		new MainConfigErrorTest().runTest("-lang", "hello.svg -lang");
		// MainConfigErrorTest.lang

		new MainConfigErrorTest().runTest("-cssUser", "hello.svg -cssUser");
		// MainConfigErrorTest.cssUser

		new MainConfigErrorTest().runTest("-dpi", "hello.svg -dpi");
		// MainConfigErrorTest.dpi

		new MainConfigErrorTest().runTest("-q", "hello.svg -q");
		// MainConfigErrorTest.quality

		new MainConfigErrorTest().runTest("-scripts", "hello.svg -scripts");
		// MainConfigErrorTest.allowedScriptTypes

		new MainIllegalArgTest().runTest("-m", "-m images/jpeq");
		// MainIllegalArgTest.mediaType

		new MainIllegalArgTest().runTest("-w", "-w abd");
		// MainIllegalArgTest.width

		new MainIllegalArgTest().runTest("-h", "-h abaa");
		// MainIllegalArgTest.height

		new MainIllegalArgTest().runTest("-maxw", "-maxw abd");
		// MainIllegalArgTest.maxWidth

		new MainIllegalArgTest().runTest("-maxh", "-maxh abaa");
		// MainIllegalArgTest.maxHeight

		new MainIllegalArgTest().runTest("a", "-a aaaaaa");
		// MainIllegalArgTest.aoi

		new MainIllegalArgTest().runTest("bg", "-bg a.b.c.d");
		// MainIllegalArgTest.bg

		new MainIllegalArgTest().runTest("dpi", "-dpi invalidDPI");
		// MainIllegalArgTest.dpi

		new MainIllegalArgTest().runTest("q", "-q illegalQuality");
		// MainIllegalArgTest.q

	}

}

abstract class AbstractMainTest {
	private static final String ERROR_UNEXPECTED_ERROR_CODE = "MainConfigErrorTest.error.unexpected.error.code";

	private boolean usagePrinted = false;

	public void runTest(String args) {
		String[] argsArray = makeArgsArray(args);
		Main main = new Main(argsArray) {
			@Override
			public void validateConverterConfig(SVGConverter c) {
				validate(c);
			}

			@Override
			protected void error(String errorCode, Object[] errorArgs) {
				onError(errorCode, errorArgs);
			}

			@Override
			protected void printUsage() {
				usagePrinted = true;
			}

		};

		main.execute();
	}

	protected void validate(SVGConverter c) {
	}

	void onError(String errorCode, Object[] errorArgs) {
		assertNoError(errorCode);
	}

	void assertNoError(String errorCode) {
		assertNull(errorCode, "Unexpected error code");
	}

	void assertError(String expectedErrorCode, String errorCode) {
		assertEquals(expectedErrorCode, errorCode, ERROR_UNEXPECTED_ERROR_CODE);
	}

	void assertUsagePrinted() {
		assertTrue(usagePrinted, "Usage was not printed");
	}

	String[] makeArgsArray(String args) {
		StringTokenizer st = new StringTokenizer(args, " ");
		String[] argsArray = new String[st.countTokens()];
		for (int i = 0; i < argsArray.length; i++) {
			argsArray[i] = st.nextToken();
		}

		return argsArray;
	}

}

class MainIllegalArgTest extends AbstractMainTest {

	private static final String ERROR_UNEXPECTED_ERROR_CODE = "MainIllegalArgTest.error.unexpected.error.code";

	public void runTest(String badOption, String args) {
		runTest(args);
	}

	@Override
	void onError(String errorCode, Object[] errorArgs) {
		assertEquals(Main.ERROR_ILLEGAL_ARGUMENT, errorCode, ERROR_UNEXPECTED_ERROR_CODE);
	}

	@Override
	void assertError(String expectedErrorCode, String errorCode) {
		super.assertError(expectedErrorCode, errorCode);
		assertUsagePrinted();
	}

}

class MainConfigErrorTest extends AbstractMainTest {

	private String badOption = null;

	public void runTest(String badOption, String args) {
		this.badOption = badOption;
		runTest(args);
	}

	@Override
	void onError(String errorCode, Object[] errorArgs) {
		assertError(Main.ERROR_NOT_ENOUGH_OPTION_VALUES, errorCode);
		assertNotNull(errorArgs);
		assertTrue(errorArgs.length > 0);
		assertEquals(badOption, errorArgs[0]);
	}

}

abstract class MainConfigTest extends AbstractMainTest {

	static final String ERROR_UNEXPECTED_OPTION_VALUE = "MainConfigTest.error.unexpected.option.value";

	static final String ENTRY_KEY_OPTION = "MainConfigTest.entry.key.option";

	static final String ENTRY_KEY_EXPECTED_VALUE = "MainConfigTest.entry.key.expected.value";

	static final String ENTRY_KEY_ACTUAL_VALUE = "MainConfigTest.entry.key.actual.value";

	@Override
	public abstract void validate(SVGConverter c);

}
