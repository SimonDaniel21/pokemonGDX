package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.Synchronized;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.screens.tempNet.MyCallback;

public class AuthenticationService extends Service{
	
	private MyCallback onAuth;
	
	public Synchronized<String> response;
	private Client client;
	private boolean authenticated;
	
	public AuthenticationService(Client c) {
		super(c);
		response = new Synchronized<String>();
	}
	
	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof LoginS) {
			LoginS p = (LoginS) o;
			if(p.response.equals("success")) {
				onAuth.perform();
				authenticated = true;
			}
			response.set(p.response);
		}
	}
	
	public void login(String name, String pw) {
		LoginC p = new LoginC();
		p.name = name;
		p.pw = pw;
		send(p);
		//System.out.println(p.name + " tried to connect with pw: " + p.pw);
	}

	public void setAuthenticationCallback(MyCallback c) {
		this.onAuth = c;
	}
}
