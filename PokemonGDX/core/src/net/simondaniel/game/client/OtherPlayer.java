package net.simondaniel.game.client;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.simondaniel.entities.Entity;
import net.simondaniel.entities.EntityInformation;
import net.simondaniel.game.client.Player.State;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.server.Response.MovementS;

public class OtherPlayer extends Entity {

	ShapeRenderer sr = new ShapeRenderer();
	
	GameClient client;
	
	int ix = 0, iy = 0;
	boolean interpolating = false;
	//Direction dir;
	
	BitmapFont font = new BitmapFont();
	
	private String name;
	
	public OtherPlayer(int x, int y, String name){
		//super(x,y);
		this.name = name;
		sr = new ShapeRenderer();
		client = PokemonGDX.game.client;
	}

	
	public void setCam(OrthographicCamera cam){
		sr.setProjectionMatrix(cam.combined);
	}

	@Override
	public void update(float delta) {
		
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
