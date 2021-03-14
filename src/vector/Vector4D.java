package vector;

import main.GameObject;
import util.Vector2D;
import util.Vector3D;
import util.Vector4D;

public class Vector4D {
	
	public double x;
	public double y;
	public double z;
	public double w;
	
	public Vector4D (double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4D (Vector4D v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}
	
	public void add (Vector4D toAdd) {
		x += toAdd.x;
		y += toAdd.y;
		z += toAdd.z;
		w += toAdd.w;
	}
	
	public void diff (Vector4D toSub) {
		x -= toSub.x;
		y -= toSub.y;
		z -= toSub.z;
		w -= toSub.w;
	}
	
	public void scale (double amt) {
		x *= amt;
		y *= amt;
		z *= amt;
		w *= amt;
	}
	
	public void normalize () {
		double dist = getLength ();
		x /= dist;
		y /= dist;
		z /= dist;
		w /= dist;
	}
	
	public void lerp (Vector4D v, double amt) {
		this.x = v.x * (amt) + this.x * (1 - amt);
		this.y = v.y * (amt) + this.y * (1 - amt);
		this.z = v.z * (amt) + this.z * (1 - amt);
		this.w = v.w * (amt) + this.w * (1 - amt);
	}
	
	public double getLength () {
		return Math.sqrt (x * x + y * y + z * z + w * w);
	}
	
	public Vector3D getVector3D () {
		return new Vector3D (x, y, z);
	}
	
	public Vector2D getVector2D () {
		return new Vector2D (x, y);
	}
	
	public static double dot (Vector3D a, Vector3D b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	@Override
	public String toString () {
		return x + ", " + y + ", " + z + ", " + w;
	}
}
