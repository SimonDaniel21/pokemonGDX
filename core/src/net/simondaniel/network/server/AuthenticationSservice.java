package net.simondaniel.network.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.jgit.transport.CredentialItem.Password;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.LogoutC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.client.Request.UserListC;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.database.DatabaseInerface;
import net.simondaniel.network.server.database.NotActivatedDO;
import net.simondaniel.network.server.database.UserProfileDO;
import net.simondaniel.util.MyRandom;

public class AuthenticationSservice extends Sservice{

	Listener requestServiceListener, connectionLostListener;
	
	private ArrayList<NeuerUser> loggedInUsers;
	
	private static final char[] forbiddenChars = new char[] {' ', '>', ':', ';', ','};
	
	public AuthenticationSservice(final PlayServer server) {
		super(server);
		loggedInUsers = new ArrayList<NeuerUser>();
		requestServiceListener = new Listener() {
			@Override
			public void received(Connection con, Object o) {
				NeuerUser u = (NeuerUser)con;
				if(o instanceof UserListC) {
					server.track.activate(con);
				}
			}
		};
		
		connectionLostListener = new Listener() {
			@Override
			public void received(Connection con, Object o) {
				NeuerUser u = (NeuerUser)con;
				if(o instanceof LogoutC) {
					logout(u);
					server.auth.activate(u);
					//u.sendTCP(o);
				}
			}
			public void disconnected(Connection con) {
				NeuerUser u = (NeuerUser)con;
				logout(u);
			};
		};
	}

	@Override
	protected void received(Connection con, Object o) {
		NeuerUser c = (NeuerUser)con;
		if(o instanceof LoginC) {
			LoginC p = (LoginC) o;
			System.out.println(p.name + " tries to connect with pw: " + p.pw);
			
			login(c, p.name, p.pw);
		}
		
		if(o instanceof RegisterUserC) {
			RegisterUserC p = (RegisterUserC) o;
			
			registerUser(c, p.name, p.pw, p.email);
		}
		
		if(o instanceof AccountActivationC) {
			AccountActivationC p = (AccountActivationC) o;
			
			tryActivateAccount(c, p.name, p.code);
		}
	}
	
	private boolean loggedIn(String name) {
		for(NeuerUser u : loggedInUsers) {
			if(u.account.getName().equals(name))
				return true;
		}
		return false;
	}
	
	private void login(NeuerUser con, String name, String pw) {

		LoginS p = new LoginS();
		String r = authenticate(con, name, pw);
		p.response = r;
		
		con.sendTCP(p);
		
		if(r.equals("success")) {
			con.addListener(requestServiceListener);
			con.addListener(connectionLostListener);
			con.account.login(name);
			loggedInUsers.add(con);
			server.auth.deactivate(con);
			server.match.activate(con);
		}
		
	}
	
	private String authenticate(NeuerUser con, String name, String pw) {
		
		if(loggedIn(name))
			return "account is already logged in";
		
		if(pw.equals("development")) return "success";
		
		UserProfileDO o = server.database.loadUser(name);
		NotActivatedDO no = server.database.loadNotActivatedName(name);
		if(o == null) return "user is not registered on this server";
		if(no != null) return "account has not been activated yet";
		return pw.equals(o.password)  ? "success" : "wrong password";
	}
	
	private void logout(NeuerUser con) {
		con.onLogout();
		con.removeListener(connectionLostListener);
		con.removeListener(requestServiceListener);
		loggedInUsers.remove(con);
		
		server.track.deactivate(con);
		server.match.deactivate(con);
	}

	public List<NeuerUser> getLoggedInUsers() {
		
		return loggedInUsers;
	}
	
	/**
	 * 
	 * @param name
	 * @return error message of whats wrong with the name or null if ok
	 */
	private String checkIfValid(String name, String pw) {
		
		for(char c : forbiddenChars) {
			if(name.indexOf(c) >= 0) {
				return "name cannot contain char '" + c + "'";
			}
			if(pw.indexOf(c) >= 0) {
				return "password cannot contain char '" + c + "'";
			}
		}
		if(name.length() < 3) {
			return "names have to be at least 3 characters long";
		}
		if(pw.length() < 3) {
			return "passwords have to be at least 3 characters long";
		}
		return null;
	}
	
	public void registerUser(Connection c, String name, String pw, String email) {
		MessageS response = new MessageS();
		response.sender = "server";
		response.type = 0;
		
		String nameErrors = checkIfValid(name, pw);
		if(nameErrors != null) {
			response.type = 0;
			response.message = nameErrors;
			c.sendTCP(response);
			return;
		}
		
		UserProfileDO updo = new UserProfileDO();
		updo.email = email;
		updo.name = name;
		updo.password = pw;
		
		String code = MyRandom.random.nextInt(9999) + "";
		
		NotActivatedDO no = new NotActivatedDO();
		no.name = name;
		no.code = code;
		DatabaseInerface database = server.database;
		response.message = database.checkValidation(updo);
		
		if(response.message.equals("success")) {
			if(!sendActivationMail(email, name, code))
				response.message = "email could not be sent";
			else {
				response.message = "registered and sent activation mail";
				response.type = 1;
				database.addUser(updo);
				database.addNotActivatedName(no);
				database.save();
			}
		}
		c.sendTCP(response);
	}
	
	private void tryActivateAccount(Connection c, String name, String code) {
		DatabaseInerface database = server.database;
		MessageS p = new MessageS();
		p.sender = "server";
		p.type = 0;
		
		UserProfileDO o = database.loadUser(name);
		NotActivatedDO no = database.loadNotActivatedName(name);
		
		if(o == null) {
			p.message = "user is not registered";
		}
		else if (no == null){
			p.message = "user does not need to activate account";
		}
		else {
			
			if(no.code.equals(code)){
				p.type = 2;
				
				p.message = "activated Account";
			}
			else {
				p.type = 3;
				p.message = "wrong code";
				database.removeUser(o.name);
			}
			database.removeNotActivatedName(name);
			database.save();
		}
		c.sendTCP(p);
	}

	private static boolean sendActivationMail(String email, String name, String code) {
		// Recipient's email ID needs to be mentioned.
	      String to = email;

	      // Sender's email ID needs to be mentioned
	      final String from = "simonservice222@gmail.com";
	      final String fromPassword = "servicepw134";

	      // Assuming you are sending email from localhost
	      
	      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	      // Get system properties
	      Properties properties = System.getProperties();
	      properties.setProperty("mail.user", from);
	      properties.setProperty("mail.password", "fromPassword");
	      properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	      properties.setProperty("mail.smtp.socketFactory.fallback", "false");
	      properties.setProperty("mail.smtp.port", "465");
	      properties.setProperty("mail.smtp.socketFactory.port", "465");
	      properties.put("mail.smtp.auth", "true");
	      
	      // Setup mail server
	      properties.setProperty("mail.smtp.host", "smtp.gmail.com");

	      try {
	    	  // Get the default Session object.
		      Session session = Session.getDefaultInstance(properties, new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, fromPassword);
                 }
                });
        
		      
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

	         // Set Subject: header field
	         message.setSubject("Aktivierung");

	         // Now set the actual message
	         message.setText("Du wolltest dich mit dem Accountnamen ( "+ name + ") registrieren\n\nhier ist dein AktivierungsCode: " + code);
	         message.setSentDate(new Date());
	         
	         // Send message
	         Transport.send(message);
	         return true;
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	        return false;
	      }
	}
}
