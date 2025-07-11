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

package io.sf.carte.echosvg.extension.svg;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.sf.carte.echosvg.bridge.StrokingTextPainter;
import io.sf.carte.echosvg.bridge.TextNode;
import io.sf.carte.echosvg.bridge.TextSpanLayout;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class FlowExtTextPainter extends StrokingTextPainter {

	/**
	 * A unique instance of this class.
	 */
	protected static FlowExtTextPainter singleton = new FlowExtTextPainter();

	/**
	 * Returns a unique instance of this class.
	 */
	public static FlowExtTextPainter getInstance() {
		return singleton;
	}

	@Override
	public List<TextRun> getTextRuns(TextNode node, AttributedCharacterIterator aci) {
		List<TextRun> textRuns = node.getTextRuns();
		if (textRuns != null) {
			return textRuns;
		}

		AttributedCharacterIterator[] chunkACIs = getTextChunkACIs(aci);
		textRuns = computeTextRuns(node, aci, chunkACIs);

		aci.first();
		@SuppressWarnings("unchecked")
		List<RegionInfo> rgns = (List<RegionInfo>) aci.getAttribute(FLOW_REGIONS);

		if (rgns != null) {
			Iterator<TextRun> i = textRuns.iterator();
			List<List<TextSpanLayout>> chunkLayouts = new ArrayList<>();
			TextRun tr = i.next();
			List<TextSpanLayout> layouts = new ArrayList<>();
			chunkLayouts.add(layouts);
			layouts.add(tr.getLayout());
			while (i.hasNext()) {
				tr = i.next();
				if (tr.isFirstRunInChunk()) {
					layouts = new ArrayList<>();
					chunkLayouts.add(layouts);
				}
				layouts.add(tr.getLayout());
			}

			FlowExtGlyphLayout.textWrapTextChunk(chunkACIs, chunkLayouts, rgns);
		}

		node.setTextRuns(textRuns);
		return textRuns;
	}

}
