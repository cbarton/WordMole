/*
 * InvitationWindow.java
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
import game.components.WordMoleList;
import game.components.WordMoleScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Window for displaying the Invite when one is received or delivered.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public abstract class InvitationWindow extends JDialog implements ActionListener{	
	private JLabel invitationLbl;
	protected JList playerList; 
	private JPanel buttonPnl;
	
	protected boolean answer; 
	
	protected String imgPth = "/game/images/buttons/multiplayer/";
	
	/**
	 * Consructor
	 * @param owner - <code>JFrame</code> for the owner of the window.
	 * @param modal - <code>boolean</code> whether modal or not.
	 */
	public InvitationWindow(JFrame owner, boolean modal){
		super(owner, modal);
		setSize(new Dimension(250, 200));
		setLocation(owner.getLocationOnScreen().x+owner.getWidth()/2-125, owner.getLocationOnScreen().y+owner.getHeight()/2-240);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setUndecorated(true);
		getContentPane().setBackground(Color.BLACK);
	}
	
	/**
	 * Sets up the top most label in the window.
	 * @param text - <code>String</code> for the text of the label.
	 */
	protected void setInvitationLbl(String text){
		invitationLbl = Util.Util.createLbl(text, Util.Util.getGameFontSmall());
	}
	
	/**
	 * Sets up the WordMoleList for the invitees.
	 * @param list - <code>Object[]</code> content in the list.
	 */
	protected void setPlayerList(Object[] list){
		playerList = new WordMoleList(this, list); 
		playerList.setBackground(Color.BLACK);
		playerList.setMinimumSize(new Dimension(200, 100));
		playerList.setPreferredSize(new Dimension(200, 100));
		playerList.setMaximumSize(new Dimension(200, 100));
	}
	
	/**
	 * Sets up the buttons on the window.
	 * @param buttons <code>JButton...</code> containing all of the buttons wished to be put in frame.
	 */
	protected void setButtons(JButton... buttons ){
		buttonPnl = Util.Util.createPnl(BoxLayout.X_AXIS);
	
		for ( JButton button : buttons ){
			buttonPnl.add(Box.createHorizontalStrut(5)); // Cushion space
			buttonPnl.add(button);
		}
	}
	
	/**
	 * Builds the window and places all components on it.
	 * @return <code>boolean</code> specifying if the build was successful or not.
	 */
	protected boolean build(){
		if ( invitationLbl != null && playerList != null && buttonPnl != null ){
			add(Util.Util.createTitlePanel("Game Invitation"));
			add(invitationLbl);
			add(new WordMoleScrollPane(playerList, null, null));
			add(buttonPnl);
			return true;
		} else
			return false;
	}
	
	public abstract void actionPerformed(ActionEvent e); 
}
