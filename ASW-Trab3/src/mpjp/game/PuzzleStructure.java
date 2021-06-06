package mpjp.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.geom.Point;

public class PuzzleStructure implements Iterable<Integer> {
	private int rows;
	private int columns;
	private double width;
	private double height;
	
	public PuzzleStructure(int rows, int columns, double width, double height) {
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}

	public PuzzleStructure(PuzzleInfo info) {
		this(info.getRows(), info.getColumns(), info.getWidth(), info.getHeight());
	}
	
	void init(int rows, int columns, double width, double height) {
		// does nothing
	}
	
	public int getPieceCount() {
		return this.rows * this.columns;
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public double getWidth() {
		return width;
	}

	void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	void setHeight(double height) {
		this.height = height;
	}

	public double getPieceWidth() {
		return getWidth() / getColumns();
	}
	
	public double getPieceHeight() {
		return getHeight() / getRows();
	}
	
	public Integer getPieceFacing(Direction direction, Integer id) {
		
		try {
			if (!(  (id == null) ||
					(getPieceColumn(id) == getColumns()-1 	&& direction.equals(Direction.EAST))  ||
					(getPieceColumn(id) == 0 				&& direction.equals(Direction.WEST))  ||
					(getPieceRow(id) 	== getRows()-1 		&& direction.equals(Direction.SOUTH)) ||
					(getPieceRow(id) 	== 0 				&& direction.equals(Direction.NORTH)) )) {
						
						int idInDirection = id + direction.getSignalY() * getColumns() + direction.getSignalX();
						
						if (!checkInvalidId(idInDirection)) {
							return idInDirection;
						}
				}
		} catch (MPJPException e) {}
		
		return null;
	}

	Point getPieceCenterFacing(Direction direction, Point point) {
		switch(direction) {
		case NORTH:
			return new Point(point.getX(), point.getY() - getPieceHeight());
		case SOUTH:
			return new Point(point.getX(), point.getY() + getPieceHeight());
		case WEST:
			return new Point(point.getX() - getPieceWidth(), point.getY());
		case EAST:
			return new Point(point.getX() + getPieceWidth(), point.getY());
		default:
			return null;
		}
	}
	
	private boolean checkInvalidId(int id) {
		return id < 0 || id >= getPieceCount();
	}
	
	private void invalidID(int id) throws MPJPException {
		if (checkInvalidId(id)) {
			throw new MPJPException("ID invalid");
		}
	}
	
	public int getPieceRow(int id) throws MPJPException {
		invalidID(id);
		return id / getColumns();
	}
	
	public int getPieceColumn(int id) throws MPJPException {
		invalidID(id);
		return id % getColumns();
	}
	
	public Map<Integer, Point> getStandardLocations() {
		HashMap<Integer, Point> map = new HashMap<>();
		
		for(int id = 0; id < getPieceCount(); id++) {
			try {
				map.put(id, getPieceStandardCenter(id));
			} catch (MPJPException e) {}
		}
		
		return map;
	}
	
	public Point getPieceStandardCenter(int id) throws MPJPException {
		invalidID(id);
		double centerX = (getPieceColumn(id) * getPieceWidth()) + getPieceWidth()/2;
		double centerY = (getPieceRow(id) * getPieceHeight()) + getPieceHeight()/2;
		return new Point(centerX, centerY);
	}
	
	private int getIDfromPos(Point point) {
		int xcount = (int) (point.getX()/getPieceWidth());
		int ycount = (int) (point.getY()/getPieceHeight());

		return ycount * getColumns() + xcount;
	}
	
	public Set<Integer> getPossiblePiecesInStandarFor(Point point) {
		Set<Integer> possible = new HashSet<>();	
		int id = getIDfromPos(point);	
		
		possible.add(id);
		for(Direction d : Direction.values()) {
			Integer cur = getPieceFacing(d, id);
			possible.add(cur);
			
			if (d.equals(Direction.NORTH) || d.equals(Direction.SOUTH)) {
				possible.add(getPieceFacing(Direction.WEST, cur));				
				possible.add(getPieceFacing(Direction.EAST, cur));
			}
		}
		
		possible.remove(null);
		return possible;
	}
	
	public Point getRandomPointInStandardPuzzle() {
		Random rand = new Random();
		double pointX = rand.nextDouble() * (getColumns() * getPieceWidth());
		double pointY = rand.nextDouble() * (getRows() * getPieceHeight());
		
		return new Point(pointX, pointY);
	}
	
	public Iterator<Integer> iterator() {
		return IntStream.range(0, getPieceCount()).iterator();
	}
}