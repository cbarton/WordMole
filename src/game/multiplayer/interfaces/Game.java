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

package game.multiplayer.interfaces;

import game.multiplayer.containers.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface represents the Game of the Remote Interface for the 
 * {@link game.multiplayer.interfaces.WordMoleServer}.
 * @author Chris Barton
 */
public interface Game extends Remote {
	/**
	 * Registers a client that joins or leaves the Game.
	 * @param client - Client - Client in action.
	 * @param {@link game.multiplayer.interfaces.mutliplayer.MultiplayerGameFrame}
	 * @param connected - boolean => true - connected.
	 * 							  => false - disconnected.
	 * @throws RemoteException
	 */
	public void register(Client client, MultiplayerGameFrame mult, boolean connected) throws RemoteException;
	
	/**
	 * Updates the score of a client.
	 * @param client - Client - Client that scored.
	 * @param score - int - Current score of the client.
	 * @throws RemoteException
	 */
	public void updateScore(Client client, int score) throws RemoteException;
	
	/**
	 * Ends the Game and notifies the clients of the winner.
	 * @param winner - Client - Client that won.
	 * @throws RemoteException
	 */
	public void winGame(Client winner) throws RemoteException;
	
	/**
	 * Returns the current Game
	 * @return Game
	 * @throws RemoteException
	 */
	public game.multiplayer.containers.Game getGame() throws RemoteException;
}
