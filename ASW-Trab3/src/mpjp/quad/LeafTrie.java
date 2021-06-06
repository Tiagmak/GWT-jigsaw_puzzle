package mpjp.quad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import mpjp.shared.HasPoint;

public class LeafTrie<T extends HasPoint> extends Trie<T> {

	private ArrayList<T> list;

	LeafTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		this.list = new ArrayList<T>(capacity);
	}

	T find(T point) {
		for (T v : this.list) {
			if (equals(v, point))
				return v;
		}
		return null;
	}

	private boolean equals(T p1, T p2) {
		return p1.getX() == p2.getX() && p1.getY() == p2.getY();
	}

	private boolean isFull() {
		return list.size() == capacity;
	}

	Trie<T> insert(T point) {
		if (isFull()) {
			NodeTrie<T> node = new NodeTrie<>(this.topLeftX, this.topLeftY, this.bottomRightX, this.bottomRightY);

			for (T n : list) {
				node.insert(n);
			}

			node.insert(point);
			return node;
		} else {
			list.add(point);
			return this;
		}

	}

	Trie<T> insertReplace(T point) {

		list.removeIf(p -> equals(p, point));
		return this.insert(point);
	}

	void delete(T point) {
		list.remove(point);
	}

	private boolean containsPoint(T point, double x, double y, double radius) {
		return getDistance(point.getX(), point.getY(), x, y) <= radius;
	}

	void collectNear(double x, double y, double radius, Set<T> nodes) {
		for (T p : list) {
			if (containsPoint(p, x, y, radius)) {
				nodes.add(p);
			}
		}
	}

	void collectAll(Set<T> nodes) {
		nodes.addAll(list);
	}

	@Override
	public String toString() {
		return "LeafTrie [toString()=" + super.toString() + "]";
	}

	public void accept(Visitor<T> visitor) {
		visitor.visit(this);
	}

	Collection<T> getPoints() {
		return list;
	}
}
