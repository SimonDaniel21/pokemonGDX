package net.simondaniel.game.client.ui.masks;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.simondaniel.util.MyRandom;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.pokes.Pokemon;

public class PokemonChooseMask extends UImask<PokemonChooseMaskInfo>{
	
	PokemonButton selected;
	PokemonButton[] buttons;
	private boolean isInAnimation = false;
	private final float MOVE_ANIMATION_TIME = 0.95f, TARGET_X = 39, TARGET_Y = 720-128-36, SMALL_SIZE = 95, BIG_SIZE = 128;
	
	Table infoTable;
	Label nameLBL, typeLBL, loreLBL;
	
	TextButton select;
	
	public PokemonChooseMask(Skin skin, UImaskHandler ui) {
		super(new PokemonChooseMaskInfo(), skin, ui);
		infoTable = new Table(skin);
		nameLBL = new Label("", skin);
		typeLBL = new Label("", skin);
		loreLBL = new Label("", skin);
		infoTable.add("name:");
		infoTable.add(nameLBL).row();
		infoTable.add("Typ:");
		infoTable.add(typeLBL).row();
		infoTable.add("pokedex:");
		infoTable.add(loreLBL).row();
		infoTable.setPosition(220, 620);
		infoTable.left();
		
		select = new TextButton("select", skin);
		select.setPosition(30, 440);
		select.left();
		select.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selected == null) {
					beep();
					return;
				}
				info.lobbyMask.setPokemon(selected.poke);
				info.lobbyMask.getInfo().joinLobby = false;
				switchTo(info.lobbyMask);
			}
		});
	}
	

	@Override
	public void enter() {
		Image image = new Image(new TextureRegion(new Texture("gfx/pokeFrame2.png")));
		buttons = new PokemonButton[6];
		
		//info.gc.addChanelListener(info.lobbyMask.listener);
		//info.lobbyMask.getInfo().userTracker.addListener(info.lobbyMask.userTrackerListener);
		
		List<Pokemon> taken = new ArrayList<Pokemon>();
		for(int i = 0; i < buttons.length; i++) {
			
			Pokemon random;
			do {
				int ri = MyRandom.random.nextInt(Pokemon.pokemon.length);
				if(i == 0) ri =5;
				random = Pokemon.pokemon[ri];
			}
			while(taken.contains(random));
			
			taken.add(random);
			
			buttons[i] = new PokemonButton(random);
		}
		
		//39 : 36
		int startX = 500, x = 0;
		int y = 300;
		for(int i = 0; i <  buttons.length; i++) {
			if(i%3 == 0) {
				x = startX;
				if(i != 0)
					y -= SMALL_SIZE;
			}
			else
				x += SMALL_SIZE;
			
			buttons[i].addTo(this, x, y);
		}
		image.setPosition(0, 720 - image.getHeight());
		image.setTouchable(Touchable.disabled);
		getStage().addActor(image);	
		getStage().addActor(infoTable);
		getStage().addActor(select);
	}

	@Override
	public void leave() {
		//info.gc.removeChanelListener(info.lobbyMask.listener);
		//info.lobbyMask.getInfo().userTracker.removeListener(info.lobbyMask.userTrackerListener);
	}

	private class PokemonButton extends Button{
		float x, y;
		
		String pokeName;
		String type;
		String lore;
		
		Pokemon poke;
		
		public PokemonButton(Pokemon p) {
			super(new TextureRegionDrawable(new TextureRegion(new Texture(p.getPreviewPicturePath()))));
			this.poke = p;
			//getImage().setFillParent(true);
			pokeName = p.name;
			type = p.type1.name();
			if(p.type2 != null) 
				type += "/" + p.type2.name();
			
			lore = p.ID + "";
			final PokemonButton ref = this;
			addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(isInAnimation) beep();
				
					nameLBL.setText("");
					typeLBL.setText("");
					loreLBL.setText("");
					
					if(selected == ref) {
						moveBack();
						selected = null;
					}
					else {
						moveSelection();
						if(selected != null)
						selected.moveBack();
						selected = ref;
					}
					
				}
			});
		}
		
		
		public void addTo(Table t, float x, float y) {
			t.getStage().addActor(this);
			this.x = x;
			this.y = y;
			setSize(SMALL_SIZE, SMALL_SIZE);
			
			setPosition(x, y);
		}
		
		private void moveSelection() {
			isInAnimation = true;
			final PokemonButton ref = this;
			addAction(Actions.sequence(
					Actions.parallel(
							Actions.sizeTo(BIG_SIZE, BIG_SIZE, MOVE_ANIMATION_TIME),
							Actions.moveTo(TARGET_X, TARGET_Y, MOVE_ANIMATION_TIME)),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							if(selected == ref) {
								nameLBL.setText(pokeName);
								typeLBL.setText(type);
								loreLBL.setText(lore);
							}
						}
					})));
		}
		
		private void moveBack() {
			isInAnimation = true;
			addAction(Actions.sequence(
					Actions.parallel(
							Actions.sizeTo(SMALL_SIZE, SMALL_SIZE, MOVE_ANIMATION_TIME),
							Actions.moveTo(x, y, MOVE_ANIMATION_TIME)),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							isInAnimation = false;
						}
					})));
		}
	}
	public Pokemon getSelectedPokemon() {
		return selected.poke;
	}


	@Override
	public void afterInit() {
		
	}
}
