package mpjp.game;


import java.util.Map;
import java.util.Set;

import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;


public class Manager {
	private static WorkspacePool workspacePool;
	
	private static Manager manager = null;
	
	/**
	 * The single instance of this class
	 * @return singleton
	 */
	public static Manager getInstance() {
		workspacePool = new WorkspacePool();
		if(manager == null) {
			manager = new Manager();
		}
		return manager;
	}
	
	/**
	 * Reset this singleton for testing purposes. Do not use in production!
	 */
	void reset(){
		workspacePool.reset();
	}
	
	/**
	 * Creates a workspace with given information and returns an ID to refer to it in methods such as getPuzzleView(String), 
	 * {getCurrentLayout(String), selectPiece(String, Point), and connect(String, int, Point).
	 * @param info  - on workspace to create
	 * @return - workspace ID
	 * @throws MPJPException  if something unexpected happens
	 */
	public String createWorkspace(PuzzleInfo info) throws MPJPException {
		String workspaceId = workspacePool.createWorkspace(info);
		return workspaceId;
	}
	
	/**
	 * A set of cutting names available to produce puzzle pieces
	 * @return set of images
	 */
	public Set<String> getAvailableCuttings() {
		CuttingFactoryImplementation cfi = new CuttingFactoryImplementation();
		return  cfi.getAvaliableCuttings();
	}
	
	/**
	 * A set of images available for puzzle backgrounds
	 * @return
	 */
	public Set<String> getAvailableImages() {
		return Images.getAvailableImages();
	}
	
	/**
	 * A map of workspace IDs (int) to PuzzleSelectInfo. The IDs must be used to identify the intended workspace to use them 
	 * with methods such as getPuzzleView(String), getCurrentLayout(String), selectPiece(String, Point), and connect(String, int, Point)
	 * @return map
	 */
	public Map<String, PuzzleSelectInfo> getAvailableWorkspaces(){
		return workspacePool.getAvailableWorkspaces();
	}
	
	/**
	 * Current layout of the workspace with given ID
	 * @param workspaceId - where puzzle is being solved
	 * @return puzzleLayout
	 * @throws MPJPException  - if the workspace ID is invalid
	 */
	public PuzzleLayout getCurrentLayout(String workspaceId) throws MPJPException {
		Workspace workspace = workspacePool.getWorkspace(workspaceId);
		return workspace.getCurrentLayout();
	}
	
	/**
	 * Puzzle view of given workspace
	 * @param workspaceId - where puzzle is being solved
	 * @return     a puzzle view
	 * @throws MPJPException - if the workspace ID is invalid
	 */
	public PuzzleView getPuzzleView(String workspaceId) throws MPJPException {
		Workspace workspace = workspacePool.getWorkspace(workspaceId);
		return workspace.getPuzzleView();
	}
	
	/**
	 * In the workspace with the given ID, connect piece with given iD after moving its center to the given point. All pieces in the same block 
	 * are moved accordingly. Changed in workspace are persisted:
	 * @param workspaceId - where puzzle is being solved
	 * @param pieceId -  of piece to connect
	 * @param point - where the center of the piece is moved
	 * @return     updated layout
	 * @throws MPJPException - if the workspace ID is invalid or if any of the pieces is moved outside the workspace
	 */
	public PuzzleLayout connect(String workspaceId, int pieceId, Point point)  throws MPJPException {
		Workspace workspace = workspacePool.getWorkspace(workspaceId);
		return workspace.connect(pieceId, point);
	}
	
	/**
	 * Select a piece in the given workspace, with its "center" near the given point
	 * @param workspaceId  - where the selection is made
	 * @param point  - of selection
	 * @return     pieceID with null if no piece was selected, or the ID of the selected piece
	 * @throws MPJPException - if the workspace ID is invalid
	 */
	public Integer selectPiece(String workspaceId, Point point)  throws MPJPException {
		Workspace workspace = workspacePool.getWorkspace(workspaceId);
		return workspace.selectPiece(point);
	}
}
