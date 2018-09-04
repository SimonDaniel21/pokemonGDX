package net.simondaniel.pokes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Pokemon {
	
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
		for(int i = 0; i < root.getChildCount(); i++) {
			Element e = root.getChild(i);
			String fieldName = e.getName();
			
			try {
				Pokemon p = new Pokemon(e);
				Pokemon.class.getField(fieldName).set(null, p);
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
	
	
	public Pokemon(Element e) {
		ID = e.getInt("id");
		name = e.get("name");
		type1 = PokemonType.fromString(e.get("firstType"));
		if(e.hasChild("secondType"))
			type2 = PokemonType.fromString(e.get("secondType"));
		else
			type2 = null;
	}
	
	public Pokemon(int id, String name, PokemonType t1, PokemonType t2) {
		this.ID = id;
		this.name = name;
		type1 = t1;
		type2 = t2;
	}
}
