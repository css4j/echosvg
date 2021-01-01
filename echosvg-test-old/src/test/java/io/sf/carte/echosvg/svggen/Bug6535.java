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
package io.sf.carte.echosvg.svggen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This test validates outputing font-size as a float
 *
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Bug6535 implements Painter {
    @Override
    public void paint(Graphics2D g){
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                           RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setPaint(Color.black);

        g.scale(10,10);

        // Set default font
        Font font=new Font("Arial", Font.PLAIN, 1);
        Font font2=font.deriveFont(1.5f);

        g.setFont(font);
        g.drawString("Hello, size 10", 4, 4);

        g.setFont(font2);
        g.drawString("Hello, size 15", 4, 8);

        g.scale(.1, .1);

        font=new Font("Arial", Font.PLAIN, 10);
        font2=font.deriveFont(15f);

        g.setFont(font);
        g.drawString("Hello, size 10", 160, 40);

        g.setFont(font2);
        g.drawString("Hello, size 15", 160, 80);

    }
}
