package map;

import json.JSONObject;

public class TileData {
	//Container class for tile metadata
	
	private String name;
	private boolean isSolid;
	
	public TileData (String name, JSONObject args) {
		//Name is in the format [tileset name].[position in tileset]
		this.name = name;
		this.isSolid = (boolean)args.get ("solid");
	}
	
	public String getName () {
		return name;
	}
	
	public boolean isSolid () {
		return isSolid;
	}
}