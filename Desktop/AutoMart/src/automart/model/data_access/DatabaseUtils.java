package automart.model.data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * This class provides utility methods for interacting with the database
 */
public class DatabaseUtils {

	public static final String MAKE = "make";
	public static final String MODEL = "model";
	public static final String MINIMUM_PRICE = "Minimum Price";
	public static final String MAXIMUM_PRICE = "Maximum Price";
	public static final String PRICE = "price";
	public static final String FUEL = "fuel";
	public static final String COLOUR = "colour";
	public static final String YEAR = "year";
	public static final String TRANSMISSION = "transmission";
	
	/**
	 * Private constructor to prevent instantiation of utility class
	 */
	private DatabaseUtils() {}	
	
	/**
	 * Retrieves all vehicles from the database
	 * 
	 * @return 		ArrayList containing all vehicles
	 */
	public static ArrayList<Vehicle> getAllVehicles() {
		String query = "SELECT * FROM Vehicle";
		return retrieveVehiclesUsingQuery(query);
	}
	
	/**
	 * Searches the database for vehicles which have the given attributes
	 * and returns them as Vehicle objects
	 * 
	 * @precondition 		search terms should be for attributes which exist, if any attributes do not exist,
	 * 						the search terms is null or empty an empty map will be returned
	 * @param searchTerms	A map containing the search term with the value to be searched for
	 * @return				A list of Vehicle objects based on the search terms
	 */
	public static ArrayList<Vehicle> searchForVehicles(HashMap<String,String> searchTerms) {	
		if (searchTerms == null || searchTerms.isEmpty() 
				|| !areSearchCriteriaValid(searchTerms.keySet())) {
			return new ArrayList<Vehicle>();
		}
		
		String query = convertToSqlQuery(searchTerms);
		return retrieveVehiclesUsingQuery(query);
	}

	/**
	 * Retrieves the makes of all vehicles stored in the database
	 * 
	 * @return	Returns all the makes of vehicles stored in the database
	 */
	public static HashSet<String> getAllMakes() {
		String query = "SELECT DISTINCT(make) FROM Vehicle";
		return retrieveStringAttributeUsingQuery(query);
	}
	
	/**
	 * Retrieves all models for the specified make that are stored in the database
	 * 
	 * @param make		The make to retrieve all models for
	 * @return			The models associated to the given make
	 * 					An empty list is returned if the make has no associated models
	 */
	public static HashSet<String> getModels(String make) {
		String query = "SELECT DISTINCT(model) FROM Vehicle WHERE make=\"" + make +"\"";
		return retrieveStringAttributeUsingQuery(query);
	}
	
	/**
	 * Retrieves a single attribute for each record in the database and converts each one into a string
	 * which is returned in a list of strings
	 * 
	 * @precondition		The attribute to be retrieved from the database should be a string
	 * @param query			The query to be executed on the database
	 * @return				A set of strings which represent the attribute for each record
	 */
	private static HashSet<String> retrieveStringAttributeUsingQuery(String query) {
		HashSet<String> attributes = new HashSet<String>();
		
		Connection dbConnection = openDbConnection();

        if (dbConnection != null) {
            // Execute the query on this object
            Statement sqlStatement = null;
            
            // Results stored in this object
            ResultSet resultSet = null;
            
            try {
                sqlStatement = dbConnection.createStatement();
                resultSet = sqlStatement.executeQuery(query);
                attributes = convertResultSetToStrings(resultSet);
            } catch (SQLException ex) {
                System.out.println("Error executing SQL");
                System.out.println("SQLException: " + ex.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                    	resultSet.close();
                    } catch (SQLException sqlEx) {
                        System.out.println("Error closing result");
                    }

                    resultSet = null;
                }

                if (sqlStatement != null) {
                    try {
                        sqlStatement.close();
                    } catch (SQLException sqlEx) {
                        System.out.println("Error closing sqlStatement");
                    }

                    sqlStatement = null;
                }        
            }
            
            closeDbConnection(dbConnection);
        }
        
