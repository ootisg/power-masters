package vector;

import main.GameObject;
import util.Vector2D;
import util.Vector3D;
import util.Vector4D;

public class Vector2D {

	public double x;
	public double y;
	
	public Vector2D (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D (Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public void add (Vector2D toAdd) {
		x += toAdd.x;
		y += toAdd.y;
	}
	
	public void diff (Vector2D toSub) {
		x -= toSub.x;
		y -= toSub.y;
	}
	
	public void scale (double amt) {
		x *= amt;
		y *= amt;
	}
	
	public static double dot (Vector2D a, Vector2D b) {
		return a.x * b.x + a.y * b.y;
	}
	
	public void normalize () {
		double dist = getLength ();
		x /= dist;
		y /= dist;
	}
	
	public void lerp (Vector2D v, double amt) {
		this.x = v.x * (amt) + this.x * (1 - amt);
		this.y = v.y * (amt) + this.y * (1 - amt);
	}
	
	public double getLength () {
		return Math.sqrt (x * x + y * y);
	}
	
	public Vector3D getVector3D () {
		return new Vector3D (x, y, 0);
	}
	
	public Vector4D getVector4D () {
		return new Vector4D (x, y, 0, 0);
	}
	
	public static Vector2D getOffset (GameObject from, GameObject to) {
		return new Vector2D (to.getCenterX () - from.getCenterX (), to.getCenterY () - from.getCenterY ());
	}
	
}
