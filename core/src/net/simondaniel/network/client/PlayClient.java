package net.simondaniel.network.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.esotericsoftware.kryonet.Client;

import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import net.simondaniel.network.Network;
import net.simondaniel.screens.NeuerGameScreen;
import net.simondaniel.screens.tempNet.MyCallback;

public class PlayClient extends Client{

	
	public AuthenticationService authService;
	public MatchmakingService matchService;
	
	
	public PlayClient() {
		final String IP_ADRESS = "127.0.0.1";
		
		authService = new AuthenticationService(this);
		matchService = new MatchmakingService(this);
		
		authService.setAuthenticationCallback(new MyCallback() {
			@Override
			public void perform() {
				authService.deactivate();
				matchService.activate();
			}
		});
		
		
		final PlayClient ref = this;
		Network.register(this);
		start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					connect(5000, IP_ADRESS, Network.port);
					authService.activate();
					System.out.println("connected: " + ref.isConnected());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}

	public void setWorld(NeuerGameScreen neuerGameScreen) {
		//l.setWorld(neuerGameScreen);
	}
}
