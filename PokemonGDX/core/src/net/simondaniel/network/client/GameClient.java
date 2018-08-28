package net.simondaniel.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import net.simondaniel.fabio.GameMode;
import net.simondaniel.network.Network;
import net.simondaniel.network.Network_interface;
import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.client.Request.MovementChandler;
import net.simondaniel.network.server.Response.AddEntityS;
import net.simondaniel.network.server.Response.AddEntityShandler;
import net.simondaniel.network.server.Response.AreaPacketS;
import net.simondaniel.network.server.Response.AreaPacketShandler;
import net.simondaniel.network.server.Response.LoadAreaS;
import net.simondaniel.network.server.Response.LoadAreaShandler;
import net.simondaniel.network.server.Response.MovementS;
import net.simondaniel.network.server.Response.MovementShandler;
import net.simondaniel.network.server.ServerMonitor;
import net.simondaniel.network.server.User;
import net.simondaniel.network.server.UserConnection;

public class GameClient extends Client implements Network_interface{
	
	
	public ClientMonitor window;
	
	State state;

	String userName;
	
	HashSet<Packet> packetBuffer;
	
	public String errorMsg;
	
	public final String IP_ADRESS, SERVER_NAME;
	
	List<MyListener> myListeners, addedMyListeners;
	
	long startTime;
	
	public GameClient (String ip, String name) {
		super();
		startTime = System.currentTimeMillis();
		IP_ADRESS = ip;
		SERVER_NAME = name;
		packetBuffer = new HashSet<Packet>();
		myListeners = new ArrayList<MyListener>();
		addedMyListeners = new ArrayList<MyListener>();
		this.start();
		state = State.IDLE;
		movementShandlers = new ArrayList<MovementShandler>();
		loadAreaShandlers = new ArrayList<LoadAreaShandler>();
		addEntityShandler = new ArrayList<AddEntityShandler>();
		areaPacketSHandlerhandlers = new ArrayList<AreaPacketShandler>();

		// For consistency, the classes to be sent over the network are
		//LobbyJoinS s = new LobbyJoinS(); registered by the same method for both the client and server.
		Network.register(this);

		// ThreadedListener runs the listener methods on a different thread.
		this.addListener(new ThreadedListener(new ClientListener(this)));
	}
	
	
	public boolean sendConnectRequest(){
		
		if(state != State.IDLE) {
			System.err.println("already tried to connect");
			return false;
		}
		try {
			this.connect(5000, IP_ADRESS, Network.port);
			this.state = State.ESTABLISHED_CONNECTION;
			return true;
			
		} 
		catch (UnknownHostException e) {
			errorMsg = IP_ADRESS + " ist eine unbekannte IP Adresse";
			return false;
		}
		catch (IOException ex) {
			ex.printStackTrace();
			errorMsg = ex.getMessage();
			state = State.CANT_ESTABLISH_CONNECTION;
			return false;
		}
		
	}
	
	public void sendLoginRequest(String name, String pw){
		if(state == State.CANT_ESTABLISH_CONNECTION || state == State.IDLE) {
			System.out.println("client cant login because it cannot reach the server");
			return;
		}
		this.userName = name;
		LoginC login = new LoginC();
		login.name = name;
		login.pw = pw;
		this.send(login);
		state = State.WATING_FOR_ANSWER;
	}
	
	public void bindMonitor(ClientMonitor m){
		window = m;
	}

	public void verify(boolean answer) {
		state = answer ? State.LOGGED_IN : State.DECLINED;
	}

	/**
	 * pauses the current thread until the client recieved a login response from the server
	 * and returns weather or not it has been accepted
	 * @return
	 */
	public boolean waitForVerification(long timeoutMillis) {
		long start = System.currentTimeMillis();
		long duration = 0;
		
		while(state == State.WATING_FOR_ANSWER) {
			try {
				duration = System.currentTimeMillis() - start;
				if(duration >= timeoutMillis) {
					errorMsg = "server timed out after " + timeoutMillis + "ms";
					return false;
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return (state == State.LOGGED_IN);
	}
	
	public enum State{
		IDLE,
		ESTABLISHED_CONNECTION,
		CANT_ESTABLISH_CONNECTION,
		WATING_FOR_ANSWER,
		LOGGED_IN,
		DECLINED,
		DISCONNECTED;
	}

	public String userName() {
		return userName;
	}
	
	
	//-------------------------------------------------------------------------
	
	private List<MovementShandler> movementShandlers;

	public void addMovementShandler(MovementShandler h){
		this.movementShandlers.add(h);
	}

	public void removeMovementShandler(MovementS h){
		this.movementShandlers.remove(h);
	}

	public void handle(MovementS p){
		for(MovementShandler h : movementShandlers){
			h.handle(p);
		}
	}
	
	private List<LoadAreaShandler> loadAreaShandlers;

	public void addLoadAreaShandler(LoadAreaShandler h){
		this.loadAreaShandlers.add(h);
	}

	public void removeLoadAreaShandler(LoadAreaShandler h){
		this.loadAreaShandlers.remove(h);
	}

	public void handle(LoadAreaS p){
		for(LoadAreaShandler h : loadAreaShandlers){
			h.handle(p);
		}
	}
	
	private List<AreaPacketShandler> areaPacketSHandlerhandlers;

	public void addAreaPacketShandler(AreaPacketShandler h){
		this.areaPacketSHandlerhandlers.add(h);
	}

	public void removeAreaPacketShandler(AreaPacketShandler h){
		this.areaPacketSHandlerhandlers.remove(h);
	}

	public void handle(AreaPacketS p){
		for(AreaPacketShandler h : areaPacketSHandlerhandlers){
			h.handle(p);
		}
	}

	private List<AddEntityShandler> addEntityShandler;

	public int packetsReceived;

	public void addAddEntityShandler(AddEntityShandler h){
		this.addEntityShandler.add(h);
	}

	public void removeAddEntityShandler(AddEntityShandler h){
		this.addEntityShandler.remove(h);
	}

	public void handle(AddEntityS p){
		for(AddEntityShandler h : addEntityShandler){
			h.handle(p);
		}
	}
	
	
	public void disconnect(String reason) {
		errorMsg = reason;
		state = State.DISCONNECTED;
	}
	
	public State getState(){
		return state;
	}
	
	public static class Packet{
		public Packet(Connection con, Object o) {
			this.con = con;
			this.o = o;
		}
		Object o;
		Connection con;
	}

	public void handlePacketBuffer() {
		Object o;
		Connection c;
		for(Packet p : packetBuffer){
			o = p.o;
			c = p.con;
			for(MyListener ml : myListeners) {
				ml.received(c, o);
			}
			
			for(MyListener ml : addedMyListeners) {
				ml.received(c, o);
				myListeners.add(ml);
			}
			addedMyListeners.clear();
			
		}
		packetBuffer.clear();
	}
	
	public void addMyListener(MyListener ml) {
		addedMyListeners.add(ml);
	}
	
	public void removeMyListener(MyListener ml) {
		myListeners.remove(ml);
	}
	
	public int packetCount() {
		return packetsReceived;
	}
	
	public float packetRate() {
		long timePassed = System.currentTimeMillis() - startTime;
		float seconds = (float) (timePassed / 1000);
		return packetsReceived / seconds;
	}


	@Override
	public void send(Object o) {
		this.sendTCP(o);
	}
	
	public void sendLobbyCreateRequest(String lobbyName, GameMode  mode){
		LobbyCreateC p = new LobbyCreateC();
		p.name = lobbyName;
		p.gameMode = mode.ordinal();
		send(p);
	}
}
