package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.server.Response.ServerInfoS;
import net.simondaniel.network.server.Response.SyncBodiesS;
import net.simondaniel.network.server.Response.WorldStateS;
import net.simondaniel.screens.NeuerGameScreen;

public class PlayClientListener extends Listener{

	NeuerGameScreen world;
	
	private PlayClient client;
	
	public PlayClientListener(PlayClient c) {
		this.client = c;
	}

	@Override
	public void received(Connection c, Object o) {
		
		if(o instanceof ServerInfoS) {
			ServerInfoS p = (ServerInfoS)o;
			//p.name;
		}
		
//		if(o instanceof SyncBodiesS) {
//			SyncBodiesS p = (SyncBodiesS)o;
//			world.feedInput(p.updates);
//		}
//		
//		if(o instanceof WorldStateS) {
//			WorldStateS p = (WorldStateS)o;
//			world.setWorldState(p);
//		}
	}

}
