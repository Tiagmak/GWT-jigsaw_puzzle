package mpjp.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayGame  extends Composite{
	
	final VerticalPanel allPanels = new VerticalPanel();
	
	PlayGame(final DeckPanel panels, final PuzzleServiceAsync managerService) {
		initWidget(allPanels);
		//Label label = new Label(cutting);
		//allPanels.add(label);
		allPanels.add(new HTML("<p>Estou no game</p>"));
	}
}
