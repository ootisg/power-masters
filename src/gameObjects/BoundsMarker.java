package gameObjects;

import java.awt.Rectangle;
import java.util.HashMap;

import main.GameObject;

public class BoundsMarker extends GameObject {
	
	private static String roomId;
	private static HashMap<String, BoundsMarker> markers;
	
	private static HashMap<String, HashMap<String, Rectangle>> reigons;
	
	public BoundsMarker () {
		super ();
	}
	
	@Override
	public void onDeclare () {
		
		//Reset for new room if needed
		String currRoomId = getRoom ().getRoomName ();
		if (!currRoomId.equals (roomId)) {
			roomId = currRoomId;
			markers = new HashMap<String, BoundsMarker> ();
			reigons = new HashMap<String, HashMap<String, Rectangle>> ();
		}
		
		//Get marker type
		String markerType = getVariantAttribute ("Type");
		if (markerType == null) {
			forget ();
			return;
		}
		
		//Get marker name
		String markerName = getVariantAttribute ("Name");
		if (markerName == null) {
			markerName = "NULL";
		}
		
		//Pair with same ID
		String markerId = markerType + ":" + markerName;
		BoundsMarker sameId = markers.get (markerId);
		if (sameId == null) {
			markers.put (markerId, this);
			return;
		}
		
		//Find the corners
		int tlx;
		int tly;
		int brx;
		int bry;
		if (sameId.getX () < this.getX ()) {
			tlx = (int)(sameId.getX () / 16);
			tly = (int)(sameId.getY () / 16);
			brx = (int)(this.getX () / 16) + 1;
			bry = (int)(this.getY () / 16) + 1;
		} else {
			tlx = (int)(this.getX () / 16);
			tly = (int)(this.getY () / 16);
			brx = (int)(sameId.getX () / 16) + 1;
			bry = (int)(sameId.getY () / 16) + 1;
		}
		Rectangle bounds = new Rectangle (tlx, tly, brx - tlx, bry - tly);
		
		//Init map for the type if not already present
		HashMap<String, Rectangle> typeMap = reigons.get (markerType);
		if (typeMap == null) {
			typeMap = new HashMap<String, Rectangle> ();
			reigons.put (markerType, typeMap);
		}
		
		//Insert bounds
		typeMap.put (markerName, bounds);
	}
	
	public static HashMap<String, Rectangle> getAllByType (String type) {
		return reigons.get (type);
	}
	
	public static Rectangle getReigonByType (String type) {
		return reigons.get (type).get ("NULL");
	}
	
	public static Rectangle getReigon (String type, String name) {
		return reigons.get (type).get (name);
	}

}
