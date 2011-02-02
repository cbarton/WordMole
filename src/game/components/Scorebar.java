/*
 * Scorebar.java
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Util.WordMoleScorebar;

/**
 * Scorebar for the {@link game.singleplayer.SingleplayerGameFrame}.
 *  Implements the {@link Util.WordMoleScorebar}
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class Scorebar extends JLabel implements WordMoleScorebar{
	private Image bg;
	private int max, curVal;
	
	/**
	 * Constructor
	 * @param min - <code>int</code> with the minimum value for the Scorebar.
	 * @param max - <code>int</code> with the maximum value for the Scorebar.
	 */
	public Scorebar(int min, int max){
		curVal = min;
		this.max = max;
		bg = new ImageIcon(getClass().getResource("/game/images/backgrounds/progbar.png")).getImage();
		
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
		int width = (int)Math.round(((double)curVal / (double)max) * 133.0); // 133: width of the bar image itself
	
		g.drawImage(bg, 0, 0, null);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font(Util.Util.getGameFont().getFamily(), Util.Util.getGameFont().getStyle(), Util.Util.getGameFont().getSize()-4));
		if ( max < 100 )
			g.drawString(Integer.toString(max), 178, 17); // two pixels needed for the size difference
		else
			g.drawString(Integer.toString(max), 174, 17);
		
		if ( curVal < 100 )
			g.drawString(Integer.toString(curVal), 10, 17);
		else
			g.drawString(Integer.toString(curVal), 6, 17);
		
		g.setColor(RED);
		if ( width > 133 )
			g.fill3DRect(37, 7, 133, 10, true); // fill to the end if its above max
		else if ( width > 0 ) // Needed for the 0 effect
			g.fill3DRect(37, 7, width, 10, true);
	}
}
