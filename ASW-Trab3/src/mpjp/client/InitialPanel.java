package mpjp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

class InitialPanel extends Composite {

	final VerticalPanel allPanels = new VerticalPanel();
	final VerticalPanel centralPanel = new VerticalPanel();
	final HorizontalPanel optionsPanel = new HorizontalPanel();

	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	Label ask = new Label("Choose an option:");
	Button buttonJoinGame = new Button("Join a new game");
	Button buttonCreateNewGame = new Button("Create a new game");

	InitialPanel(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);

		//optionsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		optionsPanel.addStyleName("optionsPanel");
		//buttonJoinGame.addStyleName("button");
		//buttonCreateNewGame.addStyleName("button");
		optionsPanel.add(buttonJoinGame);
		optionsPanel.add(buttonCreateNewGame);

		centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		centralPanel.addStyleName("centralPanel");
		ask.addStyleName("ask");
		centralPanel.add(ask);
		centralPanel.add(optionsPanel);

		title.addStyleName("title");
		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		allPanels.add(title);
		allPanels.add(centralPanel);

		buttonJoinGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				JoinGame join = new JoinGame(panels, managerService);
				panels.add(join);
				panels.showWidget(1);
			}
		});

		buttonCreateNewGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CreateGame join = new CreateGame(panels, managerService);
				panels.add(join);
				panels.showWidget(1);
			}
		});
	}

}