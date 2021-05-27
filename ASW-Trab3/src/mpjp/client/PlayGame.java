package mpjp.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayGame  extends Composite{
	
	final VerticalPanel allPanels = new VerticalPanel();
	
	PlayGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);
		
		allPanels.add(new HTML("<p>Estou no game</p>"));
	}
}
