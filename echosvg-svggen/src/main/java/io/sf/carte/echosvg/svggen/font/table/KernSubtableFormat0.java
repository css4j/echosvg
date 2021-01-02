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
package io.sf.carte.echosvg.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class KernSubtableFormat0 extends KernSubtable {

	private int nPairs;
	private int searchRange;
	private int entrySelector;
	private int rangeShift;
	private KerningPair[] kerningPairs;

	/** Creates new KernSubtableFormat0 */
	protected KernSubtableFormat0(RandomAccessFile raf) throws IOException {
		nPairs = raf.readUnsignedShort();
		searchRange = raf.readUnsignedShort();
		entrySelector = raf.readUnsignedShort();
		rangeShift = raf.readUnsignedShort();
		kerningPairs = new KerningPair[nPairs];
		for (int i = 0; i < nPairs; i++) {
			kerningPairs[i] = new KerningPair(raf);
		}
	}

	@Override
	public int getKerningPairCount() {
		return nPairs;
	}

	@Override
	public KerningPair getKerningPair(int i) {
		return kerningPairs[i];
	}

}
