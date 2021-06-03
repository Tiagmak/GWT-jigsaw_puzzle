package mpjp.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Timer;

import mpjp.game.Workspace;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

public class PlayGame extends Composite {
	private static final int SHADE_SIZE = 4;

	private final PuzzleServiceAsync puzzleServiceAsync = GWT.create(PuzzleService.class);
	final VerticalPanel allPanels = new VerticalPanel();

	private Canvas canvas = Canvas.createIfSupported();
	Context2d gc;
	private static final int POOL = 1000;
	private Date lastUpdate = new Date();

	private boolean moving = false;
	Timer poolAndPaintTimer = null;

	String workspaceId;
	PuzzleInfo puzzleInfo;
	PuzzleLayout currentPuzzleLayout;
	PuzzleView currentPuzzleView;
	//Workspace workspace;
	int imageWidth;
	int imageHeight;

	Point mousePosition;
	Integer pieceSelected;
	boolean initComplete = false;
	boolean solveComplete = false;
	Integer selectedId;
	Integer selectedBlockId;
	Point delta, diff;

	/*
	 * PlayGame(final DeckPanel panels, PuzzleInfo puzzleInfo, String workspaceId) {
	 * if (canvas == null) { initWidget(new Label("Canvas not supported")); return;
	 * } initWidget(allPanels); this.puzzleInfo = puzzleInfo; this.workspaceId =
	 * workspaceId; initOtherStructures();
	 * 
	 * if(workspaceId != null && currentPuzzleLayout != null && currentPuzzleView !=
	 * null) initComplete = true; solveComplete = false;
	 * 
	 * allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	 * allPanels.add(canvas); }
	 */

	PlayGame(final DeckPanel panels, final PuzzleServiceAsync managerService, String imageName, String cuttingName,
			int rows, int columns) throws MPJPException {
		if (canvas == null) {
			initWidget(new Label("Canvas not supported"));
			return;
		}
		initWidget(allPanels);
		initWorkspace(imageName, cuttingName, rows, columns);

		/*
		 * puzzleServiceAsync.createWorkspace(puzzleInfo, new AsyncCallback<String>() {
		 * 
		 * @Override public void onSuccess(String result) { workspaceId = result;
		 * initOtherStructures();
		 * 
		 * if(workspaceId != null && currentPuzzleLayout != null && currentPuzzleView !=
		 * null) initComplete = true; solveComplete = false;
		 * 
		 * }
		 * 
		 * @Override public void onFailure(Throwable caught) {
		 * GWT.log(caught.getMessage()); } });
		 */
		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		allPanels.add(canvas);
	}

	private void initWorkspace(String imageName, String cuttingName, int rows, int columns) throws MPJPException {
		GWT.log("tentei0");
		MPJPResources.loadImageElement(imageName, i -> {
			GWT.log("tentei1");

			ImageElement image = i;

			/*imageWidth = image.getWidth();
			imageHeight = image.getHeight();
			GWT.log("tentei2");

			canvas.setVisible(true);
			canvas.setCoordinateSpaceHeight(imageHeight);
			canvas.setCoordinateSpaceWidth(imageWidth);
			Context2d context2d = canvas.getContext2d();
			context2d.drawImage(image, 0, 0, imageWidth, imageHeight);

			puzzleInfo = new PuzzleInfo(imageName, cuttingName, rows, columns, imageWidth, imageHeight);*/
			
		});
	}

	private void initOtherStructures() {
		//TODO workspace = new Workspace(puzzleInfo);
		gc = canvas.getContext2d();
		try {
			puzzleServiceAsync.getPuzzleView(workspaceId, new AsyncCallback<PuzzleView>() {
				@Override
				public void onSuccess(PuzzleView result) {
					currentPuzzleView = result;
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage());
				}
			});
		} catch (MPJPException e) {
			e.printStackTrace();
		}
		try {
			puzzleServiceAsync.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
				@Override
				public void onSuccess(PuzzleLayout result) {
					currentPuzzleLayout = result;
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage());

				}
			});
		} catch (MPJPException e) {
			e.printStackTrace();
		}
	}


	private void timer() {
		new Timer() {
			@Override
			public void run() {
				Date now = new Date();

				if (solveComplete) {
					cancel();
				} else if (now.getTime() - lastUpdate.getTime() > POOL / 10 && initComplete) {
					try {
						puzzleServiceAsync.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
							@Override
							public void onSuccess(PuzzleLayout result) {
								currentPuzzleLayout = result;
								drawPuzzle();
							}

							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.getMessage());

							}
						});
					} catch (MPJPException e) {
						e.printStackTrace();
					}
				} else {
					// TODO pôr aqui algo não sei o quê
				}
			}

		}.scheduleRepeating(POOL);
	}
	
	void drawPuzzle() {
		
	}
	
}


