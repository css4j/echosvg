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

import java.io.ByteArrayInputStream;

/**
 * @author For later modifications, see Git history.
 * @version $Id$
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public abstract class GlyfDescript extends Program implements GlyphDescription {

	// flags
	public static final byte onCurve = 0x01;
	public static final byte xShortVector = 0x02;
	public static final byte yShortVector = 0x04;
	public static final byte repeat = 0x08;
	public static final byte xDual = 0x10;
	public static final byte yDual = 0x20;

	protected GlyfTable parentTable;
	private int numberOfContours;
	private short xMin;
	private short yMin;
	private short xMax;
	private short yMax;

	protected GlyfDescript(GlyfTable parentTable, short numberOfContours, ByteArrayInputStream bais) {
		this.parentTable = parentTable;
		this.numberOfContours = numberOfContours;
		xMin = (short) (bais.read() << 8 | bais.read());
		yMin = (short) (bais.read() << 8 | bais.read());
		xMax = (short) (bais.read() << 8 | bais.read());
		yMax = (short) (bais.read() << 8 | bais.read());
	}

	public void resolve() {
	}

	public int getNumberOfContours() {
		return numberOfContours;
	}

	@Override
	public short getXMaximum() {
		return xMax;
	}

	@Override
	public short getXMinimum() {
		return xMin;
	}

	@Override
	public short getYMaximum() {
		return yMax;
	}

	@Override
	public short getYMinimum() {
		return yMin;
	}
}
