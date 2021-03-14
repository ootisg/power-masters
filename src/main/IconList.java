package main;

import resources.Sprite;

public class IconList extends GameAPI {
	Sprite[] icons;
	public IconList (Sprite[] icons) {
		this.icons = icons;
	}
	public void render (int x, int y, int spacing) {
		IconList.render (icons, x, y, spacing);
	}
	public static void render (Sprite[] icons, int x, int y, int spacing) {
		for (int i = 0; i < icons.length; i ++) {
			getSprites ().itemBorder.draw (x + i * getSprites ().itemBorder.getImageArray ()[0].getWidth (), y);
			icons [i].draw (x + i * getSprites ().itemBorder.getImageArray ()[0].getWidth (), y);
		}
	}
}