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

package game.multiplayer.interfaces;

import game.multiplayer.containers.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface represents the MultiplayerGameFrame of the Remote Interface for the 
 * WordMoleClient.
 * @author Chris Barton
 */
public interface MultiplayerGameFrame extends Remote {
	/**
	 * Begins the current Game.
	 * @throws RemoteException
	 */
	public void startGame() throws RemoteException;
	
	/**
	 * Updates the client's score on the MultiplayerGameFrame
	 * @param client - Client - Client who needs updating.
	 * @param score - int - Current client's score
	 * @throws RemoteException
	 */
	public void updateScore(Client client, int score) throws RemoteException;
	
	/**
	 * Removes the client from the MultiplayerGameFrame window.
	 * @param client - Client - Client that disconnected.
	 * @throws RemoteException
	 */
	public void disconnectPlayer(Client client) throws RemoteException;
	
	/**
	 * Notifies the MultiplayerGameFrame of the winner and ends the current game.
	 * @param winner - Client - Winner of the game.
	 * @throws RemoteException
	 */
	public void notifyWinner(Client winner) throws RemoteException;
}
