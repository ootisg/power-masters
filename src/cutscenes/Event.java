package cutscenes;

import json.JSONObject;

public abstract class Event {
	
	private Cutscene scene;
	private JSONObject args;
	
	public Event () {
		//Default constructor for ease of inheritance
	}
	
	/**
	 * Runs this event
	 */
	public abstract void start ();
	
	/**
	 * Runs a frame of this event
	 */
	public abstract void doFrame ();
	
	/**
	 * Run at the end of this event
	 */
	public abstract void end ();
	
	/**
	 * The method to draw this cutscene event
	 */
	public abstract void draw ();
	
	/**
	 * Whether this event is finished executing
	 * @return true if the event is finished; false otherwise
	 */
	public abstract boolean isOver ();
	
	/**
	 * Gets the cutscene this event is associated with
	 * @return the Cutscene this event is in
	 */
	public Cutscene getAssociatedCutscene () {
		return scene;
	}
	
	/**
	 * Gets the arguments for this event
	 * @return the arguments for this event as a JSONObject
	 */
	public JSONObject getArgs () {
		return args;
	}
	
	/**
	 * Gets the id of this event
	 * @return The ID of this event, as a String
	 */
	public String getId () {
		return args.getString ("id");
	}
	
	/**
	 * Sets the cutscene associated with this event
	 * @param scene the Cutscene this event is in
	 */
	public void setAssociatedCutscene (Cutscene scene) {
		this.scene = scene;
	}
	
	/**
	 * Sets the arguments for this event to the given JSONObject
	 * @param args the args to use for this Event, as a JSONObject
	 */
	public void setArgs (JSONObject args) {
		this.args = args;
	}
}
