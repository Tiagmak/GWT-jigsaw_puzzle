package mpjp.quad;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mpjp.shared.HasPoint;

public class NodeTrie<T extends HasPoint> extends Trie<T>{

	Map<Trie.Quadrant, Trie<T>> tries;
	
	protected NodeTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		tries = new HashMap<>();
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
	 * @param points - set of HasPoint for collecting points
	 */
	@Override
	void collectAll(Set<T> points) {
		for (Quadrant t : tries.keySet()) {
			Trie<T> temp = tries.get(t);
			temp.collectAll(points);
		}
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
		for (Quadrant t : tries.keySet()) {
			Trie<T> temp = tries.get(t);
			if(temp != null && temp.overlaps​(x,y,radius)) {
				temp.collectNear(x, y, radius, points);
			}		
		}
	}
	
	/**
	 * Delete given point
	 * @param point - to delete
	 */
	@Override
	void delete(T point) {
		Trie<T> selectedTrie = tries.get(quadrandOf(point));
		if(selectedTrie != null) {
			selectedTrie.delete(point);
		}
	}

	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point - with requested coordinates
	 * @return - recorded point, if found; null otherwise
	 */
	@Override
	T find(T point) {
		Trie<T> selectedTrie = tries.get(quadrandOf(point));
		if(selectedTrie != null) {
			return selectedTrie.find(point);
		}
		return null;
	}

	/**
	 * A collection of tries that descend from this one
	 * @return - collection tries
	 */
	public Collection<Trie<T>> getTries() {
		return tries.values();
	}

	/**
	 * Insert given point
	 * @param point - to be inserted
	 * @return - changed parent node
	 */
	@Override
	Trie<T> insert(T point) {
		Trie.Quadrant selectedQuadrant = quadrandOf(point);
		Trie<T> selectedTrie = tries.get(selectedQuadrant);
		if(selectedTrie == null) {
			selectedTrie = creatLeaf(selectedQuadrant);
		}
		selectedTrie = selectedTrie.insert(point);
		tries.put(selectedQuadrant, selectedTrie);
		return this;
	}
	
	/**
	 * create e new LeafTrie
	 * @param quadrant
	 * @return - the new LeafTrie according with the respective quadrant
	 */
	private Trie<T> creatLeaf(Trie.Quadrant quadrant){
		
		double middleX = this.topLeftX + ((this.bottomRightX - this.topLeftX)/2.0);
		double middleY = this.bottomRightY + ((this.topLeftY - this.bottomRightY)/2.0);
		
		switch (quadrant) {
		case NW: {
			return new LeafTrie<>(this.topLeftX, this.topLeftY, middleX, middleY); 			
		}
		case NE:{
			return new LeafTrie<>(middleX, this.topLeftY, this.bottomRightX, middleY); 
		}
		case SW:{
			return new LeafTrie<>(this.topLeftX, middleY, middleX, this.bottomRightY); 
		}
		case SE:{
			return new LeafTrie<>(middleX, middleY, this.bottomRightX, this.bottomRightY);
		}
		default:
			return null;
		}
	}
	
	/**
	 * Insert given point, replacing existing points in same location
	 * @param point - point to be inserted
	 * @return - changed parent node
	 */
	@Override
	Trie<T> insertReplace(T point) {
		Trie.Quadrant quadr = quadrandOf(point);
		Trie<T> temp = tries.get(quadr);
		
		temp = temp.insertReplace(point);
		tries.put(quadr, temp);
		return this;
	}

	
	Trie.Quadrant quadrandOf(T point) {
		double middleX = this.topLeftX + ((this.bottomRightX - this.topLeftX)/2.0);
		double middleY = this.bottomRightY + ((this.topLeftY - this.bottomRightY)/2.0);
		
		if(point.getY() < middleY) {
			if(point.getX() < middleX) {
				return Trie.Quadrant.SW;
			}
			else {
				return Trie.Quadrant.SE;
			}
		}
		else {
			if(point.getX() < middleX) {
				return Trie.Quadrant.NW;
			}
			else {
				return Trie.Quadrant.NE;
			}
		}
		
	}
}
