package com.nickhulsey.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.nickhulsey.main.Game;
import com.nickhulsey.values.ObjectID;

public abstract class Entity {
	
	public int x,y,size;
	public Game game;
	public ObjectID id;
	protected Color color;
	
	public Entity(int x, int y, int size, ObjectID id, Game game) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
		this.id = id;
		this.game = game;
		color = null;
	}

	public Entity(int x, int y, int size, Color color, ObjectID id, Game game) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
		this.id = id;
		this.game = game;
	}
	
	public abstract void tick();
	public abstract void render(Graphics2D g);


	public boolean collisionLeft(Entity e){
		Rectangle coll = new Rectangle(x - 1, y, size, size);
		Rectangle Ecoll = new Rectangle(e.x,e.y,e.size,e.size);
		if(coll.intersects(Ecoll)){
			coll = null;
			Ecoll = null;
			return true;
		}
		coll = null;
		Ecoll = null;
		return false;
	}
	public boolean collisionRight(Entity e){
		Rectangle coll = new Rectangle(x + size, y, 1, size);
		Rectangle Ecoll = new Rectangle(e.x,e.y,e.size,e.size);
		if(coll.intersects(Ecoll)){
			coll = null;
			Ecoll = null;
			return true;
		}
		coll = null;
		Ecoll = null;
		return false;
	}
	
	public boolean collisionUp(Entity e){
		Rectangle coll = new Rectangle(x, y - 1, size, 1);
		Rectangle Ecoll = new Rectangle(e.x,e.y,e.size,e.size);
		if(coll.intersects(Ecoll)){
			coll = null;
			Ecoll = null;
			return true;
		}
		coll = null;
		Ecoll = null;
		return false;
	}
	public boolean collisionDown(Entity e){
		Rectangle coll = new Rectangle(x, y + size, size, 1);
		Rectangle Ecoll = new Rectangle(e.x,e.y,e.size,e.size);
		if(coll.intersects(Ecoll)){
			coll = null;
			Ecoll = null;
			return true;
		}
		coll = null;
		Ecoll = null;
		return false;
	}
	
	public boolean collisionAll(Entity e, int radius){
		Rectangle thisR = new Rectangle(x - radius,y - radius,size + (radius * 2),size + (radius * 2));
		Rectangle rectE = new Rectangle(e.x,e.y,e.size,e.size);
		if(thisR.intersects(rectE)){
			rectE = null;
			thisR = null;
			return true;
		}
		else {
			thisR = null;
			rectE = null;
			return false;
		}
		
	}

}