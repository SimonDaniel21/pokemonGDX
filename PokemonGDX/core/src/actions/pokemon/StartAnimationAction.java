package actions.pokemon;

import actions.Action;
import net.simondaniel.game.client.gfx.AnimatedSprite;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;

public class StartAnimationAction implements Action {

	private boolean finished;
	private AnimatedSprite animation;
	private String animName;
	
	public StartAnimationAction(AnimatedSprite animation, String animName) {
		this.animation = animation;
	}
	@Override
	public void update(float delta) {
		animation.runAnimation(animName);
		finished = true;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
