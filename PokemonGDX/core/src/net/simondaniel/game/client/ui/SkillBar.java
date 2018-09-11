package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.simondaniel.Assets;
import net.simondaniel.entities.Drawer;

public class SkillBar implements Drawer{
	
	Skillicon icon1, icon2, icon3, icon4;
	
	private static final int SPACE = 2, startY = -320;

	public SkillBar() {
		int startX =  -(2*Skillicon.WIDTH + SPACE + SPACE/2);
		int between = Skillicon.WIDTH + SPACE;
		icon1 = new Skillicon(new TextureRegion(new Texture("gfx/atlases/hydro_pump/hydro_64.png")), startX, startY);
		icon2 = new Skillicon(new TextureRegion(new Texture("gfx/atlases/hydro_pump/hydro_64.png")), startX + between, startY);
		icon3 = new Skillicon(new TextureRegion(new Texture("gfx/fireblast_icon.png")), startX + between*2, startY);
		icon4 = new Skillicon(new TextureRegion(new Texture("gfx/atlases/hydro_pump/hydro_64.png")), startX + between*3, startY);
	}

	@Override
	public void render(SpriteBatch sb) {
		icon1.render(sb);
		icon2.render(sb);
		icon3.render(sb);
		icon4.render(sb);
	}

	@Override
	public void render(ShapeRenderer sr) {
	
	}
}
