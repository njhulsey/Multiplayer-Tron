package com.nickhulsey.net;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.InetAddress;
import java.util.Random;

import com.nickhulsey.entity.Entity;
import com.nickhulsey.main.Window;
import com.nickhulsey.values.ObjectID;

public class ConnectedClient extends Entity{

	public String username;
	public InetAddress address;
	public int port;
	public Color color;
	public boolean dead = false;
	
	public ConnectedClient(String username, InetAddress address, int port){
		super(69,69,Window.TILE_SIZE,ObjectID.PLAYER,null);
		this.username = username;
		this.address = address;
		this.port = port;
		Random r = new Random();
		color = new Color(r.nextInt(205) + 50, r.nextInt(205) + 50, r.nextInt(205) + 50);
	}

	public void tick() {
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(color);
		g.drawString(username, x, y - 3);
		g.fillRect(x, y, size, size);
	}
	
}