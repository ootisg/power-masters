package visualEffects;

import java.awt.Color;
import java.awt.Graphics;

import json.JSONObject;
import main.MainLoop;

public class ColorOverlay extends ScreenOverlay {

	private Color overlayColor = new Color (0, 0, 0, .2f);
	
	public ColorOverlay () {

	}
	
	public Color getColor () {
		return overlayColor;
	}
	
	public void setColor (Color c) {
		overlayColor = c;
	}
	
	@Override
	public void draw () {
		int[] dimensions = MainLoop.getWindow ().getResolution ();
		Graphics g = MainLoop.getWindow ().getBufferGraphics ();
		g.setColor (overlayColor);
		g.fillRect (0, 0, dimensions[0], dimensions[1]);
	}

	@Override
	public void setProperties (JSONObject properties) {
		JSONObject c = properties.getJSONObject ("color");
		overlayColor = new Color (c.getInt ("red"), c.getInt ("green"), c.getInt ("blue"), c.getInt ("alpha"));
	}
}
