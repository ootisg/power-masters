package cutscenes;

import json.JSONObject;
import main.GameWindow;
import main.GameWindow.SoundClip;
import main.MainLoop;

public class SoundEvent extends Event {

	SoundClip clip;
	
	@Override
	public void start () {
		JSONObject params = getArgs ().getJSONObject ("params");
		Integer numPlays = params.getInt ("loopCount");
		if (numPlays == null) {
			numPlays = 1;
		}
		clip = MainLoop.getWindow ().playSound (params.getString ("path"), numPlays);
	}

	@Override
	public void doFrame () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOver () {
		if (clip.getStatus () == SoundClip.STATUS_STOPPED) {
			return true;
		}
		return false;
	}
	
}
