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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * This class contains utility methods to manipulate Java classes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ClassFileUtilities {

	// Constant pool info tags
	public static final byte CONSTANT_UTF8_INFO = 1;
	public static final byte CONSTANT_INTEGER_INFO = 3;
	public static final byte CONSTANT_FLOAT_INFO = 4;
	public static final byte CONSTANT_LONG_INFO = 5;
	public static final byte CONSTANT_DOUBLE_INFO = 6;
	public static final byte CONSTANT_CLASS_INFO = 7;
	public static final byte CONSTANT_STRING_INFO = 8;
	public static final byte CONSTANT_FIELDREF_INFO = 9;
	public static final byte CONSTANT_METHODREF_INFO = 10;
	public static final byte CONSTANT_INTERFACEMETHODREF_INFO = 11;
	public static final byte CONSTANT_NAMEANDTYPE_INFO = 12;

	/**
	 * This class does not need to be instantiated.
	 */
	protected ClassFileUtilities() {
	}

	/**
	 * Program that computes the dependencies between the EchoSVG jars.
	 * <p>
	 * Run this from the main EchoSVG distribution directory, after building the
	 * jars. For every jar file in the batik-xxx/ build directory, it will determine
	 * which other jar files it directly depends on. The output is lines of the
	 * form:
	 * </p>
	 * 
	 * <pre>
	 *   <i>number</i>,<i>from</i>,<i>to</i>
	 * </pre>
	 * <p>
	 * where mean that the <i>from</i> jar has <i>number</i> class files that depend
	 * on class files in the <i>to</i> jar.
	 * </p>
	 */
	public static void main(String[] args) {
		boolean showFiles = false;
		if (args.length == 1 && args[0].equals("-f")) {
			showFiles = true;
		} else if (args.length != 0) {
			System.err.println("usage: io.sf.carte.echosvg.util.ClassFileUtilities [-f]");
			System.err.println();
			System.err.println("  -f    list files that cause each jar file dependency");
			System.exit(1);
		}

		File cwd = new File(".");
		File buildDir = null;
		String[] cwdFiles = cwd.list();
		if (cwdFiles != null) {
			for (String cwdFile : cwdFiles) {
				if (cwdFile.startsWith("echosvg-")) {
					buildDir = new File(cwdFile);
					if (!buildDir.isDirectory()) {
						buildDir = null;
					} else {
						break;
					}
				}
			}
		}
		if (buildDir == null || !buildDir.isDirectory()) {
			System.out.println("Directory 'echosvg-xxx' not found in current directory!");
			return;
		}

		try {
			Map<String, ClassFile> cs = new HashMap<>();
			Map<String, Jar> js = new HashMap<>();
			collectJars(buildDir, js, cs);

			Set<Object> classpath = new HashSet<>();
			Iterator<Jar> i = js.values().iterator();
			while (i.hasNext()) {
				classpath.add(i.next().jarFile);
			}

			Iterator<ClassFile> cfi = cs.values().iterator();
			while (cfi.hasNext()) {
				ClassFile fromFile = cfi.next();
				// System.out.println(fromFile.name);
				Set<String> result = getClassDependencies(fromFile.getInputStream(), classpath, false);
				for (String aResult : result) {
					ClassFile toFile = cs.get(aResult);
					if (fromFile != toFile && toFile != null) {
						fromFile.deps.add(toFile);
					}
				}
			}

			cfi = cs.values().iterator();
			while (cfi.hasNext()) {
				ClassFile fromFile = cfi.next();
				for (ClassFile toFile : fromFile.deps) {
					Jar fromJar = fromFile.jar;
					Jar toJar = toFile.jar;
					if (fromFile.name.equals(toFile.name) || toJar == fromJar || fromJar.files.contains(toFile)) {
						continue;
					}
					Integer n = fromJar.deps.get(toJar);
					if (n == null) {
						fromJar.deps.put(toJar, 1);
					} else {
						fromJar.deps.put(toJar, n + 1);
					}
				}
			}

			List<Triple> triples = new ArrayList<>(10);
			i = js.values().iterator();
			while (i.hasNext()) {
				Jar fromJar = i.next();
				for (Jar toJar : fromJar.deps.keySet()) {
					Triple t = new Triple();
					t.from = fromJar;
					t.to = toJar;
					t.count = fromJar.deps.get(toJar);
					triples.add(t);
				}
			}
			Collections.sort(triples);

			Iterator<Triple> ti = triples.iterator();
			while (ti.hasNext()) {
				Triple t = ti.next();
				System.out.println(t.count + "," + t.from.name + "," + t.to.name);
				if (showFiles) {
					for (ClassFile fromFile : t.from.files) {
						for (ClassFile toFile : fromFile.deps) {
							if (toFile.jar == t.to && !t.from.files.contains(toFile)) {
								System.out.println("\t" + fromFile.name + " --> " + toFile.name);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static class ClassFile {
		public String name;
		public List<ClassFile> deps = new ArrayList<>(10);
		public Jar jar;

		public InputStream getInputStream() throws IOException {
			return jar.jarFile.getInputStream(jar.jarFile.getEntry(name));
		}
	}

	protected static class Jar {
		public String name;
		public File file;
		public JarFile jarFile;
		public Map<Jar, Integer> deps = new HashMap<>();
		public Set<ClassFile> files = new HashSet<>();
	}

	protected static class Triple implements Comparable<Triple> {
		public Jar from;
		public Jar to;
		public int count;

		@Override
		public int compareTo(Triple o) {
			return o.count - count;
		}
	}

	private static void collectJars(File dir, Map<String, Jar> jars, Map<String, ClassFile> classFiles)
			throws IOException {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				String n = file.getName();
				if (n.endsWith(".jar") && file.isFile()) {
					Jar j = new Jar();
					j.name = file.getPath();
					j.file = file;
					j.jarFile = new JarFile(file);
					jars.put(j.name, j);

					Enumeration<JarEntry> entries = j.jarFile.entries();
					while (entries.hasMoreElements()) {
						ZipEntry ze = entries.nextElement();
						String name = ze.getName();
						if (name.endsWith(".class")) {
							ClassFile cf = new ClassFile();
							cf.name = name;
							cf.jar = j;
							classFiles.put(j.name + '!' + cf.name, cf);
							j.files.add(cf);
						}
					}
				} else if (file.isDirectory()) {
					collectJars(file, jars, classFiles);
				}
			}
		}
	}

	/**
	 * Returns the dependencies of the given class.
	 * 
	 * @param path      The root class path.
	 * @param classpath The set of directories (Strings) to scan.
	 * @param rec       Whether to follow dependencies recursively.
	 * @return a list of paths representing the used classes.
	 */
	public static Set<String> getClassDependencies(String path, Set<Object> classpath, boolean rec) throws IOException {

		return getClassDependencies(new FileInputStream(path), classpath, rec);
	}

	public static Set<String> getClassDependencies(InputStream is, Set<Object> classpath, boolean rec)
			throws IOException {

		Set<String> result = new HashSet<>();
		Set<String> done = new HashSet<>();

		computeClassDependencies(is, classpath, done, result, rec);

		return result;
	}

	private static void computeClassDependencies(InputStream is, Set<Object> classpath, Set<String> done,
			Set<String> result, boolean rec) throws IOException {

		for (String s : getClassDependencies(is)) {
			if (!done.contains(s)) {
				done.add(s);

				for (Object aClasspath : classpath) {
					InputStream depis = null;
					String path = null;
					JarFile jarFile = null;
					Object cpEntry = aClasspath;
					if (cpEntry instanceof JarFile) {
						jarFile = (JarFile) cpEntry;
						String classFileName = s + ".class";
						ZipEntry ze = jarFile.getEntry(classFileName);
						if (ze != null) {
							path = jarFile.getName() + '!' + classFileName;
							depis = jarFile.getInputStream(ze);
						}
					} else {
						path = ((String) cpEntry) + '/' + s + ".class";
						File f = new File(path);
						if (f.isFile()) {
							depis = new FileInputStream(f);
						}
					}

					if (depis != null) {
						result.add(path);

						if (rec) {
							computeClassDependencies(depis, classpath, done, result, rec);
						}
						depis.close();
					}
					if (jarFile != null) {
						jarFile.close();
					}
				}
			}
		}
	}

	/**
	 * Returns the dependencies of the given class.
	 * 
	 * @return a list of strings representing the used classes.
	 */
	public static Set<String> getClassDependencies(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);

		if (dis.readInt() != 0xcafebabe) {
			throw new IOException("Invalid classfile");
		}

		dis.readInt();

		int len = dis.readShort();
		String[] strs = new String[len];
		Set<Integer> classes = new HashSet<>();
		Set<Integer> desc = new HashSet<>();

		for (int i = 1; i < len; i++) {
			int constCode = dis.readByte() & 0xff;
			switch (constCode) {
			case CONSTANT_LONG_INFO:
			case CONSTANT_DOUBLE_INFO:
				dis.readLong();
				i++;
				break;

			case CONSTANT_FIELDREF_INFO:
			case CONSTANT_METHODREF_INFO:
			case CONSTANT_INTERFACEMETHODREF_INFO:
			case CONSTANT_INTEGER_INFO:
			case CONSTANT_FLOAT_INFO:
				dis.readInt();
				break;

			case CONSTANT_CLASS_INFO:
				classes.add(dis.readShort() & 0xffff);
				break;

			case CONSTANT_STRING_INFO:
				dis.readShort();
				break;

			case CONSTANT_NAMEANDTYPE_INFO:
				dis.readShort();
				desc.add(dis.readShort() & 0xffff);
				break;

			case CONSTANT_UTF8_INFO:
				strs[i] = dis.readUTF();
				break;

			default:
				throw new RuntimeException("unexpected data in constant-pool:" + constCode);
			}
		}

		Set<String> result = new HashSet<>();

		Iterator<Integer> it = classes.iterator();
		while (it.hasNext()) {
			result.add(strs[it.next()]);
		}

		it = desc.iterator();
		while (it.hasNext()) {
			result.addAll(getDescriptorClasses(strs[it.next()]));
		}

		return result;
	}

	/**
	 * Returns the classes contained in a field or method desciptor.
	 */
	protected static Set<String> getDescriptorClasses(String desc) {
		Set<String> result = new HashSet<>();
		int i = 0;
		char c = desc.charAt(i);
		switch (c) {
		case '(':
			loop: for (;;) {
				c = desc.charAt(++i);
				switch (c) {
				case '[':
					do {
						c = desc.charAt(++i);
					} while (c == '[');
					if (c != 'L') {
						break;
					}

				case 'L':
					c = desc.charAt(++i);
					StringBuilder sb = new StringBuilder();
					while (c != ';') {
						sb.append(c);
						c = desc.charAt(++i);
					}
					result.add(sb.toString());
					break;

				default:
					break;

				case ')':
					break loop;
				}
			}
			c = desc.charAt(++i);
			switch (c) {
			case '[':
				do {
					c = desc.charAt(++i);
				} while (c == '[');
				if (c != 'L') {
					break;
				}

			case 'L':
				c = desc.charAt(++i);
				StringBuilder sb = new StringBuilder();
				while (c != ';') {
					sb.append(c);
					c = desc.charAt(++i);
				}
				result.add(sb.toString());
				break;

			default:
			case 'V':
			}
			break;

		case '[':
			do {
				c = desc.charAt(++i);
			} while (c == '[');
			if (c != 'L') {
				break;
			}

		case 'L':
			c = desc.charAt(++i);
			StringBuilder sb = new StringBuilder();
			while (c != ';') {
				sb.append(c);
				c = desc.charAt(++i);
			}
			result.add(sb.toString());
			break;

		default:
		}

		return result;
	}
}
