package mpjp.game.cuttings;

import java.util.HashMap;
import java.util.Map;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.CurveTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class StandardCutting implements Cutting {
	double pieceWidth;
	double pieceheight;
	
	public StandardCutting() {
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
		pieceWidth = structure.getPieceWidth();
		pieceheight = structure.getPieceHeight();
		double x = pieceWidth / 2;
		double y = pieceheight / 2;

		int rows = structure.getRows();
		int columns = structure.getColumns();

		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				PieceShape pieceShape = null;
				
				if (j == 0 ) {
					pieceShape = new PieceShape(new Point(x,y))		

						.addSegment(new CurveTo(new Point(x,0),new Point(x+x/2, y-y/2),new Point(x+x/2, 0)))
						.addSegment(new CurveTo(new Point(x + x/2, -y/2),new Point(x,0),new Point(x, -y)))
						.addSegment(new CurveTo(new Point(0, -y),new Point(0, -y),new Point(0, -y)))
						.addSegment(new CurveTo(new Point(-x,-y),new Point(-x,-y),new Point(-x,-y)))
						.addSegment(new CurveTo(new Point(-x, 0),new Point(-x, 0),new Point(-x, 0)))
						.addSegment(new CurveTo(new Point(-x,y),new Point(-x,y),new Point(-x,y)))
						.addSegment(new CurveTo(new Point(0,y),new Point(0,y),new Point(0,y)))
						.addSegment(new CurveTo(new Point(x, y),new Point(x, y),new Point(x, y)));
				}
				
				else{
					pieceShape = new PieceShape(new Point(x,y))		
							.addSegment(new CurveTo(new Point(x,0),new Point(x+x/2, y-y/2),new Point(x+x/2, 0)))
							.addSegment(new CurveTo(new Point(x + x/2, -y/2),new Point(x,0),new Point(x, -y)))
							
							.addSegment(new CurveTo(new Point(0, -y),new Point(0, -y),new Point(0, -y)))
							.addSegment(new CurveTo(new Point(-x,-y),new Point(-x,-y),new Point(-x,-y)))
							
							.addSegment(new CurveTo(new Point(-x, 0),new Point(-x+x/2, -y+y/2),new Point(-x+x/2, 0)))
							.addSegment(new CurveTo(new Point(-x+x/2,y/2),new Point(-x,0),new Point(-x,y)))
							
							.addSegment(new CurveTo(new Point(0,y),new Point(0,y),new Point(0,y)))
							.addSegment(new CurveTo(new Point(x, y),new Point(x, y),new Point(x, y)));
				}
				
				
				puzzle.put(i * columns + j, pieceShape);
			}
		}
		return puzzle;
	}
	
}
