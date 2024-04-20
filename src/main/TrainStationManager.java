package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.HashTableSC;

import data_structures.ArrayListStack;
import data_structures.SimpleHashFunction;

import data_structures.HashSet;


import interfaces.List;
import interfaces.Map;
import interfaces.Stack;
import interfaces.Set;

/**
 * The TrainStationManager class represents the manager responsible for handling 
 * operations related to train stations within a transportation system. 
 * <p>
 * The TrainStationManager class reads station data from a CSV file, constructs
 * station networks, finds the shortest routes between stations using Dijkstra's
 * algorithm, and calculates travel times between stations based on distance
 * and stops.
 * </p>
 * @author Carlos J. Pepin Delgado
 * @version 4/19/2023
 */
public class TrainStationManager {
	/**
	 * Here are the private properties of the class, which is essential information
	 * that a TrainStationManager must have.
	 */
	
	// Mapping of station names to lists of connected stations
	private Map<String,List<Station>> Stations = new HashTableSC<String, List<Station>>(1, new SimpleHashFunction<String>());
	// Mapping of station names to shortest routes
	private Map<String,Station> Shortest_Routes = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
	// Mapping of station names to lists of stops
	private Map<String,List<String>> stationStops = new HashTableSC<String, List<String>>(1, new SimpleHashFunction<String>());
	// Mapping of station names to travel times
	private Map<String,Double> Travel_Times = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
	
	/**
     * Default Constructor of a TrainStationManager object and populates Stations map from the default CSV file.
     */
	public TrainStationManager() {
		this("stations.csv");
	}
	
	/**
     * Parametered Constructor of a TrainStationManager object. it populates 
     * Stations map from the specified CSV file. At the end findShortestDistance() is called.
     * @param station_file The path to the CSV file containing station data.
     */
	public TrainStationManager(String station_file) {
		// INPUT DATA
				// utilize BufferedReader & FileReader to read the csv files containing the data
				BufferedReader station_Reader = null;
				try {
					station_Reader = new BufferedReader(new FileReader("inputFiles/" + station_file));
				} catch (FileNotFoundException e) {}

				String line;
				try {
					station_Reader.readLine(); //skips the first line
					while ((line = station_Reader.readLine()) != null) {
						
						String[] data = line.split(",");
						Station s=new Station(data[1], Integer.valueOf(data[2]));
						// Check if the station already exists in the map
						if(Stations.containsKey(data[0])) {
							Stations.get(data[0]).add(s);
							
						// If the station is not in the map, create a new list to hold connected stations
						}else {
							/*
							 * The reason for using ArrayList is that storing the connected 
							 * stations offers random access per position in constant time. As we often
							 * have to extract the data from the stations, without the need to delete elements,
							 *  the time complexity for its major use is O(1).
							 */
							List<Station> stations=new ArrayList<Station>();
							stations.add(s);
							Stations.put(data[0], stations);
						}
					}

				} catch (IOException e) {}
				
				// Here, we ensure that if a station wasn't initially added but is accessed by another station,
				// it gets added inside the station's entry in the map.
				int i=0;
				for(List<Station> src_cities:Stations.getValues()) {
					
					String src_cityName=Stations.getKeys().get(i);
					for(Station dest_city:src_cities) {
						String dest_cityName=dest_city.getCityName();
						
						//if its missing, add it
						Station missing=new Station(src_cityName,dest_city.getDistance());
						if(!Stations.containsKey(dest_cityName)) {
							
							List<Station> m_stations=new ArrayList<Station>();						
							m_stations.add(missing);
							Stations.put(dest_cityName, m_stations);
							
							
						}else if (!Stations.get(dest_cityName).contains(missing))
							//if the entry exists but it doesnt have the station
							Stations.get(dest_cityName).add(missing);		
					}
					
					i++; //changes src_cityName
				}
				//finds shortest distance from Westside to all other stations in the map
				findShortestDistance();
	}
	
