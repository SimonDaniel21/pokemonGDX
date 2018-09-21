package net.simondaniel.network.protocols;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.client.ChanelListener;
import net.simondaniel.network.client.ChanelListenerList;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.Response.LoginS;

public class InitialListener extends ChanelListener{

	GameServer gs;
	public InitialListener(GameServer server, ChanelListenerList list) {
		super(MessageChannel.initialChannel, true, list);
		this.gs = server;
	}



	@Override
	protected void channelReceive(Connection con, Object o) {
		UserConnection c = (UserConnection) con;
		
		if (o instanceof LoginC) {
			System.err.println("!!SErver received : " + o.getClass().getName());
			LoginC p = (LoginC) o;
			LoginS r = new LoginS();
			r.response = gs.login(c, p.name, p.pw);
			c.sendTCP(r);
		
		} else if (o instanceof RegisterUserC) {
			RegisterUserC p = (RegisterUserC) o;
			gs.registerUser(c, p.name, p.pw, p.email);
		}
	}
}