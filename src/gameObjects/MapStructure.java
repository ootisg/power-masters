package gameObjects;

import java.util.ArrayList;
import java.util.Arrays;

import main.GameObject;
import main.MainLoop;
import map.MapUnit;
import map.RoomLoadedEvent;

public class MapStructure extends GameObject implements RoomLoadedEvent {

	@Override
	public void onRoomLoaded () {
		String structId = getVariantAttribute ("id");
		if (structId != null) {
			ArrayList<GameObject> mapStructs = MainLoop.getObjectMatrix ().getObjects ("gameObjects.MapStructure");
			for (int i = 0; i < mapStructs.size (); i ++) {
				if (mapStructs.get (i) != null && mapStructs.get (i) != this && structId.equals (mapStructs.get (i).getVariantAttribute ("id"))) {
					getRoom ().addMapUnit (getMapUnit (this, mapStructs.get (i)));
					mapStructs.get (i).forget ();
					forget ();
				}
			}
		}
	}
	
	private MapUnit getMapUnit (GameObject bound1, GameObject bound2) {
		int[] xvals = new int[] {(int)(bound1.getX () / 16), (int)(bound2.getX () / 16)};
		int[] yvals = new int[] {(int)(bound1.getY () / 16), (int)(bound2.getY () / 16)};
		Arrays.sort (xvals);
		Arrays.sort (yvals);
		return new MapUnit (bound1.getVariantAttribute ("id"), xvals [0], yvals [0], xvals [1], yvals [1]);
	}
}
