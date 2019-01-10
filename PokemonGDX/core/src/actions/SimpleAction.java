package actions;

public abstract class SimpleAction implements Action{

	private boolean finished = false;
	
	public abstract void perform();
	
	@Override
	public void update(float delta) {
		perform();
		finished = true;
	}
	@Override
	public boolean isFinished() {
		return finished;
	}
}