	/**
	 * Finds the shortest distance from Westside to all other stations in the map using Dijkstra's algorithm.
	 * This algorithm explores the shortest path from the starting station (Westside) to all other
	 * stations in the Stations map. It maintains an ordered stack of stations to visit, initially starting with 
	 * Westside. At each step, it selects the station with the shortest known distance and explores its neighbors,
	 * updating their distances if a shorter path is found. The process continues until all stations have been visited. 
	 * It populates the Shortest_Routes map.
	 *
	 */
	private void findShortestDistance() {
		/*
		 * toVisit stack is used for the stations that need to be visited since only one element needs to be accessed during traversal.
		 * 
		 * The reason for using ArrayListStack is that it provides constant-time access (O(1)) to the top element of the stack,
		 * which is essential for the efficient implementation of Dijkstra's algorithm. In addition, ArrayListStack typically
		 * occupies less memory than LinkedStack or LinkedListStack implementations, making it the preferred choice for this context.
		 */
		
		Stack<Station> toVisit = new ArrayListStack<Station>();
		
		//Visited HashSet is used to keep track of the stations that were already visited.
		//This is optimal since Sets do not allows object duplications.
		Set<Station> Visited= new HashSet<Station>();										
		
		//setting all src_cities initial position in Westside
		for(String src_city: Stations.getKeys()) {
			Shortest_Routes.put(src_city, new Station("Westside",Integer.MAX_VALUE)); 
		}
			
		//start
		toVisit.push(new Station("Westside",0));
		Shortest_Routes.put("Westside", new Station("Westside",0));
		
		while(!toVisit.isEmpty()) {
			Station currStation = toVisit.pop();
			Visited.add(currStation);
			
			//neighbors
			List<Station> neighbors=Stations.get(currStation.getCityName());
			
			for(int j=0;j<neighbors.size();j++) {
				Station n=neighbors.get(j);
				Station c=Shortest_Routes.get(currStation.getCityName());
				Integer prev_dist=Shortest_Routes.get(n.getCityName()).getDistance();
				Integer calc_dist=n.getDistance()+c.getDistance();
				
				//modifies distance
				if(prev_dist>calc_dist) {
					Station mod_city=new Station(currStation.getCityName(),calc_dist);
					Shortest_Routes.put(n.getCityName(), mod_city);
				}

				//adds to toVisit stack
				if(!Visited.isMember(n)) {   
					sortStack(n, toVisit);
					
				}
			}
		}
	}
	
	/**
	 * Receives a Stack that needs to remain sorted and the station we want to add. The stack is sorted 
	 * by distance. The smaller the distance, the closer to the top the value is. The top of the stack is the station 
	 * with the shortest distance.
	 * 
	 * @param station     The station to be inserted into the sorted stack.
	 * @param stackToSort The stack of stations to be sorted.
	 */
	public void sortStack(Station station, Stack<Station> stackToSort) {
	    Stack<Station> tempStack = new ArrayListStack<Station>();										

	    //push the station to be inserted into the stack
	    stackToSort.push(station);

	    //sorting process
	    while (!stackToSort.isEmpty()) {
	        Station temp = stackToSort.pop();
	        while (!tempStack.isEmpty() && temp.getDistance() < tempStack.top().getDistance()) {
	            stackToSort.push(tempStack.pop());
	        }
	        tempStack.push(temp);
	    }

	    //fills the original stack with the sorted elements
	    while (!tempStack.isEmpty()) {
	        stackToSort.push(tempStack.pop());
	    }
	}
	
