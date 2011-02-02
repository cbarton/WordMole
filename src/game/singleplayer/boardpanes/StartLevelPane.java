/*
 * StartLevelPane.java
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
package game.singleplayer.boardpanes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * StartLevelPane is the overlay for the {@link game.singleplayer.SingleplayerGameFrame} showing the start of the level. 
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class StartLevelPane extends GameOverlayPane implements ActionListener{
	private Timer timer;
	private float alpha = 1f;
	private LevelSummaryPane ls;
	
	/**
	 * Constructor
	 * @param l - <code>int</code> for the level of the StartLevelPane.
	 * @param w - <code>int</code> for the width of the StartLevelPane.
	 * @param h - <code>int</code> for the height of the StartLevelPane.
	 * @param p - <code>Point</code> for the location of the pane.
	 * @param levelSummary - <code>LevelSummaryPane</code> containing the level summary for the completed level.
	 */
	public StartLevelPane(int l, int w, int h, Point p, LevelSummaryPane levelSummary){	
		ls = levelSummary;
		
		JPanel backgroundPnl = new JPanel();
		backgroundPnl.setLayout(new BoxLayout(backgroundPnl, BoxLayout.Y_AXIS));
		backgroundPnl.add(Box.createRigidArea(new Dimension(w/3+10, h/2-10)));
		
		JLabel lbl = new JLabel("Level " + l + " Start!");
		lbl.setFont(Util.Util.getGameFont());
		lbl.setForeground(Color.YELLOW);
		backgroundPnl.add(lbl, BorderLayout.CENTER);
		
		backgroundPnl.setBackground(Util.Util.getFadeBg());
		backgroundPnl.setOpaque(true);
		add(backgroundPnl);
		
		setUndecorated(true);
		setSize(new Dimension(w, h));
		setLocation(p);
		setAlwaysOnTop(true);
		Util.AWTUtilities.setWindowOpaque(this, false);
	
		timer = new Timer(2000, this);
		
		if ( levelSummary != null ){ // Hide the window
			setVisible(false);
			levelSummary.setSize(new Dimension(w, h));
			levelSummary.setLocation(p);
			levelSummary.setVisible(true);
			levelSummary.addWindowListener(new WindowAdapter(){
				public void windowClosed(WindowEvent e){
					setAlwaysOnTop(true);
					setVisible(true);
					timer.start();
					timer.setDelay(50); // Allow for smooth discard.
				}
			});
		} else{
			setVisible(true);
			timer.start();
			timer.setDelay(50); // Allow for smooth discard.
		}
	}
	
	public void actionPerformed(ActionEvent e){
		alpha += -0.20f;
	
	    if (alpha <= 0) {
	    	alpha = 0;
	    	timer.stop();
	    	dispose();
	    }
	    setAlpha(alpha);
	}
	
	public void updateLocation(Point location){
		setLocation(location);
		
		if ( ls != null )
			ls.setLocation(location);
	}
}
