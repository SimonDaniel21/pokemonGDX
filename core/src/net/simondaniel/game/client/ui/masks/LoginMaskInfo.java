package net.simondaniel.game.client.ui.masks;

import net.simondaniel.network.client.GameClient;

public class LoginMaskInfo extends ShowMaskInfo{

	public GameClient client;
	
	public boolean isComplete() {
		
		return !(client == null);
	}

}
