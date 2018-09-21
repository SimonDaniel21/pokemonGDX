package net.simondaniel.network.server;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.chanels.Protocol;
import net.simondaniel.network.client.ChanelListener;
import net.simondaniel.network.client.ChanelListenerList;

public class UserConnection extends Connection{


	public String name;
	public Lobby lobby = null;
	List<Protocol> protocols;
	public ChanelListenerList channelListeners;
	
	public UserConnection() {
		super();
		name = null;
		protocols = new ArrayList<Protocol>();
	}
	
	public UserConnection(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public boolean isLoggedIn() {
		return name != null;
	}
	
	public void login(String name){
		this.name = name;
	}
	
	public void logout() {
		this.name = null;
	}
	
	public void addChannelListener(ChanelListener l){
		channelListeners.add(l);
		addListener(l);
	}
	
	/**
	 * eine Methode die Sichergeht dass die zu übertragene Nachricht vom Empfänger empfangen werden kann, sofern Server und Client 
	 * kompatibel sind
	 * @return
	 */
	public int sendTCPS(Object o) {
		
		Class<?> cl = o.getClass();
		
		boolean canSend = false;
		return -1;
	}
}
