package cutscenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import json.JSONObject;
import main.MainLoop;

/**
 * Makes an awesome text event
 * @author nathan
 *
 */
public class TextEvent extends TimedEvent {
	
	TextGenerator text = null;
	int prevLength = -1;

	@Override
	public void doFrame () {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void end () {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void draw () {
		//Parse out text info
		if (text == null) {
			setupTextGenerator ();
		}
		
		//Get graphics and params
		Graphics2D g = (Graphics2D)MainLoop.getWindow ().getBufferGraphics ();
		JSONObject params = getArgs ().getJSONObject ("params");
		
		//Set default color
		Color defaultColor = new Color (0x000000);
		g.setColor (defaultColor);
		
		//Parse out font info
		JSONObject fontParams = params.getJSONObject ("font");
		if (fontParams != null) {
			String fcolor = fontParams.getString ("color");
			String fname = fontParams.getString ("name");
			String fstyle = fontParams.getString ("style");
			Integer fsize = fontParams.getInt ("size");
			int fintstyle = Font.PLAIN;
			int fintcolor = 0x000000;
			//Set defaults if null
			if (fcolor == null) {
				fcolor = "0x000000"; //Redundant
				fintcolor = 0x000000;
			} else {
				fintcolor = Integer.parseInt (fcolor, 16); //Parse out hex color
			}
			if (fname == null) {
				fname = "Arial";
			}
			if (fsize == null) {
				fsize = 12;
			}
			if (fstyle != null) {
				switch (fstyle) {
					case "bold":
						fintstyle = Font.BOLD;
						break;
					case "italic":
						fintstyle = Font.ITALIC;
						break;
				}
			}
			
			//Set font
			Font f = new Font (fname, fintstyle, fsize);
			g.setFont (f);
			
			//Set font color
			Color c = new Color (fintcolor);
			g.setColor (c);
			
		} else {
			Font f = new Font ("Arial", Font.PLAIN, 12);
			g.setFont (f);
		}
		
		//Draw text
		if (prevLength != text.getText ().length ()) {
			MainLoop.getWindow ().playSound ("resources/sounds/letter_trimmed.wav");
		}
		prevLength = text.getText ().length ();
		g.drawString (text.getText (), params.getInt ("xPos"), params.getInt ("yPos"));
	}
	
	public void setupTextGenerator () {
		JSONObject params = getArgs ().getJSONObject ("params");
		Object rawText = params.get ("text");
		if (rawText instanceof String) {
			text = new SetTextGenerator ();
			((SetTextGenerator)text).setText ((String)rawText); //Use the String
		} else if (rawText instanceof JSONObject) {
			JSONObject textObj = (JSONObject)rawText; //Parse out the TextGenerator object
			String className = "cutscenes." + textObj.getString ("type");
			try {
				Class<?> textGenClass = Class.forName (className);
				Constructor<?> textGenConstructor = textGenClass.getConstructors ()[0];
				text = (TextGenerator)textGenConstructor.newInstance ();
				text.setParams (textObj);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
