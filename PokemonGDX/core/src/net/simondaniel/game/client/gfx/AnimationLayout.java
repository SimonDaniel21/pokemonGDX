package net.simondaniel.game.client.gfx;

import java.util.HashMap;

import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;

/**
 * legt Fest welche eine Animationen ein Pokemon besitzt und in welcher Weise sie eingesetzt werden
 * @author simon
 *
 */
public class AnimationLayout {
	
	public final SpecialAnimationType sat;
	public final AttackAnimationType at;
	public final MovementAnimationType mt;
	
	public HashMap<AnimationType, String> mapping;
	
	public AnimationLayout(SpecialAnimationType sat, AttackAnimationType at, MovementAnimationType mt) {
		this(sat, at, mt, 0);
	}
	
	public AnimationLayout(SpecialAnimationType sat, AttackAnimationType at, MovementAnimationType mt, int others) {
		this.sat = sat;
		this.at = at;
		this.mt = mt;
		IdleAnimationType idle = IdleAnimationType.NORMAL;
		
		mapping = new HashMap<AnimationType, String>();
		
		switch (sat) {
		case ONE_SPECIAL:
			mapping.put(AnimationType.SPECIAL_ATTACK, "special");
			mapping.put(AnimationType.SPECIAL_ATTACK2, "special");
			break;

		case TWO_SPECIALS:
			mapping.put(AnimationType.SPECIAL_ATTACK, "special1");
			mapping.put(AnimationType.SPECIAL_ATTACK, "special2");
			break;
		}
		
		switch (at) {
		case NORMAL:
			mapping.put(AnimationType.ATTACK, "attack");
			break;

		case SHARED_WITH_IDLE:
			idle = idle.shareWithAttack();
			break;
		}
		
		switch (mt) {
		case NORMAL:
			mapping.put(AnimationType.MOVEMENT, "movement");
			break;

		case SHARED_WITH_IDLE:
			idle = idle.shareWithMovement();
			break;
		}
		
		switch (idle) {
		case NORMAL:
			mapping.put(AnimationType.IDLE, "idle");
			break;

		case SHARED_WITH_ATTACK:
			mapping.put(AnimationType.IDLE, "idle_attack");
			mapping.put(AnimationType.ATTACK, "idle_attack");
			break;
			
		case SHARED_WITH_MOVEMENT:
			mapping.put(AnimationType.IDLE, "idle_movement");
			mapping.put(AnimationType.MOVEMENT, "idle_movement");
			break;
		
		case SHARED_WITH_MOVEMENT_ATTACK:
			mapping.put(AnimationType.IDLE, "idle_movement_attack");
			mapping.put(AnimationType.MOVEMENT, "idle_movement_attack");
			mapping.put(AnimationType.ATTACK, "idle_movement_attack");
			break;
		}
		
		mapping.put(AnimationType.ASLEEP, "asleep");
		mapping.put(AnimationType.HURT, "hurt");
	}
	
	
	public String getAnimationName(AnimationType type, AnimationDirection dir) throws AnimationNotSupportedException {

		String name = mapping.get(type);
		for(AnimationType t : AnimationType.values()) {
			System.out.println(t.name + " - " + mapping.get(t));
		}
		
		if(name != null) {

			System.out.println("at: " + at.name());
			System.out.println("tm: " + mt.name());
			if(dir != AnimationDirection.NO_DIRECTION) {
				name += "_" + dir.name;
			}

			System.out.println("searching for: " + name);
			return name;
		}
		else {
			throw new AnimationNotSupportedException(type, dir);
		}
	}
	
	public static AnimationLayout getLayoutFromIndex(int i) {
		if(i < 0 || i >= PokemonAnimationLayout.values().length) return null;
		
		return PokemonAnimationLayout.values()[i].LAYOUT;
	}
	
	public static enum PokemonAnimationLayout{
		pikachu(new AnimationLayout(SpecialAnimationType.ONE_SPECIAL, AttackAnimationType.SHARED_WITH_IDLE, MovementAnimationType.NORMAL)),
		squirtle(new AnimationLayout(SpecialAnimationType.TWO_SPECIALS, AttackAnimationType.SHARED_WITH_IDLE, MovementAnimationType.SHARED_WITH_IDLE)), 
		rayquaza(new AnimationLayout(SpecialAnimationType.ONE_SPECIAL, AttackAnimationType.NORMAL, MovementAnimationType.SHARED_WITH_IDLE));
		
		public final AnimationLayout LAYOUT;
		
		private PokemonAnimationLayout(AnimationLayout l) {
			LAYOUT = l;
		}
	}
	
	public static enum MovementAnimationType{
		NORMAL,
		SHARED_WITH_IDLE;
	}
	
	public static enum SpecialAnimationType{
		ONE_SPECIAL,
		TWO_SPECIALS;
	}
	
	public static enum AttackAnimationType{
		NORMAL,
		SHARED_WITH_IDLE;
	}
	
	public static enum IdleAnimationType{
		NORMAL,
		SHARED_WITH_MOVEMENT,
		SHARED_WITH_ATTACK,
		SHARED_WITH_MOVEMENT_ATTACK;
		
		public IdleAnimationType shareWithAttack() {
			switch (this) {
			case NORMAL:
			case SHARED_WITH_ATTACK:
				return SHARED_WITH_ATTACK;
			default:
				return SHARED_WITH_MOVEMENT_ATTACK;
			}
		}
		
		public IdleAnimationType shareWithMovement() {
			switch (this) {
			case NORMAL:
			case SHARED_WITH_MOVEMENT:
				return SHARED_WITH_MOVEMENT;
			default:
				return SHARED_WITH_MOVEMENT_ATTACK;
			}
		}
	}

	
	public static class AnimationNotSupportedException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AnimationNotSupportedException(AnimationType t, AnimationDirection dir){
			super("the current layout does not support animations of type " + t.name + " and direction " + dir.name + "!");
		}
	}
}
