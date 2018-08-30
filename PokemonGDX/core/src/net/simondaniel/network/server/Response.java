package net.simondaniel.network.server;

import net.simondaniel.entities.EntityInformation;

/**
 * Responses get the suffix S for server
 * @author simon
 *
 */
public class Response {
	public static class LoginS{
		public String response;
	}
	
	public static class LobbyJoinS{
		public String name;
		public int gameMode;
		public String[][] others;
	}
	
	public static class LobbyUserJoinedS{
		public String name;
	}
	
	public static class TeamJoinedS{
		public String name;
		public int id, slotID;
	}
	
	public static class LobbyListS{
		public String[] lobbys;
	}
	
	public static class PlayerListS{
		public String[] players;
	}
	
	public static class MessageS{
		public String sender;
		public short type;
		public String message;
	}
	
	public static class MoveToS{
		public float x,y;
		public int id;
	}
	
	
	public static class StartGameS{
		public String[] players;
		public int gameMode;
 	}
	
	public static class EndConnectionS{
		public String reason;
	}
	
	public static class MovementS{
		public boolean approved;
	}
	
	public static interface MovementShandler{
		public void handle(MovementS p);
	}
	
	public static class LoadAreaS{
		public int w, h, parts;
	}

	public static class AreaPacketS{
		public int[] arrayPart;
	}
	
	public static interface AreaPacketShandler{
		public void handle(AreaPacketS p);
	}

	public static interface LoadAreaShandler{
		public void handle(LoadAreaS p);
	}
	
	public static class AddEntityS{
		public EntityInformation info;
	}

	public static interface AddEntityShandler{
		public void handle(AddEntityS p);
	}
	
	public static class InviteUserToLobbyS{
		//name is either the invite sender name OR the name of the invited player!
		public String name, lobby;
	}
}