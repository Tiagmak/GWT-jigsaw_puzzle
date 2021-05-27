package mpjp.server;

import mpjp.client.PuzzleService;
import mpjp.game.Manager;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class PuzzleServiceImpl extends RemoteServiceServlet implements PuzzleService{
	private static final long serialVersionUID = 1L;

	@Override
	public String createWorkspace(PuzzleInfo info) throws MPJPException {
		return Manager.getInstance().createWorkspace(info);
	}

	@Override
	public Set<String> getAvailableCuttings() {
		return Manager.getInstance().getAvailableCuttings();
	}

	@Override
	public Set<String> getAvailableImages() {
		return Manager.getInstance().getAvailableCuttings();
	}

	@Override
	public Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		return Manager.getInstance().getAvailableWorkspaces();
	}

	@Override
	public PuzzleLayout getCurrentLayout(String workspaceId) throws MPJPException {
		return Manager.getInstance().getCurrentLayout(workspaceId);
	}

	@Override
	public PuzzleView getPuzzleView(String workspaceId) throws MPJPException {
		return Manager.getInstance().getPuzzleView(workspaceId);
	}

	@Override
	public PuzzleLayout connect(String workspaceId, int pieceId, Point point) throws MPJPException {
		return Manager.getInstance().connect(workspaceId, pieceId, point);
	}

	@Override
	public Integer selectPiece(String workspaceId, Point point) throws MPJPException {
		return Manager.getInstance().selectPiece(workspaceId, point);
	}

}
