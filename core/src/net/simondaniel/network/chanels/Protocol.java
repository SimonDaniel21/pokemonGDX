package net.simondaniel.network.chanels;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.chanels.MessageChannelEnd.InvalidMessageTypeError;
import net.simondaniel.network.client.ChanelListener;
import net.simondaniel.network.server.UserConnection;

public abstract class Protocol {

	private MessageChannel chanel;
	
	protected ChanelListener listener;
	
	// wether the instance is the sender side
	private boolean isServer;

	public Protocol(MessageChannel chanel) {
		if(chanel == null)
			throw new IllegalArgumentException("channel cannot be null");
		this.chanel = chanel;
	}
	
	public void setListener(ChanelListener ch) {
	}


	/**
	 * checks first if the chanel is allowed to receive the Message of the Type of o
	 * @throws InvalidMessageTypeError 
	 */
	public void chanelReceive(UserConnection c, Object o) throws InvalidMessageTypeError {
		if(chanel.canReceive(o))
			listener.received(c, o);
		else
			throw new MessageChannelEnd.InvalidMessageTypeError(o.getClass().getName(), chanel);
	}
	
	public MessageChannel getChanel() {
		return chanel;
	}
	
	public boolean canReceive(Object o) {
		return isServer ? chanel.canReceive(o) : chanel.canSend(o);
	}
	
	public boolean canSend(Object o) {
		return isServer ? chanel.canSend(o): chanel.canReceive(o);
	}


	public Listener getListener() {
		return listener;
	}
}
