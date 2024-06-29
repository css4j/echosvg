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
package io.sf.carte.echosvg.swing.svg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import io.sf.carte.echosvg.swing.gvt.Overlay;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SVGUpdateOverlay implements Overlay {

	List<Rectangle> rects = new LinkedList<>();
	int size, updateCount;
	int[] counts;

	public SVGUpdateOverlay(int size, int numUpdates) {
		this.size = size;
		counts = new int[numUpdates];
	}

	public void addRect(Rectangle r) {
		rects.add(r);
		if (rects.size() > size)
			rects.remove(0);
		updateCount++;
	}

	public void endUpdate() {
		int i = 0;
		for (; i < counts.length - 1; i++) {
			counts[i] = counts[i + 1];
		}
		counts[i] = updateCount;
		updateCount = 0;

		int num = rects.size();
		for (i = counts.length - 1; i >= 0; i--) {
			if (counts[i] > num) {
				counts[i] = num;
			}
			num -= counts[i];
		}
		counts[0] += num;
	}

	@Override
	public void paint(Graphics g) {
		int count = 0;
		int idx = 0;
		int group = 0;
		while ((group < counts.length - 1) && (idx == counts[group]))
			group++;
		int cmax = counts.length - 1;
		for (Rectangle r : rects) {
			Color c;
			c = new Color(1.0f, (cmax - group) / (float) cmax, 0, (count + 1.0f) / rects.size());
			g.setColor(c);
			g.drawRect(r.x, r.y, r.width, r.height);
			count++;
			idx++;
			while ((group < counts.length - 1) && (idx == counts[group])) {
				group++;
				idx = 0;
			}
		}
	}

}
