/*
 * Invite.java
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
 * Invite container class for the multiplayer.
 * @author Chris Barton
 *
 */
public class Invite implements Serializable{
	private static final long serialVersionUID = 5679323711653594934L;
	private Client host;
	private Client[] invitees;
	
	/**
	 * Constructor
	 * @param host - Client of the host
	 * @param invitees - Client [] of the invitees
	 */
	public Invite(Client host, Client[] invitees){
		this.host = host;
		this.invitees = invitees.clone();
	}
	
	/**
	 * Returns the number of invitees on the Invite
	 * @return int - Number of invitees
	 */
	public int getNumInvitees(){
		return invitees.length;
	}
	
	/**
	 * Returns the invitees.
	 * @return Client[] - The invitees
	 */
	public Client[] getInvitees(){
		return invitees;
	}

	/**
	 * Returns the host of the Invite.
	 * @return Client - The host.
	 */
	public Client getHost(){
		return host;
	}
}
