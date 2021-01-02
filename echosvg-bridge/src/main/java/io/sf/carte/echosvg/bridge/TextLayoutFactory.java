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
package io.sf.carte.echosvg.bridge;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;

/**
 * Interface implemented by factory instances that can return TextSpanLayouts
 * appropriate to AttributedCharacterIterator instances.
 *
 * @see io.sf.carte.echosvg.bridge.TextSpanLayout
 * @author <a href="mailto:bill.haneman@ireland.sun.com">Bill Haneman</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public interface TextLayoutFactory {

	/**
	 * Returns an instance of TextSpanLayout suitable for rendering the
	 * AttributedCharacterIterator.
	 * 
	 * @param aci     the character iterator to be laid out
	 * @param charMap Indicates how chars in aci map to original text char array.
	 * @param offset  The offset position for the text layout.
	 * @param frc     the rendering context for the fonts used.
	 */
	TextSpanLayout createTextLayout(AttributedCharacterIterator aci, int[] charMap, Point2D offset,
			FontRenderContext frc);

}
