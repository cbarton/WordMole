/*
 * PlayerScorebar.java
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
package game.multiplayer.components;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Util.WordMoleScorebar;

/**
 * The Scorebar for the {@link game.multiplayer.MultiplayerGameFrameImpl}
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class PlayerScorebar extends JLabel implements WordMoleScorebar{
	private transient Image bg;
	private int max, curVal;
	private String name;
	
	/**
	 * Constructs the PlayerScorebar.
	 * @param min - <code>int</code> minimum value of the PlayerScorebar.
	 * @param max - <code>int</code> maximum value of the PlayerScorebar.
	 * @param player_name - <code>String</code> name of the PlayerScorebar.
	 */
	public PlayerScorebar(int min, int max, String player_name){
		curVal = min;
		this.max = max;
		bg = new ImageIcon(getClass().getResource("/game/images/scorebar/multiplayer/m_scorebar.png")).getImage();
		name = player_name;
		
		Dimension size = new Dimension(bg.getWidth(null), bg.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	public void setValue(int value){
		curVal = value;
		repaint();
	}
	
	public int getValue(){
		return curVal;
	}
	
	public void setMaximum(int max){
		this.max = max;
		repaint();
	}
	
	public int getMaximum(){
		return max;
	}
	
		
	public void paintComponent(Graphics g){
		int width = (int)Math.round(((double)curVal / (double)max) * 41.0); // 43: width of the bar image itself
	
		g.drawImage(bg, 0, 0, null);
		
		g.setColor(Color.WHITE);
		g.setFont(Util.Util.getGameFontSmall());
		g.drawString(name, 3, 13);
		
		g.setColor(RED);
		if ( width > 41 )
			g.fill3DRect(3, 19, 41, 12, true); // fill to the end if its above max
		else if ( width > 0 ) // Needed for the 0 effect
			g.fill3DRect(3, 19, width, 12, true);
	}
}
