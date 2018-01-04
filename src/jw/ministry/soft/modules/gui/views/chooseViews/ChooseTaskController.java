package jw.ministry.soft.modules.gui.views.chooseViews;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.gui.views.congregation.CongregationsListController;

public class ChooseTaskController {
	
	@FXML	
	AnchorPane layout;
	private Main mainApp;
	Stage stage;
	private CheckBox congreID;
	private CheckBox territoryID;
	private CheckBox emtID;
	private Button goID;
	
	
	public ChooseTaskController()
	{
		
	}
	
	// Appliying Zoom Effect
	public void  handleMouseOnCongregation()
	{
		congreID.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent me) {
						// TODO Auto-generated method stub
						congreID.setScaleX(2);
						congreID.setScaleY(2);
					}});
	}
	
	// Appliying Zoom Effect
	public void  handleMouseOnTerritory()
	{
		territoryID.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent me) {
						// TODO Auto-generated method stub
						territoryID.setScaleX(2);
						territoryID.setScaleY(2);
					}});
	}
	
	// Appliying Zoom Effect
	public void  handleMouseOnEMT()
	{
		emtID.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent me) {
						// TODO Auto-generated method stub
						emtID.setScaleX(2);
						emtID.setScaleY(2);
					}});
	}
	
	// Appliying Zoom Effect
	public void handleMouseOnGo()
	{
		goID.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent me) {
						// TODO Auto-generated method stub
						goID.setScaleX(2);
						goID.setScaleY(2);		
					}});
	}
	
	// Launch the selected Task 
	public void handleActions()
	{
		if(congreID.isSelected() == true)
		{
			try 
			{
				FXMLLoader loader =new FXMLLoader();
				loader.setLocation(ChooseTaskController.class.getResource("congregation/CongregationsListController"));
				this.layout = (AnchorPane)loader.load();

				// Show the scene containing the root layout.
				Scene scene = new Scene(layout);
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Congregation list");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(stage);
				//dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));
				dialogStage.setScene(scene);
				
				// Give the controller access to the main app.
				CongregationsListController controller = loader.getController();
				controller.setMainApp(mainApp);
				dialogStage.show();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(territoryID.isSelected()==true)
		{
			try 
			{
				FXMLLoader loader =new FXMLLoader();
				loader.setLocation(ChooseTaskController.class.getResource("territory/TerritoriesController"));
				this.layout = (AnchorPane)loader.load();

				// Show the scene containing the root layout.
				Scene scene = new Scene(layout);
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Territories Management");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(stage);
				//dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));
				dialogStage.setScene(scene);
				
				// Give the controller access to the main app.
				CongregationsListController controller = loader.getController();
				controller.setMainApp(mainApp);
				dialogStage.show();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(emtID.isSelected()==true)
		{
			try 
			{
				FXMLLoader loader =new FXMLLoader();
				loader.setLocation(ChooseTaskController.class.getResource("emt/emtController"));
				this.layout = (AnchorPane)loader.load();

				// Show the scene containing the root layout.
				Scene scene = new Scene(layout);
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Theocratic Ministry School");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(stage);
				//dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));
				dialogStage.setScene(scene);
				
				// Give the controller access to the main app.
				CongregationsListController controller = loader.getController();
				controller.setMainApp(mainApp);
				dialogStage.show();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	// Displaying the management of the thecratic  ministry school  
	public void  handleEMT()
	{
	
		this.handleActions();
	}
	
	// Displaying the management of the congregation
	public void handleCongregation()
	{
	
		this.handleActions();
	}
	
	// Displaying the management of the territories
	public void handleTerritory()
	{
		this.handleActions();
	}

	
	
	// Setters and Getters
	public AnchorPane getLayout() {
		return layout;
	}

	public Main getMainApp() {
		return mainApp;
	}

	public Stage getStage() {
		return stage;
	}

	public CheckBox getCongreID() {
		return congreID;
	}

	public CheckBox getTerritoryID() {
		return territoryID;
	}

	public CheckBox getEmtID() {
		return emtID;
	}

	public Button getGoID() {
		return goID;
	}

	public void setLayout(AnchorPane layout) {
		this.layout = layout;
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCongreID(CheckBox congreID) {
		this.congreID = congreID;
	}

	public void setTerritoryID(CheckBox territoryID) {
		this.territoryID = territoryID;
	}

	public void setEmtID(CheckBox emtID) {
		this.emtID = emtID;
	}

	public void setGoID(Button goID) {
		this.goID = goID;
	}
	
	
	
}
