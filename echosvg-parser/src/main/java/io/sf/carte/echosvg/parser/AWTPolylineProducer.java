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
package io.sf.carte.echosvg.parser;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.io.Reader;

/**
 * This class produces a polyline shape from a reader.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class AWTPolylineProducer implements PointsHandler, ShapeProducer {
    /**
     * The current path.
     */
    protected GeneralPath path;

    /**
     * Is the current path a new one?
     */
    protected boolean newPath;

    /**
     * The winding rule to use to construct the path.
     */
    protected int windingRule;

    /**
     * Utility method for creating an ExtendedGeneralPath.
     * @param r The reader used to read the path specification.
     * @param wr The winding rule to use for creating the path.
     */
    public static Shape createShape(Reader r, int wr)
        throws IOException,
               ParseException {
        PointsParser p = new PointsParser();
        AWTPolylineProducer ph = new AWTPolylineProducer();

        ph.setWindingRule(wr);
        p.setPointsHandler(ph);
        p.parse(r);

        return ph.getShape();
    }

    /**
     * Sets the winding rule used to construct the path.
     */
    @Override
    public void setWindingRule(int i) {
        windingRule = i;
    }

    /**
     * Returns the current winding rule.
     */
    @Override
    public int getWindingRule() {
        return windingRule;
    }

    /**
     * Returns the Shape object initialized during the last parsing.
     * @return the shape or null if this handler has not been used by
     *         a parser.
     */
    @Override
    public Shape getShape() {
        return path;
    }

    /**
     * Implements {@link PointsHandler#startPoints()}.
     */
    @Override
    public void startPoints() throws ParseException {
        path = new GeneralPath(windingRule);
        newPath = true;
    }

    /**
     * Implements {@link PointsHandler#point(float,float)}.
     */
    @Override
    public void point(float x, float y) throws ParseException {
        if (newPath) {
            newPath = false;
            path.moveTo(x, y);
        } else {
            path.lineTo(x, y);
        }
    }

    /**
     * Implements {@link PointsHandler#endPoints()}.
     */
    @Override
    public void endPoints() throws ParseException {
    }
}
