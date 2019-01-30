package net.simondaniel.control;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import net.simondaniel.entities.Entity;
import net.simondaniel.fabio.input.FabioInput;
import net.simondaniel.fabio.input.FabioInput.BattleInputType;
import net.simondaniel.network.Network_interface;
import net.simondaniel.network.client.Request.EntityAdressedPacket;
import net.simondaniel.network.client.Request.MoveToC;
import net.simondaniel.network.server.Response.MoveToS;

public class PokemonController extends ClientServerController<PokemonControl>{
	
	FabioInput in;

	public PokemonController(Entity e, Network_interface net,  boolean isServer) {
		this(e, net, null, isServer);
	}
	
	public PokemonController(Entity e, Network_interface net, FabioInput input,  boolean isServer) {
		super(new PokomControlClientSend(e, net), new PokomControlServer(e, net), new PokomControlClienReceive(e, net), isServer, e);
		this.in = input;
	}

	@Override
	public void clientControl() {
		if(in.isJustPressed(BattleInputType.MOVE_TO_CURSOR)) {
			controll_ifc.walkTo(new Vector2(in.getWorldX(), in.getWorldY()));
		}
		
	}
	
	@Override
	protected void serverControl() {
		
	}

	static class PokomControlClientSend extends PokemonControl{

		public PokomControlClientSend(Entity e, Network_interface net) {
			super(e, net);
		}


		@Override
		public void skill1() {
			
		}

		@Override
		public void skill2() {
			
		}

		@Override
		public void walkTo(Vector2 dest) {
			
			System.out.println("sending..");
		}
		
	}
	
	static class PokomControlClienReceive extends PokemonControl{

		public PokomControlClienReceive(Entity e, Network_interface net) {
			super(e, net);
		}


		@Override
		public void skill1() {
			
		}

		@Override
		public void skill2() {
			
		}

		@Override
		public void walkTo(Vector2 dest) {
		}
		
	}
	
	static class PokomControlServer extends PokemonControl{

		public PokomControlServer(Entity e, Network_interface net) {
			super(e, net);
		}

		@Override
		public void walkTo(Vector2 dest) {
			System.err.println("EXECUTED");
			
		}

		@Override
		public void skill1() {
			
		}

		@Override
		public void skill2() {
			
		}
		
	}

	@Override
	public void handle(MoveToS p) {
		this.clientReceive_ifc.walkTo(new Vector2(p.x, p.y));
	}
}
