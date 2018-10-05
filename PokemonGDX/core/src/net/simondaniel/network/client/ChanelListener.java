package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.Message;
import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.chanels.MessageChannelEnd;
import net.simondaniel.network.chanels.MessageChannelEnd.InvalidMessageTypeError;
import net.simondaniel.network.chanels.MessageChannelEnd.MessageNotHandledError;
import net.simondaniel.network.server.Response.LoginS;

public abstract class ChanelListener extends Listener{
	
	private ChanelListenerList list;
	MessageChannel channel;
	boolean isServer;
	
	private static Class<?>[] frameWorkMessages= FrameworkMessage.class.getDeclaredClasses();
	
	public ChanelListener(MessageChannel channel, boolean role, ChanelListenerList list) {
		this.channel = channel;
		this.isServer = role;
		this.list = list;
	}

	public void setRole(boolean serverRole) {
		this.isServer = serverRole;
	}
	
	@Override
	public final void received(Connection c, Object obj) {
		
		
		if(obj instanceof Message) {
			Message m = (Message) obj;
			Object o = m.msg;
			System.out.println("received message from type " +o.getClass().getName() + " from channel " + channel.getName());
			System.out.println("our channel: " + channel.ID + " other channel " + m.channel);

			if(m.channel != channel.ID) {
				System.out.println("not handled");
				return;
			}
			
			boolean canReceive = isServer ? channel.canReceive(o) : channel.canSend(o);
			
			if(canReceive) {
				try {
					list.startReceive(this);
					channelReceive(c, o);
				}
				finally {
					list.stopReceive();
				}
			}
			else
			{
				try {
					throw new MessageChannelEnd.InvalidMessageTypeError(o.getClass().getName(), channel);
				} catch (InvalidMessageTypeError e) {
					e.printStackTrace();
				}
			}
		}
		else {
			System.out.println("received NON message from type " + obj.getClass().getName());
		}
	}
	
	protected abstract void channelReceive(Connection c, Object o);

	public MessageChannel getChanel() {
		return channel;
	}
	
}
