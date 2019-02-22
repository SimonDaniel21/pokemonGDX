package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.client.Request.UserListC;
import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.UserJoinedS;
import net.simondaniel.network.server.Response.UserLeftS;
import net.simondaniel.screens.tempNet.MyCallback;
import net.simondaniel.util.SyncableList;

public class UserTrackService extends Service{
	
	//UserTrackService others;
	
	//List<String> others;
	
	MyCallback onJoin, onLeave;
	
	public SyncableList<String> names;

	public UserTrackService(Client c) {
		super(c);
		trackRequest = new UserListC();
		names = new SyncableList<String>();
	}

	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof PlayerListS) {
			PlayerListS p = (PlayerListS)o;
			for(UserJoinedS uj : p.joined) {
				names.add(uj.user);
			}
		}
		
		if(o instanceof UserJoinedS) {
			UserJoinedS p = (UserJoinedS)o;
			names.add(p.user);

		}
		
		if(o instanceof UserLeftS) {
			UserLeftS p = (UserLeftS)o;
			names.remove(p.user);

			System.out.println("REMOVES " + p.user);
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
