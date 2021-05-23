package mpjp.shared;
import java.io.Serializable;
import java.util.Map;
import java.util.List;

/**
 * Current puzzle layout, the status (position, rotation) of each piece, and the blocks pieces fit together.
 */
public class PuzzleLayout implements Serializable{

	private static final long serialVersionUID = 4895946914725783438L;
	
	/**
	 * blocks - Map from block IDs (integers) to the list IDs of the pieces it contains
	 * percentageSolved - Percentage of the puzzle already solved ([0 - 100])
	 * pieces - Map from piece IDs (integers) to piece status
	 */
	private Map<Integer, List<Integer>> blocks;
	private int percentageSolved;
	private Map<Integer, PieceStatus> pieces;
	
	public PuzzleLayout() {
		super();
	}

	/**
	 * An instance created from fields
	 * @param pieces - map of piece IDs no status
	 * @param blocks - map of block IDs to list of pieces IDs in that block
	 * @param percentageSolved - percentage of the puzzle solved ([0, 100])
	 */
	public PuzzleLayout(Map<Integer, PieceStatus> pieces, Map<Integer, List<Integer>> blocks, int percentageSolved) {
		super();
		this.blocks = blocks;
		this.percentageSolved = percentageSolved;
		this.pieces = pieces;
	}

	public Map<Integer, List<Integer>> getBlocks() {
		return blocks;
	}

	/**
	 * Percentage in which puzzle is solved.
	 * @return - percentage complete ([0 - 100]
	 */
	public int getPercentageSolved() {
		return percentageSolved;
	}

	/**
	 * The list of pieces belonging to a block indexed by bloc id (a positive integer)
	 * @return - map of block IDs to lists of piece IDs
	 */
	public Map<Integer, PieceStatus> getPieces() {
		return pieces;
	}
	
	/**
	 * The puzzle is complete when it has a single block When 2 pieces are fit together, they become part of the same block, as all the other pieces belonging to their blocks
	 * @return - true if solved; false otherwise
	 */
	public boolean isSolved(){
		return blocks.size() == 1;
	}
}
