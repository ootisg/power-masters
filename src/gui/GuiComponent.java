package gui;

import java.util.ArrayList;

import main.GameObject;
import main.MainLoop;
import main.TextInterface;
import resources.Sprite;

public abstract class GuiComponent extends GameObject {
	protected GuiComponent () {
		this (null);
		setPriority (-3);
	}
	protected GuiComponent (Sprite background) {
		if (background != null) {
			this.setSprite (background);
		}
		this.setPersistent (true);
	}
	public abstract String getComponentId ();
}