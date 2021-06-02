package mpjp.quad;

import mpjp.shared.HasPoint;
import java.util.*;

public class LeafTrie<T extends HasPoint> extends Trie<T> {

	private List<T> save;

	protected LeafTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		save = new ArrayList<T>();
	}

	/**
	 * Accept a visitor to operate on a node of the composite structure
	 * @param visitor - to the node
	 */
	@Override
	public void accept(Visitor<T> visitor) {
		visitor.visit(this);
	}

	/**
	 * Collect all points in this node and its descendants in given set
	 * @param nodes - set of HasPoint for collecting points
	 */
	@Override
	void collectAll(Set<T> points) {
		points.addAll(save);
	}

	/**
	 * Collect points at a distance smaller or equal to radius from (x,y) and place them in given list
	 * @param x - coordinate of point
	 * @param y - coordinate of point
	 * @param radius - from given point
	 * @param nodes - set for collecting points
	 */
	@Override
	void collectNear(double x, double y, double radius, Set<T> points) {
		for (T i : save) {
			if (getDistance(i.getX(), i.getY(), x, y) <= radius) {
				points.add(i);
			}
		}
	}

	/**
	 * Delete given point
	 * @param point - to delete
	 */
	@Override
	void delete(T point) {
		save.remove(point);
	}

	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point - with requested coordinates
	 * @return - recorded point, if found; null otherwise
	 */
	@Override
	T find(T point) {
		for (T i : save) {
			if (i.getX() == point.getX() && i.getY() == point.getY()) {
				return i;
			}
		}
		return null;
	}

	/**
	 * A collection of points currently in this leaf
	 * @return - collection of points
	 */
	Collection<T> getPoints() {
		return save;
	}

	/**
	 * Insert given point
	 * @param point - to be inserted
	 * @return - changed parent node
	 */
	@Override
	Trie<T> insert(T point) {
		if (save.size() < Trie.capacity) {
			save.add(point);
			return this;
		}

		NodeTrie<T> newNodeTrie = new NodeTrie<>(this.topLeftX, this.topLeftY, this.bottomRightX, this.bottomRightY);
		for (T t : save) {
			newNodeTrie.insert(t);
		}
		newNodeTrie.insert(point);
		return newNodeTrie;
	}

	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point - with requested coordinates
	 * @return - recorded point, if found; null otherwise
	 */
	@Override
	Trie<T> insertReplace(T point) {
		for (Iterator<T> it = save.iterator(); it.hasNext();) {
			T nextT = it.next();
			if (nextT.getX() == point.getX() && nextT.getY() == point.getY()) {
				it.remove();
			}
		}
		return this.insert(point);
	}

	@Override
	public String toString() {
		return "LeafTrie [save=" + save + "]";
	}
}
