package mpjp.game;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleSelectInfo;

public class WorkspacePool {
	static final String SERIALIAZTION_SUFFIX = ".ser";

	static File poolDirectory; 
	Map<String, PuzzleSelectInfo> allWorkspaces;
	Map<String, File> workspacesFiles;

	WorkspacePool() {
		super();
		allWorkspaces = new HashMap<>();
		workspacesFiles = new HashMap<>();		
	}
	
	/**
	 * Create a workspace that is stored using its id as key. The new workspace is also serialized.
	 * @param info - to create workspace
	 * @return worspaceId
	 * @throws MPJPException - if an error occurs during serialization
	 */
	String createWorkspace(PuzzleInfo info) throws MPJPException {
		Workspace newWorkspace = new Workspace(info);
		String workspaceId = newWorkspace.getId();
		
		PuzzleSelectInfo puzzleSelectInfo = new PuzzleSelectInfo(info,new Date(),0);
		allWorkspaces.put(workspaceId, puzzleSelectInfo);
		backup(workspaceId, newWorkspace);	
		return workspaceId;
	}
	
	/**
	 * Get the workspace with given id. If it is not available in memory then it will be recovered from a serialized file
	 * @param id  - of workspace
	 * @return workspace
	 * @throws MPJPException- if an error occurred during deserialization
	 */
	Workspace getWorkspace(String id) throws MPJPException {
		return recover(id);
	}

	/**
	 * Current pool directory, the directory where workspace serializations are saved.
	 * @return pool directory
	 */
	public static File getPoolDirectory() {
		return poolDirectory;
	}
	
	/**
	 * Change pool directory, the directory where workspace serializations are saved.
	 * @param poolDirectory - as file
	 */
	public static void setPoolDiretory(File poolDirectory) {
		WorkspacePool.poolDirectory = poolDirectory;
	}
	
	/**
	 * Convenience method for setting pool directory as a string
	 * @param pathname  - to set pool directory
	 */
	public static void setPoolDiretory(String pathname) {
		WorkspacePool.poolDirectory = new File(pathname);
	}
	
	/**
	 * A File object for given ID. The file name is the ID with suffix .ser and its parent directory is given by property poolDirectory. 
	 * File objects are stored to avoid being constantly recreated.
	 * @param workspaceId  - id of workspace
	 * @return file
	 */
	File getFile(String workspaceId) {
		String fileName = workspaceId + SERIALIAZTION_SUFFIX;
		if(workspacesFiles.get(workspaceId)!=null) {
			return workspacesFiles.get(workspaceId);
		}
		workspacesFiles.put(workspaceId, new File(poolDirectory, fileName));
		
		return workspacesFiles.get(workspaceId);
	}

	/**
	 * A map of workspace IDs to PuzzleSelectInfo used for selecting an existing puzzle to solve.
	 * @return map of workspaceIds to PuzzleSelectInfo
	 */
	Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		return allWorkspaces;
	}

	/**
	 * Serializes workspace
	 * @param workspaceId  - id of workspace
	 * @param workspace  - itself
	 * @throws MPJPException - if an error occurs during serialization
	 */
	void backup(String workspaceId, Workspace workspace) throws MPJPException {
		try {
			File initialFile = new File(poolDirectory, workspaceId + SERIALIAZTION_SUFFIX);
			FileOutputStream newFile = new FileOutputStream(initialFile);
			ObjectOutputStream newObject = new ObjectOutputStream(newFile);
			workspacesFiles.replace(workspaceId,initialFile );
			newObject.writeObject(workspace);
			newObject.close();
			newFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recover a workspace form a serialization given its ID
	 * @param workspaceId  - of intended workspace
	 * @return workspace
	 * @throws MPJPException - if an error occurs during serialization
	 */
	Workspace recover(String workspaceId) throws MPJPException {
		Workspace workspaceRecover = null;

		if (getFile(workspaceId).canRead()) {
			try (FileInputStream fileIn = new FileInputStream(getFile(workspaceId));
					ObjectInputStream in = new ObjectInputStream(fileIn);) {
				workspaceRecover = (Workspace) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			} 
		}
		return workspaceRecover;
	}

	/**
	 * Revert all fields to their original values. Reset the pool for testing purposes. Do not use this method in production!
	 */
	void reset() {
		allWorkspaces.clear();
		workspacesFiles.clear();
	}

}
