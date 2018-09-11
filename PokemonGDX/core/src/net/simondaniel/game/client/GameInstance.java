package net.simondaniel.game.client;

import java.sql.Timestamp;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.Assets;
import net.simondaniel.entities.Entity;
import net.simondaniel.entities.EntityInformation;
import net.simondaniel.entities.OnlinePlayer;
import net.simondaniel.entities.TestDrawer;
import net.simondaniel.entities.EntityInformation.OnlinePlayerInfo;
import net.simondaniel.fabio.input.FabioInput;
import net.simondaniel.fabio.phisx.Geometry;
import net.simondaniel.fabio.phisx.LogicMap;
import net.simondaniel.fabio.phisx.TiledMapLogicLoader;
import net.simondaniel.game.client.gfx.PKMNanimation;
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.game.client.ui.masks.IngameMenu;
import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.MyListener;
import net.simondaniel.network.client.Request.MoveToC;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.MyServerlistener;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.network.server.Response.AddEntityS;
import net.simondaniel.network.server.Response.MoveToS;
import net.simondaniel.pokes.Pokemon;

public class GameInstance implements MyListener, MyServerlistener{

	Lobby lobby; // server field
	
	OnlinePlayer player; // client field;
	
	public final static float PIXELS_PER_METER = 32;

	final boolean isServer;

	private int nextFreeID = 0;
	
	protected HashMap<Integer, Entity> entities;

	GameClient client;
	GameServer server;

	SpriteBatch sb;
	OrthographicCamera cam;
	Viewport view;

	FabioInput input;

	Stage stage;// only for UI

	UImaskHandler handler;
	IngameMenu ui;

	World world;
	Box2DDebugRenderer debugRenderer;
	TiledMapRenderer renderer;

	private Matrix4 cameraBox2D;
	
	PKMNanimation anim;

	ShapeRenderer sr;
	
	public GameInstance(GameClient client, SpriteBatch sb) {
		entities = new HashMap<Integer, Entity>();
		isServer = false;
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		this.client = client;
		client.addMyListener(this);
		this.sb = sb;
		cam = new OrthographicCamera();
		view = new FitViewport(1280, 720, cam);
		view.setScreenSize(1280, 720);
		stage = new Stage(view);
		handler = new UImaskHandler(null);
		Skin skin = new Skin(Gdx.files.internal("skins/sgx/sgx-ui.json"));
		ui = new IngameMenu(skin);
		stage.addActor(ui);
		ui.debug();

		// view.apply(true);
		// view.apply();
		input = new FabioInput(view);

		InputMultiplexer inmul = new InputMultiplexer();
		inmul.addProcessor(stage);
		inmul.addProcessor(input);
		Gdx.input.setInputProcessor(inmul);

	}

	public GameInstance(GameServer gs) {
		entities = new HashMap<Integer, Entity>();
		isServer = true;
		this.server = gs;
	}

	private final void init() {

		initBoth();

		if (isServer) {
			initServer();
		} else {
			initClient();
		}
	}

	protected void initBoth() {

		world = new World(new Vector2(0, 0), true);
		// Create a circle shape and set its radius to 6

		LogicMap lm = TiledMapLogicLoader.loadCollisionDataFromXML("maps/arena.tmx");
		makeGeometry(lm);
		if(!isServer) {
//			anim = new PKMNanimation(Assets.getPokeAtlas(Pokemon.rayquaza));
//			anim.scale(1.5f);
//			anim.runAnimation(4);
			TiledMap map = new TmxMapLoader().load("maps/arena.tmx");
			renderer = new OrthogonalTiledMapRenderer(map, 2);
			debugRenderer = new Box2DDebugRenderer();
			ui.show(stage);
//			OnlinePlayerInfo info = new OnlinePlayerInfo();
//			info.id = 20;
//			info.name = "simon";
//			info.radius = 0.5f;
//			info.x = 8;
//			info.y = 8;
//			player = new OnlinePlayer(world, info);
		}
	}

	protected void initClient() {

	}

	protected void initServer() {
		
	}

	public void resizeScreen(int width, int height) {
		view.update(width, height);
		stage.getViewport().update(width, height);
	}


	public void dispose() {
		if (isServer) {

		} else {
			client.removeMyListener(this);
		}

	}
	
	boolean last = false;

