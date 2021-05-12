/*
 * Copyright (c) 2020-2021 Carlos Amengual
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sf.carte.echosvg.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import io.sf.carte.echosvg.anim.dom.SAXSVGDocumentFactory;
import io.sf.jclf.text.TokenParser;

public class TestUtil {

	/**
	 * Compare the XML file at {@code refURL} with the data in {@code data}.
	 * <p>
	 * If the data is different, save {@code data} to {@code candidateFile} and
	 * return an error message.
	 * </p>
	 * 
	 * @param refURL        the URL pointing to the reference data.
	 * @param data          the data that has to be compared with the reference.
	 * @param candidateFile the file to save {@code data} to. If {@code null}, the
	 *                      data is not saved.
	 * @return a failure message, or {@code null} if the comparison matched.
	 * @throws IOException if a I/O error happened while comparing.
	 */
	public static String xmlDiff(URL refURL, byte[] data, File candidateFile) throws IOException {
		BufferedReader dataReader, refReader;
		try {
			InputStream is = refURL.openStream();
			Reader r = new InputStreamReader(is);
			refReader = new BufferedReader(r);
		} catch (IOException e) {
			save(data, candidateFile);
			throw e;
		}

		ByteArrayInputStream dataIS = new ByteArrayInputStream(data);
		Reader r = new InputStreamReader(dataIS);
		dataReader = new BufferedReader(r);

		try {
			String refStr = "";
			String dataStr = "";
			int line = 0;
			int cn = 0;
			while (refStr != null && dataStr != null) {

				if (!refStr.equals(dataStr)) {
					if ((cn = lineDiff(refStr, dataStr)) != -1) {
						break;
					}
				}

				refStr = refReader.readLine();
				dataStr = dataReader.readLine();
				line++;
			}

			refReader.close();

			if (refStr == null && dataStr == null) {
				// Test passed
				return null;
			}

			// Check for false positives caused by element-content whitespace
			if (compareDOM(refURL, data)) {
				return null;
			}

			save(data, candidateFile);

			String expectedChar = "<eol>";
			if ((cn >= 0) && (refStr != null) && (refStr.length() > cn))
				expectedChar = refStr.substring(cn, cn + 1);

			String foundChar = "<null>";
			if ((cn >= 0) && (dataStr != null) && (dataStr.length() > cn))
				foundChar = dataStr.substring(cn, cn + 1);

			if (expectedChar.equals(" "))
				expectedChar = "' '";
			if (foundChar.equals(" "))
				foundChar = "' '";

			return "Expected " + expectedChar + ", found " + foundChar + " at line/column " + line + '/' + (cn + 1)
					+ ":\n" + refStr + '\n' + dataStr;
		} catch (Exception e) {
			save(data, candidateFile);
			return "Error while comparing images: " + e.getMessage();
		}
	}

	/**
	 * Find the character index at which two lines differ.
	 * 
	 * @param refLine the reference line.
	 * @param checkLine the line to be checked.
	 * @return the character index, or -1 if the lines are equal.
	 */
	public static int lineDiff(String refLine, String checkLine) {
		ArrayList<String> refTk = new ArrayList<>();
		TokenParser st = new TokenParser(refLine, " ;{}", "\"", true);
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			refTk.add(s);
		}

		ArrayList<String> checkTk = new ArrayList<>(refTk.size() + 2);
		st = new TokenParser(checkLine, " ;{}", "\"", true);
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			checkTk.add(s);
		}

		if (refTk.size() == checkTk.size()) {
			for (String s : refTk) {
				if (!checkTk.contains(s)) {
					return computeColumnNumber(refLine, checkLine, refLine.indexOf(s));
				}
			}
			return -1;
		}

		return computeColumnNumber(refLine, checkLine, 0);
	}

	private static int computeColumnNumber(String aStr, String bStr, int offset) {
		if (aStr == null || bStr == null) {
			return -1;
		}

		int n = aStr.length();
		int i = offset;
		for (; i < n; i++) {
			char a = aStr.charAt(i);
			if (i < bStr.length()) {
				char b = bStr.charAt(i);
				if (a != b) {
					break;
				}
			} else {
				break;
			}
		}

		return i;
	}

	private static boolean compareDOM(URL refURL, byte[] data) throws IOException {
		InputStream newStream = new ByteArrayInputStream(data);
		InputStream refStream = refURL.openStream();
		String documentURI = refURL.toExternalForm();

		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(null);
		SVGDocument refDoc = factory.createDocument(documentURI, refStream);
		refStream.close();
		SVGDocument newDoc = factory.createDocument(documentURI, newStream);

		return isEquivalentNode(refDoc, newDoc);
	}

	private static boolean isEquivalentNode(Node node, Node other) {
		if (other == null) {
			return false;
		}

		int nt = other.getNodeType();
		if (nt != node.getNodeType() || !Objects.equals(node.getNodeName(), other.getNodeName())
				|| !Objects.equals(node.getLocalName(), other.getLocalName())
				|| !Objects.equals(node.getPrefix(), other.getPrefix())
				|| !Objects.equals(node.getNodeValue(), other.getNodeValue())
				|| !compareNamedNodeMaps(node.getAttributes(), other.getAttributes())) {
			return false;
		}

		if (nt == Node.DOCUMENT_TYPE_NODE) {
			DocumentType dt1 = (DocumentType) node;
			DocumentType dt2 = (DocumentType) other;
			if (!Objects.equals(dt1.getPublicId(), dt2.getPublicId())
					|| !Objects.equals(dt1.getSystemId(), dt2.getSystemId())
					|| !Objects.equals(dt1.getInternalSubset(), dt2.getInternalSubset())
					|| !compareNamedNodeMaps(dt1.getEntities(), dt2.getEntities())
					|| !compareNamedNodeMaps(dt1.getNotations(), dt2.getNotations())) {
				return false;
			}
		}

		Node n = node.getFirstChild();
		Node m = other.getFirstChild();
		if (n != null && m != null) {
			if (!isEquivalentNode(n, m)) {
				return false;
			}
		}

		return true;
	}

	private static boolean compareNamedNodeMaps(NamedNodeMap attributes, NamedNodeMap attributes2) {
		if (attributes == null) {
			return attributes2 == null;
		} else if (attributes2 == null) {
			return false;
		}

		int nl = attributes.getLength();
		int nl2 = attributes2.getLength();
		if (nl != nl2) {
			return false;
		}

		for (int i = 0; i < nl; i++) {
			Node attr = attributes.item(i);
			Node attr2 = attributes2.getNamedItemNS(attr.getNamespaceURI(), attr.getLocalName());
			if (attr2 == null) {
				attr2 = attributes2.getNamedItem(attr.getNodeName());
			}
			if (attr2 == null) {
				return false;
			}
			if (!isEquivalentNode(attr, attr2)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Save {@code data} to the candidateFile.
	 * 
	 * @param data          the data to save.
	 * @param candidateFile the file where data has to be stored.
	 * @throws IOException
	 */
	private static void save(byte[] data, File candidateFile) throws IOException {
		if (candidateFile == null) {
			return;
		}

		File parentDir = candidateFile.getParentFile();
		if (!parentDir.exists()) {
			if (!parentDir.mkdir()) {
				return;
			}
		}

		FileOutputStream os = new FileOutputStream(candidateFile);
		try {
			os.write(data);
		} finally {
			try {
				os.close();
			} catch (IOException e) {}
		}
	}

}
