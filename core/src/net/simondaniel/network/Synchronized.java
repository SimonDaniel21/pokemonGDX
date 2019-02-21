package net.simondaniel.network;

public class Synchronized<T> {
	
	private T object;
	
	public synchronized void set(T o) {
		object = o;
	}
	
	public synchronized void reset() {
		object = null;
	}
	
	public synchronized boolean isReady() {
		return object != null;
	}
	
	public synchronized T consume() {
		T res = object;
		object = null;
		return res;
	}
}
