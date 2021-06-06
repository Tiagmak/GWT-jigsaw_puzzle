package mpjp.client;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import mpjp.shared.MPJPException;

public class CreateGame extends Composite {

	final VerticalPanel allPanels = new VerticalPanel();
	final VerticalPanel centralPanel = new VerticalPanel();

	final HorizontalPanel imagesPanel = new HorizontalPanel();
	final HorizontalPanel cuttingsPanel = new HorizontalPanel();
	final HorizontalPanel dimensionPanel = new HorizontalPanel();

	final HorizontalPanel buttonsPanel = new HorizontalPanel();

	ImageElement image;
	// Image img2 = new Image("exterior3.jpg");
	// Image img3 = new Image("exterior7.jpg");

	RadioButton img1Button = new RadioButton("images", "oo");

	//RadioButton cuttingRoundButton = new RadioButton("cuttings", "Round");
	//RadioButton cuttingStraightButton = new RadioButton("cuttings", "Straight");
	//RadioButton cuttingTriangularButton = new RadioButton("cuttings", "Triangular");
	//RadioButton cuttingStandardButton = new RadioButton("cuttings", "Standard");

	RadioButton dimension1x2Button = new RadioButton("dimension", "1x2");
	RadioButton dimension2x1Button = new RadioButton("dimension", "2x1");
	RadioButton dimension5x5Button = new RadioButton("dimension", "5x5");
	RadioButton dimension8x9Button = new RadioButton("dimension", "8x9");
	RadioButton dimension10x10Button = new RadioButton("dimension", "10x10");
	RadioButton dimension14x15Button = new RadioButton("dimension", "14x15");

	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	final Label images = new HTML("Images");
	final Label cuttings = new HTML("Cutting");
	final Label dimensions = new HTML("Dimension");

	Button buttonBack = new Button("Back");
	Button buttonPlayGame = new Button("Play Game");

	CreateGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);

		final ListBox dropBox = new ListBox();
		dropBox.setMultipleSelect(false);
		GWT.log(DEBUG_ID_PREFIX);
		dropBox.addAttachHandler(e -> {
			managerService.getAvailableImages(new AsyncCallback<HashSet<String>>() {
				@Override
				public void onSuccess(HashSet<String> result) {
					for (String string : result) {
						dropBox.addItem(string);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(DEBUG_ID_PREFIX);
				}
			});
		});
		//////////////////////////////////////////////////////////////////////////////////
		final ListBox dropBoxCuttings = new ListBox();
		dropBoxCuttings.setMultipleSelect(false);
		GWT.log(DEBUG_ID_PREFIX);
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
					// TODO Auto-generated method stub
					GWT.log(DEBUG_ID_PREFIX);
				}
			});
		});
		
		
		final ListBox dropBoxDimension = new ListBox();
		dropBoxDimension.setMultipleSelect(false);
		GWT.log(DEBUG_ID_PREFIX);
		dropBoxDimension.addAttachHandler(e -> {
						dropBoxDimension.addItem("1x2");
						dropBoxDimension.addItem("2x1");
						dropBoxDimension.addItem("5x5");
						dropBoxDimension.addItem("8x9");
						dropBoxDimension.addItem("10x10");
		});
		
		
		
		imagesPanel.add(dropBox);
		
		cuttingsPanel.setSpacing(55);
		cuttingsPanel.add(dropBoxCuttings);
		//cuttingsPanel.add(cuttingRoundButton);
		//cuttingsPanel.add(cuttingStraightButton);
		//cuttingsPanel.add(cuttingTriangularButton);
		//cuttingsPanel.add(cuttingStandardButton);
		//cuttingRoundButton.setValue(true);

		dimensionPanel.setSpacing(55);
		//dimensionPanel.add(dimension1x2Button);
		//dimensionPanel.add(dimension2x1Button);
		//dimensionPanel.add(dimension5x5Button);
		//dimensionPanel.add(dimension8x9Button);
		//dimensionPanel.add(dimension10x10Button);
		//dimensionPanel.add(dimension14x15Button);
		//dimension1x2Button.setValue(true);
		dimensionPanel.add(dropBoxDimension);
		
				buttonsPanel.setSpacing(35);
		buttonsPanel.add(buttonBack);
		buttonsPanel.add(buttonPlayGame);

		centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		centralPanel.addStyleName("centralPanel");
		images.addStyleName("subtitle");
		cuttings.addStyleName("subtitle");
		dimensions.addStyleName("subtitle");
		centralPanel.add(images);
		centralPanel.add(imagesPanel);
		centralPanel.add(cuttings);
		centralPanel.add(cuttingsPanel);
		centralPanel.add(dimensions);
		centralPanel.add(dimensionPanel);
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

		//String cuttings = "Round";
		
		buttonPlayGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				PlayGame playGame = null;
				try {
					String imageName         = dropBox.getValue(dropBox.getSelectedIndex());
					String imageNamecuttings = dropBoxCuttings.getValue(dropBoxCuttings.getSelectedIndex());
					GWT.log("imageName : " + imageName);
					GWT.log("imageNamecuttings: "+imageNamecuttings);
					playGame = new PlayGame(panels, managerService, imageName, imageNamecuttings, 10, 10);
				} catch (MPJPException e) {
					e.printStackTrace();
				}
				panels.add(playGame);
				panels.showWidget(2);
			}
		});
		
	}
}
