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
	Workspace workspace;
	int imageWidth;
	int imageHeight;

	Point mousePosition;
	Integer pieceSelected;
	boolean initComplete = false;
	boolean solveComplete = false;
	Integer selectedId;
	Integer selectedBlockId;
	Point delta, diff;
	ImageElement image = null;

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

			image = i;

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
		workspace = new Workspace(puzzleInfo);
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

	private void mouseEvent() {
		canvas.addMouseDownHandler(e -> {
			mousePressed(e);
		});
		canvas.addMouseMoveHandler(e -> {
			mouseDragged(e);
		});
		canvas.addMouseUpHandler(e -> {
			mouseReleased(e);
		});
		timer();
	}

	void mousePressed(MouseDownEvent e) {
		moving = true;
		mousePosition = new Point(e.getX(),e.getY());	//start
		Integer id = workspace.selectPiece(mousePosition);
		
		if(id == null)
			selectedBlockId = null;
		else {
			PieceStatus piece = currentPuzzleLayout.getPieces().get(id);
			selectedId = id;
			selectedBlockId = piece.getBlock();
			delta = new Point(0,0);
			diff  = new Point(e.getX() - piece.getX(),e.getY() - piece.getY());
			drawPuzzle();
		}
	}
	
	private void mouseDragged(MouseMoveEvent e) {
		if(mousePosition != null) { 
			int x = e.getX();
			int y = e.getY();
			
			if(withinWorkspace(e.getX(), e.getY()))
				delta = new Point(x - mousePosition.getX(), y - mousePosition.getY());
			drawPuzzle();
		}
	}
	
	public void mouseReleased(MouseUpEvent e) {
		moving = false;
		if(selectedId != null) {
			if(withinWorkspace(e.getX(), e.getY())) {
				int blockCount = currentPuzzleLayout.getBlocks().size();
				double x = e.getX() - diff.getX();
				double y = e.getY() - diff.getY();
				Point point = new Point(x,y);
			
				try {
					puzzleServiceAsync.connect(workspaceId, POOL, mousePosition, new AsyncCallback<PuzzleLayout>() {
						@Override
						public void onSuccess(PuzzleLayout result) {
							currentPuzzleLayout = result;
							mousePosition = null;
							selectedId = null;
							selectedBlockId = null;
						}

						@Override
						public void onFailure(Throwable caught) {
							GWT.log(caught.getMessage());
						}
					});
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
			drawPuzzle();
		}
	}
	
	private boolean withinWorkspace(int x, int y) {
		int width  = (int) currentPuzzleView.getWorkspaceWidth();
		int height = (int) currentPuzzleView.getWorkspaceHeight(); 
		
		return x >= 0 && x <= width && y >= 0 && y <= height;
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

	private void drawPuzzle() {
		if(solveComplete) 
			paintFinal();
		else if(currentPuzzleLayout.isSolved()) {
			solveComplete = true;
			animateSolvedPuzzle(); 
		} else 
			paintBlocks();
	}

	private void animateSolvedPuzzle() {
		//TODO Preencher isto ???
	}
	
	private void paintFinal() {
		int totalWidth  = (int) currentPuzzleView.getWorkspaceWidth();
		int totalHeight = (int) currentPuzzleView.getWorkspaceHeight();
		
		/*if(image == null) {  //TODO Fazer isto
			//gc.setColor(Color.WHITE);
			//gc.fillRect(0,0,totalWidth,totalHeight);
		} else
			//gc.drawImage(image,0,0,totalWidth,totalHeight,this);  */
	}

	private void paintBlocks() {
		Map<Integer, List<Integer>> blocks = currentPuzzleLayout.getBlocks();
		
		for (int blockId : blocks.keySet()) {
			if (selectedBlockId != null && blockId == selectedBlockId)
				continue;
			paintBlock(blocks.get(blockId), false);
		}

		if (selectedBlockId != null)
			paintBlock(blocks.get(selectedBlockId), true);

		showFooter();
	}
	
	private void paintBlock(List<Integer> pieceIDs, boolean dragging) {
		//AffineTransform initialTransform = gc.getTransform();		//TODO ????
		try {
			if(dragging)
				for(int id: pieceIDs) {
					//gc.translate(SHADE_SIZE,SHADE_SIZE);
					paintPiece(id,dragging,true);
					//gc.setTransform(initialTransform);
				}
		
			for(int id: pieceIDs) {
				paintPiece(id,dragging,false);
			}
		} catch(RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	void paintPiece(int id,boolean dragging,boolean shading) {
		Shape shape;  /*= ShapeChanger.getShape(currentPuzzleView.getPieceShape(id));*/ ///??
		Map<Integer, PieceStatus> pieces = currentPuzzleLayout.getPieces();
		PieceStatus pieceStatus = pieces.get(id);
		Point center =  pieceStatus.getPosition();
		double rotation = pieceStatus.getRotation();

		if(dragging)
			gc.translate(delta.getX(),delta.getY());
		gc.translate(center.getX(),center.getY());
		gc.rotate(rotation);
		
		/*if(shading)
			paintShade(shape);
		else if(image == null) 
			paintPieceWithLabel(g2,id,shape);
		else 
			paintPieceWithImage(g2,id,shape);*/
	}
	
	private void paintShade(Shape shape) {
		/*gc.setShadowColor(Color.DARK_GRAY);		//????
		gc.fill(shape);*/
	}

	private void showFooter() {
		//TODO Esta função vai mostrar a percentagem resolvida e o tempo que a pessoa demorou	
	}


}
