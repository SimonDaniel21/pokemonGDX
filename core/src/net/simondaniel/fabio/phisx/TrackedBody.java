package net.simondaniel.fabio.phisx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class TrackedBody {
	
	int trackID;
	
	Body b;

	boolean inactive;

	Vector2 lastPos;
	
	private SyncBodyInfo syncInfo;


	public TrackedBody(Body b, int trackID) {
		this.b = b;
		inactive = true;
		lastPos = new Vector2(b.getPosition());
		syncInfo = new SyncBodyInfo();
		syncInfo.id = trackID;
	}

	/**
	 * syncinfo, or null if no synchronisation needed
	 * @return syncinfo, or null if no synchronisation needed
	 */
	public SyncBodyInfo sync() {
		Vector2 newPos = b.getPosition();
		if(!lastPos.equals(newPos)) {
			//System.err.println("body moved to " + newPos);
			lastPos.set(newPos);
			syncInfo.x = newPos.x;
			syncInfo.y = newPos.y;
			return syncInfo;
		}
	
		//System.out.println("body didnt move");
		return null;	
	}

	public SyncBodyInfo getSyncInfo() {
		Vector2 pos = b.getPosition();
		syncInfo.x = pos.x;
		syncInfo.y = pos.y;
		return syncInfo;
	}
}
