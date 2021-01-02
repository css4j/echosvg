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
 *
 * @author tonny@kiyut.com
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultNumberListHandler implements NumberListHandler {
	/**
	 * The only instance of this class.
	 */
	public static final NumberListHandler INSTANCE = new DefaultNumberListHandler();

	/**
	 * This class does not need to be instantiated.
	 */
	protected DefaultNumberListHandler() {
	}

	/**
	 * Implements {@link NumberListHandler#startNumberList()}.
	 */
	@Override
	public void startNumberList() throws ParseException {
	}

	/**
	 * Implements {@link NumberListHandler#endNumberList()}.
	 */
	@Override
	public void endNumberList() throws ParseException {
	}

	/**
	 * Implements {@link NumberListHandler#startNumber()}.
	 */
	@Override
	public void startNumber() throws ParseException {
	}

	/**
	 * Implements {@link NumberListHandler#numberValue(float)}.
	 */
	@Override
	public void numberValue(float v) throws ParseException {
	}

	/**
	 * Implements {@link NumberListHandler#endNumber()}.
	 */
	@Override
	public void endNumber() throws ParseException {
	}

}
