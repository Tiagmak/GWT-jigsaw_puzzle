package mpjp.client;

import java.util.HashSet;

import com.google.gwt.core.client.GWT;
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
			for(int i = 2; i<=10; i++) {
				dropBoxRows.addItem(""+i);
			}
		});
		
		dropBoxColumns.setMultipleSelect(false);
		dropBoxColumns.addAttachHandler(e -> {
			for(int i = 2; i<=10; i++) {
				dropBoxColumns.addItem(""+i);
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
		centralPanel.setSpacing(30);
		centralPanel.addStyleName("centralPanel");
		centralPanel.add(imagesLabel);
		centralPanel.add(dropBoxImages);
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

		buttonPlayGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				PlayGame playGame = null;
				try {
					String imageName         = dropBoxImages.getValue(dropBoxImages.getSelectedIndex());
					String imageNamecuttings = dropBoxCuttings.getValue(dropBoxCuttings.getSelectedIndex());
					
					String rowsString = dropBoxRows.getValue(dropBoxRows.getSelectedIndex());
					String columnsString = dropBoxColumns.getValue(dropBoxColumns.getSelectedIndex());
					
					int rowsNr =  Integer.parseInt(rowsString);
					int columnsNr =  Integer.parseInt(columnsString);
					
					playGame = new PlayGame(panels, managerService, imageName, imageNamecuttings, rowsNr, columnsNr);
				} catch (MPJPException e) {
					e.printStackTrace();
				}
				panels.add(playGame);
				panels.showWidget(2);
			}
		});
		
	}
}
