/*
 * WordMoleOptionPane.java
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

package Util;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class WordMoleOptionPane extends JDialog implements ActionListener{
	private static final int MESSAGE = 1;
	private static final int INPUT = 2;
	
	private int type;
	private JTextField inputField;
	private static String text;
	
	private WordMoleOptionPane(JFrame owner, Point location, String title, String message, int type){
		super(owner, true);
		setUpWindow(location, title, message, type);
	}
	
	private WordMoleOptionPane(JDialog owner, Point location, String title, String message, int type){
		super(owner, true);
		setUpWindow(location, title, message, type);
	}
	
	private void setUpWindow(Point location, String title, String message, int type){
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setUndecorated(true);
		AWTUtilities.setWindowOpaque(this, false);
		setSize(260, 120);
		setLocation(location.x+50, location.y+70);
		
		this.type = type;
		
		JPanel titlePnl = Util.createTitlePanel(title);
		add(titlePnl);
						
		JPanel backgroundPnl = new JPanel();
		backgroundPnl.setLayout(new BoxLayout(backgroundPnl, BoxLayout.Y_AXIS));
		backgroundPnl.setBackground(Util.getFadeBg());
		backgroundPnl.setOpaque(true);
		backgroundPnl.add(Box.createVerticalStrut(5)); // Top spacing
		
		JLabel msgLbl = Util.createLbl(message, Util.getGameFontSmall());
		backgroundPnl.add(msgLbl);
		backgroundPnl.add(Box.createVerticalStrut(10)); // Button spacing
		
		if ( type == 2 ){
			inputField = Util.createField();
			backgroundPnl.add(inputField);
			backgroundPnl.add(Box.createVerticalStrut(10)); // Button spacing
		} else
			backgroundPnl.add(Box.createVerticalStrut(30)); // Spacing for header
			
		JButton okBtn = Util.createBtn(new ImageIcon(getClass().getResource("/game/images/buttons/ok.png")), 
										new ImageIcon(getClass().getResource("/game/images/buttons/ok_over.png")),
										"OK", this, true);
		getRootPane().setDefaultButton(okBtn);
		
		backgroundPnl.add(okBtn);
		backgroundPnl.add(Box.createVerticalStrut(5)); // Bottom spacing
		
		add(backgroundPnl);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if ( "OK".equals(e.getActionCommand()) ){
			if ( type == INPUT ){
				text = inputField.getText().trim();
			}
			dispose();
		}
	}

	/**
	 * 
	 * @param owner
	 * @param location
	 * @param title
	 * @param message
	 */
	public static void showMessageDialog(Window owner, Point location, String title, String message){
		if ( owner instanceof JDialog )
			new WordMoleOptionPane((JDialog)owner, location, title, message, MESSAGE);
		else
			new WordMoleOptionPane((JFrame)owner, location, title, message, MESSAGE);
	}

	/**
	 * 
	 * @param owner
	 * @param location
	 * @param title
	 * @param message
	 */
	public static String showInputDialog(Window owner, Point location, String title, String message){
		if ( owner instanceof JDialog )
			new WordMoleOptionPane((JDialog)owner, location, title, message, INPUT);
		else
			new WordMoleOptionPane((JFrame)owner, location, title, message, INPUT);
		return text;
	}
}
