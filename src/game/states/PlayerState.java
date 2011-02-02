/*
 * PlayerState.java
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
package game.states;
import java.io.Serializable;

/**
 * PlayerState contains the possible states of the Client when connected to the server. <br/>
 * <b>DISCONNECTED</b>: the client is no longer connected to the server. <br/>
 * <b>CONNECTED</b>: the client is connected and able to receive invites from other players. <br/>
 * <b>WAITING</b>: the client has an invite and is waiting to start a game. <br/>
 * <b>PLAYING</b>: the client is in a game and is not able to receive nor give invites.
 * @author Chris Barton
 *
 */
public enum PlayerState implements Serializable{
	DISCONNECTED{
		public String toString(){
			return "Disconnected";
		}
	},
	CONNECTED{
		public String toString(){
			return "Connected";
		} 
	},
	WAITING{
		public String toString(){
			return "Waiting";
		}
	},
	PLAYING{
		public String toString(){
			return "Playing";
		}
	}
}
