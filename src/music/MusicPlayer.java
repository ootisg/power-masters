package music;

import json.JSONException;
import json.JSONObject;
import json.JSONUtil;
import main.GameAPI;
import main.GameObject;
import main.GameWindow;

public class MusicPlayer extends GameObject {

	public static final String config_path = "resources/sounds/config/bg_music.json";
	
	private static JSONObject music_params;
	
	private static GameWindow.SoundClip clip;
	
	private Thread waitingThread;
	
	private static long playTime = -1;
	private static String delayedPath;

	public MusicPlayer () {
		declare (0, 0);
		setPersistent (true);
	}
	
	public static void playSong (String filepath) {
		System.out.println (filepath);
		clip = GameAPI.getWindow ().playSound (filepath);
	}
	
	public static void playSongDelayed (String filepath, int time) {
		playTime = System.currentTimeMillis () + time;
		delayedPath = filepath;
	}
	
	public static void playCurrentMapSong () {
		
		//Load the config file if not yet loaded
		if (music_params == null) {
			loadConfig ();
		}
		
		//Get the current room name
		String roomPath = GameAPI.getRoom ().getRoomName ();
		String[] parsedPath = roomPath.split ("/");
		String roomName = parsedPath [parsedPath.length - 1].split ("\\.")[0];
		System.out.println (roomName);
		
		//Get the params for the current map
		if (music_params != null) {
			JSONObject mapParams = music_params.getJSONObject (roomName);
			String musicPath = null;
			Integer delayTime = null;
			if (mapParams != null) {
				musicPath = mapParams.getString ("path");
				delayTime = (Integer)mapParams.get ("delay");
			}
			
			//Play the song
			if (musicPath != null) {
				if (delayTime != null) {
					playSongDelayed (musicPath, delayTime);
				} else {
					playSong (musicPath);
				}
			}
		}
		
	}
	
	public static void loadConfig () {
		if (music_params == null) {
			try {
				music_params = JSONUtil.loadJSONFile (config_path);
			} catch (JSONException e) {
				//Do nothing
			}
		}
	}
	
	public static void stop () {
		if (clip != null) {
			clip.stop ();
		}
	}
	
	@Override
	public void frameEvent () {
		long elapsedTime = System.currentTimeMillis () - playTime;
		if (elapsedTime >= 0 && elapsedTime < 1000) {
			playSong (delayedPath);
			playTime = -1;
		}
	}
	
}
