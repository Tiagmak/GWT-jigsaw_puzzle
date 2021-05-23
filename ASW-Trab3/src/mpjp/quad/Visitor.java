package mpjp.quad;

import mpjp.shared.HasPoint;

public interface Visitor<T extends HasPoint>{
	/**
	 * Do a visit to a leaf in the composite structure
	 * @param leaf - to be visited
	 */
	void visit(LeafTrie<T> leaf);
	
	/**
	 * Do a visit to a node in the composite structure
	 * @param node - to be visited
	 */
	void visit(NodeTrie<T> node);
}
