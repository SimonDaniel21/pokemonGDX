package net.simondaniel.fabio.phisx;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;

import net.simondaniel.network.server.Response.WorldStateS;

public class PredictedWorld{
	
	HashMap<Integer, PredictedBody> idBodyMap;
	
	public PredictedWorld() {
		idBodyMap = new HashMap<Integer, PredictedBody>();
	}

	public void update(float delta) {
		for(PredictedBody b : idBodyMap.values()) {
			b.update(delta);
		}
	}

	public void renderDebug(Matrix4 projMatrix) {
		
	}

	public void syncWith(List<SyncBodyInfo> updates) {
		//System.out.println("predicted world syncing " + updates.size() + " bodies");
		for(SyncBodyInfo info :  updates) {
			PredictedBody b = idBodyMap.get(info.id);
			if(b == null) continue;
			
			//System.out.println("syncing body id " + info.id);
			idBodyMap.get(info.id).syncWith(info);
		}
	}

	public void setTo(WorldStateS p) {
		System.out.println("init world with " + p.bodies.size() + " bodies");
		for(SyncBodyInfo i : p.bodies) {
			System.out.println("add body id " + i.id);
			PredictedBody b = new PredictedBody();
			b.syncWith(i);
			idBodyMap.put(i.id, b);
		}
	}

	public PredictedBody getBody(int i) {
		
		return idBodyMap.get(i);
	}

}
