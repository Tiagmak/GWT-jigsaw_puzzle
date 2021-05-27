package mpjp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ASW_Trab3 implements EntryPoint {

	private final PuzzleServiceAsync managerService = GWT.create(PuzzleService.class);

	public void onModuleLoad() {

		final DeckPanel panels = new DeckPanel();

		final InitialPanel initialPanel = new InitialPanel(panels, managerService);

		panels.add(initialPanel);

		// Para fazer um pedido ao servidor
		/*
		 * managerService.getAvailableCuttings(new AsyncCallback<Set<String>>() {
		 * 
		 * @Override public void onSuccess(Set<String> result) { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public void onFailure(Throwable caught) { // TODO Auto-generated
		 * method stub
		 * 
		 * } });
		 */

		panels.showWidget(0);

		RootPanel.get("game").add(panels);
	}
}