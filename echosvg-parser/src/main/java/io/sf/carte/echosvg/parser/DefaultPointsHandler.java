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

/**
 * This class provides an adapter for PointsHandler.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultPointsHandler implements PointsHandler {
	/**
	 * The only instance of this class.
	 */
	public static final DefaultPointsHandler INSTANCE = new DefaultPointsHandler();

	/**
	 * This class does not need to be instantiated.
	 */
	protected DefaultPointsHandler() {
	}

	/**
	 * Implements {@link PointsHandler#startPoints()}.
	 */
	@Override
	public void startPoints() throws ParseException {
	}

	/**
	 * Implements {@link PointsHandler#point(float,float)}.
	 */
	@Override
	public void point(float x, float y) throws ParseException {
	}

	/**
	 * Implements {@link PointsHandler#endPoints()}.
	 */
	@Override
	public void endPoints() throws ParseException {
	}
}
