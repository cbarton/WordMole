/*
 * WordMoleClient.java
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
 * This interface represents the WordMoleClient of the Remote Interface for the 
 * WordMoleClient.
 * @author Chris Barton
 */
public interface WordMoleClient extends Remote {
	/**
	 * Updates the WordMoleClient's playerList to display the current other players
	 * states.
	 * @param clients - Client [] - List of clients.
	 * @throws RemoteException
	 */
	public void updateClientList(Client[] clients) throws RemoteException;	
	
	
	/**
	 * The WordMoleClient receives an Invite from another client.
	 * @param invite - Invite received.
	 * @throws RemoteException
	 */
	public void receiveInvite(Invitation invite) throws RemoteException;
	
	/**
	 * The WordMoleClient as a host of an Invite receives a decision from one of the 
	 * invitees.
	 * @param client - Client of the invitee.
	 * @param decision - Decision of the invitee.
	 * @throws RemoteException
	 */
	public void receiveInviteDecision(Client client, boolean decision) throws RemoteException;
	
	/**
	 * The host has cancelled the Invitation.
	 * @throws RemoteException
	 */
	public void cancelInvite() throws RemoteException;
	
	/**
	 * The WordMoleClient receives an Game from the server.
	 * @param game 
	 * @throws RemoteException
	 */
	public void receiveGame(Game game) throws RemoteException;
	
	/**
	 * Display's the message on the WordMoleClient.
	 * @param message - Message received.
	 * @throws RemoteException
	 */
	public void displayMessage(String message) throws RemoteException;
}
