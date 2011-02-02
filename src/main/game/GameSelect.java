/*
 * GameSelect.java
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
package main.game;
import game.components.WordMoleScrollPane;
import game.components.WordMoleTextArea;
import game.multiplayer.MultiplayerGameFrameImpl;
import game.multiplayer.WordMoleClientImpl;
import game.multiplayer.interfaces.Game;
import game.singleplayer.SingleplayerGameFrame;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Util.AWTUtilities;
import Util.DictionaryTree;

/**
 * GameSelect is the window that allows the player to pick which mode of Word Mole they wish to play.<br/>
 * Singleplayer, multiplayer, or practice mode.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class GameSelect extends JDialog implements ActionListener{
	private final String singleDesc = "Play against the\nclock, completing as\nmany levels as\npossible.";
	private final String multDesc = "Compete against yourfriends in one or\nmore configurable\nrounds of Word Mole.";
	private final String pracDesc = "Enhance your skills by playing the game without any time\nlimits.";
	
	private WordMoleTextArea infoArea;
	private JFrame owner;
	private DictionaryTree dt;
	
	/**
	 * Constructor
	 * @param owner - <code>JFrame</code> owner of this window.
	 * @param dt - <code>DictionaryTree</code> contains the dictionary.
	 */
	public GameSelect(JFrame owner, DictionaryTree dt){
		super(owner, true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setUndecorated(true);
		setSize(320, 205);
		setLocation((int)owner.getLocationOnScreen().getX()+20, (int)owner.getLocationOnScreen().getY()+60);
		
		this.owner = owner;
		this.dt = dt;
		
		JPanel northPnl = Util.Util.createTitlePanel("Play");
		add(northPnl);
		
		JButton singleBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/main/images/buttons/singleplayer.png")), new ImageIcon(getClass().getResource("/main/images/buttons/singleplayer_over.png")),
				"SINGLE", this, false);
		singleBtn.addMouseListener(new ButtonAdapter(singleBtn));
		JButton multBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/main/images/buttons/multiplayer.png")), new ImageIcon(getClass().getResource("/main/images/buttons/multiplayer_over.png")),
				"MULTI", this, false);
		multBtn.addMouseListener(new ButtonAdapter(multBtn));
		JButton practiceBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource("/main/images/buttons/practice.png")), new ImageIcon(getClass().getResource("/main/images/buttons/practice_over.png")),
				"PRACTICE", this, false);
		practiceBtn.addMouseListener(new ButtonAdapter(practiceBtn));
		
		practiceBtn.setEnabled(false);
		
		JPanel southPnl = new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				setOpaque(true);
			}
		};
		southPnl.setLayout(new BoxLayout(southPnl, BoxLayout.X_AXIS));
		southPnl.setBackground(Util.Util.getFadeBg());
		
		JPanel westPnl = new JPanel(){;
			public void paintComponent(Graphics g){
				setOpaque(false);
			}
		};
		westPnl.setLayout(new BoxLayout(westPnl, BoxLayout.Y_AXIS));
		westPnl.add(Box.createVerticalStrut(5)); // Spacing from top.
		westPnl.add(singleBtn);
		westPnl.add(Box.createVerticalStrut(10));
		westPnl.add(multBtn);
		westPnl.add(Box.createVerticalStrut(10));
		westPnl.add(practiceBtn);
		westPnl.add(Box.createVerticalStrut(5)); // Spacing from bottom.
		southPnl.add(Box.createHorizontalStrut(5)); // Spacing from side.
		
		southPnl.add(westPnl);
		
		WordMoleScrollPane infoPane = setupInfoPane();

		southPnl.add(infoPane);
		add(southPnl);
		AWTUtilities.setWindowOpaque(this, false);
		
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ESCAPE"), "back");
		getRootPane().getActionMap().put("back", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	public void actionPerformed(ActionEvent e){
		if ( "SINGLE".equals(e.getActionCommand()) ){
			SingleplayerGameFrame sp = new SingleplayerGameFrame(owner.getLocationOnScreen(), dt);
			dispose();
			owner.setVisible(false);
			sp.addWindowListener(new WindowAdapter(){
				public void windowClosed(WindowEvent e){
					owner.setVisible(true);
				}
			});
		} else if ( "MULTI".equals(e.getActionCommand()) ){
			final Point loc = owner.getLocationOnScreen();
			final WordMoleClientImpl wm = new WordMoleClientImpl(loc);
			wm.setSize(new Dimension(360, 535));	
			dispose();
			owner.setVisible(false);
			wm.setVisible(true);
			wm.addWindowListener(new WindowAdapter(){
				public void windowClosed(WindowEvent e){
					Game game;
					if ( (game = wm.getGame()) == null ){
						owner.setVisible(true);
					}else {
						final MultiplayerGameFrameImpl mg = new MultiplayerGameFrameImpl(loc, dt, game, wm.getClient());
						mg.addWindowListener(new WindowAdapter(){
							public void windowClosed(WindowEvent e){
								wm.pack();
								wm.setVisible(true);
								wm.reconnect();
							}
						});
					}
				}
			});
			
			wm.connect();
		
		} else if ( "PRACTICE".equals(e.getActionCommand()) ){
			
		}
	}
	
	/**
	 * Sets up the info pane which contains all of the information for each mode.
	 * @return - <code>WordMoleScrollPane</code> with the WordMoleTextArea in it for the info.
	 */
	private WordMoleScrollPane setupInfoPane(){
		infoArea = new WordMoleTextArea(Color.WHITE, Color.BLACK, 0, 0);
		WordMoleScrollPane pane = new WordMoleScrollPane(infoArea, Color.WHITE, null);
		pane.setSize(new Dimension(161, 189));
		return pane;
	}
	
	/**
	 * ButtonAdapter class for the GameSelect. <br/>
	 * This class is used for when the player hovers over a specific button it changes
	 * the information in the infoArea.
	 * @author Chris Barton
	 *
	 */
	private class ButtonAdapter extends MouseAdapter{
		private String pnlText;
		
		public ButtonAdapter(JButton n){
			if ( n.getActionCommand().equals("SINGLE") )
				pnlText = singleDesc;
			else if ( n.getActionCommand().equals("MULTI") )
				pnlText = multDesc;
			else if ( n.getActionCommand().equals("PRACTICE") )
				pnlText = pracDesc;
		}
		
		public void mouseEntered(MouseEvent e){
			infoArea.append(pnlText);
			GameSelect.this.repaint(); // Keep opaqueness
		}
		
		public void mouseExited(MouseEvent e){
			infoArea.setText(null);
			GameSelect.this.repaint();
		}
	}
}
