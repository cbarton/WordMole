/*
 * WordMoleTextArea.java
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

import javax.swing.JTextArea;

/**
 * Default JTextArea for the Word Mole game.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class WordMoleTextArea extends JTextArea {
	
	/**
	 * Constructs a new empty WordMoleTextArea with the specified number of rows and columns and colors.
	 * @param fg - Color - Foreground color.
	 * @param bg - Color - Background color.
	 * @param rows - int - Number of rows.
	 * @param cols - int - Number of columns.
	 */
	public WordMoleTextArea(Color fg, Color bg, int rows, int cols){
		super(rows, cols);
		setLineWrap(true);
		setEditable(false);
		setBackground(bg);
		setForeground(fg);
		setOpaque(false);
		setFont(Util.Util.getGameFontSmall());
	}
	
	public void setSize(Dimension size){
		super.setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setOpaque(false);
	}
}
