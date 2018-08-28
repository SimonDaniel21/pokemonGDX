package net.simondaniel.pokes;

public class PokeDef {

	public Pokemon pokemon;
	public int level;
	public int exp;
	public PokemonStats stats;
	
	public boolean isComplete() {
		return pokemon != null && stats != null;
	}
}
