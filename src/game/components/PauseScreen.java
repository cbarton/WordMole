/*
 * PauseScreen.java
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
import game.states.GameState;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.game.MainScreen;

/**
 * The window for the Pause Screen in the {@link game.singleplayer.SingleplayerGameFrame} 
 * and {@link game.multiplayer.interfaces.MultiplayerGameFrame}
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class PauseScreen extends JDialog implements ActionListener{
	
	/**
	 * Constructor 
	 * @param owner - <code>JFrame</code> containing the owner of this window.
	 * @param location - <code>Point</code> containing the upper left point of the owner's window.
	 * @param state - <code>GameState</code> whether or not the game is in single or multi player mode.
	 */
	public PauseScreen(JFrame owner, Point location, GameState state){
		super(owner, true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setUndecorated(true);
		Util.AWTUtilities.setWindowOpaque(this, false);
		setSize(260, 120);
		setLocation(location.x+50, location.y+60);
	
		// Set up the header
		JPanel headPnl;
		if ( state.equals(GameState.SINGLE) )
			headPnl = Util.Util.createTitlePanel("Game Paused");
		else
			headPnl = Util.Util.createTitlePanel("Options");
		add(headPnl);
		
		// Set up the rest
		JPanel backgroundPnl = new JPanel();
		backgroundPnl.setLayout(new BoxLayout(backgroundPnl, BoxLayout.Y_AXIS));
		backgroundPnl.setBackground(Util.Util.getFadeBg());
		backgroundPnl.setOpaque(true);
		backgroundPnl.add(Box.createVerticalStrut(5)); // Top spacing
		
		JButton resumeBtn, quitBtn, resume_mBtn, quit_mBtn;
		if ( state.equals(GameState.SINGLE) ){
			resumeBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/resume.png")), new ImageIcon(getClass().getResource("/game/images/buttons/resume_over.png")),
					"RESUME", this, true);
			quitBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/quit.png")), new ImageIcon(getClass().getResource("/game/images/buttons/quit_over.png")),
					"QUIT", this, true);
			backgroundPnl.add(resumeBtn);
			backgroundPnl.add(Box.createVerticalStrut(10)); // Button spacing
			backgroundPnl.add(quitBtn);
		} else{
			resume_mBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/multiplayer/resume.png")), new ImageIcon(getClass().getResource("/game/images/buttons/multiplayer/resume_over.png")),
					"RESUME", this, true);
			quit_mBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/multiplayer/give_up.png")), new ImageIcon(getClass().getResource("/game/images/buttons/multiplayer/give_up_over.png")),
					"QUIT", this, true);
			backgroundPnl.add(resume_mBtn);
			backgroundPnl.add(Box.createVerticalStrut(10)); // Button spacing
			backgroundPnl.add(quit_mBtn);
		}
		
		backgroundPnl.add(Box.createVerticalStrut(5)); // Bottom spacing
		add(backgroundPnl);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if ( "RESUME".equals(e.getActionCommand()) ){
			dispose();
		} else if ( "QUIT".equals(e.getActionCommand()) ){
			try {
				new MainScreen();
				getOwner().dispose();
			} catch (FileNotFoundException e1) {
				// Unchecked.
			}
		}
	}
}
