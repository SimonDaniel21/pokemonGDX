package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.client.Request.EntityAdressedPacket;
import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.MessageC;
import net.simondaniel.network.client.Request.MoveToC;
import net.simondaniel.network.client.Request.MovementC;
import net.simondaniel.network.server.GameServer.Packet;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.MoveToS;
import net.simondaniel.network.server.Response.MovementS;

public class ServerListener extends Listener{
	
	private GameServer server;
	
	public ServerListener(GameServer server) {
		this.server = server;
	}
	
	@Override
	public void received(Connection con, Object o) {
		// We know all connections for this server are actually CharacterConnections.
		UserConnection c = (UserConnection) con;
		//System.out.println("server is holding " + server.server.getConnections().length + " connections and " + server.loggedIn.size() + " users");
		
		System.out.println("received something");
		if(o instanceof LoginC) {
			LoginC p = (LoginC) o;
			LoginS r = new LoginS();
			r.response = server.login(c, p.name, p.pw);
			c.sendTCP(r);
		}
		else if(o instanceof MessageC){
			MessageC p = (MessageC) o;
			MessageS s = new MessageS();
			s.message = p.message;
			s.sender = c.user.name;
			server.window.messageReceived(s.sender, s.message);
			server.sendToAllTCP(s);
		}
		else {
			//server.packetBuffer.add(new Packet(c, o));
		}
		
		
		
		
		/*for(RequestHandler h : server.packetHandlers) h.handle(user, object);;
		
		if(object instanceof LoginRequest) {
			LoginRequest l = (LoginRequest) object;
			LoginResponse r = new LoginResponse();
			
			r.msg = server.login(user, l.name, l.pw);
			c.sendTCP(r);
			
			if(r.msg.equals("success")) {
				for(Connection con : server.server.getConnections()) {
					UserConnection uc = (UserConnection)con;
					if(uc.user != null) {
						addUserResponse add = new addUserResponse();
						//add.user = u.getName();
						uc.sendTCP(add);
						addUserResponse add2 = new addUserResponse();
						add2.user = uc.user.getName();
						c.sendTCP(add2);
					}
				}
			}
		}*/

		
	}


	
}
