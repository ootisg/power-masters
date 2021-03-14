package visualEffects;

import json.JSONObject;
import main.GameObject;

public abstract class ScreenOverlay extends GameObject {
	
	public abstract void setProperties (JSONObject properties);
	
}
