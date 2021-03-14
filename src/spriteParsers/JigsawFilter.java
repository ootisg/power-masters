package spriteParsers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class JigsawFilter {

	//Cannot be constructed
	private JigsawFilter () {
		
	}
	
	public static ArrayList<Point> filter (ArrayList<ParsedFrame> frames, ArrayList<ParsedFrame> matchFrames, int color) {
		ArrayList<Point> result = new ArrayList<Point> ();
		Iterator<ParsedFrame> frameIter = frames.iterator ();
		Iterator<ParsedFrame> matchIter = matchFrames.iterator ();
		ParsedFrame workingFrame;
		ParsedFrame workingMatch;
		while (frameIter.hasNext () && matchIter.hasNext ()) {
			workingFrame = frameIter.next ();
			workingMatch = matchIter.next ();
			if (workingFrame.getPixels ().size () != 0 && workingMatch.getPixels ().size () != 0) {
				Pixel p0 = workingFrame.getPixels ().get (0);
				Pixel p1 = workingMatch.getPixels ().get (0);
				result.add (new Point (p1.getX () - p0.getX (), p1.getY () - p0.getY ()));
			}
		}
		return result;
	}
}
