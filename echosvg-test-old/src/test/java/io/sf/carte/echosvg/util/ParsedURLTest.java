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
package io.sf.carte.echosvg.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This test validates that the ParsedURL class properly parses and cascades
 * URLs.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ParsedURLTest {

	@Test
	public void testURL() {
		runTest("just.a.path", "file:just.a.path");
		runTest("fooz:/home/deweese/.cshrc", "fooz:/home/deweese/.cshrc");
		runTest("fazz:///home/deweese/.cshrc", "fazz:/home/deweese/.cshrc");
		runTest("fooz://www.com:1234/home/deweese/.cshrc#abcd", "fooz://www.com:1234/home/deweese/.cshrc#abcd");
		runTest("http://xml.apache.org/", "~deweese", "http://xml.apache.org/~deweese");
		runTest("file:///xml.apache.org", "~deweese", "file:/~deweese");
		runTest("fooz://www.com:1234/home/deweese/.cshrc#abcd", "xyz.html#efgh",
				"fooz://www.com:1234/home/deweese/xyz.html#efgh");
		runTest("fooz://www.com:1234/home/deweese/xyz.html#efgh", "/xyz.svg#ijkl", "fooz://www.com:1234/xyz.svg#ijkl");
		runTest("file:/home/deweese/test.txt", "file:junk.html", "file:/home/deweese/junk.html");
		runTest("http://xml.apache.org/batik/", "//jakarta.apache.org/ant/", "http://jakarta.apache.org/ant/");
		runTest("http://xml.apache.org/batik/#test", "", "http://xml.apache.org/batik/#test");
		runTest("http://xml.apache.org/batik/", "/", "http://xml.apache.org/");
		runTest("http://xml.apache.org/batik/", "/fop/", "http://xml.apache.org/fop/");
		runTest("file:helloWorld.svg", "file:test.svg#Foo", "file:test.svg#Foo");
		runTest("file:", "file:junk.svg#Bar", "file:junk.svg#Bar");
		runTest("#data:1", "#data:1");
		runTest("http://foo.bar.com/path/test.svg", "#data:1", "http://foo.bar.com/path/test.svg#data:1");
	}

	@Test
	public void testJar() {
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "jar:file:dir/file.jar!/p/a/t/h/init.svg");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg#foo", "jar:file:dir/file.jar!/p/a/t/h/init.svg#foo");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "../base.svg", "jar:file:dir/file.jar!/p/a/t/base.svg");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "#foo", "jar:file:dir/file.jar!/p/a/t/h/init.svg#foo");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg#bar", "#faz", "jar:file:dir/file.jar!/p/a/t/h/init.svg#faz");
		runTest("jar:file:/path1!/path2", "jar:file:/path3!/path4", "jar:file:/path3!/path4");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "jar:file:dir/file.jar!/b/a/t/i/k/new.svg#bar",
				"jar:file:dir/file.jar!/b/a/t/i/k/new.svg#bar");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "/e/c/h/o/new.svg",
				"jar:file:dir/file.jar!/e/c/h/o/new.svg");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "/e/c/h/o/new.svg#bar",
				"jar:file:dir/file.jar!/e/c/h/o/new.svg#bar");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "t/o/new.svg", "jar:file:dir/file.jar!/p/a/t/h/t/o/new.svg");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "t/o/new.svg#foo",
				"jar:file:dir/file.jar!/p/a/t/h/t/o/new.svg#foo");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "../c/h/new.svg",
				"jar:file:dir/file.jar!/p/a/t/c/h/new.svg");
		runTest("jar:file:dir/file.jar!/p/a/t/h/init.svg", "../c/h/new.svg#foo",
				"jar:file:dir/file.jar!/p/a/t/c/h/new.svg#foo");
	}

	@Test
	public void testBase64Data() {
		runTest("data:image/png;base64,R0lGODdhMAAw",
				"ct: image/png CE: base64 DATA: 47 49 46 38 37 URL: data:image/png;base64,R0lGODdhMAAw");
		runTest("data:text/plain;charset=US-ASCII;base64,R0lGODdhMAAw",
				"ct: text/plain;charset=US-ASCII CE: base64 DATA: 47 49 46 38 37 URL: data:text/plain;charset=US-ASCII;base64,R0lGODdhMAAw");
		runTest("data:image/gif;base64,R0lGODdhMAAw",
				"ct: image/gif CE: base64 DATA: 47 49 46 38 37 URL: data:image/gif;base64,R0lGODdhMAAw");
		runTest("data:text/plain;charset=iso-8859-7,%be%fb%be",
				"ct: text/plain;charset=iso-8859-7 CE: null DATA: be fb be URL: data:text/plain;charset=iso-8859-7,%be%fb%be");
		runTest("data:,A%20brief%20note", "ct: null CE: null DATA: 2c 41 20 62 72 URL: data:,,A%20brief%20note");
		runTest("data:;=;,A%20brief%20note", "ct: ;= CE:  DATA: 41 20 62 72 69 URL: data:;=;,A%20brief%20note");
	}

	/**
	 * Run test
	 * 
	 * @param base The base url to parse
	 * @param sub  The sub url (relative to base).
	 */
	private void runTest(String base, String sub) {
		runTest(base, sub, sub);
	}

	/**
	 * Run test
	 * 
	 * @param base The base url to parse
	 * @param sub  The sub url (relative to base).
	 * @param ref  The expected result.
	 */
	private void runTest(String base, String sub, String ref) {
		ParsedURL url = new ParsedURL(base);
		if (sub != null) {
			url = new ParsedURL(url, sub);
		}

		assertEquals("Bad URL: ", ref, url.toString());
	}
}
