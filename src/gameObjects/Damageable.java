package gameObjects;

/**
 * An interface for any object that has health and can be damaged
 * @author nathan
 *
 */

public interface Damageable {
	
	/**
	 * Effects of attacks that do not directly affect health.
	 * @param inflictor The object that triggered the damageEvent method
	 */
	void damageEvent (DamageSource source);
	
	/**
	 * Reduces the health of this object by the given value.
	 * @param amount The amount of damage to be dealt
	 */
	void damage (double amount);
	
	/**
	 * Gets the health of this object.
	 * @return The health of the object
	 */
	double getHealth ();
	
	/**
	 * Gets the max health of this object.
	 * @return The max health of this object
	 */
	double getMaxHealth ();
	
	/**
	 * Sets the health of this object to the given value.
	 * @param health The new health of this object
	 */
	void setHealth (double health);

	/**
	 * Sets the max health of this object to the given value.
	 * @param health The new max health of this object
	 */
	void setMaxHealth (double health);
}