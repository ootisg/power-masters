package gui;

import java.awt.Color;
import java.awt.Graphics;

import main.GameObject;
import resources.Sprite;

public abstract class GuiBar extends GameObject {

	public static Sprite barOutline = new Sprite ("resources/sprites/health_bar.png");
	
	private double fillAmt = 1;
	private double fillMax = 1;
	
	private ColorMap colors;
	
	protected GuiBar (ColorMap colors) {
		this.colors = colors;
		setSprite (barOutline);
	}
	
	public double getFillAmt () {
		return fillAmt;
	}
	
	public double getMaxFill () {
		return fillMax;
	}
	
	public void setFillAmt (double amount) {
		fillAmt = amount;
	}
	
	public void setMaxFill (double amount) {
		fillMax = amount;
	}
	
	public void setColorMap (ColorMap map) {
		colors = map;
	}
	
	@Override
	public void draw () {
		
		//Calculate the percent fill
		double fillPercent = fillAmt / fillMax;
		
		//Get the color to use
		Color c = colors.getColor (fillPercent);
		
		//Draw the bar
		getSprite ().draw ((int)getX (), (int)getY ());
		int barWidth = 88;
		int barPixels = barWidth - (int)(barWidth * fillPercent);
		Graphics g = getWindow ().getBufferGraphics ();
		g.setColor (c);
		g.fillRect ((int)getX () + 4 + barPixels, (int)getY () + 4, barWidth - barPixels, 8);
		
	}
	
}
