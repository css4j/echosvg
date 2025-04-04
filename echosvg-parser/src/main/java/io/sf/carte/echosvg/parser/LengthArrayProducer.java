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
package io.sf.carte.echosvg.parser;

import java.util.LinkedList;

import org.w3c.css.om.unit.CSSUnit;

/**
 * A handler class that generates an array of shorts and an array floats from
 * parsing a length list.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class LengthArrayProducer extends DefaultLengthListHandler {

	/**
	 * List of <code>float[]</code> objects.
	 */
	protected LinkedList<float[]> vs;

	/**
	 * The current <code>float[]</code> object.
	 */
	protected float[] v;

	/**
	 * List of <code>short[]</code> objects.
	 */
	protected LinkedList<short[]> us;

	/**
	 * The current <code>short[]</code> object.
	 */
	protected short[] u;

	/**
	 * The index in which to store the next length.
	 */
	protected int index;

	/**
	 * The total number of lengths accumulated.
	 */
	protected int count;

	/**
	 * The unit for the current length.
	 */
	protected short currentUnit = CSSUnit.CSS_INVALID;

	/**
	 * Returns the array of length units accumulated.
	 */
	public short[] getLengthTypeArray() {
		return u;
	}

	/**
	 * Returns the array of length values accumulated.
	 */
	public float[] getLengthValueArray() {
		return v;
	}

	// LengthListHandler /////////////////////////////////////////////////////

	/**
	 * Invoked when the length list attribute starts.
	 * 
	 * @exception ParseException if an error occurs while processing the number
	 *                           list.
	 */
	@Override
	public void startLengthList() throws ParseException {
		us = new LinkedList<>();
		u = new short[11];
		vs = new LinkedList<>();
		v = new float[11];
		count = 0;
		index = 0;
	}

	/**
	 * Invoked when a float value has been parsed.
	 * 
	 * @exception ParseException if an error occurs while processing the number
	 */
	public void numberValue(float v) throws ParseException {
		throw new IllegalStateException("Unexpected <number>: " + v);
	}

	/**
	 * Implements {@link LengthHandler#lengthValue(float)}.
	 */
	@Override
	public void lengthValue(float val) throws ParseException {
		if (index == v.length) {
			vs.add(v);
			v = new float[v.length * 2 + 1];
			us.add(u);
			u = new short[u.length * 2 + 1];
			index = 0;
		}
		v[index] = val;
	}

	/**
	 * Implements {@link LengthHandler#startLength()}.
	 */
	@Override
	public void startLength() throws ParseException {
		currentUnit = CSSUnit.CSS_NUMBER;
	}

	/**
	 * Implements {@link LengthHandler#endLength()}.
	 */
	@Override
	public void endLength() throws ParseException {
		u[index++] = currentUnit;
		count++;
	}

	@Override
	protected void setUnit(short unit) {
		currentUnit = unit;
	}

	/**
	 * Invoked when the length list attribute ends.
	 * 
	 * @exception ParseException if an error occurs while processing the number
	 *                           list.
	 */
	@Override
	public void endLengthList() throws ParseException {
		float[] allValues = new float[count];
		int pos = 0;
		for (float[] a : vs) {
			System.arraycopy(a, 0, allValues, pos, a.length);
			pos += a.length;
		}
		System.arraycopy(v, 0, allValues, pos, index);
		vs.clear();
		v = allValues;

		short[] allUnits = new short[count];
		pos = 0;
		for (short[] a : us) {
			System.arraycopy(a, 0, allUnits, pos, a.length);
			pos += a.length;
		}
		System.arraycopy(u, 0, allUnits, pos, index);
		us.clear();
		u = allUnits;
	}

}
