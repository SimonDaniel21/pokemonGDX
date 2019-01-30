package actions;

public class ActionSequence implements Action{
	
	boolean finished;
	Action[] actions;
	
	int i;
	
	public ActionSequence(Action... actions){
		this.actions = actions;
	}

	@Override
	public void update(float delta) {
		while(!finished){
			actions[i].update(delta);
			if(!actions[i].isFinished()) break;
			
			i++;
			finished = i >= actions.length;
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}
}
