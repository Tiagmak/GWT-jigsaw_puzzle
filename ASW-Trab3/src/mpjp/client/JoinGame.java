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

class JoinGame extends Composite {
	final VerticalPanel allPanels = new VerticalPanel();
	final VerticalPanel centralPanel = new VerticalPanel();
	final HorizontalPanel puzzlesPanel = new HorizontalPanel();

	Label title = new Label("Welcome to Multi-Player Jigsaw Puzzle");
	final Label ask = new Label("Please choose a puzzle.");
	final Button createNewGameButton = new Button("create a new game button");

	JoinGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);

		centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		centralPanel.addStyleName("centralPanel");
		ask.addStyleName("ask");
		centralPanel.add(ask);
		centralPanel.add(createNewGameButton);
		centralPanel.add(puzzlesPanel);

		allPanels.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		title.addStyleName("title");
		allPanels.add(title);
		allPanels.add(centralPanel);

		createNewGameButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panels.remove(1);
				panels.showWidget(0);
			}
		});
		
		
	}
}
