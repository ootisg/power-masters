package spriteParsers;

import java.util.ArrayList;
import java.util.Iterator;

public class ParsedFrame {
	
	ArrayList<Pixel> pixels;
	
	public ParsedFrame (ArrayList<Pixel> pixels) {
		this.pixels = pixels;
	}
	
	public ArrayList<Pixel> getPixels () {
		return pixels;
	}
	
	@Override
	public String toString () {
		Iterator<Pixel> iter = pixels.iterator ();
		String pixelString = "[";
		while (iter.hasNext ()) {
			pixelString += iter.next ().toString ();
		}
		pixelString += "]";
		return pixelString;
	}
}
