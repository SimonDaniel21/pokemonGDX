package net.simondaniel;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.client.Request.MovementChandler;
import net.simondaniel.network.server.Response.MovementS;
import net.simondaniel.network.server.Response.MovementShandler;

public class myMapRenderer extends OrthogonalTiledMapRenderer{

	Sprite player;
	int x, y = 0;
	TiledMapTileLayer back;
	TiledMapTileLayer fore;
	//SpriteBatch sb;
	private ShapeRenderer shapeRenderer;
	OrthographicCamera cam;
	GameClient client;
	Queue<State> queue;
	
	public myMapRenderer(TiledMap map, OrthographicCamera cam) {
		super(map,2);
		queue = new LinkedList<State>();
		this.cam = cam;
		client = PokemonGDX.game.client;
		back = (TiledMapTileLayer) map.getLayers().get("background");
		fore = (TiledMapTileLayer) map.getLayers().get("foreground");
		Texture tex = new Texture(Gdx.files.internal("gfx/trainer.png"));
		player = new Sprite(tex);
		//sb = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}
	int cd = 0;
	int corrections = 0;
	int id = 0;
	@Override
	public void render(){
		cd++;
		if(cd > 10){
			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				MovementC m = new MovementC();
				//m.dir = Direction.UP;
				m.corrections = corrections;
				m.id = id;
				id++;
				client.send(m);
				cd = 0;
				State s = new State();
				s.corrections = corrections;
				//s.dir = m.dir;
				queue.add(s);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				MovementC m = new MovementC();
				//m.dir = Direction.LEFT;
				m.corrections = corrections;
				m.id = id;
				id++;
				
				client.send(m);
				cd = 0;
				State s = new State();
				s.corrections = corrections;
				//s.dir = m.dir;
				queue.add(s);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				MovementC m = new MovementC();
				//m.dir = Direction.DOWN;
				m.corrections = corrections;
				m.id = id;
				id++;
				client.send(m);
				cd = 0;
				State s = new State();
				s.corrections = corrections;
			//	s.dir = m.dir;
				queue.add(s);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				MovementC m = new MovementC();
				//m.dir = Direction.RIGHT;
				m.corrections = corrections;
				m.id = id;
				id++;
				client.send(m);
				cd = 0;
				State s = new State();
				s.corrections = corrections;
				//s.dir = m.dir;
				queue.add(s);
			}
		}
		
		this.batch.begin();
		//this.renderTileLayer(back);
		//sb.begin();
		player.setPosition(x-5, y);
		player.draw(this.batch);

		//sb.end();
		//this.renderTileLayer(fore);
		batch.end();
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
	    shapeRenderer.setColor(Color.BLUE);
	    shapeRenderer.rect(x, y, 32, 32);
	    shapeRenderer.end();
	}


	public float getX() {	
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public class State{
		public int corrections = 0;
	}
}
