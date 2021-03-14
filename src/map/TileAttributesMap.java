package map;

import java.util.HashMap;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import json.JSONUtil;

public class TileAttributesMap {
	//A container class for all TileData objects
	
	private HashMap<String, TileData> tileData;
	
	public static JSONObject defaultTileData;
	
	public TileAttributesMap () {
		tileData = new HashMap<String, TileData> ();
		try {
			defaultTileData = JSONUtil.loadJSONFile ("resources/tilesets/config/default.json");
		} catch (JSONException e) {
			e.printStackTrace();
			System.exit (1); //Should never happen
		}
	}
	
	public TileData getTile (String tileId) {
		if (tileData.containsKey (tileId)) {
			return tileData.get (tileId);
		} else {
			try {
				loadTilesetData (tileId);
			} catch (JSONException e) {
				TileData defaultData = new TileData (tileId, defaultTileData);
				tileData.put (tileId, defaultData);
			}
			return tileData.get (tileId);
		}
	}
	
	public void loadTilesetData (String tileId) throws JSONException {
		
		//Get the properties file
		if (tileId.equals ("_NULL")) {
			throw new JSONException ("Exception abuse");
		}
		String tilesetId = tileId.split (":")[0];
		String configPath = "resources/tilesets/config/" + tilesetId.split ("\\.")[0] + ".json";
		JSONObject properties = JSONUtil.loadJSONFile (configPath);
		
		//Get important attributes
		int numTiles = properties.getInt ("count");
		JSONObject defaults = properties.getJSONObject ("defaults");
		JSONObject tileProps = properties.getJSONObject ("tiles");
		
		//Revert to global defaults if defaults are not present
		if (defaults == null) {
			defaults = defaultTileData;
		}
		
		//Parse out the tile data into TileData objects
		for (int i = 0; i < numTiles; i++) {
			if (tileProps != null) {
				
				//Grab the current tile
				JSONObject currTile = (JSONObject)tileProps.get (String.valueOf (i));
				String newId = tilesetId + ":" + i;
				
				//Parse the tile's data
				if (currTile != null) {
					//Data was found for the tile, parse it
					TileData newData = new TileData (newId, currTile);
					tileData.put (newId, newData);
				} else {
					//Data was not found, use defaults
					TileData newData = new TileData (newId, defaults);
					tileData.put (newId, newData);
				}
			} else {
				//Data was not found, use defaults
				String newId = tilesetId + ":" + i;
				TileData newData = new TileData (newId, defaults);
				tileData.put (newId, newData);
			}
		}
	}
}