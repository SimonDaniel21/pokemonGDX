package net.simondaniel.desktop;

import java.awt.Component;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import net.simondaniel.Config;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.network.client.ClientMonitor;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.Request.MessageC;

public class ClientFrame extends JFrame implements ClientMonitor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	JTextArea textArea;
	JTextArea textArea_1;
	List list;
	JLabel lblNewLabel_1;
	
	GameClient client;
	private static int i = 1;
	
	/**
	 * Create the frame.
	 */
	public ClientFrame(final GameClient client) {
		this.client = client;
		
		setTitle("Kartenspiel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100+540*i, 100, 500, 400);
		i++;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 260, 217);
		contentPane.add(textArea);
		
		JLabel lblServer = new JLabel("Server:");
		lblServer.setBounds(280, 16, 71, 14);
		contentPane.add(lblServer);
		
		JLabel lblNewLabel = new JLabel("OLD");
		lblNewLabel.setBounds(363, 16, 111, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(280, 41, 71, 14);
		contentPane.add(lblName);
		
		lblNewLabel_1 = new JLabel("no-name");
		lblNewLabel_1.setBounds(363, 41, 111, 14);
		contentPane.add(lblNewLabel_1);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(10, 239, 260, 111);
		textArea_1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
					e.consume();
				}
			}
		});
		contentPane.add(textArea_1);
		
		list = new List();
		list.setBounds(282, 67, 157, 248);
		contentPane.add(list);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		JMenuItem mntmStartGame = new JMenuItem("start game");
		popupMenu.add(mntmStartGame);
		
		JButton btnInvite = new JButton("invite");
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startGame();
			}
		});
		btnInvite.setBounds(292, 325, 117, 25);
		contentPane.add(btnInvite);
		
	}
	
	public void startGame() {
		DesktopLauncher.launchClient();
	}
	
	/**
	 * sets the user
	 */
	public void LoggedInServer() {
		lblNewLabel_1.setText(client.userName());
	}
	
	private void send() {
		//Packet01TextMessage p = new Packet01TextMessage(textArea_1.getText());
		//client.send(p);
		MessageC s = new MessageC();
		s.message = textArea_1.getText();
		client.sendTCP(s);
		textArea_1.setText("");;
	}

	public void login(String s) {
		System.out.println("name: " + s);
		list.add(s);
	}

	public void logout(String s) {
		list.remove(s);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	@Override
	public void messageReceived(String sender, String message) {

		textArea.append(sender + ": " + message + "\n");
	}
}
