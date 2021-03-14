package gui;

public interface CutsceneEvent {
	
	/**
	 * Set the initial state of the object from its variable arguments
	 * @param vars A list of the provided variables
	 * @throws IllegalArgumentException
	 */
	void init (String[] vars) throws IllegalArgumentException;
	/**
	 * Begin the event
	 */
	void begin ();
	/**
	 * Performs a step of the event
	 */
	void step ();
	/**
	 * Ends the event
	 */
	void end ();
	/**
	 * Sets the start time for this event to the given value
	 * @param startTime the new start time
	 */
	void setStartTime (long startTime);
	/**
	 * Sets the end time for this event to the given value
	 * @param endTime the new end time
	 */
	void setEndTime (long endTime);
	/**
	 * Gets the current time
	 * @return the current time
	 */
	long currentTime ();
	/**
	 * Gets the start time for this event
	 * @return event start time
	 */
	long startTime ();
	/**
	 * Gets the end time for this event
	 * @return event end time
	 */
	long endTime ();
	/**
	 * Gets the full duration of this event
	 * @return event duration
	 */
	long duration ();
	/**
	 * Gets the completion progress of this event, as a double
	 * @return event completion progress
	 */
	double completionPercent ();
	/**
	 * Returns true if the event is currently active; false otherwise
	 * @return event active status
	 */
	boolean active ();
	/**
	 * Returns true if the event is complete; false otherwise
	 * @return event completion status
	 */
	boolean complete ();
	/**
	 * Render the event
	 */
	void render ();
	
}
