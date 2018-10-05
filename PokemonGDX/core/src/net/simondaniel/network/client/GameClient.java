package net.simondaniel.network.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener.LagListener;
import net.simondaniel.GameMode;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.network.Message;
import net.simondaniel.network.Network;
import net.simondaniel.network.Network_interface;
import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.chanels.MessageChannelEnd.InvalidMessageTypeError;
import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.server.Response.LoginS;

public class GameClient extends Client implements Network_interface {

	public ClientMonitor window;

	public State state;

	String userName;

	private HashSet<Packet> packetBuffer;

	public String errorMsg;

	public final String IP_ADRESS, SERVER_NAME;

	public ChanelListenerList myListeners;

	private ChanelListenerList addedMyListeners;

	private ChanelListenerList removedMyListeners;

	long startTime;

	Thread connectThread;

	public GameClient(String ip, String name) {
		super();
		startTime = System.currentTimeMillis();
		IP_ADRESS = ip;
		SERVER_NAME = name;
		packetBuffer = new HashSet<Packet>();
		myListeners = new ChanelListenerList();
		addedMyListeners = new ChanelListenerList();
		removedMyListeners = new ChanelListenerList();
		this.start();
		state = State.IDLE;

		// For consistency, the classes to be sent over the network are
		// client and server.
		Network.register(this);

		// ThreadedListener runs the listener methods on a different thread.
		// this.addListener(new ThreadedListener(new ClientListener(this)));
		this.addListener(new LagListener(10, 20, new ClientListener(this)));

		// extra thread handling the connection timeout
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
		if (state == State.DECLINED)
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
		this.send(MessageChannel.initialChannel, login);
		state = State.LOGGING_IN;
	}

	public void bindMonitor(ClientMonitor m) {
		window = m;
	}

	public void verify(boolean answer) {
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
		IDLE, ESTABLISHED_CONNECTION, CANT_ESTABLISH_CONNECTION, CONNECTING, LOGGING_IN, LOGGED_IN, DECLINED,
		DISCONNECTED;
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
		if(LaunchConfiguration.config == LaunchConfiguration.LOGGED_IN ||
				LaunchConfiguration.config == LaunchConfiguration.DEBUG_CLIENT) {
			Gdx.app.exit();
		}
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

	ReentrantLock packetLock = new ReentrantLock();

	public void addPacket(Connection con, Object o) {
		packetLock.lock();
 
		try {
			packetBuffer.add(new Packet(con, o));
		} finally {
			packetLock.unlock();

		}
	}

	public void handlePacketBuffer() {
		Object o;
		Connection c;
		packetLock.lock();
	
		try {
			for (Packet p : packetBuffer) {
				o = p.o;
				c = p.con;
				if(p.o instanceof LoginS)
					System.err.println("handling Loginanswer: " + ((LoginS)p.o).response + " " + myListeners.size() + " times");
				
				boolean handled = false;
				for (ChanelListener ml : myListeners) {
					ml.received(c, o);
					handled = true;
				}
				if(!handled) {
					System.err.println("a message of type " + o.getClass().getName() + " was not handled by channels: ");
					for(ChanelListener ml : myListeners) {
						System.err.println(ml.getChanel().getName());
					}
				}

				//myListeners.addAll(addedMyListeners);
				for (ChanelListener ml : addedMyListeners) {
					ml.received(c, o);
					myListeners.add(ml);
					System.err.println("added Chanel serious");
				}
				myListeners.removeAll(removedMyListeners);
				addedMyListeners.clear();
				removedMyListeners.clear();
			}
			packetBuffer.clear();
		}
		finally {
			packetLock.unlock();
		}
	}

	public void addChanelListener(ChanelListener ml) {
//		removedMyListeners.remove(ml);
//		myListeners.add(ml);
		addListener(ml);
	}

	public void removeChanelListener(ChanelListener ml) {
//		addedMyListeners.remove(ml);
//		removedMyListeners.add(ml);
		removeListener(ml);
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
	public void send(MessageChannel mc, Object o) {
		try {
			sendMessage(mc, o);
		} catch (InvalidMessageTypeError e) {
			e.printStackTrace();
		}
	}
	
	public void send(Object o) {
		send(myListeners.active.channel, o);
	}
	
	
	private void sendMessage(MessageChannel ch, Object msg) throws InvalidMessageTypeError{
		if(ch.canReceive(msg)) {
			Message m = new Message();
			m.channel = ch.ID;
			m.msg = msg;
			sendTCP(m);
		}else {
			throw new InvalidMessageTypeError(msg.getClass().getName(), ch, false);
		}
	}

	public void sendLobbyCreateRequest(String lobbyName, GameMode mode) {
		LobbyCreateC p = new LobbyCreateC();
		p.name = lobbyName;
		p.gameMode = mode.ordinal();
		send(MessageChannel.initialChannel, p);
	}

	public void sendLobbyJoinRequest(String lobbyName) {
		LobbyJoinC p = new LobbyJoinC();
		p.lobbyName = lobbyName;
		send(MessageChannel.initialChannel, p);
	}

}
