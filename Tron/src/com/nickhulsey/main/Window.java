package com.nickhulsey.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.nickhulsey.input.InputHandler;
import com.nickhulsey.net.GameClient;
import com.nickhulsey.net.GameServer;
import com.nickhulsey.net.PacketType;

public class Window extends JPanel implements Runnable, WindowListener{

	public static final int WIDTH = (int)(640 * 1.5);
	public static final int HEIGHT = (int)(480 * 1.5);
	public static final int SCALE = 1;
	public static final int TILE_SIZE = 8 * SCALE;
	
	private Thread thread;
	private boolean isRunning = false;
	
	private Game game;
	public InputHandler input;
	
	public GameClient client;
	private GameServer server;	

	public Window(){
		input = new InputHandler();
		game = new Game(input, this);
		thread = new Thread(this);
		this.setDoubleBuffered(true);

		String ip = new String(JOptionPane.showInputDialog(null, "Enter an IP address", "connection",JOptionPane.PLAIN_MESSAGE));
		client = new GameClient(game, ip);
		client.LOCAL_USERNAME = new String(JOptionPane.showInputDialog(null, "Enter a username", "pick a username",JOptionPane.PLAIN_MESSAGE));
		client.start();
		//tell the server we are connecting
		client.sendData(client.packetHandler.writePacket(PacketType.LOGIN, client.LOCAL_USERNAME, ""));
		game.client = client;
	
	}
	
	public void start(){
		if(!isRunning){
			isRunning = true;
			game.init();
			thread.start();
		}else return;
	}
	
	
	public void run(){
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
				repaint();
			}
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				//System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void tick(){
		game.tick();
	}
	
	public void paintComponent(Graphics g){
		if(thread.isAlive()){
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(new Color(0,0,0));
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			game.render(g2d);
		}
	}
	
	public static void main(String [] args){
		JFrame frame = new JFrame("MULTIPLAYER GAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GameServer server;
		if(JOptionPane.showConfirmDialog(null,"Run the server?", "",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			server = new GameServer();
			frame.setSize(320, 480);
			frame.add(server);
			server.start();
		}
		else {
			Window panel = new Window();
			frame.setSize(Window.WIDTH, Window.HEIGHT);
			frame.add(panel);
			frame.addMouseListener(panel.input);
			frame.addKeyListener(panel.input);
			frame.addMouseMotionListener(panel.input);
			frame.addFocusListener(panel.input);
			frame.addWindowListener(panel);
			frame.setResizable(true);
			panel.start();
		}
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void windowOpened(WindowEvent e) {
		
	}

	public void windowClosing(WindowEvent e) {
		if(server == null)
			client.sendData(client.packetHandler.writePacket(PacketType.DISCONNECT, client.LOCAL_USERNAME, ""));
	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {
		
	}

	public void windowDeiconified(WindowEvent e) {
		
	}

	public void windowActivated(WindowEvent e) {
	
	}
		
	public void windowDeactivated(WindowEvent e) {
		
	}
}