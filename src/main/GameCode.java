package main;

import java.io.FileNotFoundException;

import gameObjects.GlobalSave;
import gameObjects.Player;
import gameObjects.TestObj;
import gui.MapScreen;
import gui.MapUi;
import gui.TitleScreen;
import map.CollisionMesh;
import map.WallSegment;
import music.MusicPlayer;

public class GameCode extends GameAPI {
	private GameWindow gameWindow;
	public void initialize () {
		//Set the save file path
		getSave ().setFile ("saves/save.txt");
		//Create the global save data
		new GlobalSave ().declare (0, 0);
		//Make the music player so music can be played
		new MusicPlayer ();
		MainLoop.getWindow ().setResolution (1024, 576);
		MainLoop.getWindow ().setSize (1024, 576);
		
		//Load the room
		try {
			getRoom ().loadRMF ("resources/maps/map_1.rmf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Make the gui components
		getGui ().addComponent (new MapUi ());
		
		//Make the player
		Player p = new Player ();
		p.setCollisionMesh (new CollisionMesh ("resources/collision/test_walls.json"));
		p.declare (64, 64);
		
	}
	
	public void gameLoop () {
		getRoom ().frameEvent ();
		//Saveable.printSaves ();
		//Runs once per frame
	}
}