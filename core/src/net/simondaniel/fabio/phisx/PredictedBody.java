package net.simondaniel.fabio.phisx;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class PredictedBody {
	
	private float elapsedSinceLastSync = 0;
	private Vector2 pos, target;
	private float startX, startY;
	
	public void update(float delta) {
		elapsedSinceLastSync += delta;
		float a = Math.min(elapsedSinceLastSync / NetworkedWorld.SYNC_DELAY, 1);
		float nx = a*(target.x - startX) + startX;
		float ny = a*(target.y - startY) + startY;
		pos.set(nx, ny);
	}
	
	public PredictedBody() {
		pos = new Vector2();
		target = new Vector2();
	}
	
	public void syncWith(SyncBodyInfo info) {
		target.set(info.x, info.y);
		startX = pos.x;
		startY = pos.y;
		elapsedSinceLastSync = 0;
	}

	public float x() {
		return pos.x;
	}
	
	public float y() {
		return pos.y;
	}
}
