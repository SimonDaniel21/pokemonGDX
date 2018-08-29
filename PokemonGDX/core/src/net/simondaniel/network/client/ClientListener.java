package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.client.GameClient.Packet;
import net.simondaniel.network.client.GameClient.State;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.LobbyUserJoinedS;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;

public class ClientListener extends Listener{
	
	GameClient client;
	
	public ClientListener(GameClient client) {
		this.client = client;
	}
	
	@Override
	public void connected (Connection connection) {
	}

	@Override
	public void received (Connection c, Object o) {
		client.packetsReceived++;
		
		if(o instanceof LobbyUserJoinedS) {
			System.out.println("RECEIVED LOBBYJOIN but didnt forward");
		}
		
		if(o instanceof Ping){
			Ping p = (Ping)o;
			//System.err.println(c.getReturnTripTime());
			return;
		}
		
		if(o instanceof LoginS){
			LoginS p = (LoginS) o;
			System.out.println("client received login answer: " + p.response);
			client.errorMsg = p.response;
			client.verify(p.response.equals("success"));
			if(client.state == State.DECLINED){
				//c.close();
			}
		}
		else if(o instanceof EndConnectionS){
			EndConnectionS p = (EndConnectionS)o;
			client.disconnect(p.reason);
			client.packetBuffer.add(new Packet(c, o));
		}
		else if(o instanceof MessageS){
			MessageS r = (MessageS) o;
			//client.window.messageReceived(r.sender, r.message);
			client.packetBuffer.add(new Packet(c, o));
		}
		else{
			client.packetBuffer.add(new Packet(c, o));
		}
	}

	@Override
	public void disconnected (Connection connection) {
		if(client.getState() != State.DISCONNECTED) {
			EndConnectionS p = new EndConnectionS();
			p.reason = "connection lost";
			client.packetBuffer.add(new Packet(connection, p));
			client.disconnect(p.reason);
		}
		
		connection.close();
		System.err.println("got disconnected");
	}
}
