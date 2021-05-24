package mpjp.client;

 import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;



public class InitialPanel extends Composite{
	
	final VerticalPanel loginPanel = new VerticalPanel();
	
	final Button loginButton = new Button("Login");
	
	final TextBox nameField = new TextBox();
	final PasswordTextBox passField = new PasswordTextBox();
	final Label errorLabel = new Label();
	final Label waitingLabel = new Label();
	public String user = "";
	public String pass = "";
	
	public InitialPanel(final DeckPanel panels, final GreetingServiceAsync managerService) {
		
		Label label1 = new Label("O Tiago é feio");
		
		Style style = label1.getElement().getStyle();
		style.setFontSize(55, Unit.PX);
		style.setColor("#362757");
		
		loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		errorLabel.addStyleName("gwt-error");
		
		loginPanel.add(label1);
		loginPanel.add(new HTML("username"));
		loginPanel.add(nameField);
		loginPanel.add(new HTML("password"));
		loginPanel.add(passField);
		loginPanel.add(loginButton);
		loginPanel.add(errorLabel);
		loginPanel.add(waitingLabel);

		initWidget(loginPanel);
		//loginButton.addClickHandler(loginClick(panels,managerService));
		
	}

	/*private ClickHandler loginClick(final DeckPanel panels,final GreetingServiceAsync managerService) {
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
					loginToServer(panels,managerService);    
			}
		};
		return handler;
	}
	
	public void loginToServer(final DeckPanel panels, final GreetingServiceAsync managerService){
		// First, we validate the input.
		final String userToServer = nameField.getValue();
		final String passToServer = passField.getValue();
		if (userToServer == null || passToServer == null || userToServer.length()<1 || passToServer.length()<1) {
			Window.alert("Please fill username and password");
			return;
		}
	}*/
	
}