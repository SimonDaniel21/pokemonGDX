package net.simondaniel.game.client.ui.masks;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.InviteList;
import net.simondaniel.game.client.ui.UImask;
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.network.client.Request.InviteUserToLobbyC;
import net.simondaniel.network.client.Request.LobbyLeaveC;
import net.simondaniel.network.client.Request.TeamJoinC;
import net.simondaniel.network.client.Request.UserReadyC;
import net.simondaniel.pokes.Pokemon;

public class LobbyMask extends UImask<LobbyMaskInfo>{
	
	PlayClient client;
	
	Label title, timer;
	List<String> undecided;
	InviteList inviteList;
	TextButton joinUndecided, readyButton;
	TeamWidget tw1, tw2;
	
	boolean timerStarted;
	long targetTime;

	private boolean ready = false;
	
	public LobbyMask(Skin s, final UImaskHandler stage) {
		super(new LobbyMaskInfo(), s, stage);
		s.getFont("font").getData().markupEnabled = true;
		s.getFont("medium").getData().markupEnabled = true;
		s.getFont("small").getData().markupEnabled = true;
		s.getFont("title").getData().markupEnabled = true;
		debug();
		//this.right();
		//SuserTrackerListener = new UserListener();
		title = new Label(null, s);
		add(title).colspan(2).center();
		row();
		
		inviteList = new InviteList(s);
		inviteList.getColor().a = 0.5f;
		tw1 = new TeamWidget(s);
		tw2 = new TeamWidget(s);
		add(tw1).fillX();
		add(tw2).fillX();
		add(inviteList).fillX();
		row();
		undecided = new List<String>(s);
		undecided.setTouchable(Touchable.disabled);
		add(undecided).colspan(2).fillY();
		
		TextButton inviteButton = new TextButton("invite", s,  "small");
		inviteButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!isActive()) return;
				
				if(inviteList.isPending()) {
					beep();
					return;
				}
				
