package gameObjects;

public class GlobalSave extends Saveable {

	public static long gameTime;
	
	public GlobalSave () {
		setPersistent (true);
	}
	
	@Override
	public void onDeclare () {
		load ();
	}
	
	@Override
	public void frameEvent () {
		gameTime ++;
		save (String.valueOf (gameTime), "global");
	}
	
	@Override
	public void load () {
		String time = getSaveData ("global");
		if (time != null) {
			gameTime = Long.parseLong (time);
		} else {
			gameTime = 0;
		}
	}
	
	public static long getGameTime () {
		return gameTime;
	}
	
}
