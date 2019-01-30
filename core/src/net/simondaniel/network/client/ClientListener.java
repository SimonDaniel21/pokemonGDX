package net.simondaniel.network.client;

import java.nio.file.FileSystem;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.PixmapIO;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.FileTransfer;
import net.simondaniel.network.client.GameClient.Packet;
import net.simondaniel.network.client.GameClient.State;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.FileTransferS;
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

	@SuppressWarnings("unused")
	@Override
	public void received (Connection c, Object o) {
		client.packetsReceived++;
		
		if(true != false)
			return;
		
		if(o instanceof EndConnectionS){
			EndConnectionS p = (EndConnectionS)o;
			client.disconnect(p.reason);
			client.addPacket(c, o);
		}
		else{
			client.addPacket(c, o);
		}
//		else if(o instanceof FileTransferS) {
//			FileTransferS p = (FileTransferS) o;
//			FileTransfer.receivedFileFrame(p);
//		}
		
	}

	@Override
	public void disconnected (Connection connection) {
		if(client.getState() != State.DISCONNECTED) {
			EndConnectionS p = new EndConnectionS();
			p.reason = "connection lost";
			client.addPacket(connection, p);
			client.disconnect(p.reason);
		}
		
		connection.close();
		System.err.println("got disconnected");
	}
}
