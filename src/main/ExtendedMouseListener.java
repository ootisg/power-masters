package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class ExtendedMouseListener implements MouseListener, MouseMotionListener {
	private int xOffset;
	private int yOffset;
	private int[] mouseCoords;
	private boolean[] buttonsClicked;
	private boolean[] buttonsDown;
	private boolean[] buttonsReleased;
	private MouseEvent mouseEvent;
	
	public ExtendedMouseListener (int xOffset, int yOffset) {
		//Pretty self-explanitory constructor
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		mouseCoords = new int[] {0, 0};
		buttonsClicked = new boolean[4];
		buttonsDown = new boolean[4];
		buttonsReleased = new boolean[4];
		for (int i = 0; i < 4; i++) {
			buttonsClicked [i] = false;
			buttonsDown [i] = false;
			buttonsReleased [i] = false;
		}
	}
	
	private void updateMouseCoords (MouseEvent event) {
		mouseCoords [0] = event.getX () - xOffset;
		mouseCoords [1] = event.getY () - yOffset;
	}
	
	@Override
	public void mouseClicked (MouseEvent event) {
		//Fires when the mouse is clicked
		buttonsClicked [event.getButton ()] = true;
		mouseEvent = event;
		updateMouseCoords (event);
	}
	
	@Override
	public void mouseEntered (MouseEvent event) {
		//TODO
	}
	
	@Override
	public void mouseExited (MouseEvent event) {
		//TODO
	}
	
	@Override
	public void mousePressed (MouseEvent event) {
		if (!buttonsDown [event.getButton ()]) {
			buttonsDown [event.getButton ()] = true;
			buttonsClicked [event.getButton ()] = true;
		}
		updateMouseCoords (event);
	}
	
	@Override
	public void mouseReleased (MouseEvent event) {
		if (buttonsDown [event.getButton ()]) {
			buttonsDown [event.getButton ()] = false;
			buttonsReleased [event.getButton ()] = true;
		}
		updateMouseCoords (event);
	}
	
	@Override
	public void mouseMoved (MouseEvent event) {
		//Fires when the mouse is moved
		updateMouseCoords (event);
	}
	
	@Override
	public void mouseDragged (MouseEvent event) {
		//Fires when the mouse is dragged; acts similarly to mouseMoved and triggers a click event
		if (!buttonsDown [event.getButton ()]) {
			buttonsClicked [event.getButton ()] = true;
		}
		buttonsDown [event.getButton ()] = true;
		updateMouseCoords (event);
	}
	
	public int[] getMouseCoords () {
		//Returns the current mouse coordinates in the format [x, y]
		return mouseCoords;
	}
	
	public void startFrameUpdate () {
		for (int i = 0; i < 4; i++) {
			buttonsClicked [i] = false;
			buttonsReleased [i] = false;
		}
	}
	
	public MouseInputImage getMouseInputImage () {
		return new MouseInputImage (mouseCoords, buttonsClicked, buttonsDown, buttonsReleased);
	}
	
	public static class MouseInputImage {
		
		private int[] mouseCoords;
		private boolean[] buttonsClicked;
		private boolean[] buttonsDown;
		private boolean[] buttonsReleased;
		
		public MouseInputImage (int[] mouseCoords, boolean[] buttonsClicked, boolean[] buttonsDown, boolean[] buttonsReleased) {
			this.mouseCoords = new int[] {mouseCoords [0], mouseCoords [1]};
			this.buttonsClicked = new boolean[4];
			this.buttonsDown = new boolean[4];
			this.buttonsReleased = new boolean[4];
			for (int i = 0; i < 4; i++) {
				this.buttonsClicked [i] = buttonsClicked [i];
				this.buttonsDown [i] = buttonsDown [i];
				this.buttonsReleased [i] = buttonsReleased [i];
			}
		}
		
		public int[] getMouseCoords () {
			return mouseCoords;
		}
		
		public boolean buttonClicked (int button) {
			return buttonsClicked [button];
		}
		
		public boolean buttonDown (int button) {
			return buttonsDown [button];
		}
		
		public boolean buttonReleased (int button) {
			return buttonsReleased [button];
		}
		
	}
}