package net.simondaniel.game.client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import net.simondaniel.Assets;
import net.simondaniel.entities.Entity;
import net.simondaniel.entities.OnlinePlayer;
import net.simondaniel.game.client.gfx.AnimationLayout;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.server.GameServer;
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
			TextureAtlas atlas = Assets.manager.get("squirtle.atlas");
			PokemonAnimation anim = new PokemonAnimation(AnimationLayout.PokemonAnimationLayout.squirtle.LAYOUT, atlas);
			DrawComponent dc = new DrawComponent();
			dc.drawable = anim;
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
		test.stats = new PokemonStats();
		test.level = 100;
		
		Entity e = new OnlinePlayer(lobby.getUser(0).user.name, world, test);
		addEntity(e);
		//System.out.println("SERVER SENT ENTITY " + users[0].user.name);
	}

}
