package spriteParsers;

public class Pixel implements Comparable {
	
	private int x;
	private int y;
	private int color;
	
	public Pixel (int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public int getColor () {
		return color;
	}
	
	public int getAlpha () {
		return (color & 0xFF000000) >> 24;
	}
	
	public int getRed () {
		return (color & 0x00FF0000) >> 16;
	}
	
	public int getGreen () {
		return (color & 0x0000FF00) >> 8;
	}
	
	public int getBlue () {
		return color & 0x000000FF;
	}
	
	public int getRgb () {
		return color & 0x00FFFFFF;
	}
	
	@Override
	public int compareTo (Object o) {
		if (o instanceof Pixel) {
			Pixel p = (Pixel)o;
			if (getY () > p.getY ()) {
				return 1;
			} else if (getY () < p.getY ()) {
				return -1;
			} else {
				if (getX () > p.getX ()) {
					return 1;
				} else if (getX () < p.getX ()) {
					return -1;
				} else {
					return 0;
				}
			}
		} else {
			throw new IllegalArgumentException ();
		}
	}
	
	@Override
	public String toString () {
		return "[" + x + ", " + y + ", " + getRgb () + "]";
	}
}