package mpjp.shared;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class PuzzleView implements Serializable{

	static final long serialVersionUID = -4971465667917080717L;
	String image;
	Map<Integer, Point> locations;
	double pieceHeight, pieceWidth, puzzleHeight, puzzleWidth, workspaceHeight ,workspaceWidth;
	Map<Integer, PieceShape> shapes;
	Date start;
	
	/**
	 * Create an empty constructor
	 */
	public PuzzleView() {
		
	}

	/**
	 * Create an instance from field values
	 * @param start - date of solving this puzzle
	 * @param workspaceWidth - workspace's width
	 * @param workspaceHeight - workspace's height
	 * @param puzzleWidth - puzzle's width
	 * @param puzzleHeight - puzzle's height
	 * @param pieceWidth - piece cell's width
	 * @param pieceHeight - piece cells's height
	 * @param image - of puzzle
	 * @param shapes - of pieces (map of piece indexes to shapes)
	 * @param locations - of pieces in complete puzzle (map of piece indexes to points)
	 */
	public PuzzleView(Date start, double workspaceWidth, double workspaceHeight, double puzzleWidth,
			double puzzleHeight, double pieceWidth, double pieceHeight, String image, Map<Integer, PieceShape> shapes,
			Map<Integer, Point> locations) {
		this.image = image;
		this.locations = locations;
		this.pieceHeight = pieceHeight;
		this.pieceWidth = pieceWidth;
		this.puzzleHeight = puzzleHeight;
		this.puzzleWidth = puzzleWidth;
		this.workspaceHeight = workspaceHeight;
		this.workspaceWidth = workspaceWidth;
		this.shapes = shapes;
		this.start = start;
	}

	/**
	 * Image on the puzzle. It may be null for a blank puzzle.
	 * @return - image
	 */
	public String getImage() { 
		return image;
	}

	/**
	 * Location of piece in the complete puzzle (with pieces connected). Position (0,0) is the top left corner.
	 * @param id - of piece
	 * @return - Point
	 */
	public Point getStandardPieceLocation​(int id){
		return this.locations.get(id);
		
	}
	
	/**
	 * Height of the puzzle being solved
	 * @return - height
	 */
	public double getPieceHeight() {
		return pieceHeight;
	}

	/**
	 * Width of the puzzle being solved
	 * @return - width
	 */
	public double getPieceWidth() { 
		return pieceWidth;
	}

	/**
	 * Height of the puzzle being solved
	 * @return - height
	 */
	public double getPuzzleHeight() {
		return puzzleHeight;
	}

	/**
	 * Width of the puzzle being solved
	 * @return - width
	 */
	public double getPuzzleWidth() {
		return puzzleWidth;
	}

	/**
	 * Height of the workspace where the puzzle is solved
	 * @return - height
	 */
	public double getWorkspaceHeight() { 
		return workspaceHeight;
	}

	/**
	 * Width of the workspace where the puzzle is solved
	 * @return - width
	 */
	public double getWorkspaceWidth() { 
		return workspaceWidth;
	}
	
	/**
	 * Shape of piece with given id
	 * @param id - of piece
	 * @return - PieceShape
	 */
	public PieceShape getPieceShape(int id) {
		return this.shapes.get(id);
	}

	/**
	 * Date when this puzzle was started being solved
	 * @return - start Date
	 */
	public Date getStart() {    
		return start;
	}
}