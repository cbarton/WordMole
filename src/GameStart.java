/*
 * GameStart.java
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
import java.io.FileNotFoundException;

import main.game.MainScreen;

/**
 * Starts the Word Mole game.
 * @author Chris Barton
 *
 */
public class GameStart {
	/**
	 * Main method for the Word Mole game to open up {@link main.game.MainScreen}.
	 */
	public static void main(String[] args) throws FileNotFoundException{
		new MainScreen();
	}
}
