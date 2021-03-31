/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.svggen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This test validates that spaces are preserved correctly in drawString calls.
 * Validates bug #2657 fix.
 *
 * @author <a href="mailto:vincent.hardy@sun.com">Vincent Hardy</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class TextSpacePreserve implements Painter {
	@Override
	public void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setPaint(Color.black); // new Color(102, 102, 144));

		int legendX = 10, legendY = 12;
		g.translate(0, 30);

		// Print text with spaces.
		g.drawString("     space before.", legendX, legendY);
		g.drawString("Multiple spaces between A and B: A    B", legendX, legendY + 20);
		g.drawString("This is a first line\n     and this is a second line starting with spaces", legendX,
				legendY + 40);
		g.drawString("Should have no trailing spaces", legendX, legendY + 60);
	}
}
