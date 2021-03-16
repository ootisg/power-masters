package gameObjects;

import enemies.Enemy;
import main.GameObject;
import resources.Sprite;

public abstract class Attack extends GameObject {

	private Sprite animation;
	private Enemy target;
	
	public Attack (Sprite anim, Enemy target) {
		this.animation = anim;
		this.target = target;
		setSprite (anim);
		this.getAnimationHandler ().setRepeat (false);
	}
	
	public Enemy getTarget () {
		return target;
	}
	
	public abstract void doAttack ();
	
	@Override
	public void frameEvent () {
		if (getAnimationHandler ().isDone ()) {
			doAttack ();
			forget ();
		}
	}
	
}
