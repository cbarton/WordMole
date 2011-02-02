/*
 * ChangingButton.java
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
import javax.swing.JButton;

/**
 * ChangingButton allows for a hover effect with different Images to be overlayed on a JButton.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class ChangingButton extends JButton{
	private Image nohover, hover, other;
	private boolean hovered = false; // For hover over.
	private boolean nohovered = true; // Normal 
	private boolean othered = false; // For the hand over.
	
	/**
	 * Constructor for just a normal image
	 * @param icon_nohover - <code>ImageIcon</code> for the background image.
	 */
	public ChangingButton(ImageIcon icon_nohover){
		setupButton(icon_nohover.getImage(), null, null);
	}
	
	/**
	 * Constructor needing a hover image.
	 * @param icon_nohover - <code>ImageIcon</code> for the regular background image.
	 * @param icon_hover - <code>ImageIcon</code> for the hovered background image.
	 */
	public ChangingButton(ImageIcon icon_nohover, ImageIcon icon_hover){
		setupButton(icon_nohover.getImage(), icon_hover.getImage(), null);
	}
	
	/**
	 * Constructor needing three different overlays.
	 * @param icon_nohover - <code>ImageIcon</code> for the regular background image.
	 * @param icon_hover - <code>ImageIcon</code> for the hovered background image.
	 * @param icon_else - <code>ImageIcon</code> for the other background image.
	 */
	public ChangingButton(ImageIcon icon_nohover, ImageIcon icon_hover, ImageIcon icon_else){
		setupButton(icon_nohover.getImage(), icon_hover.getImage(), icon_else.getImage());
	}
	
	// Sets up the button 
	private void setupButton(Image icon_nohover, Image icon_hover, Image icon_else){
		nohover = icon_nohover;
		hover = icon_hover;
		other = icon_else;
		
		Dimension size = new Dimension(nohover.getWidth(null), nohover.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
	    setSize(size);
	    setBorder(null);
	}
	
	/**
	 * Sets whether the button is hovered over or not.
	 * @param isHovered - <code>boolean</code> whether the button is being hovered over or not.
	 */
	public void setHovered(boolean isHovered){
		hovered = isHovered;
		nohovered = !isHovered;
		repaint();
	}
	
	/**
	 * Sets the background of the button if there is some external effect on the buttons background.
	 * @param isEffected - <code>boolean</code> whether or not the button is effected and should change background. 
	 */
	public void setEffectedBackground(boolean isEffected){
		othered = isEffected;
		repaint();
	}
		
	public void paintComponent(Graphics g){
		if ( othered )
			g.drawImage(other, 0, 0, null);
		else if ( hovered )
			g.drawImage(hover, 0, 0, null);
		else if ( nohovered )
			g.drawImage(nohover, 0, 0, null);
			
	}
}
