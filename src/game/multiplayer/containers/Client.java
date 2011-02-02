/*
 * Client.java
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
import game.states.PlayerState;
import java.io.Serializable;

/**
 * Client container class for the multiplayer game.
 * @author Chris Barton
 *
 */
public class Client implements Serializable{
	private static final long serialVersionUID = -1347172270768966568L;
	public String name;
	public PlayerState state;
	
	/**
	 * Constructs a Client with with {@link game.states.PlayerState#CONNECTED}.
	 * @param name - <code>String</code> naming the Client.
	 */
	public Client(String name){
		this.name = name;
		this.state = PlayerState.CONNECTED;
	}
	
	/**
	 * Constructs a Client.
	 * @param name - <code>String</code> naming the Client.
	 * @param state - <code>PlayerState</code> setting the {@link #state} of the Client.
	 */
	public Client(String name, PlayerState state){
		this.name = name;
		this.state = state;
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object obj){
		if ( obj instanceof Client )
			return ((Client)obj).name.equals(name);
		else
			return false;
	}
	
	public String toString(){
		return name;
	}
}
