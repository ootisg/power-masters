package cutscenes;

import java.util.Random;

import json.JSONObject;

public class RandomTextGenerator extends ParamTextGenerator {

	private long seed;
	private long lastChange = -1;
	
	private String charPool = DEFAULT_CHAR_POOL;
	private int genLength = DEFAULT_LENGTH;
	private int displayTime = DEFAULT_DISPLAY_TIME_MS;
	
	protected static final String DEFAULT_CHAR_POOL = "1234567890";
	protected static final int DEFAULT_LENGTH = 8;
	protected static final int DEFAULT_DISPLAY_TIME_MS = 33;
	
	public RandomTextGenerator () {
		super ();
	}
	
	@Override
	public String getText () {
		//Change text every displayTime ms TODO variable time
		if (System.currentTimeMillis () - lastChange >= displayTime) {
			seed = System.nanoTime ();
			lastChange = System.currentTimeMillis ();
		}
		
		//Setup the random number generator
		Random r = new Random ();
		r.setSeed (seed);
		
		//Generate the string
		String out = "";
		for (int i = 0; i < genLength; i++) {
			out += charPool.charAt (r.nextInt (charPool.length ()));
		}
		return out;
	}

	@Override
	public void setParams (JSONObject params) {
		//Set the JSONObject for the params
		super.setParams (params);
		
		//Set charPool
		String pool = params.getString ("pool");
		if (pool != null) {
			charPool = pool;
		}
		
		//Set length
		Integer len = params.getInt ("length");
		if (len != null) {
			genLength = len;
		}
		
		//Set display time
		Integer time = params.getInt ("time");
		if (time != null) {
			displayTime = time;
		}
	}
}
