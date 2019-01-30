package net.simondaniel.network.chanels;

import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;


public class LobbyChanel extends MessageChannel {

	public LobbyChanel() {
		super("lobbyState", new ClientState(),new ServerState());
	}
	private static class ClientState extends MessageChannelEnd{

		public ClientState() {
			super(new Class<?>[] { LoginC.class, RegisterUserC.class });
		}

	}
	
	
	private static class ServerState extends MessageChannelEnd{

		public ServerState() {
			super(new Class<?>[] {});
		}
	}
}
