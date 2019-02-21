package net.simondaniel.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.simondaniel.fabio.input.InputQueue;
import net.simondaniel.fabio.input.InputSate.TransmittedInputState;
import net.simondaniel.fabio.phisx.Geometry;
import net.simondaniel.fabio.phisx.LogicMap;
import net.simondaniel.fabio.phisx.NetworkedWorld;
import net.simondaniel.fabio.phisx.TiledMapLogicLoader;
import net.simondaniel.network.server.PlayServer;
import net.simondaniel.network.server.PlayServerListener;
import com.esotericsoftware.kryonet.Listener.LagListener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

public class ServerScreen implements Screen {
	
	NetworkedWorld world;
	Box2DDebugRenderer debugRenderer;
	PlayServer server;
	PlayServerListener sl;
	
	InputQueue inputs;
	
	Body b, b2;
	@Override
	public void show() {
	
		inputs = new InputQueue();
		in = inputs.nextState();
		server = new PlayServer("Test Server");
		server.start();
	
		sl = new PlayServerListener(server);
		sl.setInputQueue(inputs);
		

		boolean lag = false;
		
		if(lag) {
			server.addListener(new LagListener(50, 200, sl));
		}
		else {
			server.addListener(new ThreadedListener(sl));
		}
		world = new NetworkedWorld(server, new World(new Vector2(0, 0), true));
		sl.setWorld(world);
		
		BodyDef def = new BodyDef();
		def.position.set(1f, 1f);
		def.type = BodyType.DynamicBody;
		//def.linearDamping = 1f;
		b = world.createTrackedBody(def, 3);
		
		
		def.position.x = 6f;
		def.position.y = 7f;
		b2 = world.createTrackedBody(def, 4);
		CircleShape cs = new CircleShape();
		cs.setRadius(0.9f);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		//fdef.isSensor = true;
		b.createFixture(fdef);
		fdef.friction = 0.1f;
		fdef.restitution = 0.8f;
		b2.createFixture(fdef);
		
		LogicMap lm = TiledMapLogicLoader.loadCollisionDataFromXML("maps/arena.tmx");
		makeGeometry(lm);
		lastPos.set(b.getPosition());
	}
	
	Vector2 lastPos = new Vector2();
	float timeout = 0.4f;
	boolean inTransition = false;
	
	
	float inputsPerSecond = 55;
	float secondsPerInput = 1/inputsPerSecond;
	float timer = 0;
	
	int i = 0;
	
	TransmittedInputState in;
	@Override
	public void render(float delta) {
		
		timer += delta;
		if(timer >= secondsPerInput) {
			in = inputs.nextState();
			timer -= secondsPerInput;
		}
		
		float xdir = 0, ydir = 0, speed = 4.8f;
		if(in.inputs[0]) {
			ydir += 1;
		}
		if(in.inputs[1]) {
			xdir -= 1;	
		}
		if(in.inputs[2]) {
			ydir -= 1;
		}
		if(in.inputs[3]) {
			xdir += 1;
		}
		b.setLinearVelocity(speed*xdir, speed*ydir);
		b2.setLinearVelocity(0, 0);
		
//		i = i % 400;
//
//		if(i < 200)
//			b.setLinearVelocity(0.1f, 0);
//		else
//			b.setLinearVelocity(0, 0);
//		i++;
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
