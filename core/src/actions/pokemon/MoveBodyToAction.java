package actions.pokemon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import actions.Action;

public class MoveBodyToAction implements Action{

	Vector2 targetPosition;
	Body body;
	boolean finished = false;
	int state = 0;
	
	public MoveBodyToAction(Body b, float x, float y) {
		this.body = b;
		this.targetPosition = new Vector2(x,y);
	}
	
	@Override
	public void update(float delta) {
		Vector2 vel = body.getLinearVelocity();
	    float desiredVel = 0;
	    switch ( state )
	    {
	      case 0:  desiredVel = 5; break;
	      case 1:  desiredVel =  0; break;
	    }
	    float velChange = desiredVel - vel.x;
	    float impulse = body.getMass()* velChange; //disregard time factor
	    body.applyLinearImpulse( new Vector2(impulse,0), body.getWorldCenter(), true);
	    System.out.println(impulse);
	}

	@Override
	public boolean isFinished( ) {
		
		return finished;
	}

}
