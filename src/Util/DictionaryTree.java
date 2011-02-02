/*
 * DictionaryTree.java
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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * High Performance alphabet search tree that will find any word 
 * 100x faster than a normal ArrayList
 * @author Chris Barton
 *
 */
public class DictionaryTree{
	private DictNode root;
	
	public DictionaryTree(){
		root = new DictNode(' ');
	}
	
	public DictionaryTree(String filename) throws FileNotFoundException{
		File file = new File(filename);
		Scanner input = new Scanner(file);
		
		root = new DictNode(' ');
				
		while ( input.hasNext() ){
			String next = input.next().trim();
			insert(next.toUpperCase());
		}
	}
	
	private class DictNode{
		public char value;
		public List<DictNode> upper, lower;
		public boolean endOfWord = false;
		
		public DictNode(char v){
			value = v;
			upper = new ArrayList<DictNode>(13); 
			lower = new ArrayList<DictNode>(13);
		}
			
		@Override
		public boolean equals(Object e){
			if ( ((DictNode)e).value == value )
				return true;
			return false;
		}
	}
	
	public void insert(String word){
		root = insert(word, root, 0);
	}
	
	private DictNode insert(String word, DictNode node, int i){
		char c;
		int idx = -1;
		DictNode temp;
		
		if ( i == word.length() ){
			node.endOfWord = true;
			return node;
		}
		
		c = word.charAt(i);
		temp = new DictNode(c);
		
		if ( c >= 'A' && c <= 'I' ){
			idx = node.upper.indexOf(temp);
						
			if ( idx == -1 )
				node.upper.add(insert(word, temp, i+1));
			else{
				temp = node.upper.get(idx);
				node.upper.set(idx, insert(word, temp, i+1));
			}
		} else if ( c >= 'J' && c <= 'Z' ){
			idx = node.lower.indexOf(temp);
							
			if ( idx == -1 )
				node.lower.add(insert(word, temp, i+1));
			else{
				temp = node.lower.get(idx);
				node.lower.set(idx, insert(word, temp, i+1));
			}
		} 
		
		return node;
	}
	
	public boolean search(String word){
		if ( word.length() > 0 )
			return search(word, root, 0);
			
		return false;
	}
	
	private boolean search(String word, DictNode node, int i){
		char c;
		int idx = -1;
		DictNode temp;
		
		if ( i == word.length() && node.endOfWord )
			return true;
		else if ( i == word.length() && !node.endOfWord )
			return false;
		
		c = word.charAt(i);
		temp = new DictNode(c);
	
		
		if ( c >= 'A' && c <= 'I' ){
			idx = node.upper.indexOf(temp);
			if ( idx != -1 )
				 return search(word, node.upper.get(idx), i+1);
		} else if ( c >= 'J' && c <= 'Z' ){
			idx = node.lower.indexOf(temp);
			if ( idx != -1 )
				 return search(word, node.lower.get(idx), i+1);
		}
		
		return false;
	}
}