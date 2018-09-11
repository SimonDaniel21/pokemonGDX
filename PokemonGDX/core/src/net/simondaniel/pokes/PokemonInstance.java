package net.simondaniel.pokes;

public class PokemonInstance {
	
	private Pokemon pokemon;
	int hp;
	public float size;
	
	public PokemonInstance(PokeDef pokedef) {
		if(!pokedef.isComplete()) {
			System.err.println("INCOMPLETE POKEDEF!");
			return;
		}
		size = pokedef.pokemon.size;
	}
	
	public Pokemon getPokemon() {
		return pokemon;
	}
}
