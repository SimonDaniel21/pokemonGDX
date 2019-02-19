package net.simondaniel.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.simondaniel.fabio.input.RemoteInput;
import net.simondaniel.fabio.phisx.LogicMap;
import net.simondaniel.fabio.phisx.PredictedBody;
import net.simondaniel.fabio.phisx.PredictedWorld;
import net.simondaniel.fabio.phisx.SyncBodyInfo;
import net.simondaniel.fabio.phisx.TiledMapLogicLoader;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.network.client.AuthenticationService;
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.network.server.Response.WorldStateS;
import net.simondaniel.pokes.Pokemon;

public class NeuerGameScreen implements Screen {

	//Player p;
	SpriteBatch sb;
	ShapeRenderer sr;
	TiledMap tmap;
	TiledMapRenderer tmRenderer;
	OrthographicCamera cam;
	RemoteInput input;
	
	//World world;
	
	
	PredictedWorld world;
	
	@Override
	public void show() {
		
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
	
		cam = new OrthographicCamera(1280, 720);
		// anim.setScale(5);
		
		tmap = new TmxMapLoader().load("maps/arena.tmx");
		tmRenderer = new OrthogonalTiledMapRenderer(tmap, 2f);
		PlayClient gc = new PlayClient();
		
		while(!gc.isConnected())Thread.yield();
		gc.authService.login("Simon", "1234");
		
		input = new RemoteInput(cam, gc, 5);
		
		Gdx.input.setInputProcessor(input);
		x = 0;
		y = 0;
		
		//world = new World(new Vector2(0, 0), true);
	
		world = new PredictedWorld();
		gc.setWorld(this);
		
		animation = new PokemonAnimation(Pokemon.pikachu);
		animation.setScale(2.0f);
		animation.runAnimation(AnimationType.MOVEMENT, AnimationDirection.RIGHT);
		anim2 = new PokemonAnimation(Pokemon.rayquaza);
		anim2.setScale(2.0f);
		anim2.runAnimation(AnimationType.MOVEMENT, AnimationDirection.LEFT);
	}

	float x = Gdx.graphics.getWidth()/2, y = Gdx.graphics.getHeight()/2, speed = 100;
	
	float inputsPerSecond = 55;
	float secondsPerInput = 1/inputsPerSecond;
	float timer = 0;
	
	public void feedInput(List<SyncBodyInfo> updates) {
		world.syncWith(updates);
	}
	
	PredictedBody player;
	
	private PokemonAnimation animation, anim2;
	
	@Override
	public void render(float delta) {

		//p.handleInput(input);10
		
		timer += delta;
		if(timer >= secondsPerInput) {
			input.update();
			timer -= secondsPerInput;
		}
		
	//	p.update(delta);
		//world.step(1/60f, 8, 3);
		world.update(delta);
		
		animation.update(delta);
		anim2.update(delta);
		
		if(player == null) {
			player = world.getBody(3);
			anim2.attachTo(world.getBody(4));
			if(player != null) {
				animation.attachTo(player);
			}
		}
		else {
			cam.position.set(player.x()*32, player.y()*32, 0);
		}
		
		//cam.position.set(p.getX()*32, p.getY(), 0*32);
		cam.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmRenderer.setView(cam);
		tmRenderer.render();
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
	//	p.draw(sb);
		animation.draw(sb);
		anim2.draw(sb);
		sb.end();
		sr.setColor(Color.RED);
		sr.begin();
		sr.line(0, 0, 1279, 719);
		sr.line(0, 719, 1279, 0);
		sr.end();
		
		world.renderDebug(cam.combined.scl(32));
	}

	@Override
	public void resize(int width, int height) {
		input.resize(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	public void makeGeometry(LogicMap map) {
		int w = map.getWidth();
	
		BodyDef def = new BodyDef();
		def.position.set(0, 0);
		def.type = BodyType.StaticBody;
		//Body geo = world.createBody(def);

		//Geometry.addGeometryFixtures(geo, map.getData(), w);
	}

	public void setWorldState(WorldStateS p) {
		world.setTo(p);
	}
}
