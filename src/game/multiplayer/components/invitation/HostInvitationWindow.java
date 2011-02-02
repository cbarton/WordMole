/*
 * HostInvitationWindow.java
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
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Hashtable;

import game.multiplayer.WordMoleClientImpl;
import game.multiplayer.containers.Client;
import game.multiplayer.containers.Invite;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import Util.WordMoleOptionPane;

/**
 * HostInvitationWindow is the host's display of the invite just sent out and contains whether
 * or not all of the invitees are ready or not.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class HostInvitationWindow extends InvitationWindow{
	private Hashtable<Client, Invitee> players = new Hashtable<Client, Invitee>();
	private WordMoleClientImpl owner;
	
	private String imgPath = "/game/images/labels/multiplayer/";
	private ImageIcon [] stateIcons = {new ImageIcon(getClass().getResource(imgPath + "red_checkmark.png")),
			new ImageIcon(getClass().getResource(imgPath + "checkmark.png"))
	};
	
	/**
	 * Constructor
	 * @param owner - <code>JFrame</code> owner of this window.
	 * @param invite - <code>Invite</code> current invite.
	 * @throws RemoteException
	 */
	public HostInvitationWindow(JFrame owner, Invite invite) throws RemoteException{
		super(owner, false);
		
		this.owner = (WordMoleClientImpl)owner;
	
		JButton startBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "start.png")), new ImageIcon(getClass().getResource(imgPth + "start_over.png")), 
				"START", this, false);
		JButton cancelBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "cancel.png")), new ImageIcon(getClass().getResource(imgPth + "cancel_over.png")), 
				"CANCEL", this, false);
		
		setInvitationLbl("Players invited:");
		setButtons(startBtn, cancelBtn);
		
		// Set up the special player list.
		Client [] clients = invite.getInvitees();
		for ( Client c : clients )
			players.put(c, new Invitee(c));
		setPlayerList(players.values().toArray());
		
		build();
		
		playerList.setCellRenderer(new ListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				
				String name = ((Invitee)value).client.name;
				int state = ((Invitee)value).accepted;
				JPanel bg = new JPanel();
				
				bg.setLayout(new BoxLayout(bg, BoxLayout.X_AXIS));
				bg.setBackground(Util.Util.getMenuGradient());
				
				if ( isSelected )
					bg.setOpaque(true);
				else{
					bg.setOpaque(false);
				}
				JLabel c = new JLabel(name);
				c.setAlignmentX(LEFT_ALIGNMENT);
				c.setForeground(Color.WHITE);
				c.setFont(Util.Util.getGameFont());					
				bg.add(c);	
				
				bg.add(Box.createHorizontalGlue());
				
				bg.add(new JLabel(stateIcons[state]));

				return bg;
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if ( "START".equals(e.getActionCommand()) ){
			if ( owner.isInviteReady() ){
				answer = true;
				dispose();
			} else
				WordMoleOptionPane.showMessageDialog((JDialog)this, getLocationOnScreen(), "Missing Players", "Not all players have accepted invitation!");
		} else if ( "CANCEL".equals(e.getActionCommand()) ){
			answer = false;
			dispose();
		}
	}
	
	/**
	 * Updates the list of invitees based on if they accept or decline.
	 * @param player - <code>Client</code> to update.
	 * @param accept - <code>boolean</code> true => accepted; false => declined.
	 */
	public void updateList(Client player, boolean accept){
		Invitee inv = players.get(player);
		
		if ( inv != null){
			if ( accept ){
				inv.accept(true);
			} else{
				inv.accept(false);
			}
			players.put(player, inv);
		}
		playerList.setListData(players.values().toArray());
	}
	
	/**
	 * Returns the answer from the host.
	 * @return true => start invite; false => cancel invite.
	 */
	public boolean getAnswer(){
		return answer;
	}
	
	private class Invitee{
		public Client client;
		public int accepted;
		
		public Invitee(Client c){
			client = c;
			accepted = 0;
		}
		
		public void accept(boolean a){
			if ( a )
				accepted = 1;
			else
				accepted = 0;
		}
	}
}
