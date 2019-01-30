package net.simondaniel.network.chanels;

import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.server.Response.LoginS;

public class InitialConnectionChanel extends MessageChannel {

	public InitialConnectionChanel() {
		super("initial", new ClientState(),new ServerState());
	}
	
	
	private static class ClientState extends MessageChannelEnd{

		public ClientState() {
			super(new Class<?>[] {LoginS.class});
		}


	}
	
	
	private static class ServerState extends MessageChannelEnd{

		public ServerState() {
			super(new Class<?>[] { LoginC.class, RegisterUserC.class });
		}

//		@Override
//		protected void received(ServerPacket packet) {
//			Object o = packet.o;
//			UserConnection c = packet.con;
//			if (o instanceof LoginC) {
//
//				LoginC p = (LoginC) o;
//				LoginS r = new LoginS();
//				r.response = gs.login(c, p.name, p.pw);
//				c.sendTCP(r);
//				c.changeStateTo(MessageChannel.loggedInState);
//				packet.handle();
//				// FileTransfer.sendFile(c, Gdx.files.internal("gfx/tim_hero.png"),
//				// "gfx/userPics/testTransfer.png");
//			} else if (o instanceof RegisterUserC) {
//				RegisterUserC p = (RegisterUserC) o;
//				gs.registerUser(c, p.name, p.pw, p.email);
//				packet.handle();
//			}
//		}
	}
}
