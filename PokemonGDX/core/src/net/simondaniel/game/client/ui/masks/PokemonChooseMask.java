package net.simondaniel.game.client.ui.masks;

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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.pokes.Pokemon;

public class PokemonChooseMask extends UImask<PokemonChooseMaskInfo>{
	
	PokemonButton selected;
	PokemonButton[] buttons;
	private boolean isInAnimation = false;
	private final float MOVE_ANIMATION_TIME = 0.95f, TARGET_X = 39, TARGET_Y = 720-128-36, SMALL_SIZE = 95, BIG_SIZE = 128;
	
	Table infoTable;
	Label nameLBL, typeLBL, loreLBL;
	
	public PokemonChooseMask(PokemonChooseMaskInfo info, Skin skin) {
		super(info, skin);
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
		
	}
	

	@Override
	public void enter() {
		Pokemon.loadFromFile();
		Image image = new Image(new TextureRegion(new Texture("gfx/pokeFrame2.png")));
		buttons = new PokemonButton[6];
		buttons[0] = new PokemonButton("gfx/pikachu_preview128.png", Pokemon.pikachu);
		buttons[1] = new PokemonButton("gfx/rayquaza_preview128.png", Pokemon.rayquaza);
		buttons[2] = new PokemonButton("gfx/kyogre_preview128.png", Pokemon.kyogre);
		buttons[3] = new PokemonButton("gfx/squirtle_preview128.png", Pokemon.squirtle);
		buttons[4] = new PokemonButton("gfx/charmander_preview128.png", Pokemon.charmander);
		buttons[5] = new PokemonButton("gfx/bulbasaur_preview128.png", Pokemon.bulbasaur);
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
	}

	@Override
	public void leave() {
	}

	private class PokemonButton extends Button{
		float x, y;
		
		String pokeName;
		String type;
		String lore;
		
		public PokemonButton(String path, Pokemon p) {
			super(new TextureRegionDrawable(new TextureRegion(new Texture(path))));
			//getImage().setFillParent(true);
			pokeName = p.name;
			type = p.type1.name();
			if(p.type2 != null) 
				type += "/" + p.type2.name();
			
			lore = p.ID + "";
			debug();
			final PokemonButton ref = this;
			addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(isInAnimation) beep();
				
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
}
