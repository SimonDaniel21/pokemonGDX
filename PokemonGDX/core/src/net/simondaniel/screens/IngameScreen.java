package net.simondaniel.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.myMapRenderer;
import net.simondaniel.control.Controller;
import net.simondaniel.control.PlayerController;
import net.simondaniel.control.PokemonControl;
import net.simondaniel.control.PokemonController;
import net.simondaniel.entities.Entity;
import net.simondaniel.fabio.input.FabioInput;
import net.simondaniel.game.client.GameInstance;
import net.simondaniel.game.client.OneVsOneGame;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.masks.IngameMenu;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.MyListener;
import net.simondaniel.network.client.Request.MoveToC;
import net.simondaniel.network.client.Request.RequestAreaC;
import net.simondaniel.network.server.Response.MoveToS;

public class IngameScreen extends GameScreen{

	public IngameScreen(GameClient client) {
		super(client, null, 1, 1);
	}

	
	SpriteBatch batch;
	ShapeRenderer sr;
	Texture img;
	myMapRenderer renderer;
	OrthographicCamera cam, staticCam;
	Texture t;
	BitmapFont font = new BitmapFont();
	ParticleEffect pe;

	GameInstance game;
	
	@Override
	public void show() {
		staticCam = new OrthographicCamera(1280, 720);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		game = new OneVsOneGame(client, batch);
		game.start();
		//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		pe = new ParticleEffect();
		pe.load(Gdx.files.internal("gfx/atlases/rain/rain.p"), Gdx.files.internal("gfx/atlases/rain/"));
		pe.getEmitters().first().setPosition(0, 0);
		pe.scaleEffect(0.1f);
		pe.start();
	}
	
	@Override
	public void handleInput(InputProcessor input) {
		
	}
	
	@Override
	public void update(float delta) {
		//map.update(delta);
		
	}
	
	@Override
	public void render() {
		//Gdx.graphics.setTitle("Pokemon GDX @" + Gdx.graphics.getFramesPerSecond());
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		client.handlePacketBuffer();
		game.update(Gdx.graphics.getDeltaTime());
		pe.update(Gdx.graphics.getDeltaTime());
		if(pe.isComplete()) {
			pe.reset();
			pe.scaleEffect(0.1f);
		}
		batch.setProjectionMatrix(staticCam.combined);
		batch.begin();
		pe.draw(batch);
		batch.end();
		if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
			Gdx.app.exit();

		}
		sr.setColor(Color.WHITE);
		sr.begin();
		sr.line(new Vector2(0,0), new Vector2(1280 -1, 720 -1));
		sr.line(new Vector2(0,720-1), new Vector2(1280 -1, 0));
		sr.end();
	}

	@Override
	public void resize(int width, int height) {
		game.resizeScreen(width, height);
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
		batch.dispose();
		img.dispose();
		renderer.dispose();
		game.dispose();
		
	}

}
