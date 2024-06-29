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

/**
 * A handler class that generates an array of floats from parsing a number list
 * or a point list.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class FloatArrayProducer extends DefaultNumberListHandler implements PointsHandler {

	/**
	 * List of <code>float[]</code> objects.
	 */
	protected LinkedList<float[]> as;

	/**
	 * The current <code>float[]</code> object.
	 */
	protected float[] a;

	/**
	 * The index in which to store the next number.
	 */
	protected int index;

	/**
	 * The total number of floats accumulated.
	 */
	protected int count;

	/**
	 * Returns the array of floats accumulated.
	 */
	public float[] getFloatArray() {
		return a;
	}

	// NumberListHandler /////////////////////////////////////////////////////

	/**
	 * Invoked when the number list attribute starts.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 *                           list.
	 */
	@Override
	public void startNumberList() throws ParseException {
		as = new LinkedList<>();
		a = new float[11];
		count = 0;
		index = 0;
	}

	/**
	 * Invoked when a float value has been parsed.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 */
	@Override
	public void numberValue(float v) throws ParseException {
		if (index == a.length) {
			as.add(a);
			a = new float[a.length * 2 + 1];
			index = 0;
		}
		a[index++] = v;
		count++;
	}

	/**
	 * Invoked when the number list attribute ends.
	 * 
	 * @exception ParseException if an error occures while processing the number
	 *                           list.
	 */
	@Override
	public void endNumberList() throws ParseException {
		float[] all = new float[count];
		int pos = 0;
		for (float[] b : as) {
			System.arraycopy(b, 0, all, pos, b.length);
			pos += b.length;
		}
		System.arraycopy(a, 0, all, pos, index);
		as.clear();
		a = all;
	}

	// PointsHandler /////////////////////////////////////////////////////////

	/**
	 * Implements {@link PointsHandler#startPoints()}.
	 */
	@Override
	public void startPoints() throws ParseException {
		startNumberList();
	}

	/**
	 * Implements {@link PointsHandler#point(float,float)}.
	 */
	@Override
	public void point(float x, float y) throws ParseException {
		numberValue(x);
		numberValue(y);
	}

	/**
	 * Implements {@link PointsHandler#endPoints()}.
	 */
	@Override
	public void endPoints() throws ParseException {
		endNumberList();
	}

}
