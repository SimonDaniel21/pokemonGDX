package net.simondaniel.network.chanels;

import java.util.List;

public class MessageChannelEnd {

	protected String name = "";	// to recognize
	protected Class<?>[] handledMessageTypes;
	
	public MessageChannelEnd(Class<?>[] handledMessageTypes) {
		this.handledMessageTypes = handledMessageTypes;
	}
	
	protected boolean handles(Object o) {
		for(Class<?> c : handledMessageTypes) {
			if(c.isInstance(o)) 
				return true;
		}

		return false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static class InvalidMessageTypeError extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2L;

		public InvalidMessageTypeError(String messageType, MessageChannel chanel){
			this(messageType, chanel, true);
		}
		
		public InvalidMessageTypeError(String messageType, MessageChannel chanel, boolean receive){
			super("a message of Type " + messageType + " should not be able to be " + (receive ? "received" : "sent") +  " in state: " + chanel);
		}
	}
	
	public static class MessageNotHandledError extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2L;

		public MessageNotHandledError(String messageType, List<Protocol> protocols){
			super("a message of Type " + messageType + " was not handled by any protocol: " + protocols);
		}
	}
	
	/**
	 * 
	 * @return all message Type classes that can be received
	 */
	public Class<?>[] receivableMessageTypes() {
		return handledMessageTypes;
	}
}
