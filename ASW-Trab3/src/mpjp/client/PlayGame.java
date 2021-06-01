package mpjp.client;

import java.util.Date;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Timer;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;

public class PlayGame extends Composite {

	private final PuzzleServiceAsync puzzleServiceAsync = GWT.create(PuzzleService.class);
	final VerticalPanel allPanels = new VerticalPanel();

	private Canvas canvas = Canvas.createIfSupported();;
	private static final int SIDE = 500;
	private static final int POOL = 1000;
	private Date lastUpdate = new Date();

	private boolean moving = false;
	Timer poolAndPaintTimer = null;
	private static final String RESOURCE_DIR = "mpjp/resources/";
	/**
	 * Pooling delay to update layout data and update view
	 */
	private static final long POOLING_DELAY = 60L * 1000L;

	String workspaceId;
	PuzzleInfo puzzleInfo;
	PuzzleLayout currentPuzzleLayout;
	PuzzleView currentPuzzleView;

	boolean initComplete = false;
	boolean solveComplete = false;

	PlayGame(final DeckPanel panels, final PuzzleServiceAsync managerService, int gameOption, String imageName,
			String cuttingName, String dimension) throws MPJPException {
		if (canvas == null) {
			initWidget(new Label("Canvas not supported"));
			return;
		}
		initWidget(allPanels);
		initWorkspace(gameOption, imageName, cuttingName, 10, 10);
		configureCanvas();

		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		allPanels.add(canvas);
	}

	private void initWorkspace(int gameOption, String imageName, String cuttingName, int rows, int columns)
			throws MPJPException {
		puzzleInfo = new PuzzleInfo(imageName, cuttingName, rows, columns, 600, 600);

		if (gameOption == 1) {		// option create a new game
			puzzleServiceAsync.createWorkspace(puzzleInfo, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					workspaceId = result;
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage());
				}
			});
		} else {
			// TODO Aqui é para o caso do user escolher juntar-se a outro jogo.
			// É suposto ir buscar o id do jogo escolhido e atribui-lo
		}

		puzzleServiceAsync.getPuzzleView(cuttingName, new AsyncCallback<PuzzleView>() {
			@Override
			public void onSuccess(PuzzleView result) {
				currentPuzzleView = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
			}
		});

		puzzleServiceAsync.getCurrentLayout(cuttingName, new AsyncCallback<PuzzleLayout>() {
			@Override
			public void onSuccess(PuzzleLayout result) {
				currentPuzzleLayout = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
			}
		});

		initComplete = true;
		solveComplete = false;

		poolAndPaint();
		//configureTimer();
	}

	/*private void configureTimer() {
		new Timer() {
			public void run() {
				Date now = new Date();

				if (now.getTime() - lastUpdate.getTime() > POOL / 10)
					drawPuzzle(null);
			}
		}.scheduleRepeating(POOL);
	}*/

	private void poolAndPaint() {

		if (poolAndPaintTimer != null)
			poolAndPaintTimer.cancel();

		//poolAndPaintTimer = new Timer();
		new Timer() {

			@Override
			public void run() {
				if (solveComplete)
					cancel();
				else {
					try {
						puzzleServiceAsync.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
							@Override
							public void onSuccess(PuzzleLayout result) {
								currentPuzzleLayout = result;
								doublePaint();
							}

							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.getMessage());							
							}
						});
					} catch (MPJPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.scheduleRepeating(POOL);
	}
	
	private void doublePaint() {
		// TODO Auto-generated method stub
		
	}

	private void configureCanvas() {
		canvas.setCoordinateSpaceWidth(SIDE);
		canvas.setCoordinateSpaceHeight(SIDE);

		canvas.addMouseDownHandler(e -> {
			moving = true;
		});
		canvas.addMouseMoveHandler(e -> {
			if (moving)
				drawPuzzle(e);
		});
		canvas.addMouseUpHandler(e -> {
			moving = false;
		});
	}

	private void drawPuzzle(MouseMoveEvent e) {
		/*
		 * puzzleServiceAsync.getCurrentLayout(workspaceId, new
		 * AsyncCallback<PuzzleLayout>() {
		 * 
		 * @Override public void onSuccess(PuzzleLayout result) { lastUpdate = new
		 * Date(); }
		 * 
		 * @Override public void onFailure(Throwable caught) {
		 * GWT.log(caught.getMessage());
		 * 
		 * } });
		 */
	}

}
