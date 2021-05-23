

package mpjp.quad;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mpjp.shared.HasPoint;

public class PointQuadtree<T extends HasPoint> implements Iterable<T> {

	public class PointIterator implements Runnable, Iterator<T>, Visitor<T> {
		T nextPoint;
		boolean terminated;
		Thread thread;

		PointIterator() {
			thread = new Thread(this, "PointIterator");
			thread.start();
		}

		/**
		 * Do a visit to a leaf in the composite structure
		 * @param leaf - to be visited
		 */
		@Override
		public void visit(LeafTrie<T> leaf) {
			for (T point : leaf.getPoints()) {
				synchronized (this) {
					if (nextPoint != null)
						handshake();
					nextPoint = point;
					handshake();
				}
			}
		}

		/**
		 * Do a visit to a node in the composite structure
		 * @param node - to be visited
		 */
		@Override
		public void visit(NodeTrie<T> node) {
			for (Trie<T> quadrant : node.getTries()) {
				quadrant.accept(this);
			}
		}

		/**
		 * 
		 */
		@Override
		public boolean hasNext() {
			synchronized (this) {
				if (!terminated)
					handshake();
			}
			return nextPoint != null;
		}

		@Override
		public T next() {
			T value = nextPoint;
			synchronized (this) {
				nextPoint = null;
			}
			return value;
		}

		@Override
		public void run() {
			terminated = false;

			top.accept(this);

			synchronized (this) {
				terminated = true;
				handshake();
			}
		}

		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected interruption while waiting", cause);
			}
		}
	}

	Trie<T> top;

	@Override
	public Iterator<T> iterator() {
		return new PointIterator();
	}

	/**
	 * Create a quad tree for points in a rectangle with given top left and bottom right corners. 
	 * This is typically used for a region in the Cartesian plane, as used in mathematics, and can also be used for geographic coordinates.
	 * @param topLeftX - x coordinate of top left corner
	 * @param topLeftY - y coordinate of top left corner
	 * @param bottomRightX - x coordinate of bottom right corner
	 * @param bottomRightY - y coordinate of bottom right corner
	 */
	public PointQuadtree(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		top = new LeafTrie<>(topLeftX, topLeftY, bottomRightX, bottomRightY);

	}

	/**
	 * Create a quad tree for a rectangle with given width and height. 
	 * It assumes the coordinate origin {code (0,0)} at the upper left corner, with y coordinates increasing downwards. 
	 * An instance created with new PointQuadtree(with,height) is equivalent to an instance created with new PointQuadtree(0,height,width,0)
	 * @param width - of quad tree
	 * @param height - of quad tree
	 */
	public PointQuadtree(double width, double height) {
		this(0, height, width, 0);
	}

	/**
	 * Create a quad tree for a rectangle with given dimensions and a margin. 
	 * It assumes the coordinate origin {code (0,0)} at the upper left corner, with y coordinates increasing downwards. 
	 * The quad tree has a margin that is clipped by the window, so that points may actually outside the visible window. 
	 * The upper left point is at (-margin,height+margin) and the lower right at (width+margin,-margin(. 
	 * An instance created with new PointQuadtree(with,height,margin) is equivalent to an instance created with new PointQuadtree(-margin,height+margin,width+margin,-margin)
	 * @param width - of the quad tree's visible part
	 * @param height - of the quad tree's visible part
	 * @param margin - invisible part of quad tree, surrounding rectangle with given dimensions
	 */
	public PointQuadtree(double width, double height, double margin) {
		this(-margin, height + margin, width + margin, -margin);
	}

	/**
	 * Delete given point from QuadTree, if it exists there
	 * @param point - to be deleted
	 */
	public void delete(T point) {
		top.delete(point);
	}

	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point - with requested coordinates
	 * @return - recorded point, if found; null otherwise
	 */
	public T find(T point) {
		return top.find(point);
	}

	/**
	 * Returns a set of points at a distance smaller or equal to radius from point with given coordinates.
	 * @param x - coordinate of point
	 * @param y - coordinate of point
	 * @param radius - from given point
	 * @return - set of instances of type HasPoint
	 */
	public Set<T> findNear(double x, double y, double radius) {
		Set<T> points = new HashSet<T>();
		top.collectNear(x, y, radius, points);
		return points;
	}

	/**
	 * A set with all points in the QuadTree
	 * @return - set of instances of type HasPoint
	 */
	public Set<T> getAll() {
		Set<T> points = new HashSet<T>();
		top.collectAll(points);
		return points;
	}

	/**
	 * Insert given point in the QuadTree
	 * @param point - to be inserted
	 */
	public void insert(T point) {
		if (!insideLimits(point)) {
			throw new PointOutOfBoundException();
		}
		top = top.insert(point);
	}

	/**
	 * Insert point, replacing existing point in the same position
	 * @param point - point to be inserted
	 */
	public void insertReplace(T point) {
		if (!insideLimits(point)) {
			throw new PointOutOfBoundException();
		}
		top = top.insertReplace(point);
	}

	/**
	 * check if a point is inside of the limites of the quad
	 * @param point - to analyse if is inside
	 * @return - true if inside the limits
	 */
	private boolean insideLimits(T point) {
		if ((point.getX() <= top.bottomRightX && point.getX() >= top.topLeftX)
				&& (point.getY() <= top.topLeftY && point.getY() >= top.bottomRightY)) {
			return true;
		}
		return false;
	}
}
