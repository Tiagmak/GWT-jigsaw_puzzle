package mpjp.client;

import java.util.HashSet;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import mpjp.shared.MPJPException;

public class CreateGame extends Composite {
	private Canvas canvas = Canvas.createIfSupported();
	Context2d gc;

	final VerticalPanel allPanels = new VerticalPanel();
	final VerticalPanel centralPanel = new VerticalPanel();
	final HorizontalPanel buttonsPanel = new HorizontalPanel();
	final HorizontalPanel rowsPanel = new HorizontalPanel();
	final HorizontalPanel columnsPanel = new HorizontalPanel();

	final ListBox dropBoxImages = new ListBox();
	final ListBox dropBoxCuttings = new ListBox();
	final ListBox dropBoxRows = new ListBox();
	final ListBox dropBoxColumns = new ListBox();

	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	final Label imagesLabel = new HTML("Images");
	final Label cuttingsLabel = new HTML("Cutting");
	final Label dimensionsLabel = new HTML("Dimensions");
	final Label rowsLabel = new HTML("Rows: ");
	final Label columnsLabel = new HTML("Columns: ");

	Button buttonBack = new Button("Back");
	Button buttonPlayGame = new Button("Play Game");

	CreateGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);
		gc = canvas.getContext2d();
		canvas.setCoordinateSpaceHeight(700);
		canvas.setCoordinateSpaceWidth(700);

		paintCanvas("exterior7.jpg");

		dropBoxImages.setMultipleSelect(false);
		dropBoxImages.addAttachHandler(e -> {
			managerService.getAvailableImages(new AsyncCallback<HashSet<String>>() {
				@Override
				public void onSuccess(HashSet<String> result) {
					for (String string : result) {
						dropBoxImages.addItem(string);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(DEBUG_ID_PREFIX);
				}
			});
		});

		dropBoxCuttings.setMultipleSelect(false);
		dropBoxCuttings.addAttachHandler(e -> {
			managerService.getAvailableCuttings(new AsyncCallback<HashSet<String>>() {
				@Override
				public void onSuccess(HashSet<String> result) {
					for (String string : result) {
						dropBoxCuttings.addItem(string);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(DEBUG_ID_PREFIX);
				}
			});
		});

		dropBoxRows.setMultipleSelect(false);
		dropBoxRows.addAttachHandler(e -> {
			for (int i = 2; i <= 10; i++) {
				dropBoxRows.addItem("" + i);
			}
		});

		dropBoxColumns.setMultipleSelect(false);
		dropBoxColumns.addAttachHandler(e -> {
			for (int i = 2; i <= 10; i++) {
				dropBoxColumns.addItem("" + i);
			}
		});

		imagesLabel.addStyleName("subtitle");
		cuttingsLabel.addStyleName("subtitle");
		dimensionsLabel.addStyleName("subtitle");

		rowsPanel.add(rowsLabel);
		rowsPanel.add(dropBoxRows);

		columnsPanel.add(columnsLabel);
		columnsPanel.add(dropBoxColumns);

		buttonsPanel.add(buttonBack);
		buttonsPanel.add(buttonPlayGame);

		centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		centralPanel.setSpacing(15);
		centralPanel.addStyleName("centralPanel");
		centralPanel.add(imagesLabel);
		centralPanel.add(dropBoxImages);
		centralPanel.add(canvas);
		centralPanel.add(cuttingsLabel);
		centralPanel.add(dropBoxCuttings);
		centralPanel.add(dimensionsLabel);
		centralPanel.add(rowsPanel);
		centralPanel.add(columnsPanel);
		centralPanel.add(buttonsPanel);

		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.addStyleName("title");
		allPanels.add(title);
		allPanels.add(centralPanel);

		buttonBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panels.remove(1);
				panels.showWidget(0);
			}
		});

		dropBoxImages.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				String imageName = dropBoxImages.getValue(dropBoxImages.getSelectedIndex());
				GWT.log(imageName);
				paintCanvas(imageName);
			}
		});

		buttonPlayGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				PlayGame playGame = null;
				try {
					String imageName = dropBoxImages.getValue(dropBoxImages.getSelectedIndex());
					String imageNamecuttings = dropBoxCuttings.getValue(dropBoxCuttings.getSelectedIndex());

					String rowsString = dropBoxRows.getValue(dropBoxRows.getSelectedIndex());
					String columnsString = dropBoxColumns.getValue(dropBoxColumns.getSelectedIndex());

					int rowsNr = Integer.parseInt(rowsString);
					int columnsNr = Integer.parseInt(columnsString);

					playGame = new PlayGame(panels, managerService, imageName, imageNamecuttings, rowsNr, columnsNr);
				} catch (MPJPException e) {
					e.printStackTrace();
				}
				panels.add(playGame);
				panels.showWidget(2);
			}
		});

	}

	void paintCanvas(String imageName) {
		MPJPResources.loadImageElement(imageName, i -> {
			ImageElement image = i;
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();

			canvas.setCoordinateSpaceHeight(200);
			canvas.setCoordinateSpaceWidth(200);
			gc.drawImage(image, 4, 4, imageWidth / 6, imageHeight / 5);
		});
	}
}
