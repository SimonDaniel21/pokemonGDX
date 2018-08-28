package net.simondaniel.network.server.database;

import java.util.Collection;
import java.util.HashMap;

public class DataTable<K, T extends DataObject<K>> {
	
	public final String name;
	
	private HashMap<K, T> data;
	
	public DataTable(String tableName) {
		data = new HashMap<K, T>();
		this.name = tableName;
	}
	
	public T get(K key) {
		return data.get(key);
	}
	
	/**
	 * adds a object to the table if it doesnt already contains its key and the object is not null
	 * @param object
	 * @return
	 */
	public boolean add(T object) {
		if(object == null || data.containsKey(object.key())) return false;
			
		data.put(object.key(), object);
		return true;
	}
	
	/**
	 * removes a object to the table if it contains a entry with the given key and the key is not null
	 * @param key
	 * @return wether the object was removed
	 */
	public boolean remove(K key) {
		
		T old = data.remove(key);
		
		return (old != null);
	}
	
	
	public boolean contains(K key) {
	
		return data.containsKey(key);
	}

	public Collection<T> values() {
		return data.values();
	}
}
