package net.simondaniel.game.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import net.simondaniel.game.client.GameInstance;
import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.client.Request.RequestAreaC;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.Response.StartGameS;
import net.simondaniel.network.server.User;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.network.server.GameServer.Packet;

/**
 * basic abstract blueprint of a runnable game with a random number of players involved.
 * Games are running ON SERVER ONLY which can cause to some serious input laag with
 * a slow internet connection
 * @author simon
 *
 */
public class ServerGame implements Runnable{
	
	
	private GameInstance instance;
	
	HashSet<Packet> packetBuffer;
	
	private Lobby lobby;
	final private int minUsers, maxUsers;
	//public static final String NAME;
	public final String VERSION;
	public final Random RANDOM = new Random();
	
	private boolean running = false;
	protected GameServer server;
	
	public static final int UPS = 20;
	public static float ACTUAL_UPS = 0;
	public static long frame = 0;
	private long frames = 0;
	private long timer = 0;
	public static final double TIME_PER_FRAME = 1000000000 / UPS;
	public static final float TIME_PER_FRAME_SECS =  (float)(TIME_PER_FRAME / 1000000000.0);
	private static final double FPS_CHECK_DELAY_secs = 0.5f;
	private static final long FPS_CHECK_DELAY = (long) (FPS_CHECK_DELAY_secs*1000000000l);
	
	public ServerGame(GameServer server,  GameInstance instance){
		VERSION = "invalid";
		packetBuffer = new HashSet<GameServer.Packet>();
		this.server = server;
		this.minUsers = 2;
		this.maxUsers = 2;
		this.instance = instance;
	}

	public void endGame(){
		running = false;
	}
	
	protected void update(float delta) {
		instance.update(delta);
	}
	
	public void handlePackets() {
		Object o;
		UserConnection c;
		//System.out.println("handling " + packetBuffer.size() + "packets");
		for(Packet p : packetBuffer){
			c = p.con;
			o = p.o;
			instance.receivedFromClient(c, o);
		}
		packetBuffer.clear();
	}
	
	private void gameLoop(){
		handlePackets();
		update(TIME_PER_FRAME_SECS);
	}

	double delta = 0;
	long now;
	long lastTime = System.nanoTime();

	@Override
	public void run() {
		
		running = true;
		instance.start(lobby);
		
		while(running) {
			now = System.nanoTime();
			delta += (now - lastTime) / TIME_PER_FRAME;
			timer += now - lastTime;
			lastTime = now;
			if(delta >= 1) {
				
				gameLoop();
				frame++;
				frames++;
				delta--;
			}
			if(timer >= FPS_CHECK_DELAY) {
				ACTUAL_UPS =  (float) (frames/FPS_CHECK_DELAY_secs);
				timer -= FPS_CHECK_DELAY;
				frames = 0;
			}
			try {
				Thread.sleep(8);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * startst the game in a new Thread
	 */
	public void start(Lobby lobby){
		if(lobby == null || lobby.getUsers().length < minUsers || lobby.getUsers().length > maxUsers) {
			throw new IllegalArgumentException("es müssen " + minUsers + " bis " 
					+ maxUsers + "nutzer spielen!");
		}
		this.lobby = lobby;
		Thread t = new Thread(this);
		t.start();
		StartGameS p = new StartGameS();
		p.gameMode = 0;
		
		p.players = new String[lobby.size()];
		for(int i = 0; i < lobby.size(); i++)
			p.players[i] = lobby.getUsers()[i].user.name;
		
		lobby.sendToAllTCP(p);
		
		System.out.println("started game instance with players " + Arrays.toString(lobby.getUsers()));
	}
}
