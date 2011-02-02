/*
 * Util.java
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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Util {
	
	public static Font getGameFont(){
		return new Font("Consolas", Font.BOLD, 20);
	}
	
	public static Font getGameFontSmall(){
		return new Font("Consolas", Font.BOLD, 12);
	}
	
	public static Font getNormalFont(){
		return new Font("Veranda", Font.BOLD, 24);
	}
	
	public static Color getFadeBg(){
		return new Color(0, 0, 0, 200);
	}
	
	public static Color getMenuGradient(){
		return  new Color(85, 172, 85);
	}
	
	public static Dimension getGameSize(){
		return new Dimension(360, 505);
	}
	
	public static JLabel createLbl(String s, Font font){
		JLabel lbl = new JLabel(s);
		lbl.setForeground(Color.WHITE);
		lbl.setFont(font);
		lbl.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		return lbl;
	}
	
	public static JButton createBtn(ImageIcon nonhover, ImageIcon hover, String cmd, ActionListener al, boolean centered){
		JButton btn = new JButton(nonhover);
		btn.setRolloverIcon(hover);
		btn.setBorder(null);
		btn.setActionCommand(cmd);
		btn.addActionListener(al);
		if ( centered )
			btn.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		return btn;
	}
	
	public static JPanel createTitlePanel(final String title){
		return new JPanel(){
			public void paintComponent(Graphics g){
				int w = getWidth();
				int h = getHeight();
			
				GradientPaint gp = new GradientPaint(0, 0, getMenuGradient(), 0, h, Color.WHITE);
				Graphics2D g2d = (Graphics2D)g;
				g2d.setPaint(gp);
				g.fillRect(0, 0, w, h);
				
				g.setFont(getGameFontSmall());
				g.setColor(Color.BLACK);
				g.drawString(title, 1, getHeight()-2);
			}
		};
	}
	
	public static JPanel createPnl(int axis){
		JPanel pnl = new JPanel();
		pnl.setBackground(Color.BLACK);
		pnl.setLayout(new BoxLayout(pnl, axis));
		return pnl;
	}
	
	public static JTextField createField(){
		JTextField f = new JTextField(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				
				g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 5);
			}
		};
		f.setCaretColor(Color.BLACK);
		f.setFont(getGameFontSmall());
		f.setBackground(Color.WHITE);
		f.setForeground(Color.BLACK);
		f.setBorder(null);
		Dimension size = new Dimension(230, 20);
	    f.setPreferredSize(size);
	    f.setMaximumSize(size);
	    f.setSize(size);
	    f.requestFocusInWindow();
		return f;
	}
}
