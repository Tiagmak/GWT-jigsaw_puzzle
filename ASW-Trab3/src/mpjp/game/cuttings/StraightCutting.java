package mpjp.game.cuttings;

import java.util.HashMap;
import java.util.Map;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;


public class StraightCutting implements Cutting{
	
	public StraightCutting() {
		super();
	}
	
	/**
	 * Produce a map of piece ID (Integer) to PieceShape
	 * @param structure -  - an instance of PuzzleStructure
	 * @return  map
	 */
	@Override
	public Map<Integer, PieceShape> getShapes(PuzzleStructure structure) {
		Map<Integer, PieceShape> puzzle = new HashMap<>();
		double x = structure.getPieceWidth()/2;
		double y = structure.getPieceHeight()/2;
		
		int rows = structure.getRows();
		int columns = structure.getColumns();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				Point initialPoint = new Point(x,y);
				PieceShape pieceShape = new PieceShape(initialPoint)
						.addSegment(new LineTo(new Point(x , -y)))
						.addSegment(new LineTo(new Point(-x , -y)))
						.addSegment(new LineTo(new Point(-x , y)))
						.addSegment(new LineTo(new Point(x , y)));
				puzzle.put(i * columns + j, pieceShape);
			}
		}
		return puzzle;
	}
}
