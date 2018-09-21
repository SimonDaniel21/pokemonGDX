package net.simondaniel.network.client;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ChanelListenerList extends ArrayList<ChanelListener>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	ReentrantLock activeLock = new ReentrantLock();
	
	public ChanelListener active;
	
	public void startReceive(ChanelListener l) {
		activeLock.lock();
		active = l;
	}
	
	public void stopReceive() {
		active = null;
		activeLock.unlock();
	}
	
	@Override
	public boolean add(ChanelListener e) {
		activeLock.lock();
		try {
			return super.add(e);
		}
		finally {
			activeLock.unlock();
		}
	}
	
	@Override
	public boolean remove(Object o) {
		activeLock.lock();
		try {
			return super.remove(o);
		}
		finally {
			activeLock.unlock();
		}
	}
}
