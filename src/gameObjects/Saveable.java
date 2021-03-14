package gameObjects;

import java.util.ArrayList;

import main.GameObject;

public abstract class Saveable extends GameObject {
	
	private static ArrayList<String> saveData = new ArrayList<String> ();
	
	protected Saveable () {
		super ();
	}
	
	public void save (String data, String roomName) {
		getSave ().save (roomName, getSaveId (), data);
	}
	
	public void save (String data) {
		save (data, getRoom ().getRoomName ());
	}
	
	protected String getSaveData (String roomName) {
		return getSave ().getSaveData (roomName, getSaveId ());
	}
	
	protected String getSaveData () {
		return getSaveData (getRoom ().getRoomName ());
	}
	
	public String getSaveId () {
		return (int)getStartPos ()[0] + "," + (int)getStartPos ()[1];
	}
	
	public abstract void load ();
}
