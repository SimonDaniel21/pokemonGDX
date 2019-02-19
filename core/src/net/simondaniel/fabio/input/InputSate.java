package net.simondaniel.fabio.input;

public class InputSate {

	public float mx, my;
	private boolean[] buttons; // may count number of toggles instead
	private boolean[] activatedThisRound;
	private boolean[] pendingRelease;
	
	public InputSate(int size) {
		buttons = new boolean[size];
		activatedThisRound = new boolean[size];
		pendingRelease = new boolean[size];
	}
	
	public void set(InputSate other) {
		this.mx = other.mx;
		this.my = other.my;
		for(int i = 0; i < buttons.length; i++) {
			this.buttons[i] = !other.pendingRelease[i] && other.buttons[i];
			this.activatedThisRound[i] = false;
			this.pendingRelease[i] = false;
		}
	}
	
	public void down(int i) throws IndexOutOfBoundsException{
		buttons[i]  = true;
		activatedThisRound[i] = true;
		pendingRelease[i] = false;
	}
	
	public void up(int i) throws IndexOutOfBoundsException{
		buttons[i]  = activatedThisRound[i];
		pendingRelease[i] = buttons[i];
	}
	
	@Override
	public String toString() {
		String s = "mx=" + mx + "   my=" + my + "  ";
		for(int i = 0; i < buttons.length; i++) {
			s += "[" + i + "]=" + buttons[i] + " ";
		}
		return s;
	}
	
	public static class TransmittedInputState{
		public float mx, my;
		public boolean[] inputs;
		@Override
		public String toString() {
			String s = "mx=" + mx + "   my=" + my + "  ";
			for(int i = 0; i < inputs.length; i++) {
				s += "[" + i + "]=" + inputs[i] + " ";
			}
			return s;
		}
	}
	
	public boolean[] getButtonsCopy() {
		return buttons.clone();
	}
}
