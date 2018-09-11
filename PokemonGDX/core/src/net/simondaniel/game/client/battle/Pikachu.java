package net.simondaniel.game.client.battle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.simondaniel.Assets;
import net.simondaniel.Assets.Atlas;
import net.simondaniel.entities.Entity;
import net.simondaniel.fabio.input.BattleInput;
import net.simondaniel.fabio.input.InputHandler;
import net.simondaniel.game.client.MoveAction;
import net.simondaniel.game.client.attacks.Bite;
import net.simondaniel.game.client.attacks.Fireblast;
import net.simondaniel.game.client.gfx.AnimatedSprite;
import net.simondaniel.game.client.gfx.AnimationLayout.PokemonAnimationLayout;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.server.Response.MovementS;
import net.simondaniel.network.server.Response.MovementShandler;

public class Pikachu extends MovingEntity implements MovementShandler, InputHandler<BattleInput> {

	GameClient client;

	BitmapFont font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));

	private String name;

	MoveToAction move;
	int cd = 0;
	
	PokemonAnimation pkmn;
	
	List<Fireblast> blasts;
	List<Bite> bites;
	
	public AnimationDirection dir = AnimationDirection.DOWN;

	public Pikachu(int x, int y, String name) {
		this.name = name;
		move = new MoveToAction(this, 0.25f);

		pkmn = new PokemonAnimation(PokemonAnimationLayout.pikachu.LAYOUT, Assets.manager.get(Atlas.PIKACHU.assetDescriptor));
		blasts = new ArrayList<Fireblast>();
		bites = new ArrayList<Bite>();
	}
	

	@Override
	public void handle(BattleInput input) {
		
	}


	public void render(SpriteBatch sb) {
		//font.draw(sb, "hier text", getScreenX(), getScreenY() + 32);
		
		pkmn.render(sb);
		
		for(Fireblast b : blasts){
			b.render(sb);
		}
		for(Bite b : bites){
			b.render(sb);
		}
		
	}
	
	public void render(ShapeRenderer sr) {
		sr.set(ShapeType.Line);
		sr.setColor(Color.BLUE);
		//sr.circle(getScreenX() + 8, getScreenY() + 8, 16, 16);
		Gdx.gl20.glLineWidth(10);
		//sr.curve(getScreenX() + 8, getScreenY() + 8, Gdx.input.getX(), Gdx.input.getY(), 10, 10, 10, 10, 1000);

		sr.setColor(Color.RED);
		sr.rect(move.xServer * 16, move.yServer * 16, 16, 16);
		for(Fireblast b : blasts){
			b.render(sr);
		}
		for(Bite b : bites){
			b.render(sr);
		}
		
	}

	public void update(float delta) {
//		//System.err.println("x: " + x)s
//		pkmn.update(delta);
//		List<Fireblast> dead = new ArrayList<Fireblast>();
//		List<Bite> dead1 = new ArrayList<Bite>();
//		for(Fireblast b : blasts){
//			b.update(delta);
//			if(!b.isAlive()){
//				dead.add(b);
//			}
//		}
//		for(Bite b : bites) {
//			b.update(delta);
//			if(!b.isAlive()){
//				dead1.add(b);
//			}
//		}
//		for(Fireblast b : dead){
//			blasts.remove(b);
//		}
//		dead.clear();
//		for(Bite b : dead1){
//			bites.remove(b);
//		}
//		dead1.clear();
//		//this.fireblast.setFrameDuration(elapsed);
//
//		if(Gdx.input.isKeyJustPressed(Input.Keys.X) && cd > 5){
//			bites.add(new Bite(this.x, this.y, AnimationDirection.DOWN));
//			cd = 0;
//		}
//		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && cd > 5){
//			blasts.add(new Fireblast(this, 0, 0, 9));
//			cd = 0;
//		}
//		cd++;
//		
//		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//			move.execute(new Position(Gdx.input.getX(), Gdx.input.getY()));
//		}
//		
//		
//		move.update(delta);
//		pkmn.setPosition(getScreenX()-7, getScreenY());
	}

	public class State {
		public int corrections = 0;
		public AnimationDirection dir;
	}

	@Override
	public void handle(MovementS p) {

		move.verify(p.approved);
	}
	
	@Override
	public void move() {
	
	}
	
	@Override
	public void stopMove() {

	}

	public String getName() {
		return name;
	}
}
