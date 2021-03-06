package net.simondaniel.network.server;

import java.awt.image.BufferedImage;
import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;

import net.simondaniel.fabio.phisx.SyncBodyInfo;

/**
 * Responses get the suffix S for server
 * @author simon
 *
 */
public class Response {
	
	public static class ServerInfoS{
		public String name;
	}
	
	public static class SyncBodiesS{
		public List<SyncBodyInfo> updates;
	}
	
	public static class LoginS{
		public String response;
	}
	
	public static class WorldStateS{
		public List<SyncBodyInfo> bodies;
	}
	
	public static class LobbyJoinS{
		public String name;
		public int gameMode;
		public String[] others;
		public int[] team;
		public String[] invitedPending, invitedAccepted, invitedDeclined;
	}
	
	public static class LobbyUserJoinedS{
		public String name;
	}
	
	public static class LobbyUserLeftS{
		public String name;
	}
	
	public static class TeamJoinedS{
		public String name;
		public int id, slotID;
	}
	
	public static class LobbyAddedS{
		public String name;
	}
	
	public static class PlayerListS{
		public UserJoinedS[] joined;
	}
	
	public static class UserJoinedS{
		public String user;
	}
	
	public static class UserLeftS{
		public String user;
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
	
	public static class UserReadyS{
		public String user;
		public boolean ready;
	} 
	
	public static class InviteUserToLobbyS{
		public String name, sender, lobby;
	}
	
	public static class InviteAnswerS{
		public String name;
		public boolean answer;
	}
	
	public static class LobbyStartTimerS{
		public long start;
	}
	public static class LobbyStopTimerS{
		public long start;
	}
	
	public static class FileTransferS{
		public byte[] data;
		public int index;
		public String fileName;
	}
}