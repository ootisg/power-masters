package gui;

public abstract class GenericEvent implements CutsceneEvent {

	private boolean active;
	
	private long startTime;
	private long endTime;
	
	private long duration;
	private double startPercent;
	private double endPercent;
	
	@Override
	public void init (String[] vars) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin () {
		setActive (true);
	}

	@Override
	public void step () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end () {
		setActive (false);
	}
	
	@Override
	public void setStartTime (long startTime) {
		this.startTime = startTime;
	}

	@Override
	public void setEndTime (long endTime) {
		this.endTime = endTime;
	}

	@Override
	public long currentTime () {
		return System.currentTimeMillis ();
	}

	@Override
	public long startTime () {
		return startTime;
	}

	@Override
	public long endTime () {
		return endTime;
	}

	@Override
	public long duration () {
		return endTime - startTime;
	}

	@Override
	public double completionPercent () {
		return ((double)(currentTime () - startTime ())) / duration ();
	}
	
	@Override
	public boolean active () {
		return active;
	}

	@Override
	public boolean complete () {
		return currentTime () > startTime ();
	}
	
	@Override
	public void render () {
		
	}
	
	public void beginStep () {
		step ();
	}
	
	public void endStep () {
		step ();
	}
	
	protected void setActive (boolean active) {
		this.active = active;
	}
}
