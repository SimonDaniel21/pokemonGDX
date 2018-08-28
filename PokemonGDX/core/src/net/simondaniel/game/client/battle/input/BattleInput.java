package net.simondaniel.game.client.battle.input;

import javax.crypto.spec.SecretKeySpec;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import net.simondaniel.screens.MainMenuScreen;

public class BattleInput implements InputProcessor{

	private int l;
	private int[] keyBindings;
	
	private int xw, yw, sWidth, sHeight, xOff, yOff, xp, yp;
	private float  cWidth, cHeight, x, y;
	
	private boolean[] pressed, justPressed, justReleased;
	private float angle;
	
	private OrthographicCamera worldCam;
	
	private int scroll;
	
	private String typedKeys = "";
	
	public void update(){
		for(int i = 0; i < l; i++){
			justPressed[i] = false;
			if(justReleased[i]){
				pressed[i] = false;
				justReleased[i] = false;
			}
		}
		scroll = 0;
		typedKeys = "";
	}
	
	public BattleInput(int sWidth, int sHeight) {
		resizeScreen(sWidth, sHeight);
		l = BattleInputType.values().length;
		keyBindings = new int[l];
		for(BattleInputType t : BattleInputType.values()) {
			bindKey(t, t.standardBind);
		}
		pressed = new boolean[l];
		justPressed = new boolean[l];
		justReleased = new boolean[l];
	}
	
	private int camW2, camH2;
	
	public void resizeWorldCamera(OrthographicCamera worldCam){
		this.worldCam = worldCam;
		cWidth = worldCam.viewportWidth;
		cHeight = worldCam.viewportHeight;
		//camW2 = (int) (cWidth/2 * MainMenuScreen.UNITS_PER_PIXEL);
		//camH2 = (int) (cHeight/2 * MainMenuScreen.UNITS_PER_PIXEL);
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
		typedKeys += character;
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
	
	private void updatePosition(int newX, int newY){
		x = newX / (float)(sWidth);
		y = newY / (float)(sHeight);
		
		xp = Math.round(x*MainMenuScreen.WIDTH);
		yp = MainMenuScreen.HEIGHT - Math.round(y*MainMenuScreen.HEIGHT);
		int dx = newX - sWidth/2;
		int dy = newY - sHeight/2;
		angle = (float)Math.atan2(dy, dx) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
	}
	public void updateWorldPosition(int xOff, int yOff){
		this.xOff = xOff;
		this.yOff = yOff;
		//xw = (Math.round((x*cWidth)*MainMenuScreen.UNITS_PER_PIXEL ))  - camW2+ xOff;
		//yw = (Math.round(((1 - y)*cHeight)*MainMenuScreen.UNITS_PER_PIXEL))  - camH2 + yOff;
		
		//System.err.println("pos: " + xw + "(" + (Math.round((x*cWidth)*BattleScreen.UNITS_PER_PIXEL )) +  ") | " + yw + " (" + (Math.round(((1 - y)*cHeight)*BattleScreen.UNITS_PER_PIXEL)) + "))");
	}

	@Override
	public boolean scrolled(int amount) {
		scroll += amount;
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
	
	public int getScroll(){
		return scroll;
	}
	
	public void resizeScreen(int w, int h){
		this.sWidth = w;
		this.sHeight = h;
	}
	
	public int getWorldX(){
		return xw;
	}
	
	public int getWorldY(){
		return yw;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public int getXpixel(){
		return xp;
	}
	
	public int getYpixel(){
		return yp;
	}
	
	public float angle() {
		return angle;
	}
	
	public String getTypedKeys() {
		return typedKeys;
	}
}