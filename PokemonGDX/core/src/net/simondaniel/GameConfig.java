package net.simondaniel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import net.simondaniel.network.Download;

public class GameConfig extends Config{

	public int PORT;
	public boolean REGISTERED_ONCE;
	public String LAST_LOGIN_NAME;
	public String LAST_LOGIN_PASSWORD;
	public boolean KEEP_PASSWORD;
	public boolean FORCE_NEWEST_VERSION;
	public String DOWNLOAD_URL;
	public String FILENAME;
	public String  VERSION_URL;
	public String  CONFIG_URL;
	public String[] SERVERS;
	public String[] CUSTOM_SERVERS;
	
	
	public GameConfig() {
		super("config.txt", new String[]{
				"port",
				"registeredOnce",
				"last_loginName",
				"last_loginPassword",
				"keep_password",
				"force_newest_version",
				"download_URL",
				"filename",
				"version_url",
				"config_url",
				"standard_servers",
				"custom_servers"});
	}

	@Override
	protected void setGlobals() {
		int i = 0;
		PORT = readAsInteger(MUST_HAVE[i++]);
		REGISTERED_ONCE = readAsBool(MUST_HAVE[i++]);
		LAST_LOGIN_NAME = readAsString(MUST_HAVE[i++]);
		LAST_LOGIN_PASSWORD = readAsString(MUST_HAVE[i++]);
		KEEP_PASSWORD = readAsBool(MUST_HAVE[i++]);
		FORCE_NEWEST_VERSION = readAsBool(MUST_HAVE[i++]);
		DOWNLOAD_URL = readAsString(MUST_HAVE[i++]);
		FILENAME = readAsString(MUST_HAVE[i++]);
		VERSION_URL = readAsString(MUST_HAVE[i++]);
		CONFIG_URL = readAsString(MUST_HAVE[i++]);
		SERVERS = readAsStringArray(MUST_HAVE[i++], ",");
		CUSTOM_SERVERS = readAsStringArray(MUST_HAVE[i++], ",");
	}

	@Override
	protected void create(FileHandle f){
		try {
			Download d = new Download("https://dl.dropbox.com/s/kf2udk7tp3k41tq/stdConfig?dl=0", "temp");
			d.finish();
			f.mkdirs();
			Files.move(new File("temp").toPath(), f.file().toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
