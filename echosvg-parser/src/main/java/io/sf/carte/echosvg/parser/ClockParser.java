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

import java.io.IOException;

/**
 * A parser for clock values.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class ClockParser extends TimingParser {

	/**
	 * The handler used to report parse events.
	 */
	protected ClockHandler clockHandler;

	/**
	 * Whether this parser should parse offsets rather than clock values.
	 */
	protected boolean parseOffset;

	/**
	 * Creates a new ClockParser.
	 */
	public ClockParser(boolean parseOffset) {
		super(false, false);
		this.parseOffset = parseOffset;
	}

	/**
	 * Registers a parse event handler.
	 */
	public void setClockHandler(ClockHandler handler) {
		clockHandler = handler;
	}

	/**
	 * Returns the parse event handler in use.
	 */
	public ClockHandler getClockHandler() {
		return clockHandler;
	}

	/**
	 * Parses a clock value.
	 */
	@Override
	protected void doParse() throws ParseException, IOException {
		current = reader.read();
		float clockValue = parseOffset ? parseOffset() : parseClockValue();
		if (current != -1) {
			reportError("end.of.stream.expected", new Object[] { current });
		}
		if (clockHandler != null) {
			clockHandler.clockValue(clockValue);
		}
	}

}
