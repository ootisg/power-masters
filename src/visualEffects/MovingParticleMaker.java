package visualEffects;

import java.awt.Color;

public abstract class MovingParticleMaker {

	private double minAng;
	private double maxAng;
	private double minSpeed;
	private double maxSpeed;
	private int minLife;
	private int maxLife;
	private Color color1;
	private Color color2;
	
	public double getMinAng () {
		return minAng;
	}
	
	public double getMaxAng () {
		return maxAng;
	}
	
	public double getMinSpeed () {
		return minSpeed;
	}
	
	public double getMaxSpeed () {
		return maxSpeed;
	}
	
	public int getMinLifespan () {
		return minLife;
	}
	
	public int getMaxLifespan () {
		return maxLife;
	}
	
	public Color getColor1 () {
		return color1;
	}
	
	public Color getColor2 () {
		return color2;
	}

	public void setMinAng (double ang) {
		minAng = ang;
	}
	
	public void setMaxAng (double ang) {
		maxAng = ang;
	}
	
	public void setAng (double ang) {
		minAng = ang;
		maxAng = ang;
	}
	
	public void setMinSpeed (double speed) {
		minSpeed = speed;
	}
	
	public void setMaxSpeed (double speed) {
		maxSpeed = speed;
	}
	
	public void setSpeed (double speed) {
		minSpeed = speed;
		maxSpeed = speed;
	}
	
	public void setMinLifespan (int frames) {
		minLife = frames;
	}
	
	public void setMaxLifespan (int frames) {
		maxLife = frames;
	}
	
	public void setLifespan (int frames) {
		minLife = frames;
		maxLife = frames;
	}
	
	public void setColor1 (Color c) {
		color1 = c;
	}
	
	public void setColor2 (Color c) {
		color2 = c;
	}
	
	public void setColor (Color c) {
		color1 = c;
		color2 = color1;
	}
	
}
