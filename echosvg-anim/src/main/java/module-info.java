/*
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
 */

/**
 * Animation engine.
 */
module io.sf.carte.echosvg.anim {
	exports io.sf.carte.echosvg.anim.values;
	exports io.sf.carte.echosvg.anim.dom;
	exports io.sf.carte.echosvg.anim;
	exports io.sf.carte.echosvg.anim.timing;

	requires transitive io.sf.carte.echosvg.awt;
	requires io.sf.carte.echosvg.external;
	requires io.sf.carte.echosvg.i18n;
	requires io.sf.carte.echosvg.constants;
	requires transitive io.sf.carte.echosvg.dom.svg;
	requires io.sf.carte.css4j;
	requires java.xml;
	requires jdk.xml.dom;
}
