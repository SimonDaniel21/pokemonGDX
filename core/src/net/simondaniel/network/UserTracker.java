package net.simondaniel.network;

import java.util.List;

import net.simondaniel.network.client.GameClient;

public class UserTracker{

	private List<String> users;
	
	private GameClient trackedClient;
	
	private boolean active;
//	
//	public UserTracker(ChanelListenerList list) {
//		super(MessageChannel.userTrackChanel, false, list);
//		users = new ArrayList<String>();
//		listeners = new ArrayList<UserTrackerListener>();
//		active = false;
//	}
//	
//	public void startTracking(GameClient gc) {
//		if(active) return;
//		gc.addChanelListener(this);
//		
//		for(UserTrackerListener l : listeners)
//			l.reset();
//		
//		users.clear();
//		UserListC c = new UserListC();
//		gc.sendTCP(c);
//		active = true;
//	}
//	
//	public void stopTracking() {
//		if(!active) return;
//		trackedClient.removeChanelListener(this);
//	}
//
//	public void addListener(UserTrackerListener l) {
//		listeners.add(l);
//	}
//	
//	public void removeListener(UserTrackerListener l) {
//		listeners.remove(l);
//	}
//	
//	public List<String> getUsers(){
//		return users;
//	}
//	
//	public static interface UserTrackerListener{
//		public void userJoined(String name);
//		public void userLeft(String name);
//		public void reset();
//	}
//
//	@Override
//	public void channelReceive(Connection c, Object o) {
//		if(o instanceof PlayerListS) {
//			PlayerListS p = (PlayerListS)o;
//			for(UserJoinedS ujs : p.joined) {
//				users.add(ujs.user);
//				for(UserTrackerListener l : listeners) 
//					l.userJoined(ujs.user);
//			}
//		}
//		else if(o instanceof UserJoinedS) {
//			UserJoinedS p = (UserJoinedS)o;
//			users.add(p.user);
//			for(UserTrackerListener l : listeners) 
//				l.userJoined(p.user);
//		}
//		else if(o instanceof UserLeftS) {
//			UserLeftS p = (UserLeftS)o;
//			users.add(p.user);
//			for(UserTrackerListener l : listeners) 
//				l.userLeft(p.user);
//		}
//	}
}
