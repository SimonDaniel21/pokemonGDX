package net.simondaniel.game.client.attacks;

import net.simondaniel.entities.Entity;

public abstract class Attack{
	int x,  y;

	public Attack(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
