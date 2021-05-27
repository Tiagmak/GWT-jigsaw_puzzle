package mpjp.client;


import java.util.Set;
import java.util.function.Consumer;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateGame extends Composite {

	final VerticalPanel allPanels = new VerticalPanel();
	final VerticalPanel centralPanel = new VerticalPanel();

	final HorizontalPanel imagesPanel = new HorizontalPanel();
	final HorizontalPanel cuttingsPanel = new HorizontalPanel();
	final HorizontalPanel dimensionPanel = new HorizontalPanel();

	final HorizontalPanel buttonsPanel = new HorizontalPanel();
	
	ImageElement image;
	//Image img2 = new Image("exterior3.jpg");
	//Image img3 = new Image("exterior7.jpg");

	RadioButton img1Button = new RadioButton("images","oo");




	RadioButton cuttingRoundButton = new RadioButton("cuttings", "Round");
	RadioButton cuttingStraightButton = new RadioButton("cuttings", "Straight");
	RadioButton cuttingTriangularButton = new RadioButton("cuttings", "Triangular");
	RadioButton cuttingStandardButton = new RadioButton("cuttings", "Standard");
	
	RadioButton dimension1x2Button = new RadioButton("dimension", "1x2");
	RadioButton dimension2x1Button = new RadioButton("dimension", "2x1");
	RadioButton dimension5x5Button = new RadioButton("dimension", "5x5");
	RadioButton dimension8x9Button = new RadioButton("dimension", "8x9");
	RadioButton dimension10x10Button = new RadioButton("dimension", "10x10");
	RadioButton dimension14x15Button = new RadioButton("dimension", "14x15");

	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	final Label images = new HTML("Images");
	final Label cuttings = new HTML("Cuting");
	final Label dimensions = new HTML("Dimension");
	
	Button buttonBack = new Button("Back");
	Button buttonPlayGame = new Button("Play Game");

	CreateGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);
		
		Canvas canvas = Canvas.createIfSupported();
		final Context2d context2d = canvas.getContext2d();
		
		/*MPJPResources.loadImageElement("exterior2.png", face -> img = face);
		context2d.drawImage(image, 0, 0);
		
		image.addLoadHandler(new LoadHandler() {

	        @Override
	        public void onLoad(LoadEvent event) {
	            context2d.drawImage(img, 0, 0);
	        }
	    });
		
			
			
		imagesPanel.add(face);*/

		cuttingsPanel.setSpacing(55);
		cuttingsPanel.add(cuttingRoundButton);
		cuttingsPanel.add(cuttingStraightButton);
		cuttingsPanel.add(cuttingTriangularButton);
		cuttingsPanel.add(cuttingStandardButton);
		
		dimensionPanel.setSpacing(55);
		dimensionPanel.add(dimension1x2Button);
		dimensionPanel.add(dimension2x1Button);
		dimensionPanel.add(dimension5x5Button);
		dimensionPanel.add(dimension8x9Button);
		dimensionPanel.add(dimension10x10Button);
		dimensionPanel.add(dimension14x15Button);
		
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
		
		buttonPlayGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PlayGame playGame = new PlayGame(panels, managerService);
				panels.add(playGame);
				panels.showWidget(3);
			}
		});
	}
}
