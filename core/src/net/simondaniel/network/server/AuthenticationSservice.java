package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.server.Response.LoginS;

public class AuthenticationSservice extends Sservice{

	public AuthenticationSservice(Connection c) {
		super(c);
	}

	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof LoginC) {
			LoginC p = (LoginC) o;
			System.out.println(p.name + " tries to connect with pw: " + p.pw);
			LoginS r = new LoginS();
			r.response = authenticate(p.name, p.pw);
			send(r);
			//r.response = gs.login(c, p.name, p.pw);
		}
	}
	
	private String authenticate(String name, String pw) {
		return "success";
	}

}
