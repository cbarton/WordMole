/*
 * InviteeInvitationWindow.java
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

package game.multiplayer.components.invitation;

import game.multiplayer.containers.Invite;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * InviteeInvitationWindow shows a window on the invitee's side of the invitation.  
 * It displays the other players and who the invite is hosted by.
 * @author Chris Barton
 *
 */

@SuppressWarnings("serial")
public class InviteeInvitationWindow extends InvitationWindow{
	
	/**
	 * Constructor
	 * @param owner - JFrame - The frame calling for the window.
	 * @param invite - Invite - The invite associated with the window.
	 */
	public InviteeInvitationWindow(JFrame owner, Invite invite) {
		super(owner, true);
		
		JButton yesBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "yes.png")), new ImageIcon(getClass().getResource(imgPth + "yes_over.png")), 
				"YES", this, false);
		JButton noBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "no.png")), new ImageIcon(getClass().getResource(imgPth + "no_over.png")), 
				"NO", this, false);
		
		setPlayerList(invite.getInvitees());
		
		setButtons(yesBtn, noBtn);
		
		setInvitationLbl(invite.getHost() + "'s Invitation:");
		
		build();
	}

	public void actionPerformed(ActionEvent e) {
		if ( "YES".equals(e.getActionCommand()) ){
			answer = true;
			dispose();
		}else if ( "NO".equals(e.getActionCommand()) ){
			answer = false;
			dispose();
		}
	}
	
	/**
	 * Cancels the particular invite.
	 */
	public void cancel(){
		dispose();
	}
	
	/**
	 * Returns the response from the invitee either accepting or declining the invite.
	 * @return boolean - Answer from the invite.
	 */
	public boolean getAnswer(){
		return answer;
	}
}
