/*
 * WordTree.java
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
import java.util.ArrayList;

import Util.board.Square;

/**
 * A tree data structure containing the individual letters
 * for a fully formed word in the Word Mole game
 * @author Chris Barton
 *
 */
public class WordTree{
	private SquareNode root;
	private ArrayList<Integer> selectedLocations;
	
	public WordTree(){
		root = null;
		selectedLocations = new ArrayList<Integer>();
	}
		
	public void insert(Square s){
		SquareNode node = new SquareNode(s);
		
		// Empty tree
		if ( root == null ){
			root = node;
			selectedLocations.add(root.square.getSquareLocation());
		}
		else // Not empty
			root = insertNode(root, s);
	}
	
	public SquareNode insertNode(SquareNode node, Square s){
		SquareNode temp;
		
		if ( node == null ){
			return new SquareNode(s);
		}
		temp = insertNode(node.child, s);
		node.child = temp;
		
		if ( !selectedLocations.contains(temp.square.getSquareLocation())){
			selectedLocations.add(temp.square.getSquareLocation());
		}
		
		return node;
	}
	
	public void remove(Square s){
		root = remove(root, s);
	}
	
	private SquareNode remove(SquareNode node, Square s){
		if ( node.square.equals(s) ){
			return deleteChildren(node);
		}
		node.child = remove(node.child, s);
		
		return node;
	}
	
	private SquareNode deleteChildren(SquareNode node){
		if ( node == null ){
			return node;
		}
		node.child = deleteChildren(node.child);
		node.square.removeSelection();
		
		selectedLocations.remove(selectedLocations.indexOf(node.square.getSquareLocation()));
		node = null;
		return node;
	}
	
	/**
	 * Delete all children + root from the tree.
	 */
	public void cleanTree(){
		root = deleteChildren(root);
	}
	
	public String getWord(){
		StringBuilder word = new StringBuilder(getWord(root));
		return word.reverse().toString(); // Comes in reversed 
	}
	
	private String getWord(SquareNode node){
		String word = "";
		if ( node == null )
			return "";
			
		
		word = getWord(node.child);
		word += node.square.getLetter();
		
		return word;
	}
	
	public ArrayList<Integer> getLocations(){
		return selectedLocations;
	}
	
	/**
	 * Reset the selected Locations.
	 */
	public void empty(){
		selectedLocations = new ArrayList<Integer>();
	}
	
	private class SquareNode{
		private Square square;
		private SquareNode child;
		
		public SquareNode(Square s){
			square = s;
			child = null;
		}
	}
}


