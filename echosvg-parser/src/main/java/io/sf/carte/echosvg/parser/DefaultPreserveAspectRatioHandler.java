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
 * This class provides an adapter for PreserveAspectRatioHandler.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultPreserveAspectRatioHandler implements PreserveAspectRatioHandler {
	/**
	 * The only instance of this class.
	 */
	public static final PreserveAspectRatioHandler INSTANCE = new DefaultPreserveAspectRatioHandler();

	/**
	 * This class does not need to be instantiated.
	 */
	protected DefaultPreserveAspectRatioHandler() {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#startPreserveAspectRatio()}.
	 */
	@Override
	public void startPreserveAspectRatio() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#none()}.
	 */
	@Override
	public void none() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMaxYMax()}.
	 */
	@Override
	public void xMaxYMax() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMaxYMid()}.
	 */
	@Override
	public void xMaxYMid() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMaxYMin()}.
	 */
	@Override
	public void xMaxYMin() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMidYMax()}.
	 */
	@Override
	public void xMidYMax() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMidYMid()}.
	 */
	@Override
	public void xMidYMid() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMidYMin()}.
	 */
	@Override
	public void xMidYMin() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMinYMax()}.
	 */
	@Override
	public void xMinYMax() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMinYMid()}.
	 */
	@Override
	public void xMinYMid() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#xMinYMin()}.
	 */
	@Override
	public void xMinYMin() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#meet()}.
	 */
	@Override
	public void meet() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#slice()}.
	 */
	@Override
	public void slice() throws ParseException {
	}

	/**
	 * Implements {@link PreserveAspectRatioHandler#endPreserveAspectRatio()}.
	 */
	@Override
	public void endPreserveAspectRatio() throws ParseException {
	}
}
