package net.simondaniel.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.simondaniel.Assets;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.pokes.Pokemon;
import net.simondaniel.screens.LoginScreen;
import net.simondaniel.screens.ServerScreen;

public class PokemonGDX extends Game {

	public static String VERSION;
	
	public static LaunchConfiguration CONFIGURATION;

	public static void init() {
		FileHandle fh = Gdx.files.internal("version");
		VERSION = fh.readString();

		VERSION = "DEV";
	}

	SpriteBatch batch;
	public PlayClient client;
	public static PokemonGDX game;

	public PokemonGDX(LaunchConfiguration config) {
		super();
		game = this;
		CONFIGURATION = config;
		// this.client = client;
	}

	int i = 0;

	int mb = 1024*1024;
	@Override
	public void render() {
		super.render();
//		if (i++ == 200) {
//			int p = (client == null) ? 0 : client.packetCount();
//			float rate = (float) (p==0 ? 0 : Math.round(client.packetRate() * 100.0) / 100.0);
//			Gdx.graphics.setTitle(Gdx.app.getJavaHeap()/mb + " mb used, " + p + " packets received (" + rate + "p/s)");
//			
//			i = 0;
//		}
	}

	@Override
	public void create() {
		
//		GameClient gc = new GameClient("127.0.0.1", "serva");
//		if(gc.sendConnectRequest()) {
//			gc.sendLoginRequest("mario", "development");
//			if(!gc.waitForVerification(2000)) {
//				System.err.println("no login");
//			}
//			gc.sendLobbyCreateRequest("testlobby", 0);
//		}
//		else {
//			System.err.println("no connect");
//		}
	
		switch (CONFIGURATION) {
		case DEBUG_CLIENT:
			Pokemon.loadFromFile();
			Assets.load();
			batch = new SpriteBatch();
			setScreen(new LoginScreen());
			break;
		case DEBUG_SERVER:
			setScreen(new ServerScreen());
		default:
			break;
		}
		
		
	}

}
