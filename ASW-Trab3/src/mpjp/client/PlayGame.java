package mpjp.client;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Timer;

import mpjp.client.MPJPResources.MPJPAudioResource;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.CurveTo;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;
import mpjp.shared.geom.QuadTo;
import mpjp.shared.geom.Segment;

public class PlayGame extends Composite {
	private static final int SHADE_SIZE = 4;

	String labelFont;
	private static final int POOL = 1000;
	private static final String CLICK_SOUND_NAME = "click.wav";  
	private static final String SOLVED_SOUND_NAME = "complete.wav";
	private static final String ERROR_SOUND_NAME = "error.wav";
	private static final long FRAMES_PER_SEC = 20L;
	private static final long ANIMATION_TIME_IN_MILLISECS = 500L;
	private static final long ANIMAMTION_DELAY = 1000L / FRAMES_PER_SEC;
	private static final int TOTAL_ANIMATION_FRAMES = (int)
			(ANIMATION_TIME_IN_MILLISECS * FRAMES_PER_SEC / 1000L);

	private final PuzzleServiceAsync puzzleServiceAsync = GWT.create(PuzzleService.class);
	final VerticalPanel allPanels = new VerticalPanel();
	
	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	Label text = new Label("Percentage resolved: 0%        Time: 0:0");
	final Button joinAnotherGame = new Button("Joint to another game");

	private Canvas canvas = Canvas.createIfSupported();
	Context2d gc;

	Timer poolAndPaintTimer = null;

	String workspaceId;
	PuzzleInfo puzzleInfo;
	PuzzleLayout currentPuzzleLayout;
	PuzzleView currentPuzzleView;
	int imageWidth;
	int imageHeight;

	private boolean moving = false;
	Point mousePosition;
	Integer pieceSelected;
	boolean initComplete = false;
	boolean solveComplete = false;
	Integer selectedId;
	Integer selectedBlockId;
	Point delta, diff;

