package ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import main.GameObject;
import main.MainLoop;

public class PathDisplayer extends GameObject {
	
	String pathData;
	String renderData;
	Scanner dataScanner;
	LinkedList<Point> pts;
	
	Point anchor;
	
	int gridWidth;
	
	public PathDisplayer (String pathData, LinkedList<Point> pts, int width) {
		this.pathData = pathData;
		this.pts = pts;
		dataScanner = new Scanner (pathData);
		if (dataScanner.hasNextLine ()) {
			renderData = dataScanner.nextLine ();
		}
		gridWidth = width;
		anchor = new Point (0, 0);
	}
	
	public void next () {
		renderData = dataScanner.nextLine ();
	}
	
	@Override
	public void draw () {
		Graphics g = MainLoop.getWindow ().getBufferGraphics ();
		if (renderData != null) {
			Scanner s = new Scanner (renderData);
			g.setColor (new Color (0x000000));
			for (int wy = 0; s.hasNext (); wy ++) {
				for (int wx = 0; wx < gridWidth; wx ++) {
					//int num = s.nextInt ();
					g.drawRect (wx * 16, wy * 16, 16, 16);
					/*if (num != -1) {
						g.drawString (String.valueOf (num), wx * 16 + 2, wy * 16 + 12);
					}*/
				}
			}
		}
		if (pts != null) {
			int anchorX = (anchor.x - pts.getFirst ().x) * gridWidth;
			int anchorY = (anchor.y - pts.getFirst ().y) * gridWidth;
			g.setColor (new Color (0xFF0000));
			Iterator<Point> iter = pts.iterator ();
			Point last;
			if (iter.hasNext ()) {
				last = iter.next ();
				while (iter.hasNext ()) {
					Point current = iter.next ();
					g.drawLine (last.x * 16 + 8 + anchorX - getRoom ().getViewX (), last.y * 16 + 8 + anchorY - getRoom ().getViewY (), current.x * 16 + 8 + anchorX - getRoom ().getViewX (), current.y * 16 + 8 + anchorY - getRoom ().getViewY ());
					last = current;
				}
			}
		}
	}
	
	@Override
	public void frameEvent () {
		if (keyPressed ('F')) {
			next ();
		}
	}
	
	public void setAnchor (Point point) {
		anchor = point;
	}
}
