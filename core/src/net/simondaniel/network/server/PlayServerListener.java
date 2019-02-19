package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.fabio.input.InputQueue;
import net.simondaniel.network.client.Request.ClientInputC;
import net.simondaniel.network.server.Response.WorldStateS;
import net.simondaniel.screens.tempNet.NetworkedWorld;

public class PlayServerListener extends Listener{

	InputQueue inputs;
	NetworkedWorld world;
	
	AuthenticationListener authListener;
	
	public PlayServerListener(PlayServer ref) {
		authListener = new AuthenticationListener();
	}
	
	@Override
	public void connected(Connection con) {
		WorldStateS p = world.getStatePacket();
		con.sendTCP(p);
		con.addListener(authListener);
		super.connected(con);
	}
	
	@Override
	public void received(Connection c, Object o) {
		
		if(o instanceof ClientInputC) {
			ClientInputC p = (ClientInputC) o;
//			System.out.println("received inputs");
//			System.out.println("tcp write buffer size " + c.getTcpWriteBufferSize());
//			System.out.println("rount trip time " + c.getReturnTripTime());
			for(int i = 0; i < p.inputs.length; i++) {
				//System.out.println(p.inputs[i]);
			}
			inputs.feedInputs(p.inputs);
		}
	}
	
	public void setInputQueue(InputQueue q) {
		this.inputs = q;
	}
	
	public void setWorld(NetworkedWorld w) {
		this.world = w;
	}
}
