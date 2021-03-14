package vector;

import main.GameObject;

public class Vector3D {
	
	public double x;
	public double y;
	public double z;
	
	public Vector3D (double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D (Vector3D v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public void add (Vector3D toAdd) {
		x += toAdd.x;
		y += toAdd.y;
		z += toAdd.z;
	}
	
	public void diff (Vector3D toSub) {
		x -= toSub.x;
		y -= toSub.y;
		z -= toSub.z;
	}
	
	public void scale (double amt) {
		x *= amt;
		y *= amt;
		z *= amt;
	}
	
	public void normalize () {
		double dist = getLength ();
		x /= dist;
		y /= dist;
		z /= dist;
	}
	
	public void lerp (Vector3D v, double amt) {
		this.x = v.x * (amt) + this.x * (1 - amt);
		this.y = v.y * (amt) + this.y * (1 - amt);
		this.z = v.z * (amt) + this.z * (1 - amt);
	}
	
	public double getLength () {
		return Math.sqrt (x * x + y * y + z * z);
	}
	
	public Vector2D getVector2D () {
		return new Vector2D (x, y);
	}
	
	public Vector4D getVector4D () {
		return new Vector4D (x, y, z, 0);
	}
	
	public static double dot (Vector3D a, Vector3D b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	@Override
	public String toString () {
		return x + ", " + y + ", " + z;
	}
}
