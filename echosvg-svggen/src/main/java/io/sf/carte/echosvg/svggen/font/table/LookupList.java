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
public class LookupList {

	private int lookupCount;
	private int[] lookupOffsets;
	private Lookup[] lookups;

	/** Creates new LookupList */
	public LookupList(RandomAccessFile raf, int offset, LookupSubtableFactory factory) throws IOException {
		raf.seek(offset);
		lookupCount = raf.readUnsignedShort();
		lookupOffsets = new int[lookupCount];
		lookups = new Lookup[lookupCount];
		for (int i = 0; i < lookupCount; i++) {
			lookupOffsets[i] = raf.readUnsignedShort();
		}
		for (int i = 0; i < lookupCount; i++) {
			lookups[i] = new Lookup(factory, raf, offset + lookupOffsets[i]);
		}
	}

	public Lookup getLookup(Feature feature, int index) {
		if (feature.getLookupCount() > index) {
			int i = feature.getLookupListIndex(index);
			return lookups[i];
		}
		return null;
	}

}
