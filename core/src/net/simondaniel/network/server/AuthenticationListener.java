package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.fabio.input.InputQueue;
import net.simondaniel.network.client.Request.ClientInputC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.WorldStateS;
import net.simondaniel.screens.tempNet.NetworkedWorld;

public class AuthenticationListener extends Listener{

	@Override
	public void connected(Connection con) {
		System.out.println("connected to authentication");
		super.connected(con);
	}
	
	@Override
	public void received(Connection c, Object o) {
		if(o instanceof LoginC) {
			LoginC p = (LoginC) o;
			System.out.println(p.name + " tries to connect with pw: " + p.pw);
			LoginS r = new LoginS();
		}
	}
}
