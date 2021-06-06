package mpjp.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;

class JoinGame extends Composite {
	final VerticalPanel allPanels = new VerticalPanel();
	final VerticalPanel centralPanel = new VerticalPanel();
	final HorizontalPanel buttonspanel = new HorizontalPanel();
	final ListBox dropBox = new ListBox();

	Map<String, PuzzleSelectInfo> allAvailableWorkspaces = new HashMap<>();
	HashMap<Integer, String> puzzleNrs = new HashMap<Integer, String>();

	Grid grid = new Grid(1, 3);

	int currentRow = 0;
	int currentColumn = 0;
	int puzzleNr = 0;

	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	final Label ask = new Label("Please choose a puzzle:");
	final Button startGameButton = new Button("Start");

	JoinGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);

		dropBox.setMultipleSelect(false);

		dropBox.addAttachHandler(e -> {
			managerService.getAvailableWorkspaces(new AsyncCallback<HashMap<String, PuzzleSelectInfo>>() {

				public void onSuccess(HashMap<String, PuzzleSelectInfo> result) {
					if (result.isEmpty()) {
						centralPanel.add(new Label("There aren't available puzzles now"));
						allPanels.add(title);
						allPanels.add(centralPanel);
						return;
					}
					int rows = result.size() / 3 + 1;
					grid.resize(rows, 3);

					for (Entry<String, PuzzleSelectInfo> w : result.entrySet()) {
						allAvailableWorkspaces.put(w.getKey(), w.getValue());

						String workspaceId = w.getKey();
						PuzzleSelectInfo puzzleSelectInfo = w.getValue();
						puzzleNrs.put(puzzleNr, workspaceId);

						Canvas canvas = Canvas.createIfSupported();
						VerticalPanel imagePanel = new VerticalPanel();
						Label nrPuzzleLabel = new Label("Puzzle " + puzzleNr);
						dropBox.addItem("" + puzzleNr);
						Label imageFooter = new Label("Resolved percentaged:" + puzzleSelectInfo.getPercentageSolved());
						Context2d gc = canvas.getContext2d();

						try {
							managerService.getPuzzleView(workspaceId, new AsyncCallback<PuzzleView>() {
								@Override
								public void onSuccess(PuzzleView result) {
									MPJPResources.loadImageElement(result.getImage(), i -> {
										ImageElement image = i;
										int imageWidth = image.getWidth();
										int imageHeight = image.getHeight();

										canvas.setCoordinateSpaceHeight(250);
										canvas.setCoordinateSpaceWidth(250);
										gc.drawImage(image, 4, 4, imageWidth / 6, imageHeight / 5);

										imagePanel.addStyleName("imagePanel");
										imagePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
										imagePanel.add(nrPuzzleLabel);
										imagePanel.add(canvas);
										imagePanel.add(imageFooter);
										grid.setWidget(currentRow, currentColumn, imagePanel);

										if (currentColumn == 2) {
											currentRow++;
											currentColumn = 0;
										} else {
											currentColumn++;
										}
									});
								}

								@Override
								public void onFailure(Throwable caught) {
									GWT.log(DEBUG_ID_PREFIX);
								}
							});
						} catch (MPJPException e) {
							e.printStackTrace();
						}
						puzzleNr++;
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(DEBUG_ID_PREFIX);
				}
			});
		});

		buttonspanel.add(startGameButton);

		centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		centralPanel.setSpacing(30);
		ask.addStyleName("ask");
		centralPanel.add(ask);
		centralPanel.add(dropBox);
		centralPanel.add(grid);
		centralPanel.add(buttonspanel);

		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.addStyleName("title");
		allPanels.add(title);
		allPanels.add(centralPanel);

		startGameButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String imageNr = dropBox.getValue(dropBox.getSelectedIndex());
				int nrSelect = Integer.parseInt(imageNr);

				String WId = puzzleNrs.get(nrSelect);
				PuzzleSelectInfo puzzleSelectInfo = allAvailableWorkspaces.get(WId);
				GWT.log(puzzleSelectInfo.getCuttingName());

				PuzzleInfo puzzleInfo = new PuzzleInfo(puzzleSelectInfo.getImageName(),
						puzzleSelectInfo.getCuttingName(), puzzleSelectInfo.getRows(), puzzleSelectInfo.getColumns(),
						500, 500);

				PlayGame playGame = new PlayGame(panels, managerService, puzzleInfo, WId);
				panels.add(playGame);
				panels.showWidget(2);
			}
		});

	}
}
