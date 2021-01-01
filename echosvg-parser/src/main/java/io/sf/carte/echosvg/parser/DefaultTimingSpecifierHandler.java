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

import java.util.Calendar;

/**
 * An adapter class for {@link TimingSpecifierHandler}.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultTimingSpecifierHandler implements TimingSpecifierHandler {

    /**
     * The default handler.
     */
    public static final TimingSpecifierHandler INSTANCE
        = new DefaultTimingSpecifierHandler();

    protected DefaultTimingSpecifierHandler() {
    }

    /**
     * Invoked when an offset value timing specifier is parsed.
     */
    @Override
    public void offset(float offset) {
    }

    /**
     * Invoked when a syncbase value timing specifier is parsed.
     */
    @Override
    public void syncbase(float offset, String syncbaseID, String timeSymbol) {
    }

    /**
     * Invoked when an eventbase value timing specifier is parsed.
     */
    @Override
    public void eventbase(float offset, String eventbaseID, String eventType) {
    }

    /**
     * Invoked when a repeat value timing specifier with no iteration
     * is parsed.
     */
    @Override
    public void repeat(float offset, String syncbaseID) {
    }

    /**
     * Invoked when a repeat value timing specifier with an iteration
     * is parsed.
     */
    @Override
    public void repeat(float offset, String syncbaseID, int repeatIteration) {
    }

    /**
     * Invoked when an accesskey value timing specifier is parsed.
     */
    @Override
    public void accesskey(float offset, char key) {
    }

    /**
     * Invoked when an SVG 1.2 accessKey value timing specifier is parsed.
     */
    @Override
    public void accessKeySVG12(float offset, String keyName) {
    }

    /**
     * Invoked when a media marker value timing specifier is parsed.
     */
    @Override
    public void mediaMarker(String syncbaseID, String markerName) {
    }

    /**
     * Invoked when a wallclock value timing specifier is parsed.
     */
    @Override
    public void wallclock(Calendar time) {
    }

    /**
     * Invoked when an indefinite value timing specifier is parsed.
     */
    @Override
    public void indefinite() {
    }
}
