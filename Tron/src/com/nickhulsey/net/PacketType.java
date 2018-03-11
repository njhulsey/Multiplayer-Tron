package com.nickhulsey.net;

public enum PacketType{
	INVALID("-1"), LOGIN("00"), DISCONNECT("01"),RESTART("04"), 
	MOVE("02");
	
	public String id;
	PacketType(String id){
		this.id = id;
	}
}
