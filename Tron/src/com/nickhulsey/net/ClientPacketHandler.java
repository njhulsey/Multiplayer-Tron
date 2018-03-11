package com.nickhulsey.net;

import java.net.InetAddress;

import com.nickhulsey.entity.Wall;
import com.nickhulsey.main.Window;
import com.nickhulsey.values.ObjectID;

public class ClientPacketHandler {
	
	public GameClient client;
	
	public ClientPacketHandler(GameClient client){
		this.client = client;
	}
	
	//receive data. decide what to do with it. 
	public void handlePacket(byte[] data, InetAddress address, int port){
		String message = new String(data);
		PacketType type = lookupPacketType(message);
		String [] strData = message.substring(2).trim().split(",");
		//System.out.println("Decompiled "+type+": " + Arrays.toString(strData));
		
		switch(type){
			default:
			case INVALID:
				break;
			case LOGIN: login(strData[0], address, port);
				break;
			case DISCONNECT: disconnect(strData[0]);
				break;
			case RESTART: restart();
				break;
			case MOVE: 
				create(strData[0],strData[1],strData[2],""+Window.TILE_SIZE);
				move(strData[0], strData[1], strData[2]);
		}
	}
	
	public void restart(){
		client.game.entities.clear();
		client.game.createBarrier();
		client.game.player.randomPosition();
		client.sendData(writePacket(PacketType.RESTART,client.LOCAL_USERNAME,"false"));
		client.game.player.dead = false;
	}
	
	public void create(String username, String x, String y, String size){
		int xI = Integer.parseInt(x.trim());
		int yI = Integer.parseInt(y.trim());
		int sizeI = Integer.parseInt(size.trim());
		for(int i = 0; i < client.clients.size(); i++){
			if(client.clients.get(i) != null && client.clients.get(i).username.equals(username))
				client.game.addEntities.add(new Wall(xI, yI, sizeI, client.clients.get(i).color, ObjectID.WALL, client.game));
		}

	}
	
	public void move(String username, String x, String y){
		//System.out.println("RECIEVED MOVE FROM: " +  username + " at: " + x + "," + y);
	
		for(int i = 0; i < client.clients.size(); i++){
			if(client.clients.get(i).username.equals(username)){
				try{
					client.clients.get(i).x = Integer.parseInt(x.trim());
					client.clients.get(i).y = Integer.parseInt(y.trim());
				}catch(NumberFormatException e){System.out.println("ERROR FORMATING");}
			}
		}
	
	}
	
	public void login(String username, InetAddress address, int port){
		System.out.println("Created a new login: " + username);

		for(int i = 0; i < client.clients.size(); i++){
			if(client.clients.get(i).username.equals(username))
				return;
		}
		client.clients.add(new ConnectedClient(username, address, port));
	}
	
	public void disconnect(String username){
		for(int i = 0; i < client.clients.size(); i++){
			if(client.clients.get(i).username.equals(username))
				client.clients.remove(i);
		}
	}
	
	public byte[] writePacket(PacketType type, String username, String additional){
		return (type.id + username + "," + additional).getBytes();
	}
	
	public static PacketType lookupPacketType(String message){
		String id = message.substring(0,2).trim();
		for(PacketType p: PacketType.values())
			if(p.id.equals(id))
				return p;
		return PacketType.INVALID;
	}
}