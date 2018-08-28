package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.fabio.GameMode;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.MyListener;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.TeamJoinC;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.LobbyUserJoinedS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.StartGameS;
import net.simondaniel.network.server.Response.TeamJoinedS;
import net.simondaniel.screens.IngameScreen;

public class LobbyMask extends UImask<LobbyMaskInfo>{
	
	
	Label title;
	List<String> addable, undecided;
	TextButton joinUndecided;
	TeamWidget tw1, tw2;
	
	LobbyMaskListener listener;

	
	public LobbyMask(Skin s) {
		super(new LobbyMaskInfo(), s);
		listener = new LobbyMaskListener();
		title = new Label(null, s);
		add(title).colspan(2);
		row();
		
		addable = new List<String>(s);
		tw1 = new TeamWidget(s);
		tw2 = new TeamWidget(s);
		add(tw1);
		add(tw2);
		add(addable);
		row();
		undecided = new List<String>(s);
		undecided.setTouchable(Touchable.disabled);
		add(undecided).colspan(2).fillY().row();
		joinUndecided = new TextButton("join", s);
		joinUndecided.setTouchable(Touchable.disabled);
		joinUndecided.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
					TeamJoinC p = new TeamJoinC();
					p.teamID = 0;
					info.gc.sendTCP(p);
			}
			
		});
		add(joinUndecided).colspan(2);
		
		addable.setItems("other player1", "otherplayer2");
	}
	
	public void set(String lobbyName, String[][] others, GameMode mode, final GameClient gc, Skin skin) {
		
	}

	public void addPlayerToLobby(String playersName) {
//		for(Slot s : team.getItems()) {
//			if(!s.occupied) {
//				s.set(playersName);
//				System.out.println(playersName);
//				if(s.name.equals(gc.userName()))
//					tw1.list.setSelected(s);
//				return;
//			}
//		}
		undecided.getItems().add(playersName);
		undecided.setItems(undecided.getItems());
		if(playersName.equals(info.gc.userName())) {
			undecided.setSelected(playersName);
		}
	}
	
	/**
	 * adds player Strings to the Lobby if they arent null
	 * @param playersNames name of player, may be null
	 */
	public void addPlayersToLobby(String... playersNames) {
		for(String s : playersNames) {
			if(s != null)
				addPlayerToLobby(s);
		}
	}
	

	public void addPlayerToTeam(TeamWidget team, String playersName) {
		
		for(Slot s : team.getItems()) {
			if(!s.occupied) {
				s.set(playersName);
				System.out.println(playersName);
				if(s.name.equals(info.gc.userName()))
					tw1.list.setSelected(s);
				return;
			}
		}
		undecided.setItems(playersName);
		undecided.setSelected(info.gc.userName());
	}
	
	/**
	 * adds player Strings to the Lobby if they arent null
	 * @param playersNames name of player, may be null
	 */
	public void addPlayersToTeam(TeamWidget team, String... playersNames) {
		for(String s : playersNames) {
			if(s != null)
				addPlayerToTeam(team, s);
		}
	}
	
	public void joinTeam(String name, int id, int slotID) {
		tw1.removeName(name);
		tw2.removeName(name);
		undecided.getItems().removeValue(name, false);
		if(id == 0) {
			undecided.getItems().add(name);
		}
		else if(id == 1) {
			tw1.addName(name, slotID);
		}
		else if(id == 2) {
			tw2.addName(name, slotID);
		}
		
		if(name.equals(info.gc.userName())) {
			joinUndecided.setTouchable(Touchable.enabled);
			tw1.join.setTouchable(Touchable.enabled);
			tw2.join.setTouchable(Touchable.enabled);
			undecided.setSelectedIndex(-1);
			tw1.list.setSelectedIndex(-1);
			tw2.list.setSelectedIndex(-1);
			
			switch (id) {
			case 0:
				joinUndecided.setTouchable(Touchable.disabled);
				undecided.setSelected(name);
				break;
			case 1:
				tw1.join.setTouchable(Touchable.disabled);
				tw1.setSelectedSlot(name);
				break;
			case 2:
				tw2.join.setTouchable(Touchable.disabled);
				tw2.setSelectedSlot(name);
				break;
			default:
				break;
			}
		}
	}

	public static class TeamWidget extends Table{
	
		GameClient gc;
		
		int id;
		int size;
		String name;
		
		List<Slot> list;
		TextButton join;
		
		public TeamWidget(Skin skin) {
			super(skin);
			debug();
			list = new List<Slot>(skin);
			list.setColor(Color.GOLD);
			list.setTouchable(Touchable.disabled);
			join = new TextButton("join", skin);
			join.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					TeamJoinC p = new TeamJoinC();
					p.teamID = id;
					gc.sendTCP(p);
					System.out.println("join team " + id + "!");
				}
			});
			add(name).row();
			add(list).fillX().row();
			add(join);
		}
		
		public void set(String name, int size, final int id, final GameClient gc) {
			this.gc = gc;
			this.id = id;
			this.name = name;
			this.size = size;
			for(int i = 0; i < size; i++) {
				list.getItems().add(new Slot("Player " + i));
			}
			list.setItems(list.getItems());
		}
		
		public void setSelectedSlot(String name2) {
			for(Slot s : list.getItems()) {
				if(s.name.equals(name2)) {
					list.setSelected(s);
				}
			}
		}

		public void addName(String name, int slot) {
			list.getItems().get(slot).set(name);
		}
		
		public void removeName(String name) {
			for(Slot s : getItems()) {
				if(s.occupied && s.name.equals(name)) {
					s.empty();
				}
			}
		}

		public Array<Slot> getItems() {
			return list.getItems();
		}
	}
	
	public static class Slot{
		private boolean occupied = false;
		private String slotName, name = "";
		
		public Slot(String slotName) {
			this.slotName = slotName;
		}
		
		public void set(String playerName) {
			this.name = playerName;
			occupied = true;
		}
		
		@Override
		public String toString() {
			return occupied ? name : slotName;
		}
		
		public void empty() {
			this.name = "";
			occupied = false;
		}
	}
	
	@Override
	public void act(float delta) {
		info.gc.handlePacketBuffer();
		super.act(delta);
	}
	

	@Override
	public void enter() {
		
		title.setText(info.lobbyName + " (" +  info.mode + ")");
		
		final GameClient gc = info.gc;
		
		gc.addMyListener(listener);
		
		tw1.set("Team 1", info.mode.maxPlayersInTeam(0), 1,gc);
		tw2.set("Team 2",info. mode.maxPlayersInTeam(1), 2, gc);
	
	
		
		addable.setItems(info.inviteableUsers);
		addPlayersToLobby(info.others[0]);
		
		addPlayersToTeam(tw1, info.others[1]);
		addPlayersToTeam(tw2, info.others[2]);
		
		ready();
	}

	@Override
	public void leave() {
		info.gc.removeMyListener(listener);
	}
	
	
	class LobbyMaskListener implements MyListener{

		@Override
		public void received(Connection c, Object o) {
			if(o instanceof LobbyUserJoinedS) {
				//System.out.println("RECEIVED LOBBYJOIN " + info.gc.userName());
				LobbyUserJoinedS p = (LobbyUserJoinedS) o;
				addPlayerToLobby(p.name);
				
			}
			if(o instanceof TeamJoinedS) {
				TeamJoinedS p = (TeamJoinedS) o;
				if(p.id == Lobby.LOBBY_FULL) {
					InfoDialog.show("the team is already full", getStage());
				}
				else if(p.id == Lobby.NO_SUCH_USER) {
					InfoDialog.show("you are not member of the Lobby!", getStage());
				}
				else {
					joinTeam(p.name, p.id, p.slotID);
					//System.out.println("receivedd a TEAM " + p.id + "@" + p.slotID);
				}
			}
		}
	}
}
