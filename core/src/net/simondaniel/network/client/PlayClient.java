package net.simondaniel.network.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterUDP;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import net.simondaniel.network.Network;
import net.simondaniel.network.server.Response.ServerInfoS;
import net.simondaniel.screens.NeuerGameScreen;
import net.simondaniel.screens.tempNet.MyCallback;

public class PlayClient extends Client{

	public String SERVER_NAME = null;
	public AuthenticationService authService;
	public MatchmakingService matchService;
	public UserTrackService trackingService;
	
	private Listener serverNameListener;
	
	Object infoLock = new Object();
	
	public PlayClient() {
		final String IP_ADRESS = "127.0.0.1";
		
		authService = new AuthenticationService(this);
		matchService = new MatchmakingService(this);
		trackingService = new UserTrackService(this);
		
		authService.setAuthenticationCallback(new MyCallback() {
			@Override
			public void perform() {
				authService.deactivate();
				matchService.activate();
			}
		});
		
		
		serverNameListener = new Listener() {
			@Override
			public void received(Connection c, Object o) {
				
				if(o instanceof ServerInfoS) {
					ServerInfoS p = (ServerInfoS)o;
					synchronized (infoLock) {
						SERVER_NAME = p.name;
					}
				}
			}
		};
		
		Network.register(this);
		start();
	}
	
	/**
	 * 
	 * @param host actual IP
	 * @param name just a name
	 */
	public void connectAsync(final String host) {
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					connectBlocking(host);
				}
			});
	}
	
	public String connectBlocking(final String host) {
		SERVER_NAME = null;
		addListener(serverNameListener);
		int CONNECT_TIMEOUT = 5000;
		long INFO_TIMEOUT = 1000;
		
		try {
			
			connect(CONNECT_TIMEOUT, host, Network.port);
			long endTime = System.currentTimeMillis() + INFO_TIMEOUT;
			
			synchronized (infoLock) {
				while (!isConnected() && System.currentTimeMillis() < endTime) {
					try {
						infoLock.wait(100);
					} catch (InterruptedException ignored) {
					}
				}
			}
			
			if(!isConnected()) {
				this.close();
				return "connected but\ndid not receive server info in time";
			}
			return "connected";
		} catch (UnknownHostException e) {
			return e.getMessage();
		} catch (IOException ex) {
			return ex.getMessage();
		}
	}
	
	@Override
	public boolean isConnected() {
		return super.isConnected() && SERVER_NAME != null;
	}

	public void setWorld(NeuerGameScreen neuerGameScreen) {
		//l.setWorld(neuerGameScreen);
	}
	
}
