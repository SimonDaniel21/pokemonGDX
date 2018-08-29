package net.simondaniel.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener.LagListener;
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

public class GameClient extends Client implements Network_interface {

	public ClientMonitor window;

	State state;

	String userName;

	HashSet<Packet> packetBuffer;

	public String errorMsg;

	public final String IP_ADRESS, SERVER_NAME;

	private List<MyListener> myListeners, addedMyListeners, removedMyListeners;

	long startTime;

	Thread connectThread;
	
	public GameClient(String ip, String name) {
		super();
		startTime = System.currentTimeMillis();
		IP_ADRESS = ip;
		SERVER_NAME = name;
		packetBuffer = new HashSet<Packet>();
		myListeners = new ArrayList<MyListener>();
		addedMyListeners = new ArrayList<MyListener>();
		removedMyListeners = new ArrayList<MyListener>();
		this.start();
		state = State.IDLE;

		// For consistency, the classes to be sent over the network are
		// client and server.
		Network.register(this);

		// ThreadedListener runs the listener methods on a different thread.
		//this.addListener(new ThreadedListener(new ClientListener(this)));
		this.addListener(new LagListener(40, 50, new ClientListener(this)));
	
		//extra thread handling the connection timeout
		connectThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					connect(5000, IP_ADRESS, Network.port);
					state = State.ESTABLISHED_CONNECTION;
				} catch (UnknownHostException e) {
					errorMsg = IP_ADRESS + " ist eine unbekannte IP Adresse";
					state = State.CANT_ESTABLISH_CONNECTION;
				} catch (IOException ex) {
					ex.printStackTrace();
					errorMsg = ex.getMessage();
					state = State.CANT_ESTABLISH_CONNECTION;
				}
			}
		});
	}

	public void sendConnectRequest() {

		if (state != State.IDLE) {
			System.err.println("already tried to connect");
			return;
		}
		state = State.CONNECTING;
		System.out.println("start");
		connectThread.start();

		System.out.println("end");
	}
	
	public boolean isConnecting() {
		return state == State.CONNECTING;
	}
	public boolean isConnectionFinished() {
		return state == State.CANT_ESTABLISH_CONNECTION || state == State.ESTABLISHED_CONNECTION;
	}
	
	public void resetConnection() {
		close();
		state = State.IDLE;
	}
	
	public boolean isLoggingIn() {
		return state == State.LOGGING_IN;
	}
	public boolean isLoginFinished() {
		return state == State.LOGGED_IN || state == State.DECLINED;
	}
	
	public void resetLogin() {
		if(state == State.DECLINED)
			state = State.ESTABLISHED_CONNECTION;
	}
	
	public boolean isLoggedIn() {
		return (state == State.LOGGED_IN);
	}


	public void sendLoginRequest(String name, String pw) {
		if (state != State.ESTABLISHED_CONNECTION) {
			System.err.println("client cant login");
			return;
		}
		this.userName = name;
		LoginC login = new LoginC();
		login.name = name;
		login.pw = pw;
		this.send(login);
		state = State.LOGGING_IN;
	}

	public void bindMonitor(ClientMonitor m) {
		window = m;
	}

	void verify(boolean answer) {
		state = answer ? State.LOGGED_IN : State.DECLINED;
	}

	public boolean waitForConnection() {
		while (state == State.CONNECTING) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return isConnected();
	}
	
	/**
	 * pauses the current thread until the client recieved a login response from the
	 * server and returns weather or not it has been accepted
	 * 
	 * @return
	 */
	public boolean waitForLogin() {

		while (state == State.LOGGING_IN) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return (state == State.LOGGED_IN);
	}

	public enum State {
		IDLE, ESTABLISHED_CONNECTION, CANT_ESTABLISH_CONNECTION, CONNECTING, LOGGING_IN, LOGGED_IN, DECLINED, DISCONNECTED;
	}

	public String userName() {
		return userName;
	}

	// -------------------------------------------------------------------------

	public int packetsReceived;

	

	public void disconnect(String reason) {
		errorMsg = reason;
		state = State.DISCONNECTED;
		close();
	}

	public State getState() {
		return state;
	}

	public static class Packet {
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
		for (Packet p : packetBuffer) {
			o = p.o;
			c = p.con;
			for (MyListener ml : myListeners) {
				ml.received(c, o);
			}

			//myListeners.addAll(addedMyListeners);
			for (MyListener ml : addedMyListeners) {
				ml.received(c, o);
				myListeners.add(ml);
			}
			myListeners.removeAll(removedMyListeners);
			addedMyListeners.clear();
			removedMyListeners.clear();

		}
		packetBuffer.clear();
	}

	public void addMyListener(MyListener ml) {
		removedMyListeners.remove(ml);
		addedMyListeners.add(ml);
	}

	public void removeMyListener(MyListener ml) {
		addedMyListeners.remove(ml);
		removedMyListeners.add(ml);
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

	public void sendLobbyCreateRequest(String lobbyName, GameMode mode) {
		LobbyCreateC p = new LobbyCreateC();
		p.name = lobbyName;
		p.gameMode = mode.ordinal();
		send(p);
	}
	
}
