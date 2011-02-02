/*
 * Board.java
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

import game.components.BackgroundPanel;
import game.status.InfoPopup;

import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;


import Util.DictionaryTree;
import Util.WordTree;

/**
 * Sets up the board and its features for the Word Mole game
 * @author Chris Barton
 * @rules 3 of each letter, incl. vowels, however there are only allowed to be a min. 3 different vowels on the board.
 */
public class Board extends BackgroundPanel{
	private final int BOARD_SIZE = 36;
	private Square [] board;
	private transient DictionaryTree dictionary;
	private transient WordTree squares;
	private Hashtable<Character, Integer> table;
	private final char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z'};
	private final char [] vowels = { 'A', 'E', 'I', 'O', 'U'};
	private final int [] letterScores = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 
			1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10	};
	
	private String currentWord;
	
	private ArrayList<Integer> holes;
	private final int MAX_HOLE_NUM = 6;
	
	private ArrayList<Integer> vegetables;
	private final int VEGE_BONUS = 8;
	
	private int num_vowels = 0;
	private final int MAX_VOWEL_NUM = 4;
	
	public Board(DictionaryTree dt, ImageIcon img){
		super(img);
		setLayout(new GridLayout(6,6));
		
		dictionary = dt;
		currentWord = "";
		
		board = new Square[BOARD_SIZE];
		squares = new WordTree();
		holes = new ArrayList<Integer>();
		vegetables = new ArrayList<Integer>();
		table = new Hashtable<Character, Integer>();
		
		// Set up the board
		for ( int i = 0; i < BOARD_SIZE; i++ ){
			char let = getNewLetter(false);
			board[i] = new Square(let, i, this);
					
			add(board[i]);
		}
		
		checkBoard();
	}
	
	public void select(Square s){
		if ( s.isSelected() )
			squares.insert(s);
		else
			squares.remove(s);
		
		currentWord = squares.getWord();
		if ( currentWord.length() > 0 )
			firePropertyChange("selected", "", currentWord);
		else
			firePropertyChange("selected", "", null);
	}
	
	public void createVegetable(){
		Square s;
		int k = 0;
		
		do{
			k = (int)(Math.random()*36);
			s = board[k];
			
			if ( !s.isHole() || !s.isVegetable() ){
				vegetables.add(k);
				s.setVegetable(true);
			}
			
		}while( s.isHole() );
	}
	
	/** Remove all vegetables from board
	 * 
	 */
	public void removeVegetable(){
		int len = vegetables.size();
		for ( int i = len-1; i >= 0; i-- )
			removeVegetable(vegetables.get(i));
	}
	
	/** Remove a specific vegetable off the board.
	 * 
	 * @param loc - int location of the square to be removed.
	 */
	public void removeVegetable(int loc){
		Square s = board[loc];
		// keep vowels updated
		if ( isVowel(s.getLetter()) )
			num_vowels--;
		
		s.setVegetable(false);
		holes.add(loc);
		vegetables.remove(vegetables.indexOf(loc));
		
		// If selected, remove from the locations and the word
		if ( s.isSelected() ){
			s.removeSelection();
			select(s);
		}
	}
	
	/**
	 * Replaces the vegetable with another letter.
	 * @param loc - int location of the square to be removed.
	 */
	private void replaceVegetable(int loc){
		board[loc].setVegetable(false);
		board[loc].setHole(false);
		vegetables.remove(vegetables.indexOf(loc));
		genSquare(board[loc]);
	}
	
	/**
	 * Reset the board.
	 */
	public void emptySquares(){
		table = new Hashtable<Character, Integer>();
		num_vowels = 0;
		
		for ( int i = 0; i < BOARD_SIZE; i++ ){
			char let = getNewLetter(false);
			board[i].setLetter(let);
		}
		squares.empty();
		checkBoard();
	}
	
	/**
	 * Reset the current word and its selected locations.
	 */
	public void cleanup(){
		squares.cleanTree();
		firePropertyChange("selected", "", null);
		currentWord = "";
	}
		
	public int checkWord(){
		int score = 0;
		int k = 0;
		boolean hasVegetable = false;
		int size = currentWord.length();
		setStatus status;
		
		if ( size == 0 ){
			status = new setStatus("No word selected!", new Point((int)getLocationOnScreen().getX()+getWidth()-200, (int)getLocationOnScreen().getY()+getHeight())); // Start at submit button.
			status.execute();
			return -1;
		}
		
		if ( size < 2 ){
			status = new setStatus("Length too short!");
			status.execute();
			return 0; // has to be greater than 2 chars
		}
		if ( dictionary.search(currentWord.trim()) ){
			for ( int idx = 0; idx < size; idx++ ){
				k = squares.getLocations().get(idx);
				if ( board[k].isVegetable() ){
					hasVegetable = true;
					replaceVegetable(k);
					firePropertyChange("veg", 0, 1);
				}
				score += letterScores[currentWord.charAt(idx)-65]; // Adjust for ASCII
			}
			
			if ( size >= 3 )
				score *= Math.round(size / 3);
			if ( hasVegetable ){
				score = (score * 2) + VEGE_BONUS;
				//status = new setStatus(String.format("<html><body><p><font color='green'>Vegetable: " + VEGE_BONUS + "</font></p><p>" +
				//		currentWord +" ("+ score + " points)</p></body></html>"));
				status = new setStatus(currentWord +"  ("+ score + " points) + " + VEGE_BONUS);
			}else
				status = new setStatus(currentWord +"  ("+ score + " points)");
			status.execute();
			
			refillBoard(squares.getLocations());
			
			return score;
		} 
		new setStatus("Invalid word!").execute();
		return 0;
	}
	
	private void refillBoard(ArrayList<Integer> locs){		 
		for ( int k : locs ){
			Square s = board[k];
			genSquare(s);
		}
	}
	
	private char getNewLetter(boolean needVowel){
		Integer i;
		boolean done = false;
		char letter = 0;

		while ( !done ){
			if ( !needVowel )
				letter = alphabet[(int)(Math.random()*alphabet.length)];
			else
				letter = vowels[(int)(Math.random()*vowels.length)];
			
			if ( (i = table.get(letter)) != null ){
				if ( i < 3 ){
					table.put(letter, ++i);
					done = true;
				}
			} else{
				if ( needVowel || isVowel(letter) ){ // Checked for both cases:  need a vowel or not
					if ( num_vowels < MAX_VOWEL_NUM )
						num_vowels++;
					else
						continue; // Not allowed to have more than 3 on the board
				}
				table.put(letter, 1);
				done = true;
			}
		}
		return letter;
	}
	
	private void genSquare(Square s){
		char c;
		char sLetter = s.getLetter();
		int count = table.get(sLetter);
		
		if ( count == 1 ){
			if ( isVowel(sLetter) )
				num_vowels--; // If there is only 1 of them then this will remove the last one from the board
			table.remove(sLetter); // MUST REMOVE in order to keep NUM VOWELS on board
		} else
			table.put(sLetter, --count); // Take out 1 occurrence from the board.	
		
		if ( num_vowels < MAX_VOWEL_NUM )
			c = getNewLetter(true);
		else
			c = getNewLetter(false);
		
		s.setLetter(c);
	}
	
	private boolean isVowel(char c){
		for ( char v : vowels ){
			if ( c == v )
				return true;
		}
		return false;
	}
	
	
	// Check if there are enough vowels on the board and no more than 2 of the same letter on the board (-vowels)
	private void checkBoard(){
		Square s;
		while ( num_vowels < MAX_VOWEL_NUM ){
			int k = (int)(Math.random()*BOARD_SIZE);
			s = board[k];
			
			if ( isVowel(s.getLetter()) || s.isHole() )
				continue; // No need to replace a already vowel or hole
			s.setLetter(getNewLetter(true)); // Add a vowel
		}
	}
	
	// Return the last location selected
	private Square getLastSquare(){
		return board[squares.getLocations().get(squares.getLocations().size()-1)];
	}
	
	private class setStatus extends SwingWorker<Void, Void>{
		private String status;
		private Point p;
		private InfoPopup info;
		
		public setStatus(String s){
			status = s;
			p = null;
		}
		
		public setStatus(String s, Point p){
			status = s;
			this.p = p;
		}
		
		public Void doInBackground(){
			Square s;
			int x, y;
			
			try{
				if ( p == null ){ // Allow for the hover over submit button effect
					s = Board.this.getLastSquare();
					x = (int)(s.getLocationOnScreen().getX()+15);
					y = (int)(s.getLocationOnScreen().getY()+15);
				}
				else {
					x = (int) p.getX();
					y = (int) p.getY();
				}
				info = new InfoPopup(status, x, y);
				info.setVisible(true);
				Thread.sleep(1550); // leave on the board for 1.5 seconds
				info.dispose();
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			return null;
		}
	}
	
	public void manageHoles(boolean flag){
		Square s;
		int num_holes = 0, hole = 0, loc, curHole, curSize;
		int num_restore = 0;
		
		// Create holes
		if ( flag ){
			if ( holes.size() > 0 ){ // Restore some holes
				curSize = holes.size();
				num_restore = (int)(Math.random()*curSize);
			
				for ( int i = num_restore-1; i >= 0; i-- ){
					curHole = holes.get(i);
					board[curHole].setHole(false);
					genSquare(board[curHole]);
					holes.remove(i);
				}
			}
			
			num_holes = (int)(Math.random()*currentWord.length());
			for ( int i = 0; i < num_holes; i++ ){				
				if ( holes.size() < MAX_HOLE_NUM ){
				// Allow for holes to be restored and created
				// Make sure the hole is part of the word submitted
					hole = (int)(Math.random()*currentWord.length());
					curHole = squares.getLocations().get(hole); // Get the location of the hole
					s = board[curHole];
					loc = s.getSquareLocation();
					
					if ( !holes.contains(loc) ){
						s.setHole(true);
						holes.add(loc);
					}
				}
			}
		} else{ // Eliminate holes (end of level)
			curSize = holes.size();
			for ( int k : holes  ){
				board[k].setHole(false);
			}
			holes = new ArrayList<Integer>();
		}
	}
	
	public void setVisible(boolean visible){
		for ( int i = 0; i < BOARD_SIZE; i++ )
			board[i].setVisible(visible);
	}
}
