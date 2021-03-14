package visualEffects;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import main.GameObject;
import resources.Sprite;

public class Outline extends GameObject {

	private BufferedImage tracedImg;
	private boolean redraw = false;
	
	private Sprite outlineSprite;
	
	private GameObject tracedObj;
	
	int[] color;
	
	public Outline () {
		
	}
	
	public Outline (GameObject obj, int[] color) {
		setTracedObject (obj);
		setOutlineColor (color);
	}
	
	@Override
	public void draw () {
		if (tracedObj != null) {
			
			//Get the frame to trace and set the redraw parameter appropriately
			int traceFrame = tracedObj.getAnimationHandler ().getFrame ();
			BufferedImage toTrace = tracedObj.getSprite ().getImageArray ()[traceFrame];
			if (tracedImg != toTrace) {
				redraw = true;
			}
			tracedImg = toTrace;
			
			//Genarate and draw the outline
			if (redraw) {
				//Make the rasters
				WritableRaster pxs = tracedImg.getAlphaRaster ();
				BufferedImage outlineImg = new BufferedImage (pxs.getWidth () + 2, pxs.getHeight () + 2, BufferedImage.TYPE_4BYTE_ABGR);
				WritableRaster outlinePxs = outlineImg.getRaster ();
				//Fill the outline raster
				for (int wx = 0; wx < outlinePxs.getWidth (); wx++) {
					for (int wy = 0; wy < outlinePxs.getHeight (); wy++) {
						if (isOutlinePx (pxs, wx - 1, wy - 1)) {
							outlinePxs.setPixel (wx, wy, color);
						}
					}
				}
				//Save the outline
				outlineSprite = new Sprite (outlineImg);
			}
			
		}
		
		//Draw the outline
		if (outlineSprite != null) {
			int[] drawCoords = tracedObj.getDrawCoords ();
			outlineSprite.draw (drawCoords [0] - 1, drawCoords [1] - 1);
		}
	}
	
	public GameObject getTracedObject () {
		return tracedObj;
	}
	
	public int[] getOutlineColor () {
		return color;
	}
	
	public void setTracedObject (GameObject obj) {
		tracedObj = obj;
	}
	
	public void setOutlineColor (int[] color) {
		this.color = color;
	}
	
	private boolean isOutlinePx (WritableRaster srcPixels, int x, int y) {
		
		//Check if the middle pixel is empty
		if (x >= 0 && y >= 0 && x < srcPixels.getWidth () && y < srcPixels.getHeight ()) {
			if (srcPixels.getSample (x, y, 0) != 0) {
				return false;
			}
		}
		
		//Check for surrounding pixels
		for (int wx = x - 1; wx <= x + 1; wx++) {
			for (int wy = y - 1; wy <= y + 1; wy++) {
				if (wx >= 0 && wy >= 0 && wx < srcPixels.getWidth () && wy < srcPixels.getHeight () && !(wx == x && wy == y)) {
					if (srcPixels.getSample (wx, wy, 0) != 0) {
						return true;
					}
				}
			}
		}
		
		//Return false if no pixels were found
		return false;
	}
	
}
