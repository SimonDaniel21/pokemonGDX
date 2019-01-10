package net.simondaniel.game.client.battle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.simondaniel.Assets;
import net.simondaniel.Assets.Atlas;
import net.simondaniel.entities.Entity;
import net.simondaniel.fabio.input.BattleInput;
import net.simondaniel.fabio.input.InputHandler;
import net.simondaniel.fabio.input.BattleInput.BattleInputType;
import net.simondaniel.MyInterpolation;
import net.simondaniel.game.client.MoveAction;
import net.simondaniel.game.client.attacks.Bite;
import net.simondaniel.game.client.attacks.Fireblast;
import net.simondaniel.game.client.attacks.HydroPump;
import net.simondaniel.game.client.gfx.AnimatedSprite;
import net.simondaniel.game.client.gfx.AnimationLayout;
import net.simondaniel.game.client.gfx.AnimationLayout.PokemonAnimationLayout;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.game.client.ui.SkillBar;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.server.Response.MovementS;
import net.simondaniel.network.server.Response.MovementShandler;
import net.simondaniel.screens.MainMenuScreen;

public class Schiggy extends MovingEntity implements MovementShandler, InputHandler<BattleInput> {

	GameClient client;

	BitmapFont font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));

	private String name;

	MoveToAction move;
	int cd = 0;
	
	PokemonAnimation pkmn;
	
	List<Fireblast> blasts;
	List<HydroPump> pumps;
	List<Bite> bites;
	
	SkillBar bar;

	private float elapsedTime = 0, totalTime;
	private Position source, dest;
	private boolean moving = false;
	private float speed = 100;
	
	public Schiggy(int x, int y, String name) {
		this.name = name;
		move = new MoveToAction(this, 100.0f);
		//pkmn = new PokemonAnimation(PokemonAnimationLayout.squirtle.LAYOUT, Assets.manager.get(Atlas.SQUIRTLE.assetDescriptor));
		blasts = new ArrayList<Fireblast>();
		bites = new ArrayList<Bite>();
		pumps = new ArrayList<HydroPump>();
		source = new Position(x, y);
		dest = new Position(x, y);
		bar = new SkillBar();
	}
	
	@Override
	public void handle(BattleInput input) {
		if(input.isJustPressed(BattleInputType.MOVE_TO_CURSOR)){
			//this.x = input.getWorldX();
			//this.y = input.getWorldY();
			System.err.println("schiggy moved to: " + input.getWorldX() + " - " + input.getWorldY() + " - " );
			dest.set(input.getWorldX(), input.getWorldY());
			//source.set(x, y);
			float dist =  source.distance(dest);
			totalTime = dist * (1 / speed);
			moving = true;
			elapsedTime = 0;
			pkmn.runAnimation(AnimationType.MOVEMENT, AnimationDirection.fromAngle(source.angleTo(dest)));
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && cd > 5){
			//pumps.add(new HydroPump(this, 0, input.getWorldX(), input.getWorldY()));
			cd = 0;
			System.out.println("hydro!");
		}
		
	}
	
	public void update(float delta) {
//		pkmn.update(delta);
//		List<Fireblast> dead = new ArrayList<Fireblast>();
//		List<Bite> dead1 = new ArrayList<Bite>();
//		List<HydroPump> dead2 = new ArrayList<HydroPump>();
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
//		for(HydroPump b : pumps) {
//			b.update(delta);
//			if(!b.isAlive()){
//				dead2.add(b);
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
//		for(HydroPump b : dead2){
//			pumps.remove(b);
//		}
//		dead1.clear();
//		//this.fireblast.setFrameDuration(elapsed);
//
//		if(Gdx.input.isKeyJustPressed(Input.Keys.X) && cd > 5){
//			bites.add(new Bite(this.x, this.y, AnimationDirection.DOWN));
//			cd = 0;
//		}
//		
//		cd++;
//		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//			move.execute(new Position(Gdx.input.getX(), Gdx.input.getY()));
//		}
//		
//		//move.update(delta);
//		if(moving) {
//			x = MyInterpolation.linear(elapsedTime / totalTime, source.x, dest.x);
//			y = MyInterpolation.linear(elapsedTime / totalTime, source.y, dest.y);
//			//System.err.println("step: " + e.x + " | " + e.y + " @" + (elapsedTime / totalTime) + "  -  " + source.x + " | " + dest.x);
//			elapsedTime += delta;
//			if(elapsedTime >= totalTime) {
//				moving = false;
//				pkmn.haltAnimation(1);
//			}
//		}
//		pkmn.setPosition(getScreenX(), getScreenY());
	}

	public void render(SpriteBatch sb) {
		//font.draw(sb, "hier text", getScreenX(), getScreenY() + 32);
		
		//pkmn.render(sb);
		
		for(Fireblast b : blasts){
			b.render(sb);
		}
		for(Bite b : bites){
			b.render(sb);
		}
		for(HydroPump b : pumps){
			b.render(sb);
		}
		bar.render(sb);
		//sb.draw(r, getX(), getY());
		
	}
	
	public void render(ShapeRenderer sr) {
		sr.set(ShapeType.Line);
		sr.setColor(Color.BLUE);
		//sr.circle(getScreenX(), getScreenY(), 16, 16);
		//sr.rect(getScreenX(), getScreenY(), pkmn.getAnimationWidth(), pkmn.getAnimationHeight());
		
		//Gdx.gl20.glLineWidth(10);
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

	

	public class State {
		public int corrections = 0;
	}

	@Override
	public void handle(MovementS p) {

		move.verify(p.approved);
	}
	
	@Override
	public void move() {
	
	}
	
	@Override
	public void stopMove(){
		//pkmn.haltAnimation(1);
	}

	public String getName() {
		return name;
	}

}