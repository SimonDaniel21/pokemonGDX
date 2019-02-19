package net.simondaniel.fabio.input;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import net.simondaniel.fabio.input.InputSate.TransmittedInputState;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;
import net.simondaniel.network.client.Request.ClientInputC;
import net.simondaniel.network.server.GameServer;

public class RemoteInput extends InputAdapter{

	
	private boolean relevant = true;
	private HashMap<Integer, Integer> mappingToRemoteInputs;
	InputSate[] bufferedStates;
	ClientInputC inputPacket;
	InputSate currentState;
	int currentIndex = 0;
	
	private int mousePxlX, mousePxlY; // exact pixel position
	private float mouseX, mouseY; // value from 0 to 1
	private float worldX, worldY; // value in world view coordinates, offset taken in account
	
	
	private int screenWidth, screenHeight; 	//Window dimension
	private float viewWidth, viewHeight; 	//cameras view dimension
	
	//private boolean[] pressed, justPressed, justReleased;
	//private float angle;
	
	Vector3 camPos;
	
	OrthographicCamera cam;
	
	PlayClient client;
	
	public void update(){
		updateWorldPosition();
		int old = currentIndex;
		
		putCurrentStateToPacket();
		
		currentIndex = (currentIndex + 1) % bufferedStates.length;
		if(currentIndex == 0 && relevant) {
			for(int i = 0; i < bufferedStates.length; i++) {
				//System.out.println(i + "\n" + bufferedStates[i] );
			}
			
			client.sendTCP(inputPacket);
			//relevant = false;
		}
		
		currentState = bufferedStates[currentIndex];
		currentState.set(bufferedStates[old]);
	}
	
	private void putCurrentStateToPacket() {
		TransmittedInputState s = inputPacket.inputs[currentIndex];
		s.inputs = currentState.getButtonsCopy();
		s.mx = currentState.mx;
		s.my = currentState.my;
	}
	
	public RemoteInput(OrthographicCamera cam, PlayClient gc, int stateBufferSize) {
		this.client = gc;
		bufferedStates = new InputSate[stateBufferSize];
		inputPacket = new ClientInputC();
		inputPacket.inputs = new TransmittedInputState[stateBufferSize];
		
		for(int i = 0; i < bufferedStates.length; i++) {
			bufferedStates[i] = new InputSate(5);
			inputPacket.inputs[i] = new TransmittedInputState();
		}
		
		currentState = bufferedStates[0];
		resize(screenWidth, screenHeight);
		camPos = cam.position;
		viewWidth = cam.viewportWidth;
		viewHeight = cam.viewportHeight;
		this.cam = cam;
		mappingToRemoteInputs = new HashMap<Integer, Integer>();
		mappingToRemoteInputs.put(Keys.W, 0);
		mappingToRemoteInputs.put(Keys.A, 1);
		mappingToRemoteInputs.put(Keys.S, 2);
		mappingToRemoteInputs.put(Keys.D, 3);
	
	}
	
	public void resize(int screenWidth, int screenHeight){
		this.screenWidth = screenWidth-1;
		this.screenHeight = screenHeight-1;
	}
	
	@Override
	public boolean keyDown(int keycode) {
	
		Integer remoteInput = mappingToRemoteInputs.get(keycode);
		if(remoteInput == null) return false;
		
		currentState.down(mappingToRemoteInputs.get(keycode));
		relevant = true;
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		Integer remoteInput = mappingToRemoteInputs.get(keycode);
		if(remoteInput == null) return false;
		
		currentState.up(mappingToRemoteInputs.get(keycode));
		relevant = true;
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//System.out.println("button: " + button);
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
	
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
		mousePxlX = screenX;
		mousePxlY = screenHeight - screenY;
		mouseX = screenX / (float)screenWidth;
		mouseY = mousePxlY / (float)screenHeight;
		
		updateWorldPosition();
	}
	
	private void updateWorldPosition(){
		worldX = mouseX * viewWidth + camPos.x - viewWidth/2;
		worldY = mouseY * viewHeight + camPos.y - viewHeight/2;
		
		Vector3 v = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		worldX = v.x;
		worldY = v.y;
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

	public float getWorldX(){
		return worldX;
	}
	
	public float getWorldY(){
		return worldY;
	}
	
	public float getScreenX(){
		return mousePxlX;
	}
	
	public float getScreenY(){
		return mousePxlY;
	}
}
