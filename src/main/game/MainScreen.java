/*
 * MainScreen.java
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

package main.game;
import game.components.BackgroundPanel;
import game.components.ChangingButton;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Util.DictionaryTree;
import Util.Util;

/**
 * The main screen of Word Mole, allows the user to choose between creating a game, setting the options for the game or
 * getting help in using the game.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class MainScreen extends JFrame{
	private final ImageIcon playOverWest = new ImageIcon(getClass().getResource("/main/images/play_over/play_over_west.png"));
	private final ImageIcon optionsOverWest = new ImageIcon(getClass().getResource("/main/images/options_over/options_over_west.png"));
	private final ImageIcon helpOverWest = new ImageIcon(getClass().getResource("/main/images/help_over/help_over_west.png"));
	private final ImageIcon exitOverWest = new ImageIcon(getClass().getResource("/main/images/exit_over/exit_over_west.png"));
	
	private final ImageIcon playOverEast = new ImageIcon(getClass().getResource("/main/images/play_over/play_over_east.png"));
	private final ImageIcon optionsOverEast = new ImageIcon(getClass().getResource("/main/images/options_over/options_over_east.png"));
	private final ImageIcon helpOverEast = new ImageIcon(getClass().getResource("/main/images/help_over/help_over_east.png"));
	private final ImageIcon exitOverEast = new ImageIcon(getClass().getResource("/main/images/exit_over/exit_over_east.png"));
	
	private ChangingButton playBtn;
	private ChangingButton optionsBtn;
	private ChangingButton helpBtn;
	private ChangingButton exitBtn;
	
	private DictionaryTree dt;
	
	public MainScreen() throws FileNotFoundException{
		super("Word Mole");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Util.getGameSize());
		setResizable(false);
		
		try{
		dt = new DictionaryTree("dict.txt");
		} catch (FileNotFoundException fe){
			dt = new DictionaryTree("src/dict.txt");
		}
		// Create the buttons.
		playBtn = new ChangingButton(new ImageIcon(getClass().getResource("/main/images/none_over/play.png")),
				new ImageIcon(getClass().getResource("/main/images/play_over/play_over.png")));
		optionsBtn = new ChangingButton(new ImageIcon(getClass().getResource("/main/images/none_over/options.png")),
				new ImageIcon(getClass().getResource("/main/images/options_over/options_over.png")),
				new ImageIcon(getClass().getResource("/main/images/play_over/options.png")));
		helpBtn = new ChangingButton(new ImageIcon(getClass().getResource("/main/images/none_over/help.png")),
				new ImageIcon(getClass().getResource("/main/images/help_over/help_over.png")),
				new ImageIcon(getClass().getResource("/main/images/options_over/help_with_options.png"))); 
		exitBtn = new ChangingButton(new ImageIcon(getClass().getResource("/main/images/none_over/exit.png")),
				new ImageIcon(getClass().getResource("/main/images/exit_over/exit_over.png")),
				new ImageIcon(getClass().getResource("/main/images/help_over/exit_with_help.png")));
				
		/// SETUP NORTH PANEL ///
		JPanel northPnl = new JPanel();
		northPnl.setLayout(new BoxLayout(northPnl, BoxLayout.X_AXIS));
			/// SETUP NORTHWEST PANEL ///
		BackgroundPanel westPnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/main/images/none_over/west.png")));
			// SETUP NORTHEAST PANEL ///
		BackgroundPanel eastPnl = new BackgroundPanel(new ImageIcon(getClass().getResource("/main/images/none_over/east.png")));
		eastPnl.setLayout(new BoxLayout(eastPnl, BoxLayout.Y_AXIS));
		eastPnl.add(Box.createVerticalStrut(75)); // Break between top of screen and play button.
		eastPnl.add(playBtn);
		eastPnl.add(Box.createVerticalStrut(12)); // Break between play and options button.
		eastPnl.add(optionsBtn);
		eastPnl.add(Box.createVerticalStrut(10)); // Break between options and help button.
		eastPnl.add(helpBtn);
		eastPnl.add(Box.createVerticalStrut(9));  // Break between help and exit button.
		eastPnl.add(exitBtn);
		
		northPnl.add(westPnl);
		northPnl.add(eastPnl);
		add(northPnl);
		
		/// SETUP SOUTH PANEL ///
		add(new BackgroundPanel(new ImageIcon(getClass().getResource("/main/images/none_over/south.png")))); 
		
		// Setup the mouse listeners.		
		playBtn.addMouseListener(new ButtonAdapter(playBtn, westPnl, eastPnl, playOverWest.getImage(), playOverEast.getImage()));
		optionsBtn.addMouseListener(new ButtonAdapter(optionsBtn, westPnl, eastPnl, optionsOverWest.getImage(), optionsOverEast.getImage()));
		helpBtn.addMouseListener(new ButtonAdapter(helpBtn, westPnl, eastPnl, helpOverWest.getImage(), helpOverEast.getImage()));
		exitBtn.addMouseListener(new ButtonAdapter(exitBtn, westPnl, eastPnl, exitOverWest.getImage(), exitOverEast.getImage()));
		
		// Setup the action listeners
		playBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GameSelect gs = new GameSelect(MainScreen.this, dt);
				gs.setVisible(true);
			}
		});
		
		exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		setVisible(true);
	}
	
	/**
	 * ButtonAdapter class to make sure the user has the correct image up on the screen when he/she
	 * hovers over the image that needs to be changed.
	 * @author Chris Barton
	 *
	 */
	private class ButtonAdapter extends MouseAdapter{
		private ChangingButton button;
		private BackgroundPanel west, east;
		private Image wimg, eimg;
		
		public ButtonAdapter(ChangingButton b, BackgroundPanel w, BackgroundPanel e, Image wi, Image ei){
			button = b;
			west = w;
			east = e;
			wimg = wi;
			eimg = ei;
		}
		
		public void mouseEntered(MouseEvent e){
			button.setHovered(true);
			west.changeImage(wimg);
			east.changeImage(eimg);
		
			if ( button.equals(playBtn) )
				optionsBtn.setEffectedBackground(true);
			else if ( button.equals(optionsBtn) )
				helpBtn.setEffectedBackground(true);
			else if ( button.equals(helpBtn) )
				exitBtn.setEffectedBackground(true);
			
		}
		
		public void mouseExited(MouseEvent e){
			button.setHovered(false);
			west.changeImage(null);
			east.changeImage(null);
			
			if ( button.equals(playBtn) )
				optionsBtn.setEffectedBackground(false);
			else if ( button.equals(optionsBtn) )
				helpBtn.setEffectedBackground(false);
			else if ( button.equals(helpBtn) )
				exitBtn.setEffectedBackground(false);
		}
	}
}
