package mpjp.quad;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mpjp.shared.HasPoint;

public class PointQuadtree<T extends HasPoint> implements Iterable<T> {

	class PointIterator implements Iterator<T>, Runnable, Visitor<T> {

		Thread thread;
		boolean terminated;
		T nextPoint;

		PointIterator() {
			thread = new Thread(this);
			thread.start();
		}

		public void run() {
			terminated = false;

			top.accept(this);

			synchronized (this) {
				terminated = true;
				handshake();
			}
		}

		public boolean hasNext() {
			synchronized (this) {
				if (!terminated)
					handshake();
			}
			return nextPoint != null;
		}

		public T next() {
			T point = nextPoint;

			synchronized (this) {
				nextPoint = null;
			}

			return point;
		}

		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected interruption while waiting", cause);
			}
		}

		public void visit(NodeTrie<T> node) {
			for (Trie<T> t : node.getTries()) {
				t.accept(this);
			}
		}

		public void visit(LeafTrie<T> leaf) {
			for (T v : leaf.getPoints()) {
				synchronized (this) {
					if (nextPoint != null) {
						handshake();
					}
					nextPoint = v;
					handshake();
				}
			}
		}
	}

	Trie<T> top;

	public PointQuadtree(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		top = new LeafTrie<T>(topLeftX, topLeftY, bottomRightX, bottomRightY);
	}

	public PointQuadtree(double width, double height) {
		this(0.0, height, width, 0.0);
	}

	public PointQuadtree(double width, double height, double margin) {
		this(-margin, height + margin, width + margin, -margin);
	}

	public T find(T point) {
		return top.find(point);
	}

	private boolean pointIntoRec(T point) {
		return point.getX() >= top.topLeftX && point.getX() <= top.bottomRightX && point.getY() >= top.bottomRightY
				&& point.getY() <= top.topLeftY;
	}

	public void insert(T point) {
		if (!pointIntoRec(point)) {
			throw new PointOutOfBoundException();
		}

		top = top.insert(point);
	}

	public void insertReplace(T point) {
		if (!pointIntoRec(point)) {
			throw new PointOutOfBoundException();
		}

		top = top.insertReplace(point);
	}

	public Set<T> findNear(double x, double y, double radius) {
		Set<T> temp = new HashSet<T>();
		top.collectNear(x, y, radius, temp);

		return temp;
	}

	public void delete(T point) {
		top.delete(point);
	}

	public Set<T> getAll() {
		Set<T> temp = new HashSet<T>();
		top.collectAll(temp);

		return temp;
	}

	public Iterator<T> iterator() {
		return new PointIterator();
	}
}
