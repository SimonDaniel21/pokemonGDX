package net.simondaniel.util;

import java.util.ArrayList;

public class SyncableList<T>{

	ArrayList<T> added, removed;
	
	public SyncableList() {
		added = new ArrayList<T>();
		removed = new ArrayList<T>();
	}
	
	public void add(T item) {
		synchronized (added) {
			added.add(item);
		}
	}
	
	public void remove(T item) {
		synchronized (removed) {
			removed.add(item);
		}
	}
	
	public ArrayList<T> getAdded(){
		return added;
	}
	
	public ArrayList<T> getRemoved(){
		return removed;
	}
	
	public boolean sync(ArrayList<T> toBeSynced) {
		boolean add = !added.isEmpty();
		boolean rem = ! removed.isEmpty();
		
		
		if(add) {
			synchronized (added) {
				for(T i : added) {
					toBeSynced.add(i);
				}
				added.clear();
			}
		}
		
		if(rem) {
			synchronized (removed) {
				for(T i : removed) {
					toBeSynced.remove(i);
				}
				removed.clear();
			}
		}
		return add || rem;
	}
	
	public boolean isUpToDate() {
		boolean add, rem;
		synchronized (added) {
			add = !added.isEmpty();
		}
		synchronized (removed) {
			rem = !removed.isEmpty();
		}
		return add || rem;
	}
}
