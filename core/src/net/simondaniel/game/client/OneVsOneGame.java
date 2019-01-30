package net.simondaniel.game.client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import net.simondaniel.Assets;
import net.simondaniel.MyRandom;
import net.simondaniel.entities.Entity;
import net.simondaniel.entities.OnlinePlayer;
import net.simondaniel.game.client.gfx.AnimationLayout;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.pokes.PokeDef;
import net.simondaniel.pokes.Pokemon;
import net.simondaniel.pokes.PokemonStats;

public class OneVsOneGame extends GameInstance{

	public OneVsOneGame(GameClient net, SpriteBatch sb) {
		super(net, sb);
	}
	
	public OneVsOneGame(GameServer gs) {
		super(gs);
	}

	@Override
	public void initBoth() {
		super.initBoth();
		
		if(!isServer) {
			
		}
	
	}
	
	@Override
	public void initClient() {
		super.initClient();
		
	}
	
	@Override
	public void initServer() {
		super.initServer();
		
		PokeDef test = new PokeDef();
		test.pokemon = Pokemon.squirtle;
		test.exp = 1;
		test.level = 100;
		test.stats = new PokemonStats();
		
		Entity e;
		for(UserConnection c : lobby.getUsers()) {
			int r = MyRandom.random.nextInt(12)+1;
			e = new OnlinePlayer(c.name, world, test);
			e.setPosition(r, 1);
			addEntity(e);
		}
	}

}
