package net.simondaniel.fabio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import net.simondaniel.game.client.gfx.AnimatedSprite;

public class AnimatedFabio extends AnimatedSprite {

	public AnimatedFabio() {
		super(new TextureAtlas("gfx/atlases/fabio/fabio.pack"), new String[] {"hit", "jump", "walk", "side", "crouch"});
		this.runAnimation(4);
	}

}
