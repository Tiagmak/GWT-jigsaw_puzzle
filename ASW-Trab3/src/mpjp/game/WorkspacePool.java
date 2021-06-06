package mpjp.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleSelectInfo;

public class WorkspacePool {
	static final String SERIALIAZTION_SUFFIX = ".ser";

	private static File poolDirectory = new File(".");

	private static Map<String, Workspace> availableWorkspaces;
	private static Map<String, File> files;

	public WorkspacePool() {
		availableWorkspaces = new HashMap<>();
		files = new HashMap<>();
	}

	public static File getPoolDirectory() {
		return poolDirectory;
	}

	public static void setPoolDiretory(File poolDirectory) {
		WorkspacePool.poolDirectory = poolDirectory;
	}

	public static void setPoolDiretory(String pathname) {
		WorkspacePool.poolDirectory = new File(pathname);
	}

	String createWorkspace(PuzzleInfo info) throws MPJPException {
		Workspace workspace = new Workspace(info);
		String id = workspace.getId();

		availableWorkspaces.put(id, workspace);

		try {
			backup(id, workspace);
			return id;
		} catch (Exception e) {
			throw new MPJPException();
		}
	}

	Workspace getWorkspace(String id) throws MPJPException {
		return availableWorkspaces.get(id);
	}

	Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		Map<String, PuzzleSelectInfo> info = new HashMap<>();

		for (Entry<String, Workspace> workspace : availableWorkspaces.entrySet()) {
			info.put(workspace.getKey(), workspace.getValue().getPuzzleSelectInfo());
		}

		return info;
	}

	File getFile(String workspaceId) {
		if (!files.containsKey(workspaceId)) {
			files.put(workspaceId, new File(poolDirectory, workspaceId + SERIALIAZTION_SUFFIX));
		}
		return files.get(workspaceId);
	}

	void backup(String workspaceId, Workspace workspace) throws MPJPException {
		try (FileOutputStream stream = new FileOutputStream(getFile(workspaceId));
			 ObjectOutputStream serializer = new ObjectOutputStream(stream);) {
				serializer.writeObject(workspace);
		} catch (IOException cause) {
			throw new MPJPException();
		}
	}

	Workspace recover(String workspaceId) throws MPJPException {
		Workspace workspace;
		try (FileInputStream stream = new FileInputStream(getFile(workspaceId));
			 ObjectInputStream deserializer = new ObjectInputStream(stream);) {
				workspace = (Workspace) deserializer.readObject();
				workspace.restore(); // recuperar quadtree
				availableWorkspaces.put(workspaceId, workspace);
		} catch (IOException | ClassNotFoundException cause) {
			throw new MPJPException();
		}
		return workspace;
	}

	void reset() {
		poolDirectory = new File(".");
		availableWorkspaces = new HashMap<String, Workspace>();
	}
}