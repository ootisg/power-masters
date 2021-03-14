package spriteParsers;

import java.util.ArrayList;
import java.util.Iterator;

import main.Hitbox;

public class HitboxFilter {

	private HitboxFilter () {
		//Cannot be constructed
	}
	
	public static ArrayList<Hitbox> filter (ArrayList<ParsedFrame> frames, int color) {
		ArrayList<Hitbox> result = new ArrayList<Hitbox> ();
		Iterator<ParsedFrame> frameIter = frames.iterator ();
		Iterator<Pixel> pixelIter;
		while (frameIter.hasNext ()) {
			ArrayList<Pixel> hitboxPixels = new ArrayList<Pixel> ();
			pixelIter = frameIter.next ().getPixels ().iterator ();
			while (pixelIter.hasNext ()) {
				Pixel working = pixelIter.next ();
				if (working.getRgb () == color) {
					hitboxPixels.add (working);
				}
			}
			if (hitboxPixels.size () == 2) {
				Pixel topLeft = hitboxPixels.get (0);
				Pixel bottomRight = hitboxPixels.get (1);
				result.add (new Hitbox (topLeft.getX (), topLeft.getY (), bottomRight.getX () - topLeft.getX (), bottomRight.getY () - topLeft.getY ()));
			} else {
				result.add (null);
			}
		}
		return result;
	}
}
