/*
 * Game.java
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
package game.multiplayer.containers;
import java.io.Serializable;

/**
 * Game container class for the multiplayer
 * @author Chris Barton
 *
 */
public class Game implements Serializable{
	private static final long serialVersionUID = -1093243983239756310L;
	private Client[] players;
	
	/**
	 * Constructor
	 * @param players - Client[] - The players playing in the game.
	 */
	public Game(Client[] players){
		this.players = players.clone();
	}
	
	/**
	 * Gets the game players.
	 * @return players - Client []
	 */
	public Client[] getPlayers(){
		return players;
	}
	
	/**
	 * Gets the number of players in the game.
	 * @return int
	 */
	public int getNumPlayers(){
		return players.length;
	}
}
