package editor;

public class PokemonField<T> {

	private String name;
	private T value;
	
	public PokemonField(String name, T v) {
		this.value = v;
		this.name = name;
	}
	
	public void set(T v) {
		this.value = v;
	}

	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		if(value == null) return "null";
		return value.toString();
	}
}
