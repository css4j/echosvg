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
package io.sf.carte.echosvg.gvt.event;

/**
 * An abstract adapter class for receiving graphics node key events. The methods
 * in this class are empty. This class exists as convenience for creating
 * listener objects.
 *
 * <p>
 * Extend this class to create a <code>GraphicsNodeKeyEvent</code> listener and
 * override the methods for the events of interest. (If you implement the
 * <code>GraphicsNodeKeyListener</code> interface, you have to define all of the
 * methods in it. This abstract class defines null methods for them all, so you
 * can only have to define methods for events you care about.)
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public abstract class GraphicsNodeKeyAdapter implements GraphicsNodeKeyListener {

	@Override
	public void keyPressed(GraphicsNodeKeyEvent evt) {
	}

	@Override
	public void keyReleased(GraphicsNodeKeyEvent evt) {
	}

	@Override
	public void keyTyped(GraphicsNodeKeyEvent evt) {
	}

}
