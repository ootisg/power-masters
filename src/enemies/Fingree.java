package enemies;

import resources.Sprite;

public class Fingree extends Enemy {
	
	public static Sprite fingreeIcon = new Sprite ("resources/sprites/fingree.png");
	
	public Fingree () {
		setSprite (fingreeIcon);
		setSize (32);
		setHealth (10);
	}
	
}
