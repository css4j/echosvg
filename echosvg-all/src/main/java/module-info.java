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
 * Aggregate module.
 */
module io.sf.carte.echosvg {
	requires transitive io.sf.carte.echosvg.apps.slideshow;
	requires transitive io.sf.carte.echosvg.anim;
	requires transitive io.sf.carte.echosvg.awt;
	requires transitive io.sf.carte.echosvg.bridge;
	requires transitive io.sf.carte.echosvg.constants;
	requires transitive io.sf.carte.echosvg.css;
	requires transitive io.sf.carte.echosvg.dom;
	requires transitive io.sf.carte.echosvg.dom.svg;
	requires transitive io.sf.carte.echosvg.extension;
	requires transitive io.sf.carte.echosvg.external;
	requires transitive io.sf.carte.echosvg.gvt;
	requires transitive io.sf.carte.echosvg.i18n;
	requires transitive io.sf.carte.echosvg.parser;
	requires transitive io.sf.carte.echosvg.script;
	requires transitive io.sf.carte.echosvg.svggen;
	requires transitive io.sf.carte.echosvg.svgpp;
	requires transitive io.sf.carte.echosvg.swing;
	requires transitive io.sf.carte.echosvg.transcoder;
	requires transitive io.sf.carte.echosvg.transcoder.api;
	requires transitive io.sf.carte.echosvg.transcoder.svg;
	requires transitive io.sf.carte.echosvg.transcoder.svg2svg;
	requires transitive io.sf.carte.echosvg.transcoder.tosvg;
	requires transitive io.sf.carte.echosvg.ttf2svg;
	requires transitive io.sf.carte.echosvg.util;
	requires transitive io.sf.carte.echosvg.util.gui;
	requires transitive io.sf.carte.echosvg.xml;

	requires transitive org.w3c.dom.svg;
}
