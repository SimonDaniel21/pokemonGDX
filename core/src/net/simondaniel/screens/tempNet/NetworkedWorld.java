package net.simondaniel.screens.tempNet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import net.simondaniel.fabio.phisx.SyncBodyInfo;
import net.simondaniel.network.server.PlayServer;
import net.simondaniel.network.server.Response.SyncBodiesS;
import net.simondaniel.network.server.Response.WorldStateS;

public class NetworkedWorld {
	
	float timeStep = 1/60f;
	
	float syncTimer = 0;;
	public static final float SYNC_DELAY = 0.1f;
	
	public static final float PIXELS_PER_METER = 32;

	World b2dWorld;
	List<TrackedBody> bodies;
	List<SyncBodyInfo> syncInfos;
	SyncBodiesS packet;
	WorldStateS statePacket;
	
	PlayServer server;
	

	public NetworkedWorld(PlayServer server, World b2dWorld) {
		this.b2dWorld = b2dWorld;
		this.server = server;
		bodies = new ArrayList<TrackedBody>();
		packet = new SyncBodiesS();
		packet.updates = new ArrayList<SyncBodyInfo>();
		syncInfos = packet.updates;
		
		statePacket = new WorldStateS();
		statePacket.bodies = new ArrayList<SyncBodyInfo>();
		
	}

	public void addBody(Body b, int trackID) {
		bodies.add(new TrackedBody(b, trackID));
	}
	
	public Body createTrackedBody(BodyDef def, int trackID) {
		Body b = b2dWorld.createBody(def);
		bodies.add(new TrackedBody(b, trackID));
		return b;
	}
	
	public Body createBody(BodyDef def) {
		return b2dWorld.createBody(def);
	}
	
	public void syncPositions(float delta) {
		//System.out.println("syncing " + delta + " - " + bodies.size());
		for(TrackedBody tb : bodies) {
			SyncBodyInfo info = tb.sync();
			if(info != null)
				syncInfos.add(info);
		}
		
		server.sendToAllTCP(packet);
		syncInfos.clear();
	}

	public void step() {
		b2dWorld.step(timeStep, 8, 3);
		syncTimer += timeStep;
		
		//System.out.println("stepping " + syncTimer + ","  + timeStep);
		if(syncTimer >= SYNC_DELAY) {
			syncPositions(SYNC_DELAY);
			syncTimer -= SYNC_DELAY;
		}
	}
	
	
	//TODO
	public WorldStateS getStatePacket() {
		statePacket.bodies.clear();
		for(TrackedBody tb : bodies) {
			SyncBodyInfo info = tb.getSyncInfo();
			statePacket.bodies.add(info);
		}
		
		return statePacket;
	}

	
}
