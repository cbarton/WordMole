/*
 * MultiplayerGameFrame.java
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
package game.multiplayer;

import game.components.BackgroundLabel;
import game.components.BackgroundPanel;
import game.components.PauseScreen;

import game.components.Stopwatch;
import game.components.Watercan;
import game.multiplayer.components.PlayerScorebar;
import game.multiplayer.containers.Client;
import game.multiplayer.interfaces.Game;
import game.multiplayer.interfaces.MultiplayerGameFrame;
import game.states.GameState;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Util.DictionaryTree;
import Util.Util;
import Util.WordMoleOptionPane;
import Util.board.Board;

/**
 * MultiplayerGameFrame is the player game frame for the multiplayer part of Word Mole.  
 * The objective is to beat your opponents to the score to win.
 * @author Chris Barton
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MultiplayerGameFrameImpl extends JFrame implements ActionListener, MultiplayerGameFrame{
	private transient Board board;
	private Stopwatch stopwatch;

	private JButton submitButton;
	
	private JLabel scoreLbl;
	private JLabel nameLbl;
	private JLabel currentWordLbl;
	
	private PlayerScorebar [] playerProgress;
	private Hashtable<Client, PlayerScorebar> playerTable; // keeps track of the players scorebar to update it
	private PlayerScorebar myScorebar;
	private Game game;
	private Client client;
	
	private Watercan waterProgress;
	
	private final int BEGINNING_LVL = 100;
	private final int WATER = 65;
			
	private int level_score = 0;
	private int water_score = 0;
	
	/**
	 * Constructs a MultiplayerGameFrame
	 * @param location - <code>Point</code> containing the upper leftmost point of the parent window.
	 * @param dt - <code>DictionaryTree</code> containing the dictionary.
	 * @param game - <code>game.multiplayer.game.Game</code> containing the Game Server.
	 * @param myClient - <code>Client</code> containing this client.
	 */
	public MultiplayerGameFrameImpl(Point location, DictionaryTree dt, final Game game, Client myClient){
		super("Word Mole Multiplayer");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(Util.getGameSize());
		setLocation(location);
		
		this.game = game;
		client = myClient;
		
		/// ----------------------- SET UP THE NORTH PANEL ---------------------------///
		JPanel northPnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/game/images/backgrounds/north.png")));
		northPnl.setLayout(new BoxLayout(northPnl, BoxLayout.X_AXIS));
		
		stopwatch = new Stopwatch(new ImageIcon(getClass().getResource("/game/images/backgrounds/clock.png")), GameState.MULTI);
		
		currentWordLbl = new JLabel(); 
		currentWordLbl.setForeground(Color.YELLOW);
		currentWordLbl.setFont(Util.getGameFont());
		
		northPnl.add(stopwatch);
		northPnl.add(Box.createHorizontalStrut(20)); // Distance between the stopwatch and the label.
		northPnl.add(currentWordLbl);
		
		add(northPnl);
		
		/// ----------------------- SET UP THE BOARD ---------------------------///
		board = new Board(dt, new ImageIcon(getClass().getResource("/game/images/backgrounds/center.png")));
		board.setVisible(false);
		board.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent e){
				String property = e.getPropertyName();
				if ( "selected".equals(property) ){
					currentWordLbl.setText((String)e.getNewValue());
				} else if ( "veg".equals(property) ){
					stopwatch.stopVegTimer();
				}
			}
		});
		add(board);
		
		/// ----------------------- SET UP THE SOUTH PANEL ---------------------------///
			/// -------------- SET UP THE SUBMIT PANEL -----------------///
		JPanel submitPnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/game/images/backgrounds/southupper.png")));
		submitPnl.setLayout(new BoxLayout(submitPnl, BoxLayout.X_AXIS) );
		
		submitButton = Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/submit.png")), new ImageIcon(getClass().getResource("/game/images/buttons/over.png")), 
				"SUBMIT", this, false);
		submitButton.setEnabled(false);
		
		game.multiplayer.containers.Game thisGame = null;
		try {
			thisGame = game.getGame();
		} catch (RemoteException e1) {
			WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
			dispose();
		}
		int num_players= thisGame.getNumPlayers();
		Client [] players = thisGame.getPlayers();
		playerProgress = new PlayerScorebar[num_players];
		playerTable = new Hashtable<Client, PlayerScorebar>();
		
		waterProgress = new Watercan(0, WATER);
		
		submitPnl.add(Box.createHorizontalStrut(7)); // Distance from left end to placement of can.
		submitPnl.add(waterProgress);
		submitPnl.add(Box.createHorizontalStrut(8)); // Distance from can to placement of the bar(s).
		
		// Init and add the players bars to the frame
		for ( int i = 0; i < num_players; i++ ){
			Client user = players[i];
			playerProgress[i] = new PlayerScorebar(0, BEGINNING_LVL, user.name);
			submitPnl.add(playerProgress[i]);
			submitPnl.add(Box.createHorizontalStrut(1)); // Distance between bars.
			playerTable.put(user, playerProgress[i]); // Add to the table
		}
		
		myScorebar = playerTable.get(myClient);
		
		submitPnl.add(Box.createHorizontalGlue()); // Distance from the bar to the placement of the submit button.
		submitPnl.add(submitButton);
		add(submitPnl);
				
			/// -------------- SET UP THE SCORE PANEL -----------------///
		JPanel scorePnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/game/images/backgrounds/southlower.png")));
		scorePnl.setLayout(new BoxLayout(scorePnl, BoxLayout.X_AXIS));
		
		scoreLbl = new BackgroundLabel(new ImageIcon(getClass().getResource("/game/images/labels/score.png")), Integer.toString(myScorebar.getValue()) + "/" + BEGINNING_LVL, 3);
		nameLbl = new BackgroundLabel(new ImageIcon(getClass().getResource("/game/images/labels/overall_score.png")), myClient.name, 4);
		
		scorePnl.add(scoreLbl);
		scorePnl.add(Box.createHorizontalStrut(50)); // Distance from the score to the placement of the overall score.
		scorePnl.add(nameLbl);
		
		add(scorePnl);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				try {
					game.register(client, (MultiplayerGameFrame)MultiplayerGameFrameImpl.this, false);
				} catch (RemoteException e1) {
					WordMoleOptionPane.showMessageDialog(MultiplayerGameFrameImpl.this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
					dispose();
				}
			}
		});
		
		
		board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ESCAPE"), "pause");
		board.getActionMap().put("pause", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				pauseGame(true);
				new PauseScreen(MultiplayerGameFrameImpl.this, board.getLocationOnScreen(), GameState.MULTI).addWindowListener(new WindowAdapter(){
					public void windowClosed(WindowEvent e){
						pauseGame(false);
					}
				});
			}
		});
				
		setVisible(true);
		try {
			UnicastRemoteObject.exportObject(this);
			game.register(myClient, this, true);
		} catch (RemoteException e1) {
			WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
			dispose();
		}
	}
		
	public void actionPerformed(ActionEvent e){
		if ( "SUBMIT".equals(e.getActionCommand()) ){
			try{
				int score = board.checkWord();
				
				if ( score > 0 ){
					level_score += score;
					water_score += score;
					
					// Update the labels and progress bars
					scoreLbl.setText(Integer.toString(level_score) + "/" + BEGINNING_LVL);
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							waterProgress.setValue(water_score);
						}
					});
					// Send the score off.
					game.updateScore(client, level_score);
					board.manageHoles(true);
					board.cleanup();
				} 
				if ( level_score >= myScorebar.getMaximum() ){
					game.winGame(client);
				}
				if ( water_score >= waterProgress.getMaximum() && score > 0 ) // Can't have a vegetable grow when level is over.
					goVegetable(water_score-waterProgress.getMaximum());
			} catch ( RemoteException e1 ){
				WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
				dispose();
			}
		} 			
	}
	
	/**
	 * Initiates a vegetable on the board.
	 * @param leftover - int - The remaining score left in water_score.
	 */
	private void goVegetable(final int leftover){
		water_score = 0;
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				waterProgress.setValue(leftover);
			}
		});
		board.createVegetable();
		stopwatch.startVegTimer(board);
	}
	
	public void startGame() throws RemoteException{
		changeGameStatus(true);
	}
	
	public void notifyWinner(final Client winner) throws RemoteException{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				changeGameStatus(false);
				WordMoleOptionPane.showMessageDialog(MultiplayerGameFrameImpl.this, getLocationOnScreen(), "Game Over", winner.name + " wins!");
				dispose();
			}
		});
	}
	
	/**
	 * Pauses the current MultiplayerGameFrame.
	 * @param isPaused - boolean => true - Paused
	 * 							 => false - Resumed.
	 */
	private void pauseGame(boolean isPaused){
		board.setVisible(!isPaused);
		submitButton.setEnabled(!isPaused);
	}
	
	public synchronized void updateScore(final Client player, final int score) throws RemoteException{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				playerTable.get(player).setValue(score);
			}
		});
	}
	
	public synchronized void disconnectPlayer(Client player) throws RemoteException{
		playerTable.get(player).setVisible(false);
	}
	
	/**
	 * Sets up the critical components of the game that submit to disallow compromises.
	 * @param gameGoing - boolean => true - Everything works.
	 * 							  => false - Disable.
	 */
	private void changeGameStatus(boolean gameGoing){
		submitButton.setEnabled(gameGoing);
		board.setVisible(gameGoing);
		if ( gameGoing )
			stopwatch.start();
		else
			stopwatch.stop();
	}
	
	public int hashCode(){
		return client.name.hashCode();
	}
	
	public boolean equals(Object obj){
		if ( obj instanceof MultiplayerGameFrame )
			return ((MultiplayerGameFrame)obj).hashCode() == hashCode();
		else
			return false;
	}
}
