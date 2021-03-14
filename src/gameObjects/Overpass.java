package gameObjects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import main.GameObject;
import main.MainLoop;
import resources.Sprite;

public class Overpass extends GameObject {
	
	public static final float MAX_OPACITY = 1.0f;
	public static final float MIN_OPACITY = 0.25f;
	public static final float STEP_OPACITY = 0.1f;
	
	private static String roomId;
	private static HashMap<String, Overpass> overpasses;
	
	private BufferedImage overlay;
	private boolean[][] collisionMap;
	
	private float opacity = MAX_OPACITY;
	private boolean redraw = true;
	
	public Overpass () {
		super ();
		this.setPriority (-1);
	}
	
	@Override
	public void onDeclare () {
		
		//Reset for new room if needed
		String currRoomId = getRoom ().getRoomName ();
		if (!currRoomId.equals (roomId)) {
			roomId = currRoomId;
			overpasses = new HashMap<String, Overpass> ();
		}
		
		//Get overpass ID
		String overpassId = getVariantAttribute ("id");
		if (overpassId == null) {
			forget ();
			return;
		}
		
		//Pair with same ID
		Overpass sameId = overpasses.get (overpassId);
		if (sameId == null) {
			overpasses.put (overpassId, this);
			return;
		}
		
		//Find the corners
		Overpass tlOverpass;
		int tlx;
		int tly;
		int brx;
		int bry;
		if (sameId.getX () < this.getX ()) {
			tlOverpass = sameId;
			tlx = (int)(sameId.getX () / 16);
			tly = (int)(sameId.getY () / 16);
			brx = (int)(this.getX () / 16) + 1;
			bry = (int)(this.getY () / 16) + 1;
		} else {
			tlOverpass = this;
			tlx = (int)(this.getX () / 16);
			tly = (int)(this.getY () / 16);
			brx = (int)(sameId.getX () / 16) + 1;
			bry = (int)(sameId.getY () / 16) + 1;
		}
		
		//Set up the overpass sprite and collision
		int rwidth = brx - tlx;
		int rheight = bry - tly;
		tlOverpass.createHitbox (0, 0, rwidth * 16, rheight * 16);
		tlOverpass.collisionMap = new boolean [rheight][rwidth];
		tlOverpass.overlay = new BufferedImage (rwidth * 16, rheight * 16, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = tlOverpass.overlay.getGraphics ();
		
		//Delete the tiles within the bounds
		for (int wy = 0; wy < rheight; wy++) {
			for (int wx = 0; wx < rwidth; wx++) {
				Sprite tileSprite = getRoom ().getTileIcon (getRoom ().getTileId (tlx + wx, tly + wy));
				if (tileSprite != null) {
					tlOverpass.collisionMap [wy][wx] = true;
					g.drawImage (tileSprite.getImageArray ()[0], wx * 16, wy * 16, null);
				} else {
					tlOverpass.collisionMap [wy][wx] = false;
				}
				getRoom ().setTile (0, tlx + wx, tly + wy, getRoom ().getTileId (tlx + wx, tly + wy, 1)); //Copy layer 1 to layer 0
				getRoom ().setTile (0, tlx + wx, tly + wy, (short)0); //Remove layer 1
			}
		}
	}
	
	@Override
	public void forget () {
		overpasses.remove (getVariantAttribute ("id"));
		super.forget ();
	}
	
	@Override
	public void frameEvent () {
		
		//Change the opacity if the player is under the overpass
		double prevOpacity = opacity;
		if (isCollidingWithTiles (getPlayer ())) {
			opacity -= STEP_OPACITY;
		} else {
			opacity += STEP_OPACITY;
		}
		
		//Bind the opacity
		if (opacity > MAX_OPACITY) {
			opacity = MAX_OPACITY;
		}
		if (opacity < MIN_OPACITY) {
			opacity = MIN_OPACITY;
		}
		
		//Set redraw if applicable
		if (prevOpacity != opacity) {
			redraw = true;
		}
		
	}
	
	@Override
	public void draw () {
		if (overlay != null) {
			//Only the top-left overpass gets to draw stuff
			if (redraw) {
				//Change the transparency
				BufferedImage postOverlay = new BufferedImage (overlay.getWidth (), overlay.getHeight (), BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D g = (Graphics2D)postOverlay.createGraphics ();
				g.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_OVER, opacity));
				g.drawImage (overlay, 0, 0, null);
				setSprite (new Sprite (postOverlay));
				redraw = false;
			}
			super.draw ();
		}
	}
	
	public boolean isCollidingWithTiles (GameObject obj) {
		
		//Check tiles for collision
		if (isColliding (obj)) {
			int startX = (int)((obj.getX () - this.getX ()) / 16);
			int startY = (int)((obj.getY () - this.getY ()) / 16);
			int endX = (int)((obj.getX () - this.getX () + obj.getHitbox ().width) / 16);
			int endY = (int)((obj.getY () - this.getY () + obj.getHitbox ().height) / 16);
			for (int wx = startX; wx <= endX; wx++) {
				for (int wy = startY; wy <= endY; wy++) {
					try {
						if (collisionMap [wy][wx]) {
							return true;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						//Do nothing and continue
					}
				}
			}
		}
		
		//Return false for no collision
		return false;
		
	}
	
}
