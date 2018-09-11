package net.simondaniel.game.client.gfx;

/**
 * legt Fest welche eine Animationen ein Pokemon besitzt und in welcher Weise sie eingesetzt werden
 * @author simon
 *
 */
public class AnimationLayout {
	
	public final SpecialAnimationType sat;
	public final AttackAnimationType at;
	public final MovementAnimationType mt;
	public final int others;
	
	public final String[] animationNames;
	public final int[] mapping;
	
	public AnimationLayout(SpecialAnimationType sat, AttackAnimationType at, MovementAnimationType mt) {
		this(sat, at, mt, 0);
	}
	
	public AnimationLayout(SpecialAnimationType sat, AttackAnimationType at, MovementAnimationType mt, int others) {
		this.sat = sat;
		this.at = at;
		this.mt = mt;
		this.others = others;
		
		mapping = new int[AnimationType.values().length];
		
		int l = 0;
		switch (sat) {
		case ONE_SPECIAL:
			l += 1;
			break;
		case TWO_SPECIALS:
			l += 2;
			break;
		}
		
		switch (at) {
		case NORMAL:
			l += 1;
			break;
		case SHARED_WITH_IDLE:
			break;
		}
		
		switch (mt) {
		case NORMAL:
			l += 1;
			break;
		case SHARED_WITH_IDLE:
			break;
		}
		
		l += 1; // idle
		l += 1; // asleep
		l += 1; // hurt;
		l += others;
		
		animationNames = new String[l];
		int ptr = 0;
		
		int idlePTR = ptr;
		put(AnimationType.IDLE, ptr);
		String idleName = "idle";
		if(mt == MovementAnimationType.SHARED_WITH_IDLE) {
			idleName += "_movement";
		}
		if(at ==AttackAnimationType.SHARED_WITH_IDLE) {
			idleName += "_attack";
		}
		animationNames[ptr++] = idleName;
		put(AnimationType.ASLEEP.ASLEEP, ptr);
		animationNames[ptr++] = "asleep";
		put(AnimationType.HURT, ptr);
		animationNames[ptr++] = "hurt";
		
		switch (sat) {
		case ONE_SPECIAL:
			put(AnimationType.SPECIAL_ATTACK, ptr);
			put(AnimationType.SPECIAL_ATTACK2, ptr);
			animationNames[ptr++] = "special";
			break;
		case TWO_SPECIALS:
			put(AnimationType.SPECIAL_ATTACK, ptr);
			animationNames[ptr++] = "special1";
			put(AnimationType.SPECIAL_ATTACK, ptr);
			animationNames[ptr++] = "special2";
			break;
		}
		
		switch (at) {
		case NORMAL:
			put(AnimationType.ATTACK, ptr);
			animationNames[ptr++] = "attack";
			break;
		case SHARED_WITH_IDLE:
			put(AnimationType.ATTACK, idlePTR);
			break;
		}
		
		switch (mt) {
		case NORMAL:
			put(AnimationType.MOVEMENT, ptr);
			animationNames[ptr++] = "movement";
			break;
		case SHARED_WITH_IDLE:
			put(AnimationType.MOVEMENT, idlePTR);
			break;
		}
		
		for(int i = 0; i < others; i++) {
			animationNames[ptr++] = "misc" + (i + 1);
		}
	}
	
	private void put(AnimationType t, int i) {
		mapping[t.ordinal()] = i;
	}
	
	public static enum PokemonAnimationLayout{
		pikachu(new AnimationLayout(SpecialAnimationType.ONE_SPECIAL, AttackAnimationType.NORMAL, MovementAnimationType.NORMAL)),
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

	public int getAnimationIndex(AnimationType t) throws AnimationNotSupportedException{
		int r = mapping[t.ordinal()];
		if(r == -1) throw new AnimationNotSupportedException(t);
		return r;
	}
	
	public static class AnimationNotSupportedException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AnimationNotSupportedException(AnimationType t){
			super("the current layout does not support animations of type " + t + " !");
		}
	}
}
