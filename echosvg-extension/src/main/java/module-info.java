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
 * Extension support.
 * 
 * @provides io.sf.carte.echosvg.bridge.BridgeExtension
 * @provides io.sf.carte.echosvg.dom.DomExtension
 */
module io.sf.carte.echosvg.extension {
	exports io.sf.carte.echosvg.extension;
	exports io.sf.carte.echosvg.extension.svg;

	requires transitive io.sf.carte.echosvg.bridge;
	requires io.sf.carte.echosvg.constants;

	requires transitive org.w3c.dom.svg;

	requires jdk.xml.dom;

	provides io.sf.carte.echosvg.bridge.BridgeExtension
	with io.sf.carte.echosvg.extension.svg.EchoSVGBridgeExtension;

	provides io.sf.carte.echosvg.dom.DomExtension
	with io.sf.carte.echosvg.extension.svg.EchoSVGDomExtension;

}
