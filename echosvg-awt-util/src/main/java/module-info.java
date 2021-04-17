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
module io.sf.carte.echosvg.awt {
    exports io.sf.carte.echosvg.ext.awt.image.renderable;
    exports io.sf.carte.echosvg.ext.awt;
    exports io.sf.carte.echosvg.ext.swing;
    exports io.sf.carte.echosvg.ext.awt.image.spi;
    exports io.sf.carte.echosvg.ext.awt.image.rendered;
    exports io.sf.carte.echosvg.ext.awt.image;
    exports io.sf.carte.echosvg.ext.awt.geom;
    exports io.sf.carte.echosvg.ext.awt.color;
    exports io.sf.carte.echosvg.ext.awt.font;
    exports io.sf.carte.echosvg.ext.awt.g2d;

    requires io.sf.carte.echosvg.util;
    requires transitive java.desktop;
    requires xmlgraphics.commons;
}
