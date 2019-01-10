package net.simondaniel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import actions.ActionPool;
import actions.ActionQueue;
import actions.pokemon.LinInterpolateVecAction;
import actions.pokemon.MoveAction;
import net.simondaniel.fabio.input.MyInput;
import net.simondaniel.game.client.gfx.AnimatedSprite;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.pokes.Pokemon;

public class Player {

	private PokemonAnimation animation;

	ActionPool actions;
	ActionQueue moveQueue;
	
	Vector2 pos;
	
	Pokemon p;
	
	public Player(Pokemon p, World w) {
		this.p = p;
		pos = new Vector2();
		animation = new PokemonAnimation(p, pos);
		animation.setScale(2.0f);
		animation.runAnimation(AnimationType.MOVEMENT, AnimationDirection.RIGHT);
		actions = new ActionPool();
		moveQueue = new ActionQueue();
		actions.runAction(moveQueue);
	}
	AnimationDirection lastDir = AnimationDirection.NO_DIRECTION;
	AnimationDirection newDir = AnimationDirection.NO_DIRECTION;
	
	public void handleInput(MyInput in){
		float amount = 0.4f;
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			//animation.move(0, amount);
			
			newDir = AnimationDirection.UP;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			//animation.move(-amount, 0);
			newDir = AnimationDirection.LEFT;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			//animation.move(0, -amount);
			newDir = AnimationDirection.DOWN;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			//animation.move(amount, 0);
			newDir = AnimationDirection.RIGHT;
		}
		
		if(newDir != lastDir) {
			animation.runAnimation(AnimationType.MOVEMENT, newDir);
			lastDir = newDir;
		}
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			System.err.println("pressed @" + in.getWorldX() + "|" + in.getWorldY());
			//moveQueue.add(new LinInterpolateVecAction(pos, in.getWorldX(), in.getWorldY(), 50.0f));
			moveQueue.add(new MoveAction(this, in.getWorldX(), in.getWorldY()));
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
		animation.move(x, y);
	}

	public float getX() {
		return animation.getX();
	}

	public float getY() {
		return animation.getY();
	}

	public PokemonAnimation getAnimation() {
		
		return animation;
	}

	public Pokemon getPokemon() {
		return p;
	}

	public Vector2 getPos() {
		return pos;
	}
	
}
