/*
 * WordMoleClientImpl.java
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
package game.multiplayer;

import game.components.WordMoleScrollPane;
import game.components.WordMoleTextArea;
import game.multiplayer.components.WordMoleClientList;
import game.multiplayer.components.invitation.HostInvitationWindow;
import game.multiplayer.components.invitation.InviteeInvitationWindow;
import game.multiplayer.containers.Client;
import game.multiplayer.containers.Invite;
import game.multiplayer.interfaces.Game;
import game.multiplayer.interfaces.Invitation;
import game.multiplayer.interfaces.WordMoleClient;
import game.multiplayer.interfaces.WordMoleServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Util.WordMoleOptionPane;

/**
 * This class represents the WordMoleClient Implementation of the Remote Interface for the 
 * WordMoleClient.
 * @author Chris Barton
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WordMoleClientImpl extends JFrame implements ActionListener, WordMoleClient{
	private Client client;
	private WordMoleClientList playerList;
	
	private WordMoleServer server;
	private Invitation inviteServer;
	
	private JTextField chatField;
	private WordMoleTextArea chatArea;
	
	private JButton sendBtn;
	
	private HostInvitationWindow hiw;
	private InviteeInvitationWindow iiw;
	
	private Game game;
	
	private String imgPth = "/game/images/buttons/multiplayer/";
	
	public WordMoleClientImpl(Point loc){
		super("Word Mole Multiplayer");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocation(loc);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.BLACK);
		
		String username;
		do {
			username = WordMoleOptionPane.showInputDialog(this, loc, "Select a Username", "Please Select a username:");
		} while ( username == null || username.equals("") );
		client = new Client(username);
		
		/** SETUP THE TITLE PANEL **/
		JPanel titlePnl = Util.Util.createTitlePanel("Multiplayer Game Setup");
		
		/** SETUP THE PLAYERS LIST **/
		playerList = new WordMoleClientList(this, new Object[]{});
		WordMoleScrollPane pane = new WordMoleScrollPane(playerList, null, null);
		pane.setSize(new Dimension(355, 185));
					
		chatArea = new WordMoleTextArea(Color.WHITE, Color.BLACK, 12, 12);
		
		/** SETUP THE CONVERSATION PANEL **/
		JPanel convoPnl = Util.Util.createPnl(BoxLayout.X_AXIS);
		convoPnl.setMaximumSize(new Dimension(355, 34));
		convoPnl.add(Util.Util.createLbl("Talk:", Util.Util.getGameFontSmall()));
		convoPnl.add(Box.createHorizontalGlue());
		
		chatField = Util.Util.createField();
		convoPnl.add(chatField);
		
		sendBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "send.png")), new ImageIcon(getClass().getResource(imgPth + "send_over.png")), 
				"SEND", this, false);
		convoPnl.add(Box.createHorizontalGlue());
		convoPnl.add(sendBtn);
		
		/** SETUP THE BUTTONS PANEL **/
		JPanel buttonPnl = Util.Util.createPnl(BoxLayout.X_AXIS);
		
		JButton inviteBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "invite.png")), new ImageIcon(getClass().getResource(imgPth + "invite_over.png")), 
				"INVITE", this, false);
		JButton setupBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "setup.png")), new ImageIcon(getClass().getResource(imgPth + "setup_over.png")), 
				"SETUP", this, false);
		JButton quitBtn = Util.Util.createBtn(new ImageIcon(getClass().getResource(imgPth + "quit.png")), new ImageIcon(getClass().getResource(imgPth + "quit_over.png")), 
				"QUIT", this, false);
		
		buttonPnl.add(inviteBtn);
		buttonPnl.add(Box.createHorizontalStrut(5)); // Cushion space
		buttonPnl.add(setupBtn);
		buttonPnl.add(Box.createHorizontalGlue());
		buttonPnl.add(quitBtn);
		
		add(titlePnl);
		add(pane);
		add(Box.createVerticalStrut(10));
		add(new WordMoleScrollPane(chatArea, Color.YELLOW, "Chat:"));
		add(Box.createVerticalStrut(10));
		add(convoPnl);
		add(Box.createVerticalStrut(10));
		add(buttonPnl);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				if ( server != null ){
					try {
						if ( inviteServer != null ){
							inviteServer.makeDecision(client, false);
							inviteServer = null;
						}
						if ( game == null){
							server.register(WordMoleClientImpl.this, client, false);
							server = null;
						}
					} catch (RemoteException e1) {
						WordMoleOptionPane.showMessageDialog(WordMoleClientImpl.this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
						connect();
					}
				}
			}
		});
		
		getRootPane().setDefaultButton(sendBtn);
		chatField.requestFocusInWindow();
			
		setSendables(false);
	}

	public void actionPerformed(ActionEvent e) {
		if ( "INVITE".equals(e.getActionCommand()) ){
			final Client [] players = playerList.getSelectedValues();
			
			for ( Client player : players ){
				if (player.state.ordinal() > 1){
					WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Bad Player Select", player.name + " is not available.");
					return;
				}
			}
			if ( players.length == 0 )
				WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "No Players", "No players selected");
			else if ( players.length >= 4 ) {
				WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Too Many Players", "Too many players selected");
		} else {
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					try{
						Invite invite = new Invite(client, players);
						inviteServer = server.createInvite(invite);
						hiw = new HostInvitationWindow(WordMoleClientImpl.this, invite);
						hiw.addWindowListener(new WindowAdapter(){
							public void windowClosed(WindowEvent e){
								try {
									if ( inviteServer != null ){
										inviteServer.initializeGame(hiw.getAnswer());
										inviteServer = null;
									}
								} catch (RemoteException e1) {
									WordMoleOptionPane.showMessageDialog(WordMoleClientImpl.this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
									connect();
								}
							}
						});
						hiw.setVisible(true);
					} catch (RemoteException e1){
						WordMoleOptionPane.showMessageDialog(WordMoleClientImpl.this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
						connect();
					}
				}
			});
		
			}
				
		} else if ( "SETUP".equals(e.getActionCommand()) ){
			// TODO
		} else if ( "QUIT".equals(e.getActionCommand()) ){
			if ( server != null ){
				try {
					if ( inviteServer != null )
						inviteServer.makeDecision(client, false);
					server.register(WordMoleClientImpl.this, client, false);
					server = null;
				} catch (RemoteException e1) {
					e1.printStackTrace();
				} 
			}
			dispose();
		} else if ( "SEND".equals(e.getActionCommand()) ){
			try{
				String message = chatField.getText().trim();
				if ( message.length() > 0 )
					server.postMessage(client, message);
				chatField.setText(null);
				chatField.requestFocus();
			} catch (RemoteException e1){
				WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
				connect();
			}
		}
	}
	
	/**
	 * Method to disable the functioning keys in the window until a connection is established.
	 * @param enabled - boolean => true - enable the buttons.
	 * 							=> false - disable the buttons.
	 */
	private void setSendables(boolean enabled){
		sendBtn.setEnabled(enabled);
		
		if ( !enabled ){
			chatArea.setText("");
			chatField.setText(null);
		}
	}
	
	/**
	 * Attempts to connect to the WordMoleServer
	 */
	public void connect(){
		try
			{
				if ( System.getSecurityManager() == null )
					System.setSecurityManager(new RMISecurityManager());
			
				server = (WordMoleServer)Naming.lookup("//cbarton.mine.nu:8888/WordMoleServer");
				UnicastRemoteObject.exportObject(this);
				server.register(this, client, true);
				setSendables(true);
			} catch (Exception e){ 
				e.printStackTrace();
				WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Server Not Online", "Server not online");
				dispose();
			} 
	}
	
	/**
	 * Reconnects to the server.
	 */
	public void reconnect(){
		try{
			playerList.setListData(new Object[]{});
			game = null;
			inviteServer = null; // just in case
			server.reregister(client);
		} catch (RemoteException re){
			WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
			connect();
		}
	}

	public void displayMessage(String message) throws RemoteException {
		chatArea.append(message);
	}

	public void receiveGame(Game game) throws RemoteException {
		this.game = game;
		dispose();
	}
	
	/**
	 * Returns the current game that the WordMoleClient is in.
	 * @return Game
	 */
	public Game getGame(){
		return game;
	}
	
	public void updateClientList(final Client [] clients) throws RemoteException{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				playerList.update(clients);		
			}
		});
	}
	
	public void receiveInvite(final Invitation invite) throws RemoteException {
		inviteServer = invite;
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try {
					iiw = new InviteeInvitationWindow(WordMoleClientImpl.this, invite.getInvite());
					iiw.addWindowListener(new WindowAdapter(){
						public void windowClosed(WindowEvent e){
							try {
								if ( inviteServer != null ){
									inviteServer.makeDecision(client, iiw.getAnswer());
									inviteServer = null;
								}
							} catch (RemoteException e1) {
								WordMoleOptionPane.showMessageDialog(WordMoleClientImpl.this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
								connect();
							}
						}
					});
					iiw.setVisible(true);
				} catch (RemoteException e) {
					WordMoleOptionPane.showMessageDialog(WordMoleClientImpl.this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
					connect();
				}		
			}
		});
	}

	public void receiveInviteDecision(final Client client, final boolean decision) throws RemoteException {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				hiw.updateList(client, decision);		
			}
		});
	}

	public void cancelInvite() throws RemoteException {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				inviteServer = null;
				WordMoleOptionPane.showMessageDialog(WordMoleClientImpl.this, getLocationOnScreen(), "Invite cancelled", "Host has cancelled invitation!");
				if ( iiw.isVisible() )
					iiw.cancel();
			}
		});
	}
	
	/**
	 * Determines if the invite out is ready or not.
	 * @return - boolean - If invite is ready or not.
	 */
	public boolean isInviteReady(){
		try {
			return inviteServer.ready();
		} catch (RemoteException e) {
			WordMoleOptionPane.showMessageDialog(this, getLocationOnScreen(), "Connection Lost", "Connection Lost");
			connect();
		}
		return false;
	}
	
	/**
	 * Returns this client.
	 * @return Client this client.
	 */
	public Client getClient(){
		return client;
	}
	
	public int hashCode(){
		return (client == null) ? super.hashCode() : client.hashCode();
	}
	
	public boolean equals(Object obj){
		if ( obj instanceof WordMoleClient ){
			return ((WordMoleClient)obj).toString().equals(client.name);
		} else
			return false;
	}
	
	public String toString(){
		return client.name;
	}
}