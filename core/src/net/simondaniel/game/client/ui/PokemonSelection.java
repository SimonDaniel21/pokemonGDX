package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;

import net.simondaniel.MyRandom;
import net.simondaniel.fabio.input.BattleInput;
import net.simondaniel.fabio.input.InputHandler;
import net.simondaniel.screens.MainMenuScreen;

public class PokemonSelection implements InputHandler<BattleInput>{

	int x, y;
	
	private static final int ICON_SIZE = 128, W = 3, H = 1;
	
	private TextureRegion r, r2, r3;
	private Sprite s[];
	
	private int selected = -1;
	

	BitmapFont font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));
	
	public PokemonSelection() {

		r = new TextureRegion(new Texture("gfx/squirtle_preview128.png"));
		
		r2 = new TextureRegion(new Texture("gfx/pikachu_preview128.png"));
		
		r3 = new TextureRegion(new Texture("gfx/bulbasaur_preview128.png"));
		font.getData().setScale(4);
		
		
		s = new Sprite[W*H];
		y = 0;
		x = MainMenuScreen.WIDTH - W*ICON_SIZE;
		for(int x = 0; x < W; x++) {
			for(int y = 0; y < H; y++) {
				int n = MyRandom.random.nextInt(3);
				if(n == 0) {
					s[x + y*W] = new Sprite(r);
				}
				else if(n == 1) {
					s[x + y*W] = new Sprite(r2);
				}
				else {
					s[x + y*W] = new Sprite(r3);
				}
				
				s[x + y*W].setPosition(this.x + ICON_SIZE*x, this.y + ICON_SIZE*y);
			}
		}
	}
	
	float scale = 1;
	
	@Override
	public void handle(BattleInput input) {
		int mx = input.getXpixel();
		int my = input.getYpixel();
		if(mx  < x + W*ICON_SIZE  && mx >= x && my < y + H*ICON_SIZE && my >= y) {
			int dx = mx - x;
			int dy = my - y;
			int xi = dx / ICON_SIZE;
			int yi = dy / ICON_SIZE;
			select(xi+ yi*W);
		}
		else if(selected != -1){
			s[selected].setScale(1);
			selected = -1;
		}
	}
	
	public void render(SpriteBatch sb) {
		//sb.draw(r, x, y);
		for(int i = 0; i < s.length; i++) {
			if(i == selected) {
				
			}else {
				s[i].draw(sb);
			}
		}
		if(selected != -1) {
			s[selected].draw(sb);
			if(s[selected].getTexture() == r.getTexture()) {
				font.draw(sb, "squirtle", x, y + ICON_SIZE*H+40);
			}
			if(s[selected].getTexture() == r2.getTexture()) {
				font.draw(sb, "pikachu", x, y + ICON_SIZE*H+40);
			}
			if(s[selected].getTexture() == r3.getTexture()) {
				font.draw(sb, "bulbasaur", x, y + ICON_SIZE*H + 40);
			}
			
		}
			
		
	}
	
	public void select(int i) {
		if(selected != -1) {
			s[selected].setScale(1);
		}
		selected = i;
		s[i].setScale(1.25f);
	}
	

}
