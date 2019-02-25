package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.Synchronized;
import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.screens.tempNet.MyCallback;

public class AuthenticationService extends Service{
	
	private MyCallback onAuth;
	
	public final Synchronized<String> response;
	public final Synchronized<MessageS> regResponse;
	private Client client;
	private boolean authenticated;
	
	RegisterUserC registerPacket = new RegisterUserC();
	
	public AuthenticationService(Client c) {
		super(c);
		response = new Synchronized<String>();
		regResponse = new Synchronized<MessageS>();
	}
	
	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof LoginS) {
			LoginS p = (LoginS) o;
			if(p.response.equals("success")) {
				onAuth.perform();
				authenticated = true;
			}
			response.set(p.response);
		}
		
		if(o instanceof MessageS) {
			MessageS p = (MessageS) o;
			regResponse.set(p);
		}
	}
	
	public void login(String name, String pw) {
		LoginC p = new LoginC();
		p.name = name;
		p.pw = pw;
		send(p);
		//System.out.println(p.name + " tried to connect with pw: " + p.pw);
	}

	public void setAuthenticationCallback(MyCallback c) {
		this.onAuth = c;
	}

	public void registerNewUser(String name, String pw, String mail) {
		
		registerPacket.name = name;
		registerPacket.pw = pw;
		registerPacket.email = mail;
		send(registerPacket);
	}

	public void activateAccount(String name, String code) {
		AccountActivationC p = new AccountActivationC();
		p.name = name;
		p.code = code;
		send(p);
	}
}
