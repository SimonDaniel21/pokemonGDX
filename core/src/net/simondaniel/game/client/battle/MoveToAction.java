package net.simondaniel.game.client.battle;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import net.simondaniel.util.MyInterpolation;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ReversableAction;
import net.simondaniel.network.client.Request.MovementC;

public class MoveToAction extends ReversableAction<Position, MovingEntity>{
	
	private MovingEntity e;
	private float elapsedTime = 0, totalTime = 10.0f;
	private Position source, dest;
	private boolean playing = false;
	private float speed;
	
	public int xServer, yServer;

	/**
	 * speed in seconds per tile
	 * @param pikachu
	 * @param speed
	 */
	public MoveToAction(MovingEntity e, float speed) {
		super(e, PokemonGDX.game.client);
		this.speed = speed;
		this.e = this.object;
		
//		xServer = e.getX();
//		yServer = e.getY();
	}

	@Override
	protected void play(Position dest) {
		if(playing == true) {
			System.out.println("ALREADY PLAYING!!");
			
		}
		elapsedTime = 0;
		
		//this.source = new Position(e.x, e.y);
		this.dest = dest;
		float dist = source.distance(dest);
		totalTime = dist * (1 / speed);
		System.err.println("dest: " + dest.x + " | " + dest.y);
		playing = true;
		//e.move(dir);
		//e.dir = dir;
		//System.out.println("move to : (" + dest.x + "|" + dest.y + ")");
	}

	@Override
	protected void replay(Position dest) {
		//toExecute.add(dir.Opposing());
		
	}
	
	@Override
	public boolean execute(Position action) {
		
		return super.execute(action);
	}

	@Override
	public void update(float delta) {
		
		if(!playing && !toExecute.isEmpty()) {
			play(toExecute.poll());
		}

		if(playing) {
//			e.x = MyInterpolation.linear(elapsedTime / totalTime, source.x, dest.x);
//			e.y = MyInterpolation.linear(elapsedTime / totalTime, source.y, dest.y);
//			System.err.println("step: " + e.x + " | " + e.y + " @" + (elapsedTime / totalTime) + "  -  " + source.x + " | " + dest.x);
			elapsedTime += delta;
			if(elapsedTime >= totalTime) {
				playing = false;
			}
		}
	}

	@Override
	protected boolean acceptsNewActions() {
		return (!playing && toExecute.isEmpty());
	}

	@Override
	public void informServer(Position dest) {
		MovementC m = new MovementC();
		//m.dir = Direction.DOWN;
		m.corrections = corrections;
		m.id = -1;
		//client.send(m);
	}

	@Override
	public void approves(Position d) {
		
	}
}
