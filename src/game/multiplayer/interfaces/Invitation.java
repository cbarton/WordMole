/*
 * Invitation.java
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
import game.multiplayer.containers.Invite;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * This interface represents the Invitation of the Remote Interface for the 
 * {@link game.multiplayer.interfaces.WordMoleServer}.
 * @author Chris Barton
 */
public interface Invitation extends Remote {
	/**
	 * Notifies the host of the decision that an invited client has made to either accept of decline.
	 * @param client Client - Invitee client.
	 * @param decision boolean - Invitee decision for the invite.
	 * 						   -> true => Accepts Invite
	 * 						   -> false => Declines Invite
	 * @throws RemoteException
	 */
	public void makeDecision(Client client, boolean decision) throws RemoteException;
	
	/**
	 * Initializes the game to be created out of the invite and sends it to the server to be
	 * processed.
	 * @param initialize - boolean - Determines whether or not to start the game or cancel the invite.
	 * @throws RemoteException
	 */
	public void initializeGame(boolean initialize) throws RemoteException;
	
	/**
	 * Returns true if the Invitation is ready and all have accepted, else returns false.
	 * @return boolean true => ready; false => not ready.
	 */
	public boolean ready() throws RemoteException;
	
	/**
	 * Returns the current invite.
	 * @return Invite
	 * @throws RemoteException
	 */
	public Invite getInvite() throws RemoteException;
}
