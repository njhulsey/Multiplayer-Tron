package com.nickhulsey.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.nickhulsey.entity.Entity;
import com.nickhulsey.entity.Player;
import com.nickhulsey.entity.Wall;
import com.nickhulsey.input.InputHandler;
import com.nickhulsey.net.GameClient;
import com.nickhulsey.net.PacketType;
import com.nickhulsey.values.ObjectID;

public class Game {

	public Window window;
	public InputHandler input;
	public GameClient client;

	public Player player;
	public ArrayList<Entity> entities;
	public ArrayList<Entity> addEntities;
	public int count = 0; 

	public Game(InputHandler input, Window window) {
		this.input = input;
		this.window = window;
		entities = new ArrayList<Entity>();
		addEntities = new ArrayList<Entity>();
	}

	public void init(){
		player = new Player(0, 0, Window.TILE_SIZE, new Color(0,102,204), ObjectID.PLAYER, this);
		createBarrier();
	}
	
	public void tick() {
		player.tick();
		
		entities.addAll(addEntities);
		addEntities.clear();
		count ++;
	}

	public void render(Graphics2D g) {
		for(int i = 0; i < entities.size(); i++)
			if(entities.get(i) != null)entities.get(i).render(g);

		for(int i = 0; i < client.clients.size(); i++)
			client.clients.get(i).render(g);
		
		player.render(g);
	}
	public void createBarrier(){
		int yG = Window.HEIGHT / Window.TILE_SIZE;
		int xG = Window.WIDTH / Window.TILE_SIZE;

		//top
		for(int i = 0; i < xG; i ++)
			entities.add(new Wall(i * Window.TILE_SIZE, 0, Window.TILE_SIZE, Color.white, ObjectID.WALL, this));
		
		for(int i = 0; i < xG; i ++)
			entities.add(new Wall(i * Window.TILE_SIZE, Window.HEIGHT - Window.TILE_SIZE, Window.TILE_SIZE, Color.white, ObjectID.WALL, this));
		
		for(int i = 0; i < yG; i ++)
			entities.add(new Wall(0, i * Window.TILE_SIZE, Window.TILE_SIZE, Color.white, ObjectID.WALL, this));
		
		for(int i = 0; i < yG; i ++)
			entities.add(new Wall(Window.WIDTH - Window.TILE_SIZE, i * Window.TILE_SIZE, Window.TILE_SIZE, Color.white, ObjectID.WALL, this));
	}
	
}