	/**
	 * Calculates and returns the travel times from Westside to all other stations in the Stations map.
	 * The method iterates through each station, setting up station stops and then calculates the travel
	 * times based on the shortest routes. The travel time for each destination station is calculated as 
	 * the sum of the station's distance from Westside multiplied by 2.5 (to simulate travel time in minutes) 
	 * and 15 minutes for each intermediate stop.
	 *
	 * @return A map containing the travel times (in minutes) from Westside to each destination station.
	 */
	public Map<String, Double> getTravelTimes() {
		
		for(String src_city:Stations.getKeys()) {
			stationStops.put(src_city, new ArrayList<String>());					
			stationStops.get(src_city).add("Westside");
		}

		List<String> dest_cities=Shortest_Routes.getKeys();
		List<Station> src_cities=Shortest_Routes.getValues();
			
		for(int i=0;i<getStations().size();i++) {
			String d=dest_cities.get(i);       
			Station s = src_cities.get(i);		
			String curr=d;
			
			Integer station_stops=-1; //starts at Westside
			while(curr!="Westside") {
				stationStops.get(d).add(1, curr);
				curr=Shortest_Routes.get(curr).getCityName();
				station_stops++;
			}
			if(d=="Westside") {
				station_stops=0;
			}
			Travel_Times.put(d, (s.getDistance()*2.5)+(15*station_stops));
		}

		return Travel_Times;
		
	}
	
	/**
	 * Retrieves the Stations map.
	 * 
	 * @return A map containing the stations in Stations map, where each key represents a city name
	 *         and the corresponding value is a list of stations located in that city.
	 */
	public Map<String, List<Station>> getStations() {
		return this.Stations;
	}

	/**
	 * Retrieves the map of shortest routes to each station from the starting station (Westside).
	 * 
	 * @return A map containing the shortest routes to each station from the starting station,
	 *         where each key represents the destination station and the corresponding value
	 *         is the nearest station on the shortest route.
	 */
	public Map<String, Station> getShortestRoutes() {
		return this.Shortest_Routes;
	}
	
	/**
	 * Retrieves the map of stations and their corresponding stops along the travel route.
	 * 
	 * @return A map containing the stations as keys and their respective stops along the travel route as values.
	 *         Each station is associated with a list of stops, where the first stop is always "Westside" (the starting point).
	 */
	public Map<String, List<String>> getStationStops() {
		return this.stationStops;
	}
	
	/**
	 * Sets the map of stations and their corresponding lists of connected stations.
	 * 
	 * @param cities A map containing the stations as keys and their respective lists of connected stations as values.
	 */
	public void setStations(Map<String, List<Station>> cities) {
		this.Stations=cities;
	}

	/**
	 * Sets the map of shortest routes from the starting station to all other stations.
	 * 
	 * @param shortestRoutes A map containing the names of stations as keys and their corresponding shortest route stations as values.
	 */
	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		this.Shortest_Routes=shortestRoutes;
	}
	
	/**
	 * BONUS EXERCISE
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return (String) String representation of the path taken to reach stationName.
	 */
	public String traceRoute(String stationName) {
		getTravelTimes();
		List<String> src_cities=stationStops.get(stationName);
		
		String traceRoute="";
		
		for(int i=0;i<src_cities.size();i++) {
			traceRoute+=src_cities.get(i);
			if(i+1!=src_cities.size()) {
				traceRoute+="->";
			}
		}
		return traceRoute;
	}
	
	/**
	 * Main method that starts the project by creating an TrainStationManager() Object.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TrainStationManager tsm = new TrainStationManager();

		Map<String,List<Station>> stns= tsm.getStations();
		List<String> ki =stns.getKeys();
		List<List<Station>> vi=stns.getValues();
		for(int i=0;i<stns.size();i++) {
			System.out.println(ki.get(i));
			System.out.println(vi.get(i));
		}
		System.out.println();
		System.out.println();
		
		Map<String, Station> shrt = tsm.getShortestRoutes();
		List<String> k =shrt.getKeys();
		List<Station> v=shrt.getValues();
		for(int i=0;i<shrt.size();i++) {
			System.out.println(k.get(i));
			System.out.println(v.get(i));
		}
		
		System.out.println();
		System.out.println();

		for(int i=0;i<ki.size();i++) {
			System.out.println(tsm.traceRoute(ki.get(i)));
		}
		
		new TrainStationGUI();
	}

}