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

/**
 * SVG test utility components.
 */
module io.sf.carte.echosvg.test.util {

	exports io.sf.carte.echosvg.test;
	exports io.sf.carte.echosvg.test.image;
	exports io.sf.carte.echosvg.test.xml;

	requires io.sf.carte.echosvg.awt;
	requires io.sf.carte.echosvg.i18n;
	requires transitive io.sf.carte.echosvg.util;

	requires io.sf.carte.util;
	requires io.sf.carte.xml.dtd;
	requires io.sf.jclf.text;

	requires transitive java.desktop;

}
