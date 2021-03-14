package cutscenes;

public abstract class TimedEvent extends Event {

	long startTime;

	protected TimedEvent () {
		
	}
	
	@Override
	public void start () {
		startTime = System.currentTimeMillis ();
	}
	
	@Override
	public boolean isOver () {
		//System.out.println(getElapsedTimeMs () + ", " + getDuration ());
		if (getElapsedTimeMs () >= getDuration ()) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getElapsedTimeMs () {
		return (int)(System.currentTimeMillis () - startTime);
	}
	
	public int getDuration () {
		return getArgs ().getInt ("duration");
	}

}
