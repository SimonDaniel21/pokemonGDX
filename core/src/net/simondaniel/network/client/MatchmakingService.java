package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

public class MatchmakingService extends Service{

	public MatchmakingService(Client c) {
		super(c);
	}

	@Override
	protected void received(Connection c, Object o) {
		
	}

}
