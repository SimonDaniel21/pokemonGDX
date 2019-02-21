package net.simondaniel.util;

import java.io.File;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileSystem {

	private static String LOCATION;
	
	static {
		LOCATION = ".sdeseus" + File.separator + "pokemonGDX";
		File dir = new File(LOCATION);
		if(!dir.exists()) {
			if(!dir.mkdirs()) {
				JOptionPane.showMessageDialog(null, "could not create the file System!", "Error",  JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		}
	}

	public static FileHandle loadFile(String path) {
		return Gdx.files.external(LOCATION + File.separator + path);
	}
}