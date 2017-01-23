package automart;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class AutoMart extends Application {

	 private Stage primaryStage;
	 private BorderPane autoMartWindowLayout;
	 private ObservableList<String> makeList = FXCollections.observableArrayList();
	 
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AutoMart UK - Find Used and Approved Cars");

        initAutoMartWindowLayout();
        showAutoMartGui();
	}
	
	/**
     * Initializes the window layout.
     */
    public void initAutoMartWindowLayout() {
        try {
        	
            // Load window layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AutoMart.class.getResource("view/AutoMartLayout.fxml"));
            autoMartWindowLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(autoMartWindowLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the gui inside the root layout.
     */
    public void showAutoMartGui() {
        try {
            // Load automart gui
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AutoMart.class.getResource("view/AutoMartGui.fxml"));
            AnchorPane autoMartGui = (AnchorPane) loader.load();

            // Set automartgui into the center of root layout.
            autoMartWindowLayout.setCenter(autoMartGui);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}