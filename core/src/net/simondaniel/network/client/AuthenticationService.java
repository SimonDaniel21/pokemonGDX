package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.ResponseRequest;
import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;

public class AuthenticationService extends Service{
		
	//public final Synchronized<String> response;
	
	public final ResponseRequest<LoginC, LoginS> loginRequest;
	public final ResponseRequest<RegisterUserC, MessageS> registerRequest;
	public final ResponseRequest<AccountActivationC, MessageS> activateAccountRequest;
	
	
	private final LoginC loginPacket = new LoginC();
	private final RegisterUserC registerPacket = new RegisterUserC();
	private final AccountActivationC accActivationPacket = new AccountActivationC();
	
	public AuthenticationService(Client c) {
		super(c);
	
		loginRequest = new ResponseRequest<LoginC, LoginS>(c, loginPacket) {
			@Override
			protected void receive(Object o) {
				if(o instanceof LoginS) {
					setReplyPacket((LoginS) o);
				}
			}
		};
		
		
		registerRequest = new ResponseRequest<RegisterUserC, MessageS>(c, registerPacket) {	
			@Override
			protected void receive(Object o) {
				if(o instanceof MessageS) {
					MessageS p = (MessageS) o;
					setReplyPacket(p);
				}
			}
		};
		
		
		activateAccountRequest = new ResponseRequest<AccountActivationC, MessageS>(c, accActivationPacket) {	
			@Override
			protected void receive(Object o) {
				if(o instanceof MessageS) {
					MessageS p = (MessageS) o;
					setReplyPacket(p);
				}
			}
		};
		
		//regResponse = new Synchronized<MessageS>();
	}
	
	@Override
	protected void received(Connection c, Object o) {
	}
	
	public void login(String name, String pw) {
		loginPacket.name = name;
		loginPacket.pw = pw;
		loginRequest.perform();
	}


	public void registerNewUser(String name, String pw, String mail) {
		
		registerPacket.name = name;
		registerPacket.pw = pw;
		registerPacket.email = mail;
		registerRequest.perform();
	}

	public void activateAccount(String name, String code) {
		
		accActivationPacket.name = name;
		accActivationPacket.code = code;
		activateAccountRequest.perform();
	}
}
