package net.simondaniel.network.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.esotericsoftware.kryonet.Client;

import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import net.simondaniel.network.Network;
import net.simondaniel.screens.NeuerGameScreen;

public class PlayClient extends Client{

	
	PlayClientListener l;
	
	public PlayClient() {
		final String IP_ADRESS = "127.0.0.1";
		l = new PlayClientListener();
		this.addListener(new ThreadedListener(l));
		final PlayClient ref = this;
		Network.register(this);
		start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					connect(5000, IP_ADRESS, Network.port);
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
		l.setWorld(neuerGameScreen);
	}
}
