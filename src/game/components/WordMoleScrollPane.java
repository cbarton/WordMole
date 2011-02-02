/*
 * WordMoleScrollPane.java
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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 * Default JScrollPane for the Word Mole game.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class WordMoleScrollPane extends JScrollPane{
	/**
	 * Creates a WordMoleScrollPane that displays the view component in a viewport whose view position can be controlled with a pair of scrollbars.
	 * @param comp - <code>JComponent</code> to display in the scrollpanes viewport.
	 * @param c - <code>Color</code> for the border of the WordMoleScrollPane.
	 * @param title - <code>String</code> for the title of the border.
	 */
	public WordMoleScrollPane(JComponent comp, Color c, String title){
		super(comp);
		getViewport().setOpaque(false);
		setAlignmentX(CENTER_ALIGNMENT);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
	  
	    TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(c), title);
	    border.setTitleColor(Color.WHITE);
	    border.setTitleFont(Util.Util.getGameFontSmall());
	    setBorder(border);
	   
	    getVerticalScrollBar().setUI(new WordMoleScrollbar(this));
	}
	
	public void setSize(Dimension size){
		super.setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
	}
	
	public void paintComponent(Graphics g){
		setOpaque(false);
		super.paintComponent(g);
	}
}