package mpjp.quad;

import mpjp.shared.HasPoint;
import java.util.*;


public abstract class Trie <T extends HasPoint> implements Element<T>{

	static enum Quadrant {
		NE,
		NW,
		SE,
		SW;
	}
	
	protected double bottomRightX;
	protected double bottomRightY;
	static int capacity;
	protected double topLeftX;
	protected double topLeftY;
	
	protected Trie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super();
		this.bottomRightX = bottomRightX;
		this.bottomRightY = bottomRightY;
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
	}

	/**
	 * Get capacity of a bucket
	 * @return - capacity
	 */
	public static int getCapacity() {
		return capacity;
	}

	/**
	 * Set capacity of a bucket
	 * @param capacity - of bucket
	 */
	public static void setCapacity(int capacity) {
		Trie.capacity = capacity;
	}
	
	/**
	 * Euclidean distance between two pair of coordinates of two points
	 * @param x1 - x coordinate of first point
	 * @param y1 - y coordinate of first point
	 * @param x2 - x coordinate of second point
	 * @param y2 - y coordinate of second point
	 * @return - distance between given points
	 */
	static double getDistance​(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2-x1, y2-y1);
	}
	
	/**
	 * Collect all points in this node and its descendants in given set
	 * @param points - set of HasPoint for collecting points
	 */
	abstract void collectAll(Set<T> points);
	
	/**
	 * Collect points at a distance smaller or equal to radius from (x,y) and place them in given list
	 * @param x - coordinate of point
	 * @param y - coordinate of point
	 * @param radius - from given point
	 * @param points - set for collecting points
	 */
	abstract void collectNear(double x, double y, double radius, Set<T> points);
	
	/**
	 * Insert given point
	 * @param point - to be inserted
	 * @return - changed parent node
	 */
	abstract Trie<T> insert(T point);
	
	/**
	 * Insert given point, replacing existing points in same location
	 * @param point - point to be inserted
	 * @return - changed parent node
	 */
	abstract Trie<T> insertReplace(T point);
	
	/**
	 * Check if overlaps with given circle
	 * @param x - coordinate of circle
	 * @param y - coordinate of circle
	 * @param radius - of circle
	 * @return - true if overlaps and false otherwise
	 */
	boolean overlaps​(double x, double y, double radius) {
		double RectWidth = (x - Math.max(topLeftX, Math.min(x, bottomRightX)));
		double RectHeight = (y - Math.max(bottomRightY, Math.min(y, topLeftY)));

		return Math.pow(RectWidth, 2) + Math.pow(RectHeight, 2) <= Math.pow(radius, 2);
		
	}

	@Override
	public String toString() {
		return "Trie [bottomRightX=" + bottomRightX + ", bottomRightY=" + bottomRightY + ", topLeftX=" + topLeftX
				+ ", topLeftY=" + topLeftY + "]";
	}

	/**
	 * Delete given point
	 * @param point - to delete
	 */
	abstract void delete(T point);

	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point - with requested coordinates
	 * @return - recorded point, if found; null otherwise
	 */
	abstract T find(T point);
}
