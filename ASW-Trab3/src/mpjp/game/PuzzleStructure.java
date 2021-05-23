package mpjp.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.geom.Point;

public class PuzzleStructure implements Iterable<Integer> {

	private int rows, columns;
	private double width, height;
	
	/**
	 * Create an instance from raw data
	 * @param rows  - in puzzle's panel
	 * @param columns  - in puzzle's panel
	 * @param width  - of puzzle
	 * @param height  - of puzzle
	 */
	public PuzzleStructure(int rows, int columns, double width, double height) {
		super();
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Create instance from data in PuzzleInfo
	 * @param info - containing data for instantiation
	 */
	public PuzzleStructure(PuzzleInfo info) {
		super();
		this.rows = info.getRows();
		this.columns = info.getColumns();
		this.width = info.getWidth();
		this.height = info.getHeight();
	}
	
	/**
	 * Number of pieces in this piece structures  (rows × columns)
	 * @return picecCount
	 */
	
	public int getPieceCount() {
		return rows * columns;
	}
	
	/**
	 * Current number of rows
	 * @return rows
	 */
	
	public int getRows() {
		return rows;
	}
	
	/**
	 * Change number of rows
	 * @param rows - to change
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	/**
	 * Current number of columns
	 * @return columns
	 */
	public int getColumns() {
		return columns;
	}
	
	/**
	 * Change number of columns
	 * @param columns
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	/**
	 * Current width
	 * @return width
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Change width
	 * @param width - to change
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	
	/**
	 * Current height
	 * @return height
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Change height
	 * @param height
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
	 * Width of a panel cell assigned to a piece. Depends on the puzzle's width and number of rows
	 * @return pieceWidth
	 */
	public double getPieceWidth() {
		return width / columns;
	}
	
	/**
	 * Height of a panel cell assigned to a piece. Depends on the puzzle's height and number of rows
	 * @return pieceHeight
	 */
	public double getPieceHeight() {
		return height / rows;
	}
	
	/**
	 * The ID of the piece facing in the given direction the give piece 
	 * @param direction - where returned piece is facing
	 * @param id - of the piece used as reference
	 * @return id of facing piece, or null if no piece is facing in that direction
	 */
	public Integer getPieceFacing(Direction direction, Integer id) {
		Integer idFacingPiece = null;
		switch (direction) {
		case NORTH:
			idFacingPiece = (int) (id - columns);
			break;
		case SOUTH:
			idFacingPiece = (int) (id + columns);
			break;
		case EAST:
			idFacingPiece = (int) (id + 1);
			break;
		case WEST:
			idFacingPiece = (int) (id - 1);
			break;
		default:
			idFacingPiece = null;
		}

		if (idFacingPiece < 0 || idFacingPiece >= getPieceCount()) {
			idFacingPiece = null;
		} else {
			try {
				switch (direction) {
				case EAST:
					if (getPieceRow((int) id) != getPieceRow((int) idFacingPiece)) {
						idFacingPiece = null;
					}
					break;
				case WEST:
					if (getPieceRow((int) id) != getPieceRow((int) idFacingPiece)) {
						idFacingPiece = null;
					}
					break;
				default:
					break;
				}
			} catch (MPJPException e) {
				System.out.println("erro");
			}
		}
		return idFacingPiece;
	}
	
	/**
	 * The center of a matching piece facing in the given direction the piece with given center 
	 * @param direction - where returned piece center is facing
	 * @param base - at the center of the piece used as reference
	 * @return center of matching piece
	 */ 
	public Point getPieceCenterFacing(Direction direction, Point base) {
		double x = base.getX();
		double y = base.getY();
		double pieceWidth = getPieceWidth();
		double pieceHeight = getPieceHeight();
		Point newPoint;
		switch (direction) {
		case NORTH:
			newPoint = new Point(x, y - pieceHeight);
			break;
		case SOUTH:
			newPoint = new Point(x, y + pieceHeight);
			break;
		case EAST:
			newPoint = new Point(x + pieceWidth, y);
			break;
		case WEST:
			newPoint = new Point(x - pieceWidth, y);
			break;
		default:
			newPoint = null;
			break;
		}
		return newPoint;
	}
	
	/**
	 * The row of the given piece ID
	 * @param id  - of the piece
	 * @return row
	 * @throws MPJPException  - if id is invalid
	 */
	public int getPieceRow(int id) throws MPJPException {
		if (id < 0 || id >= rows * columns)
			throw new MPJPException("Invalid Id");
		return id / columns;
	}
	/**
	 * The column of given piece ID
	 * @param id  - of the piece
	 * @return column
	 * @throws MPJPException - if id is invalid
	 */
	public int getPieceColumn(int id) throws MPJPException {
		if (id < 0 || id >= rows * columns)
			throw new MPJPException("Invalid Id");
		return id % columns;
	}
	
	/**
	 * Locations of given piece in the final position, assuming the origin at (0,0) on the upper left corner
	 * @return map of IDs to locations
	 */
	public Map<Integer, Point> getStandardLocations() {
		Map<Integer, Point> locations = new HashMap<>();
		Iterator<Integer> iterator = iterator();
		while (iterator.hasNext()) {
			int id = iterator.next();
			try {
				locations.put(id, getPieceStandardCenter(id));
			} catch (MPJPException e) {
				e.printStackTrace();
			}
		}
		return locations;
	}
	
	/**
	 * Center of given piece in the final position, assuming the origin at (0,0) on the upper left corner
	 * @param id  - of piece
	 * @return     center as a Point
	 * @throws MPJPException  if id is invalid
	 */
	public Point getPieceStandardCenter(int id) throws MPJPException {
		if (id < 0 || id >= rows * columns)
			throw new MPJPException("Invalid ID");
		int pieceRow = getPieceRow(id);
		int pieceColumn = getPieceColumn(id);

		double pieceHeight = getPieceHeight();
		double pieceWidth = getPieceWidth();

		double x = (double) (pieceWidth * (pieceColumn) + pieceWidth / 2);
		double y = (double) (pieceHeight * (pieceRow) + pieceHeight / 2);

		Point pieceStandardCenter = new Point(x, y);

		return pieceStandardCenter;
	}
	
	/**
	 * Set of pieces where a point might be located in a complete puzzle. Center of coordinates in to top left corner of the puzzle
	 * @param point
	 * @return set of piece IDs (integers) that may contain given point
	 */
	public Set<Integer> getPossiblePiecesInStandarFor(Point point) {
		Set<Integer> possibleSet = new HashSet<Integer>();
		Iterator<Integer> iterator = iterator();
		while (iterator.hasNext()) {
			int idPoint = iterator.next();
			Point centerIdPoint;

			try {
				centerIdPoint = getPieceStandardCenter(idPoint);

				double minXRange = centerIdPoint.getX() - 2 * getPieceWidth();
				double maxXRange = centerIdPoint.getX() + 2 * getPieceWidth();
				double minYRange = centerIdPoint.getY() - 2 * getPieceHeight();
				double maxYRange = centerIdPoint.getY() + 2 * getPieceHeight();

				if (point.getX() > minXRange && point.getX() < maxXRange && point.getY() > minYRange
						&& point.getY() < maxYRange) {
					possibleSet.add(idPoint);
				}
			} catch (MPJPException e) {
				e.printStackTrace();
			}
		}

		return possibleSet;
	}
	
	/**
	 * A random point in the standard puzzle, with origin (0,0) at the upper left corner and bottom right corner a (width,height).
	 * @return     point in standard puzzle
	 */
	public Point getRandomPointInStandardPuzzle() {
		Random rand = new Random();
		double x = rand.nextDouble() * width;
		double y = rand.nextDouble() * height;
		Point newRandomPoint = new Point(x, y);
		return newRandomPoint;
	}

	/**
	 * Iterator over piece IDs, an integer from 0 (inclusive) to the number of pieces (exclusive), in ascending order.
	 * @return  iterator - iterator in interface java.lang.Iterable<java.lang.Integer>
	 */
	@Override
	public Iterator<Integer> iterator() {
		Iterator<Integer> iterator = new Iterator<Integer>() {
			private int currentId = 0;

			@Override
			public boolean hasNext() {
				return currentId < getPieceCount();
			}

			@Override
			public Integer next() {
				return currentId++;
			}
		};
		return iterator;
	}
}
