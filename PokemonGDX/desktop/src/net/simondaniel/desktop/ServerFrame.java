package net.simondaniel.desktop;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.ServerMonitor;

public class ServerFrame extends JFrame implements ServerMonitor{

	private JPanel contentPane;

	private GameServer server;
	JTextArea textArea;
	JLabel lblGames;
	
	List userList, lobbyList;
	private JScrollPane scrollPane;
	
	int lobbysActive = 0;
	
	int packetsReceived = 0;
	long startTime;
	
	/**
	 * Create the frame.
	 */
	public ServerFrame(final GameServer server) {
		
		this.server = server;
		//this.server.addListener(this);
		//this.server.addPacketHandler(this);
		
		setTitle("Server Control");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		userList = new List();
		userList.setBounds(10, 31, 110, 220);
		contentPane.add(userList);
		
		JButton filterAll = new JButton("all");
		filterAll.setBounds(460, 31, 110, 20);
		filterAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		contentPane.add(filterAll);
		
		JButton filterLobbyless = new JButton("no lobby");
		filterLobbyless.setBounds(460, 53, 110, 20);
		filterLobbyless.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		contentPane.add(filterLobbyless);
		
		lobbyList = new List();
		lobbyList.setBounds(460, 75, 110, 176);
		contentPane.add(lobbyList);
		
		JLabel lblUsers = new JLabel("Users");
		lblUsers.setBounds(10, 11, 46, 14);
		contentPane.add(lblUsers);
		
		JButton btnKickUser = new JButton("kick user");
		btnKickUser.setBounds(126, 31, 89, 23);
		btnKickUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("klicked");
				if(userList.getSelectedIndex() == -1) return;
				String s = userList.getItem(userList.getSelectedIndex());
				
				System.out.println("cant");
				if(server.getUser(s) != null) {
					System.out.println("find");
					//DisconnectResponse d = new DisconnectResponse();
					//d.msg = "you got kicked by the server";
					//server.getUser(s).sendTCP(d);
					
					server.kickUser(server.getUser(s));
				}
			}
		});
		contentPane.add(btnKickUser);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(126, 115, 298, 135);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		lblGames = new JLabel("0");
		lblGames.setBounds(378, 31, 46, 14);
		contentPane.add(lblGames);
		
		JLabel lblNewLabel_1 = new JLabel("Lobbys:");
		lblNewLabel_1.setBounds(258, 31, 110, 14);
		contentPane.add(lblNewLabel_1);
		
		startTime = System.currentTimeMillis();
	}

	@Override
	public void connected(String u) {
		userList.add(u);
	}
	@Override
	public void disConnected(String u) {
		userList.remove(u);
	}

	@Override
	public void updateUPS(float aCTUAL_UPS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(String sender, String message) {
		textArea.append(sender + ": " + message +"\n");
	}

	@Override
	public void addedLobby(String s) {
		messageReceived("[server]", " added Lobby " + s);
		lobbysActive++;
		lobbyList.add(s);
		lblGames.setText(lobbysActive+ "");
	}

	@Override
	public void removedLobby(String name) {
		messageReceived("[server]", " removed Lobby " + name);
		lobbysActive--;
		for(String s : lobbyList.getItems())
			if(s.equals(name))
				lobbyList.remove(s);
		
		lblGames.setText(lobbysActive+ "");
	}

	@Override
	public void packetReceived() {
		packetsReceived++;
		long timeDelta = System.currentTimeMillis() - startTime;
		float seconds = timeDelta / 1000.0f;
		float packetsPerSecond = packetsReceived / seconds;
		packetsPerSecond =  (float) (Math.round(packetsPerSecond* 100.0) / 100.0);
		setTitle("Server Contro (in: " + packetsReceived + ", " + packetsPerSecond + "/s)");
	}

	@Override
	public void packetSend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLobby(String l) {
		lobbyList.remove(l);
		lobbyList.add(l + " (+)");
	}

	@Override
	public void stopLobby(String l) {
		lobbyList.remove(l + " (+)");
		lobbyList.add(l);
	}

}