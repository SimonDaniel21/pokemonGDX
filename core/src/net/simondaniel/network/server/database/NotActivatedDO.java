package net.simondaniel.network.server.database;

public class NotActivatedDO implements DataObject<String>{

	public String name, code;

	@Override
	public String key() {
		return name;
	}

	@Override
	public boolean set(String[] sa) {
		if(sa.length != 2) return false;
		for(int i = 0; i < sa.length; i++) {
			if(sa[i] == null) return false;
		}
		name = sa[0];
		code = sa[1];
		return true;
	}

	@Override
	public String[] fieldsAsString() {
		return new String[] {name, code};
	}
	
	
}
