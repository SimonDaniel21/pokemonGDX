package actions.pokemon;

import actions.Action;
import actions.ActionSequence;
import actions.SimpleAction;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.screens.Player;

public class MoveAction implements Action {

	ActionSequence actions;
	Player p;

	public MoveAction(final Player p, float x, float y) {
		this.p = p;
		float angle = getAngle(p.getX(), p.getY(), x, y);

		final AnimationDirection dir = AnimationDirection.fromAngle(angle);

		Action startWalk = new SimpleAction() {
			@Override
			public void perform() {
				p.getAnimation().runAnimation(AnimationType.MOVEMENT, dir);
			}
		};
		Action interpolation = new LinInterpolateVecAction(p.getPos(), x, y, 80);
		Action stopWalk = new SimpleAction() {
			
			@Override
			public void perform() {
				p.getAnimation().runAnimation(AnimationType.IDLE, dir);
			}
		};
		
		actions = new ActionSequence(startWalk, interpolation, stopWalk);
	}
	
	private static float getAngle(float x, float y, float x2, float y2) {
	    float angle = (float) Math.toDegrees(Math.atan2(y2 - y, x2 - x));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}

	@Override
	public void update(float delta) {
		actions.update(delta);
	}

	@Override
	public boolean isFinished() {

		return actions.isFinished();
	}

}
