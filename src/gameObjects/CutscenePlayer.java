package gameObjects;

import cutscenes.Callback;
import cutscenes.Cutscene;
import main.GameObject;
import main.MainLoop;

public class CutscenePlayer extends GameObject implements Callback {

	@Override
	public void onDeclare () {
		String scenePath = getVariantAttribute ("path");
		System.out.println (scenePath);
		MainLoop.pause ();
		Cutscene scene = new Cutscene (scenePath);
		scene.declare (0, 0);
		scene.setPersistent (true);
		scene.setCallback (this);
		scene.setIgnorePause (true);
	}
	
	@Override
	public void call () {
		MainLoop.resume ();
		forget ();
	}
	
}
