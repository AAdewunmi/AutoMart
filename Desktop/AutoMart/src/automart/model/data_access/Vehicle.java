package automart.model.data_access;


/**
 * Class which represents the notion of a vehicle
 */
public class Vehicle {
	private Integer vehicleId;
	private String make;
	private String model;
	private Double price;
	private String fuel;
	private String colour;
	private String transmission;
	
	/**
	 * Constructor which assigns basic data
	 */
	public Vehicle() {
		vehicleId = 0;
		make = "";
		model = "";
		fuel = "";
		colour = "";
		transmission = "";
		price = 0.00;
	}
	
	/**
	 * Returns the vehicleId
	 */
	public Integer getVehicleId() {
		return vehicleId;
	}

	/**
	 * Sets the vehicleId
	 */
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	/**
	 * Returns the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make
	 */
	public void setMake(String make) {
		this.make = make;
	}
	
	/**
	 * Returns the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Returns the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * Sets the price
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * Returns the fuel type
	 */
	public String getFuel() {
		return fuel;
	}

	/**
	 * Sets the fuel type
	 */
	public void setFuel(String fuel) {
		this.fuel = fuel;
	}
	
	/**
	 * Returns the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * Sets the colour
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	/**
	 * Returns the weight
	 */
	public String getTransmission() {
		return transmission;
	}

	/**
	 * Sets the weight
	 */
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
}

