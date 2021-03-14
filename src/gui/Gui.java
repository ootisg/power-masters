package gui;

import java.util.HashMap;

import main.GameObject;

public class Gui extends GameObject {

	public HashMap<String, GuiComponent> components;
	
	public Gui () {
		components = new HashMap<String, GuiComponent> ();
	}
	
	public void addComponent (GuiComponent c) {
		String cid = c.getComponentId ();
		components.put (cid, c);
		c.declare (0, 0);
	}
	
	public void frameEvent () {
		
	}
	
	public void draw () {
		
	}
	
}