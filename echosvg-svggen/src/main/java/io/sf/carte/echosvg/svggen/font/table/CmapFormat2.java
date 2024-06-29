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
 * @author For later modifications, see Git history.
 * @version $Id$
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public class CmapFormat2 extends CmapFormat {

	private short[] subHeaderKeys = new short[256];
	private int[] subHeaders1;
	private int[] subHeaders2;
	private short[] glyphIndexArray;

	protected CmapFormat2(RandomAccessFile raf) throws IOException {
		super(raf);
		format = 2;
	}

	@Override
	public int getFirst() {
		return 0;
	}

	@Override
	public int getLast() {
		return 0;
	}

	@Override
	public int mapCharCode(int charCode) {
		return 0;
	}

}
