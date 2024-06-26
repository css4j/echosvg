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
package io.sf.carte.echosvg.util.gui.resource;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * This class represents the buttons used in toolbars.
 *
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class JToolbarToggleButton extends JToggleButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new toolbar button.
	 */
	public JToolbarToggleButton() {
		initialize();
	}

	/**
	 * Creates a new toolbar button.
	 * 
	 * @param txt The button text.
	 */
	public JToolbarToggleButton(String txt) {
		super(txt);
		initialize();
	}

	/**
	 * Initializes the button.
	 */
	protected void initialize() {
		if (!System.getProperty("java.version").startsWith("1.3")) {
			setOpaque(false);
			setBackground(new java.awt.Color(0, 0, 0, 0));
		}
		setBorderPainted(false);
		setMargin(new Insets(2, 2, 2, 2));

		// Windows XP look and feel seems to have a bug due to which the
		// size of the parent container changes when the border painted
		// property is set. Temporary fix: disable mouseover behavior if
		// installed lnf is Windows XP
		if (!UIManager.getLookAndFeel().getName().equals("Windows")) {
			addMouseListener(new MouseListener());
		}
	}

	/**
	 * To manage the mouse interactions.
	 */
	protected class MouseListener extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent ev) {
			setBorderPainted(true);
		}

		@Override
		public void mouseExited(MouseEvent ev) {
			setBorderPainted(false);
		}

	}

}
