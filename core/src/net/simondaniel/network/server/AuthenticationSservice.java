package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.UserListC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.PlayerListS;

public class AuthenticationSservice extends Sservice{

	Listener requestServiceListener;
	
	public AuthenticationSservice(Connection c) {
		super(c);
		requestServiceListener = new Listener() {
			@Override
			public void received(Connection con, Object o) {
				NeuerUser u = (NeuerUser)con;
				if(o instanceof UserListC) {
					u.trackService.toggle();
				}
			}
		};
	}

	@Override
	protected void received(Connection con, Object o) {
		NeuerUser c = (NeuerUser)con;
		if(o instanceof LoginC) {
			LoginC p = (LoginC) o;
			System.out.println(p.name + " tries to connect with pw: " + p.pw);
			LoginS r = new LoginS();
			r.response = authenticate(p.name, p.pw);
			send(r);
			if(r.response.equals("success")) {
				c.addListener(requestServiceListener);
				c.account.login(p.name);
			}
			//r.response = gs.login(c, p.name, p.pw);
		}
	}
	
	private String authenticate(String name, String pw) {
		return "success";
	}

}
