/*
 * Stopwatch.java
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.text.NumberFormat;

import javax.swing.Timer;

import Util.board.Board;


/**
 * Stopwatch for the {@link game.singleplayer.SingleplayerGameFrame} 
 * and {@link game.multiplayer.interfaces.MultiplayerGameFrame}
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class Stopwatch extends BackgroundLabel{
	private final int ONE_SECOND = 1000;
	private final int TWO_MIN = 1000*60*2;
	
	private int vegTimer = 0;
	private boolean vegUp = false;
	
	private long remaining;
	
	private boolean isPaused = false;
	
	private NumberFormat format;
	
	private Timer timer;
	private Board board;
	
	private GameState state;
	
	/**
	 * Constructor
	 * @param img - <code>ImageIcon</code> for the clock, found at {@link game.images.backgrounds.clock.png}
	 * @param state - <code>GameState</code> whether or not the game is in single or multi player mode.
	 */
	public Stopwatch(ImageIcon img, GameState state){
		super(img, "", 0);
		setOpaque(true);
		this.state = state;
		
		if ( state.equals(GameState.SINGLE) ){
			setText("02:00");
			remaining = TWO_MIN;
		}
		else{
			setText("00:00");
			remaining = 0;
		}
		format = NumberFormat.getNumberInstance();
		format.setMinimumIntegerDigits(2);
	}
	
	/**
	 * Starts the clock.
	 */
	public void start(){
		timer = new Timer(ONE_SECOND, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if ( !isPaused ){
					if ( state.equals(GameState.SINGLE) )
						remaining -= ONE_SECOND;
					else
						remaining += ONE_SECOND;
					updateLabel();
						if ( vegUp )
							startVegTimer(board);
						
					if ( remaining == 0 && state.equals(GameState.SINGLE) ){
						timer.stop();
						Stopwatch.this.firePropertyChange("level over", false, true);
					}
				}
			}
		});
		timer.start();
	}
	
	/**
	 * Resumes the clock.
	 */
	public void resume(){
		isPaused = false;
	}
	
	/**
	 * Pauses the clock.
	 */
	public void pause(){
		isPaused = true;
	}
	
	/**
	 * Stops and resets the Vegetable from board if there is one.
	 */
	public void stop(){
		if ( vegUp ){
			board.removeVegetable();
			vegUp = false;
			vegTimer = 0;
		}
		setForeground(Color.WHITE); // Just in case.
		timer.stop();
	}
	
	/**
	 * Resets the clock.
	 */
	public void reset(){
		setText("02:00");
		remaining = TWO_MIN;
	}
	
	/**
	 *  Updates the label to the newest time click. 
	 */
	private void updateLabel(){
		int minutes = (int)(remaining/60000);
		int seconds = (int)((remaining%60000)/1000);
		
		if ( getForeground().equals(Color.WHITE) && state.equals(GameState.SINGLE) ){
			if ( seconds <= 10 && minutes == 0 ){
					setForeground(Color.RED);
			} else if ( seconds <= 25 && minutes == 0 ){
					setForeground(Color.YELLOW);
			} 
		} else
			setForeground(Color.WHITE);
		
		setText(format.format(minutes) + ":" + format.format(seconds));
	}
	
	/**
	 * Starts the Vegetable time for the {@link Util.board.Board}
	 * @param board - <code>Board</code> containing the board with the Vegetable.
	 */
	public void startVegTimer(Board board){
		vegUp = true;
		this.board = board;
		
		if ( vegTimer == 0 )
			vegTimer = 21;
		
		if ( vegTimer > 0 ){
			if ( --vegTimer == 0 ){
				board.removeVegetable();
				vegUp = false;
			}
		}
	}
	
	/**
	 * Stops the Vegetable timer.
	 */
	public void stopVegTimer(){
		vegUp = false;
		vegTimer = 0;
	}
	
	/**
	 * Returns the current time on the clock.
	 * @return - <code>long</code> remaining time on clock.
	 */
	public long getTime(){
		return remaining;
	}
	
	/**
	 * Sets the time for the clock.
	 * @param min - <code>int</code> minutes.
	 * @param sec - <code>int</code> seconds.
	 */
	public void setTime(int min, int sec){
		setText(format.format(min) + ":" + format.format(sec));
	}
}
