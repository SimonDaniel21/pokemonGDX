package editor;

import java.util.ArrayList;
import java.util.List;

import net.simondaniel.pokes.PokemonType;

public class PokemonData {

	public static final int FIELD_COUNT = 3;
	
	List<PokemonField<?>> data = new ArrayList<PokemonField<?>>();
	PokemonField<String> nameField;
	PokemonField<Integer> pokedexField;
	PokemonField<PokemonType> typ1Field;
	PokemonField<PokemonType> typ2Field;
	
	public PokemonData() {
		nameField = new PokemonField<String>("name", "");
		pokedexField = new PokemonField<Integer>("id", -1);
		typ1Field = new PokemonField<PokemonType>("typ1", null);
		typ2Field = new PokemonField<PokemonType>("typ2", null);
	}
	
	public int fieldCount(){
		return data.size();
	}
	
	public boolean isValid() {
		if(nameField.getValue() == null || nameField.getValue().equals("")) return false;
		
		int id = pokedexField.getValue();
		if(id <= 0 || id > 1000) return false;
		
		if(typ1Field.getValue() == null) return false;
		return true;
	}
}
