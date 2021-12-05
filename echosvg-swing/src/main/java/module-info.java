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

/**
 * SVG Swing components.
 */
module io.sf.carte.echosvg.swing {
	exports io.sf.carte.echosvg.swing.svg;
	exports io.sf.carte.echosvg.swing.gvt;
	exports io.sf.carte.echosvg.swing;

	requires transitive io.sf.carte.echosvg.bridge;
	requires io.sf.carte.echosvg.constants;
	requires io.sf.carte.echosvg.util.gui;
	requires io.sf.carte.echosvg.gvt;
	requires io.sf.carte.echosvg.dom.svg;
	requires io.sf.carte.echosvg.i18n;
	requires java.datatransfer;
}
