/*
 * BackgroundLabel.java
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

import Util.Util;

/**
 * BackgroundLabel allows for a image to be displayed as the background for a label.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class BackgroundLabel extends JLabel{
	private transient Image bg;
	private String text;
	private Color color;
	private int offset; // Used for offsetting text from the picture
	
	/**
	 * Constructor for just the image in the background with no offset for a particular component.
	 * @param img - ImageIcon - The background image.
	 * @param s - String - The text to go on the background.
	 */
	public BackgroundLabel(ImageIcon img, String s){
		bg = img.getImage();
		text = s;
		color = Color.WHITE;
		offset = 2;
	    Dimension size = new Dimension(bg.getWidth(null), bg.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	/**
	 * Constructor for a particular offset of a component, the components are discussed in {@link #paintComponent(Graphics)}
	 * @param img - ImageIcon - The background image.
	 * @param s - String - The text to go on the background.
	 * @param offset - int - The magic number for the component's view.
	 */
	public BackgroundLabel(ImageIcon img, String s, int offset){
		bg = img.getImage();
		text = s;
		color = Color.WHITE;
		this.offset = offset;
	    Dimension size = new Dimension(bg.getWidth(null), bg.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	/** 
	 * Offset: 0 {@link Stopwatch}
	 * Offset: 1 {@link game.singleplayer.SingleplayerGameFrame} CurrentWord
	 * Offset: 2 {@link game.singleplayer.SingleplayerGameFrame} LevelLbl
	 * Offset: 2 {@link game.singleplayer.SingleplayerGameFrame} ScoreLbl
	 * Offset: 2 {@link game.singleplayer.SingleplayerGameFrame} OverallLbl
	 **/
	public void paintComponent(Graphics g){
		g.drawImage(bg, 0, 0, null);
		
		g.setColor(color);
		g.setFont(Util.getGameFontSmall());
		
		switch ( offset ){
			case 0:
				g.drawString(text, 42, 25);
				break;
			case 1:
				g.drawString(text, 0, 15);
				break;
			case 2: 
				g.drawString(text, bg.getWidth(this)/2-2, bg.getHeight(this)-8);
				break;
			case 3:
				g.drawString(text, bg.getWidth(this)/2+6, bg.getHeight(this)-8);
				break;
			case 4:
				g.drawString(text, bg.getWidth(this)/2-10, bg.getHeight(this)-8);
				break;
		}
	}
	
	public void setText(String text){
		this.text = text;
		repaint();
	}
	
	public void setForeground(Color fg){
		color = fg;
		repaint();
	}
	
	public Color getForeground(){
		return color;
	}
}
