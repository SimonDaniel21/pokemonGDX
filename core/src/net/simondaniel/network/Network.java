package net.simondaniel.network;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;

import com.badlogic.gdx.graphics.Pixmap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import net.simondaniel.fabio.input.InputSate.TransmittedInputState;
import net.simondaniel.fabio.phisx.SyncBodyInfo;
import net.simondaniel.network.client.Request;
import net.simondaniel.network.server.Response;
import net.simondaniel.network.server.Response.UserJoinedS;

public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.

	static public void register (EndPoint endPoint) {
		
		Kryo kryo = endPoint.getKryo();
		//own classes
		kryo.register(int[].class);
		kryo.register(short[].class);
		kryo.register(String[].class);
		kryo.register(String[][].class);
		kryo.register(UserJoinedS[].class);
		kryo.register(byte[].class);
		kryo.register(Message.class);
		kryo.register(TransmittedInputState.class);
		kryo.register(TransmittedInputState[].class);
		kryo.register(boolean[].class);
		kryo.register(java.util.ArrayList.class);
		kryo.register(SyncBodyInfo.class);
		
		for(Class<?> c : Request.class.getClasses()) {
			kryo.register(c);
		}
		
		for(Class<?> c : Response.class.getClasses()) {
			kryo.register(c);
		}
		
		
		//Register Requests
//		kryo.register(LoginC.class);
//		kryo.register(LobbyCreateC.class);
//		kryo.register(MessageC.class);
//		kryo.register(MovementC.class);
//		kryo.register(RequestAreaC.class);
		
		//Register Responses
//		kryo.register(LobbyJoinS.class);
//		kryo.register(LoginS.class);
//		kryo.register(MessageS.class);
//		kryo.register(MovementS.class);
//		kryo.register(EndConnectionS.class);
//		kryo.register(LoadAreaS.class);
//		kryo.register(AreaPacketS.class);
//		kryo.register(AddEntityS.class);
		
		
		//auto
		
	}
}
