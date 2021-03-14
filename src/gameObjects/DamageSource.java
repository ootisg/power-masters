package gameObjects;

/**
 * An interface for objects that can inflict damage
 * @author nathan
 *
 */
public interface DamageSource {
	
	/**
	 * Gets the base damage of this DamageSoruce
	 * @return The base damage this object deals
	 */
	public double getBaseDamage ();
	
}