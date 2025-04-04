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
 * This class provides an adapter for FragmentIdentifierHandler.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class DefaultFragmentIdentifierHandler extends DefaultPreserveAspectRatioHandler
		implements FragmentIdentifierHandler {

	public DefaultFragmentIdentifierHandler() {
		super();
	}

	/**
	 * Implements {@link FragmentIdentifierHandler#startFragmentIdentifier()}.
	 */
	@Override
	public void startFragmentIdentifier() throws ParseException {
	}

	/**
	 * Invoked when an ID has been parsed.
	 * 
	 * @param s The string that represents the parsed ID.
	 * @exception ParseException if an error occured while processing the fragment
	 *                           identifier
	 */
	@Override
	public void idReference(String s) throws ParseException {
	}

	/**
	 * Invoked when 'viewBox(x,y,width,height)' has been parsed.
	 * 
	 * @param x      the x coordinate of the viewbox.
	 * @param y      the y coordinate of the viewbox.
	 * @param width  the width of the viewbox.
	 * @param height the height of the viewbox.
	 * @exception ParseException if an error occured while processing the fragment
	 *                           identifier
	 */
	@Override
	public void viewBox(float x, float y, float width, float height) throws ParseException {
	}

	/**
	 * Invoked when a view target specification starts.
	 * 
	 * @exception ParseException if an error occured while processing the fragment
	 *                           identifier
	 */
	@Override
	public void startViewTarget() throws ParseException {
	}

	/**
	 * Invoked when a view target component has been parsed.
	 * 
	 * @param name the target name.
	 * @exception ParseException if an error occured while processing the fragment
	 *                           identifier
	 */
	@Override
	public void viewTarget(String name) throws ParseException {
	}

	/**
	 * Invoked when a view target specification ends.
	 * 
	 * @exception ParseException if an error occured while processing the fragment
	 *                           identifier
	 */
	@Override
	public void endViewTarget() throws ParseException {
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

	/**
	 * Invoked when a 'zoomAndPan' specification has been parsed.
	 * 
	 * @param magnify true if 'magnify' has been parsed.
	 * @exception ParseException if an error occured while processing the fragment
	 *                           identifier
	 */
	@Override
	public void zoomAndPan(boolean magnify) {
	}

	/**
	 * Implements {@link FragmentIdentifierHandler#endFragmentIdentifier()}.
	 */
	@Override
	public void endFragmentIdentifier() throws ParseException {
	}

}
