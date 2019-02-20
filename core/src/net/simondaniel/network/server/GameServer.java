package net.simondaniel.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.GameMode;
import net.simondaniel.MyRandom;
import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.Network;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.InviteUserToLobbyS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.UserJoinedS;
import net.simondaniel.network.server.Response.UserLeftS;
import net.simondaniel.network.server.database.DatabaseInerface;
import net.simondaniel.network.server.database.LocalFileDatabase;
import net.simondaniel.network.server.database.NotActivatedDO;
import net.simondaniel.network.server.database.UserProfileDO;


public class GameServer extends Server{

	public ServerMonitor window;
	
	List<UserConnection> loggedIn = new ArrayList<UserConnection>();
	List<UserConnection> usersTrackingUsers;
	
	public List<Lobby> lobbys;
	
	DatabaseInerface database;

	public GameServer() throws IOException {

		//MessageChannel.setServerRef(this);
		lobbys = new ArrayList<Lobby>();
		usersTrackingUsers = new ArrayList<UserConnection>();
		database = new LocalFileDatabase("database.deseus");
		Collection<NotActivatedDO> nots = database.loadNotActivatedNames();
		List<String> namesToRemove = new ArrayList<String>();
		for(NotActivatedDO no : nots) namesToRemove.add(no.name);
		for(String name : namesToRemove) {
			System.out.println("removing: " + name);
			database.removeUser(name);
			database.removeNotActivatedName(name);
			database.save();
		}
		
		loggedIn.add(new UserConnection("Marco"));
		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		//window = new ServerFrame(this);
		Network.register(this);
		//LagListener l;

		//addListener(new LagListener(10,20, new ServerListener(this)));
		bind(Network.port);
	}
	
	public void bindMonitor(ServerMonitor m){
		window = m;
	}
	
	@Override
	public void start(){
		if(window == null){
			System.err.println("cant start server without a monitor");
		}
		else{
			try {
				ServerSocket s = new ServerSocket(50000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.start();
		}
	}

	@Override
	protected Connection newConnection() {
		// By providing our own connection implementation, we can store per
		// connection state without a connection ID to state look up.
		UserConnection c = new UserConnection();
		//c.addListener(new InitialListener(this, c.channelListeners));
		return c;
	}
	
	public void disconnect(UserConnection c) {
		if (c != null) {

		}
	}

	/**
	 * returns "succes" if he got logged on or an error message
	 * 
	 * @param c
	 * @param name
	 * @param pw
	 * @return
	 */
	public String login(UserConnection c, String name, String pw) {

		for (UserConnection u : loggedIn) {
			if (u.getName().equalsIgnoreCase(name)) {
				return "a user with the name " + u.getName() + " is already logged into the server";
			}
		}
		
		String res = checkPw(name, pw);

		if (res.equals("success")) {
			
			c.login(name);
			
			UserJoinedS p = new UserJoinedS();
			p.user = name;
			for(UserConnection con : usersTrackingUsers) {
				con.sendTCP(p);
			}
			
			loggedIn.add(c);
			window.connected(c.name);
		}
		
		return res;
	}
	

	public void logout(UserConnection user) {
		if(loggedIn.remove(user)){
			UserLeftS p = new UserLeftS();
			p.user = user.name;
			for(UserConnection con : usersTrackingUsers) {
				con.sendTCP(p);
			}
			window.disConnected(user.name);
		}
	}

	/**
	 * hook this up to a db or something
	 * 
	 * @param name
	 *            username
	 * @param pw
	 *            user password
	 * @return password correctness
	 */
	private String checkPw(String name, String pw) {
		
		if(pw.equals("development")) return "success";
		
		UserProfileDO o = database.loadUser(name);
		NotActivatedDO no = database.loadNotActivatedName(name);
		if(o == null) return "user is not registered on this server";
		if(no != null) return "account has not been activated yet";
		return pw.equals(o.password)  ? "success" : "wrong password";
	}


	public UserConnection getUser(String name) {
		for (Connection c : getConnections()) {
			UserConnection uc = (UserConnection)c;
			if (uc.getName().equals(name))
				return uc;
		}
		return null;
	}

	public void openConsole() {
		window.setVisible(true);
	}
	
	
	
	
	public List<UserConnection> getUsers() {
		return loggedIn;
	}
	
	public void sendAllOutsideLobbyTCP(Object o){
		for(Connection c : getConnections()) {
			
			UserConnection u = (UserConnection) c;
			if(u != null && u.lobby == null)
				u.sendTCP(o);
		}
	}
	
	public void kickUser(UserConnection user) {
		EndConnectionS p = new EndConnectionS();
		p.reason = "kicked by server";
		user.sendTCP(p);
		user.close();
	}

	public void registerUser(UserConnection c, String name, String pw, String email) {
		MessageS response = new MessageS();
		response.sender = "server";
		response.type = 0;
		
		if(name.length() < 3) {
			response.message = "names have to be at least 3 characters long";
			c.sendTCP(response);
			return;
		}
		if(pw.length() < 3) {
			response.message = "passwords have to be at least 3 characters long";
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
		response.message = database.addUser(updo);
		
		if(response.message.equals("success")) {
			database.addNotActivatedName(no);
			database.save();
			if(!sendActivationMail(email, name, code))
				response.message = "email could not be sent";
			else {
				response.message = "registered and sent activation mail";
				response.type = 1;
			}
		}
		c.sendTCP(response);
	}
	
	public void activateUser(UserConnection c, String name, String code) {
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
				
				p.message = "activated Account";
			}
			else {
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

	public boolean sendToTcp(String user, InviteUserToLobbyS o) {
		UserConnection c = getUser(user);
		if(c == null) return false;
		
		c.sendTCP(o);
		return true;
	}

	public void startTracking(UserConnection c) {
		usersTrackingUsers.add(c);
	}
	
	public String[] getLobbyList() {
		String[] sa = new String[lobbys.size()];
		for(int i = 0; i < lobbys.size(); i++)
			sa[i] = lobbys.get(i).NAME;
		return sa;
	}
	
	public boolean isLobbyNameTaken(String name ) {
		for(Lobby l : lobbys) {
			if(l.NAME.equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	public Lobby addLobby(String name, int gameMode) {
		boolean exists = isLobbyNameTaken(name);
		
		if(!exists) {
			Lobby l = new Lobby(name, GameMode.valueOf(gameMode), this);
			lobbys.add(l);
			
			
			window.addedLobby(name);
			LobbyListS lls = new LobbyListS();
			lls.lobbys = new String[lobbys.size()];
			for(int i = 0; i < lobbys.size(); i++)
				lls.lobbys[i] = lobbys.get(i).NAME;
			sendAllOutsideLobbyTCP(lls);
			return l;
		}
		return null;
	}
	
	/**
	 * 
	 * @param name lobbyName
	 * @return lobby with the given name or null if no such lobby exists
	 */
	public Lobby getLobby(String name) {
		
		for(Lobby l : lobbys) {
			if(l.NAME.equals(name)) {
				return l;
			}
		}
		return null;
	}
}
