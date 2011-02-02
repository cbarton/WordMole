/*
 * Watercan.java
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
package game.components;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Watercan for the {@link game.singleplayer.SingleplayerGameFrame} and
 * {@link game.multiplayer.interfaces.MultiplayerGameFrame}
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class Watercan extends JLabel{
	private transient Image bg;
	private int curVal, max;
	
	/**
	 * Constructor
	 * @param min - <code>int</code> minimum amount for the watercan.
	 * @param max - <code>int</code> maximum amount for the watercan.
	 */
	public Watercan(int min, int max){
		bg = new ImageIcon(getClass().getResource("/game/images/backgrounds/watercan.png")).getImage();
		this.max = max;
		curVal = min;
		
		Dimension size = new Dimension(bg.getWidth(null), bg.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	/**
	 * Sets the current value of the water level.
	 * @param value - <code>int</code> for the current level of water.
	 */
	public void setValue(int value){
		curVal = value;
		repaint();
	}
	
	/**
	 * Returns the maximum value of the watercan.
	 * @return - <code>int</code> maximum value.
	 */
	public int getMaximum(){
		return max;
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(bg, 0, 0, null);
		
		int height = (int)Math.round((double)curVal / (double)max * 28.0); // 28: size of the can
		Color blue = new Color(168, 243, 239);
		
		g.setColor(blue);
		if ( height > 28 )
			g.fill3DRect(11, 29, 20, -28, true);
		else if ( height > 0 )
			g.fill3DRect(11, 29, 20, -height, true);
	}
}
