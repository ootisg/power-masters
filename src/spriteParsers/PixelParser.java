package spriteParsers;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import resources.Sprite;

public class PixelParser {
	
	private PixelParser () {
		//Cannot be constructed
	}
	
	public static ArrayList<ParsedFrame> parse (Sprite sprite) {
		ArrayList<ParsedFrame> frames = new ArrayList<ParsedFrame> ();
		BufferedImage[] images = sprite.getImageArray ();
		for (int i = 0; i < images.length; i ++) {
			WritableRaster alpha = images [i].getAlphaRaster ();
			WritableRaster color = images [i].getRaster ();
			int[] colorSample = new int[4];
			int[] alphaSample = new int[1];
			ArrayList<Pixel> pixels = new ArrayList<Pixel> ();
			for (int wy = 0; wy < alpha.getHeight (); wy ++) {
				for (int wx = 0; wx < alpha.getWidth (); wx ++) {
					int pixelAlpha = alpha.getSample (wx, wy, 0);
					if (pixelAlpha != 0) {
						int workingColor = 0;
						workingColor += (color.getSample (wx, wy, 0) << 16);
						workingColor += (color.getSample (wx, wy, 1) << 8);
						workingColor += color.getSample (wx, wy, 2);
						workingColor = (workingColor | (pixelAlpha << 24));
						pixels.add (new Pixel (wx, wy, workingColor));
					}
				}
			}
			frames.add (new ParsedFrame (pixels));
		}
		return frames;
	}
}