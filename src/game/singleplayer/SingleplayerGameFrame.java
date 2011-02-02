/*
 * SingleplayerGameFrame.java
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

package game.singleplayer;

import game.components.BackgroundLabel;
import game.components.BackgroundPanel;
import game.components.PauseScreen;
import game.components.Scorebar;
import game.components.Stopwatch;
import game.components.Watercan;
import game.singleplayer.boardpanes.GameOverPane;
import game.singleplayer.boardpanes.LevelSummaryPane;
import game.singleplayer.boardpanes.StartLevelPane;
import game.states.GameState;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import Util.AWTUtilities;
import Util.DictionaryTree;
import Util.Util;
import Util.board.Board;


/**
 * SingleplayerGameFrame is the playing game frame for the single player part of Word Mole.  
 * The purpose of the game is to collect as many words as you can to gather points to beat the current 
 * level before the clock runs out.  Get as far as you can.
 * @author Chris Barton
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SingleplayerGameFrame extends JFrame implements ActionListener{
	private Board board;
	private Stopwatch stopwatch;
	
	private boolean gamePaused = true;
	private boolean gameOver = false;

	private JButton submitButton;
	
	private JLabel levelLbl;
	private JLabel scoreLbl;
	private JLabel overallLbl;
	private JLabel currentWordLbl;
	
	private Scorebar levelProgress;
	private Watercan waterProgress;
	
	//private final int BONUS = 5;
	private final int BEGINNING_LVL = 30;
	private final int LVL_INCREASE = 10;
	private final int WATER = 50;
	
	private int current_level = 1;
	private ArrayList<String> level_words;
	
	private int level_score = 0;
	private int overall_score = 0;
	private int water_score = 0;
	private int overall_words = 0;
	
	private StartLevelPane newLevelPane;
	private GameOverPane gameOverPane;
	
	private float alpha = 0f;
	private Timer timer;
	
	/**
	 * Constructs an instance of the singleplayer.
	 * @param location - <code>Point</code> containing the upper left corner of its parent.
	 * @param dt - <code>DictionaryTree</code> containing the dictionary.
	 */
	public SingleplayerGameFrame(Point location, DictionaryTree dt){
		super("Word Mole");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(Util.getGameSize());
		setLocation(location);
				
		/// ----------------------- SET UP THE NORTH PANEL ---------------------------///
		JPanel northPnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/game/images/backgrounds/north.png")));
		northPnl.setLayout(new BoxLayout(northPnl, BoxLayout.X_AXIS));
		
		stopwatch = new Stopwatch(new ImageIcon(getClass().getResource("/game/images/backgrounds/clock.png")), GameState.SINGLE);
		stopwatch.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent e){
				String property = e.getPropertyName();
				if ( "level over".equals(property) ){
					gameOver();
				}
			}
		});
		
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
		
		levelProgress = new Scorebar(0, BEGINNING_LVL);
		waterProgress = new Watercan(0, WATER);
		
		submitPnl.add(Box.createHorizontalStrut(7)); // Distance from left end to placement of can.
		submitPnl.add(waterProgress);
		submitPnl.add(Box.createHorizontalStrut(8)); // Distance from can to placement of the bar.
		submitPnl.add(levelProgress);
		submitPnl.add(Box.createHorizontalStrut(8)); // Distance from the bar to the placement of the submit button.
		submitPnl.add(submitButton);
		add(submitPnl);
				
			/// -------------- SET UP THE SCORE PANEL -----------------///
		JPanel scorePnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/game/images/backgrounds/southlower.png")));
		scorePnl.setLayout(new BoxLayout(scorePnl, BoxLayout.X_AXIS));
		
		levelLbl = new BackgroundLabel(new ImageIcon(getClass().getResource("/game/images/labels/level.png")), Integer.toString(current_level), 2);
		scoreLbl = new BackgroundLabel(new ImageIcon(getClass().getResource("/game/images/labels/score.png")), Integer.toString(levelProgress.getValue()), 3);
		overallLbl = new BackgroundLabel(new ImageIcon(getClass().getResource("/game/images/labels/overall_score.png")),Integer.toString(overall_score), 4);
		
		scorePnl.add(levelLbl);
		scorePnl.add(Box.createHorizontalStrut(30)); // Distance from the level to the placement of the score.
		scorePnl.add(scoreLbl);
		scorePnl.add(Box.createHorizontalStrut(20)); // Distance from the score to the placement of the overall score.
		scorePnl.add(overallLbl);
		
		add(scorePnl);
		
		level_words = new ArrayList<String>();
		
		addComponentListener(new ComponentAdapter(){
			public void componentMoved(ComponentEvent e) {
				Point boardloc = board.getLocationOnScreen();
				if ( boardloc != null )
					updateLocation(boardloc);
			}
		});
		
		// Used to keep the pane on top
		//TODO fix this ish
		addWindowListener(new WindowAdapter(){
			public void windowIconified(WindowEvent e){
				minimizePane();
			}
			
			public void windowDeiconified(WindowEvent e){
				maximizePane();
			}
		});
		
		
		board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ESCAPE"), "pause");
		board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released BACK_SPACE"), "clear");
		board.getActionMap().put("pause", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				pauseGame(true);
				new PauseScreen(SingleplayerGameFrame.this, board.getLocationOnScreen(), GameState.SINGLE).addWindowListener(new WindowAdapter(){
					public void windowClosed(WindowEvent e){
						pauseGame(false);
					}
				});
			}
		});
		board.getActionMap().put("clear", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				board.cleanup();
			}
		});
		
		// Fade in
		timer = new Timer(0, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				alpha += 0.10f;
				
			    if (alpha >= 1) {
			    	alpha = 1;
			    	timer.stop();
			    	startNewLevel(current_level, BEGINNING_LVL);
			    }
			    
			    AWTUtilities.setWindowOpacity(SingleplayerGameFrame.this, alpha);
			}
		});
		timer.start();
		setVisible(true);
		timer.setDelay(50);
	}
	
	private void minimizePane(){
		if ( gameOverPane != null && gameOverPane.isVisible() )
			gameOverPane.setVisible(false);
		else if ( newLevelPane != null && newLevelPane.isVisible() )
			newLevelPane.setVisible(false);
	}
	
	private void maximizePane(){
		if ( gameOverPane != null && !gameOverPane.isVisible() )
			gameOverPane.setVisible(true); 
		else if ( newLevelPane != null && !newLevelPane.isVisible() )
			newLevelPane.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if ( "SUBMIT".equals(e.getActionCommand()) ){
			int score = board.checkWord();
			Depletion deplete = null;
			
			if ( score > 0 ){
				level_words.add(currentWordLbl.getText());
				level_score += score;
				water_score += score;
				overall_score += score;
				overall_words++;
				
				// Update the labels and progress bars
				scoreLbl.setText(Integer.toString(level_score));
				overallLbl.setText(Integer.toString(overall_score));
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						levelProgress.setValue(level_score);
						waterProgress.setValue(water_score);
					}
				});
				board.manageHoles(true);
				board.cleanup();
			} 
			if ( level_score >= levelProgress.getMaximum() ){
				deplete = new Depletion();
				deplete.execute(); 
			}
			if ( water_score >= waterProgress.getMaximum() && (deplete == null) && score > 0 ) // Can't have a vegetable grow when level is over.
				goVegetable(water_score-waterProgress.getMaximum());
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
	
	/**
	 * Ends the current game.
	 */
	private void gameOver(){
		gameOverPane = new GameOverPane(board.getWidth(), board.getHeight(), board.getLocationOnScreen(), overall_score, overall_words);
		gameOver = true;
		gameOverPane.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
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
		if ( isPaused ){
			board.setVisible(false);
			stopwatch.pause();
		} else{
			board.setVisible(true);
			stopwatch.resume();
		}
	}
	
	/**
	 * Depletion is the SwingWorker class to give the player 1 point per second left on the clock 
	 * at the end of the level and initiates the next level.
	 */
	private class Depletion extends SwingWorker<Void, Void>{
			private long time;
			private int seconds, minutes, level;
			
			public Void doInBackground(){
				try{
					submitButton.setEnabled(false);
					stopwatch.stop();
					time = stopwatch.getTime();
					do{
						minutes = (int)(time/60000);
						seconds = (int)((time%60000)/1000);
						stopwatch.setTime(minutes, seconds);
						time -= 1000;
						overallLbl.setText(Integer.toString(++overall_score));
						Thread.sleep(50); // for visiblity
					} while ( time >= 0 );
				} catch(InterruptedException ie){
					ie.printStackTrace();
				} 
				return null;
			}
			
			public void done(){
				board.setVisible(false);
				current_level++;
				level = levelProgress.getMaximum()+LVL_INCREASE;
				startNewLevel(current_level, level);
			}
	}
	
	/**
	 * Ends the current level and resets the board.
	 * @param nextLevel - <code>int</code> score for next level to be achieved.
	 */
	//TODO implement bonus levels
	private void endLevel(int nextLevel){
		stopwatch.reset();
		board.setVisible(true);
		submitButton.setEnabled(true);
		stopwatch.start();
		
		// Resetup GUI.
		//if ( level % BONUS == 0 ) bonusround!
		levelProgress.setMaximum(nextLevel);
		levelProgress.setValue(0);
		
		levelLbl.setText(Integer.toString(current_level));
		level_score = 0;
		scoreLbl.setText(Integer.toString(level_score));
		
		level_words = new ArrayList<String>();
		
		board.manageHoles(false);
		board.emptySquares();	
	}
	
	/**
	 * Starts a new level by showing the overlay pane of results.
	 * @param level - <code>int</code> current level.
	 * @param points - <code>int</code> current amount of points for this game.
	 */
	private void startNewLevel(int level, final int points){
		gamePaused = true;
		if ( level_words.isEmpty() ) // first level
			newLevelPane = new StartLevelPane(level, board.getWidth(), board.getHeight(), board.getLocationOnScreen(), null);
		else
			newLevelPane = new StartLevelPane(level, board.getWidth(), board.getHeight(), board.getLocationOnScreen(),
					new LevelSummaryPane(newLevelPane, level_words));
		newLevelPane.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				gamePaused = false;
				endLevel(points);
		}
		});
	}
	
	/**
	 * Updates the location of the board to update any overlaying panes that might be open.
	 * @param p - <code>Point</code> containing the new point for the upper left corner of the board.
	 */
	private void updateLocation(Point p){
		if ( gamePaused && newLevelPane != null )
			newLevelPane.updateLocation(p);
		else if ( gameOver && gameOverPane != null )
			gameOverPane.updateLocation(p);
	}
}
