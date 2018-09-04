package net.simondaniel.pokes;

public enum PokemonType {

	normal("normal"),
	fire("fire"),
	fighting("fighting"),
	water("water"),
	flying("flying"),
	grass("grass"),
	poison("poison"),
	electric("electric"),
	ground("ground"),
	psychic("psychic"),
	rock("rock"),
	ice("ice"),
	bug("bug"),
	dragon("dragon"),
	ghost("ghost"),
	dark("dark"),
	steel("steel");
	
	
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
