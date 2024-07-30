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
 * SVG rasterizer library.
 */
module io.sf.carte.echosvg.svgrasterizer {
	exports io.sf.carte.echosvg.apps.rasterizer;

	requires transitive io.sf.carte.echosvg.transcoder;
	requires io.sf.carte.echosvg.i18n;

	requires transitive java.desktop;
	requires java.xml;
}
