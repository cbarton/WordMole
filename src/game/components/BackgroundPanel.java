/*
 * BackgroundPanel.java
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * BackgroundPanel allows for a image to be displayed as the background for a panel.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel{
	private transient Image bg;
	private transient Image bg2;
	private boolean needRepaint = false;
	
	/**
	 * Constructor taking in the background image for the panel.
	 * @param img - <code>ImageIcon</code> for the background image. 
	 */
	public BackgroundPanel(ImageIcon img){
		bg = img.getImage();
	    Dimension size = new Dimension(bg.getWidth(null), bg.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	}
	
	/**
	 * Changes the image on the background.
	 * @param img - <code>Image</code> to be changed too.
	 */
	public void changeImage(Image img){
		bg2 = img;
		needRepaint = !needRepaint;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		if ( needRepaint )
			g.drawImage(bg2, 0, 0, null);
		else
			g.drawImage(bg, 0, 0, null);
		
	}
}
