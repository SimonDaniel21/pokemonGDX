package net.simondaniel.game.client.ui.masks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryonet.Connection;
import net.simondaniel.fabio.GameMode;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.Friendlist;
import net.simondaniel.game.client.ui.Inbox;
import net.simondaniel.game.client.ui.Inbox.MailListener;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.InviteList;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.MyListener;
import net.simondaniel.network.client.Request.InviteAnswerC;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.LobbyListC;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.InviteUserToLobbyS;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.StartGameS;
import net.simondaniel.network.server.Response.UserJoinedS;
import net.simondaniel.network.server.Response.UserLeftS;
import net.simondaniel.screens.IngameScreen;

public class GameMenu extends UImask<LoginMaskInfo>{

	NamingDialog nda;
	Table lobbyTable;
	Friendlist fl;
	ArrayList<String> otherPlayers;
	LobbyMask lobbyMask;
	
	MyListener listener;
	
	Inbox inbox;
	
	MailListener gameInviteListener;
	
	public GameMenu(final Skin skin) {
		super(new LoginMaskInfo(), skin);
		
		listener = new GameMenuListener();
		gameInviteListener = new GameInviteListener();
		
		lobbyMask  = new LobbyMask(skin);
		
		lobbyTable = new Table(skin);
		
		otherPlayers = new ArrayList<String>();
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
		info.inviteableUsers = otherPlayers;
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
		
		gc.addMyListener(listener);
		
	}

	@Override
	public void leave() {
		info.client.removeMyListener(listener);
	}
	
	private class GameMenuListener implements MyListener{

		private void playerJoined(String name) {
			if(!info.client.userName().equals(name)) {
				otherPlayers.add(name);
				fl.addUser(name);
			}
		}
		
		private void playerLeft(String name) {
			otherPlayers.remove(name);
		}
		
		@Override
		public void received(Connection c, Object o) {
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
				System.out.println("RECEIVED LOBBY LIST");
				LobbyListS p = (LobbyListS)o;
				lobbyTable.clear();
				lobbyTable.add("public Lobbys: ").row();
				for(String l : p.lobbys) {
					final TextButton b = new TextButton(l, getSkin(), "menu-list-button");
					b.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							String lobbyName = b.getText().toString();
							LobbyJoinC p = new LobbyJoinC();
							p.lobbyName = lobbyName;
							info.client.send(p);
						}
					});
					b.setColor(Color.GREEN);
					lobbyTable.add(b);
					lobbyTable.row();
				}
			}
			if(o instanceof PlayerListS) {
				PlayerListS p = (PlayerListS)o;
				for(UserJoinedS ujs : p.joined) {
					playerJoined(ujs.user);
				}
			}
			if(o instanceof UserJoinedS) {
				UserJoinedS p = (UserJoinedS)o;
				playerJoined(p.user);
			}
			if(o instanceof UserLeftS) {
				UserLeftS p = (UserLeftS)o;
				playerLeft(p.user);
			}
			if(o instanceof StartGameS) {
				System.err.println("received Startgame");
				PokemonGDX.game.setScreen(new IngameScreen(info.client));
				getStage().dispose();
			}
			if(o instanceof InviteUserToLobbyS) {
				System.out.println("received invite message");
				InviteUserToLobbyS p = (InviteUserToLobbyS) o;
				if(p.name.equals(info.client.userName())) {
					System.out.println("name is correct");
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
			info.client.send(p);
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
}
