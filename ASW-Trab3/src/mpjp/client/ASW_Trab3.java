package mpjp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootPanel;


public class ASW_Trab3 implements EntryPoint {
	

	private final GreetingServiceAsync managerService = GWT.create(GreetingService.class);
	
	public void onModuleLoad() {

		final DeckPanel panels = new DeckPanel();
		
		final InitialPanel login = new InitialPanel(panels,managerService);

		panels.add(login); 

		panels.showWidget(0);
		
		RootPanel.get("game").add(panels);
	}
}