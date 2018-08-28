package net.simondaniel.control;

import net.simondaniel.entities.Entity;

public abstract class ClientServerController<T extends Control> extends Controller<T>{

	private boolean isServer;
	
	public ClientServerController(T clientSend_ifc, T server_ifc, T clientReceive_ifc, boolean isServer, Entity e) {
		super(isServer ? server_ifc : clientSend_ifc);
		this.clientSend_ifc = clientSend_ifc;
		this.clientReceive_ifc = clientReceive_ifc;
		this.server_ifc = server_ifc;
		this.isServer = isServer;
	}

	public final T clientSend_ifc, clientReceive_ifc;
	public final T server_ifc;

	public T getControlIFC(boolean isServer) {
		if(isServer) {
			return server_ifc;
		}
		return clientReceive_ifc;
	}
	
	@Override
	public final void control() {
		if(isServer)
			serverControl();
		else
			clientControl();
	}
	
	protected abstract void serverControl();
	protected abstract void clientControl();
	
	
}
