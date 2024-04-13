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
//	private Map<String,Double> Travel_Times = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
	private Map<String,Station> Shortest_Routes = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
	private Stack<Station> toVisit = new ArrayListStack<Station>();
	
//	private Set<Station> Visited= new HashSet<Station>();
	private Set<Station> Visited= new HashSet<Station>();
	
	private List<Station> visitedList=new ArrayList<Station>();
	
	public TrainStationManager() {
		this("stations.csv"); //se cambio para test
	}	
	public TrainStationManager(String station_file) {
		// INPUT DATA

				BufferedReader station_Reader = null;
				try {
					station_Reader = new BufferedReader(new FileReader("inputFiles/" + station_file));
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				String line;

				try {
					station_Reader.readLine(); //skips the first line
					while ((line = station_Reader.readLine()) != null) {
						
						String[] data = line.split(",");
						
						Station s=new Station(data[1], Integer.valueOf(data[2]));
						
						if(Stations.containsKey(data[0])) {
							List<Station> stations=Stations.get(data[0]);
							stations.add(s);
							Stations.put(data[0], stations);
							
						}else {
							List<Station> stations=new ArrayList<Station>();
							stations.add(s);
							Stations.put(data[0], stations);
						}
						
				
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//se anaden los que faltan
				List<String> kys=Stations.getKeys();
				List<List<Station>> vls=Stations.getValues();
				Set<String> missing_stations= new HashSet<String>();
				
				for(int i=0;i<vls.size();i++) {
					for(int j=0;j<vls.get(i).size();j++) {
						Station station=vls.get(i).get(j);
						//si el nombre no esta
						if(!Stations.containsKey(station.getCityName())) {
							
							Station missing=new Station(kys.get(i),vls.get(i).get(j).getDistance());
							List<Station> blank=new ArrayList<Station>();
							
							blank.add(missing);
							Stations.put(station.getCityName(), blank);
							missing_stations.add(station.getCityName());
							
						}
						else if(missing_stations.isMember(station.getCityName())){
							List<Station> outdated= Stations.get(station.getCityName());
							for(Station s:outdated) {
								if(s.getDistance()!=vls.get(i).get(j).getDistance()){
									Station missing_station=new Station(kys.get(i),vls.get(i).get(j).getDistance());
									outdated.add(missing_station);
									break;
								}
							}
						}
						
					}
				}
				
				
				
				findShortestDistance();
				

	}
	
	private void findShortestDistance() {
		List<String> kys=Stations.getKeys();
		Station s=new Station("Westside",Integer.MAX_VALUE);
		
		for( String k:kys) {
			Shortest_Routes.put(k, s);
		}
			
		
	//Main
		//after setup (Westside,INFINITE)
		//start
		s=new Station("Westside",0);
		toVisit.push(s);
		Shortest_Routes.put("Westside", s);
		
		
		while(!toVisit.isEmpty()) {
			Station currStation = toVisit.pop();
			Visited.add(currStation);
			//visitedList.add(currStation);
			
			
			//neighbors
			List<Station> neighbors=Stations.get(currStation.getCityName());
			
			//problema, todos los neighbors del curr station siguen anadiendose
				
				
			for(int j=0;j<neighbors.size();j++) {
				Station n=neighbors.get(j);
				Station c=Shortest_Routes.get(currStation.getCityName());
				
				Integer a=Shortest_Routes.get(n.getCityName()).getDistance();
				Integer b_and_c=n.getDistance()+c.getDistance();
				
				
				//se modifica la distancia
				if(a>b_and_c) {
					Station mod_n=new Station(currStation.getCityName(),b_and_c);
					Shortest_Routes.put(n.getCityName(), mod_n);
				}
				
				//se anade al toVisit
				if(!Visited.isMember(n)) {   //error esta en esta linea porque se repiten en el tovisit
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
		// 5 minutes per kilometer
		// 15 min per station
		Map<String,Double> Travel_Times = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
		
		
		return Travel_Times;
		
		
	}
//
//
	public Map<String, List<Station>> getStations() {
		return Stations;
	}


//	public void setStations(Map<String, List<Station>> cities) {
//		this.Stations=cities;
//	}


	public Map<String, Station> getShortestRoutes() {
		return Shortest_Routes;
	}


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
		// Remove if you implement the method, otherwise LEAVE ALONE
		throw new UnsupportedOperationException();
	}
	
	public static void main(String[] args) {
		TrainStationManager tsm = new TrainStationManager();
//		List<String> keys = tsm.T_Station_Manager.getKeys();
//		List<List<Station>> vals = tsm.T_Station_Manager.getValues();
//		
//		for(String k:keys) {
//			System.out.println(k);
//		}
//		
//		for(int i=0;i<vals.size();i++) {
//			for(int j=0;j<vals.get(i).size();j++) {
//				System.out.println(vals.get(i).get(j));
//			}
//			System.out.println();
//			System.out.println();
//		}
		
		Map<String,Station> shrt = tsm.getShortestRoutes();
		List<String> k =shrt.getKeys();
		List<Station> v =shrt.getValues();
		
		for(int i=0;i<shrt.size();i++) {
			System.out.println(k.get(i));
			System.out.println(v.get(i));
		}
		
		Map<String,List<Station>> stns= tsm.getStations();
		List<String> ki =stns.getKeys();
		List<List<Station>> vi=stns.getValues();
		for(int i=0;i<stns.size();i++) {
			System.out.println(ki.get(i));
			System.out.println(vi.get(i));
		}
		
	}

}