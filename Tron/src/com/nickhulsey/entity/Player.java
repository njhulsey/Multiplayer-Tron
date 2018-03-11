package com.nickhulsey.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import com.nickhulsey.main.Game;
import com.nickhulsey.main.Window;
import com.nickhulsey.net.GameClient;
import com.nickhulsey.net.PacketType;
import com.nickhulsey.values.Direction;
import com.nickhulsey.values.ObjectID;

public class Player extends Entity{

	private int speed = 2;
	private int count = 0;
	private Direction direction = Direction.RIGHT;
	public boolean dead = false;
	
	public Player(int x, int y, int size, Color color, ObjectID id, Game game) {
		super(x, y, size, color, id, game);
		randomPosition();
	}
	
	public void randomPosition(){
		Random r = new Random();
		int xG = Window.WIDTH / Window.TILE_SIZE;
		int yG = Window.HEIGHT / Window.TILE_SIZE;
		
		x = r.nextInt(xG) * Window.TILE_SIZE;
		y = r.nextInt(yG) * Window.TILE_SIZE;
		
		for(int i = 0; i < game.entities.size(); i++)
			if(x == game.entities.get(i).x && y == game.entities.get(i).y)
				randomPosition();
	}

	public void tick() {
		if(!dead){
			move();
			if(x < 0 || x > Window.WIDTH || y < 0 || y > Window.HEIGHT){
				dead = true;
				game.client.sendData(game.client.packetHandler.writePacket(PacketType.RESTART, GameClient.LOCAL_USERNAME, ""+dead));
			}
		} 
		game.client.sendData(game.client.packetHandler.writePacket(PacketType.RESTART, GameClient.LOCAL_USERNAME, ""+dead));
	}
	
	public void move(){
		count ++;
		if(count >= speed){
			count = 0;
			if(direction == Direction.DOWN)y+= Window.TILE_SIZE;
			if(direction == Direction.UP)y-= Window.TILE_SIZE;
			if(direction == Direction.LEFT)x-= Window.TILE_SIZE;
			if(direction == Direction.RIGHT)x+= Window.TILE_SIZE;
			
			game.addEntities.add(new Wall(x,y,size,color,ObjectID.WALL,game));
			game.client.sendData(game.client.packetHandler.writePacket(PacketType.MOVE, GameClient.LOCAL_USERNAME, x+","+y));
			
			for(int i = 0; i < game.entities.size(); i ++){
				if(game.entities != null && game.entities.get(i).id.solid && game.entities.get(i).x == x && game.entities.get(i).y == y){
					dead = true;
					game.client.sendData(game.client.packetHandler.writePacket(PacketType.RESTART, GameClient.LOCAL_USERNAME, "true"));
				}
			}
		}
		
		if(game.input.key[Direction.LEFT.key] && direction.getFalseMove() != Direction.LEFT)
			direction = Direction.LEFT;
		
		if(game.input.key[Direction.RIGHT.key] && direction.getFalseMove() != Direction.RIGHT)
			direction = Direction.RIGHT;
		
		if(game.input.key[Direction.UP.key] && direction.getFalseMove() != Direction.UP)
			direction = Direction.UP;
		
		if(game.input.key[Direction.DOWN.key] && direction.getFalseMove() != Direction.DOWN)
			direction = Direction.DOWN;
	}

	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, size, size);
		
		
	}

	
}