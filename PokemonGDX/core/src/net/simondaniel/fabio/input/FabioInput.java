package net.simondaniel.fabio.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FabioInput extends InputAdapter{
	
	private Viewport view;
	

	private int l;
	int[] keyBindings;
	
	private int mouseScreenX, mouseScreenY;
	private float worldX, worldY;
	private Vector2 tempVec;
	
	private boolean[] pressed, justPressed, justReleased;
	private float angle;
	
	public void update(){
		for(int i = 0; i < l; i++){
			justPressed[i] = false;
			if(justReleased[i]){
				pressed[i] = false;
				justReleased[i] = false;
			}
		}
		updateWorldPosition();
	}
	
	public FabioInput(Viewport view) {
		tempVec = new Vector2();
		this.view = view;
		l = BattleInputType.values().length;
		keyBindings = new int[l];
		for(BattleInputType t : BattleInputType.values()) {
			bindKey(t, t.standardBind);
		}
		pressed = new boolean[l];
		justPressed = new boolean[l];
		justReleased = new boolean[l];
	}
	
	public void resize(int screenWidth, int screenHeight){
		view.update(screenWidth, screenHeight);
	}
	
	public void bindKey(BattleInputType type, int keyBinding) {
		keyBindings[type.ordinal()] = keyBinding;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		System.out.println("key pressed: " + keycode);
		for(int i = 0; i < l; i ++) {
			System.out.println("testing for " + BattleInputType.values()[i].name() + " - " + keyBindings[i]);
			if(keycode == keyBindings[i]) {
				pressed[i] = true;
				justPressed[i] = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for(int i = 0; i < l; i ++) {
			if(keycode == keyBindings[i]) {
				justReleased[i] = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//System.out.println("button: " + button);
		for(int i = 0; i < l; i ++) {
			//System.out.println("testing for " + BattleInputType.values()[i].name() + " - " + keyBindings[i]);
			if(button == keyBindings[i]) {
				pressed[i] = true;
				justPressed[i] = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for(int i = 0; i < l; i ++) {
			if(button == keyBindings[i]) {
				justReleased[i] = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updatePosition(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		updatePosition(screenX, screenY);
		return false;
	}
	
	private void updatePosition(int screenX, int screenY){
		mouseScreenX = screenX;
		mouseScreenY = screenY;
		
		updateWorldPosition();
	}
	
	private void updateWorldPosition(){
		
		if (!isInsideViewport(mouseScreenX, mouseScreenY)) return;
		
		tempVec.set(mouseScreenX, mouseScreenY);
		tempVec = view.unproject(tempVec);
		worldX = tempVec.x;
		worldY = tempVec.y;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public static enum BattleInputType{
		MOVE_TO_CURSOR(Input.Buttons.RIGHT),
		SKILL1(Input.Keys.NUM_1),
		SKILL2(Input.Keys.NUM_2),
		SKILL3(Input.Keys.NUM_3),
		SKILL4(Input.Keys.NUM_4);
		
		public final int standardBind;
		
		private BattleInputType(int standardBind) {
			this.standardBind = standardBind;
		}
	}

	public boolean isPressed(BattleInputType t) {
		return pressed[t.ordinal()];
	}
	
	public boolean isJustPressed(BattleInputType t) {
		return justPressed[t.ordinal()];
	}
	
	public boolean isJustReleased(BattleInputType t) {
		return justReleased[t.ordinal()];
	}
	
	public float getWorldX(){
		return worldX;
	}
	
	public float getWorldY(){
		return worldY;
	}
	
	public float getScreenX(){
		return mouseScreenX;
	}
	
	public float getScreenY(){
		return mouseScreenY;
	}
	
	public float angle() {
		return angle;
	}
	
	/** Check if screen coordinates are inside the viewport's screen area. */
	protected boolean isInsideViewport (int screenX, int screenY) {
		int x0 = view.getScreenX();
		int x1 = x0 + view.getScreenWidth();
		int y0 = view.getScreenY();
		int y1 = y0 + view.getScreenHeight();
		screenY = Gdx.graphics.getHeight() - 1 - screenY;
		return screenX >= x0 && screenX < x1 && screenY >= y0 && screenY < y1;
	}

}