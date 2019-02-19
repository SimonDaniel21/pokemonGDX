package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.GameMode;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.Friendlist;
import net.simondaniel.game.client.ui.Inbox;
import net.simondaniel.game.client.ui.Inbox.MailListener;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.network.UserTracker;
import net.simondaniel.network.UserTracker.UserTrackerListener;
import net.simondaniel.network.chanels.MessageChannel;
import net.simondaniel.network.client.ChanelListener;
import net.simondaniel.network.client.ChanelListenerList;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.Request.InviteAnswerC;
import net.simondaniel.network.client.Request.LobbyListC;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.InviteUserToLobbyS;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.StartGameS;
import net.simondaniel.screens.IngameScreen;

public class GameMenu extends UImask<LoginMaskInfo>{

	NamingDialog nda;
	Table lobbyTable;
	Friendlist fl;
	LobbyMask lobbyMask;
	
	ChanelListener listener;
	
	Inbox inbox;
	
	MailListener gameInviteListener;
	UserTrackerListener userTrackerListener;
	
	UserTracker userTracker;
	
	
	public GameMenu(final Skin skin) {
		super(new LoginMaskInfo(), skin);
		
		gameInviteListener = new GameInviteListener();
		
	
		lobbyMask  = new LobbyMask(skin);
		
		
		
		lobbyTable = new Table(skin);
	
		final List<String> list = new List<String>(skin);
		list.setItems(new String[]{"1v1", "2v2", "3v3"});
		add(lobbyTable);
		
		final NamingDialog nda = new NamingDialog("enter Name", skin, new Entry("new lobby")) {
			
			@Override
			public void updateValues(String[] values) {
				
				info.client.sendLobbyCreateRequest(values[0], GameMode.ONE_VS_ONE);
				deactivateUntilResponse();
			}
		};
		Table startTable = new Table(skin);
		TextButton startGame = new TextButton("start", skin);
		startGame.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameMode mode = GameMode.valueOf(list.getSelectedIndex());
				if(mode == GameMode.ONE_VS_ONE)
					nda.show(getStage());
				else {
					InfoDialog.show("not iplemented", getStage());
				}
			}
		});
		startTable.add(list);
		startTable.add(startGame);
		this.add(startTable).expand().colspan(3);
		this.row();
		inbox = new Inbox(skin);
		this.add(inbox).size(270,350).left().bottom();
		add(lobbyTable).expandX();
		fl = new Friendlist(skin);
		this.add(fl).size(300, 300).right().bottom();
		
		
	}
	
	public void enterLobby(String name,GameMode mode, String[][] others,
			String[] sa, String[] sd, String[] sp) {
	
		LobbyMaskInfo info = lobbyMask.getInfo();
		info.gc = getInfo().client;
		info.lobbyName = name;
		info.mode = mode;
		info.others = others;
		info.userTracker = userTracker;
		info.joinLobby = true;
		switchTo(lobbyMask);
		for(String s : sa) {
			lobbyMask.inviteList.setAccepted(s);
		}
		for(String s : sd) {
			lobbyMask.inviteList.setDeclined(s);
		}
		for(String s : sp) {
			lobbyMask.inviteList.setPending(s);
		}
	}
	
	@Override
	public void act(float delta) {
		info.client.handlePacketBuffer();
		super.act(delta);
	}

	@Override
	public void enter() {
		final GameClient gc = info.client;
	
		gc.sendTCP(new LobbyListC());
		
		userTracker = new UserTracker(info.client.myListeners);
		userTrackerListener = new UserListener();
		userTracker.addListener(userTrackerListener);
		userTracker.startTracking(gc);
		
		listener = new GameMenuListener(info.client.myListeners);
		gc.addChanelListener(listener);
		
		if(PokemonGDX.CONFIGURATION == LaunchConfiguration.LOGGED_IN) {
			info.client.sendLobbyJoinRequest("testLobby");
		}
	}

	@Override
	public void leave() {
		info.client.removeChanelListener(listener);
		userTracker.removeListener(userTrackerListener);
	}
	
	private class GameMenuListener extends ChanelListener{
		
		public GameMenuListener(ChanelListenerList list) {
			super(MessageChannel.gameMenuState, false, list);
		}


		@Override
		protected void channelReceive(Connection c, Object o) {
			if(o instanceof EndConnectionS) {
				EndConnectionS p = (EndConnectionS) o;
				InfoDialog.show(p.reason, getStage());
			}
			if(o instanceof LobbyJoinS) {
				LobbyJoinS p = (LobbyJoinS)o;
				reActivateUI();
				if(p.gameMode != -1) {
					enterLobby(p.name, GameMode.valueOf(p.gameMode), p.others,
							p.invitedAccepted,
							p.invitedDeclined,
							p.invitedPending);
				}
				else {
					InfoDialog.show("lobby is full already", getStage());
				}
			}
			if(o instanceof MessageS) {
				MessageS s = (MessageS)o;
				if(s.sender.equals("server")) {
					InfoDialog.show(s.message, getStage());
					reActivateUI();
				}
			}
			if(o instanceof LobbyListS) {
				LobbyListS p = (LobbyListS)o;
				lobbyTable.clear();
				lobbyTable.add("public Lobbys: ").row();
				for(String l : p.lobbys) {
					final TextButton b = new TextButton(l, getSkin(), "menu-list-button");
					b.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							String lobbyName = b.getText().toString();
							info.client.sendLobbyJoinRequest(lobbyName);
						}
					});
					b.setColor(Color.GREEN);
					lobbyTable.add(b);
					lobbyTable.row();
				}
			}
			if(o instanceof StartGameS) {
				System.err.println("received Startgame");
				PokemonGDX.game.setScreen(new IngameScreen(info.client));
				getStage().dispose();
			}
			if(o instanceof InviteUserToLobbyS) {
			
				InviteUserToLobbyS p = (InviteUserToLobbyS) o;
				if(p.name.equals(info.client.userName())) {
					inbox.addMail("game invite", p.sender + " wants you to join " + p.lobby,
							new String[] {p.lobby},
							gameInviteListener);
				}
			}
		}
		
	}
	
	private class GameInviteListener implements MailListener{

		
		@Override
		public void accept(String[] data) {
			InviteAnswerC p = new InviteAnswerC();
			p.lobby = data[0];
			p.answer = true;
			info.client.send(MessageChannel.initialChannel, p);
			deactivateUntilResponse();
		}

		@Override
		public void decline(String[] data) {
			InviteAnswerC p = new InviteAnswerC();
			p.lobby = data[0];
			p.answer = false;
			info.client.send(p);
		}
	}
	
	private class UserListener implements UserTrackerListener{

		@Override
		public void userJoined(String name) {
			if(!info.client.userName().equals(name)) {
				fl.addUser(name);
				System.out.println("adding " + name);
			}
		}

		@Override
		public void userLeft(String name) {
			fl.removeUser(name);
		}

		@Override
		public void reset() {
		
		}
		
	}
}