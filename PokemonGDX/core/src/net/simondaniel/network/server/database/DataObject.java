package net.simondaniel.network.server.database;

/**
 *
 * @author simon
 *
 *modelliert eine Art Datenbank Table mit einem Primarykey vom typ T
 *
 * @param <T>
 */
public interface DataObject<T> {
	
	public T key();
	
	/**
	 * String array red from file
	 * @param sa
	 */
	public boolean set(String[] sa);
	
	/**
	 * String array red from file
	 * @param sa
	 */
	public String[] fieldsAsString();
}
