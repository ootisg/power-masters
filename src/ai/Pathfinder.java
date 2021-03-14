package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Pathfinder {
	
	private static boolean[][] collisionMap;
	private static int[][] distMap;
	private static ArrayList<ScheduledTile> scheduled;
	private static ArrayList<ScheduledTile> next;
	private static boolean pathFound;
	
	private static int goalX;
	private static int goalY;
	
	/**
	 * Gets the collision map (as a list of tiles).
	 * @param x horizontal tile index
	 * @param y vertical tile index
	 * @param width horizontal tile count
	 * @param height vertical tile count
	 * @return The collision map as a boolean[][]; first index is vertical, second index is horizontal
	 */
	private static boolean[][] getCollisionMap (int x, int y, int width, int height) {
		boolean[][] collisionGrid = new boolean[height][width];
		TileCollider collider = new TileCollider ();
		for (int wy = 0; wy < height; wy ++) {
			for (int wx = 0; wx < width; wx ++) {
				collisionGrid [wy][wx] = collider.checkCollision (x + wx, y + wy);
			}
		}
		return collisionGrid;
	}
	
	//For debug purposes
	private static void printBoolGrid (boolean[][] arr) {
		for (int i = 0; i < arr.length; i ++) {
			String working = "[" + String.valueOf (arr [i][0]);
			for (int j = 1; j < arr [0].length; j ++) {
				working += ", " + arr [i][j];
			}
			working += "]";
			System.out.println (working);
		}
	}
	
	//For debug purposes
	private static void printIntGrid (int[][] arr) {
		for (int i = 0; i < arr.length; i ++) {
			String working = "[" + String.valueOf (arr [i][0]);
			for (int j = 1; j < arr [0].length; j ++) {
				working += ", " + arr [i][j];
			}
			working += "]";
			System.out.println (working);
		}
		System.out.println ("----------------");
	}
	
	//For debug purposes
	private static String getIntGrid (int[][] arr) {
		String working = "";
		for (int i = 0; i < arr.length; i ++) {
			for (int j = 0; j < arr [0].length; j ++) {
				working += arr [i][j];
				if (!(i == arr.length - 1 && j == arr [0].length - 1)) {
					working += " ";
				}
			}
		}
		return working + "\n";
	}
	
	public static LinkedList<Point> findPath (int x, int y, int width, int height, int xStart, int yStart, int xEnd, int yEnd) {
		pathFound = false;
		goalX = xEnd;
		goalY = yEnd;
		collisionMap = getCollisionMap (x, y, width, height);
		distMap = new int[height][width];
		for (int wy = 0; wy < height; wy ++) {
			for (int wx = 0; wx < width; wx ++) {
				distMap [wy][wx] = -1;
			}
		}
		scheduled = new ArrayList<ScheduledTile> ();
		next = new ArrayList<ScheduledTile> ();
		next.add (new ScheduledTile (xStart, yStart, xStart - x, yStart - y));
		
		while (next.size () != 0) {
			scheduled = next;
			next = new ArrayList<ScheduledTile> ();
			Iterator<ScheduledTile> iter = scheduled.iterator ();
			while (iter.hasNext ()) {
				iter.next ().propogate ();
			}
			if (pathFound) {
				return backtrace (distMap, xEnd - x, yEnd - y);
			}
		}
		return null;
	}
	
	public static LinkedList<Point> backtrace (int[][] distMap, int xEnd, int yEnd) {
		Stack<Point> pathPoints = new Stack<Point> ();
		int previousDirection = -1;
		int wx = xEnd;
		int wy = yEnd;
		while (distMap [wy][wx] != 0) {
			int px = wx;
			int py = wy;
			int current = distMap [wy][wx];
			int nextDirection = -1;
			if (checkTrace (distMap, wx, wy, 0, -1)) {
				wy --;
				nextDirection = 0;
			} else if (checkTrace (distMap, wx, wy, -1, 0)) {
				wx --;
				nextDirection = 1;
			} else if (checkTrace (distMap, wx, wy, 0, 1)) {
				wy ++;
				nextDirection = 2;
			} else if (checkTrace (distMap, wx, wy, 1, 0)) {
				wx ++;
				nextDirection = 3;
			}
			if (nextDirection != previousDirection) {
				pathPoints.push (new Point (px, py));
				previousDirection = nextDirection;
			}
			if (distMap [wy][wx] == 0) {
				pathPoints.push (new Point (wx, wy));
			}
		}
		LinkedList<Point> path = new LinkedList<Point> ();
		while (!pathPoints.isEmpty ()) {
			path.add (pathPoints.pop ());
		}
		return path;
	}
	
	private static boolean checkTrace (int[][] distMap, int wx, int wy, int xOffset, int yOffset) {
		int newX = wx + xOffset;
		int newY = wy + yOffset;
		if (newX >= 0 && newY < distMap.length && newY >= 0 && newY < distMap [0].length) {
			int current = distMap [wy][wx];
			if (current - 1 == distMap [newY][newX]) {
				return true;
			}
		}
		return false;
	}
	
	private static class ScheduledTile {
		
		public int x;
		public int y;
		
		public int localX;
		public int localY;
		
		private int generation;
		
		public ScheduledTile (int x, int y, int localX, int localY) {
			this.x = x;
			this.y = y;
			this.localX = localX;
			this.localY = localY;
			this.generation = 0;
			distMap [localY][localX] = generation;
			if (goalX == x && goalY == y) {
				pathFound = true;
			}
		}
		
		private ScheduledTile (int x, int y, int localX, int localY, ScheduledTile parent) {
			this.x = x;
			this.y = y;
			this.localX = localX;
			this.localY = localY;
			this.generation = parent.generation + 1;
			distMap [localY][localX] = generation;
			if (goalX == x && goalY == y) {
				pathFound = true;
			}
		}
		
		public int getGeneration () {
			return generation;
		}
		
		public void propogate () {
			scheduleTile (0, -1);
			scheduleTile (-1, 0);
			scheduleTile (0, 1);
			scheduleTile (1, 0);
		}
		
		private void scheduleTile (int xOffset, int yOffset) {
			int newLocalX = localX + xOffset;
			int newLocalY = localY + yOffset;
			if (newLocalX >= 0 && newLocalY >= 0 && newLocalY < collisionMap.length && newLocalX < collisionMap [0].length) {
				if (!collisionMap [newLocalY][newLocalX] && distMap [newLocalY][newLocalX] == -1) {
					 int newX = x + xOffset;
					 int newY = y + yOffset;
					 ScheduledTile newTile = new ScheduledTile (newX, newY, newLocalX, newLocalY, this);
					 next.add (newTile);
				}
			}
			
		}
	}
}
