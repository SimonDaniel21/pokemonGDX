package net.simondaniel.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public abstract class ResponseRequest<P, R> {

	// only one request can be pending at a time!
	
	public P packet; 
	
	private R reply;
	
	private Client client;
	
	private Listener replyListener;
	
	private boolean pending = false;
	
	public ResponseRequest(final Client client, P packet) {
		this.packet = packet;
		this.client = client;
		replyListener = new Listener() {
			@Override
			public void received(Connection c, Object o) {
				
				receive(o);
			}
		};
	}

	public void perform() {
		if(pending) {
			throw new IllegalStateException("cannot send request when a request is pending!");
		}
		client.addListener(replyListener);
		client.sendTCP(packet);
		pending = true;
	}
	
	protected abstract void receive(Object o);
	
	protected void setReplyPacket(R reply) {
		this.reply = reply;
		client.removeListener(replyListener);
		pending = false;
	}
	
	public R consume() {
		R r = reply;
		reset();
		return r;
	}
	
	public R getResponse() {
		return reply;
	}
	
	public void reset() {
		reply = null;
	}

}
