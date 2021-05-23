package mpjp.shared;

import java.util.Date;

/**
 *Information required to select a puzzle currently being solved. 
 *It will be used by users to decide to join to an existing workspace. 
 *Most of the information is the same used for workspace creation, thus it extends PuzzleInfo.
 *The extra information is the start date and the percentage of the puzzle that is solved.
 */
public class PuzzleSelectInfo extends PuzzleInfo{

	/**
	 * percentageSolved - Percentage of the puzzle solved ([0, 100])
	 * start - Moment when the jigsaw puzzle started being solved
	 */
	private static final long serialVersionUID = 1L;
	private int percentageSolved;
	Date start;
	
	
	public PuzzleSelectInfo() {
		super();
	}

	/**
	 * Creates an instance from a puzzle view and start date and percentage solved
	 * @param info - used to create the workspace
	 * @param start2 - date/time when this puzzle started being solved
	 * @param percentageSolved - percentage [0, 100] off the puzzle already solved
	 */
	public PuzzleSelectInfo(PuzzleInfo info, Date start2, int percentageSolved) {
		super();
		this.percentageSolved = percentageSolved;
		this.start = start2;
	}

	/**
	 * Percentage of the puzzle that is solved, an integer in [0,100]
	 * @return - percentage
	 */
	public int getPercentageSolved() {
		return percentageSolved;
	}

	/**
	 * The moment when the puzzle start being solved
	 * @return - start date
	 */
	public Date getStart() {
		return start;
	}
}
