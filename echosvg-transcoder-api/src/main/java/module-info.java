/*
 * Copyright (c) 2020-2024 Carlos Amengual
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
 * Transcoder API.
 */
module io.sf.carte.echosvg.transcoder.api {

	exports io.sf.carte.echosvg.transcoder;
	exports io.sf.carte.echosvg.transcoder.keys;

	requires io.sf.carte.echosvg.anim;

	requires transitive java.desktop;

}
