package actions;

public class WaitAction implements Action{
	
	private float duration;
	private float elapsed;
	
	public WaitAction(float seconds) {
		this.duration = seconds;	}
	
	@Override
	public void update(float delta) {
		elapsed += delta;
	}

	@Override
	public boolean isFinished() {
		return elapsed >= duration;
	}
}
