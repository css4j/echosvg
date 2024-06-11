/*
 * Copyright (c) 2020-2023 Carlos Amengual
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
 * Bridge.
 * 
 * @provides io.sf.carte.echosvg.script.InterpreterFactory
 * @uses io.sf.carte.echosvg.bridge.BridgeExtension
 */
module io.sf.carte.echosvg.bridge {
	exports io.sf.carte.echosvg.bridge;
	exports io.sf.carte.echosvg.bridge.svg12;

	requires transitive io.sf.carte.echosvg.external;
	requires transitive io.sf.carte.echosvg.gvt;
	requires transitive io.sf.carte.echosvg.script;
	requires transitive io.sf.carte.echosvg.anim;
	requires io.sf.carte.echosvg.parser;
	requires io.sf.carte.echosvg.xml;
	requires io.sf.carte.echosvg.constants;
	requires io.sf.carte.echosvg.i18n;
	requires org.apache.xmlgraphics.commons;

	provides io.sf.carte.echosvg.script.InterpreterFactory
	with io.sf.carte.echosvg.bridge.RhinoInterpreterFactory;

	uses io.sf.carte.echosvg.bridge.BridgeExtension;
}
