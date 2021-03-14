package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import gameObjects.GameObjectLoader;
import main.GameWindow;
import main.GameAPI;
import main.GameObject;
import main.Hitbox;
import main.MainLoop;
import main.ObjectMatrix;
import music.MusicPlayer;
import resources.Sprite;
import resources.Spritesheet;

public class Room {
	
	public static int TILE_SOURCE_SIZE = 16;
	public static int TILE_SIZE = 8;
	public static final int MAX_COLLISION_STEPS = 1000;
	
	private String roomName;
	private Sprite[] tileList;
	private String[] tileIdList;
	private String[] objectList;
	private short[][][] tileData;
	private boolean[] collisionData;
	private double gravity = .65625;
	private int levelWidth;
	private int levelHeight;
	private int viewX;
	private int viewY;
	private int readBit;
	private byte[] inData;
	private static double[] hitboxCorners = new double[] {0, 0, 1, 0, 1, 1, 0, 1, 0, 0};
	private TileAttributesMap tileAttributesList;
	private ArrayList<Background> backgroundList;
	private HashMap<String, MapUnit> mapUnits;
	public TileBuffer tileBuffer = new TileBuffer ();
	public Room () {
		//A fairly generic constructor
		tileAttributesList = new TileAttributesMap ();
		tileData = new short[1][32][32];
		levelWidth = 32;
		levelHeight = 32;
		viewX = 0;
		viewY = 0;
		readBit = 0;
		backgroundList = new ArrayList<Background> ();
		mapUnits = new HashMap<String, MapUnit> ();
	}
	private int readBits (int num) {
		//Reads a number of bits from the byte[] inData equal to num and returns them as an int
		int result = 0;
		int mask;
		while (num > 0) {
			if (num >= 8 - (readBit % 8)) {
				int numbits = 8 - (readBit % 8);
				mask = (1 << numbits) - 1;
				result = result + ((inData [readBit >> 3] & mask) << (num - numbits));
				num = num - numbits;
				readBit += numbits;
			} else {
				mask = ((1 << num) - 1) << (8 - (readBit % 8) - num);
				result = result + ((inData [readBit >> 3] & mask) >> (8 - (readBit % 8) - num));
				readBit += num;
				num = 0;
			}
		}
		return result;
	}
	public char readChar () {
		return (char)readBits (8);
	}
	public String readChars (int numChars) {
		String out = "";
		for (int i = 0; i < numChars; i++) {
			out += readChar ();
		}
		return out;
	}
	public int readInt () {
		return readBits (32);
	}
	public String readTo (char c) {
		char current;
		String out = "";
		while (true) {
			current = readChar ();
			if (current != c) {
				out += current;
			} else {
				break;
			}
		}
		return out;
	}
	public boolean isColliding (double x1, double y1, double x2, double y2) {
		if (getCollidingCoords (x1, y1, x2, y2) != null) {
			return true;
		}
		return false;
	}
	public double[] getCollidingCoords (double x1, double y1, double x2, double y2) {
		if (x1 < 0 || x1 > levelWidth * TILE_SIZE || x2 < 0 || x2 > levelWidth * TILE_SIZE || y1 < 0 || y1 > levelHeight * TILE_SIZE || y2 < 0 || y2 > levelHeight * TILE_SIZE) {
			return null;
		}
		int xdir = 1;
		int ydir = 1;
		double xcheck1 = 0;
		double ycheck1 = 0;
		double xcheck2 = 0;
		double ycheck2 = 0;
		double xstep = x1;
		double ystep = y1;
		byte tileXOffset = 0;
		byte tileYOffset = 0;
		if (x1 > x2) {
			xdir = -1;
		}
		if (y1 > y2) {
			ydir = -1;
		}
		if (collisionData [getTileId ((int) x1 / TILE_SIZE, (int) y1 / TILE_SIZE)]) {
			return new double[] {x1, y1};
		}
		int steps;
		if (x1 == x2) {
			steps = 0;
			while (steps < MAX_COLLISION_STEPS) {
				tileYOffset = 0;
				ystep = snapTile (ystep, ydir);
				if (ydir == -1 && ystep % TILE_SIZE == 0) {
					tileYOffset = -1;
				}
				if (!isBetween (ystep, y1, y2)) {
					return null;
				}
				if (collisionData [getTileId ((int) x1 / TILE_SIZE, (int) ystep / TILE_SIZE + tileYOffset)]) {
					return new double[] {x1, ystep};
				}
				steps ++;
			}
		}

		if (y1 == y2) {
			steps = 0;
			while (steps < MAX_COLLISION_STEPS) {
				tileXOffset = 0;
				xstep = snapTile (xstep, xdir);
				if (xdir == -1 && xstep % TILE_SIZE == 0) {
					tileXOffset = -1;
				}
				if (!isBetween (xstep, x1, x2)) {
					return null;
				}
				if (collisionData [getTileId ((int) x1 / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE)]) {
					return new double[] {xstep, y1};
				}
				steps ++;
			}
		}
		double m = (y1 - y2) / (x1 - x2);
		double b = y1 - m * x1;
		steps = 0;
		while (steps < MAX_COLLISION_STEPS) {
			tileXOffset = 0;
			tileYOffset = 0;
			xcheck1 = snapTile (xstep, xdir);
			ycheck1 = m * xcheck1 + b;
			ycheck2 = snapTile (ystep, ydir);
			xcheck2 = (ycheck2 - b) / m;
			if (Math.abs (x1 - xcheck1) > Math.abs (x1 - xcheck2)) {
				double temp = xcheck1;
				xcheck1 = xcheck2;
				xcheck2 = temp;
				temp = ycheck1;
				ycheck1 = ycheck2;
				ycheck2 = temp;
			}
			xstep = xcheck1;
			ystep = ycheck1;
			if (!isBetween (xstep, x1, x2) || !isBetween (ystep, y1, y2)) {
				return null;
			}
			if (xdir == -1 && xstep % TILE_SIZE == 0) {
				tileXOffset = -1;
			}
			if (ydir == -1 && ystep % TILE_SIZE == 0) {
				tileYOffset = -1;
			}
			if (collisionData [getTileId ((int) xstep / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE + tileYOffset)]) {
				return new double[] {xstep, ystep};
			}
			steps ++;
		}
		return null;
	}
	public void setTileBuffer (double x1, double y1, double x2, double y2) {
		int xdir = 1;
		int ydir = 1;
		byte tileXOffset = 0;
		byte tileYOffset = 0;
		if (x1 >= x2 && x1 % TILE_SIZE == 0) {
			xdir = -1;
			tileXOffset = -1;
		}
		if (y1 >= y2 && y1 % TILE_SIZE == 0) {
			ydir = -1;
			tileYOffset = -1;
		}
		if (tileInBounds ((int)(x1 / TILE_SIZE + tileXOffset), (int)(y1 / TILE_SIZE + tileYOffset))) {
			if (collisionData [getTileId ((int)(x1 / TILE_SIZE + tileXOffset), (int)(y1 / TILE_SIZE + tileYOffset))] == true) {
				tileBuffer.enabled = true;
				tileBuffer.collisionX = x1;
				tileBuffer.collisionY = y1;
				tileBuffer.spriteUsed = tileList [getTileId ((int)(x1 / TILE_SIZE), (int)(y1 / TILE_SIZE))];
				tileBuffer.mapTile.tileId = tileIdList [getTileId ((int)(x1 / TILE_SIZE), (int)(y1 / TILE_SIZE))];
				tileBuffer.mapTile.x = (int) x1 / TILE_SIZE;
				tileBuffer.mapTile.y = (int) y1 / TILE_SIZE;
				return;
			}
		}
		if ((x1 < 0 && x2 < 0) || (x1 > levelWidth * TILE_SIZE && x2 > levelWidth * TILE_SIZE) || (y1 < 0 && y2 < 0) || (y1 > levelWidth * TILE_SIZE && y2 > levelWidth * TILE_SIZE)) {
			tileBuffer.enabled = false;
			return;
		} else {
			tileBuffer.enabled = true;
		}
		double xcheck1 = 0;
		double ycheck1 = 0;
		double xcheck2 = 0;
		double ycheck2 = 0;
		double xstep = x1;
		double ystep = y1;
		/*if (collisionData [getTileId ((int) x1 / TILE_SIZE, (int) y1 / TILE_SIZE)]) {
			tileBuffer.collisionX = x1;
			tileBuffer.collisionY = y2;
			tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / TILE_SIZE, (int) y1 / TILE_SIZE)];
			return;
		}*/
		int steps;
		if (x1 == x2) {
			steps = 0;
			while (steps < MAX_COLLISION_STEPS) {
				tileYOffset = 0;
				ystep = snapTile (ystep, ydir);
				if (ydir == -1 && ystep % TILE_SIZE == 0) {
					tileYOffset = -1;
				}
				if (!isBetween (ystep, y1, y2)) {
					tileBuffer.enabled = false;
					return;
				}
				int tileFinalX = (int) x1 / TILE_SIZE;
				int tileFinalY = (int) ystep / TILE_SIZE + tileYOffset;
				if (x1 % TILE_SIZE == 0) {
					if (tileFinalX <= 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
						tileBuffer.enabled = false;
						return;
					}
					if (collisionData [getTileId (tileFinalX, tileFinalY)] && collisionData [getTileId (tileFinalX - 1, tileFinalY)]) {
						tileBuffer.collisionX = x1;
						tileBuffer.collisionY = y2;
						tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / TILE_SIZE, (int) ystep / TILE_SIZE + tileYOffset)];
						tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) x1 / TILE_SIZE, (int) ystep / TILE_SIZE + tileYOffset)];
						tileBuffer.mapTile.x = (int) x1 / TILE_SIZE;
						tileBuffer.mapTile.y = (int) ystep / TILE_SIZE + tileYOffset;
						return;
					}
				} else {
					if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
						tileBuffer.enabled = false;
						return;
					}
					if (collisionData [getTileId (tileFinalX, tileFinalY)]) {
						tileBuffer.collisionX = x1;
						tileBuffer.collisionY = y2;
						tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / TILE_SIZE, (int) ystep / TILE_SIZE + tileYOffset)];
						tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) x1 / TILE_SIZE, (int) ystep / TILE_SIZE + tileYOffset)];
						tileBuffer.mapTile.x = (int) x1 / TILE_SIZE;
						tileBuffer.mapTile.y = (int) ystep / TILE_SIZE + tileYOffset;
						return;
					}
				}
				steps ++;
			}
		}
		/*System.out.print (y1);
		System.out.print (", ");
		System.out.println (y2);*/
		if (y1 == y2) {
			steps = 0;
			while (steps < MAX_COLLISION_STEPS) {
				tileXOffset = 0;
				xstep = snapTile (xstep, xdir);
				if (xdir == -1 && xstep % TILE_SIZE == 0) {
					tileXOffset = -1;
				}
				if (!isBetween (xstep, x1, x2)) {
					tileBuffer.enabled = false;
					return;
				}
				int tileFinalX = (int) x1 / TILE_SIZE + tileXOffset;
				int tileFinalY = (int) ystep / TILE_SIZE;
				if (y1 % TILE_SIZE == 0) {
					if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY <= 0 || tileFinalY >= levelHeight) {
						tileBuffer.enabled = false;
						return;
					}
					if (collisionData [getTileId (tileFinalX, tileFinalY)] && collisionData [getTileId (tileFinalX, tileFinalY - 1)]) {
						tileBuffer.collisionX = xstep;
						tileBuffer.collisionY = y1;
						tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE)];
						tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) x1 / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE)];
						tileBuffer.mapTile.x = (int) x1 / TILE_SIZE + tileXOffset;
						tileBuffer.mapTile.y = (int) ystep / TILE_SIZE;
						return;
					}
				} else {
					if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
						tileBuffer.enabled = false;
						return;
					}
					if (collisionData [getTileId (tileFinalX, tileFinalY)]) {
						tileBuffer.collisionX = xstep;
						tileBuffer.collisionY = y1;
						tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE)];
						tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) x1 / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE)];
						tileBuffer.mapTile.x = (int) x1 / TILE_SIZE + tileXOffset;
						tileBuffer.mapTile.y = (int) ystep / TILE_SIZE;
						return;
					}
				}
				steps ++;
			}
		}
		double m = (y1 - y2) / (x1 - x2);
		double b = y1 - m * x1;
		steps = 0;
		while (steps < MAX_COLLISION_STEPS) {
			tileXOffset = 0;
			tileYOffset = 0;
			xcheck1 = snapTile (xstep, xdir);
			ycheck1 = m * xcheck1 + b;
			ycheck2 = snapTile (ystep, ydir);
			xcheck2 = (ycheck2 - b) / m;
			if (Math.abs (x1 - xcheck1) > Math.abs (x1 - xcheck2)) {
				double temp = xcheck1;
				xcheck1 = xcheck2;
				xcheck2 = temp;
				temp = ycheck1;
				ycheck1 = ycheck2;
				ycheck2 = temp;
			}
			xstep = xcheck1;
			ystep = ycheck1;
			//MainLoop.getWindow ().getBuffer ().fillRect ((int)xstep, (int)ystep, 1, 1);
			if (!isBetween (xstep, x1, x2) || !isBetween (ystep, y1, y2)) {
				tileBuffer.enabled = false;
				return;
			}
			if (xdir == -1 && xstep % TILE_SIZE == 0) {
				tileXOffset = -1;
			}
			if (ydir == -1 && ystep % TILE_SIZE == 0) {
				tileYOffset = -1;
			}
			int tileFinalX = (int) xstep / TILE_SIZE + tileXOffset;
			int tileFinalY = (int) ystep / TILE_SIZE + tileYOffset;
			if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
				tileBuffer.enabled = false;
				return;
			}
			if (collisionData [getTileId (tileFinalX, tileFinalY)]) {
				tileBuffer.collisionX = xstep;
				tileBuffer.collisionY = ystep;
				tileBuffer.spriteUsed = tileList [getTileId ((int) xstep / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE + tileYOffset)];
				tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) xstep / TILE_SIZE + tileXOffset, (int) ystep / TILE_SIZE + tileYOffset)];
				tileBuffer.mapTile.x = (int) xstep / TILE_SIZE + tileXOffset;
				tileBuffer.mapTile.y = (int) ystep / TILE_SIZE + tileYOffset;
				return;
			}
			steps ++;
		}
	}
	public MapTile[][] getTiles (int x, int y, int width, int height) {
		MapTile[][] result = new MapTile[height][width];
		//wx is working x; wy is working y
		for (int wy = 0; wy < height; wy ++) {
			for (int wx = 0; wx < width; wx ++) {
				result [wx][wy] = new MapTile (getTileIdString (x + wx, y + wy), x + wx, y + wy);
			}
		}
		return result;
	}
	public short[][][] getTileIds (int x, int y, int width, int height) {
		short[][][] result = new short[tileData.length][width][height];
		//wx is working x; wy is working y
		for (int layer = 0; layer < tileData.length; layer ++) {
			for (int wy = 0; wy < height; wy ++) {
				for (int wx = 0; wx < width; wx ++) {
					result [layer][wx][wy] = tileData [layer][x + wx][y + wy];
				}
			}
		}
		return result;
	}
	public boolean[][] filter (int x, int y, int width, int height, String filter) {
		return null;
	}
	public double snapTile (double num, int direction) {
		if (num % TILE_SIZE == 0) {
			if (direction == 1) {
				return num + TILE_SIZE;
			} else {
				return num - TILE_SIZE;
			}
		} else {
			if (direction == 1) {
				return Math.ceil (num / TILE_SIZE) * TILE_SIZE;
			} else {
				return Math.floor (num / TILE_SIZE) * TILE_SIZE;
			}
		}
	}
	public boolean isColliding (Hitbox hitbox) {
		//Returns true if the given Hitbox is colliding with a solid tile
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int x2 = bind ((x + width) / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int y1 = bind (y / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		int y2 = bind ((y + height) / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		for (int i = x1; i <= x2; i ++) {
			for (int j = y1; j <= y2; j ++) {
				if (collisionData [getTileId (i, j)] == true) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isColliding (Hitbox hitbox, String tileId) {
		//Returns true if the given Hitbox is colliding with a tile of type tileId
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int x2 = bind ((x + width) / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int y1 = bind (y / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		int y2 = bind ((y + height) / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		for (int i = x1; i <= x2; i ++) {
			for (int j = y1; j <= y2; j ++) {
				if (tileIdList [getTileId (i, j)].equals (tileId)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isColliding (Hitbox hitbox, double xTo, double yTo) {
		for (int i = 0; i <= 6; i += 2) {
			if (isColliding (hitbox.x + hitboxCorners [i] * hitbox.width, hitbox.y + hitboxCorners [i + 1] * hitbox.height, xTo + hitboxCorners [i] * hitbox.width, yTo + hitboxCorners [i + 1] * hitbox.height)) {
				return true;
			}
		}
		return false;
	}
	public double[] getCollidingCoords (Hitbox hitbox, double xTo, double yTo) {
		double[][] collisionData = new double [4][2];
		for (int i = 0; i <= 6; i += 2) {
			collisionData [i / 2] = getCollidingCoords (hitbox.x + hitboxCorners [i] * hitbox.width, hitbox.y + hitboxCorners [i + 1] * hitbox.height, xTo + hitboxCorners [i] * hitbox.width, yTo + hitboxCorners [i + 1] * hitbox.height);
			if (collisionData [i / 2] != null) {
				if (collisionData [i / 2][0] >= hitbox.x + hitboxCorners [i]) {
					collisionData [i / 2][0] -= hitboxCorners [i] * hitbox.width;
				}
				if (collisionData [i / 2][1] >= hitbox.y * hitboxCorners [i + 1]) {
					collisionData [i / 2][1] -= hitboxCorners [i + 1] * hitbox.height;
				}
			}
		}
		double[] returnData = null;
		double minDist = -1;
		double currentDist;
		for (int i = 0; i < 4; i ++) {
			//System.out.println(collisionData[i][1]);
			if (collisionData [i] != null) {
				currentDist = (collisionData [i][0] - hitbox.x) * (collisionData [i][0] - hitbox.x) + (collisionData [i][1] - hitbox.y) * (collisionData [i][1] - hitbox.y);
				if (currentDist <= minDist || minDist == -1) {
					minDist = currentDist;
					returnData = collisionData [i];
				}
			}
		}
		return returnData;
	}
	public double[] doHitboxVectorCollison (Hitbox hitbox, double xTo, double yTo) {
		//Performs a hitbox-on-tilemap vector collision, sets tileBuffer accordingly, and returns the position the object was in when it collided
		MapTile[] collidingTiles = new MapTile [4];
		double[][] collisionData = new double [4][2];
		double[][] collisionPoints = new double [4][2];
		MapTile[] tileList = new MapTile[4];
		double xDiff = xTo - hitbox.x;
		double yDiff = yTo - hitbox.y;
		int[] ptrlist;
		if (xDiff < 0 && yDiff < 0) {
			ptrlist = new int[] {0, 1, 2, 3};
		} else if (xDiff >= 0 && yDiff < 0) {
			ptrlist = new int[] {1, 0, 2, 3};
		} else if (xDiff < 0 && yDiff >= 0) {
			ptrlist = new int[] {2, 0, 1, 3};
		} else {
			ptrlist = new int[] {3, 0, 1, 2};
		}
		for (int i = 0; i <= 6; i += 2) {
			setTileBuffer (hitbox.x + hitboxCorners [i] * hitbox.width, hitbox.y + hitboxCorners [i + 1] * hitbox.height, xTo + hitboxCorners [i] * hitbox.width, yTo + hitboxCorners [i + 1] * hitbox.height);
			//MainLoop.getWindow ().getBuffer ().drawLine ((int)(hitbox.x + hitboxCorners [i] * hitbox.width), (int)(hitbox.y + hitboxCorners [i + 1] * hitbox.height), (int)(xTo + hitboxCorners [i] * hitbox.width), (int)(yTo + hitboxCorners [i + 1] * hitbox.height));
			if (tileBuffer.enabled) {
				collisionData [i / 2][0] = this.tileBuffer.collisionX;
				collisionPoints [i / 2][0] = this.tileBuffer.collisionX;
				collisionData [i / 2][1] = this.tileBuffer.collisionY;
				collisionPoints [i / 2][1] = this.tileBuffer.collisionY;
				if (collisionData [i / 2][0] >= hitbox.x + hitboxCorners [i]) {
					collisionData [i / 2][0] -= hitboxCorners [i] * hitbox.width;
				}
				if (collisionData [i / 2][1] >= hitbox.y * hitboxCorners [i + 1]) {
					collisionData [i / 2][1] -= hitboxCorners [i + 1] * hitbox.height;
				}
				/*MainLoop.getWindow ().getBuffer ().setColor (new Color(0xFF0000));
				MainLoop.getWindow ().getBuffer ().fillRect ((int)tileBuffer.collisionX, (int)tileBuffer.collisionY, 2, 2);
				MainLoop.getWindow ().getBuffer ().setColor (new Color(0x000000));*/
				//MainLoop.getWindow ().getBuffer ().fillRect ((int)tileBuffer.collisionX, (int)tileBuffer.collisionY, 2, 2);
				collidingTiles [i / 2] = new MapTile (tileBuffer.mapTile.tileId, tileBuffer.mapTile.x, tileBuffer.mapTile.y);
			}
		}
		int closestTile = -1;
		double minDist = -1;
		double currentDist;
		int usedIndex;
		for (int i = 0; i < 4; i ++) {
			//System.out.println(collisionData[i][1]);
			usedIndex = ptrlist [i];
			if (collidingTiles [usedIndex] != null) {
				currentDist = (collisionData [usedIndex][0] - hitbox.x) * (collisionData [usedIndex][0] - hitbox.x) + (collisionData [usedIndex][1] - hitbox.y) * (collisionData [usedIndex][1] - hitbox.y);
				if (currentDist <= minDist || minDist == -1) {
					minDist = currentDist;
					closestTile = usedIndex;
				}
			}
		}
		if (closestTile != -1) {
			this.tileBuffer.collisionX = collisionPoints [closestTile][0];
			this.tileBuffer.collisionY = collisionPoints [closestTile][1];
			this.tileBuffer.mapTile.x  = collidingTiles [closestTile].x;
			this.tileBuffer.mapTile.y = collidingTiles [closestTile].y;
			this.tileBuffer.mapTile.tileId = collidingTiles [closestTile].tileId;
			this.tileBuffer.spriteUsed = null; //Because I'm lazy
			MainLoop.getWindow ().getBufferGraphics ().setColor (new Color(0xFF0000));
			MainLoop.getWindow ().getBufferGraphics ().fillRect ((int)tileBuffer.collisionX, (int)tileBuffer.collisionY, 2, 2);
			MainLoop.getWindow ().getBufferGraphics ().setColor (new Color(0x000000));
			return collisionData [closestTile];
			//Returns true for a successful collision
		} else {
			return null;
			//Returns false for no collision detected
		}
	}
	public boolean[][] getCollidingTiles (Hitbox hitbox) {
		//Returns a matrix of tiles that are being collided with by the given Hitbox
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int x2 = bind ((x + width) / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int y1 = bind (y / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		int y2 = bind ((y + height) / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		boolean[][] result = new boolean [(x2 - x1 + 1)][(y2 - y1 + 1)];
		for (int i = y1; i <= y2; i ++) {
			for (int j = x1; j <= x2; j ++) {
				result [j - x1][i - y1] = collisionData [getTileId (j, i)];
			}
		}
		return result;
	}
	public boolean[][] getCollidingTiles (Hitbox hitbox, String tileId) {
		//Returns a matrix of tiles that are under the given Hitbox and have the given tileId
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int x2 = bind ((x + width) / TILE_SIZE, 0, levelWidth * TILE_SIZE);
		int y1 = bind (y / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		int y2 = bind ((y + height) / TILE_SIZE, 0, levelHeight * TILE_SIZE);
		boolean[][] result = new boolean [(x2 - x1 + 1)][(y2 - y1 + 1)];
		for (int i = y1; i <= y2; i ++) {
			for (int j = x1; j <= x2; j ++) {
				result [j - x1][i - y1] = tileIdList [getTileId (j, i)].equals (tileId);
			}
		}
		return result;
	}
	public short getTileId (int x, int y, int layer) {
		//Returns the numerical tile ID at x, y
		return tileData [layer][x][y];
	}
	public short getTileId (int x, int y) {
		//Returns the numerical tile ID at x, y
		return tileData [0][x][y];
	}
	public Sprite getTileIcon (short tileId) {
		return tileList [tileId];
	}
	public short getNumericalId (String tileId) {
		//Returns the numerical ID associated with the tile tileId; returns -1 if no ID is found
		for (int i = 0; i < tileIdList.length; i ++) {
			if (tileIdList [i].equals (tileId)) {
				return (short)i;
			}
		}
		return -1;
	}
	public boolean isSolid (int x, int y) {
		//Returns true if the tile at x, y is solid
		return collisionData[tileData [0][x][y]];
	}
	public String getTileIdString (int x, int y) {
		//Returns the string tile ID at x, y
		return tileIdList [tileData [0][x][y]];
	}
	public String getTileIdString (int x, int y, int layer) {
		return tileIdList [tileData [layer][x][y]];
	}
	public boolean setTile (int x, int y, String id) {
		return setTile (0, x, y, id);
	}
	public boolean setTile (int layer, int x, int y, String id) {
		for (short i = 0; i < tileIdList.length; i ++) {
			if (tileIdList [i].equals (id)) {
				tileData [layer][x][y] = i;
				return true;
			}
		}
		return false;
	}
	public void setTile (int layer, int x, int y, short id) {
		tileData [layer][x][y] = id;
	}
	public void addMapUnit (MapUnit unit) {
		mapUnits.put (unit.getName (), unit);
	}
	public void frameEvent () {
		//Renders the room
		int windowWidth = MainLoop.getWindow ().getResolution ()[0];
		int windowHeight = MainLoop.getWindow ().getResolution()[1];
		for (int layer = tileData.length - 1; layer >= 0; layer --) {
			if (backgroundList.size () != 0 && backgroundList.get (layer) != null) {
				backgroundList.get (layer).draw (viewX, viewY);
			} else {
				for (int i = -(viewX % TILE_SIZE); i < windowWidth && i < levelWidth * TILE_SIZE; i += TILE_SIZE) {
					for (int j = (-viewY % TILE_SIZE); j < windowHeight && j < levelHeight * TILE_SIZE; j += TILE_SIZE) {
						Sprite currTile = tileList [tileData [layer][(viewX + i) / TILE_SIZE][(viewY + j) / TILE_SIZE]];
						if (currTile != null) {
							currTile.draw (i, j);
						}
					}
				}
			}
		}
	}
	public void loadRoom (String path) throws FileNotFoundException {
		loadRMF (path);
		return;
	}
	public void loadRMF (String path) throws FileNotFoundException {
		//START OF HEADER
		//Bytes 0-3: RMF# (# is version number)
		//Bytes 4-7: Map width, in tiles
		//Bytes 8-11: Map height, in tiles
		//Bytes 12-15: Number of layers
		//Bytes TILE_SIZE-19: Number of objects (placed)
		//END OF HEADER
		//Tileset list (background layers are excluded)
		//Object import list
		//Background list
		//Tiles
		//Object list (x, y, id, variant)
		
		//This section is copy-pasted from the CMF loading code
		String previousName = roomName;
		roomName = path;
		try {
			//Purges the gameObjects
			ArrayList<ArrayList<GameObject>> objList = MainLoop.getObjectMatrix ().objectMatrix;
			for (int i = 0; i < objList.size (); i ++) {
				if (objList.get (i) != null) {
					int listSize = objList.get (i).size ();
					for (int j = 0; j < listSize; j ++) {
						if (objList.get (i).get (j) != null && !objList.get (i).get (j).isPersistent ()) {
							objList.get (i).get (j).forget ();
						}
					}
				}
			}
			//Loads the RMF file at the given filepath
			readBit = 0;
			File file = null;
			FileInputStream stream = null;
			file = new File (path);
			inData = new byte[(int) file.length ()];
			stream = new FileInputStream (file);
			try {
				stream.read (inData);
				stream.close ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Read the header
			if (!readChars (3).equals ("RMF")) {
				System.out.println ("Error: file is corrupted or in an invalid format");
			}
			//Read header metadata
			char version = readChar ();
			int mapWidth = readInt ();
			int mapHeight = readInt ();
			int numLayers = readInt ();
			int numObjects = readInt ();
			
			//Apply metadata
			levelWidth = mapWidth;
			levelHeight = mapHeight;
			
			//Read tileset list
			String tilesetString = readTo (';');
			String[] tilesetArr = tilesetString.split (",");
			
			//Import the tilesets
			ArrayList<Sprite> tileList = new ArrayList<Sprite> ();
			ArrayList<String> tileIdList = new ArrayList<String> ();
			for (int i = 0; i < tilesetArr.length; i++) {
				//Get tile images
				if (tilesetArr[i].equals ("_NULL")) {
					tileList.add (null);
					tileIdList.add ("_NULL");
				} else {
					
					//Make the spritesheet
					Spritesheet ss = new Spritesheet (tilesetArr[i]);
					double scale = (double)TILE_SIZE / TILE_SOURCE_SIZE;
					int newWidth = (int)(ss.getWidth () * scale);
					int newHeight = (int)(ss.getHeight () * scale);
					
					//Make the image
					BufferedImage tileImg = new BufferedImage (newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
					Graphics g = tileImg.getGraphics ();
					g.drawImage (ss.getImage (), 0, 0, newWidth, newHeight, 0, 0, ss.getWidth (), ss.getHeight (), null);
					Spritesheet newSheet = new Spritesheet (tileImg);
					
					Sprite[] s = newSheet.toSpriteArray (TILE_SIZE, TILE_SIZE);
					//Add tiles into map
					for (int j = 0; j < s.length; j++) {
						tileList.add (s[j]);
						tileIdList.add (tilesetArr [i] + ":" + String.valueOf (j));
					}
				}
			}
			
			//Set instance variables appropriately
			this.tileList = tileList.toArray (new Sprite[0]);
			this.tileIdList = new String[tileIdList.size ()];
			for (int i = 0; i < tileIdList.size (); i++) {
				String currId = tileIdList.get (i);
				String[] currSplit = currId.split ("/");
				this.tileIdList [i] = currSplit [currSplit.length - 1];
			}
			
			//Add collision data for the tilesets
			collisionData = new boolean[tileIdList.size ()];
			for (int i = 0; i < collisionData.length; i ++) {
				TileData workingTile = tileAttributesList.getTile (this.tileIdList [i]);
				if (workingTile != null) {
					collisionData [i] = workingTile.isSolid ();
				} else {
					collisionData [i] = true;
				}
			}
			collisionData[0] = false;
			
			//Read object list
			String objectString = readTo (';');
			String[] objArr = objectString.split (",");

			//Read background list
			String backgroundString = readTo (';');
			String[] backgroundArr = backgroundString.split (",");
			
			//Import backgrounds (TODO)
			ArrayList<Background> backgroundList = new ArrayList<Background> ();
			for (int i = 0; i < backgroundArr.length; i++) {
				if (backgroundArr[i].equals ("_NULL")) {
					backgroundList.add (null);
				} else {
					backgroundList.add (new Background (new Sprite ("resources/backgrounds/" + backgroundArr[i])));
					i += 2; //TODO
				}
			}
			
			//Size tile data appropriately
			tileData = new short[numLayers][mapWidth][mapHeight];
			
			//Import tile data
			int numBytes = 1; //TODO
			for (int i = 0; i < numLayers; i++) {
				if (backgroundList.get (i) == null) {
					for (int wy = 0; wy < mapHeight; wy++) {
						for (int wx = 0; wx < mapWidth; wx++) {
							tileData [i][wx][wy] = (short)readBits (numBytes * 8);
						}
					}
				}
			}
			
			//Import placed objects (TODO add MapUnit support)
			int xBytes = 1; //TODO
			int yBytes = 1; //TODO
			int idBytes = 1; //TODO
			for (int i = 0; i < numObjects; i++) {
				//Read object properties
				int objX = readBits (xBytes * 8);
				int objY = readBits (yBytes * 8);
				int objId = readBits (idBytes * 8);
				String variant = readTo (';');
				if (variant.length () > 0) {
					variant = variant.substring (1);
				}
				
				//Make object
				//TODO allow multiple packages
				try {
					//Make the object
					GameObject newObject = ObjectMatrix.makeInstance (objArr[objId]);
					//Assign the object's variants
					String[] variantSplit = variant.split (",");
					for (int vi = 0; vi < variantSplit.length; vi++) {
						String[] attribPair = variantSplit [vi].split (":");
						if (attribPair.length == 2) {
							newObject.setVariantAttribute (attribPair [0], attribPair [1]);
						}
					}
					//Declare the object
					newObject.declare (objX * TILE_SIZE, objY * TILE_SIZE);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					//Object is invalid and cannot be instantiated
					System.out.println ("Error while loading room " + roomName + ": object with index " + i + " could not be instantiated");
				}
			}
			
			//Set object list
			this.objectList = objArr;
			this.backgroundList = backgroundList; //TODO
			
		} catch (FileNotFoundException e) {
			//Error in room loading; keep previous room
			roomName = previousName;
			throw e;
		}
		
		//I haven't a goddamn clue what this does
		mapUnits.clear ();
		
		//Play the current music
		MusicPlayer.stop ();
		MusicPlayer.playCurrentMapSong ();
	}
	public void loadCMF (String path) throws FileNotFoundException {
		String previousName = roomName;
		roomName = path;
		//Purges the gameObjects
		try {
			ArrayList<ArrayList<GameObject>> objList = MainLoop.getObjectMatrix ().objectMatrix;
			for (int i = 0; i < objList.size (); i ++) {
				if (objList.get (i) != null) {
					int listSize = objList.get (i).size ();
					for (int j = 0; j < listSize; j ++) {
						if (objList.get (i).get (j) != null && !objList.get (i).get (j).isPersistent ()) {
							objList.get (i).get (j).forget ();
						}
					}
				}
			}
			//Loads the CMF file at the given filepath
			readBit = 0;
			File file = null;
			FileInputStream stream = null;
			file = new File (path);
			inData = new byte[(int) file.length ()];
			stream = new FileInputStream (file);
			try {
				stream.read (inData);
				stream.close ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (readBits (24) != 0x434D46) {
				System.out.println ("Error: file is corrupted or in an invalid format");
			}
			int version = readBits (8); //For future use
			int layerCount = readBits (8); //For future use
			//resizeLevel (readBits (TILE_SIZE), readBits (TILE_SIZE));
			levelWidth = readBits (TILE_SIZE);
			levelHeight = readBits (TILE_SIZE);
			tileData = new short[layerCount][levelWidth][levelHeight];
			for (int layer = 0; layer < layerCount; layer ++) {
				for (int i = 0; i < levelWidth; i ++) {
					for (int c = 0; c < levelHeight; c ++) {
						tileData [layer][i][c] = -1;
					}
				}
			}
			int tilesUsedLength = readBits (TILE_SIZE);
			int objectsPlacedLength = readBits (32);
			int tempReadBit = readBit;
			int result = 0;
			int index = 0;
			//Parse tile set list
			while (result != 0x3B) {
				result = readBits (8);
				index ++;
			}
			readBit = tempReadBit;
			char[] tilesetNames = new char[index - 1];
			for (int i = 0; i < index - 1; i ++) {
				tilesetNames [i] = (char) readBits (8);
			}
			readBit += 8;
			String tilesetList = new String (tilesetNames);
			String[] tilesetNameArray = tilesetList.split (",");
			//Parse object list
			tempReadBit = readBit;
			index = 0;
			result = 0;
			while (result != 0x3B) {
				result = readBits (8);
				index ++;
			}
			readBit = tempReadBit;
			char[] objectNames = new char[index - 1];
			for (int i = 0; i < index - 1; i ++) {
				objectNames [i] = (char) readBits (8);
			}
			readBit += 8;
			String objectString = new String (objectNames);
			if (objectString.equals ("")) {
				objectList = new String[0];
			} else {
				objectList = objectString.split (",");
			}
			//Import tiles
			ArrayList<Sprite> tileSheet = new ArrayList<Sprite> ();
			ArrayList<String> tileIdArrList = new ArrayList<String> ();
			Spritesheet importSheet;
			for (int i = 0; i < tilesetNameArray.length; i ++) {
				//System.out.println("resources/tilesets/" + tilesetNameArray [i]);
				importSheet = new Spritesheet ("resources/tilesets/" + tilesetNameArray [i]);
				Sprite[] tempSheet = importSheet.toSpriteArray (TILE_SIZE, TILE_SIZE);
				//System.out.println(tempSheet.length);
				for (int j = 0; j < tempSheet.length; j ++) {
					tileSheet.add (tempSheet [j]);
					tileIdArrList.add (tilesetNameArray [i] + ":" + String.valueOf (j));
				}
			}
			short[] tilesUsed = new short[tilesUsedLength];
			int tileBits = numBits (tilesUsedLength - 1);
			tileList = new Sprite[tilesUsed.length];
			tileIdList = new String[tilesUsed.length];
			int tileSheetBits = numBits (tileSheet.size () - 1);
			for (int i = 0; i < tilesUsedLength; i ++) {
				tilesUsed [i] = (short) readBits (tileSheetBits);
			}
			for (int i = 0; i < tileList.length; i ++) {
				tileList [i] = tileSheet.get (tilesUsed [i]);
				tileIdList [i] = tileIdArrList.get (tilesUsed [i]);
			}
			for (int i = 0; i < tileList.length; i ++) {
				tileSheet.add (tileList [i]);
			}
			collisionData = new boolean[tileIdList.length];
			for (int i = 0; i < collisionData.length; i ++) {
				TileData workingTile = tileAttributesList.getTile (tileIdList [i]);
				if (workingTile != null) {
					collisionData [i] = workingTile.isSolid ();
				} else {
					collisionData [i] = true;
				}
			}
			//Import object icons
			int widthBits = numBits (levelWidth - 1);
			int heightBits = numBits (levelHeight - 1);
			int objectBits = numBits (objectList.length - 1);
			int objId;
			int objX;
			int objY;
			Class<?> objectClass = null;
			Constructor<?> constructor = null;
			String workingName;
			ArrayList<GameObject> toDeclare = new ArrayList<GameObject> ();
			boolean hasVariants;
			for (int i = 0; i < objectsPlacedLength; i ++) {
				objId = readBits (objectBits);
				objX = readBits (widthBits);
				objY = readBits (heightBits);
				if (objectList [objId].split ("#").length == 2) {
					workingName = objectList [objId].split ("#")[0];
					hasVariants = true;
				} else {
					workingName = objectList [objId];
					hasVariants = false;
				}
				objectClass = GameObjectLoader.getClass (workingName);
				try {
					GameObject obj = (GameObject) objectClass.newInstance ();
					if (hasVariants) {
						obj.setVariantData (objectList [objId].split ("#")[1]);
					}
					obj.setX (objX * TILE_SIZE);
					obj.setY (objY * TILE_SIZE);
					toDeclare.add (obj);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//objectList.add (new GameObject (readBits (objectBits), readBits (widthBits), readBits (heightBits)));
			}
			short id;
			int x1;
			int x2;
			int y1;
			int y2;
			for (int layer = 0; layer < layerCount; layer ++) {
				int fullRangesSize = readBits (32);
				int horizRangesSize = readBits (32);
				int vertRangesSize = readBits (32);
				for (int i = 0; i < fullRangesSize; i ++) {
					id = (short) readBits (tileBits);
					x1 = readBits (widthBits);
					x2 = readBits (widthBits);
					y1 = readBits (heightBits);
					y2 = readBits (heightBits);
					//System.out.println(readBit);
					for (int j = x1; j <= x2; j ++) {
						for (int k = y1; k <= y2; k ++) {
							tileData [layer][j][k] = id;
						}
					}
				}
				for (int i = 0; i < horizRangesSize; i ++) {
					id = (short) readBits (tileBits);
					x1 = readBits (widthBits);
					x2 = readBits (widthBits);
					y1 = readBits (heightBits);
					for (int j = x1; j <= x2; j ++) {
						tileData [layer][j][y1] = id;
					}
				}
				for (int i = 0; i < vertRangesSize; i ++) {
					id = (short) readBits (tileBits);
					x1 = readBits (widthBits);
					y1 = readBits (heightBits);
					y2 = readBits (heightBits);
					for (int j = y1; j <= y2; j ++) {
						tileData [layer][x1][j] = id;
					}
				}
				for (int i = 0; i < levelWidth; i ++) {
					for (int c = 0; c < levelHeight; c ++) {
						if (tileData [layer][i][c] == -1) {
							tileData [layer][i][c] = (short) readBits (tileBits);
						}
					}
				}
			}
			for (int i = 0; i < toDeclare.size (); i ++) {
				GameObject d = toDeclare.get (i);
				d.declare (d.getX (), d.getY ());
			}
			for (int i = 0; i < toDeclare.size (); i ++) {
				GameObject d = toDeclare.get (i);
				if (d instanceof RoomLoadedEvent) {
					((RoomLoadedEvent)d).onRoomLoaded ();
				}
			}
		} catch (FileNotFoundException e) {
			roomName = previousName;
			throw e;
		}
		mapUnits.clear ();
	}
	public int numBits (int num) {
		//Returns the number of bits needed to represent a given number
		for (int i = 31; i > 0; i --) {
			if (num >= (1 << (i - 1))) {
				return i;
			}
		}
		return 1;
	}
	public void setView (int x, int y) {
		//Sets the top-right coordinate of the viewport of the room to (x, y)
		this.viewX = x;
		this.viewY = y;
	}
	public int getViewX () {
		//Returns the x-coordinate of the viewport of the room
		return viewX;
	}
	public int getViewY () {
		//Returns the y-coordinate of the viewport of the room
		return viewY;
	}
	public int getWidth () {
		//Returns the width of the room in tiles
		return levelWidth;
	}
	public int getHeight () {
		//Returns the height of the room in tiles
		return levelHeight;
	}
	public int bind (int value, int min, int max) {
		//Binds a value to within the range min, max
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
	public boolean isBetween (double num, double bound1, double bound2) {
		//Returns true if num is between bound1 and bound2
		if (bound1 >= bound2) {
			double temp = bound2;
			bound2 = bound1;
			bound1 = temp;
		}
		return (num >= bound1 && num <= bound2);
	}
	public ArrayList<Background> getBackgroundList () {
		return backgroundList;
	}
	public double getGravity () {
		return this.gravity;
	}
	public void setGravity (double gravity) {
		this.gravity = gravity;
	}
	public TileAttributesMap getTileAttributesList () {
		return this.tileAttributesList;
	}
	public void setTileAttributesList (TileAttributesMap tileAttributesList) {
		this.tileAttributesList = tileAttributesList;
	}
	public String getRoomName () {
		return roomName;
	}
	public boolean tileInBounds (int x, int y) {
		if (x >= 0 && x < this.levelWidth && y >= 0 && y < this.levelHeight) {
			return true;
		} else {
			return false;
		}
	}
}