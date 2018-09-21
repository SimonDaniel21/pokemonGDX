package net.simondaniel.network.chanels;

import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.LobbyListC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.client.Request.UserListC;

public class LoggedInChanel extends MessageChannel{

	public LoggedInChanel() {
		super("loggedIn", new ClientState(),new ServerState());
	}
	
	
	private static class ClientState extends MessageChannelEnd{

		public ClientState() {
			super(new Class<?>[] { LoginC.class, RegisterUserC.class });
		}
	}
	
	
	private static class ServerState extends MessageChannelEnd{

		public ServerState() {
			super(new Class<?>[] {LobbyListC.class, UserListC.class, LobbyCreateC.class, LobbyJoinC.class});
		}

//		@Override
//		protected void received(ServerPacket packet) {
//			Object o = packet.o;
//			UserConnection c = packet.con;
//			if(o instanceof LobbyListC) {
//				LobbyListS lls = new LobbyListS();
//				lls.lobbys = gs.getLobbyList();
//				c.sendTCP(lls);
//				packet.handle();
//			}
//			else if(o instanceof UserListC) {
//				PlayerListS pls = new PlayerListS();
//				pls.joined = new UserJoinedS[gs.getUsers().size()];
//				for(int i = 0; i < gs.getUsers().size(); i++) {
//					UserJoinedS s = new UserJoinedS();
//					s.user = gs.getUsers().get(i).getName();
//					pls.joined[i] = s;
//				}
//				c.sendTCP(pls);
//				gs.startTracking(c);
//				packet.handle();
//			}
//			else if(o instanceof LobbyCreateC){
//				LobbyCreateC p = (LobbyCreateC) o;
//			
//				if(gs.isLobbyNameTaken(p.name)) {
//						MessageS m = new MessageS();
//						m.sender = "server";
//						m.message = "lobby name already exists";
//						m.type = 1;
//						c.sendTCP(m);
//				}
//				else {
//					Lobby l = gs.addLobby(p.name, p.gameMode);
//					l.addUser(c);
//					c.changeStateTo(MessageChannel.lobbyState);
//				}
//			}
//			else if(o instanceof LobbyJoinC) {
//				LobbyJoinC p = (LobbyJoinC) o;
//				Lobby l = gs.getLobby(p.lobbyName);
//				if(l != null) {
//					l.addUser(c);
//					c.changeStateTo(MessageChannel.lobbyState);
//				}
//			}
//		}
	}
}
