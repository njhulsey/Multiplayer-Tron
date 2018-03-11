package com.nickhulsey.values;

import java.awt.event.KeyEvent;

public enum Direction {
	
	LEFT(KeyEvent.VK_A, "right"), RIGHT(KeyEvent.VK_D, "left"), UP(KeyEvent.VK_W, "down"), DOWN(KeyEvent.VK_S, "up");
	
	private String falseMove;
	public int key;
	
	private Direction(int key, String falseMove){
		this.falseMove = falseMove;
		this.key = key;
	}
	
	public Direction getFalseMove(){
		if(falseMove.equals("left"))return LEFT;
		if(falseMove.equals("right")) return RIGHT;
		if(falseMove.equals("up"))return UP;
		if(falseMove.equals("down"))return DOWN;
		return null;
	}
	

}
