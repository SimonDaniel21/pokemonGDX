package net.simondaniel.network.protocols;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.chanels.Protocol;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.client.ChanelListener;
import net.simondaniel.network.client.ChanelListenerList;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.UserConnection;

public class InitialProtocol extends Protocol{

	public InitialProtocol(boolean isServer) {
		super(MessageChannel.initialChannel);
	}

	private static class ClientListener extends ChanelListener{

		public ClientListener(boolean role, ChanelListenerList list) {
			super(MessageChannel.initialChannel, role, list);
		}

		@Override
		public void channelReceive(Connection c, Object o) {
			
		}
		
	}
	
	private static class ServerListener extends ChanelListener{

		public ServerListener(boolean role, ChanelListenerList list) {
			super(MessageChannel.initialChannel, role, list);
		}

		@Override
		public void channelReceive(Connection con, Object o) {
			UserConnection c = (UserConnection) con;
			
			System.err.println("SErver received : " + o.getClass().getName());
			if (o instanceof LoginC) {

				LoginC p = (LoginC) o;
				LoginS r = new LoginS();
				//r.response = gs.login(c, p.name, p.pw);
				c.sendTCP(r);
			
			} else if (o instanceof RegisterUserC) {
				RegisterUserC p = (RegisterUserC) o;
				//gs.registerUser(c, p.name, p.pw, p.email);
			}
		}
		
	}
}
