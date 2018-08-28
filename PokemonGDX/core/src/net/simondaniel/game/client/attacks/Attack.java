package net.simondaniel.game.client.attacks;

import net.simondaniel.entities.Entity;
import net.simondaniel.game.client.battle.BattleEntity;

public abstract class Attack extends BattleEntity{

	public Attack(int x, int y) {
		super(x,y);
	}
}
