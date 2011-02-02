/*
 * GameOverlayPane.java
 * Copyright (C) 2010  Chris Barton
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 *   Questions/Comments: c.chris.b@gmail.com
 *   WordMole is available free at http://wordmole.sourceforge.net/
 */
package game.singleplayer.boardpanes;
import java.awt.Point;

import javax.swing.JFrame;

/**
 * GameOverlayPane is the superclass for all of the overlay panes for 
 * {@link game.singleplayer.SingleplayerGameFrame}.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class GameOverlayPane extends JFrame{
	/**
	 * Sets the opacity of the window.
	 * @param alpha - <code>float</code> alpha opacity.
	 */
	protected void setAlpha(float alpha){
		 Util.AWTUtilities.setWindowOpacity(this, alpha);
	}
	
	/**
	 * Sets the location of the pane.
	 * @param location - <code>Point</code> containing the new location.
	 */
	public void updateLocation(Point location){
		setLocation(location);
	}
}
