package net.simondaniel.network.server;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.UserJoinedS;

public class UserTrackSservice extends Sservice{
	
	List<NeuerUser> awareOfUsers;
	
	PlayerListS initPacket;
	
	PlayServer server;

	public UserTrackSservice(Connection con, PlayServer server) {
		super(con);
		awareOfUsers = new ArrayList<NeuerUser>();
		initPacket = new PlayerListS();
		this.server = server;
	}

	@Override
	protected void received(Connection c, Object o) {
		
	}
	
	@Override
	protected void onActivation(Connection c) {
		List<UserJoinedS> joinPackets = new ArrayList<UserJoinedS>();
		for(NeuerUser u : server.users) {
			if(u.account.isLoggedIn() && !isTracked(u)) {
				UserJoinedS p = new UserJoinedS();
				p.user = u.account.getName();
				joinPackets.add(p);
				track(u);
			}
		}
		
		initPacket.joined = new UserJoinedS[joinPackets.size()];
		for(int i = 0; i < initPacket.joined.length; i++) {
			initPacket.joined[i] = joinPackets.get(i);
		}
		c.sendTCP(initPacket);
		System.err.println("SENT PLAYER LIST " + initPacket.joined.length);
	}
	
	private boolean isTracked(NeuerUser user) {
		return awareOfUsers.contains(user);
	}
	
	private boolean track(NeuerUser user) {
		return awareOfUsers.add(user);
	}

}
