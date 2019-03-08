package net.simondaniel.game.client.ui.masks;

import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;

public class ServerSelectionInfo extends ShowMaskInfo{

	public PlayClient client;
	
	public String greetingMessage;
	
	public boolean autoConnect;
	
	public boolean isComplete() {
		return !(client == null);
	}
}