	public void update(float deltaTime) {

		Vector2 dir = new Vector2();
		
		if(!isServer && last == false && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			MoveToC p = new MoveToC();
			p.id = player.id;
			System.out.println("PLAYER ID IS " + p.id);
			
			p.x = input.getWorldX()/ 32;
			p.y = input.getWorldY() / 32;
			client.send(p);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			//System.out.println("sent @" + timestamp);
		}
		if(!isServer) {
			last = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		}

		
		
		for(Entity e : entities.values()) {
			//System.out.println("updating " + isServer);
			e.update(deltaTime);
			
		}

		doPhysicsStep(deltaTime);
		

		if (!isServer && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			if (ui.isVisible()) {
				ui.hide();
			} else {
				ui.show(stage);
			}
		}

		if (!isServer)
			stage.act(deltaTime);
		// engine.update(deltaTime);
		if (!isServer && player != null) {
		
			if(player != null) {
				cam.position.x = player.body().getPosition().x * PIXELS_PER_METER;
				cam.position.y = player.body().getPosition().y * PIXELS_PER_METER;
			}
			
			input.update();
			cameraBox2D = cam.combined.cpy(); // Copy the main camera
			cameraBox2D.scl(PIXELS_PER_METER); // Scale the camera projection to the ratio you're using for Box2D
			renderer.setView(cam);
			renderer.render(new int[] { 0, 1 });
			sb.setProjectionMatrix(cam.combined);
			sr.setProjectionMatrix(cam.combined);
			sb.begin();
			//anim.draw(sb);
			sb.end();
			sr.begin();
			//anim.drawOutline(sr);
			sr.end();
			debugRenderer.render(world, cameraBox2D);
			stage.draw();
			sb.begin();
			
			for(Entity e : entities.values()) {
				e.draw(sb);
				//System.out.println("drawn");
			}
			sb.end();
		}
	}

	private float accumulator = 0;
	private final float TIME_STEP = 1 / 60f;

	private void doPhysicsStep(float deltaTime) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		world.step(TIME_STEP, 6, 2);

		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIME_STEP) {
			// world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
	}

	public void makeGeometry(LogicMap map) {
		int w = map.getWidth();
	
		BodyDef def = new BodyDef();
		def.position.set(0, 0);
		def.type = BodyType.StaticBody;
		Body geo = world.createBody(def);

		Geometry.addGeometryFixtures(geo, map.getData(), w);
	}
	
	/**
	 * server ONLY function
	 */
	public void addEntity(Entity e) {
		if(!isServer)
			return;
		e.activate(nextFreeID);
		nextFreeID++;
		entities.put(e.id, e);
		EntityInformation info = e.getInfo();
		AddEntityS p = new AddEntityS();
		p.info = info;
		System.out.println("SERVER " + ((OnlinePlayerInfo)info).x);
		server.sendToAllTCP(p);
	}
	

	/**
	 * Client ONLY function
	 */
	public void addEntity(EntityInformation info) {
		if(isServer)
			return;
		
		Entity e = Entity.createFromInfo(world, info);
	
		e.activate(info.id);
		entities.put(e.id, e);
		if(e instanceof OnlinePlayer) {
			OnlinePlayer p = (OnlinePlayer) e;
			p.setDrawer(new TestDrawer(p));
			if(p.getName().equals(client.userName())){
				player = p;
			}
		}
	}

	public void start(Lobby lobby) {
		this.lobby = lobby;
		init();
	}

	public void start() {
		init();
	}

	@Override
	public void received(Connection c, Object o) {
		if (o instanceof MoveToS) {
			MoveToS p = (MoveToS) o;
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			//player.moveTo(p.x, p.y);
			entities.get(p.id).moveTo(p.x, p.y);
			//System.out.println("processed moved message on client @" + timestamp);
		}
		if(o instanceof AddEntityS) {
			AddEntityS p = (AddEntityS) o;
			addEntity(p.info);
			System.out.println("received add " + ((OnlinePlayerInfo)p.info).name + p.info.id);
		}
	}
	
	@Override
	public void receivedFromClient(UserConnection con, Object o) {
		if(o instanceof MoveToC) {
			MoveToC p = (MoveToC) o;
			
			MoveToS s = new MoveToS();
			s.id = p.id;
			s.x = p.x;
			s.y = p.y;
			lobby.sendToAllTCP(s);
			server.sendToAllTCP(s);
		}
//		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//		dir.add(0, 1);
//	}
//	if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//		dir.add(0, -1);
//	}
//	if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//		dir.add(-1, 0);
//	}
//	if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//		dir.add(1, 0);
//	}
//	if (!dir.isZero()) {
//		//b.setLinearVelocity(dir.nor().scl(5));
//		if(player != null) {
//			
//			player.body().setLinearVelocity(dir.nor().scl(5));
//		}
//	}
		//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//System.out.println("processed message on server @" + timestamp);
	}
	
	public Object[] initWorldPackets() {
		Object[] packets = new Object[entities.size()];
		for(int i : entities.keySet()) {
			AddEntityS p = new AddEntityS();
			p.info = entities.get(i).getInfo();
			packets[i] = p;
		}
		return packets;
	}
}
