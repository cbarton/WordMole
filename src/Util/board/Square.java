/*
 * Square.java
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

package Util.board;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Util.Util;

/**
 * The Square class containing the individual squares for the board in 
 * Word Mole
 * @author Chris Barton
 *
 */
public class Square extends JLabel{
		private char letter;
		private int location;
		private Board board;
		private boolean selected = false; // If the square is currently selected
		private boolean firstSelect = false; // For the first selection.
		private boolean isHole = false; // Used for 'holes' in real game
		private boolean isVegetable = false; // Used for double points
		
		public Square(char letter, int location, Board board){			
			this.letter = letter;
			this.location = location;
			this.board = board;
			
			addMouseListener(new MouseAdapter(){
				public void mouseReleased(MouseEvent e){
					if ( !isHole ){
						selected = !selected;
						(Square.this.board).select(Square.this);	
						repaint();
					}
				}
				
				public void mouseEntered(MouseEvent e){
					if ( !isHole ){
						firstSelect = true;
						repaint();
					}	
				}
				
				public void mouseExited(MouseEvent e){
					if ( !isHole ){
						firstSelect = false;
						repaint();
					}	
				}
			});
		}
		
		public boolean isSelected(){
			return selected;
		}
		
		public void setHole(boolean hole){
			isHole = hole;
			repaint();
		}
		
		public boolean isHole(){
			return isHole;
		}
		
		public int getSquareLocation(){
			return location;
		}
		
		public char getLetter(){
			return letter;
		}
		
		public void setVegetable(boolean veg){
			isVegetable = veg;
			if ( !isVegetable )
				isHole = true;
			repaint();
		}
		
		public boolean isVegetable(){
			return isVegetable;
		}
		
		public void setLetter(char letter){
			this.letter = letter;
			removeSelection();
		}
		
		public void removeSelection(){
			selected = false;
			repaint();
		}
		
		public Dimension getMinimumSize(){
			return new Dimension(30, 30);
		}
		
		public Dimension getPreferredSize(){
			return getMinimumSize();
		}
		
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			g.setColor(Color.WHITE);
			
			if ( !isHole ){
				if ( firstSelect && isVegetable )
					g.drawImage(new ImageIcon(getClass().getResource("/game/images/square/first_selected_vege.png")).getImage(), 0, 0, this);
				else if ( isVegetable )
					g.drawImage(new ImageIcon(getClass().getResource("/game/images/square/vegetable.png")).getImage(), 8, 10, this);
				else if ( firstSelect )
					g.drawImage(new ImageIcon(getClass().getResource("/game/images/square/first_selected.png")).getImage(), 0, 0, this);
				
				if ( selected && isVegetable ){
					g.drawImage(new ImageIcon(getClass().getResource("/game/images/square/selected_vege.png")).getImage(), 0, 1, this);
					g.setColor(Color.YELLOW);
				}
				else if ( selected ){
					g.drawImage(new ImageIcon(getClass().getResource("/game/images/square/selected.png")).getImage(), 0, 0, this);
					g.setColor(Color.YELLOW);
				}
				
				g.setFont(Util.getGameFont());
				g.drawString(Character.toString(letter), 23, 37);
			}
			else // it's a hole
				g.drawImage(new ImageIcon(getClass().getResource("/game/images/square/hole.png")).getImage(), 10, 10, this);
		}
	}
