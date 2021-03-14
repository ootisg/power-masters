package ai;

import main.GameObject;

public class TileCollider extends Collider {
	
	public TileCollider () {
		super (14, 14);
	}
	
	public boolean checkCollision (int x, int y) {
		return super.checkCollision (x * 16 + 1, y * 16 + 1);
	}
}
