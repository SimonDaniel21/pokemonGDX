package net.simondaniel.network.server;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.UserJoinedS;
import net.simondaniel.network.server.Response.UserLeftS;

public class UserTrackSservice extends Sservice{

	PlayerListS initPacket;
	
	private ArrayList<NeuerUser> trackingUsers;

	public UserTrackSservice(PlayServer server) {
		super(server);
		//awareOfUsers = new ArrayList<NeuerUser>();
		initPacket = new PlayerListS();
		this.server = server;
		trackingUsers = new ArrayList<NeuerUser>();
	}

	@Override
	protected void received(Connection c, Object o) {
		
	}
	
	@Override
	protected void onActivation(Connection c) {
		NeuerUser user = (NeuerUser)c;
		informNewUser(user);
		informOthersAbout(user);
		trackingUsers.add(user);
	}
	
	@Override
	protected void onDeactivation(Connection c) {
		NeuerUser user = (NeuerUser)c;
		informOthersAboutLeave(user);
		trackingUsers.remove(user);
	}
	
	private void informNewUser(NeuerUser user) {
		List<UserJoinedS> joinPackets = new ArrayList<UserJoinedS>();
		for(NeuerUser u : server.auth.getLoggedInUsers()) {
			if(!user.track.tracks(u) && u != user) {
				UserJoinedS p = new UserJoinedS();
				p.user = u.account.getName();
				joinPackets.add(p);
				user.track.track(u);
			}
		}
		
		initPacket.joined = new UserJoinedS[joinPackets.size()];
		for(int i = 0; i < initPacket.joined.length; i++) {
			initPacket.joined[i] = joinPackets.get(i);
		}
		user.sendTCP(initPacket);
		//System.err.println("SENT PLAYER LIST " + initPacket.joined.length);
	}
	
	private void informOthersAbout(NeuerUser user) {
		UserJoinedS p = new UserJoinedS();
		p.user = user.account.getName();
		for(NeuerUser u : trackingUsers) {
			u.sendTCP(p);
		}
	}
	
	private void informOthersAboutLeave(NeuerUser user) {
		UserLeftS p = new UserLeftS();
		p.user = user.account.getName();
		for(NeuerUser u : trackingUsers) {
			if(u != user)
			u.sendTCP(p);
		}
	}
	
	
	static class UserTrackServiceData{
		
		public ArrayList<NeuerUser> awareOfUsers;
		
		public UserTrackServiceData() {
			awareOfUsers = new ArrayList<NeuerUser>();
		}

		public boolean tracks(NeuerUser u) {
			return awareOfUsers.contains(u);
		}
		
		public boolean track(NeuerUser u) {
			return awareOfUsers.add(u);
		}
		
		public boolean untrack(NeuerUser u) {
			return awareOfUsers.remove(u);
		}
		
	}
}
