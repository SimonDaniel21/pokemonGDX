package net.simondaniel;

import java.io.IOException;
import java.util.HashMap;
import com.badlogic.gdx.files.FileHandle;

import net.simondaniel.util.FileSystem;

public abstract class Config {

	public static GameConfig gameConfig;
	
	private HashMap<String, String> variables;
	protected final String[] MUST_HAVE;
	
	private FileHandle fh;
	
	public static void init(){
		gameConfig = new GameConfig();
	}
	
	public Config(String path, String[] mustHave){
		this.MUST_HAVE= mustHave;
		load(path);
	}
	
	public void load(String path) {
		variables = new HashMap<String, String>();

		fh = FileSystem.loadFile(path);
		
		if(!fh.exists()){
			create(fh);
		}
		
		String content[] = fh.readString().split( "\n");
		System.out.println("data: " + fh.readString()); 
		System.out.println("len " + content.length); 
		for(String s : content) {
			s = s.replace("\r", "");
			//System.out.println("line: " + s); 
			readLine(s);
		}
		
		if(!isComplete()) {
			System.err.println("config file is missing something...");
			complete();
		}
		setGlobals();
	}
	
	protected void create(FileHandle f){
		try {
			f.file().createNewFile();
			for(String s : MUST_HAVE){
				fh.writeString(s + " = null" + System.getProperty("line.separator"), true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected final String readAsString(String key){
		return variables.get(key);
	}
	
	protected final String[] readAsStringArray(String key , String regex){
		String v =  variables.get(key);
		if(v.equalsIgnoreCase("null")) {
			return new String[0];
		}
		return v.split(regex);
	}
	
	protected final int readAsInteger(String key){
		return Integer.valueOf(variables.get(key));
	}
	
	protected final boolean readAsBool(String key){
		return (variables.get(key).equals("true"));
	}
	
	protected abstract void setGlobals();
	
	private boolean isComplete() {
		for(String s : MUST_HAVE) {
			boolean found = false;
			for(String ss : variables.keySet()) {
				if(s.equalsIgnoreCase(ss)) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}
	
	private void complete(){
		for(String s : MUST_HAVE){
			if(!variables.containsKey(s)) variables.put(s, "null");
		}
	}
	
	
	/**
	 * writes the config to the persistent file
	 */
	public void save(){
		fh.delete();
		try {
			fh.file().createNewFile();
			
			for(String s : MUST_HAVE){
				fh.writeString(s + " = " + variables.get(s) +  System.getProperty("line.separator"), true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void writeAsString(String key, String value) {
		if(variables.containsKey(key)) {
			variables.put(key, value);
		}
	}
	
	public void writeAppendToStringArray(String key, String value, String regex) {
		if(variables.containsKey(key)) {
			String oldText = variables.get(key);
			
			if(oldText.equals("null")) oldText = "";
			else oldText = oldText + regex;
			
			variables.put(key, oldText + value);
		}
	}
	
	public void writeAsBool(String key, boolean value) {
		writeAsString(key, value ?  "true" : "false");
	}
	
	private void readLine(String line) {
		String[] s = line.replaceAll(" ", "").split("=", 2);
		if(s.length != 2) {
			System.err.println("corrupted config file!" + s.length);
			System.exit(-1);
		}
		String left = s[0];
		String right = s[1];
		variables.put(left, right);
	}
}