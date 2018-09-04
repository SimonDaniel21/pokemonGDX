package net.simondaniel.pokes;

public enum PokemonType {

	grass("grass"),
	electric("electric"),
	psychic("psychic"),
	water("water"),
	fire("fire"),
	fly("flying"),
	dragon("dragon");
	
	public final String name;
	
	private PokemonType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static PokemonType fromString(String type) {
		for(PokemonType t : values()) {
			if(t.name().equals(type) || t.name.equals(type))
				return t;
		}
		return null;
	}
}
