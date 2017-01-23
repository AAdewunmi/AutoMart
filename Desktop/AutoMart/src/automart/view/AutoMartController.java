package automart.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

//import com.sun.java.swing.plaf.motif.MotifComboBoxUI.ComboBoxLayoutManager;

import automart.model.data_access.DatabaseUtils;
import automart.model.data_access.Vehicle;
import automart.model.chat.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class AutoMartController implements Initializable{

	@FXML
	private ComboBox<Object> makeComboBox;
	
	@FXML
	private ComboBox<Object> modelComboBox;
	
	@FXML
	private ComboBox<Object> colourComboBox;
	
	@FXML
	private ComboBox<Object> fuelComboBox;
	
	@FXML
	private ComboBox<Object> yearComboBox;
	
	@FXML
	private ComboBox<Object> gearboxComboBox;
	
	@FXML
	private ComboBox<Object> minPriceComboBox;
	
	@FXML
	private ComboBox<Object> maxPriceComboBox;
	
	@FXML
	private ListView<Object> resultListView;
	
	@FXML
	private Label notiicationLabel = new Label();
	
	@FXML
	private Button searchButton;
	
	@FXML
	private Button resetButton;
	
	@FXML
	private Button chatButton;
	
	private ChatClient chatClient;
	private Server chatServser;
	
	 
    private ObservableList<Object> searchQuery = FXCollections.observableArrayList();
    private ObservableList<Object> colourdata = FXCollections.observableArrayList(" ","Beige", "Black", "Brown", "Blue", "Green", "Grey", "Red","White","Yellow");
    private ObservableList<Object> fueldata = FXCollections.observableArrayList(" ","Petrol","Diesel", "Biofuel");
    private ObservableList<Object> yeardata = FXCollections.observableArrayList(" ", "2005", "2006","2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015");
    private ObservableList<Object> gearboxdata = FXCollections.observableArrayList("Automatic","Manual");
    private ObservableList<Object> maxPricedata = FXCollections.observableArrayList("5000", "10000", "15000", "20000", "25000", "30000", "35000","40000", "60000");
    private ObservableList<Object> minPricedata = FXCollections.observableArrayList("1000", "2000", "3000", "4000", "5000", "10000", "15000","20000");

	private ObservableList<Object> resultData = FXCollections.observableArrayList();

    private ArrayList<Vehicle> vehicles; 
    

	public AutoMartController() {
		// TODO Auto-generated constructor stub		
		vehicles = DatabaseUtils.getAllVehicles();

	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		colourComboBox.getItems().addAll(colourdata);
		fuelComboBox.getItems().addAll(fueldata);
		yearComboBox.getItems().addAll(yeardata);
		gearboxComboBox.getItems().addAll(gearboxdata);
		minPriceComboBox.getItems().addAll(minPricedata);
		maxPriceComboBox.getItems().addAll(maxPricedata);
		setMakeList();
	}
	@FXML
	private void searchAction(ActionEvent e){
			
		 resultListView.setItems(setResultListView());

	}

	@FXML
	private void resetAction(){		
		ObservableList<Object> resultData = FXCollections.observableArrayList();
		ArrayList<Vehicle> allVehicles;
		
		try {
			makeComboBox.setValue(null);
			makeComboBox.setValue(null);
			modelComboBox.setValue(null);
			colourComboBox.setValue(null);
			fuelComboBox.setValue(null);
			yearComboBox.setValue(null);
			gearboxComboBox.setValue(null);
			
			resultListView.setItems(getAllVehicles());
			
		} catch (Exception e2) {
			// TODO: handle exception
		}		
		
	}
	
	@FXML
	private void chatAction(){	
		try {
			//chatServser = new Server(5000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		chatClient = new ChatClient("AutoMart Client");
		
	}
	@FXML
	public void exitProgram(){
		Platform.exit();
	}
	
	@FXML
	public void setMakeList(){ 
		HashSet<String> allMake = DatabaseUtils.getAllMakes();
		
		for(String make : allMake)
			makeComboBox.getItems().add(make);
		
		makeComboBox.setOnAction(e -> setModelList((String) makeComboBox.getValue()));  
    }
    
    public void setModelList(String selectedMake){  
    	
    	HashSet<String> allModels = DatabaseUtils.getModels(selectedMake);
		
		for(String model : allModels)
			modelComboBox.getItems().add(model);
		
    }
      

	public ObservableList<Object> setResultListView(){
		ObservableList<Object> resultData = FXCollections.observableArrayList();
		HashMap<String,String> searchTerms = new HashMap<String,String>();
		ArrayList<Vehicle> selectedVehicles;
		
		
		//searchTerms.put(DatabaseUtils.MAKE, (String)makeComboBox.getValue());
		//searchTerms.put(DatabaseUtils.MODEL, (String)modelComboBox.getValue());
		//String test = (String)minPriceComboBox.getValue();
		if ((String)minPriceComboBox.getValue() != null)
			searchTerms.put(DatabaseUtils.MINIMUM_PRICE, (String)minPriceComboBox.getValue());
		if((String)maxPriceComboBox.getValue() !=null)
			searchTerms.put(DatabaseUtils.MAXIMUM_PRICE, (String)maxPriceComboBox.getValue());
		if((String)colourComboBox.getValue() != null)
			searchTerms.put(DatabaseUtils.COLOUR, (String)colourComboBox.getValue());
		if( (String)fuelComboBox.getValue() != null)
			searchTerms.put(DatabaseUtils.FUEL, (String)fuelComboBox.getValue());
		if((String)yearComboBox.getValue() != null)
			searchTerms.put(DatabaseUtils.YEAR, (String)yearComboBox.getValue());
		if((String)gearboxComboBox.getValue() != null)
			searchTerms.put(DatabaseUtils.TRANSMISSION, (String)gearboxComboBox.getValue());

		selectedVehicles = DatabaseUtils.searchForVehicles(searchTerms);
		
		for(Vehicle vehicle : selectedVehicles){
			
			resultData.add("Your selected Vehicle is:");
			resultData.add("	Make --->" + vehicle.getMake());
			resultData.add("	Model--->" +vehicle.getModel());
			resultData.add("	Price --->" +vehicle.getPrice());
			resultData.add("	Fuel --->" +vehicle.getFuel());
			resultData.add("	Colour --->" +vehicle.getColour());
			resultData.add("	Gearbox --->" +vehicle.getTransmission());

		}
		
		return resultData;
		
	}
	
	
	public ObservableList<Object> getAllVehicles(){
		
		ObservableList<Object> resultData = FXCollections.observableArrayList();
		for(Vehicle vehicle : vehicles){
			resultData.add("Vehicle No " + vehicle.getVehicleId());
			resultData.add("	Make		:	" + vehicle.getMake());
			resultData.add("	Model		:	" + vehicle.getModel());
			resultData.add("	colour		:	" + vehicle.getColour());
			resultData.add("	Fuel		:	" + vehicle.getFuel());
			/*resultData.add("	Gearbox		:	" + vehicle.getGearbox());
			resultData.add("	Year		:	" + vehicle.getYear());
			resultData.add("	Registration:	" + vehicle.getRegistration());
			resultData.add("	Weight		:	"+ vehicle.getWeight());*/
		}
		
		return resultData;
	}
	
	public void resetSearch(){
		
	}
	
}
