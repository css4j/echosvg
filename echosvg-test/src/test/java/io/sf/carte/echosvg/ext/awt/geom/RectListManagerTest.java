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

package io.sf.carte.echosvg.ext.awt.geom;

import static org.junit.Assert.fail;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.junit.Test;

import io.sf.carte.echosvg.test.TestLocations;
import io.sf.carte.echosvg.util.Base64Test;

/**
 * This test validates that the text selection APIs work properly.
 *
 * @author <a href="mailto:deweese@apache.org">Thomas DeWeese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class RectListManagerTest {

	/**
	 * Directory for reference files
	 */
	private static final String REFERENCE_DIR = TestLocations.PROJECT_ROOT_URL
			+ "test-references/io/sf/carte/echosvg/ext/awt/geom/";

	//private static final String VARIATION_DIR = "variation/";

	private static final String CANDIDATE_DIR = "candidate-ref/";

	private static final String RECT_PREF = "rect";
	private static final String RLM_PREF = "rectlistmanger";
	private static final String MERGE_PREF = "merge";
	private static final String ADD_PREF = "add";
	private static final String SUBTRACT_PREF = "subtract";
	private static final String CONTAINS_ALL_PREF = "containsall";
	private static final String REMOVE_ALL_PREF = "removeall";
	private static final String RETAIN_ALL_PREF = "retainall";
	private static final String PRINT_PREF = "print";

	/**
	 * Resolves the input string. If it is an invalid
	 * URL, an IllegalArgumentException is thrown.
	 */
	private URL resolveURI(String url) {
		try {
			return new URL(REFERENCE_DIR + url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(url);
		}
	}

	@Test
	public void test() throws IOException {
		testRectListManager("rlm.sort.in", "rlm.sort.out");
		testRectListManager("rlm.containsall.in", "rlm.containsall.out");
		testRectListManager("rlm.removeall.in", "rlm.removeall.out");
		testRectListManager("rlm.retainall.in", "rlm.retainall.out");
		testRectListManager("rlm.merge.in", "rlm.merge.out");
		testRectListManager("rlm.subtract.in", "rlm.subtract.out");
	}

	/**
	 * refFilename is ignored if action == ROUND.
	 * 
	 * @param rectsFilename The rects file to load
	 * @param refFilename   The reference file.
	 * @throws IOException 
	 */
	private void testRectListManager(String rectsFilename, String refFilename) throws IOException {
		URL rects = null;
		URL ref = null;
		//URL var = null;
		File can = null;

		rects = resolveURI(rectsFilename);
		ref = resolveURI(refFilename);
		// var = resolveURI(VARIATION_DIR+ref); // no variation
		// file(s)

		File parent = new File(resolveURI(CANDIDATE_DIR).getFile());
		if (!parent.exists()) {
			if (!parent.mkdir()) {
				fail("Could not create candidate directory: " + parent.getAbsolutePath());
			}
		}
		can = new File(parent, refFilename);

		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(rects.openStream()));

		// Now write a candidate reference/variation file...
		if (can.exists())
			can.delete();

		FileOutputStream fos = new FileOutputStream(can);
		PrintStream ps = new PrintStream(fos);

		Map<String, Object> rlms = new HashMap<>();
		RectListManager currRLM = null;
		String currID = null;
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.toLowerCase();
			StringTokenizer st = new StringTokenizer(line);

			// Check blank line...
			if (!st.hasMoreTokens())
				continue;

			// Get first token
			String pref = st.nextToken();

			// Check for comment.
			if (pref.startsWith("#"))
				continue;

			if (RECT_PREF.equals(pref)) {
				if (st.countTokens() != 4)
					continue;
				if (currRLM == null)
					continue;

				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				int w = Integer.parseInt(st.nextToken());
				int h = Integer.parseInt(st.nextToken());
				currRLM.add(new Rectangle(x, y, w, h));
			} else if (RLM_PREF.equals(pref)) {
				String id = st.nextToken();
				Object o = rlms.get(id);
				if (o == null) {
					o = new RectListManager();
					rlms.put(id, o);
				}
				currRLM = (RectListManager) o;
				currID = id;
			} else if (MERGE_PREF.equals(pref)) {
				if (currRLM == null)
					continue;
				int overhead = Integer.parseInt(st.nextToken());
				int lineOverhead = Integer.parseInt(st.nextToken());
				currRLM.mergeRects(overhead, lineOverhead);
			} else if (ADD_PREF.equals(pref)) {
				if (currRLM == null)
					continue;
				String id = st.nextToken();
				Object o = rlms.get(id);
				if (o == null)
					continue;
				currRLM.add((RectListManager) o);
			} else if (SUBTRACT_PREF.equals(pref)) {
				if (currRLM == null)
					continue;
				String id = st.nextToken();
				Object o = rlms.get(id);
				if (o == null)
					continue;
				int overhead = Integer.parseInt(st.nextToken());
				int lineOverhead = Integer.parseInt(st.nextToken());
				currRLM.subtract((RectListManager) o, overhead, lineOverhead);
			} else if (CONTAINS_ALL_PREF.equals(pref)) {
				if (currRLM == null)
					continue;
				String id = st.nextToken();
				Object o = rlms.get(id);
				if (o == null)
					continue;
				RectListManager rlm = (RectListManager) o;
				ps.println("ID: " + currID + " Sz: " + currRLM.size());

				if (currRLM.containsAll(rlm)) {
					ps.println("  Contains all: " + id + " Sz: " + rlm.size());
				} else {
					ps.println("  Does not contain all: " + id + " Sz: " + rlm.size());
				}
				ps.println();
			} else if (REMOVE_ALL_PREF.equals(pref)) {
				if (currRLM == null)
					continue;
				String id = st.nextToken();
				Object o = rlms.get(id);
				if (o == null)
					continue;
				currRLM.removeAll((RectListManager) o);
			} else if (RETAIN_ALL_PREF.equals(pref)) {
				if (currRLM == null)
					continue;
				String id = st.nextToken();
				Object o = rlms.get(id);
				if (o == null)
					continue;
				currRLM.retainAll((RectListManager) o);
			} else if (PRINT_PREF.equals(pref)) {
				if (currRLM == null)
					continue;

				Iterator<Rectangle> i = currRLM.iterator();
				ps.println("ID: " + currID + " Sz: " + currRLM.size());
				while (i.hasNext()) {
					ps.println("  " + i.next());
				}
				ps.println();
			}
		}

		ps.close();
		fos.close();

		InputStream refIS = ref.openStream();
		try {
//			refIS = var.openStream();
		} catch (Exception e) {
		}

		int mismatch = -2;
		if (refIS != null) {
			InputStream canIS = new FileInputStream(can);
			Checker check = new Checker(canIS, refIS);
			check.start();
			mismatch = check.getMismatch();
		} else {
			fail("Reference file does not exist: " + ref.toExternalForm());
		}

		if (mismatch == -1) {
			can.delete();
			return;
		}

		fail("Result doesn't match, starting at byte: " + mismatch);
	}

	private static class Checker extends Thread {
		int mismatch = -2;
		InputStream is1, is2;

		public Checker(InputStream is1, InputStream is2) {
			this.is1 = is1;
			this.is2 = is2;
		}

		public int getMismatch() {
			while (true) {
				try {
					this.join();
					break;
				} catch (InterruptedException ie) {
				}
			}

			return mismatch;
		}

		@Override
		public void run() {
			mismatch = Base64Test.compareStreams(is1, is2, true);
		}
	}

}
