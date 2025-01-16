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

import org.w3c.css.om.unit.CSSUnit;

/**
 * This class implements an event-based parser for the SVG points attribute
 * values (used with polyline and polygon elements).
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public class PointsParser extends NumberParser {

	/**
	 * The points handler used to report parse events.
	 */
	private PointsHandler pointsHandler;

	/**
	 * Whether the last character was a 'e' or 'E'.
	 */
	protected boolean eRead;

	/**
	 * Whether we are pending the processing of an 'y' value.
	 */
	private boolean processingY = false;

	private float lastX;

	/**
	 * Creates a new PointsParser with the given handler.

	 * @param handler The transform list handler.
	 */
	public PointsParser(PointsHandler handler) {
		pointsHandler = handler;
	}

	/**
	 * Allows an application to register a points handler.
	 *
	 * <p>
	 * If the application does not register a handler, all events reported by the
	 * parser will be silently ignored.
	 *
	 * <p>
	 * Applications may register a new or different handler in the middle of a
	 * parse, and the parser must begin using the new handler immediately.
	 * </p>
	 * 
	 * @param handler The transform list handler.
	 */
	public void setPointsHandler(PointsHandler handler) {
		pointsHandler = handler;
	}

	/**
	 * Returns the points handler in use.
	 */
	public PointsHandler getPointsHandler() {
		return pointsHandler;
	}

	/**
	 * Parses the current stream.
	 */
	@Override
	protected void doParse() throws ParseException, IOException {
		pointsHandler.startPoints();

		current = reader.read();
		skipSpaces();

		loop: for (;;) {
			if (current == -1) {
				break loop;
			}
			float x;
			try {
				x = parseFloat();
			} catch (CalcParseException e) {
				processingY = false;
				cssParse();
				pointsHandler.endPoints();
				return;
			}

			skipCommaSpaces();

			float y;
			try {
				y = parseFloat();
			} catch (CalcParseException e) {
				lastX = x;
				processingY = true;
				cssParse();
				pointsHandler.endPoints();
				return;
			}

			pointsHandler.point(x, y);
			skipCommaSpaces();
		}

		pointsHandler.endPoints();
	}

	@Override
	protected void handleNumber(short unitType, float floatValue) throws ParseException {
		if (unitType != CSSUnit.CSS_NUMBER) {
			throw new ParseException(createErrorMessage("dimension.not.number",
					new Object[] { CSSUnit.dimensionUnitString(unitType) }), -1, -1);
		}

		if (processingY) {
			pointsHandler.point(lastX, floatValue);
		} else {
			lastX = floatValue;
		}
		processingY = !processingY;
	}

}
