package gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import vector.Vector4D;

public class ColorMap {
	
	ArrayList<ColorPt> colors;
	
	public static final Color DEFAULT_COLOR = new Color (0xC0C0C0);

	public ColorMap () {
		colors = new ArrayList<ColorPt> ();
	}
	
	public void addColor (Color color, double pos) {
		colors.add (new ColorPt (fromColor (color), pos));
		Collections.sort (colors);
		System.out.println (colors);
	}
	
	public Color getColor (double pos) {
		if (colors.size () == 0) {
			//Default when colors is empty
			return DEFAULT_COLOR;
		} else if (colors.size () == 1) {
			//Return the only color when there's only one
			return fromVec4D (colors.get (0).color);
		} else {
			for (int i = 0; i < colors.size (); i++) {
				if (i == colors.size () - 1) {
					return fromVec4D (colors.get (colors.size () - 1).color);
				}
				ColorPt curr = colors.get (i);
				if (curr.pos >= pos) {
					if (i != 0) {
						//Lerp the two colors pos falls between
						curr = colors.get (i - 1);
						ColorPt next = colors.get (i);
						double lerpPos = (pos - curr.pos) / (next.pos - curr.pos);
						Vector4D a = new Vector4D (curr.color);
						Vector4D b = new Vector4D (next.color);
						System.out.println ((pos - curr.pos) + " / " + (next.pos - curr.pos));
						a.lerp (b, lerpPos);
						return fromVec4D (a);
					} else {
						//Return the last color if applicable
						return fromVec4D (curr.color);
					}
				}
			}
		}
		return DEFAULT_COLOR; //TODO problems here with first color
	}
	
	private Color fromVec4D (Vector4D vec) {
		return new Color ((float)vec.x, (float)vec.y, (float)vec.z, (float)vec.w);
	}
	
	private Vector4D fromColor (Color c) {
		return new Vector4D ((double)c.getRed () / 255, (double)c.getGreen () / 255, (double)c.getBlue () / 255, (double)c.getAlpha () / 255);
	}
	
	private class ColorPt implements Comparable<ColorPt> {
		
		public Vector4D color;
		public double pos;
		
		public ColorPt (Vector4D color, double pos) {
			this.color = color;
			this.pos = pos;
		}
		
		@Override
		public int compareTo (ColorPt c) {
			if (this.pos < c.pos) {
				return -1;
			} else if (this.pos > c.pos) {
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public String toString () {
			return "{" + color.toString () + "; " + pos + "}";
		}
		
	}
	
}
