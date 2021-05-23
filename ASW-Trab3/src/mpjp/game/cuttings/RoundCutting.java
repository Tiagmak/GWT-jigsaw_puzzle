package mpjp.game.cuttings;

import java.util.HashMap;
import java.util.Map;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;
import mpjp.shared.geom.QuadTo;

public class RoundCutting implements Cutting {
	double pieceWidth;
	double pieceheight;
	
	public RoundCutting() {
		super();
	}
	
	/**
	 * piece to top Left corner
	 * @param x
	 * @param y
	 * @return pieceShape
	 */
	PieceShape pieceTopLeftCorner(double x, double y) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape;
		pieceShape = new PieceShape(initialPoint)
				.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
				.addSegment(new QuadTo(new Point(-x, 0), new Point(-x,-y)))			//curva mais esquerda
				.addSegment(new QuadTo(new Point(0, -y ), new Point(x,-y)))			//curva mais acima 
				.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
		
		return pieceShape;
	}
	
	/**
	 * piece to bottom Left corner
	 * @param x
	 * @param y
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape pieceBottomLeftCorner(double x, double y, int nrRow) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;
		
		if (nrRow % 2 == 0) {
			pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y ), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x , 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita

		} 
		else {
			pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y ), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
			
		}
		
		
		
		return pieceShape;
	}
	
	/**
	 * piece to top right corner
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @return pieceShape
	 */
	PieceShape pieceTopRightCorner(double x, double y, int nrColumn) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;
		
		if(nrColumn % 2 == 0) {
			pieceShape = new PieceShape(initialPoint)
				.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
				.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
				.addSegment(new QuadTo(new Point(0, -y ), new Point(x,-y)))	//curva mais acima 
				.addSegment(new QuadTo(new Point(x ,0), new Point(x,y)));		//curva mais direita
		}
		else {
			pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y ), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x ,0), new Point(x,y)));		//curva mais direita
		}

		
		return pieceShape;
	}
	
	/**
	 * piece to bottom right corner
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape pieceBottomRightCorner(double x, double y, int nrRow, int nrColumn) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;
		
		if (nrRow % 2 == 0) {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y ), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x ,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y ), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x ,0), new Point(x,y)));		//curva mais direita
			}
		} 
		else {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y ), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x ,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y ), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x ,0), new Point(x,y)));		//curva mais direita
			}
		}
		
		return pieceShape;
	}

	
	/**
	 * piece to first line
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape piecesFirstLine(double x, double y, int nrRow, int nrColumn) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;
		
		if(nrColumn % 2 == 0) {
			pieceShape = new PieceShape(initialPoint)
				.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
				.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
				.addSegment(new QuadTo(new Point(0, -y), new Point(x,-y)))		//curva mais acima 
				.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
		}
		else {
			pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y), new Point(x,-y)))		//curva mais acima 
					.addSegment(new QuadTo(new Point(x - x/2,0), new Point(x,y)));		//curva mais direita
		}

		return pieceShape;
	}

	/**
	 * piece to last line
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape piecesLastLine(double x, double y, int nrRow, int nrColumn) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;
		
		if (nrRow % 2 == 0) {
			if(nrColumn % 2 == 0) {
				System.out.println("1");
				pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y), new Point(-x,y)))			//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x - x/2,0), new Point(x,y)));		//curva mais direita
			}
		} 
		else {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x - x/2,0), new Point(x,y)));		//curva mais direita
			}
		}
		

		return pieceShape;
	}

	/**
	 * piece to first column
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape piecesFirstColumn(double x, double y, int nrRow, int nrColumn) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;

		
		if (nrRow % 2 == 0) {
			pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita	
		} 
		else {
			pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y - y/2), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita		
		}
		
		return pieceShape;
	}

	/**
	 * piece to last column
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape piecesLastColumn(double x, double y, int nrRow, int nrColumn) {
		Point initialPoint = new Point(x, y);
		PieceShape pieceShape = null;

		
		if (nrRow % 2 == 0) {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x,0), new Point(x,y)));		//curva mais direita
			}
		} 
		else {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y - y/2), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y - y/2), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x,0), new Point(x,y)));		//curva mais direita
			}
		}
		return pieceShape;
	}

	/**
	 * piece to all cases except especial cases
	 * @param x
	 * @param y
	 * @param nrColumn - number of columns
	 * @param nrRow - number of rows
	 * @return pieceShape
	 */
	PieceShape othersPieces(double x, double y, int nrRow, int nrColumn) {
		Point initialPoint = new Point(x , y);
		PieceShape pieceShape = null;

		if (nrRow % 2 == 0) {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
					.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
					.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
					.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
					.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y + y/2), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y - y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x - x/2,0), new Point(x,y)));		//curva mais direita
			}
		} 
		else {
			if(nrColumn % 2 == 0) {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y - y/2), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x -x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x + x/2,0), new Point(x,y)));		//curva mais direita
			}
			else {
				pieceShape = new PieceShape(initialPoint)
						.addSegment(new QuadTo(new Point(0, y - y/2), new Point(-x,y)))		//curva mais abaixo
						.addSegment(new QuadTo(new Point(-x + x/2, 0), new Point(-x,-y)))	//curva mais esquerda
						.addSegment(new QuadTo(new Point(0, -y + y / 2), new Point(x,-y)))	//curva mais acima 
						.addSegment(new QuadTo(new Point(x - x/2,0), new Point(x,y)));		//curva mais direita
			}
		}
		return pieceShape;
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
				PieceShape pieceShape ;
				
				if(i == 0 && j == 0) {
					pieceShape = pieceTopLeftCorner(x,y);
				}
				else if(i == rows - 1 && j == 0) {
					pieceShape = pieceBottomLeftCorner(x,y,i);
				}
				
				else if(i == 0 && j == columns -1) {
					pieceShape = pieceTopRightCorner(x,y,j);
					
				}
				else if(i == rows - 1 &&  j == columns -1) {
					pieceShape = pieceBottomRightCorner(x,y,i,j);
				}
				else if(i == 0) { 
					pieceShape = piecesFirstLine(x,y, i, j); 
				}
				else if(i == rows - 1) {
					pieceShape = piecesLastLine(x,y,i,j);
				}
				else if(j == 0){
					pieceShape = piecesFirstColumn(x,y,i,j);
				}
				else if(j == columns - 1){
					pieceShape = piecesLastColumn(x,y,i,j);
				}
				else {
					pieceShape = othersPieces(x, y, i, j);
				}
				puzzle.put(i * columns + j, pieceShape);
			}
		}
		return puzzle;
	}

}
