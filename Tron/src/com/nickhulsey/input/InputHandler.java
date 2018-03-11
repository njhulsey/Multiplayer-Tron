package com.nickhulsey.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
	
	public boolean[] key = new boolean[68836];
	public int MouseX;
	public int MouseY;
	public boolean clicked = false;
	
	
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		MouseX = (int)(e.getX());
		MouseY = (int)(e.getY());
	}

	public void mouseClicked(MouseEvent e) {
		MouseX = (int)(e.getX());
		MouseY = (int)(e.getY());
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		clicked = true;
	}

	public void mouseReleased(MouseEvent e) {
		clicked = false;
	}

	public void focusGained(FocusEvent e) {
		
	}

	public void focusLost(FocusEvent e) {
		for(int i = 0; i < key.length; i ++){
			key[i] = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		if(KeyCode > 0 && KeyCode < key.length)key[KeyCode] = true;
	}

	public void keyReleased(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		if(key[KeyCode] == true)key[KeyCode] = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
