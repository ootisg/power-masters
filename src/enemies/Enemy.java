package enemies;

import main.GameObject;
import resources.Sprite;

public abstract class Enemy extends GameObject {
	
	private double size;
	private double health = 1;
	
	public Enemy () {
		
	}
	
	protected void setSize (double size) {
		this.size = size;
		createHitbox (0, 0, (int)size, (int)size);
	}
	
	public void setHealth (double health) {
		this.health = health;
	}
	
	public double getSize () {
		return size;
	}
	
	public double getHealth () {
		return health;
	}
	
	public void damage (double amt) {
		health -= amt;
	}
	
	public void deathEvent () {
		
	}
	
	@Override
	public void frameEvent () {
		
		//Make this enemy die if its health is drained
		if (health <= 0) {
			deathEvent ();
			forget ();
		}
		
	}
	
}
