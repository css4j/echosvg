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
 * This class provides an adapter for TransformListHandler.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultTransformListHandler implements TransformListHandler {

	/**
	 * The only instance of this class.
	 */
	public static final TransformListHandler INSTANCE = new DefaultTransformListHandler();

	/**
	 * This class does not need to be instantiated.
	 */
	protected DefaultTransformListHandler() {
	}

	/**
	 * Implements {@link TransformListHandler#startTransformList()}.
	 */
	@Override
	public void startTransformList() throws ParseException {
	}

	/**
	 * Implements
	 * {@link TransformListHandler#matrix(float,float,float,float,float,float)}.
	 */
	@Override
	public void matrix(float a, float b, float c, float d, float e, float f) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#rotate(float)}.
	 */
	@Override
	public void rotate(float theta) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#rotate(float,float,float)}.
	 */
	@Override
	public void rotate(float theta, float cx, float cy) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#translate(float)}.
	 */
	@Override
	public void translate(float tx) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#translate(float,float)}.
	 */
	@Override
	public void translate(float tx, float ty) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#scale(float)}.
	 */
	@Override
	public void scale(float sx) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#scale(float,float)}.
	 */
	@Override
	public void scale(float sx, float sy) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#skewX(float)}.
	 */
	@Override
	public void skewX(float skx) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#skewY(float)}.
	 */
	@Override
	public void skewY(float sky) throws ParseException {
	}

	/**
	 * Implements {@link TransformListHandler#endTransformList()}.
	 */
	@Override
	public void endTransformList() throws ParseException {
	}
}
