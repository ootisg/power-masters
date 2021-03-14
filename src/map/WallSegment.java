package map;

import java.awt.Color;
import java.awt.Graphics;

import main.GameObject;
import main.Hitbox;
import main.MainLoop;

public class WallSegment extends GameObject {
	
	private double sx;
	private double sy;
	private double ex;
	private double ey;
	
	public WallSegment (double sx, double sy, double ex, double ey) {
		
		//Set the line attributes accordingly
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
		
		//Construct a bounding box
		double tlx = Math.min (sx, ex);
		double tly = Math.min (sy, ey);
		double brx = Math.max (sx, ex);
		double bry = Math.max (sy, ey);
		
		//Align the hitbox with the bounding box
		declare (tlx, tly);
		createHitbox (0, 0, (int)(brx - tlx), (int)(bry - tly));
		
	}
	
	public boolean pointAbove (double x, double y) {
		
		//Get the difference in x and y for slope calculations
		double diffx = ex - sx;
		double diffy = ey - sy;
		
		if (diffx != 0) {
			
			//General case
			double slope = diffy / diffx;
			double py = slope * (x - sx) + sy;
			if (py > y) {
				return true;
			} else {
				return false;
			}
			
		} else {
			
			//Special case (left is considered 'above' for vertical lines)
			if (x < sx) {
				return true;
			} else {
				return false;
			}
			
		}
	}
	
	public boolean collidesWith (Hitbox h) {
		
		if (getHitbox ().checkOverlap (h)) {
			
			//Check for the line segment overlap
			boolean first = pointAbove (h.x, h.y);
			if (first != pointAbove (h.x + h.width, h.y)) {return true;}
			if (first != pointAbove (h.x, h.y + h.height)) {return true;}
			if (first != pointAbove (h.x + h.width, h.y + h.height)) {return true;}
			return false;
			
		} else {
			
			//Line cannot be colliding with the given hitbox
			return false;
			
		}
		
	}
	
	@Override
	public void draw () {
		
		Graphics g = MainLoop.getWindow ().getBufferGraphics ();
		g.setColor (new Color (0x000000));
		g.drawLine ((int)sx, (int)sy, (int)ex, (int)ey);
		
	}
	
}