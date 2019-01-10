package net.simondaniel.network.client;

import net.simondaniel.network.server.UserConnection;

/**
 * Responses get the suffix S for server
 * @author simon
 *
 */
public class Request {

	public static class ListenToChannelC{
		public String ChannelName;
	}
	
	public static class LoginC{
		public String name, pw;
	}
	
	public static class RegisterUserC{
		public String name, pw, email;
	}
	
	public static class AccountActivationC{
		public String name;
		public String code;
	}
	
	public static interface LoginChandler{
		public void handle(LoginC p);
	}
	
	public static class LobbyCreateC{
		public String name;
		public int gameMode;
	}
	
	public static class LobbyJoinC{
		public String lobbyName;
	}
	
	public static class LobbyLeaveC{
	}
	
	public static class TeamJoinC{
		public int teamID; // if zero undecided
	}
	
	public static class LobbyListC{
	}
	
	public static class UserListC{
	}
	
	public static class MessageC{
		public String message;
	}
	
	public static interface MessageChandler{
		public void handle(MessageC p);
	}
	
	public static class EntityAdressedPacket {
		
		public EntityAdressedPacket(int id, Object packet) {
			this.entityId = id;
			this.o = packet;
		}
		
		public int entityId;
		public Object o;
	}
	
	public static class MoveToC{
		public float x,y;
		public int id;
	}
	
	public static class InviteUserToLobbyC{
		public String user, lobby;
	}
	
	public static class InviteAnswerC{
		public String lobby;
		public boolean answer;
	}
	
	public static class UserReadyC{
		public boolean ready;
	} 
	//--------------------------------------
	
	public static class RequestAreaC{
		
	}

	public static interface RequestAreaChandler{
		public void handle(RequestAreaC p, UserConnection c);
	}
	
	public static class MovementC{
		public int corrections;
		public int id;
	}

	public static interface MovementChandler{
		public void handle(MovementC p, UserConnection c);
	}
}
