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
package io.sf.carte.echosvg.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class SingleSubstFormat2 extends SingleSubst {

	private int coverageOffset;
	private int glyphCount;
	private int[] substitutes;
	private Coverage coverage;

	/** Creates new SingleSubstFormat2 */
	protected SingleSubstFormat2(RandomAccessFile raf, int offset) throws IOException {
		coverageOffset = raf.readUnsignedShort();
		glyphCount = raf.readUnsignedShort();
		substitutes = new int[glyphCount];
		for (int i = 0; i < glyphCount; i++) {
			substitutes[i] = raf.readUnsignedShort();
		}
		raf.seek(offset + coverageOffset);
		coverage = Coverage.read(raf);
	}

	@Override
	public int getFormat() {
		return 2;
	}

	@Override
	public int substitute(int glyphId) {
		int i = coverage.findGlyph(glyphId);
		if (i > -1) {
			return substitutes[i];
		}
		return glyphId;
	}

}
