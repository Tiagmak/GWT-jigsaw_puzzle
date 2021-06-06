package mpjp.quad;

import java.util.Set;

import mpjp.shared.HasPoint;

public abstract class Trie<T extends HasPoint> implements Element<T> {

	private static final int DEFAULT_CAPACITY = 8;

	static enum Quadrant {
		NW, NE, SE, SW;
	}

	static int capacity;
	// Par superior esquerdo (x,y)
	protected double topLeftX;
	protected double topLeftY;
	// Par inferior direito (x,y)
	protected double bottomRightX;
	protected double bottomRightY;

	protected Trie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.bottomRightX = bottomRightX;
		this.bottomRightY = bottomRightY;
		
		if(Trie.capacity == 0) {
			Trie.capacity = DEFAULT_CAPACITY;
		}
	}

	public static int getCapacity() {
		return capacity;
	}

	public static void setCapacity(int capacity) {
		Trie.capacity = capacity;
	}

	abstract T find(T point);

	abstract Trie<T> insert(T point);

	abstract Trie<T> insertReplace(T point);

	abstract void collectNear(double x, double y, double radius, Set<T> points);

	abstract void collectAll(Set<T> points);

	abstract void delete(T point);

	boolean overlaps(double x, double y, double radius) {
		double ClosestX = x - Math.max(topLeftX, Math.min(x, bottomRightX));
		double ClosestY = y - Math.max(bottomRightY, Math.min(y, topLeftY));
		return (ClosestX * ClosestX + ClosestY * ClosestY) <= (radius * radius);
	}

	@Override
	public String toString() {
		return "Trie [topLeftX=" + topLeftX + ", topLeftY=" + topLeftY + ", bottomRightX=" + bottomRightX
				+ ", bottomRightY=" + bottomRightY + "]";
	}

	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}

}
