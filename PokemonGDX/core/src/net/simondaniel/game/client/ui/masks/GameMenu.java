package net.simondaniel.game.client.ui.masks;

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
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.NamingDialog;
import net.simondaniel.game.client.ui.NamingDialog.Entry;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.MyListener;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.LobbyListC;
import net.simondaniel.network.server.Response.EndConnectionS;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.StartGameS;
import net.simondaniel.screens.IngameScreen;

public class GameMenu extends UImask<LoginMaskInfo>{

	NamingDialog nda;
	Table lobbyTable;
	Friendlist fl;
	String[] otherPlayers;
	LobbyMask lobbyMask;
	
	MyListener listener;
	
	public GameMenu(final Skin skin) {
		super(new LoginMaskInfo(), skin);
		
		listener = new GameMenuListener();
		
		lobbyMask  = new LobbyMask(skin);
		
		lobbyTable = new Table(skin);
		final List<String> list = new List<String>(skin);
		list.setItems(new String[]{"1v1", "2v2", "3v3"});
		add(lobbyTable);
		
		final NamingDialog nda = new NamingDialog("enter Name", skin, new Entry("new lobby")) {
			
			@Override
			public void updateValues(String[] values) {
				
				info.client.sendLobbyCreateRequest(values[0], GameMode.ONE_VS_ONE);
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
		Inbox ib = new Inbox(skin);
		this.add(ib).size(270,350).left().bottom();
		add(lobbyTable).expandX();
		fl = new Friendlist(skin);
		this.add(fl).size(300, 300).right().bottom();
		
		
	}
	
	public void enterLobby(String name,GameMode mode, String[][] others) {
	
		LobbyMaskInfo info = lobbyMask.getInfo();
		info.gc = getInfo().client;
		info.lobbyName = name;
		info.mode = mode;
		info.others = others;
		info.inviteableUsers = otherPlayers;
		switchTo(lobbyMask);
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
	
	class GameMenuListener implements MyListener{

		@Override
		public void received(Connection c, Object o) {
			if(o instanceof EndConnectionS) {
				EndConnectionS p = (EndConnectionS) o;
				InfoDialog.show(p.reason, getStage());
			}
			if(o instanceof LobbyJoinS) {
				LobbyJoinS p = (LobbyJoinS)o;
				if(p.gameMode != -1) {
					enterLobby(p.name, GameMode.valueOf(p.gameMode), p.others);
				}
				else {
					InfoDialog.show("lobby is full already", getStage());
				}
			}
			if(o instanceof MessageS) {
				MessageS s = (MessageS)o;
				if(s.sender.equals("server")) {
					InfoDialog.show(s.message, getStage());
				}
			}
			if(o instanceof LobbyListS) {
				System.out.println("RECEIVED LOBBY LIST");
				LobbyListS p = (LobbyListS)o;
				lobbyTable.clear();
				lobbyTable.add("public Lobbys: ").row();
				for(String l : p.lobbys) {
					final TextButton b = new TextButton(l, getSkin());
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
				otherPlayers = p.players;
				System.err.println("setting fl!");
				fl.set(p.players);
			}
			if(o instanceof StartGameS) {
				System.err.println("received Startgame");
				PokemonGDX.game.setScreen(new IngameScreen(info.client));
				getStage().dispose();
			}
		
		}
		
	}
}