	/**
	 * Paint game and to do some inicializations
	 * @param panels
	 * @param managerService
	 * @param puzzleInfo
	 * @param workspaceId
	 */
	PlayGame(final DeckPanel panels, final PuzzleServiceAsync managerService, PuzzleInfo puzzleInfo, String workspaceId) {
		if (canvas == null) {
			initWidget(new Label("Canvas not supported"));
			return;
		}
		initWidget(allPanels);
		this.puzzleInfo = puzzleInfo;
		this.workspaceId = workspaceId;
		
		initStructureToJoinOption();
		mouseEvent();

		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.addStyleName("title");
		allPanels.add(title);
		allPanels.add(canvas);
		allPanels.add(text);
		allPanels.add(joinAnotherGame);
		
		joinAnotherGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				JoinGame join = new JoinGame(panels, managerService);
				panels.add(join);
				panels.showWidget(3);
			}
		});
	}
	 

	/**
	 * Paint game and to do some inicializations
	 * @param panels
	 * @param managerService
	 * @param imageName
	 * @param cuttingName
	 * @param rows
	 * @param columns
	 * @throws MPJPException
	 */
	PlayGame(final DeckPanel panels, final PuzzleServiceAsync managerService, String imageName, String cuttingName,
			int rows, int columns) throws MPJPException {
		if (canvas == null) {
			initWidget(new Label("Canvas not supported"));
			return;
		}
		initWidget(allPanels);

		initWorkspace(imageName, cuttingName, rows, columns);
		mouseEvent();

		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.addStyleName("title");
		allPanels.add(title);
		allPanels.add(canvas);
		allPanels.add(text);
		allPanels.add(joinAnotherGame);
		
		joinAnotherGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				JoinGame join = new JoinGame(panels, managerService);
				panels.add(join);
				panels.showWidget(3);
			}
		});
	}
	

	/**
	 * Initialize workspace
	 * @param imageName
	 * @param cuttingName
	 * @param rows
	 * @param columns
	 * @throws MPJPException
	 */
	private void initWorkspace(String imageName, String cuttingName, int rows, int columns) throws MPJPException {
		gc = canvas.getContext2d();
		canvas.setCoordinateSpaceHeight(800);
		canvas.setCoordinateSpaceWidth(1200);
		canvas.setStyleName("canvas");
		
		puzzleInfo = new PuzzleInfo(imageName, cuttingName, rows, columns, 500, 500);
		
		try {
			puzzleServiceAsync.createWorkspace(puzzleInfo, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					workspaceId = result;
					try {
						puzzleServiceAsync.getPuzzleView(workspaceId, new AsyncCallback<PuzzleView>() {
							@Override
							public void onSuccess(PuzzleView result2) {
								currentPuzzleView = result2;
								try {
									puzzleServiceAsync.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
										@Override
										public void onSuccess(PuzzleLayout result) {
											currentPuzzleLayout = result;
											if (workspaceId != null && currentPuzzleLayout != null && currentPuzzleView != null)
												initComplete = true;
											solveComplete = false;
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
							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.getMessage());
							}
						});
					} catch (MPJPException e) {
						e.printStackTrace();
					}
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

	
	/**
	 * Initialize workspace
	 * 
	 */
	private void initStructureToJoinOption() {
		gc = canvas.getContext2d();
		canvas.setCoordinateSpaceHeight(800);
		canvas.setCoordinateSpaceWidth(1200);
		canvas.setStyleName("canvas");
		
		try {
			puzzleServiceAsync.getPuzzleView(workspaceId, new AsyncCallback<PuzzleView>() {
				@Override
				public void onSuccess(PuzzleView result2) {
					currentPuzzleView = result2;
					try {
						puzzleServiceAsync.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
							@Override
							public void onSuccess(PuzzleLayout result) {
								currentPuzzleLayout = result;
								if (workspaceId != null && currentPuzzleLayout != null && currentPuzzleView != null)
									initComplete = true;
								solveComplete = false;
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
				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage());
				}
			});
		} catch (MPJPException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Pool data (needed on in concurrent solving) and paints the puzzle.
	 * This will update the elapsed time on the footer 
	 * even if the person is idle.
	 */
	private void poolAndPaint() {
		new Timer() {
			@Override
			public void run() {

				if (solveComplete) {
					cancel();
				} else if (initComplete) {
					try {
						puzzleServiceAsync.getCurrentLayout(workspaceId, new AsyncCallback<PuzzleLayout>() {
							@Override
							public void onSuccess(PuzzleLayout result) {

								currentPuzzleLayout = result;
								drawPuzzle(result);
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
	
	private void mouseEvent() {
		canvas.addMouseDownHandler(e -> {
			mousePressed(e);
		});
		canvas.addMouseMoveHandler(e -> {
			if(moving)
				mouseDragged(e);
		});
		canvas.addMouseUpHandler(e -> {
			mouseReleased(e);
		});
		poolAndPaint();
	}

	
	/**
	 * Activated when mouse is pressed

	 * @param MouseMoveEvent e
	 */
	void mousePressed(MouseDownEvent e) {
		moving = true;
		mousePosition = new Point(e.getX(),e.getY());	//start
		
		try {
			puzzleServiceAsync.selectPiece(workspaceId, mousePosition, new AsyncCallback<Integer>() {
				@Override
				public void onSuccess(Integer result) {
					Integer id = result;
					if(id == null) {
						selectedBlockId = null;
					}
					else {
						PieceStatus piece = currentPuzzleLayout.getPieces().get(id);
						selectedId = id;
						selectedBlockId = piece.getBlock();
						delta = new Point(0,0);
						diff  = new Point(mousePosition.getX() - piece.getX(),mousePosition.getY() - piece.getY());
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.getMessage());
				}
			});
		} catch (MPJPException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Activated when mouse is dragged

	 * @param MouseMoveEvent e
	 */
	private void mouseDragged(MouseMoveEvent e) {
		if(mousePosition != null) { 
			int x = e.getX();
			int y = e.getY();
			
			if(withinWorkspace(e.getX(), e.getY())) {
				delta = new Point(x - mousePosition.getX(), y - mousePosition.getY());
				drawPuzzle(currentPuzzleLayout);
			}
		}
	}
	/**
	 * Activated when mouse is realeased
	 * @param e
	 */
	public void mouseReleased(MouseUpEvent e) {
		moving = false;
		if(selectedId != null) {
			if(withinWorkspace(e.getX(), e.getY())) {
				int blockCount = currentPuzzleLayout.getBlocks().size();
				double x = e.getX() - diff.getX();
				double y = e.getY() - diff.getY();
				Point newMousePosition = new Point(x,y);
			
				try {
					puzzleServiceAsync.connect(workspaceId, selectedId, newMousePosition, new AsyncCallback<PuzzleLayout>() {
						@Override
						public void onSuccess(PuzzleLayout result) {
							currentPuzzleLayout = result;
							mousePosition = null;
							selectedId = null;
							selectedBlockId = null;
							drawPuzzle(result);
							if(result.getBlocks().size() < blockCount) 
								playClip(CLICK_SOUND_NAME);
						}

						@Override
						public void onFailure(Throwable caught) {
							playClip(ERROR_SOUND_NAME);
						}
					});
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		}
	}
	
	/**
	 * Play a loaded sound clip from the start
	 * @param soundName
	 */
	private void playClip(String soundName) {
        MPJPAudioResource clip = MPJPResources.loadAudio(soundName);
        clip.play();
    }
	

	/**
	  Checks if event is within this workspace. When dragging, events
	 * with coordinates outside the window can be sent to it.
	 * @param x
	 * @param y
	 * @return {@code true} is event is within workspace, false otherwise 
	 */
	private boolean withinWorkspace(int x, int y) {
		int width  = (int) currentPuzzleView.getWorkspaceWidth();
		int height = (int) currentPuzzleView.getWorkspaceHeight(); 
		
		return x >= 0 && x <= width && y >= 0 && y <= height;
	}
	
	/**
	 * Repaint with double buffering with given graphic context 
	 * @param puzzleLayout PuzzleLayout puzzleLayout 
	 */
	private void drawPuzzle(PuzzleLayout puzzleLayout) {
		canvas.setCoordinateSpaceWidth(canvas.getCoordinateSpaceWidth());

		//TODO Verificar melhor isto do PaintFinal
		if(solveComplete) 
			paintFinal();
		else if(puzzleLayout.isSolved()) {
			solveComplete = true;
			animateSolvedPuzzle();
			playClip(SOLVED_SOUND_NAME);
		} else 
			paintBlocks(puzzleLayout);
	}
	
	/**
	 * Default paint when puzzle is solved
	 */
	private void paintFinal() {
		int totalWidth  = (int) currentPuzzleView.getWorkspaceWidth();
		int totalHeight = (int) currentPuzzleView.getWorkspaceHeight();
		
		MPJPResources.loadImageElement(puzzleInfo.getImageName(), i -> {
			ImageElement image = i;

			gc.drawImage(image, 0, 0, totalWidth, totalHeight);
		});

	}
	
	
	/**
	 * Animate the complete puzzle (without the pieces borders)
	 * from current position until filling the complete workspace 
	 */
	private void animateSolvedPuzzle() {		
		int totalWidth  = (int) currentPuzzleView.getWorkspaceWidth();
		int totalHeight = (int) currentPuzzleView.getWorkspaceHeight();

		int puzzleWidth = (int) currentPuzzleView.getPuzzleWidth();
		int puzzleHeight = (int) currentPuzzleView.getPuzzleHeight();
		
		showFooter();

		Map<Integer,PieceStatus> pieces = currentPuzzleLayout.getPieces();
		PieceStatus piece0 = pieces.get(0);
		Point location0 = currentPuzzleView.getStandardPieceLocation(0);

		int x0 = (int) (piece0.getX() - location0.getX()); 
		int y0 = (int) (piece0.getY() - location0.getY());
		
		playClip(SOLVED_SOUND_NAME);
		
		new Timer(){
			private int count = 0;

			@Override
			public void run() {
				int x		= inLine(x0,0);
				int y 		= inLine(y0,0);
				int width 	= inLine(puzzleWidth,totalWidth);
				int height 	= inLine(puzzleHeight,totalHeight);
				
				
				MPJPResources.loadImageElement(puzzleInfo.getImageName(), i -> {
					ImageElement image = i;
					gc.drawImage(image,x,y,width,height);
					if(count++ == TOTAL_ANIMATION_FRAMES)
						cancel();
				});

			}	

			private int inLine(int start, int end) {
				return start +  count * (end - start) / TOTAL_ANIMATION_FRAMES;
			}

		}.scheduleRepeating((int) ANIMAMTION_DELAY);
	}
	
	/**
	 * Paint all the blocks, leaving the selected block for last
	 * to make it hover over the others
	 * 
	 * @param puzzleLayout PuzzleLayout puzzleLayout 
	 */
	private void paintBlocks(PuzzleLayout puzzleLayout) {
		Map<Integer, List<Integer>> blocks = puzzleLayout.getBlocks();
		
		for (int blockId : blocks.keySet()) {
			if (selectedBlockId != null && blockId == selectedBlockId)
				continue;
			moving = false;
			paintBlock(blocks.get(blockId), false, puzzleLayout);
		}

		if (selectedBlockId != null) {
			paintBlock(blocks.get(selectedBlockId), true, puzzleLayout);
			moving = true;
		}

		showFooter();
	}
	
	
	/**
	 * Paint a block of connected (thus, non-overlapping) pieces
	 * 
	 * @param puzzleLayout PuzzleLayout puzzleLayout 
	 * @param pieceIDs list piece IDs
	 * @param dragging {@true} if user dragging; {@false} otherwise
	 */
	private void paintBlock(List<Integer> pieceIDs, boolean dragging, PuzzleLayout puzzleLayout) {
		try {
			if(dragging)
				for(int id: pieceIDs) {
					gc.save();	//Guarda o contexto do que está guardado  atualmente
					gc.translate(SHADE_SIZE,SHADE_SIZE);
					paintPiece(id,dragging, puzzleLayout);
					gc.restore();
				}
		
			for(int id: pieceIDs) {
				gc.save();	//Guarda o contexto do que está guardado  atualmente
				paintPiece(id,dragging,puzzleLayout);
				gc.restore();

			}
		} catch(RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Paint a single piece in a block, or its shade
	 *  
	 * @param puzzleLayout PuzzleLayout puzzleLayout 
	 * @param id of piece being painted
	 * @param dragging {@true} if user dragging; {@false} otherwise
	 */
	void paintPiece(int id,boolean dragging, PuzzleLayout puzzleLayout) {
		
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		PieceStatus pieceStatus = pieces.get(id);
		Point center =  pieceStatus.getPosition();
		double rotation = pieceStatus.getRotation();

		if(dragging)
			gc.translate(delta.getX(),delta.getY());
		gc.translate(center.getX(),center.getY());
		gc.rotate(rotation);

		paintShape(currentPuzzleView.getPieceShape(id));
		paintPieceWithImage(id);
	}
	
	/**
	 * Convert a PieceShape into a {@code java.asw.Shape},
	 * useful for displaying using AWT and use its features 
	 * 
	 * @param pieceShape
	 * @return
	 */
	private void paintShape(PieceShape pieceShape) {
		Point start = pieceShape.getStartPoint();
		gc.beginPath();
		gc.moveTo(start.getX(), start.getY());
		
		for(Segment segment: pieceShape.getSegments()) {
			if(segment instanceof LineTo) {
				LineTo lineTo = (LineTo) segment;
				Point endPoint = lineTo.getEndPoint();
				
				gc.lineTo(endPoint.getX(), endPoint.getY());
			} else if(segment instanceof QuadTo) {
				QuadTo quadTo = (QuadTo) segment;
				Point endPoint = quadTo.getEndPoint();
				Point controlPoint = quadTo.getControlPoint();
				
				gc.quadraticCurveTo(
						controlPoint.getX(),controlPoint.getY(),
						endPoint.getX(), endPoint.getY());
			} else if(segment instanceof CurveTo) {
				CurveTo curveTo = (CurveTo) segment;
				Point endPoint = curveTo.getEndPoint();
				Point controlPoint1 = curveTo.getControlPoint1();
				Point controlPoint2 = curveTo.getControlPoint2();
				
				gc.bezierCurveTo(
						controlPoint1.getX(),controlPoint1.getY(),
						controlPoint2.getX(),controlPoint2.getY(),
						endPoint.getX(), endPoint.getY());
			}
		}
		gc.setStrokeStyle("#000000");
		gc.stroke();
	}

	/**
	 * Paint a piece given its shape with the image as background
	 * 
	 * @param id of piece
	 * @param shape of piece
	 */
	private void paintPieceWithImage(int id ) {
		gc.save();
		Point location = currentPuzzleView.getStandardPieceLocation(id);
		int x = (int) -location.getX();
		int y = (int) -location.getY();
	
		int width = (int) currentPuzzleView.getPuzzleWidth();
		int height = (int) currentPuzzleView.getPuzzleHeight();
		
		
		MPJPResources.loadImageElement(puzzleInfo.getImageName(), i -> {
			ImageElement image = i;
			gc.clip();
			gc.drawImage(image, x, y, width, height);
			gc.restore();
		});
	}
	
	
	/**
	 * Show footer with puzzle status: percentage complete e solving time
	 */
	private void showFooter() {
		int complete = currentPuzzleLayout.getPercentageSolved();
		
		Date start = currentPuzzleView.getStart();
		int elapsed = (int) ((new Date().getTime() - start.getTime())/60L/1000L);
		int hours  = elapsed / 60;
		int minutes = elapsed % 60;
		
		text.setText("Percentage resolved: " + complete + "%" + "        Time: " + hours + ":" + minutes);
	}
	
}
