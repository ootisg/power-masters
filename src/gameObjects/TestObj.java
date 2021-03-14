package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import main.GameObject;
import main.MainLoop;
import map.CollisionMesh;
import map.WallSegment;

public class TestObj extends GameObject {

	public boolean testCond = false;
	private CollisionMesh mesh;
	
	public TestObj (int width, int height) {
		createHitbox (0, 0, width, height);
		mesh = new CollisionMesh ("resources/collision/test_walls.json");
	}
	
	@Override
	public void frameEvent () {
		this.setX (getMouseX ());
		this.setY (getMouseY ());
		if (mesh.isColliding (this)) {
			testCond = true;
		} else {
			testCond = false;
		}
	}
	
	@Override
	public void draw () {
		Graphics2D g = (Graphics2D)MainLoop.getWindow ().getBufferGraphics ();
		if (testCond) {
			g.setColor (new Color (0xFF0000));
		} else {
			g.setColor (new Color (0x000000));
		}
		g.fillRect ((int)getX (), (int)getY (), getHitbox ().width, getHitbox ().height);
	}
	
}
