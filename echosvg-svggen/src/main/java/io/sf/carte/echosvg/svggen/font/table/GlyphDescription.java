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
package io.sf.carte.echosvg.svggen.font.table;

/**
 * Specifies access to glyph description classes, simple and composite.
 * 
 * @author For later modifications, see Git history.
 * @version $Id$
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public interface GlyphDescription {

	int getEndPtOfContours(int i);

	byte getFlags(int i);

	short getXCoordinate(int i);

	short getYCoordinate(int i);

	short getXMaximum();

	short getXMinimum();

	short getYMaximum();

	short getYMinimum();

	boolean isComposite();

	int getPointCount();

	int getContourCount();

	// public int getComponentIndex(int c);
	// public int getComponentCount();
}
