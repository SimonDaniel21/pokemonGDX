package net.simondaniel.pokes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import net.simondaniel.game.client.gfx.AnimationLayout;

public class Pokemon {
	
	public static Pokemon[] pokemon;
	
	public static Pokemon 
			squirtle,
			pikachu,
			kyogre,
			charmander,
			rayquaza,
			bulbasaur,
			mewto,
			pidgey;
	
	public static void loadFromFile() {
		XmlReader r = new XmlReader();
		Element root = r.parse(Gdx.files.internal("Pokemons.xml"));
		
		pokemon = new Pokemon[root.getChildCount()];
		
		for(int i = 0; i < pokemon.length; i++) {
			Element e = root.getChild(i);
			String fieldName = e.getName();
			
			try {
				Pokemon p = new Pokemon(e);
				Pokemon.class.getField(fieldName).set(null, p);
				pokemon[i] = p;
				if(!p.isComplete())
					System.err.println("---------------------------------------------");
			
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			} catch (NoSuchFieldException ex) {
				ex.printStackTrace();
			} catch (SecurityException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	public final int ID; // PokedexNumber
	public final String name;
	public final PokemonType type1, type2;
	public float size;
	
	public int layout;
	
	
	public Pokemon(Element e) {
		ID = e.getInt("id");
		name = e.get("name");
		type1 = PokemonType.fromString(e.get("firstType"));
		if(e.hasChild("secondType"))
			type2 = PokemonType.fromString(e.get("secondType"));
		else
			type2 = null;
		
		layout = e.getInt("animationLayout");
		
	}
	
	public Pokemon(int id, String name, PokemonType t1, PokemonType t2) {
		this.ID = id;
		this.name = name;
		type1 = t1;
		type2 = t2;
	}
	
	public String getPreviewPicturePath() {
		return "gfx/pokemon/" + name + "/" + name + "_preview128.png";
	}
	
	public String getAtlasPath() {
		return "gfx/pokemon/" + name + "/" + name +  ".atlas";
	}
	
	/**
	 * checks if all neccessary values are set and all assets are loaded for a specific Pokemon(mainly debug purpose)
	 * @return
	 */
	public boolean isComplete() {
		boolean complete = true;
		if(ID <= 0 || ID > 1000) {
			complete = false;
		}
		
		if(name == null || name.equals("")) complete = false;
		
		if(type1 == null) complete = false;
		
		FileHandle fh = Gdx.files.internal(getPreviewPicturePath());
		
		if(!fh.exists()) {
			System.err.println("Pokemon (" + name + ") is missing a preview Picture");
			complete = false;
		}
		
		fh = Gdx.files.internal(getAtlasPath());
		
		if(!fh.exists()) {
			System.err.println("Pokemon (" + name + ") is missing a Atlas");
			complete = false;
		}
		
		
		return complete;
	}
}
