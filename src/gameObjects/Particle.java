package gameObjects;

import java.awt.Color;

import main.GameObject;
import main.MainLoop;

public class Particle extends GameObject {
	
	public double FADE_START_POINT = .4;
	
	public Color color;
	public int size;
	public double durability;
	public double initialDurability;
	public double direction;
	public double vx;
	public double vy;
	public double speed;
	public Particle (double x, double y, Color color, int size, double durability) {
		this.declare (x, y);
		this.color = color;
		this.size = size;
		this.durability = durability;
		this.initialDurability = durability;
		this.direction = 0;
		this.speed = 0;
		computeVectors ();
	}
	public Particle (double x, double y, Color color, int size, double durability, double direction, double speed) {
		this.declare (x, y);
		this.color = color;
		this.size = size;
		this.durability = durability;
		this.initialDurability = durability;
		this.direction = direction;
		this.speed = speed;
		computeVectors ();
	}
	public Particle (double x, double y, Color color, int size, double durability, double direction, double speed, double randomDecayProbability) {
		this.declare (x, y);
		this.color = color;
		this.size = size;
		this.durability = durability - Math.random () * durability * randomDecayProbability;
		this.initialDurability = this.durability;
		this.direction = direction;
		this.speed = speed;
		computeVectors ();
	}
	@Override
	public void frameEvent () {
		if (durability <= 0) {
			this.forget ();
		}
		durability -= 1;
		if (speed != 0) {
			this.setX (this.getX () + vx);
			this.setY (this.getY () - vy);
		}
	}
	@Override
	public void draw () {
		int fadePoint = (int)((double)(FADE_START_POINT) * initialDurability);
		Color c = this.color;
		if (durability < fadePoint) {
			double percentFade = ((double)durability / fadePoint);
			c = new Color (color.getRed (), color.getGreen (), color.getBlue (), (int)(color.getAlpha () * percentFade));
		}
		MainLoop.getWindow ().getBufferGraphics ().setColor (c);
		MainLoop.getWindow ().getBufferGraphics ().fillRect ((int)this.getX () - getRoom ().getViewX (), (int)this.getY () - getRoom ().getViewY (), size, size);
	}
	public void computeVectors () {
		vx = speed * Math.cos (direction);
		vy = speed * Math.sin (direction);
	}
}
