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
module io.sf.carte.echosvg.codec {
    exports io.sf.carte.echosvg.ext.awt.image.codec.png;
    exports io.sf.carte.echosvg.ext.awt.image.codec.util;
    exports io.sf.carte.echosvg.ext.awt.image.codec.imageio;

    requires io.sf.carte.echosvg.awt;
    requires io.sf.carte.echosvg.bridge;
    requires io.sf.carte.echosvg.i18n;
    requires io.sf.carte.echosvg.transcoder;
    requires io.sf.carte.echosvg.util;
    requires java.desktop;
    requires java.xml;
}
