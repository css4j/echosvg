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

import java.awt.Shape;
import java.io.IOException;
import java.io.Reader;

/**
 * This class produces a polygon shape from a reader.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AWTPolygonProducer extends AWTPolylineProducer {

	/**
	 * Utility method for creating an ExtendedGeneralPath.
	 * 
	 * @param r  The reader used to read the path specification.
	 * @param wr The winding rule to use for creating the path.
	 */
	public static Shape createShape(Reader r, int wr) throws IOException, ParseException {
		AWTPolygonProducer ph = new AWTPolygonProducer();
		ph.setWindingRule(wr);

		PointsParser p = new PointsParser(ph);
		p.parse(r);

		return ph.getShape();
	}

	/**
	 * Implements {@link PointsHandler#endPoints()}.
	 */
	@Override
	public void endPoints() throws ParseException {
		path.closePath();
	}

}
