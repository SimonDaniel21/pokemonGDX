package net.simondaniel.screens.tempNet;

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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.simondaniel.fabio.input.MyInput;
import net.simondaniel.fabio.phisx.Geometry;
import net.simondaniel.fabio.phisx.LogicMap;
import net.simondaniel.fabio.phisx.TiledMapLogicLoader;
import net.simondaniel.pokes.Pokemon;

public class ServerScreen implements Screen {
	
	NetworkedWorld world;
	Box2DDebugRenderer debugRenderer;

	Body b;
	@Override
	public void show() {
	
		world = new NetworkedWorld(new World(new Vector2(0, 0), true));
		
		BodyDef def = new BodyDef();
		def.position.set(1f, 1f);
		def.type = BodyType.DynamicBody;
		b = world.createTrackedBody(def, 3);
		CircleShape cs = new CircleShape();
		cs.setRadius(0.8f);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		fdef.isSensor = true;
		b.createFixture(fdef);
		
		LogicMap lm = TiledMapLogicLoader.loadCollisionDataFromXML("maps/arena.tmx");
		makeGeometry(lm);
		lastPos.set(b.getPosition());
	}
	
	Vector2 lastPos = new Vector2();
	float timeout = 0.4f;
	float timer = 0;
	boolean inTransition = false;
	
	
	int i = 0;
	@Override
	public void render(float delta) {
		
		i = i % 400;

		if(i < 200)
			b.setLinearVelocity(0.1f, 0);
		else
			b.setLinearVelocity(0, 0);
		i++;
		world.step();
		
	}


	@Override
	public void resize(int width, int height) {
		
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
		Body geo = world.createBody(def);

		Geometry.addGeometryFixtures(geo, map.getData(), w);
	}
}
