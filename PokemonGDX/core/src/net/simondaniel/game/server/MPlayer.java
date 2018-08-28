package net.simondaniel.game.server;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.client.Request.MovementChandler;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.network.server.Response.MovementS;

public class MPlayer extends EntityS implements MovementChandler{

	public String name;
	private UserConnection con;
	
	public MPlayer(UserConnection con){
		name = con.user.getName();
		this.con = con;
	}
	

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}
	
	int corrections = 0;


	@Override
	public void handle(MovementC p, UserConnection c) {
		if(p.corrections < corrections)return;
			
		MovementS s = new MovementS();
		int nx = x, ny = y;
		
		
		s.approved = (nx < 50);
		//s.approved = !tiles[nx + ny*width].isSolid();
		if(s.approved) {
			x = nx;
			y = ny;
		}
		else {
			corrections++;
		}
		System.out.println("x is " + nx + " sending " + s.approved);
		c.sendTCP(s);
	}

}
