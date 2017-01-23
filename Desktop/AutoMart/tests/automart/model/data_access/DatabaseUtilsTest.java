package automart.model.data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 * Class which tests the interface methods in the DatabaseUtils class
 */
public class DatabaseUtilsTest {
	
	/**
	 * Inserts basic data into the database
	 */
	@Before
	public void setup() {    
        Connection dbConnection = null;
        Statement sqlStatement = null;
        
        String deleteQuery = "DELETE FROM Vehicle";
        String insertQuery = "INSERT INTO Vehicle(make,model,price,fuel,colour,transmission) "
        						+ "VALUES(\"Porsche\",\"911\",55000.00,\"Petrol\",\"White\",\"Manual\"),"
        						+ "(\"VW\",\"Golf\",9000.00,\"Diesel\",\"Yellow\",\"Automatic\")";        
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AutoMart?user=root");
            sqlStatement = dbConnection.createStatement();
            sqlStatement.execute(deleteQuery);
            sqlStatement.execute(insertQuery);
        } catch (SQLException ex) {
            System.out.println("Error in insertion");
            System.out.println("SQLException: " + ex.getMessage());
        } finally {
            if (sqlStatement != null) {
                try {
                    sqlStatement.close();
                } catch (SQLException sqlEx) {
                    System.out.println("Error closing sqlStatement");
                }

                sqlStatement = null;
            }        
        }
	}
	
	@Test
	public void test_getAllVehicles_retrievesAllVehicles() {
		// When
		ArrayList<Vehicle> vehicles = DatabaseUtils.getAllVehicles();
		
		// Then
		Double price = 55000.00;
		
		assertEquals("Should only be 2 vehicles in the db", 2, vehicles.size());
		assertEquals("Should be a porsche", "Porsche", vehicles.get(0).getMake());
		assertEquals("Should be 55000.00", price, vehicles.get(0).getPrice());
	}

	@Test
	public void test_searchForVehicles_givenYellowColour_returns1Vehicle() {
		// Given
		HashMap<String,String> searchTerms = new HashMap<String,String>();
		searchTerms.put(DatabaseUtils.COLOUR, "Yellow");

		// When
		ArrayList<Vehicle> vehicles = DatabaseUtils.searchForVehicles(searchTerms);
		
		// Then	
		assertEquals("Should only be 1 vehicle retrieved", 1, vehicles.size());
		assertEquals("Should be a VW", "VW", vehicles.get(0).getMake());
	}

	@Test
	public void test_searchForVehicles_givenMinPrice5000MaxPrice15000_returns1Vehicle() {
		// Given
		HashMap<String,String> searchTerms = new HashMap<String,String>();
		searchTerms.put(DatabaseUtils.MINIMUM_PRICE, "5000.00");
		searchTerms.put(DatabaseUtils.MAXIMUM_PRICE, "15000.00");

		// When
		ArrayList<Vehicle> vehicles = DatabaseUtils.searchForVehicles(searchTerms);
		
		// Then	
		Double price = 9000.00;
		assertEquals("Should only be 1 vehicle retrieved", 1, vehicles.size());
		assertEquals("Should be a VW", "VW", vehicles.get(0).getMake());
		assertEquals("Price should be 9000.00", price, vehicles.get(0).getPrice());
	}

	@Test
	public void test_searchForVehicles_givenInvalidSearchTerm_returnsEmptyList() {
		// Given
		HashMap<String,String> searchTerms = new HashMap<String,String>();
		searchTerms.put("Fail", "5000.00");

		// When
		ArrayList<Vehicle> vehicles = DatabaseUtils.searchForVehicles(searchTerms);
		
		// Then	
		assertEquals("Should only be an empty arraylist", 0, vehicles.size());
	}

	@Test
	public void test_searchForVehicles_givenNullSearchTerm_returnsEmptyList() {
		// Given
		HashMap<String,String> searchTerms = null;

		// When
		ArrayList<Vehicle> vehicles = DatabaseUtils.searchForVehicles(searchTerms);
		
		// Then	
		assertEquals("Should only be an empty arraylist", 0, vehicles.size());
	}

	@Test
	public void test_getAllMakes_returnsAllMakes() {
		// When
		HashSet<String> makes = DatabaseUtils.getAllMakes();
		
		// Then
		assertEquals("Should only be 2 makes represented by 2 strings", 2, makes.size());
		assertEquals("Should contain Porsche", true, makes.contains("Porsche"));
		assertEquals("Should contain VW", true, makes.contains("VW"));
	}
	
	@Test
	public void test_getModels_givenPorsche_returns911() {
		// When
		HashSet<String> models = DatabaseUtils.getModels("Porsche");
		
		// Then
		assertEquals("Should only be 1 model by Porsche", 1, models.size());
		assertEquals("Should contain 911", true, models.contains("911"));
	}

	@Test
	public void test_getModels_givenNonExistentMake_returnsEmptyList() {
		// When
		HashSet<String> models = DatabaseUtils.getModels("Fail");
		
		// Then
		assertEquals("Should only be no models for the Fail make", 0, models.size());
	}

	@Test
	public void test_getModels_givenEmptyMake_returnsEmptyList() {
		// When
		HashSet<String> models = DatabaseUtils.getModels("");
		
		// Then
		assertEquals("Should only be no models for the Fail make", 0, models.size());
	}
}
