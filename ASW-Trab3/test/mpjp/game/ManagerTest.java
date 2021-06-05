package mpjp.game;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import mpjp.game.PuzzleData.Puzzle;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

/**
* Template for a test class on Manager - YOU NEED TO IMPLEMENTS THESE TESTS!
* 
*/
class ManagerTest extends PuzzleData {
	static Manager manager;
	List<String> ids = new ArrayList<>();
	static final String SERIALIAZTION_SUFFIX = ".ser";

	
	/**
	 * Get the singleton instance for tests 
	 */
	@BeforeAll
	static void firstSetUp() {
		manager = Manager.getInstance();
	}
	
	/**
	 * Reset the singleton to its previous state
	 */
	@BeforeEach
	void setUp() {
		manager.reset();
		ids.clear();
	}
	
	/**
	 * GetIntance should return always the same instance
	 */
	@RepeatedTest(value = 10)
	void testGetInstance() {
		assertEquals(manager.getInstance(), manager.getInstance(),"Instances are different");
	}

	/**
	 * Check if a set of cuttings with is, al least one, is available.
	 */
	@Test
	void testGetAvailableCuttings() {
		Set<String> set = manager.getAvailableCuttings();
		assertNotNull(set,"expected a set of strings");
		assertTrue(set.size() >= 1);
	}

	/**
	 * Check if images in test resources are available
	 */
	@Test
	void testGetAvailableImages() {
		Set<String> images = manager.getAvailableImages();
		for(String image: TEST_IMAGES)
			assertTrue(images.contains(image),
					"expected image from test resources: "+image);
	}

	/**
	 * Check if available workspace reflect those that where
	 * created so far, and map IDs to the correct type.   
	 * 
	 * @throws MPJPException if something unexpected happens
	 */
	@Test
	void testGetAvailableWorkspaces() throws MPJPException {
		Map<String, PuzzleSelectInfo> map = null;
		Set<String> keys = null;
		
		for(Puzzle puzzle: Puzzle.values()) {
			String id = manager.createWorkspace(puzzle.getPuzzleInfo());
			ids.add(id);
			
			map = manager.getAvailableWorkspaces();
			
			assertNotNull(map,"instance expected");
			
			keys = map.keySet();
			
			assertEquals(ids.size(),keys.size(),"Invalid size");
			assertEquals(new HashSet<>(ids),keys,"Unexpected keys");
		
			PuzzleSelectInfo info = map.get(id);
			
			assertNotNull(info,"PuzzleSelectInfo expected");
		}
	}

	/**
	 * Check if workspaces created from an info return an ID
	 * and that IDs change even after a minimal delay  
	 *  
	 * @param puzzle with data for testing
	 * @throws InterruptedException on error during sleep 
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testCreateWorkspace(Puzzle puzzle) 
			throws InterruptedException, MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		
		String workspaceId, workspaceIdwithDelay;
		
		try {
			workspaceId = manager.createWorkspace(puzzle.getPuzzleInfo());
			assertNotNull(workspaceId,"workspace expected");
			Thread.sleep(1);
			workspaceIdwithDelay = manager.createWorkspace(puzzle.getPuzzleInfo());
			assertNotEquals(workspaceId,workspaceIdwithDelay, "expected workspace id to change after a minimal ddelay" );
			ids.add(workspaceIdwithDelay);
			ids.add(workspaceId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Check if piece selection using current layout.
	 * Using their locations it should retrieve either the
	 * same piece or one with a higher block id, if two or
	 * more pieces overlap
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if an unexpected exception occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testSelectPiece(Puzzle puzzle) throws MPJPException {
		String workspaceId = manager.createWorkspace(puzzle.getPuzzleInfo());
		PuzzleLayout puzzleLayout = manager.getCurrentLayout(workspaceId);
		PuzzleView puzzleView = manager.getPuzzleView(workspaceId);
		double pieceHeight = puzzleView.getPieceHeight();
		double pieceWidth = puzzleView.getPieceWidth();
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		double radius = Math.sqrt(Math.pow(pieceHeight, 2) + Math.pow(pieceWidth, 2)) / 2;
		double delta = Math.sqrt(radius);
		
		for(Integer id: pieces.keySet()) {
			PieceStatus piece = pieces.get(id);
			Point point = piece.getPosition();
			Point near = new Point(
					point.getX()+getDelta(delta),
					point.getY()+getDelta(delta));
			Integer block = piece.getBlock();
			
			Integer selected = manager.selectPiece(workspaceId, near);
			
			Integer selectedBlock = pieces.get(selected).getBlock();
			
			assertNotNull(selected,"Some piece selected");
			
			assertTrue((int)id == (int)selected || (int)selectedBlock > (int)block,
					"At least a higher block expected");
		}
	}

	/**
	 * Check if placing the first piece (0) at the center raises no exception,
	 * but trying place a non existing piece does raize an exception.
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testConnect(Puzzle puzzle) throws MPJPException {
		String workspaceId = manager.createWorkspace(puzzle.getPuzzleInfo());
		Point center = new Point(puzzle.width/2, puzzle.height/2);
		int nonExistingPiece=-1;
		assertDoesNotThrow(() -> manager.connect(workspaceId,0,center));
		assertThrows(
				MPJPException.class,
				() -> manager.connect(workspaceId, nonExistingPiece, center)
			);
	}

	/**
	 * Check if puzzle view corresponds to given puzzle info
	 * 
	 * @param puzzle to test
	 * @throws MPJPException  if unexpected exceptions occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetPuzzleView(Puzzle puzzle) throws MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		String workspaceId = manager.createWorkspace(info);
		PuzzleView puzzleView = manager.getPuzzleView(workspaceId);
		
		assertNotNull(puzzleView,"puzzle view expected");
		assertEquals(info.getWidth(),puzzleView.getPuzzleWidth(),"wrong width");
		assertEquals(info.getHeight(),puzzleView.getPuzzleHeight(),"wrong height");
		assertEquals(info.getImageName(),puzzleView.getImage(),"wrong image");
	}

	/**
	 * Check if puzzle layout corresponds to given puzzle info,
	 * particularly in the number of pieces, and the initial
	 * layout should be unsolved. 
	 * 
	 * @param puzzle to test
	 * @throws MPJPException  if unexpected exceptions occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetCurrentLayout(Puzzle puzzle) throws MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		String workspaceId = manager.createWorkspace(info);
		PuzzleLayout puzzleLayout = manager.getCurrentLayout(workspaceId);
		int pieceCount = info.getRows() * info.getColumns();
		
		assertNotNull(puzzleLayout,"puzzleLayout expected");
		assertAll(
				() -> assertEquals(pieceCount,puzzleLayout.getPieces().size(),
						"unexpected #pieces"),
				() -> assertEquals(pieceCount,puzzleLayout.getBlocks().size(),
						"unexpected #blocks (initialy equal to #pieces)")
				);
	}

}
