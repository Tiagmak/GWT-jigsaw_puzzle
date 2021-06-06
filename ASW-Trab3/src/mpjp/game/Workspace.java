package mpjp.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Queue;
import java.util.Set;

import mpjp.game.cuttings.Cutting;
import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.quad.PointQuadtree;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class Workspace implements Serializable {

	private static final long serialVersionUID = -8893336838860720058L;
	
	private static double widthFactor = 2;
	private static double heightFactor = 2;
	private static double radius;
	
	private transient PointQuadtree<PieceStatus> puzzlePieces;
	private transient PuzzleStructure structure;
	
	private Map<Integer,PieceStatus> pieces;
	private Map<Integer,PieceShape> shapes;
	
	PuzzleInfo info;
	Date start;
	
	Workspace(PuzzleInfo info) throws MPJPException {
		
		this.info = info;
		this.start = new Date();
		this.pieces = new HashMap<>();
		this.structure = new PuzzleStructure(info);
		
		radius = Math.min(this.structure.getPieceWidth() / 2, this.structure.getPieceHeight() / 2);
		
		createQuadTree();
		
		scatter();
		
		Cutting cutting = new CuttingFactoryImplementation().createCutting(info.getCuttingName());
		this.shapes = cutting.getShapes(structure);
	}
	
	private void createQuadTree() {
		double margin = Math.max(structure.getPieceWidth() / 2, structure.getPieceHeight() / 2);
		radius = margin * 0.95;
		
		this.puzzlePieces = new PointQuadtree<PieceStatus>(info.getWidth() * Workspace.widthFactor, info.getHeight() * Workspace.heightFactor, margin);	
	}

	
	private Map<Integer,Point> getLocations(){
		Map<Integer, Point> locations = new HashMap<Integer, Point>();
		
		for(Integer p : pieces.keySet()) {
			try {
				locations.put(p, this.structure.getPieceStandardCenter(p));
			} catch(Exception e) {}
		}
		return locations;
	}
	
	private Map<Integer, List<Integer>> getBlocks() {
		Map<Integer, List<Integer> > blocks = new HashMap<Integer, List<Integer>>();
		for(Integer id : pieces.keySet()) {
			int block = pieces.get(id).getBlock();
			if(! blocks.containsKey(block)) {
				blocks.put(block, new ArrayList<Integer>());
			}
			blocks.get(block).add(id);
		}
		return blocks;
	}
	
	public static double getWidthFactor() {
		return widthFactor;
	}
	
	public static void setWidthFactor(double widthFactor) {
		Workspace.widthFactor = widthFactor;
	}
	
	public static double getHeightFactor() {
		return heightFactor;
	}
	
	public static void setHeightFactor(double heightFactor) {
		Workspace.heightFactor = heightFactor;
	}
	
	public static double getRadius() {
		return radius;
	}
	
	public static void setRadius(double radius) {
		Workspace.radius = radius;
	}
	
	String getId() {
		return info.getImageName() + start.getTime();
	}
	
	double getSelectRadius() {
		double ratioH = info.getHeight()/info.getColumns();
		double ratioW = info.getWidth()/info.getRows();
		
		return Math.max(ratioH, ratioW)/2;
	}
	
	PuzzleSelectInfo getPuzzleSelectInfo() {
		return new PuzzleSelectInfo(info,start,getPercentageSolved());
	}
	
	int getPercentageSolved() {
		return 100 * (pieces.size() - getBlocks().size()) / (pieces.size() - 1);
	}
	
	PuzzleView getPuzzleView() {
		return new PuzzleView(
				start,
				info.getWidth() * getWidthFactor(),
				info.getHeight() * getHeightFactor(),
				info.getWidth(),
				info.getHeight(),
				structure.getPieceWidth(),
				structure.getPieceHeight(),
				info.getImageName(),
				shapes,
				getLocations());
	}
	
	PuzzleLayout getCurrentLayout() {
		return new PuzzleLayout(pieces, getBlocks(), getPercentageSolved());
	}
	
	void scatter() {		
		Map<Integer,Point> locations = getLocations();
		
		for(int id = 0; id < structure.getPieceCount(); id++) {
			Point point;
			do {
				point = structure.getRandomPointInStandardPuzzle();
			} while (locations.containsValue(point));
			
			PieceStatus piece = new PieceStatus(id, point);
			piece.setBlock(id);
			
			puzzlePieces.insert(piece);
			
			pieces.put(id, piece);
			locations.put(id, point);
		}
	}
	
	void restore() {
		this.structure = new PuzzleStructure(info);
		
		createQuadTree();
		
		for(PieceStatus i : pieces.values())
			puzzlePieces.insert(i);
	}
	
	Integer selectPiece(Point point) {
		Set<PieceStatus> set = puzzlePieces.findNear(point.getX(), point.getY(), getSelectRadius());
		
		PieceStatus f = null;
		for(PieceStatus p : set) {
			if(f == null || p.getBlock() > f.getBlock()) {
				f = p;
			}
		}
		
		return (f == null) ? null : f.getBlock();
	}
	
	private boolean validId(int movedId, int numberOfPieces) {
		return movedId >= 0 && movedId < numberOfPieces;
	}
	
	private boolean validPoint(Point point) {
		return point.getX() >= 0 && 
			   point.getX() < info.getWidth() * getWidthFactor() &&
			   point.getY() >= 0 &&
			   point.getY() < info.getHeight() * getHeightFactor();
	}
	
	private Queue<PieceStatus> translateBlock(Point translateVector, int block) {
		Queue<PieceStatus> queue = new LinkedList<PieceStatus>();
		
		for(Integer i : pieces.keySet()) {
			PieceStatus piece = pieces.get(i);
			if(piece.getBlock() == block) {
				puzzlePieces.delete(piece);
				
				Point point = piece.getPosition();
				point.setX(point.getX() + translateVector.getX());
				point.setY(point.getY() + translateVector.getY());
				
				piece.setPosition(point);
				
				puzzlePieces.insert(piece);
				queue.add(piece);	
			}
		}
		return queue;
	}
	
	PuzzleLayout connect(int movedId, Point point) throws MPJPException {
		if(!(validId(movedId, pieces.size()) && validPoint(point)))
			throw new MPJPException();
		
		int block = pieces.get(movedId).getBlock();
		Point translateVector = new Point(
				point.getX() - pieces.get(movedId).getX(),
				point.getY() - pieces.get(movedId).getY());
		
		Queue<PieceStatus> queue = translateBlock(translateVector, block);
		
		while(!queue.isEmpty()) {
			PieceStatus piece = queue.poll();
			
			for(Direction d : Direction.values()) {
				Integer nearId = structure.getPieceFacing(d, piece.getId());
				
				if(nearId != null) {
					Point nearPiece = structure.getPieceCenterFacing(d, piece.getPosition());
					
					Set<PieceStatus> near = puzzlePieces.findNear(nearPiece.getX(), nearPiece.getY(), radius);
					
					if(!near.isEmpty()) {
						for(PieceStatus candidate: near) {
							if(candidate.getId() == nearId && candidate.getBlock() != block) {
								candidate.setPosition(nearPiece);
								candidate.setBlock(block);
								
								queue.add(candidate);
							}
						}
					}
				}
			}
		}
		
		return getCurrentLayout();
	}
	
	PuzzleStructure getPuzzleStructure() {
		return structure;
	}
}