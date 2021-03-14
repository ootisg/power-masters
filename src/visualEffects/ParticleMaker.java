package visualEffects;

import java.awt.Color;

import gameObjects.Particle;

public class ParticleMaker {

	private double minAng = 0;
	private double maxAng = Math.PI * 2;
	private double minSpeed = 0;
	private double maxSpeed = 0;
	private int minLife = 30;
	private int maxLife = 30;
	private int minSize = 1;
	private int maxSize = 1;
	private Color color1 = new Color (0x000000);
	private Color color2 = new Color (0x000000);
	private boolean useWhilePaused = false;
	
	public Particle makeParticle (int x, int y) {
		
		//Generate motion/decay values
		double ang = randomBetween (minAng, maxAng);
		double speed = randomBetween (minSpeed, maxSpeed);
		int life = randomBetweenInt (minLife, maxLife);
		
		//Generate the particle size
		int size = randomBetweenInt (minSize, maxSize);
		
		//Generate the particle color
		int red = randomBetweenInt (color1.getRed (), color2.getRed ());
		int green = randomBetweenInt (color1.getGreen (), color2.getGreen ());
		int blue = randomBetweenInt (color1.getBlue (), color2.getBlue ());
		int alpha = randomBetweenInt (color1.getAlpha (), color2.getAlpha ());
		Color color = new Color (red, green, blue, alpha);
		
		//Generate the particle
		Particle p = new Particle (x, y, color, size, life, ang, speed);
		p.setIgnorePause (useWhilePaused);
		return p;
		
	}
	
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
	
	public int getMinSize () {
		return minSize;
	}
	
	public int getMaxSize () {
		return maxSize;
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
	
	public void setMinSize (int size) {
		minSize = size;
	}
	
	public void setMaxSize (int size) {
		maxSize = size;
	}
	
	public void setSize (int size) {
		minSize = size;
		maxSize = size;
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
	
	public void setParticlesIgnorePause (boolean ignorePause) {
		useWhilePaused = ignorePause;
	}
	
	private double randomBetween (double a, double b) {
		return a + (b - a) * Math.random ();
	}
	
	private int randomBetweenInt (int a, int b) {
		return a + (int)((b + 1 - a) * Math.random ());
	}
	
}
