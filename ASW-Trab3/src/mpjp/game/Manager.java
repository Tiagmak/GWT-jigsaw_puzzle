package mpjp.game;

import java.util.Set;

import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

import java.util.Map;

public class Manager {
	private static WorkspacePool pool;
	private static Manager instance;
	
	private Manager() {
		Manager.pool = new WorkspacePool();
	}
	
	public static Manager getInstance() {
		if(Manager.instance == null) {
			Manager.instance = new Manager();
		}
		return Manager.instance;
	}
	
	void reset() {
		Manager.pool = new WorkspacePool();
	}
	
	public Set<String> getAvailableCuttings() {
		return new CuttingFactoryImplementation().getAvaliableCuttings();
	}
	
	public Set<String> getAvailableImages() {
		return Images.getAvailableImages();
	}
	
	public Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		return Manager.pool.getAvailableWorkspaces();
	}
	
	public String createWorkspace(PuzzleInfo info) throws MPJPException {
		return Manager.pool.createWorkspace(info);
	}
	
	public Integer selectPiece(String workspaceId, Point point) throws MPJPException {
		return Manager.pool.getWorkspace(workspaceId).selectPiece(point);
	}
	
	public PuzzleLayout connect(String workspaceId, int pieceId, Point point) throws MPJPException {
		return Manager.pool.getWorkspace(workspaceId).connect(pieceId, point);
	}
	
	public PuzzleView getPuzzleView(String workspaceId) throws MPJPException {
		return Manager.pool.getWorkspace(workspaceId).getPuzzleView();
	}
	
	public PuzzleLayout getCurrentLayout(String workspaceId) throws MPJPException {
		return Manager.pool.getWorkspace(workspaceId).getCurrentLayout();
	}
}