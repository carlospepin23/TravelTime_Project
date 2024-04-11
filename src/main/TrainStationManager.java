package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.HashTableSC;
//import data_structures.LinkedListStack;
import data_structures.LinkedStack;
import data_structures.SimpleHashFunction;

import data_structures.HashSet;


import interfaces.List;
import interfaces.Map;
import interfaces.Stack;
import interfaces.Set;

public class TrainStationManager {
	private Map<String,List<Station>> T_Station_Manager = new HashTableSC<String, List<Station>>(1, new SimpleHashFunction<String>());
//	private Map<String,Double> Travel_Times = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
	private Map<String,Station> Shortest_Distance = new HashTableSC<String, Station>(1, new SimpleHashFunction<String>());
	private Stack<Station> toVisit = new LinkedStack<Station>();
	
//	private Stack<Station> Discovered = new LinkedStack<Station>();
	private Set<Station> Visited= new HashSet<Station>();
	
	public TrainStationManager() {
		this("stations.csv");
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
					while ((line = station_Reader.readLine()) != null) {
						
						String[] data = line.split(",");
						
						if(data[0].equals("src_city")) continue;
						
						Station s=new Station(data[1], Integer.valueOf(data[2]));
						
						if(T_Station_Manager.containsKey(data[0])) {
							List<Station> stations=T_Station_Manager.get(data[0]);
							stations.add(s);
							T_Station_Manager.put(data[0], stations);
							
						}else {
							List<Station> stations=new ArrayList<Station>();
							stations.add(s);
							T_Station_Manager.put(data[0], stations);
						}
						
				
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				
			//first station setup
				List<String> kys=T_Station_Manager.getKeys();
				List<List<Station>> vls=T_Station_Manager.getValues();
				
				for(int i=0;i<vls.size();i++) {
					String s1_name=kys.get(i);
					
					for(int j=0;j<vls.get(i).size();j++) {
						String s2_name=vls.get(i).get(j).getCityName();
						Integer s2_dist=vls.get(i).get(j).getDistance();
						
						Station to_add=new Station(s1_name,s2_dist);
						List<Station> to_add_list=new ArrayList<Station>();
						to_add_list.add(to_add);
						T_Station_Manager.put(s2_name, to_add_list);
					}
				}
				
				kys=T_Station_Manager.getKeys();
				
				
				Station s=new Station("Westside",Integer.MAX_VALUE);
				
				for( String k:kys) {
					Shortest_Distance.put(k, s);
				}
					
				
			//Main
				//after setup
				
				
				s=new Station("Westside",0);
				toVisit.push(s);
				Shortest_Distance.put("Westside", s);
				while(!toVisit.isEmpty()) {
					Station currStation = toVisit.pop();
					Visited.add(currStation);
					
					//neighbors
					List<Station> neighbors=T_Station_Manager.get(currStation.getCityName());
						
						
					for(int j=0;j<neighbors.size();j++) {
						Station n=neighbors.get(j); //Buga
						Station n_dist=Shortest_Distance.get(currStation.getCityName());
						Integer calc_dist=n.getDistance()+n_dist.getDistance();
						
						//se modifica la distancia
						if(calc_dist<Shortest_Distance.get(n.getCityName()).getDistance()) {
							Station mod_n=new Station(currStation.getCityName(),calc_dist);
							Shortest_Distance.put(n.getCityName(), mod_n);
						}
						//se anade al toVisit
						if(!Visited.isMember(n)) {
							sortStack(n, toVisit);
						}
						
									
					}
					
					
						
					
				}
				
				
				

	}
	
	private void findShortestDistance() {
				
	}

	public void sortStack(Station station, Stack<Station> stackToSort) {
		
		Stack<Station> left=new LinkedStack<Station>();
		Stack<Station> right=new LinkedStack<Station>();
	
		while(!stackToSort.isEmpty()) {
			left.push(stackToSort.pop());
		}
		
		left.push(station);
	
		while(!left.isEmpty()) {
			Station temp=left.pop();
			while(!right.isEmpty() && temp.getDistance()>right.top().getDistance()) {
				left.push(right.pop());
			}
			right.push(temp);
		}
	
		while(!right.isEmpty()) {
			stackToSort.push(right.pop());
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
		return T_Station_Manager;
	}


	public void setStations(Map<String, List<Station>> cities) {
		
	}


	public Map<String, Station> getShortestRoutes() {
		return Shortest_Distance;
	}


	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		
	}
	
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
		List<String> keys = tsm.T_Station_Manager.getKeys();
		List<List<Station>> vals = tsm.T_Station_Manager.getValues();
		
		for(String k:keys) {
			System.out.println(k);
		}
		
		for(int i=0;i<vals.size();i++) {
			for(int j=0;j<vals.get(i).size();j++) {
				System.out.println(vals.get(i).get(j));
			}
			System.out.println();
			System.out.println();
		}
		
		
	}

}