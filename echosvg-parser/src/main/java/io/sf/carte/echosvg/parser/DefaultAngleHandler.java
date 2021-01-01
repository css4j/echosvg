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

/**
 * This class provides an adapter for AngleHandler
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultAngleHandler implements AngleHandler {
    /**
     * The only instance of this class.
     */
    public static final AngleHandler INSTANCE
        = new DefaultAngleHandler();

    /**
     * This class does not need to be instantiated.
     */
    protected DefaultAngleHandler() {
    }

    /**
     * Implements {@link AngleHandler#startAngle()}.
     */
    @Override
    public void startAngle() throws ParseException {
    }

    /**
     * Implements {@link AngleHandler#angleValue(float)}.
     */
    @Override
    public void angleValue(float v) throws ParseException {
    }

    /**
     * Implements {@link AngleHandler#deg()}.
     */
    @Override
    public void deg() throws ParseException {
    }

    /**
     * Implements {@link AngleHandler#grad()}.
     */
    @Override
    public void grad() throws ParseException {
    }

    /**
     * Implements {@link AngleHandler#rad()}.
     */
    @Override
    public void rad() throws ParseException {
    }

    /**
     * Implements {@link AngleHandler#endAngle()}.
     */
    @Override
    public void endAngle() throws ParseException {
    }
}
