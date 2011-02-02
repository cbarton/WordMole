/*
 * InfoPopup.java
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
 */package game.status;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Util.AWTUtilities;

/**
 * InfoPopup is the window for displaying each word compeleted in Word Mole.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class InfoPopup extends JFrame implements ActionListener{
	private Timer timer = new Timer(50, this);
	private float alpha = 1f;
	private Point loc;
	
	/**
	 * Constructor
	 * @param s - <code>String</code> containing the content of the window.
	 * @param x - <code>int</code> for the x location on the screen.
	 * @param y - <code>int</code> for the y location on the screen.
	 */
	public InfoPopup(String s, int x, int y){
		loc = new Point(x, y);

		JPanel backgroundPnl = new JPanel();
		JLabel lbl = new JLabel(s);
		
		lbl.setFont(Util.Util.getGameFont());
		lbl.setForeground(Color.YELLOW);
		backgroundPnl.add(lbl);
			
		backgroundPnl.setBackground(Util.Util.getFadeBg());
		backgroundPnl.setOpaque(true);
		add(backgroundPnl);
		
		setBounds(x, y, (int)backgroundPnl.getPreferredSize().getWidth(), (int)backgroundPnl.getPreferredSize().getHeight());
		setUndecorated(true);
		setAlwaysOnTop(true);
		
		// Curve the edges
		Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 5, 5);
		AWTUtilities.setWindowShape(this, shape);
		AWTUtilities.setWindowOpaque(this, false);
		
		setAlpha(alpha);		
		timer.start();
	}
 
	private void setAlpha(float alpha) {
		AWTUtilities.setWindowOpacity(this, alpha);
    }
	
	public void actionPerformed(ActionEvent e) {
		alpha += -0.033f;
		
		loc.setLocation(loc.x, loc.y-1);
		setLocation(loc); // Fade up
		
	    if (alpha <= 0) {
	    	alpha = 0;
	    	timer.stop();
	    }
	    setAlpha(alpha);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5); 
	}
}
