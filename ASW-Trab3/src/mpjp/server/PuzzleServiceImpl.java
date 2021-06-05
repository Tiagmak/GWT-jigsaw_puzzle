package mpjp.server;

import mpjp.client.PuzzleService;
import mpjp.game.Images;
import mpjp.game.Manager;
import mpjp.game.WorkspacePool;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class PuzzleServiceImpl extends RemoteServiceServlet implements PuzzleService{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext context = getServletContext();
		File base = new File(context.getRealPath("/"));

		File imagesdir = new File(base, "WEB-INF/classes/mpjp/resources");
		File poolDir = new File(base, "WEB-INF/pool");

		if (!poolDir.exists())
			poolDir.mkdir();

		Images.setImageDirectory(imagesdir);
		WorkspacePool.setPoolDiretory(poolDir);
	}
	
	@Override
	public String createWorkspace(PuzzleInfo info) throws MPJPException {
		return Manager.getInstance().createWorkspace(info);
	}

	@Override
	public HashSet<String> getAvailableCuttings() {
		HashSet<String> strings = new HashSet<>();
		for (String s :  Manager.getInstance().getAvailableCuttings()) {
			strings.add(s);
		}
		return strings;
	}

	@Override
	public HashSet<String> getAvailableImages() {
		HashSet<String> strings = new HashSet<>();
		for (String s : Manager.getInstance().getAvailableImages()) {
			strings.add(s);
		}
		return strings;
	}

	@Override
	public HashMap<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		HashMap<String, PuzzleSelectInfo> strings = new HashMap<>();
		for (Entry<String, PuzzleSelectInfo> s : Manager.getInstance().getAvailableWorkspaces().entrySet()) {
			strings.put(s.getKey(), s.getValue());
		}
		return strings;
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
