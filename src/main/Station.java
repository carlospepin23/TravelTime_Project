package main;

public class Station {
	//comentar cuando termine
	//private properties
	private String name;
	private Integer distance;
	
	public Station(String name, int dist) {
		setCityName(name);
		setDistance(dist);
	}
	
	public String getCityName() {
		return this.name;
	}
	public void setCityName(String cityName) {
		this.name=cityName;
	}
	public int getDistance() {
		return this.distance;
	}
	public void setDistance(int distance) {
		this.distance=distance;
	}

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
	@Override
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}

}
