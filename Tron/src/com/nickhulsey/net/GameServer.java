package com.nickhulsey.net;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class GameServer extends JPanel implements Runnable{
	
	private DatagramSocket socket;
	private static final int PORT = 1333;
	public ServerPacketHandler packetHandler;
	public Thread thread;
	
	public ArrayList<ConnectedClient> clients;
	
	//GUI STUFF
	public JTextArea body;
	
	public GameServer(){
		thread = new Thread(this);
		try {
			socket = new DatagramSocket(PORT);
			packetHandler = new ServerPacketHandler(this);
		} catch (SocketException e) {e.printStackTrace();}
		
		
		body = new JTextArea("Server started: ");
		body.setLineWrap(true);
		body.setWrapStyleWord(true);
		body.setTabSize(2);
		body.setEditable(false);
		JScrollPane scroller = new JScrollPane(body);
		this.setLayout(new GridLayout(1,1));
		this.add(scroller);
		
		
	}
	
	public void start(){
		if(!thread.isAlive())
			thread.start();
		else return;
		
	}
	
	public void run(){
		clients = new ArrayList<ConnectedClient>();
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {e.printStackTrace();}
			
			//when we receive a packet, send it to the handler
			packetHandler.handlePacket(data, packet.getAddress(), packet.getPort());
			restart();
			
		}
	}

	public void restart(){
		for(int i = 0; i < clients.size(); i++)
			if(clients.get(i).dead == false)return;
		
		for(int i = 0; i < clients.size(); i++){
			display("Sending restart packet....");
			sendDataTo(packetHandler.writePacket(PacketType.RESTART, clients.get(i).username, ""), clients.get(i));
		}
	}
	
	public void sendDataAll(byte[] data, String username){
		//send data to everyone besides username
		for(int i = 0; i < clients.size(); i++){
			if(clients.get(i).username.equals(username))
				continue;
			else sendDataTo(data,clients.get(i));
		}
	
	}
	public void sendDataTo(byte[] data, ConnectedClient client){
		//the string username is who recently sent the package.
		DatagramPacket packet = new DatagramPacket(data,data.length,client.address,client.port);
		try {
			socket.send(packet);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public ConnectedClient findByUsername(String username){
		for(int i = 0; i < clients.size(); i++)
			if(clients.get(i).username.equals(username))
				return clients.get(i);
		return null;
	
	}
	
	public void display(String text){
		body.append("\n" + text);
		body.setCaretPosition(body.getDocument().getLength());
	}
	
}
