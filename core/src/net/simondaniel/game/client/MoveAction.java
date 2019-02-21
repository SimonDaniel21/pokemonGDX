//package net.simondaniel.game.client;
//
//import net.simondaniel.entities.Entity;
//import net.simondaniel.game.client.battle.MovingEntity;
//import net.simondaniel.game.client.battle.Pikachu;
//import net.simondaniel.game.client.battle.Schiggy;
//import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
//import net.simondaniel.network.client.Request.MovementC;
//
//public class MoveAction extends ReversableAction<AnimationDirection, Entity>{
//	
//	private Entity e;
//	private float elapsedTime = 0;
//	private AnimationDirection dir;
//	private boolean interpolating = false;
//	private float speed;
//	
//	public int xServer, yServer;
//
//	/**
//	 * speed in seconds per tile
//	 * @param pikachu
//	 * @param speed
//	 */
//	public MoveAction(Entity e, float speed) {
//		super(e, PokemonGDX.game.client);
//		this.speed = speed;
//		e = this.object;
//		//xServer = e.getX();
//		//yServer = e.getY();
//	}
//
//	@Override
//	protected void play(AnimationDirection dir) {
//		elapsedTime = 0;
//		this.dir = dir;
//		interpolating = true;
//		//e.move();
//		//((Schiggy) e).dir = dir;
//	}
//
//	@Override
//	protected void replay(AnimationDirection dir) {
//		toExecute.add(dir.Opposing());
//		
//	}
//
//	@Override
//	public void update(float delta) {
//		
//		if(!interpolating && !toExecute.isEmpty()) {
//			play(toExecute.poll());
//		}
//		
//		if(interpolating){
//			elapsedTime += delta;
//			int i = (int)((elapsedTime / speed)*16);
//			switch(dir){
//			case UP:
//				//e.iy = i;
//				break;
//			case DOWN:
//				//e.iy = -i;
//				break;
//			case LEFT:
//				//e.ix = -i;
//				break;
//			case RIGHT:
//				//e.ix = i;
//				break;
//			}
//		}
//		
//		if(elapsedTime >= speed && interpolating){
//			//e.ix = 0;
//			//e.iy = 0;
//			interpolating = false;
//			switch(dir){
//			case UP:
//				//e.y+=1;
//				break;
//			case DOWN:
//				//e.y-=1;
//				break;
//			case LEFT:
//				//e.x-=1;
//				break;
//			case RIGHT:
//				//e.x+=1;
//				break;
//			}
//			//e.stopMove();
//		}
//	}
//
//	@Override
//	protected boolean acceptsNewActions() {
//		return (toExecute.isEmpty() && !interpolating);
//	}
//
//	@Override
//	public void informServer(AnimationDirection dir) {
//		MovementC m = new MovementC();
//		//m.dir = dir;
//		m.corrections = corrections;
//		m.id = -1;
//		//client.send(m);
//	}
//
//	@Override
//	public void approves(AnimationDirection d) {
//		switch(d){
//		case UP:
//			yServer+=1;
//			break;
//		case DOWN:
//			yServer-=1;
//			break;
//		case LEFT:
//			xServer-=1;
//			break;
//		case RIGHT:
//			xServer+=1;
//			break;
//		}
//	}
//
//}
