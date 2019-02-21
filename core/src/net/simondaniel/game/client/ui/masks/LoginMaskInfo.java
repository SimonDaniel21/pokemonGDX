package net.simondaniel.game.client.ui.masks;

import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;

public class LoginMaskInfo extends ShowMaskInfo{

	public PlayClient client;
	
	public boolean isComplete() {
		
		return !(client == null);
	}

}
