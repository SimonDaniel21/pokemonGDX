package net.simondaniel.fabio.input;

import com.badlogic.gdx.utils.Queue;

import net.simondaniel.fabio.input.InputSate.TransmittedInputState;

public class InputQueue {
	
	int fetchedInputs = 0;

	Queue<TransmittedInputState> bufferedStates;
	TransmittedInputState notReadyState = new TransmittedInputState(), lastState = notReadyState;

	public InputQueue() {
		bufferedStates = new Queue<InputSate.TransmittedInputState>();
		notReadyState.inputs = new boolean[5];
	}
	
	public synchronized TransmittedInputState nextState() {
		
		fetchedInputs++;
		if(bufferedStates.isEmpty()) {
			notReadyState.mx = lastState.mx;
			notReadyState.my = lastState.my;
			//System.err.println("missing an input " + fetchedInputs);
			return notReadyState;
		}
		return bufferedStates.removeFirst();
	}
	
	public synchronized void feedInputs(TransmittedInputState[] inputStates) {
		for(int i = 0; i < inputStates.length; i++) {
			bufferedStates.addLast(inputStates[i]);
		}
	}
}
