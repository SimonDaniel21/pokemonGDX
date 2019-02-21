package net.simondaniel.network.client;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.client.Request.UserListC;
import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.UserJoinedS;

public class UserTrackService extends Service{
	
	//UserTrackService others;
	
	List<String> others;

	public UserTrackService(Client c) {
		super(c);
		trackRequest = new UserListC();
		others = new ArrayList<String>();
	}

	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof PlayerListS) {
			PlayerListS p = (PlayerListS)o;
			for(UserJoinedS uj : p.joined) {
				others.add(uj.user);
			}
		}
	}
	
	UserListC trackRequest;

	@Override
	protected void onActivation() {
		send(trackRequest);
	}
	
	@Override
	protected void onDeactivation() {
		send(trackRequest);
	}
}
