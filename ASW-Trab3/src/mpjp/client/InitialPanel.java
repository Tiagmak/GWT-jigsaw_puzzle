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

	/**
	 * Paint initial Panel
	 * @param panels
	 * @param managerService
	 */
	InitialPanel(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);

		buttonJoinGame.addStyleName("designButtons");
		buttonCreateNewGame.addStyleName("designButtons");
		optionsPanel.setSpacing(30);
		optionsPanel.add(buttonJoinGame);
		optionsPanel.add(buttonCreateNewGame);

		centralPanel.addStyleName("centralInitialPanel");
		centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ask.addStyleName("ask");
		centralPanel.add(ask);
		centralPanel.add(optionsPanel);

		title.addStyleName("title");
		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		allPanels.add(title);
		allPanels.add(centralPanel);

		/**
		 * Activate when user click on buttonJoinGame
		 * @param event
		 */
		buttonJoinGame.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				JoinGame join = new JoinGame(panels, managerService);
				panels.add(join);
				panels.showWidget(1);
			}
		});

		/**
		 * Activate when user click on buttonCreateNewGame
		 * @param event
		 */
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