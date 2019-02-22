package net.simondaniel.network.server;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.UserListC;
import net.simondaniel.network.server.Response.LoginS;

public class AuthenticationSservice extends Sservice{

	Listener requestServiceListener, connectionLostListener;
	
	private ArrayList<NeuerUser> loggedInUsers;
	
	public AuthenticationSservice(final PlayServer server) {
		super(server);
		loggedInUsers = new ArrayList<NeuerUser>();
		requestServiceListener = new Listener() {
			@Override
			public void received(Connection con, Object o) {
				NeuerUser u = (NeuerUser)con;
				if(o instanceof UserListC) {
					server.track.activate(con);
				}
			}
		};
		
		connectionLostListener = new Listener() {
			public void disconnected(Connection con) {
				NeuerUser u = (NeuerUser)con;
				server.track.deactivate(con);
				server.match.deactivate(con);
				logout(u);
			};
		};
	}

	@Override
	protected void received(Connection con, Object o) {
		NeuerUser c = (NeuerUser)con;
		if(o instanceof LoginC) {
			LoginC p = (LoginC) o;
			System.out.println(p.name + " tries to connect with pw: " + p.pw);
			
			login(c, p.name, p.pw);
		}
	}
	
	private boolean loggedIn(String name) {
		for(NeuerUser u : loggedInUsers) {
			if(u.account.getName().equals(name))
				return true;
		}
		return false;
	}
	
	private void login(NeuerUser con, String name, String pw) {

		LoginS p = new LoginS();
		String r = authenticate(con, name, pw);
		p.response = r;
		
		con.sendTCP(p);
		
		if(r.equals("success")) {
			con.addListener(requestServiceListener);
			con.addListener(connectionLostListener);
			con.account.login(name);
			loggedInUsers.add(con);
			server.auth.deactivate(con);
			server.match.activate(con);
		}
		
	}
	
	private String authenticate(NeuerUser con, String name, String pw) {
		if(loggedIn(name))
			return "account is already logged in";
		return "success";
	}
	
	private void logout(NeuerUser con) {
		con.account.logout();
		con.removeListener(connectionLostListener);
		con.removeListener(requestServiceListener);
		loggedInUsers.remove(con);
	}

	public List<NeuerUser> getLoggedInUsers() {
		
		return loggedInUsers;
	}

}
