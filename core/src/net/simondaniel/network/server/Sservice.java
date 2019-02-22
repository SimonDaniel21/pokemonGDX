package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public abstract class Sservice {
	
	private Listener listener;
	
	protected PlayServer server;
	
	protected abstract void received(Connection c, Object o);
	
	public Sservice(PlayServer server) {
		//this.con = con;
		this.server = server;
		
		final Sservice ref = this;
		listener = new Listener() {
			@Override
			public void received(Connection c, Object o) {
				ref.received(c, o);
			}
		};
	}
	
	public void activate(Connection c) {
		onActivation(c);
		c.addListener(listener);
	}

	
	public void deactivate(Connection c) {
		
		onDeactivation(c);
		c.removeListener(listener);
	}
	
//	/**
//	 * TODO
//	 * sends a tcp packet to all clients connected with the service. if the service is not active, this method will throw an error instead
//	 */
//	protected void send(Object o) {
//		if(!active)
//			throw new RuntimeException("tried to send a message on an inactive service");
//		
//		con.sendTCP(o);
//	}
	
	
	protected void onActivation(Connection c) {
		
	}
	
	protected void onDeactivation(Connection c) {
		
	}
}
