package map;

import java.util.ArrayList;
import java.util.LinkedList;

import main.GameAPI;
import main.GameObject;
import main.MainLoop;

public class MapUnit extends GameObject {
	
	private short[][][] tileData;
	private LinkedList<GameObject> objectData;
	
	private String unitName;
	
	public MapUnit (String name, int xfrom, int yfrom, int xto, int yto) {
		//Coordinates are in tiles
		unitName = name;
		Room room = GameAPI.getRoom ();
		objectData = new LinkedList<GameObject> ();
		tileData = room.getTileIds (xfrom, yfrom, xto - xfrom + 1, yto - yfrom + 1);
		short transparentTile = room.getNumericalId ("transparent.png:0");
		for (int l = 0; l < tileData.length; l ++) {
			for (int wx = xfrom; wx <= xto; wx ++) {
				for (int wy = yfrom; wy <= yto; wy ++) {
					room.setTile (l, wx, wy, transparentTile); 
				}
			}
		}
		setX (xfrom * 16);
		setY (yfrom * 16);
		createHitbox (0, 0, (xto - xfrom) * 16, (yto - yfrom) * 16);
		ArrayList<GameObject> objs = MainLoop.getObjectMatrix ().getAll ();
		for (int i = 0; i < objs.size (); i ++) {
			if (isColliding (objs.get (i))) {
				objectData.add (objs.get (i));
				objs.get (i).forget ();
			}
		}
		put (xfrom, yfrom);
	}
	
	public boolean put (int x, int y) {
		Room room = GameAPI.getRoom ();
		for (int i = 0; i < objectData.size (); i ++) {
			try {
				GameObject o = objectData.get (i).getClass ().newInstance ();
				o.declare (objectData.get (i).getX (), objectData.get (i).getY ());
				o.setVariantData (objectData.get (i).getVariantData ());
			} catch (InstantiationException | IllegalAccessException e) {
				return false;
			}
		}
		if (x >= 0 && y >= 0 && x + tileData[0].length < room.getWidth () && y + tileData[0][0].length < room.getHeight ()) {
			for (int layer = 0; layer < tileData.length; layer ++) {
				for (int wx = 0; wx < tileData[0].length; wx ++) {
					for (int wy = 0; wy < tileData [0][0].length; wy ++) {
						room.setTile (layer, x + wx, y + wy, tileData [layer][wx][wy]);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public String getName () {
		return unitName;
	}
	
}
