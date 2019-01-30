package net.simondaniel.game.client.gfx;

public enum AnimationType {

	IDLE("idle"),
	MOVEMENT("movement"),
	ATTACK("attack"),
	SPECIAL_ATTACK("special_attack"),
	SPECIAL_ATTACK2("special_attack2"),
	HURT("hurt"),
	ASLEEP("asleep");
	
	public final String name;
	
	private AnimationType(String n){
		this.name = n;
	}
	
	public enum AnimationDirection {
		UP("up"), UP_RIGHT("up_right"),
		RIGHT("right"), DOWN_RIGHT("down_right"),
		DOWN("down"), DOWN_LEFT("down_left"),
		LEFT("left"), UP_LEFT("up_left"),
		NO_DIRECTION("");
		
		public final String name;
		
		public static AnimationDirection fromAngle(float angle) {
			if(angle >= 337.5 || angle < 22.5f) {
				return RIGHT;
			}
			else if(angle < 67.5f) {
				return UP_RIGHT;
			}
			else if(angle < 112.5f) {
				return UP;
			}
			else if(angle < 157.5f) {
				return UP_LEFT;
			}
			else if(angle < 202.5f) {
				return LEFT;
			}
			else if(angle < 247.5f) {
				return DOWN_LEFT;
			}
			else if(angle < 292.5f) {
				return DOWN;
			}
			else{
				return DOWN_RIGHT;
			}
		}
		
		private AnimationDirection(String n){
			this.name = n;
		}

		public AnimationDirection Opposing() {
			switch (this) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case UP_LEFT:
				return DOWN_RIGHT;
			case DOWN_RIGHT:
				return UP_LEFT;
			case UP_RIGHT:
				return DOWN_LEFT;
			case DOWN_LEFT:
				return UP_RIGHT;
			default:
				return NO_DIRECTION;
			}
		}
	}
}