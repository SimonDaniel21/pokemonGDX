package net.simondaniel.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.esotericsoftware.kryonet.Server;

import net.simondaniel.Config;
import net.simondaniel.GameConfig;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.network.client.ClientMonitor;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.GameClient.State;
import net.simondaniel.network.server.GameServer;



public class MainMenu extends JFrame {

	JFrame frame;
	private JPanel contentPane;
	private JTextField txtSimon;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		
		frame = this;
		setTitle("Kartenspiel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		GameConfig config = Config.gameConfig;
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(10, 11, 66, 14);
		contentPane.add(lblPort);
		
		JLabel portLabel = new JLabel(config.PORT + "");
		portLabel.setBounds(86, 11, 46, 14);
		contentPane.add(portLabel);
		
		JLabel lblVersion = new JLabel("Version:");
		lblVersion.setBounds(10, 36, 66, 14);
		contentPane.add(lblVersion);
		
		JLabel versionLabel = new JLabel(PokemonGDX.VERSION);
		versionLabel.setBounds(86, 36, 90, 14);
		contentPane.add(versionLabel);
		
		JButton btnNewButton = new JButton("Registrieren");
		btnNewButton.setBounds(302, 7, 122, 43);
		btnNewButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("enter name:");
				if(name == null) return;
				String password = JOptionPane.showInputDialog("enter password:");
				if(password == null) return;
				String mail = JOptionPane.showInputDialog("enter email:");
				if(mail == null) return;
				register(name, password, mail);
			}
		});
		contentPane.add(btnNewButton);
		
		txtSimon = new JTextField();
		txtSimon.setText("simon");
		txtSimon.setBounds(86, 61, 86, 20);
		contentPane.add(txtSimon);
		txtSimon.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("development");
		textField_1.setBounds(86, 90, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		btnNewButton_1.setBounds(302, 55, 122, 55);
		contentPane.add(btnNewButton_1);
		
		JLabel lblName = new JLabel("name:");
		lblName.setBounds(10, 61, 46, 14);
		contentPane.add(lblName);
		
		JLabel lblPassword = new JLabel("password:");
		lblPassword.setBounds(10, 92, 66, 14);
		contentPane.add(lblPassword);
		
		JButton btnNewButton_2 = new JButton("start Server");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});
		btnNewButton_2.setBounds(302, 121, 122, 100);
		contentPane.add(btnNewButton_2);
		
		JLabel lblServerip = new JLabel("Server IP:");
		lblServerip.setBounds(10, 124, 66, 14);
		contentPane.add(lblServerip);
		
		textField_2 = new JTextField("OLD");
		textField_2.setBounds(86, 121, 86, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
	}
	
	public void login(){
		login(textField_2.getText(), txtSimon.getText(), textField_1.getText());
	}
	
	private boolean validClient = false;
	
	public void login(final String ip, final String name, final String pw){
		
		final MainMenu m = this;
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				GameClient client = new GameClient(ip, "server");
			
				client.errorMsg = "server timed out after 2000ms";
				if(client.waitForLogin()) {
					
					frame.dispose();
					validClient = true;
				}
				else {
					JOptionPane.showMessageDialog(frame, client.errorMsg);
				}
			}
		});
		t.start();
	}
	
	public void startServer(){
		GameServer server;
		try {
			server = new GameServer();
			server.bindMonitor(new ServerFrame(server));
			server.openConsole();
			server.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		this.dispose();
	}
	
	private void register(String name, String password, String email) {
		 // Recipient's email ID needs to be mentioned.
	      String to = "simonservice222@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "simonservice222@gmail.com";

	      // Assuming you are sending email from localhost
	      
	      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	      // Get system properties
	      Properties properties = System.getProperties();
	      properties.setProperty("mail.user", "simonservice222@gmail.com");
	      properties.setProperty("mail.password", "servicepw134");
	      properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	      properties.setProperty("mail.smtp.socketFactory.fallback", "false");
	      properties.setProperty("mail.smtp.port", "465");
	      properties.setProperty("mail.smtp.socketFactory.port", "465");
	      properties.put("mail.smtp.auth", "true");
	      
	      // Setup mail server
	      properties.setProperty("mail.smtp.host", "smtp.gmail.com");

	      String pw = "servicepw134";
	      String us = "simonservice222@gmail.com";

//	      try {
//	    	  // Get the default Session object.
//		      Session session = Session.getDefaultInstance(properties, new Authenticator(){
//                  protected PasswordAuthentication getPasswordAuthentication() {
//                      return new PasswordAuthentication(us, pw);
//                   }
//                  });
//          
//		      
//	         // Create a default MimeMessage object.
//	         MimeMessage message = new MimeMessage(session);
//
//	         // Set From: header field of the header.
//	         message.setFrom(new InternetAddress(from));
//
//	         // Set To: header field of the header.
//	         message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//
//	         // Set Subject: header field
//	         message.setSubject("Registrierung");
//
//	         // Now set the actual message
//	         message.setText("Folgender Nutzer will sich registrieren: \n\n" + name + "\n" + password + "\n" + email);
//	         message.setSentDate(new Date());
//	         
//	         // Send message
//	         Transport.send(message);
//	         JOptionPane.showMessageDialog(this, "Sent registration Mail!");
//	      } catch (MessagingException mex) {
//	         mex.printStackTrace();
//	         JOptionPane.showMessageDialog(this, "could not send Mail!");
//	      }
	}
}
