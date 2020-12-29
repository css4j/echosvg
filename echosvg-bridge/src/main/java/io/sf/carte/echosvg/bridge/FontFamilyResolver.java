/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

/* $Id$ */

package io.sf.carte.echosvg.bridge;

import java.io.InputStream;

import io.sf.carte.echosvg.gvt.font.GVTFontFamily;

public interface FontFamilyResolver {

    /**
     * Resolves a font family name into a GVTFontFamily. If the font
     * family cannot be resolved then null will be returned.
     *
     * @param familyName The Font Family name to resolve
     *
     * @return A resolved GVTFontFamily or null if the font family could not
     * be resolved.
     */
    GVTFontFamily resolve(String familyName);

    GVTFontFamily resolve(String familyName, FontFace fontFace);

    GVTFontFamily loadFont(InputStream in, FontFace fontFace) throws Exception;

    GVTFontFamily getDefault();

    GVTFontFamily getFamilyThatCanDisplay(char c);

}
