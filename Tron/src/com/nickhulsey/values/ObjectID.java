package com.nickhulsey.values;

public enum ObjectID {
	
	PLAYER(false), WALL(true);
	
	public boolean solid;
	private ObjectID(boolean solid){
		this.solid = solid;
	}

}