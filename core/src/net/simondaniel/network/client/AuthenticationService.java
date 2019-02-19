package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.client.Request.LoginC;

public class AuthenticationService extends Listener{

	Client client;
	
	public AuthenticationService(Client c) {
		this.client = c;
	}
	
	public void login(String name, String pw) {
		LoginC p = new LoginC();
		p.name = name;
		p.pw = pw;
		client.sendTCP(p);
		System.out.println(p.name + " tried to connect with pw: " + p.pw);
	}
}
