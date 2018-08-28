package net.simondaniel.game.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.simondaniel.Assets;
import net.simondaniel.Assets.Atlas;
import net.simondaniel.entities.Entity;
import net.simondaniel.entities.EntityInformation;
import net.simondaniel.myMapRenderer.State;
import net.simondaniel.game.client.attacks.Bite;
import net.simondaniel.game.client.attacks.Fireblast;
import net.simondaniel.game.client.gfx.AnimatedSprite;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.server.Response.MovementS;
import net.simondaniel.network.server.Response.MovementShandler;

public class Player extends Entity implements MovementShandler {

	GameClient client;

	BitmapFont font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));

	private String name;

	MoveAction move;
	int cd = 0;
	
	AnimatedSprite sprite;
	PokemonAnimation pkmn;
	
	List<Fireblast> blasts;
	List<Bite> bites;


	public Player(int x, int y, String name) {
		this.name = name;
		move = new MoveAction(this, 0.2f);
//		sprite = new AnimatedSprite("gfx/atlases/trainer.atlas", "stand_up",  "stand_left",  "stand_down",
//				"walk_up",  "walk_left",  "walk_down",
//				"run_up",  "run_left",  "run_down");
		blasts = new ArrayList<Fireblast>();
		bites = new ArrayList<Bite>();
	}


	@Override
	public void update(float delta) {
		//System.err.println("x: " + x);
		sprite.update(delta);
		pkmn.update(delta);
		List<Fireblast> dead = new ArrayList<Fireblast>();
		List<Bite> dead1 = new ArrayList<Bite>();
		for(Fireblast b : blasts){
			b.update(delta);
			if(!b.isAlive()){
				dead.add(b);
			}
		}
		for(Bite b : bites) {
			b.update(delta);
			if(!b.isAlive()){
				dead1.add(b);
			}
		}
		for(Fireblast b : dead){
			blasts.remove(b);
		}
		dead.clear();
		for(Bite b : dead1){
			bites.remove(b);
		}
		dead1.clear();
		//this.fireblast.setFrameDuration(elapsed);



	}

	public class State {
		public int corrections = 0;
	}

	@Override
	public void handle(MovementS p) {

		move.verify(p.approved);
	}
	


	public String getName() {
		return name;
	}


	@Override
	public EntityInformation getInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
