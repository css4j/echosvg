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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

/**
 * This test validates drawImage conversions.
 *
 * @author <a href="mailto:vhardy@eng.sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class Bug4389 implements Painter {
	@Override
	public void paint(Graphics2D g) {
		ImageIcon image = new ImageIcon(
				ClassLoader.getSystemResource("io/sf/carte/echosvg/svggen/resources/vangogh.png"));
		g.translate(40, 40);
		g.drawImage(image.getImage(), new AffineTransform(), null);
	}
}
