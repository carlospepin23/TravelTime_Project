package main;

/**
 * Represents a train station, encapsulating its name and distance.
 * Note that the interpretation of the station name and distance may vary depending on the context.
 * 
 * @author Carlos J. Pepin Delgado
 * @version 4/19/2023
 *
 * 
 */

public class Station {
	/**
	 * Here are the private properties of the class, which is essential information
	 * that a Station must have.
	 */
	private String name;
	private Integer distance;
	
	/**
     * Constructs a Station object with the specified name and distance.
     * @param name The name of the train station.
     * @param dist The distance of the train station.
     */
	
	public Station(String name, int dist) {
		setCityName(name);
		setDistance(dist);
	}
	
	/**
	 * Retrieves the train station's name
	 * @return station's name
	 */
	
	public String getCityName() {
		return this.name;
	}
	
	/**
	 * Sets the train station name
	 * @param distance
	 */
	public void setCityName(String cityName) {
		this.name=cityName;
	}
	
	/**
	 * Retrieves the train station's distance
	 * @return stations's distance
	 */
	public int getDistance() {
		return this.distance;
	}
	
	/**
	 * Sets the train station distance
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.distance=distance;
	}

	/**
	 * This method compares two stations to evaluate if they are equals.
	 * @param obj The object to compare with this Station object.
	 * @return true if the specified object is equal to this Station object;
	 *         otherwise, false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		return this.getCityName().equals(other.getCityName()) && this.getDistance() == other.getDistance();
	}
	
	/**
     * Returns a string representation of the Station object.
     * @return A string representation including the name and distance of the train station.
     */
	@Override
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}

}
