package com.nickhulsey.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.nickhulsey.main.Game;
import com.nickhulsey.values.ObjectID;

public class Wall extends Entity{
	
	public Wall(int x, int y, int size, Color color, ObjectID id, Game game) {
		super(x, y, size, color, id, game);
	}

	public void tick() {
		
	}

	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, size, size);
	}

}