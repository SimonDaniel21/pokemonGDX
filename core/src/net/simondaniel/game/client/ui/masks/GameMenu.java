package net.simondaniel.game.client.ui.masks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.util.MyColor;

public class GameMenu extends UImask<LoginMaskInfo>{

	NamingDialog nda;
	Table lobbyTable;
	Friendlist fl;
	LobbyMask lobbyMask;
	LoginMask loginMask;
	

	Inbox inbox;
	
	MailListener gameInviteListener;
	
	PlayClient client;
	
	
	public GameMenu(final Skin skin, UImaskHandler stage) {
		super(new LoginMaskInfo(), skin, stage);
		
		//SgameInviteListener = new GameInviteListener();
		
		this.debug();
		lobbyMask  = stage.lobby_mask;	
		
		TextButton logoutButton = new TextButton(MyColor.dye(Color.PINK, "logout"), skin);
		logoutButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				client.logout();
				switchTo(loginMask);
			}
			
		});
		add(logoutButton).top().left();
		
		
		lobbyTable = new Table(skin);
	
		final List<String> list = new List<String>(skin);
		list.setItems(new String[]{"1v1", "2v2", "3v3"});
		//add(lobbyTable);
		
		final NamingDialog nda = new NamingDialog("enter Name", skin, new Entry("new lobby")) {
			
			@Override
			public void updateValues(String[] values) {
				
				//info.client.sendLobbyCreateRequest(values[0], GameMode.ONE_VS_ONE);
				client.matchService.addLobby(values[0], 0);
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
	
	private void enterLobby(String name,GameMode mode, String[][] others,
			String[] sa, String[] sd, String[] sp) {
	
		LobbyMaskInfo info = lobbyMask.getInfo();
		//info.gc = getInfo().client;
		info.lobbyName = name;
		info.mode = mode;
		//info.others = others;
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
	
	ArrayList<String> dummy = new ArrayList<String>();
	ArrayList<String> lobbys = new ArrayList<String>();
	
	@Override
	public void act(float delta) {
		if(client.trackingService.names.sync(dummy)) {
			fl.set(dummy);
		}
		if(client.matchService.lobbyNames.sync(lobbys)) {
			buildLobbyTable();
		}
		if(client.matchService.lobbyToJoin.isReady()) {
			LobbyJoinS lobbyToJoin = client.matchService.lobbyToJoin.consume();
			
			if(lobbyToJoin.gameMode == -1) {
				InfoDialog.showError("the lobby is full already!", getStage());
			}
			else {
				System.err.println("CLIENT WANTS TO JOIN " + lobbyToJoin.name);
				lobbyMask.info.client = client;
				lobbyMask.info.joinLobby = true;
				lobbyMask.info.lobbyName = lobbyToJoin.name;
				lobbyMask.info.others = lobbyToJoin.others;
				lobbyMask.info.team = lobbyToJoin.team;
				lobbyMask.info.mode = GameMode.valueOf(lobbyToJoin.gameMode);
				switchTo(lobbyMask);
			}
		}
		super.act(delta);
	}


	
	private void buildLobbyTable() {
		lobbyTable.clear();
		for(String l : lobbys) {
			final TextButton b = new TextButton(l, getSkin(), "menu-list-button");
			b.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					String lobbyName = b.getText().toString();
					client.matchService.joinLobby(lobbyName);
				}
			});
			b.setColor(Color.GREEN);
			lobbyTable.add(b);
			lobbyTable.row();
		}
	}

	
	@Override
	public void enter() {
		client = info.client;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.trackingService.activate();
	
		if(PokemonGDX.CONFIGURATION == LaunchConfiguration.LOGGED_IN) {
			//info.client.sendLobbyJoinRequest("testLobby");
		}
	}
	
	@Override
	public void leave() {
		client = null;
		//info.client.removeChanelListener(listener);
		//userTracker.removeListener(userTrackerListener);
	}

	@Override
	public void afterInit() {
		lobbyMask = stage.lobby_mask;
		loginMask = stage.login_mask;
	}
	

}
