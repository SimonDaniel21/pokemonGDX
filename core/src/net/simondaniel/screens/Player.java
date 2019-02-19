package net.simondaniel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import actions.ActionPool;
import actions.ActionQueue;
import actions.pokemon.LinInterpolateVecAction;
import actions.pokemon.MoveAction;
import actions.pokemon.MoveBodyToAction;
import net.simondaniel.fabio.input.MyInput;
import net.simondaniel.fabio.phisx.PhysicObject;
import net.simondaniel.fabio.phisx.PhysicsWorld;
import net.simondaniel.game.client.gfx.AnimatedSprite;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.pokes.Pokemon;
import net.simondaniel.pokes.PokemonInstance;

public class Player {

	private PokemonAnimation animation;

	ActionPool actions;
	ActionQueue moveQueue;
	
	//Vector2 pos;
	
	Pokemon p;
	
	PhysicObject obj;
	
	public Player(Pokemon p, PhysicsWorld world) {
		this.p = p;
		//pos = new Vector2();
		
		
		actions = new ActionPool();
		moveQueue = new ActionQueue();
		actions.runAction(moveQueue);
		
		obj = world.createPhysicsObject(1, 1);
		
		//body = world.createBody(def);
		CircleShape cs = new CircleShape();
		cs.setRadius(0.8f);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		fdef.isSensor = true;
		obj.createFixture(fdef);
		animation = new PokemonAnimation(p);
		animation.setScale(2.0f);
		animation.runAnimation(AnimationType.MOVEMENT, AnimationDirection.RIGHT);
	}

	AnimationDirection lastDir = AnimationDirection.NO_DIRECTION;
	AnimationDirection newDir = AnimationDirection.NO_DIRECTION;
	
	public void handleInput(MyInput in){
		float amount = 0.4f;
		

//		 float desiredVel = body.getLinearVelocity().x * 0.8f;
//		
//		if (Gdx.input.isKeyPressed(Keys.W)) {
//			//animation.move(0, amount);
//			
//			newDir = AnimationDirection.UP;
//		}
//		if (Gdx.input.isKeyPressed(Keys.A)) {
//			//animation.move(-amount, 0);
//			newDir = AnimationDirection.LEFT;
//			desiredVel = Math.max(body.getLinearVelocity().x - 0.4f, -4);
//		}
//		if (Gdx.input.isKeyPressed(Keys.S)) {
//			//animation.move(0, -amount);
//			newDir = AnimationDirection.DOWN;
//		}
//		 System.out.println(". "+ desiredVel);
//		if (Gdx.input.isKeyPressed(Keys.D)) {
//			//animation.move(amount, 0);
//			newDir = AnimationDirection.RIGHT;
//			//body.applyLinearImpulse(new Vector2(1099, 0), body.getWorldCenter(), true);
//			//body.setLinearVelocity(1,0);
//
//			desiredVel = Math.min(body.getLinearVelocity().x + 0.4f, 4);
//		}
//		Vector2 vel = body.getLinearVelocity();
//		   
//	    float velChange = desiredVel - vel.x;
//	    float impulse =body.getMass()* velChange; //disregard time factor
//	 
//	    body.applyLinearImpulse(new Vector2(impulse, 0), body.getWorldCenter(), true);
//		
		if(newDir != lastDir) {
			//animation.runAnimation(AnimationType.MOVEMENT, newDir);
			//lastDir = newDir;
		}
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && !moveQueue.isBusy()) {
			
			//moveQueue.add(new LinInterpolateVecAction(pos, in.getWorldX(), in.getWorldY(), 50.0f));
			//moveQueue.add(new MoveAction(this, in.getWorldX(), in.getWorldY()));
			//moveQueue.add(new MoveBodyToAction(body, in.getWorldX(), in.getWorldY()));
		}
	}
	
	public void draw(Batch b) {
		animation.draw(b);
	}

	public void update(float delta) {
		actions.update(delta);
		animation.update(delta);
	}

	public void move(float x, float y) {
		
	}
	
	public PokemonAnimation getAnimation() {
		
		return animation;
	}

	public Pokemon getPokemon() {
		return p;
	}

	public Vector2 getPos() {
		return null;
	}

	public float getX() {
		return obj.getX();
	}
	
	public float getY() {
		return obj.getY();
	}
}
