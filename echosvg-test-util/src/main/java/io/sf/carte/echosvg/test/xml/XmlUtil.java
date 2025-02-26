/*
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
 *
 */
package io.sf.carte.echosvg.test.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import io.sf.carte.doc.xml.dtd.DefaultEntityResolver;
import io.sf.carte.util.agent.AgentUtil;
import io.sf.jclf.text.TokenParser;

/**
 * XML-related comparison utilities.
 */
public class XmlUtil {

	private XmlUtil() {
	}

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
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}

		DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}

		builder.setEntityResolver(new DefaultEntityResolver());

		return xmlDiff(refURL, data, candidateFile, builder);
	}

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
	 * @param builder       the document builder to use.
	 * @return a failure message, or {@code null} if the comparison matched.
	 * @throws IOException if a I/O error happened while comparing.
	 */
	public static String xmlDiff(URL refURL, byte[] data, File candidateFile, DocumentBuilder builder)
			throws IOException {
		BufferedReader refReader;
		try {
			refReader = urlToReader(refURL);
		} catch (FileNotFoundException e) {
			save(data, candidateFile);
			return e.getMessage();
		} catch (IOException e) {
			save(data, candidateFile);
			throw e;
		}

		ByteArrayInputStream dataIS = new ByteArrayInputStream(data);
		Reader r = new InputStreamReader(dataIS, StandardCharsets.UTF_8);
		BufferedReader dataReader = new BufferedReader(r);

		String refStr = "";
		String dataStr = "";
		int line = 0;
		int cn = 0;
		try {
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
		} catch (IOException e) {
			save(data, candidateFile);
			return "Error while comparing images: " + e.getMessage();
		} finally {
			try {
				refReader.close();
			} catch (IOException e) {
			}
		}

		try {
			if (refStr == null && dataStr == null) {
				// Test passed
				return null;
			}

			// Check for false positives caused by element-content whitespace
			if (compareDOM(refURL, data, builder)) {
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

	private static BufferedReader urlToReader(URL refURL) throws IOException {
		URLConnection urlc = refURL.openConnection();
		InputStream is = urlc.getInputStream();
		String conType = urlc.getContentType();
		String contentEncoding = urlc.getContentEncoding();
		Reader r;
		try {
			r = AgentUtil.inputStreamToReader(is, conType, contentEncoding, StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			try {
				is.close();
			} catch (IOException e1) {
			}
			throw e;
		}
		return new BufferedReader(r);
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

	private static boolean compareDOM(URL refURL, byte[] data, DocumentBuilder builder) throws IOException {
		String uri = refURL.toExternalForm();

		BufferedReader refReader = urlToReader(refURL);
		InputSource source = new InputSource(refReader);
		source.setSystemId(uri);

		Document refDoc;
		try {
			refDoc = builder.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}

		InputStream newStream = new ByteArrayInputStream(data);
		InputStreamReader candReader = new InputStreamReader(newStream, StandardCharsets.UTF_8);
		source = new InputSource(candReader);
		source.setSystemId(uri);

		Document newDoc;
		try {
			newDoc = builder.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}

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
		if (n != null) {
			if (!isEquivalentNode(n, m)) {
				return false;
			}
		} else if (m != null) {
			return false;
		}

		n = node.getNextSibling();
		m = other.getNextSibling();
		if (n != null) {
			if (!isEquivalentNode(n, m)) {
				return false;
			}
		} else if (m != null) {
			return false;
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
			String localName = attr.getLocalName();
			Node attr2;
			if (localName != null) {
				attr2 = attributes2.getNamedItemNS(attr.getNamespaceURI(), localName);
			} else {
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

		try (FileOutputStream os = new FileOutputStream(candidateFile)) {
			os.write(data);
		}
	}

}
