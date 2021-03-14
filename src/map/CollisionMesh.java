package map;

import java.util.ArrayList;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import json.JSONUtil;
import main.GameObject;
import main.Hitbox;

public class CollisionMesh {

	private ArrayList<WallSegment> walls;
	
	public CollisionMesh (String filename) {
		
		//Load the JSON file
		try {
			JSONObject wallData = JSONUtil.loadJSONFile (filename);
			JSONArray wallArr = wallData.getJSONArray ("walls");
			walls = new ArrayList<WallSegment> ();
			for (int i = 0; i < wallArr.getContents ().size (); i++) {
				walls.add (wallSegFromJSONArray ((JSONArray)wallArr.get (i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static WallSegment wallSegFromJSONArray (JSONArray obj) {
		int xfrom = (Integer)obj.get (0);
		int xto = (Integer)obj.get (1);
		int yfrom = (Integer)obj.get (2);
		int yto = (Integer)obj.get (3);
		return new WallSegment (xfrom, xto, yfrom, yto);
	}
	
	public boolean isColliding (GameObject obj) {
		Hitbox h = obj.getHitbox ();
		for (int i = 0; i < walls.size (); i++) {
			if (walls.get (i).collidesWith (h)) {
				return true;
			}
		}
		return false;
	}
	
}