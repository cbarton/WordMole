/*
 * WordMoleScrollbar.java
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
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * The default Scrollbar for the Word Mole game
 * @author Chris Barton
 *
 */
public class WordMoleScrollbar extends BasicScrollBarUI{
	private JScrollPane pane;
	private ImageIcon increase = new ImageIcon(getClass().getResource("/game/images/scrollbar/increase.png"));
	private ImageIcon decrease = new ImageIcon(getClass().getResource("/game/images/scrollbar/decrease.png"));
	
	/**
	 * Constructor
	 * @param sp - <code>WordMoleScrollPane</code> to have this scrollbar scheme.
	 */
	public WordMoleScrollbar(WordMoleScrollPane sp){
		pane = sp;
	}
	
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
		c.setOpaque(false);
		c.repaint();		
	}
	
	protected void paintTrack(Graphics g, JComponent c, Rectangle thumbBounds){
		c.setOpaque(false);
		c.repaint();
	}
	
	protected void paintDecreaseHighlight(Graphics g){
		g.drawImage(decrease.getImage(), 0, 0, null);
		pane.repaint();
	}
	
	protected void paintIncreaseHighlight(Graphics g){
		g.drawImage(increase.getImage(), 0, 0, null);
		pane.repaint();
	}
	
	protected JButton createIncreaseButton(int orientation){
		JButton Button = new JButton(increase){
			public void paintComponent(Graphics g){
				setOpaque(false);
				super.paintComponent(g);
			}
		}; 
		Button.setBackground(Color.BLACK);
		Button.setBorder(null);
		Button.addActionListener(scrollListener);
		return Button;
	}
	
	protected JButton createDecreaseButton(int orientation){
		JButton Button = new JButton(decrease){
			public void paintComponent(Graphics g){
				setOpaque(false);
				super.paintComponent(g);
			}
		}; 
		Button.setBackground(Color.BLACK);
		Button.setBorder(null);
		Button.addActionListener(scrollListener);
		return Button;
	}
}
