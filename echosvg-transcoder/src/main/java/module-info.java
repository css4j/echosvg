/*
 * Copyright (c) 2020 Carlos Amengual
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
module io.sf.carte.echosvg.transcoder {
    exports io.sf.carte.echosvg.transcoder.wmf.tosvg;
    exports io.sf.carte.echosvg.transcoder.print;
    exports io.sf.carte.echosvg.transcoder.svg2svg;
    exports io.sf.carte.echosvg.transcoder.wmf;
    exports io.sf.carte.echosvg.transcoder.image;
    exports io.sf.carte.echosvg.transcoder.keys;
    exports io.sf.carte.echosvg.transcoder;
    exports io.sf.carte.echosvg.transcoder.image.resources;

    requires io.sf.carte.echosvg.awt;
    requires io.sf.carte.echosvg.bridge;
    requires io.sf.carte.echosvg.css;
    requires io.sf.carte.echosvg.gvt;
    requires io.sf.carte.echosvg.svggen;
    requires io.sf.carte.echosvg.xml;
    requires io.sf.carte.echosvg.anim;
    requires io.sf.carte.echosvg.dom;
    requires java.desktop;
    requires java.xml;
    requires jdk.xml.dom;
    requires xml.apis.ext;
}
