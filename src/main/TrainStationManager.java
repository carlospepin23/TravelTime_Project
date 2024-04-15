package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.HashTableSC;
//import data_structures.LinkedListStack;
//import data_structures.LinkedStack;
import data_structures.ArrayListStack;
import data_structures.SimpleHashFunction;

import data_structures.HashSet;


import interfaces.List;
import interfaces.Map;
import interfaces.Stack;
import interfaces.Set;

public class TrainStationManager {
	private Map<String,List<Station>> Stations = new HashTableSC<String, List<Station>>(1, new SimpleHashFunction<String>());
	private Map<String,Station> Shortest_Routes = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
	private Map<String,List<String>> stationStops = new HashTableSC<String, List<String>>(1, new SimpleHashFunction<String>());
	
	public TrainStationManager() {
		this("stations.csv"); //se cambio para test
	}	
	public TrainStationManager(String station_file) {
		// INPUT DATA

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
						
						if(Stations.containsKey(data[0])) {
							Stations.get(data[0]).add(s);
							
						}else {
							List<Station> stations=new ArrayList<Station>();
							stations.add(s);
							Stations.put(data[0], stations);
						}
					}

				} catch (IOException e) {}
				
				//se anaden los que faltan
				int i=0;
				for(List<Station> src_cities:Stations.getValues()) {
					
					String src_cityName=Stations.getKeys().get(i);
					for(Station dest_city:src_cities) {
						String dest_cityName=dest_city.getCityName();
						
						//si el nombre no esta
						Station missing=new Station(src_cityName,dest_city.getDistance());
						if(!Stations.containsKey(dest_cityName)) {
							
							List<Station> blank=new ArrayList<Station>();
							blank.add(missing);
							Stations.put(dest_cityName, blank);
							
							
						}else if (!Stations.get(dest_cityName).contains(missing))
							Stations.get(dest_cityName).add(missing);  //si el nombre esta, y le falta la estacion
								
					}
					
					i++; //changes src_cityName
				}
				
				for(String src_city:Stations.getKeys()) {
					stationStops.put(src_city, new ArrayList<String>());
					stationStops.get(src_city).add("Westside");
				}
				
				findShortestDistance();
	}
	
	private void findShortestDistance() {
		Stack<Station> toVisit = new ArrayListStack<Station>();
		Set<Station> Visited= new HashSet<Station>();
		
		
		for(String src_city: Stations.getKeys()) {
			Shortest_Routes.put(src_city, new Station("Westside",Integer.MAX_VALUE)); //setting all src_cities initial position in Westside
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
				
				
				//se modifica la distancia
				if(prev_dist>calc_dist) {
					Station mod_city=new Station(currStation.getCityName(),calc_dist);
					Shortest_Routes.put(n.getCityName(), mod_city);
				}

								
				//se anade al toVisit
				if(!Visited.isMember(n)) {   
					sortStack(n, toVisit);
					
				}
			}
		}
	}

	public void sortStack(Station station, Stack<Station> stackToSort) {
		
		Stack<Station> tempStack=new ArrayListStack<Station>();
	
		stackToSort.push(station);
	
		while(!stackToSort.isEmpty()) {
			Station temp=stackToSort.pop();
			while(!tempStack.isEmpty() && temp.getDistance()<tempStack.top().getDistance()) {
				stackToSort.push(tempStack.pop());
			}
			tempStack.push(temp);
		}
	
		while(!tempStack.isEmpty()) {
			stackToSort.push(tempStack.pop());
		}
	}
	
	
	public Map<String, Double> getTravelTimes() {
		Map<String,Double> Travel_Times = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());

		List<String> dest_cities=Shortest_Routes.getKeys();
		List<Station> src_cities=Shortest_Routes.getValues();
			
		for(int i=0;i<dest_cities.size();i++) {
			String d=dest_cities.get(i);        //left
			Station s = src_cities.get(i);		//right
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
	
	
//
//
	public Map<String, List<Station>> getStations() {
		return this.Stations;
	}


//	public void setStations(Map<String, List<Station>> cities) {
//		this.Stations=cities;
//	}


	public Map<String, Station> getShortestRoutes() {
		return this.Shortest_Routes;
	}
	
//	//added
//	public Map<String, List<String>> getStationStops() {
//		return this.stationStops;
//	}


//	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
//		this.Shortest_Routes=shortestRoutes;
//	}
	
	/**
	 * BONUS EXERCISE THIS IS OPTIONAL
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
		
//		System.out.println(traceRoute);
		return traceRoute;
	}
	
	public static void main(String[] args) {
		TrainStationManager tsm = new TrainStationManager();

		
//		Map<String,List<Station>> stns= tsm.getStations();
//		List<String> ki =stns.getKeys();
//		List<List<Station>> vi=stns.getValues();
//		for(int i=0;i<stns.size();i++) {
//			System.out.println(ki.get(i));
//			System.out.println(vi.get(i));
//		}
		
//		Map<String,List<String>> stns= tsm.getStationStops();
//		List<String> ki =stns.getKeys();
//		List<List<String>> vi=stns.getValues();
//		for(int i=0;i<stns.size();i++) {
//			System.out.println(ki.get(i));
//			System.out.println(vi.get(i));
//		}
//		
//		
//		
//		tsm.traceRoute("Dome");
		
		tsm.getTravelTimes();
		
		
		
	}

}