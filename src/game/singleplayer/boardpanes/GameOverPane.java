/*
 * GameOverPane.java
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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GameOverPane is the overlay for the {@link game.singleplayer.SingleplayerGameFrame} ending the game.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class GameOverPane extends GameOverlayPane implements ActionListener{
	private Timer timer;
	private float alpha = 0f;
	
	/**
	 * Constructor
	 * @param width <code>int</code> for the width of the board.
	 * @param height <code>int</code> for the height of the board.
	 * @param location <code>Point</code> for the location of the board.
	 * @param score <code>int</code> for the final score of the game.
	 * @param wordsFound <code>int</code> for the number of words found in the game.
	 */
	public GameOverPane(int width, int height, Point location, int score, int wordsFound){
		JPanel backgroundPnl = new JPanel();
		backgroundPnl.setLayout(new BoxLayout(backgroundPnl, BoxLayout.Y_AXIS));
				
		JLabel overLbl = Util.Util.createLbl("GAME OVER", Util.Util.getNormalFont());
		backgroundPnl.add(overLbl);
		backgroundPnl.add(Box.createVerticalGlue());
		
		JLabel scoreLbl = Util.Util.createLbl("Final Score: " + score, Util.Util.getGameFont());
		backgroundPnl.add(scoreLbl);
		
		JLabel wordsLbl = Util.Util.createLbl("Words Found: " + wordsFound, Util.Util.getGameFont());
		backgroundPnl.add(wordsLbl);
		backgroundPnl.add(Box.createVerticalGlue());
		
		JButton exitButton = Util.Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/exit_over.png")), null, "EXIT", this, true);
		backgroundPnl.add(exitButton);
		
		backgroundPnl.setBackground(Util.Util.getFadeBg());
		backgroundPnl.setOpaque(true);
		add(backgroundPnl);
		
		setUndecorated(true);
		setSize(new Dimension(width, height));
		setLocation(location);
		setAlwaysOnTop(true);
		setAlpha(alpha);
		Util.AWTUtilities.setWindowOpaque(this, false);
		
		timer = new Timer(0, this);
		setVisible(true);
		timer.start();
		timer.setDelay(50); // Allow for smooth fade in.
	}
	
	public void actionPerformed(ActionEvent e){
		if ( "EXIT".equals(e.getActionCommand()) )
			dispose();
		else{
			alpha += 0.10f;
		
		    if (alpha >= 1) {
		    	alpha = 1;
		    	timer.stop();
		    }
		    setAlpha(alpha);
		}
	}
	
	
}
