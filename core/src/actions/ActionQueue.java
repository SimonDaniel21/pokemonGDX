package actions;

import com.badlogic.gdx.utils.Queue;

public class ActionQueue implements Action{
	
	private boolean busy, done; // when done the queue doesnt accept more Actions
	private boolean finished;
	private Queue<Action> actions;
	
	public ActionQueue(Action... actions){
		this.actions = new Queue<Action>();
		for(Action a : actions)
			this.actions.addLast(a);
	}

	@Override
	public void update(float delta) {
		while(busy && !finished){
			Action head = actions.first();
			head.update(delta);
			if(!head.isFinished()) break;
			
			actions.removeFirst();
			busy = actions.size != 0;
			if(done && !busy)
				finished = true;
		}
	}

	public boolean isBusy() {
		return busy;
	}
	
	public void endQueueWhenDone() {
		done = true;
	}
	
	@Override
	public boolean isFinished() {
		return finished;
	}
	
	public void add(Action...actions) {
		if(done) return;
		for(Action a : actions)
			this.actions.addLast(a);
		busy = this.actions.size != 0;
	}
}