				InviteUserToLobbyC p = new InviteUserToLobbyC();
				p.user = inviteList.getSelectedName();
				p.lobby = info.lobbyName;
				//info.gc.send(p);
			}
		});
		add(inviteButton).row();
		joinUndecided = new TextButton("join", s);
		joinUndecided.setTouchable(Touchable.disabled);
		joinUndecided.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
					if(!isActive())return;
				
					TeamJoinC p = new TeamJoinC();
					p.teamID = 0;
					//info.gc.sendTCP(p);
					deactivateUntilResponse();
			}
			
		});
		add(joinUndecided).colspan(2);
		
		readyButton = new TextButton("", s);
		
		final LobbyMask ref = this;
		
		readyButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!isActive()) return;
				for(String s : undecided.getItems()) {
//					if(s.equals(info.gc.userName())){
//						beep();
//						return;
//					}
				}
				
				PokemonChooseMaskInfo info = new PokemonChooseMaskInfo();
				info.lobbyMask = ref;
				//info.gc = ref.info.client;
				PokemonChooseMask m = stage.pokemon_choose_mask;
				switchTo(m);
			}
			
		});
		updateReadyButton();
		add(readyButton).width(210).row();
		add().colspan(2);
		timer = new Label("", s);
		add(timer).row();
		TextButton leave = new TextButton("leave", s);
		leave.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!isActive())return;
				LobbyLeaveC p = new LobbyLeaveC();
				//info.gc.send(p);
				deactivateUntilResponse();
			}
		});
		add(leave);
		
	}
	
	public void setPokemon(Pokemon p) {
		//timer.setText(p.name);
	}
	
	private void updateReadyButton() {
		if(ready) {
			readyButton.setText("ready");
			readyButton.setColor(Color.GREEN);
		}
		else {
			readyButton.setText("not ready");
			readyButton.setColor(Color.RED);
		}
		
	}
	
	public void addPlayerToLobby(String playersName) {

		undecided.getItems().add(playersName);
		
		if(playersName.equals("TODO")) {
			undecided.setSelected(playersName);
		}
		undecided.setItems(undecided.getItems());
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
	
	/**
	 * removes player Strings from the Lobby if they arent null
	 * @param playersNames name of player, may be null
	 */
	public void removePlayerFromLobby(String name) {
		undecided.getItems().removeValue(name, false);
		undecided.setItems(undecided.getItems());
		tw1.removeName(name);
		tw2.removeName(name);
	}

	public void addPlayerToTeam(TeamWidget team, String playersName) {
		
		for(Slot s : team.getItems()) {
			if(!s.occupied) {
				s.set(playersName);
				System.out.println(playersName);
//				if(s.name.equals(info.client.userName()))
//					tw1.list.setSelected(s);
				return;
			}
		}
		undecided.setItems(playersName);
		//undecided.setSelected(info.gc.userName());
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
		
		if(name.equals("TODO")) {
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

	public void startTimer() {
		
	}
	
	public class TeamWidget extends Table{
	
		int id;
		int size;
		String name;

		List<Slot> list;
		TextButton join;
		
		public TeamWidget(Skin skin) {
			super(skin);
			list = new List<Slot>(skin);
			list.setColor(Color.GOLD);
			list.setTouchable(Touchable.disabled);
			join = new TextButton("join", skin);
			join.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					
					if(!isActive())return;
					
					
					client.matchService.joinTeam(id);
					System.out.println("join team " + id + "!");
					
					deactivateUntilResponse();
				}
			});
			add(name).row();
			add(list).fillX().row();
			add(join);
		}
		
		public void set(String name, int size, final int id) {
			
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
			list.setItems(list.getItems());
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
		
		long timeLeft = (targetTime - System.currentTimeMillis() + 999) / 1000;
		if(timerStarted)
			timer.setText(timeLeft + "");
		super.act(delta);
	}
	

	@Override
	public void enter() {
		client = info.client;
		
		//final GameClient gc = info.gc;
		//listener = new LobbyMaskListener(info.gc.myListeners);
		//gc.addChanelListener(listener);
		//info.userTracker.addListener(userTrackerListener);
		
		if(!info.joinLobby) {
//			for(String s : undecided.getItems()) {
//				if(s.equals(info.gc.userName())){
//					beep();
//					return;
//				}
//			}
			
			UserReadyC p = new UserReadyC();
			p.ready = true;
			//info.gc.send(p);
			deactivateUntilResponse();
			return;
		}
		
		timerStarted = false;
		
		title.setText(info.lobbyName + " (" +  info.mode + ")");
		
		//tw1.set("Team 1", info.mode.maxPlayersInTeam(0), 1,gc);
		//tw2.set("Team 2",info. mode.maxPlayersInTeam(1), 2, gc);
	
//		for(String s : info.userTracker.getUsers()) {
//			if(!s.equals(info.gc.userName()))
//				inviteList.addName(s);
//		}
//		
		addPlayersToLobby(info.others[0]);
		
		for(int i = 0; i < info.others.length; i++) {
			if(info.team[i] == 0)
				addPlayerToTeam(tw1, info.others[i]);
			if(info.team[i] == 1)
				addPlayerToTeam(tw2, info.others[i]);
				
		}
	}

	@Override
	public void leave() {
		//info.gc.removeChanelListener(listener);
		//info.userTracker.removeListener(userTrackerListener);
	}

	@Override
	public void afterInit() {
		// TODO Auto-generated method stub
		
	}
	
	
//	private class LobbyMaskListener extends ChanelListener{
//
//		public LobbyMaskListener(ChanelListenerList list) {
//			super(MessageChannel.lobbyState, false, list);
//		}
//
//
//		@Override
//		protected void channelReceive(Connection c, Object o) {
//			if(o instanceof LobbyUserJoinedS) {
//				//System.out.println("RECEIVED LOBBYJOIN " + info.gc.userName());
//				LobbyUserJoinedS p = (LobbyUserJoinedS) o;
//				addPlayerToLobby(p.name);
//			}
//			else if(o instanceof LobbyUserLeftS) {
//				LobbyUserLeftS p = (LobbyUserLeftS) o;
//				if(p.name.equals(info.gc.userName())) {
//					reActivateUI();
//					goBack();
//				}
//				else {
//					removePlayerFromLobby(p.name);
//				}
//			}
//			
//			if(o instanceof TeamJoinedS) {
//				TeamJoinedS p = (TeamJoinedS) o;
//				if(p.id == Lobby.LOBBY_FULL) {
//					InfoDialog.show("the team is already full", getStage());
//				}
//				else if(p.id == Lobby.NO_SUCH_USER) {
//					InfoDialog.show("you are not member of the Lobby!", getStage());
//				}
//				else {
//					joinTeam(p.name, p.id, p.slotID);
//					//System.out.println("receivedd a TEAM " + p.id + "@" + p.slotID);
//				}
//				reActivateUI();
//			}
//			if(o instanceof InviteUserToLobbyS) {
//				InviteUserToLobbyS p = (InviteUserToLobbyS) o;
//				inviteList.setPending(p.name);
//			}
//			if(o instanceof InviteAnswerS) {
//				InviteAnswerS p = (InviteAnswerS) o;
//				inviteList.reply(p.name, p.answer);
//			}
//			if(o instanceof UserReadyS) {
//				UserReadyS p = (UserReadyS) o;
//				if(p.user.equals(info.gc.userName())){
//					ready = p.ready;
//					updateReadyButton();
//					reActivateUI();
//				}
//				if(p.ready == false) {
//					timerStarted = false;
//					timer.setText("");
//				}
//			}
//			if(o instanceof LobbyStartTimerS) {
//				LobbyStartTimerS p = (LobbyStartTimerS) o;
//				targetTime = p.start;
//				timerStarted = true;
//				System.err.println("START TIMER");
//			}
//			if(o instanceof StartGameS) {
//				StartGameS p = (StartGameS) o;
//				PokemonGDX.game.setScreen(new IngameScreen(info.gc));
//			}
//		}
//	}
//	
//	private class UserListener implements UserTrackerListener{
//
//		@Override
//		public void userJoined(String name) {
//			inviteList.addName(name);
//		}
//
//		@Override
//		public void userLeft(String name) {
//			inviteList.removeName(name);
//			removePlayerFromLobby(name);	
//		}
//
//		@Override
//		public void reset() {
//			// TODO Auto-generated method stub
//			
//		}
//	}
}
