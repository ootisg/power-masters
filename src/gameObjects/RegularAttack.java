package gameObjects;

import enemies.Enemy;
import resources.Sprite;
import resources.Spritesheet;

public class RegularAttack extends Attack {

	public static Spritesheet regSheet = new Spritesheet ("resources/sprites/attack.png");
	public static Sprite regAnim = new Sprite (regSheet, 32, 32);
	
	public RegularAttack (Enemy target) {
		super (regAnim, target);
		getAnimationHandler ().setAnimationSpeed (.5);
		System.out.println (regAnim.getFrameCount ());
	}

	@Override
	public void doAttack () {
		getTarget ().damage (5);
	}

}
