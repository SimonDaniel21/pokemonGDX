package net.simondaniel.game.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.simondaniel.network.server.GameServer;

public class PokemonGDXS{
	
	public static String VERSION;
	

	public void init(){
		game = this;
		FileHandle fh = Gdx.files.internal("version");
		VERSION = fh.readString();
		VERSION = "DEV";
	}

	public static PokemonGDXS game;
	
	public PokemonGDXS(GameServer server) {

	}
	
	public void start() {
		
	}

	protected void update(float delta) {
	}
}
