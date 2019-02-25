package net.simondaniel.network.server.database;

import java.io.IOException;
import java.util.Collection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch.Config;

import net.simondaniel.util.FileSystem;

public class LocalFileDatabase extends Config implements DatabaseInerface{

	DataTable<String, UserProfileDO> userTable;
	DataTable<String, NotActivatedDO> notActivatedTable;
	
	FileHandle fh;
	
	public LocalFileDatabase(String path) {
		userTable = new DataTable<String, UserProfileDO>("users");
		notActivatedTable = new DataTable<String, NotActivatedDO>("notActivated");
		fh = FileSystem.loadFile(path);
		if(!fh.exists()) {
			save();
		}
		String data = fh.readString();
		data = data.replaceAll(System.lineSeparator(), "").replaceAll("\t", "");
		
		String[] tables = data.split(">");
		for(int i = 0; i < tables.length; i++) {
			String tableString = tables[i];
			String[] table = tables[i].split(":");
			String tableName = table[0];
			tableString = tableString.substring(tableName.length()+1, tableString.length());
			
			System.out.println("working with table " + tableName);
			System.out.println("and content " + tableString);
			
			String[] entries = tableString.split(";");
			
			for(String e : entries) {
				
				System.out.println("working with entry " + e);
			
				String[] sa = e.split(":");
				
				if(sa.length <= 0) continue;
				
				if(tableName.equals(userTable.name)){
					UserProfileDO o = new UserProfileDO();
					if(o.set(sa))
						userTable.add(o);
				}
				if(tableName.equals(notActivatedTable.name)){
					NotActivatedDO o = new NotActivatedDO();
					if(o.set(sa))
						notActivatedTable.add(o);
				}
			}
		}
		
		save();
	}
	
	@Override
	public void save() {
		fh.delete();
		try {
			fh.file().createNewFile();
			
			String line = "";
			
			line += saveTable(userTable) + System.lineSeparator();
			line += saveTable(notActivatedTable);
			fh.writeString(line, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private <K, T extends DataObject<K>> String saveTable(DataTable<K,T> table) {
		
		String res = "";
		int c = 0;
		res += table.name + ":" + System.lineSeparator();
		System.out.println("usertable size: " + userTable.values().size());
		for(T o : table.values()){
			c++;
			String[] fields = o.fieldsAsString();
			res += "\t";
			for(int i = 0; i < fields.length; i++) {
				res += fields[i] + ":";
				System.out.println("field[" + i + "] : "  + fields[i]);
			}
			res = res.substring(0, res.length()-1);
			System.out.println("c" + c);
			if(c < userTable.values().size())
				res += ";";
			
			res += System.lineSeparator();
		}
		res += ">";
		return res;
	}
	
	@Override
	public UserProfileDO loadUser(String uniqueName) {
		
		return userTable.get(uniqueName);
	}

	@Override
	public void addUser(UserProfileDO user) {
		
		userTable.add(user);
	}
	
	@Override
	public String checkValidation(UserProfileDO user) {
		
		if(userTable.contains(user.name)) return "name already exists";
		
		for(UserProfileDO updo : userTable.values()) {
			if(updo.email.equalsIgnoreCase(user.email))
				return "email already taken";
			
		}
		
		return "success";
	}

	@Override
	public void removeUser(String name) {
		userTable.remove(name);
	}


	@Override
	public NotActivatedDO loadNotActivatedName(String key) {
		return notActivatedTable.get(key);
	}

	@Override
	public void addNotActivatedName(NotActivatedDO o) {
		notActivatedTable.add(o);
	}
	
	@Override
	public void removeNotActivatedName(String uniqueName) {
		notActivatedTable.remove(uniqueName);
	}

	@Override
	public Collection<NotActivatedDO> loadNotActivatedNames() {
		return notActivatedTable.values();
	}
}
