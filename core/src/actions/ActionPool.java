package actions;

import java.util.ArrayList;
import java.util.List;

public class ActionPool {

	List<Action> actions, dead;
	
	public ActionPool() {
		actions = new ArrayList<Action>();
		dead = new ArrayList<Action>();
	}
	
	public void runAction(Action a){
		actions.add(a);
		System.out.println("add");
	}
	
	public void update(float delta){
		
		for(Action a : actions){
			a.update(delta);
			if(a.isFinished())
				dead.add(a);
		}
		for(Action a : dead){
			actions.remove(a);
			
			System.out.println("rem");
		}
		dead.clear();
	}
}
