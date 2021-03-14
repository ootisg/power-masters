package gui;

import java.awt.Point;
import java.awt.event.KeyEvent;

import main.MainLoop;

public class MapUi extends GuiComponent {

	private Point mousePrev = null;
	
	private int scrollX = 0;
	private int scrollY = 0;
	
	private static int SCROLL_X_MIN = 0;
	private static int SCROLL_Y_MIN = 0;
	private static int SCROLL_X_MAX = 2048;
	private static int SCROLL_Y_MAX = 2048;
	
	@Override
	public String getComponentId () {
		return "PumkinUi";
	}
	
	@Override
	public void frameEvent () {
		//Handle dragging the screen
		if (keyCheck (KeyEvent.VK_SHIFT) && mouseButtonDown (1)) {
			if (mousePrev == null) {
				//If dragging just started
				mousePrev = new Point (getMouseX (), getMouseY ());
			} else {
				//Dragging has occured
				//Set the new scroll position accordingly
				double diffX = mousePrev.getX () - getMouseX ();
				double diffY = mousePrev.getY () - getMouseY ();
				scrollX += diffX;
				scrollY += diffY;
				//Bound the scroll position
				if (scrollX < SCROLL_X_MIN) {
					scrollX = SCROLL_X_MIN;
				}
				if (scrollX + MainLoop.getWindow ().getResolution ()[0] > SCROLL_X_MAX) {
					scrollX = SCROLL_X_MAX - MainLoop.getWindow ().getResolution ()[0];
				}
				if (scrollY < SCROLL_Y_MIN) {
					scrollY = SCROLL_Y_MIN;
				}
				if (scrollY + MainLoop.getWindow ().getResolution ()[1] > SCROLL_Y_MAX) {
					scrollY = SCROLL_Y_MAX - MainLoop.getWindow ().getResolution ()[1];
				}
				//Update mouse coords
				mousePrev = new Point (getMouseX (), getMouseY ());
			}
		} else {
			mousePrev = null;
		}
		
		getRoom ().setView (scrollX, scrollY);
	}
	
	@Override
	public void draw () {
		
	}

}
