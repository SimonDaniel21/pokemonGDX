package net.simondaniel.network.chanels;

import net.simondaniel.network.server.GameServer;

public class MessageChannel {

	protected static GameServer gs;
	
	private static short idCounter = 1;
	public final short ID;
	
	private MessageChannelEnd receiver, sender;
	private String name;
	
	public MessageChannel(String chanelName, MessageChannelEnd sender, MessageChannelEnd receiver) {
		this.receiver = receiver;
		this.receiver.setName(chanelName + "-receiver"); 
		this.sender = sender;
		this.sender.setName(chanelName + "-sender"); 
		this.name = chanelName;
		ID = idCounter;
		idCounter++;
	}
	
	public static void setServerRef(GameServer gs) {
		MessageChannel.gs = gs;
	}
	
	public MessageChannelEnd sender() {
		return sender;
	}
	public MessageChannelEnd receiver() {
		return receiver;
	}
	
	public static MessageChannel initialChannel;
	
	public static MessageChannel loggedInState;
	
	public static MessageChannel lobbyState;
	
	public static MessageChannel userTrackChanel;
	
	public static MessageChannel gameMenuState;
	
	public static void initCommunication(boolean isServer) {
		
		initialChannel = new InitialConnectionChanel();
		loggedInState = new LoggedInChanel();
		lobbyState = new LobbyChanel();
		userTrackChanel = new UserTrackChanel();
		gameMenuState = new MessageChannel("gameMenu",
				new MessageChannelEnd(new Class<?>[] {}), 
				new MessageChannelEnd(new Class<?>[] {}));
	}

	public String getName() {
		return name;
	}

	/**
	 * @param o Message object
	 * @return weather the current Receiver (client or server) should be able to receive a MessageType through this chanel
	 */
	public boolean canReceive(Object o) {
		
		return receiver.handles(o);
	}
	
	/**
	 * @param o Message object
	 * @return weather the Receiving side (client (if server is Sender) or server(if client is Sender)) 
	 * should be able to receive a MessageType through this chanel
	 */
	public boolean canSend(Object o) {

		return sender.handles(o);
	}

	@Override
	public String toString() {
		return name;
	}
}
