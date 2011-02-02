/*
 * WordMoleList.java
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
package game.components;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Window;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * WordMoleList is the JList for the Word Mole game.
 * @author Chris Barton
 *
 */
@SuppressWarnings("serial")
public class WordMoleList extends JList{
	
	/**
	 * Constructor
	 * @param frame - <code>Window</code> containing which window the WordMoleList is currently in.
	 * @param content - <code>Object[]</code> containing the elements to populate.
	 */
	public WordMoleList(final Window frame, Object[] content){
		super(content);
		setForeground(Color.WHITE);
		setFont(Util.Util.getGameFont());
		setSelectionModel(new DefaultListSelectionModel(){
            public void setSelectionInterval(int index0, int index1) {
                if (index0==index1) {
                    if (isSelectedIndex(index0)) {
                        removeSelectionInterval(index0, index0);
                        return;
                    }
                }
                super.setSelectionInterval(index0, index1);
            }
            
            public void addSelectionInterval(int index0, int index1) {
                if (index0==index1) {
                    if (isSelectedIndex(index0)) {
                        removeSelectionInterval(index0, index0);
                        return;
                    }
                super.addSelectionInterval(index0, index1);
                }
            }
        });
		
		setCellRenderer(new ListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				
				JPanel bg = new JPanel();
				
				if ( isSelected )
					bg.setOpaque(true);
				else{
					bg.setOpaque(false);
					frame.repaint(); // repaint behind for transparent feeling	
				}
				
				bg.setLayout(new BoxLayout(bg, BoxLayout.X_AXIS));
				bg.setBackground(Util.Util.getMenuGradient());
				
				JLabel c = new JLabel(value.toString());
				c.setAlignmentX(LEFT_ALIGNMENT);
				c.setForeground(Color.WHITE);
				c.setFont(Util.Util.getGameFont());					
				bg.add(c);	

				return bg;
			}
			
		});
	}		
	
	public void paintComponent(Graphics g){
		setOpaque(false);
		super.paintComponent(g);
	}
}
