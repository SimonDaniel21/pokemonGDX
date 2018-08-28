package net.simondaniel.game.client.gfx;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import net.simondaniel.game.client.gfx.AnimationLayout.PokemonAnimationLayout;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;

public class PKMNanimation extends AnimatedSprite{

	public PKMNanimation(TextureAtlas textureAtlas) {
		super(textureAtlas, getNamesPlusDirection(PokemonAnimationLayout.squirtle.LAYOUT.animationNames));
	}
	
	static String[] getNamesPlusDirection(String[] sa) {
		int directions = AnimationDirection.values().length;
		
		int l = sa.length;
		
		String[] res = new String[l*directions];
		
		for(int i = 0; i < l; i++) {
			for(int d = 0; d < directions; d++){
				
				res[i + d*l] = sa[i] + "_" + AnimationDirection.values()[d].name;
			}
		}
		return res;
	}

}