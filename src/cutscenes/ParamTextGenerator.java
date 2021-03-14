package cutscenes;

import json.JSONObject;

public abstract class ParamTextGenerator implements TextGenerator {

	private JSONObject params;
	private long creationTime;
	
	public ParamTextGenerator () {
		creationTime = System.currentTimeMillis ();
	}
	
	@Override
	public JSONObject getParams () {
		return params;
	}
	
	@Override
	public void setParams (JSONObject params) {
		this.params = params;
	}
	
	public long getInitialTime () {
		return creationTime;
	}
	
	public long getElapsedTime () {
		return System.currentTimeMillis () - creationTime;
	}
}
