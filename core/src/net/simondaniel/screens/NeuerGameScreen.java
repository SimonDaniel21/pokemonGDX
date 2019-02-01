package net.simondaniel.screens;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.simondaniel.fabio.input.MyInput;
import net.simondaniel.fabio.phisx.Geometry;
import net.simondaniel.fabio.phisx.LogicMap;
import net.simondaniel.fabio.phisx.PhysicsWorld;
import net.simondaniel.fabio.phisx.RealWorld;
import net.simondaniel.fabio.phisx.TiledMapLogicLoader;
import net.simondaniel.pokes.Pokemon;

public class NeuerGameScreen implements Screen {

	Player p;
	SpriteBatch sb;
	ShapeRenderer sr;
	TiledMap tmap;
	TiledMapRenderer tmRenderer;
	OrthographicCamera cam;
	MyInput input;
	
	//World world;
	
	
	PhysicsWorld world;

	@Override
	public void show() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
	
		cam = new OrthographicCamera(1280, 720);
		// anim.setScale(5);
		
		tmap = new TmxMapLoader().load("maps/arena.tmx");
		tmRenderer = new OrthogonalTiledMapRenderer(tmap, 2f);
		input = new MyInput(1280, 720, cam);
		
		Gdx.input.setInputProcessor(input);
		x = 0;
		y = 0;
		
		//world = new World(new Vector2(0, 0), true);
	
		world = new RealWorld();
		p = new Player(Pokemon.pikachu, world);
		// Create a circle shape and set its radius to 6

		LogicMap lm = TiledMapLogicLoader.loadCollisionDataFromXML("maps/arena.tmx");
		makeGeometry(lm);
	}

	float x = Gdx.graphics.getWidth()/2, y = Gdx.graphics.getHeight()/2, speed = 100;
	
	@Override
	public void render(float delta) {

		p.handleInput(input);

		p.update(delta);
		//world.step(1/60f, 8, 3);
		world.update(delta);
		cam.position.set(p.getX()*32, p.getY(), 0*32);
		cam.update();
		input.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmRenderer.setView(cam);
		tmRenderer.render();
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		p.draw(sb);
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
}
