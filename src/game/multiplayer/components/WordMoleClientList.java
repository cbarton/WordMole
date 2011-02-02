/*
 * WordMoleClientList.java
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
package game.multiplayer.components;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import game.components.WordMoleList;
import game.multiplayer.containers.Client;
import game.states.PlayerState;

/**
 * WordMoleList for the {@link game.multiplayer.WordMoleClientImpl}
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class WordMoleClientList extends WordMoleList{
	private String imgPath = "/game/images/labels/multiplayer/";
	private ImageIcon [] stateIcons = { new ImageIcon(getClass().getResource(imgPath + "not_connected.png")),
			new ImageIcon(getClass().getResource(imgPath + "connected.png")),
			new ImageIcon(getClass().getResource(imgPath + "waiting.png")),
			new ImageIcon(getClass().getResource(imgPath + "playing.png"))
	};
	
	private DefaultListModel thisModel = new DefaultListModel();
	
	/**
	 * Constructs the WordMoleClientList
	 * @param frame - <code>Window</code> for the owner of the list.
	 * @param content - <code>Object[]</code> containing the elements the list shows.
	 */
	public WordMoleClientList(Window frame, Object[] content) {
		super(frame, content);
		setBackground(Color.BLACK);
		setModel(thisModel);
		setCellRenderer(new ListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				
				String name = ((Client)value).name;
				PlayerState state = ((Client)value).state;
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
				c.setFont(Util.Util.getGameFontSmall());					
				bg.add(c);	
				
				bg.add(Box.createHorizontalGlue());
				
				bg.add(new JLabel(stateIcons[state.ordinal()]));

				return bg;
			}
		});
	}
	
	/**
	 * Updates the list based on new Clientelle
	 * @param clients - <code>Client[]</code> containing the list of Clients.
	 */
	public void update(Client[] clients){
		setListData(clients);
	}
	
	public Client [] getSelectedValues(){
		// Converts the selected values to an Client [].
		Object [] obj = super.getSelectedValues();
		Client [] ret = new Client[obj.length];
		for ( int i = 0; i < obj.length; i++ )
			ret[i] = (Client)obj[i];
		return ret;
	}	
}
