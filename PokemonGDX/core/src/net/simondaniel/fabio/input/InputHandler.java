package net.simondaniel.fabio.input;

import com.badlogic.gdx.InputProcessor;

public interface InputHandler<T extends InputProcessor> {
	
	public void handle(T input);
}
