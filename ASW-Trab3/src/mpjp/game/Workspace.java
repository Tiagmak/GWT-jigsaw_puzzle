package mpjp.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mpjp.game.cuttings.Cutting;
import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class Workspace implements Serializable {
	private static final long serialVersionUID = 1L;
	private PuzzleInfo info;
	private Date start;

	static double radius, heightFactor, widthFactor;
	Map<Integer, PieceStatus> pieces;
	Map<Integer, List<Integer>> blocks;
	Map<Integer, Point> locations;
		
	public Workspace(PuzzleInfo info) {
		super();
		widthFactor = 2;
		heightFactor = 2;
		this.info = info;
		start = new Date();
		scatter();
	}
	
	/**
	 * Current proportion between board height and puzzle height. For instance, a factor 2 means that the boards are created with twice the puzzle's height.
	 * @return     heightFactor ratio between workspace and puzzle heights
	 */
	public static double getHeightFactor() {
		return heightFactor;
	}
	
	/**
	 * Change proportion between board width and puzzle width. For instance, a factor 2 means that the boards are created with twice the puzzle's height.
	 * @param heightFactor - ratio between workspace and puzzle heights
	 */
	public static void setHeightFactor(double heightFactor) {
		Workspace.heightFactor = heightFactor;
	}
	
	/**
	 * Current proportion between board width and puzzle width. For instance, a factor 2 means that the boards are created with twice the puzzle's width.
	 * @return widthFactor ratio between workspace and puzzle widths
	 */
	public static double getWidthFactor() {
		return widthFactor;
	}
	
	/**
	 * Change proportion between board width and puzzle width. For instance, a factor 2 means that the boards are created with twice the puzzle's width.
	 * @param widthFactor - ratio between workspace and puzzle widths
	 */
	public static void setWidthFactor(double widthFactor) {
		Workspace.widthFactor = widthFactor;
	}
	
	/**
	 * Current radius for matching
	 * @return radius
	 */
	public static double getRadius() {
		return radius;
	}
	
	/**
	 * Set radius for matching pieces
	 * @param radius -  for matching pieces
	 */
	public static void setRadius(double radius) {
		Workspace.radius = radius;
	}
	
	/**
	 * The current piece positions and blocks formed by connected pieces
	 * @return PuzzleLayout
	 */
	public PuzzleLayout getCurrentLayout() {
		PuzzleLayout puzzleLayout = new PuzzleLayout(pieces, blocks, getPercentageSolved());
		return puzzleLayout;
	}
	
	/**
	 * Information on this puzzle that may be used by a person to decide to help solving it. It is contains the data used to create the puzzle, 
	 * the moment when it started being solved, and the percentage that was already solved.
	 * @return  puzzleSelectInfo
	 */
	PuzzleSelectInfo getPuzzleSelectInfo() {
		PuzzleSelectInfo puzzleSelectInfo = new PuzzleSelectInfo(info, start, getPercentageSolved());
		return puzzleSelectInfo;
	}
	
	/**
	 * Puzzle structure used in this workspace's puzzle. This data is needed for unit testing.
	 * @return     puzzle structure
	 */
	public PuzzleStructure getPuzzleStructure() {
		PuzzleStructure puzzleStructure = new PuzzleStructure(info);
		return puzzleStructure;
	}
	
	/**
	 * The visual part of the puzzle, sent to the client when the user starts solving the puzzle.
	 * @return PuzzleView
	 */
	PuzzleView getPuzzleView() {
		PuzzleStructure puzzleStructure = getPuzzleStructure();

		double puzzleWidth = info.getWidth();
		double puzzleHeight = info.getHeight();

		double pieceWidth = puzzleStructure.getPieceWidth();
		double pieceHeight = puzzleStructure.getPieceHeight();

		double workspaceWidth = puzzleWidth * widthFactor;
		double workspaceHeight = puzzleHeight * heightFactor;

		String image = info.getImageName();

		CuttingFactoryImplementation cfi = new CuttingFactoryImplementation();
		Cutting cut;
		Map<Integer, PieceShape> shapes = null;
		try {
			cut = cfi.createCutting(info.getCuttingName());
			shapes = cut.getShapes(puzzleStructure);
		} catch (MPJPException e) {
			e.printStackTrace();
		}

		PuzzleView puzzleView = new PuzzleView(start, workspaceWidth, workspaceHeight, puzzleWidth, puzzleHeight,
				pieceWidth, pieceHeight, image, shapes, locations);
		return puzzleView;
	}
	
	/**
	 * Creates a string ID for this solution by combining some of its features (e.g. image) and the start date. 
	 * This ID must be different for 2 workspaces created from the same data in different moments.
	 * @return id for this workspace
	 */
	public String getId() {
		return info.getImageName() + start.getTime();
	}
	
	/**
	 * Percentage in which puzzle is solved. 
	 * @return percentageComplete
	 */
	public int getPercentageSolved() {
		int nrPieces = pieces.size();
		int nrBlocks = blocks.size();
		return 100 * (nrPieces - nrBlocks) / (nrPieces - 1);
	}
	
	/**
	 * The radius from the "center" of the piece where it can be selected. This radius should be maximum of piece width and height.
	 * @return  radius for piece selection
	 */
	public double getSelectRadius() {
		PuzzleStructure puzzleStructure = new PuzzleStructure(info);
		double pieceHeight = puzzleStructure.getPieceHeight();
		double pieceWidth = puzzleStructure.getPieceWidth();
		return Math.sqrt(Math.pow(pieceHeight, 2) + Math.pow(pieceWidth, 2)) / 2;
	}
	
	/**
	 * Check if exist common ids 
	 * @param listBlock - block of Id's
	 * @param listBlockCurrent - block of Id's
	 * @return commonIds - List of common ID's
	 */
	private List<Integer> verifyCommonIds(List<Integer> listBlock, List<Integer> listBlockCurrent) {
		List<Integer> commonIds = new ArrayList<>();

		List<Integer> smallestList = listBlock.size() < listBlockCurrent.size() ? listBlock : listBlockCurrent;
		List<Integer> highestList = listBlock.size() < listBlockCurrent.size() ? listBlockCurrent : listBlock;

		for (Integer i : smallestList) {
			if (highestList.contains(i)) {
				commonIds.add(i);
			}
		}
		return commonIds;
	}
	
	/**
	 * Merge 2 blocks of integers
	 * @param listBlockCurrent - block of Id's
	 * @param listBlock - block of Id's
	 * @return listBlock - block with ids of 2 inicial blocks
	 */
	private List<Integer> transferIds(List<Integer> listBlockCurrent, List<Integer> listBlock) {
		for (Integer i : listBlockCurrent) {
			listBlock.add(i);
		}
		return listBlock;
	}
	
	/**
	 * Change block Id of pieces
	 * @param id
	 */
	private void changeBlockIdOfPieces(Integer id) {
		for (Integer i : pieces.keySet()) {
			PieceStatus pieceStatus = pieces.get(i);
			pieceStatus.setBlock(id);
			pieces.put(id, pieceStatus);
		}
	}
	
	/**
	 * Move the piece with given id to given point and check if it connects with other pieces. 
	 * Pieces in the same block are moved accordingly. If two (or more pieces are connected) then they will merged into a single block. 
	 * The new merged block will have as ID of the lowest ID. 
	 *  If one of more pieces fall off the workspace the a exception is raised. However, all affected pieces are rolled back to their previous position.
	 * @param moveId  - of a piece
	 * @param point  - to where the piece's "center" is moved
	 * @return PuzzleLayout
	 * @throws MPJPException - if ID is invalid or moved pieces fall out of the quad tree
	 */
	PuzzleLayout connect(int moveId, Point point) throws MPJPException {
		if (moveId < 0 || moveId >= info.getRows() * info.getColumns())
			throw new MPJPException("Id is invalid");

		if (point.getX() < 0 || point.getX() > info.getWidth() * widthFactor || point.getY() < 0
				|| point.getY() > info.getHeight() * heightFactor)
			throw new MPJPException("Coordinates are invalid");

		PieceStatus pieceStatus = new PieceStatus(moveId, point);
		pieces.put(moveId, pieceStatus);

		locations.put(moveId, point);

		List<Integer> listBlock = new ArrayList<>();
		listBlock.add(moveId);
		for (Integer i : locations.keySet()) {
			Point pointLocations = locations.get(i);
			if (i == moveId)
				continue;
			if (Math.pow(pointLocations.getX() - point.getX(), 2)
					+ Math.pow(pointLocations.getY() - point.getY(), 2) <= Math.pow(radius, 2)) {
				listBlock.add(i);
			}
		}

		int idMin = blocks.size() + 1;
		List<Integer> idsToRemove = new ArrayList<>();
		List<Integer> listBlockCurrent = new ArrayList<>();
		for (Integer i : blocks.keySet()) {
			listBlockCurrent = blocks.get(i);
			List<Integer> commonIds = verifyCommonIds(listBlock, listBlockCurrent);
			if (commonIds != null) {
				listBlock = transferIds(listBlockCurrent, listBlock);
				idsToRemove.add(i);
			}
			if (i < idMin)
				idMin = i;
		}

		if (!idsToRemove.isEmpty()) {
			blocks.keySet().removeAll(idsToRemove);
		}

		blocks.put(idMin, listBlock);

		changeBlockIdOfPieces(idMin);

		return getCurrentLayout();
	}
	
	/**
	 * Restore transient fields that are not saved by serialization. In particular, the PointQuadtree is not serializable and can be easily restored from pieces.
	 */
	private void restore() {
		PuzzleStructure puzzleStructure = new PuzzleStructure(info);
		int nrPieces = puzzleStructure.getPieceCount();

		for (Integer id = 0; id < nrPieces; id++) {
			try {
				Point correctPosition = puzzleStructure.getPieceStandardCenter(id);
				PieceStatus pieceStatus = new PieceStatus(id, correctPosition);
				List<Integer> list = new ArrayList<>();
				pieceStatus.setBlock(id);
				list.add(id);
				pieces.put(id, pieceStatus);
				blocks.put(id, list);
				locations.put(id, correctPosition);
			} catch (MPJPException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Scatter the puzzle's pieces on the board before solving it. This method is invoked when this workspace is instantiated.
	 */
	private void scatter() {
		pieces = new HashMap<>();
		blocks = new HashMap<>();
		locations = new HashMap<>();
		PuzzleStructure puzzleStructure = new PuzzleStructure(info);
		int nrPieces = puzzleStructure.getPieceCount();

		for (int id = 0; id < nrPieces; id++) {
			Point randomPosition = puzzleStructure.getRandomPointInStandardPuzzle();

			while (locations.containsValue(randomPosition)) {
				randomPosition = puzzleStructure.getRandomPointInStandardPuzzle();
			}

			PieceStatus pieceStatus = new PieceStatus(id, randomPosition);
			List<Integer> list = new ArrayList<>();
			pieceStatus.setBlock(id);
			list.add(id);
			pieces.put(id, pieceStatus);
			blocks.put(id, list);
			locations.put(id, randomPosition);
		}
	}

	/**
	 * Select a piece given a pair of coordinates. If several pieces are selected then is returned the one with the highest block number. 
	 * Note that lower ID blocks tend to be be larger (have more pieces) and higher ID blocks then to be disconnected pieces, the ones the 
	 * player probably wants to move and connect.
	 * @param point  - on the piece
	 * @return id of the piece near the given endPoint with highest block number, or null if none is selected
	 */
	public Integer selectPiece(Point point) {
		List<Integer> piecesSelected = new ArrayList<>();

        for (Integer id : pieces.keySet()) {
            PieceStatus pieceStatus = pieces.get(id);
            Point pointCenterPosition = pieceStatus.getPosition();

            if (Math.pow(pointCenterPosition.getX() - point.getX(), 2)
                    + Math.pow(pointCenterPosition.getY() - point.getY(), 2) <= Math.pow(getSelectRadius(), 2)) {
                piecesSelected.add(id);
            }
        }

        int highestBlockId = 0;
        Integer selectedId = null;

        if (piecesSelected.size() == 1)
            return piecesSelected.get(0);

        for (Integer id : piecesSelected) {
            PieceStatus pieceStatus = pieces.get(id);
            if (pieceStatus.getBlock() > highestBlockId) {
                highestBlockId = pieceStatus.getBlock();
                selectedId = id;
            }
        }
        return selectedId;

	}

}
