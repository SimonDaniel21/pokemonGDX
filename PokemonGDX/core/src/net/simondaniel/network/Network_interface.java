package net.simondaniel.network;

import net.simondaniel.network.chanels.MessageChannel;

public interface Network_interface {

	public void send(MessageChannel mc, Object o);
}
