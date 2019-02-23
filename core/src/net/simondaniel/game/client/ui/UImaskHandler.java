package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.simondaniel.game.client.ui.masks.GameMenu;
import net.simondaniel.game.client.ui.masks.LobbyMask;
import net.simondaniel.game.client.ui.masks.LoginMask;
import net.simondaniel.game.client.ui.masks.PokemonChooseMask;
import net.simondaniel.game.client.ui.masks.ServerSelection;

public class UImaskHandler extends Stage {
	

	public final ServerSelection server_select_mask;
	public final LoginMask login_mask;
	public final LobbyMask lobby_mask;
	public final GameMenu game_menu_mask;
	public final PokemonChooseMask pokemon_choose_mask;
	
	
	private Table backgroundtable;
	
	
	
	
	public UImaskHandler(TextureRegion tr, Skin skin) {
		super();
		server_select_mask = new ServerSelection(skin, this);
		login_mask = new LoginMask(skin, this);
		lobby_mask = new LobbyMask(skin, this);
		game_menu_mask = new GameMenu(skin, this);
		pokemon_choose_mask = new PokemonChooseMask(skin, this);
		
		server_select_mask.afterInit();
		login_mask.afterInit();
		lobby_mask.afterInit();
		game_menu_mask.afterInit();
		pokemon_choose_mask.afterInit();
		
		backgroundtable = new Table();
		backgroundtable.background(new Image(tr).getDrawable());
		backgroundtable.setFillParent(true);
		addActor(backgroundtable);
	}
	
	@Override
	public void clear() {
		super.clear();
		addActor(backgroundtable);
	}
}
