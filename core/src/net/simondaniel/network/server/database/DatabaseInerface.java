package net.simondaniel.network.server.database;

import java.util.Collection;

public interface DatabaseInerface {

	public UserProfileDO loadUser(String uniqueName);
	public void addUser(UserProfileDO user);
	public String checkValidation(UserProfileDO user);
	public void removeUser(String uniqueName);
	
	public NotActivatedDO loadNotActivatedName(String name);
	public void addNotActivatedName(NotActivatedDO name);
	public void removeNotActivatedName(String name);
	public Collection<NotActivatedDO> loadNotActivatedNames();
	
	/**
	 * saves all changes to the DatabaseInterface
	 */
	public void save();
	
	
}
