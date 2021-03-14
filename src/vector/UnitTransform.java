package vector;

public interface UnitTransform {
	
	/**
	 * Outputs a double inclusively between 0 and 1 for the given non-negative 'time' value t.
	 * If the output follows some pattern for which it would be LT 0 or GT 1 for a given t, transform should output 0 or 1 respectively.
	 * @param t the time value to use
	 * @return some value between 0 and 1
	 */
	public double transform (double t);
	
}
