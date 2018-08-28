package net.simondaniel.pokes;

public class Pokemon {
	
	public static final Pokemon 
			squirtle = new Pokemon(0),
			pikachu = new Pokemon(1);
	static {
		squirtle.setType(PokemonType.water);
		squirtle.size = 1;
		
		pikachu.setType(PokemonType.electro);
		pikachu.size = 0.5f;
	}
	
	
	public final int ID; // PokedexNumber
	public PokemonType type1, type2;
	public float size;
	
	public Pokemon(int id) {
		this.ID = id;
	}
	
	private void setType(PokemonType type1) {
		setType(type1, null);
	}
	private void setType(PokemonType type1, PokemonType type2) {
		this.type1 = type1;
		this.type2 = type2;
	}
}
