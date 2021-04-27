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
module io.sf.carte.echosvg.bridge {
	exports io.sf.carte.echosvg.bridge;
	exports io.sf.carte.echosvg.bridge.svg12;

	requires io.sf.carte.echosvg.awt;
	requires io.sf.carte.echosvg.external;
	requires io.sf.carte.echosvg.gvt;
	requires io.sf.carte.echosvg.script;
	requires io.sf.carte.echosvg.dom.svg;
	requires io.sf.carte.css4j;
	requires io.sf.carte.echosvg.anim;
	requires io.sf.carte.echosvg.parser;
	requires java.desktop;
	requires java.xml;
	requires jdk.xml.dom;
	requires org.mozilla.rhino;
	requires xalan;
	requires xml.apis.ext;
	requires xmlgraphics.commons;
}
