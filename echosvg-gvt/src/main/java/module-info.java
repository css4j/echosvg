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
 * Graphics Vector Tree (GVT).
 */
module io.sf.carte.echosvg.gvt {

	exports io.sf.carte.echosvg.gvt.font;
	exports io.sf.carte.echosvg.gvt.flow;
	exports io.sf.carte.echosvg.gvt.event;
	exports io.sf.carte.echosvg.gvt;
	exports io.sf.carte.echosvg.gvt.renderer;
	exports io.sf.carte.echosvg.gvt.text;
	exports io.sf.carte.echosvg.gvt.filter;

	requires transitive io.sf.carte.echosvg.awt;
	requires transitive io.sf.carte.echosvg.util;

}
