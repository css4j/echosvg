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
package io.sf.carte.echosvg.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.sf.carte.echosvg.util.CleanerThread;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class MemoryLeakTest {

	// I know that 120 seems _really_ high but it turns out
	// That the "GraphicsNodeTree" was not being cleared when I
	// tested with as high as 60. So I would leave it at 120
	// (why so large I don't know) - it will bail if the all
	// the objects of interest are collected sooner so the runtime
	// is really only a concern for failures.
	private static final int NUM_GC = 10;
	private static final int MIN_MEMORY = 200000; // 200KB
	private static final int ALLOC_SZ = 1000; // 100KB

	private static final String ERROR_OBJS_NOT_CLEARED = "MemoryLeakTest.message.error.objs.not.cleared";

	private String testedUri = null;

	public static String fmt(String key, Object[] args) {
		return Messages.formatMessage(key, args);
	}

	public MemoryLeakTest() {
	}

	Map<String, WeakRef> objs = new HashMap<>();
	List<String> entries = new ArrayList<>();

	public String getURI() {
		return testedUri;
	}

	protected String getFilename() {
		URL url;
		try {
			url = new URL(getURI());
		} catch (MalformedURLException e) {
			return "";
		}
		String path = url.getPath();
		int idx = path.lastIndexOf('/');
		if (idx == -1) {
			return path;
		}
		return path.substring(idx + 1);
	}

	public void registerObject(Object o) {
		synchronized (objs) {
			String desc = o.toString();
			objs.put(desc, new WeakRef(o, desc));
		}
	}

	public void registerObjectDesc(Object o, String desc) {
		synchronized (objs) {
			objs.put(desc, new WeakRef(o, desc));
		}
	}

	public boolean checkObject(String desc) {
		String[] strs = new String[1];
		strs[0] = desc;
		return checkObjects(strs);
	}

	public boolean checkObjects(String[] descs) {
		Runtime rt = Runtime.getRuntime();
		List<List<byte[]>> l = new ArrayList<>();
		int nBlock = (int) (rt.totalMemory() / (ALLOC_SZ * NUM_GC));
		try {
			while (true) {
				boolean passed = true;
				synchronized (objs) {
					// System.err.println("FreeMemory: " + rt.freeMemory() +
					// " of " +rt.totalMemory());

					for (String desc : descs) {
						WeakRef wr = objs.get(desc);
						if ((wr == null) || (wr.get() == null))
							continue;
						passed = false;
						break;
					}
				}
				if (passed)
					return true;

				List<byte[]> l2 = new ArrayList<>();
				for (int i = 0; i < nBlock; i++) {
					l2.add(new byte[ALLOC_SZ]);
				}
				l.add(l2);
			}
		} catch (OutOfMemoryError oom) {
		} finally {
			l = null;
		}

		for (int i = 0; i < NUM_GC; i++)
			rt.gc();

		StringBuffer sb = new StringBuffer();
		boolean passed = true;
		synchronized (objs) {
			for (String desc : descs) {
				WeakRef wr = objs.get(desc);
				if (wr == null)
					continue;
				Object o = wr.get();
				if (o == null)
					continue;
				if (!passed)
					sb.append(",");
				passed = false;
				sb.append("'");
				sb.append(wr.getDesc());
				sb.append("'");
			}
			if (passed)
				return true;
		}

		String objStr = sb.toString();
		entries.add(objStr);

		if (objStr.length() > 40)
			objStr = objStr.substring(0, 40) + "...";
		System.err.println(">>>>> Objects not cleared: " + objStr);
		// System.err.println("Waiting 5 second for heap dump...");
		// try { Thread.sleep(5000); } catch (InterruptedException ie) { }
		return false;
	}

	public boolean checkObjectsList(List<String> descs) {
		String[] strs = new String[descs.size()];
		descs.toArray(strs);
		return checkObjects(strs);
	}

	public boolean checkAllObjects() {
		Runtime rt = Runtime.getRuntime();
		List<List<byte[]>> l = new ArrayList<>();
		int nBlock = (int) (rt.totalMemory() / (ALLOC_SZ * NUM_GC));
		try {
			while (true) {
				// System.err.println("FreeMemory: " + rt.freeMemory() +
				// " of " +rt.totalMemory());

				boolean passed = true;
				synchronized (objs) {
					for (WeakRef wr : objs.values()) {
						if ((wr != null) && (wr.get() != null)) {
							passed = false;
							break;
						}
					}
				}
				if (passed)
					return true;

				List<byte[]> l2 = new ArrayList<>();
				for (int i = 0; i < nBlock; i++) {
					l2.add(new byte[ALLOC_SZ]);
				}
				l.add(l2);
			}
		} catch (OutOfMemoryError oom) {
		} finally {
			l = null;
		}

		for (int i = 0; i < NUM_GC; i++)
			rt.gc();

		StringBuffer sb = new StringBuffer();
		synchronized (objs) {
			boolean passed = true;
			for (WeakRef wr : objs.values()) {
				Object o = wr.get();
				if (o == null)
					continue;
				if (!passed)
					sb.append(",");
				passed = false;
				sb.append("'");
				sb.append(wr.getDesc());
				sb.append("'");
			}
			if (passed)
				return true;
		}

		String objStr = sb.toString();

		entries.add(objStr);

		if (objStr.length() > 40)
			objStr = objStr.substring(0, 40) + "...";
		// Objects not cleared: objStr
		// System.err.println("Waiting for 5 seconds for heap dump...");
		// try { Thread.sleep(5000); } catch (InterruptedException ie) { }
		return false;
	}

	public String runTest(String testedUri) throws Exception {
		this.testedUri = resolveURI(testedUri);
		String error = doSomething();
		if (error != null)
			return error;

		checkAllObjects();

		if (entries.size() == 0) {
			return null;
		}
		return ERROR_OBJS_NOT_CLEARED + ": " +  entries.toArray(new String[entries.size()]);
	}

	private static String resolveURI(String uri) throws MalformedURLException {
		final String resName = TestLocations.getRootBuildURL() + uri;
		URL url = new URL(resName);
		return url.toExternalForm();
	}

	public abstract String doSomething() throws Exception;

	public class WeakRef extends CleanerThread.WeakReferenceCleared<Object> {
		String desc;

		public WeakRef(Object o) {
			super(o);
			this.desc = o.toString();
		}

		public WeakRef(Object o, String desc) {
			super(o);
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public void cleared() {
			synchronized (objs) {
				objs.remove(desc);
			}
		}

	}

}
