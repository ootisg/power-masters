package gameObjects;

import java.util.HashMap;

import main.GameObject;

public class GameObjectLoader {
	
	public static String[] packages = {
			"gameObjects",
			"puzzle"
	};
	public static HashMap<String, Class<?>> classMap = new HashMap<String, Class<?>> ();
	
	public static Class<?> getClass (String className) {
		if (classMap.containsKey (className)) {
			return classMap.get (className);
		} else {
			for (int i = 0; i < packages.length; i ++) {
				Class<?> toAdd = null;
				try {
					toAdd = Class.forName (packages [i] + "." + className);
				} catch (ClassNotFoundException e) {
					//Do nothing
				}
				if (toAdd != null) {
					classMap.put (className, toAdd);
					return toAdd;
				}
			}
		}
		return null;
	}
	
	public static GameObject newInstance (String className) {
		Class<?> addClass = getClass (className);
		try {
			return (GameObject)(addClass.newInstance ());
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
}
