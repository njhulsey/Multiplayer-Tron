package com.nickhulsey.net;

import java.net.InetAddress;


public class ServerPacketHandler {
	private GameServer server; 
	
	public ServerPacketHandler(GameServer server){
		this.server = server;
	}

	//receive data. decide what to do with it. 
	public void handlePacket(byte[] data, InetAddress address, int port){
		String message = new String(data);
		PacketType type = lookupPacketType(message);
		String [] strData = message.substring(2).split(",");
		
		//server.display("Decompiled data: " + type + " "+ Arrays.toString(strData));

		switch(type){
			default:
			case INVALID:
				break;
			case LOGIN: 
				login(strData[0],address,port);
				server.sendDataAll(writePacket(PacketType.LOGIN, strData[0], ""), strData[0]);
				break;
			case DISCONNECT: 
				disconnect(strData[0]);
				server.sendDataAll(writePacket(PacketType.DISCONNECT, strData[0], ""), strData[0]);
				break;
			case RESTART: restart(strData[0],strData[1]);
						  //server.display("received restart from " + strData[0] + ": " + strData[1]);
				break;
			case MOVE: 
				move(strData[0], strData[1], strData[2]);
				server.sendDataAll(writePacket(PacketType.MOVE, strData[0], strData[1] + "," + strData[2]),strData[0]);
				break;
		}
	}
	public void restart(String username, String pause){
		for(int i = 0; i < server.clients.size(); i++){
			if(server.clients.get(i).username.equals(username))
				server.clients.get(i).dead = Boolean.parseBoolean(pause.trim());
		}
	}
	
	public void move(String username, String x, String y){
		//server.display("RECIEVED MOVE FROM: " +  username + " to: " + x + "," + y);
		ConnectedClient client = server.findByUsername(username);
		try{
			client.x = new Integer(x.trim());
			client.y = new Integer(y.trim());
		}catch(NumberFormatException e){server.display("ERROR FORMATING STRING TO NUMBER");}
	}
	
	public void login(String username, InetAddress address, int port){
		for(int i = 0; i < server.clients.size(); i ++)
			if(server.clients.get(i).username.equals(username)){
				server.display("ERROR: same username: " + server.clients.get(i).username);
				return;
			}
		
		ConnectedClient newclient = new ConnectedClient(username,address,port);
		server.clients.add(new ConnectedClient(username,address,port));
		
		for(int i = 0; i < server.clients.size(); i ++){
			if(!server.clients.get(i).username.equals(username)){
				server.sendDataTo(writePacket(PacketType.LOGIN, server.clients.get(i).username, ""), newclient);
				server.sendDataTo(writePacket(PacketType.MOVE, server.clients.get(i).username, server.clients.get(i).x + ","+ server.clients.get(i).y), newclient);
			}
		}
		
		server.display("(" + username + ") connected");
		
	}
	
	public void disconnect(String username){
		server.clients.remove(server.findByUsername(username));
		server.display("(" + username + ") has disconnected");
	}
	
	public byte[] writePacket(PacketType type, String username, String additional){
		return(type.id + username + ","+additional).getBytes();
	}
	
	public static PacketType lookupPacketType(String message){
		String id = message.substring(0,2).trim();
		for(PacketType p: PacketType.values())
			if(p.id.equals(id))
				return p;
		return PacketType.INVALID;
	}
	
}