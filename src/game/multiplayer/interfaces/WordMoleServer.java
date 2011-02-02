/*
 * WordMoleServer.java
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
import game.multiplayer.containers.Game;
import game.multiplayer.containers.Invite;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface represents the WordMoleServer of the Remote Interface for the 
 * WordMoleServer.
 * @author Chris Barton
 */
public interface WordMoleServer extends Remote {
	/**
	 * Registers a WordMoleClient with the WordMoleServer
	 * @param client - WordMoleClient to register
	 * @param name - client of the WordMoleClient
	 * @param connected - boolean of whether the client is connected or disconnected.
	 * @throws RemoteException
	 */
	public void register(WordMoleClient client, Client name, boolean connected) throws RemoteException;
	
	/**
	 * Reconnects a client from a game to the lobby.
	 * @param name - Client the client
	 * @throws RemoteException
	 */
	public void reregister(Client name) throws RemoteException;
	
	/**
	 * Creates an invite to be sent to all of the invitees.
	 * @param invite - Invite with all of the invitees.
	 * @return Invitation stub
	 * @throws RemoteException
	 */
	public Invitation createInvite(Invite invite) throws RemoteException;
	
	/**
	 * Creates a game to be initialized.
	 * @param game - The game to be started.
	 * @throws RemoteException
	 */
	public void createGame(Game game) throws RemoteException;
	
	/**
	 * Sends a message to all of the clients connected.
	 * @param sender - client of the sender.
	 * @param message - Message to be posted.
	 * @throws RemoteException
	 */
	public void postMessage(Client sender, String message) throws RemoteException;
}
