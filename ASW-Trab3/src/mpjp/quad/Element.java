package mpjp.quad;

import mpjp.shared.HasPoint;

public interface Element<T extends HasPoint>{
	/**
	 * Accept a visitor to operate on a node of the composite structure
	 * @param visitor - to the node
	 */
	void accept(Visitor<T> visitor);
}
