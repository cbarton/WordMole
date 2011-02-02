/*
 * LevelSummaryPane.java
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
import game.components.WordMoleList;
import game.components.WordMoleScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.Timer;

/**
 * LevelSummaryPane is the overlay for the {@link game.singleplayer.SingleplayerGameFrame} showing the level stats. 
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class LevelSummaryPane extends JDialog implements ActionListener{
	private ArrayList<String> levelWords;	
	private Timer timer;
	private float alpha = 0f;
	private boolean fadein = true, fadeout = false;
	
	/**
	 * Constructor
	 * @param owner - <code>JFrame</code> for the owner of the frame.
	 * @param words - <code>ArrayList&gt;String&lt;</code> containing the levels words found.
	 */
	public LevelSummaryPane(JFrame owner, ArrayList<String> words){
		super(owner, true);
		levelWords = words;
		
		JPanel backgroundPnl = new JPanel(){
			public void paintComponent(Graphics g){
				setOpaque(true); // Allow dynamic updates.
				super.paintComponent(g);
			}
		};
		backgroundPnl.setLayout(new BoxLayout(backgroundPnl, BoxLayout.Y_AXIS));
		
		JLabel complete = Util.Util.createLbl("LEVEL COMPLETE!", Util.Util.getNormalFont());
		backgroundPnl.add(complete);
		backgroundPnl.add(Box.createVerticalGlue());
		
		JLabel num_wordsLbl = Util.Util.createLbl("Words Found: " + words.size(), Util.Util.getGameFont());
		backgroundPnl.add(num_wordsLbl);
		
		WordMoleScrollPane pane = new WordMoleScrollPane(new WordMoleList(this, levelWords.toArray()), Color.WHITE, null);
		pane.setSize(new Dimension(310, 220));
		backgroundPnl.add(pane);
		backgroundPnl.add(Box.createVerticalGlue());
		
		JButton continueButton = Util.Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/continue.png")), new ImageIcon(getClass().getResource("/game/images/buttons/continue_over.png")),
				"CONTINUE", this, true);
		backgroundPnl.add(continueButton);
		
		backgroundPnl.setBackground(Util.Util.getFadeBg());
		add(backgroundPnl);
		
		setAlwaysOnTop(true);
		setUndecorated(true);
		setAlpha(alpha);
		Util.AWTUtilities.setWindowOpaque(this, false);	
		
		timer = new Timer(0, this);
		timer.start();
		timer.setDelay(50); // Allow for smooth fade in and out.
	}	
	
	private void setAlpha(float alpha) {
	       Util.AWTUtilities.setWindowOpacity(this, alpha);
	}
	
	public void actionPerformed(ActionEvent e){
		if ( "CONTINUE".equals(e.getActionCommand()) )
			timer.start();
		else if ( fadein ){
			alpha += 0.10f;
			
		    if (alpha >= 1) {
		    	alpha = 1;
		    	timer.stop();
		    	fadeout = true;
		    	fadein = false;
		    }
		    setAlpha(alpha);
		} else if ( fadeout ){
			alpha += -0.33f;
			
		    if (alpha <= 0) {
		    	alpha = 0;
		    	timer.stop();
		    	dispose();
		    }
		    setAlpha(alpha);
		}
		
	}
}
