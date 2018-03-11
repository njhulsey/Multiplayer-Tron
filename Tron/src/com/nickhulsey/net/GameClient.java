package com.nickhulsey.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.nickhulsey.main.Game;

public class GameClient extends Thread {

	public static String LOCAL_USERNAME;
	private static final int PORT = 1333;
	private DatagramSocket socket;
	private InetAddress ip;
	public Game game;
	public ClientPacketHandler packetHandler;
	public ArrayList<ConnectedClient> clients;
	
	public GameClient(Game game, String ipAddress) {
		this.game = game;
		packetHandler = new ClientPacketHandler(this);
		clients = new ArrayList<ConnectedClient>();
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {e.printStackTrace();
		} catch (UnknownHostException e) {e.printStackTrace();}

	}

	public void run() {
		System.out.println("Client Started with local username: " + LOCAL_USERNAME);
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			packetHandler.handlePacket(data, packet.getAddress(), packet.getPort());
			
		}
	}
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, PORT);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}