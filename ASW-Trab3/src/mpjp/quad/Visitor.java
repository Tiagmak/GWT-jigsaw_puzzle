package mpjp.quad;

import mpjp.shared.HasPoint;

public interface Visitor<T extends HasPoint> {
	void visit(LeafTrie<T> leaf);
	void visit(NodeTrie<T> node);
}
