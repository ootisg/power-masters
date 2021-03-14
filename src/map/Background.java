package map;

import main.MainLoop;
import resources.AnimationHandler;
import resources.Sprite;

public class Background {
	private AnimationHandler animationHandler;
	private double scrollRate;
	public Background (Sprite image) {
		this.animationHandler = new AnimationHandler (image);
		this.scrollRate = 1.0;
	}
	public void draw (double viewX, double viewY) {
		int width = MainLoop.getWindow ().getResolution () [0];
		int height = MainLoop.getWindow ().getResolution () [1];
		int imgWidth = animationHandler.getSprite ().getImageArray ()[animationHandler.getFrame ()].getWidth ();
		int imgHeight = animationHandler.getSprite ().getImageArray ()[animationHandler.getFrame ()].getHeight ();
		for (int i = -((int)viewX % imgWidth); i < width; i += imgWidth) {
			for (int j = -((int)viewY % imgHeight); j < height; j += imgHeight) {
				animationHandler.animate (i, j, false, false);
			}
		}
		animationHandler.animate ((int)(-viewX / scrollRate), (int)(-viewY / scrollRate), false, false);
	}
	public void setImage (Sprite image) {
		animationHandler.setSprite (image);
	}
	public void setScrollRate (double scrollRate) {
		this.scrollRate = scrollRate;
	}
	public Sprite getImage () {
		return animationHandler.getSprite ();
	}
	public double getScrollRate () {
		return scrollRate;
	}
}