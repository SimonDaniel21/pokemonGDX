package net.simondaniel.network.server.database;

public class UserProfileDO implements DataObject<String>{

	public String name, password, email;

	@Override
	public String key() {
		return name;
	}

	@Override
	public boolean set(String[] sa) {
		if(sa.length != 3) return false;
		for(int i = 0; i < sa.length; i++) {
			if(sa[i] == null) return false;
		}
		name = sa[0];
		password = sa[1];
		email = sa[2];
		return true;
		
	}


	@Override
	public String[] fieldsAsString() {
		
		return new String[] {name, password, email};
	}

}
