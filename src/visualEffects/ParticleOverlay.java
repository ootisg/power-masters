package visualEffects;

import java.awt.Color;

import gameObjects.Particle;
import json.JSONObject;
import main.MainLoop;

public class ParticleOverlay extends ScreenOverlay {
	
	private ParticleMaker maker;
	
	public ParticleOverlay () {
		this (null);
		this.setIgnorePause (true);
	}
	
	public ParticleOverlay (ParticleMaker maker) {
		this.maker = new ParticleMaker ();
		this.maker.setMinAng (Math.PI / 3);
		this.maker.setMaxAng (Math.PI * 2 / 3);
		this.maker.setMinSpeed (1);
		this.maker.setMaxSpeed (2);
		this.maker.setMinSize (1);
		this.maker.setMaxSize (3);
		this.maker.setColor1 (new Color (0x600040));
		this.maker.setColor2 (new Color (0x000000));
	}
	
	@Override
	public void frameEvent () {
		int[] res = MainLoop.getWindow ().getResolution ();
		for (int i = 0; i < 10; i++) {
			int x = (int)(res[0] * Math.random ());
			int y = (int)(res[1] * Math.random ());
			Particle p = maker.makeParticle (getRoom ().getViewX () + x, getRoom ().getViewY () + y);
		}
	}
	
	@Override
	public void draw () {
		//Do nothing
	}

	/**
	 * JSON Formatting
	 */
	@Override
	public void setProperties (JSONObject properties) {
		Object minAng = properties.get ("minAng");
		Object maxAng = properties.get ("maxAng");
		Object minSpeed = properties.get ("minSpeed");
		Object maxSpeed = properties.get ("maxSpeed");
		Object minSize = properties.get ("minSize");
		Object maxSize = properties.get ("maxSize");
		Object color1 = properties.get ("color1");
		Object color2 = properties.get ("color2");
		maker = new ParticleMaker ();
		maker.setMinAng ((double)minAng);
		maker.setMaxAng ((double)maxAng);
		maker.setMinSpeed ((double)minSpeed);
		maker.setMaxSpeed ((double)maxSpeed);
		maker.setMinSize ((int)minSize);
		maker.setMaxSize ((int)maxSize);
		maker.setColor1 (new Color ((int)color1));
		maker.setColor2 (new Color ((int)color2));
		maker.setParticlesIgnorePause (true);
	}
	

	
}
