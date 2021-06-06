package mpjp.quad;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mpjp.shared.HasPoint;

public class NodeTrie<T extends HasPoint> extends Trie<T> {
	Map<Trie.Quadrant, Trie<T>> tries;

	NodeTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		tries = new HashMap<>();
	}

	T find(T point) {
		Trie<T> trie = tries.get(quadrantOf(point));

		if (trie != null)
			return trie.find(point);
		else
			return null;
	}

	Trie.Quadrant quadrantOf(T point) {
		double midX = (this.topLeftX + this.bottomRightX) / 2;
		double midY = (this.topLeftY + this.bottomRightY) / 2;

		if (point.getY() > midY) {
			if (point.getX() > midX) {
				return Trie.Quadrant.NE;
			} else {
				return Trie.Quadrant.NW;
			}
		} else {
			if (point.getX() > midX) {
				return Trie.Quadrant.SE;
			} else {
				return Trie.Quadrant.SW;
			}
		}
	}

	private LeafTrie<T> newLeaf(Trie.Quadrant pos) {
		double midX = (this.topLeftX + this.bottomRightX) / 2;
		double midY = (this.topLeftY + this.bottomRightY) / 2;

		switch (pos) {
		case NW:
			return new LeafTrie<T>(this.topLeftX, this.topLeftY, midX, midY);
		case NE:
			return new LeafTrie<T>(midX, this.topLeftY, this.bottomRightX, midY);
		case SW:
			return new LeafTrie<T>(this.topLeftX, midY, midX, this.bottomRightY);
		case SE:
			return new LeafTrie<T>(midX, midY, this.bottomRightX, this.bottomRightY);
		}
		return null;

	}

	Trie<T> insert(T point) {
		Trie.Quadrant pos = quadrantOf(point);
		Trie<T> trie = tries.get(pos);

		if (trie == null) {
			trie = newLeaf(pos);
		}

		trie = trie.insert(point);
		tries.put(pos, trie);
		return this;
	}

	Trie<T> insertReplace(T point) {
		Trie.Quadrant pos = quadrantOf(point);
		Trie<T> trie = tries.get(pos);

		if (trie == null) {
			trie = newLeaf(pos);
		}

		trie = trie.insertReplace(point);
		tries.put(pos, trie);
		return this;
	}

	void delete(T point) {
		Trie<T> trie = tries.get(quadrantOf(point));

		if (trie != null) {
			trie.delete(point);
		}

		return;
	}

	void collectNear(double x, double y, double radius, Set<T> nodes) {
		for (Trie.Quadrant q : Trie.Quadrant.values()) {
			Trie<T> trie = tries.get(q);
			if (trie != null && trie.overlaps(x, y, radius)) {
				trie.collectNear(x, y, radius, nodes);
			}
		}
	}

	void collectAll(Set<T> nodes) {
		for (Trie.Quadrant q : Trie.Quadrant.values()) {
			Trie<T> trie = tries.get(q);
			if (trie != null) {
				trie.collectAll(nodes);
			}
		}
	}

	@Override
	public String toString() {
		return "NodeTrie [tries=" + tries + "]";
	}

	public void accept(Visitor<T> visitor) {
		visitor.visit(this);
	}

	Collection<Trie<T>> getTries() {
		return tries.values();
	}

}