		return attributes;
	}

	/**
	 * Retrieves data from the database and converts them to a list of Vehicle objects
	 * based on the given query
	 * 
	 * @param query		The query to execute on the database
	 * @return			A list of Vehicle objects based on the results from the database
	 */
	private static ArrayList<Vehicle> retrieveVehiclesUsingQuery(String query) {
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		Connection dbConnection = openDbConnection();

        if (dbConnection != null) {
            // Execute the query on this object
            Statement sqlStatement = null;
            
            // Results stored in this object
            ResultSet resultSet = null;
            
            try {
                sqlStatement = dbConnection.createStatement();
                resultSet = sqlStatement.executeQuery(query);
                vehicles = convertResultSetToVehicles(resultSet);
            } catch (SQLException ex) {
                System.out.println("Error executing SQL");
                System.out.println("SQLException: " + ex.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                    	resultSet.close();
                    } catch (SQLException sqlEx) {
                        System.out.println("Error closing result");
                    }

                    resultSet = null;
                }

                if (sqlStatement != null) {
                    try {
                        sqlStatement.close();
                    } catch (SQLException sqlEx) {
                        System.out.println("Error closing sqlStatement");
                    }

                    sqlStatement = null;
                }        
            }
            
            closeDbConnection(dbConnection);
        }
        
		return vehicles;
	}

	/**
	 * Validates search criteria to ensure they are valid for the database
	 * 
	 * @param searchCriteria	The attributes on which the database will be searched
	 * @return	Boolean			Returns false if the searchCriteria contains invalid
	 * 							search terms, otherwise returns true.
	 * 							Returns true if no search criteria are specified
	 */
	private static Boolean areSearchCriteriaValid(Set<String> searchCriteria) {
		for (String searchTerm : searchCriteria) {
			if (searchTerm != MAKE && searchTerm != MODEL
						&& searchTerm != MINIMUM_PRICE && searchTerm != MAXIMUM_PRICE
						&& searchTerm != PRICE && searchTerm != FUEL
						&& searchTerm != COLOUR && searchTerm != YEAR
						&& searchTerm != TRANSMISSION) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Converts the given criteria into a SQL query which can be executed on the database
	 * 
	 * @precondition 					searchTerms should not be an empty map or null, 
	 * 									all vehicles will be returned in this case
	 * @param searchTerms				A map containing the search terms as keys with the values to be searched for
	 * @return							A string containing the SQL query
	 * @throws	NumberFormatException	Throws this if one of the number attributes in the given search terms
	 * 									fails to be parsed to a number
	 */
	private static String convertToSqlQuery(HashMap<String,String> searchTerms) throws NumberFormatException {
		String query = "SELECT * FROM Vehicle";
		
		if (searchTerms == null || searchTerms.isEmpty())
			return query;
		
		query += " WHERE ";
		Set<String> searchTermKeys = searchTerms.keySet();
		Iterator<String> iterator = searchTermKeys.iterator();
		String searchTermKey = "";
		while (iterator.hasNext()) {
			searchTermKey = iterator.next();

			if (searchTermKey == MINIMUM_PRICE) {
				query += PRICE
						+ ">="
						+ Double.parseDouble(searchTerms.get(searchTermKey));
			} else if (searchTermKey == MAXIMUM_PRICE) {
				query += PRICE 
						+ "<="
						+ Double.parseDouble(searchTerms.get(searchTermKey));
			} else {
				query += searchTermKey
						+ "=\"" 
						+ searchTerms.get(searchTermKey)
						+ "\"";
			}
			
			if (iterator.hasNext())
				query += " AND ";
		}
		
		return query;
	}
	
	/**
	 * Opens a connection to the database
	 * 
	 * @return	The connection to the database
	 */
    private static Connection openDbConnection() {
        Connection dbConnection = null;
        
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/AutoMart?user=root");
        } catch (SQLException ex) {
            System.out.println("Error opening connection");
            System.out.println("SQLException: " + ex.getMessage());
        }
        
        return dbConnection;
    }
    
    /**
     * Closes the connection to the database
     * 
     * @param dbConnection		The database connection to be closed
     */
    private static void closeDbConnection(Connection dbConnection) {
    	Connection connection = dbConnection;
    	try {
    		connection.close();
    	} catch (SQLException ex) {
            System.out.println("Error closing connection");
            System.out.println("SQLException: " + ex.getMessage());
        }  
	}
    
    /**
     * Converts a single attribute resultset to a list of strings
     * 
     * @precondition		The resultset must contain a single column whose type is a string
     * @param resultSet		The resultset to extract the strings from
     * @return				An ArrayList of strings representing the required attribute
     */
    private static HashSet<String> convertResultSetToStrings(ResultSet resultSet) {
    	HashSet<String> attributeList = new HashSet<String>();
    	
    	try {
	        while (resultSet.next()) {
	        	attributeList.add(resultSet.getString(1));
	        }
    	} catch (SQLException ex) {
            System.out.println("Error converting vehicles");
            System.out.println("SQLException: " + ex.getMessage());
		}
    	
        return attributeList;
    }
    
    /**
     * Converts the given ResultSet into a list of Vehicles and returns them
     * 
     * @param resultSet		The ResultSet to be converted
     * @return				A list of Vehicle's which have been created from the ResultSet
     */
    private static ArrayList<Vehicle> convertResultSetToVehicles(ResultSet resultSet) {
    	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    	
    	try {
	        while (resultSet.next()) {
	        	Integer vehicleId = resultSet.getInt(1);
	        	String make = resultSet.getString(2);
	        	String model = resultSet.getString(3);
	        	Double price = resultSet.getDouble(4);
	        	String fuel = resultSet.getString(5);
	        	String colour = resultSet.getString(6);
	        	String transmission = resultSet.getString(7);
	        	
	        	Vehicle vehicle = new Vehicle();
	        	vehicle.setVehicleId(vehicleId);
	        	vehicle.setMake(make);
	        	vehicle.setModel(model);
	        	vehicle.setPrice(price);
	        	vehicle.setFuel(fuel);
	        	vehicle.setColour(colour);
	        	vehicle.setTransmission(transmission);       	
	        	
	        	vehicles.add(vehicle);
	        }
    	} catch (SQLException ex) {
            System.out.println("Error converting vehicles");
            System.out.println("SQLException: " + ex.getMessage());
		}
    	
        return vehicles;
    }
}

