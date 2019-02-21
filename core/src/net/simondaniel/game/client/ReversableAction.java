package net.simondaniel.game.client;

import java.util.LinkedList;
import java.util.Queue;

import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;

public abstract class ReversableAction<T, O> {
	
	protected int corrections = 0;	// keeps track how often the server has not approved a action
	protected O object;	// object that executes a ReversableAction

	protected Queue<T> toExecute; // execution buffer locally for client
	protected Queue<T> waitForApproval; // actions that havent been approved by the server
	
	protected PlayClient client; // a reference to a network object just for convenience
	
	/**
	 * constructs a ReversableAction
	 * @param object that executes the Action
	 * @param client2 ref to a GameClient
	 */
	public ReversableAction(O object, PlayClient client2){
		toExecute = new LinkedList<T>();
		waitForApproval = new LinkedList<T>();
		this.object = object;
		this.client = client2;
	}
	
	/**
	 * initiates a Action of Type T
	 * @param action action
	 */
	protected abstract void play(T action);
	/**
	 * undos a given action of Type T,
	 * happens to undo all not approved Actions
	 * @param action
	 */
	protected abstract void replay(T action);
	/**
	 * gets called every Frame for additional logic
	 * @param delta time in seconds
	 */
	public abstract void update(float delta);
	
	/**
	 * should send the Action notication to the server, use client for convenience
	 * @param action
	 */
	public abstract void informServer(T action);
	
	/**
	 * gets called when the server finaly approved a action
	 * @param action
	 */
	public abstract void approves(T action);
	
	/**
	 * call this when receive the answer from server wheather he approved  the Action
	 * @param action
	 */
	public void verify(boolean approved) {
		if(approved) {
			approves(waitForApproval.poll());
		}
		else {
			for(T action : waitForApproval) {
				replay(action);
			}
			waitForApproval.clear();
			corrections++;
		}
	}
	
	/**
	 * a guard that can prevent new actions from being accepted by execute
	 * @return
	 */
	protected abstract boolean acceptsNewActions();
	
	/**
	 * executes the given Action on the Object if and only if acceptsNewActions() is true
	 * @param action
	 * @return weather or not the Action is going to be qued
	 */
	public boolean execute(T action) {
		if(acceptsNewActions()) {
			toExecute.add(action);
			informServer(action);
			waitForApproval.add(action);
			return true;
		}
		return false;
	}
}
