package gameObjects;

import java.awt.Color;
import java.util.ArrayList;

import enemies.Enemy;
import main.GameObject;
import main.MainLoop;
import map.CollisionMesh;
import vector.Vector2D;
import visualEffects.ParticleMaker;

public class Player extends GameObject {

	private ParticleMaker playerEffect;
	
	private CollisionMesh walls;
	
	private Attack currentAttack;
	
	public Player () {
		
		setPersistent (true);
		createHitbox (-8, -8, 16, 16);
		
		initParticleMaker ();
		
	}
	
	public void initParticleMaker () {
		
		//Make the particle maker
		playerEffect = new ParticleMaker ();
		
		//Set the colors
		playerEffect.setColor1 (new Color (0x00, 0xFF, 0xFF, 0xC0));
		playerEffect.setColor2 (new Color (0x00, 0x00, 0xFF, 0x80));
		
		//Set the size parameters
		playerEffect.setMinSize (3);
		playerEffect.setMaxSize (5);
		
		//Set the motion parameters
		playerEffect.setMinAng (0);
		playerEffect.setMaxAng (2 * Math.PI);
		playerEffect.setSpeed (1);
		
		//Set the decay parameters
		playerEffect.setMinLifespan (5);
		playerEffect.setMaxLifespan (10);
		
	}
	
	public void setCollisionMesh (CollisionMesh walls) {
		
		this.walls = walls;
		
	}
	
	@Override
	public void frameEvent () {
		
		//Get mouse coords relative to the play area
		double mouseX = getMouseX () + getRoom ().getViewX ();
		double mouseY = getMouseY () + getRoom ().getViewY ();
		
		//Move towards the cursor
		Vector2D v = new Vector2D (mouseX - getX (), mouseY - getY ());
		v.normalize ();
		v.scale (5);
		move (v);
		
		//Check for collision with walls
		if (walls != null && walls.isColliding (this)) {
			backstep ();
		}
		
		//Check for collision with enemies
		Enemy targeted = null;
		ArrayList<GameObject> enemies = MainLoop.getObjectMatrix ().getAll (Enemy.class);
		for (int i = 0; i < enemies.size (); i++) {
			Enemy curr = (Enemy)enemies.get (i);
			Vector2D offs = new Vector2D (getCenterX () - curr.getCenterX (), getCenterY () - curr.getCenterY ());
			if (offs.getLength () < curr.getSize ()) {
				
				//Move the player outside of the enemy
				offs.normalize ();
				offs.scale (curr.getSize ());
				setPositionByCenter (curr.getCenterX (), curr.getCenterY ());
				move (offs);
				
				//Allow for attacking
				targeted = curr;
				
			}
		}
		
		//Attack the enemy
		if (targeted != null && (currentAttack == null || !currentAttack.isDeclared ())) {
			if (this.mouseButtonReleased (1)) {
				currentAttack = new RegularAttack (targeted);
				currentAttack.declare (targeted.getX (), targeted.getY ());
			}
		}
		
	}
	
	@Override
	public void draw () {
		
		//Make 3 particles
		for (int i = 0; i < 3; i++) {
			
			//Get offset within a circle to make the particle
			double randomDir = Math.random () * Math.PI * 2;
			double randomMgt = Math.random () * 10;
			int xoff = (int)(Math.cos (randomDir) * randomMgt);
			int yoff = (int)(Math.sin (randomDir) * randomMgt);
			
			//Make the particle
			playerEffect.makeParticle ((int)getX () + xoff, (int)getY () + yoff);
			
		}
		
	}
	
}
