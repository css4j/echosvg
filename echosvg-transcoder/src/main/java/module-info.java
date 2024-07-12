/*
 * Copyright (c) 2020-2022 Carlos Amengual
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
 * SVG transcoder.
 */
module io.sf.carte.echosvg.transcoder {
	exports io.sf.carte.echosvg.transcoder;
	exports io.sf.carte.echosvg.transcoder.image;
	exports io.sf.carte.echosvg.transcoder.image.resources;
	exports io.sf.carte.echosvg.transcoder.keys;
	exports io.sf.carte.echosvg.transcoder.print;
	exports io.sf.carte.echosvg.transcoder.svg2svg;
	exports io.sf.carte.echosvg.transcoder.util;
	exports io.sf.carte.echosvg.transcoder.wmf;
	exports io.sf.carte.echosvg.transcoder.wmf.tosvg;

	requires transitive io.sf.carte.echosvg.bridge;
	requires transitive io.sf.carte.echosvg.svggen;
	requires io.sf.carte.echosvg.codec;
	requires io.sf.carte.echosvg.i18n;
	requires io.sf.carte.echosvg.xml;

	requires io.sf.carte.xml.dtd;
	requires io.sf.carte.css4j.awt;
}
