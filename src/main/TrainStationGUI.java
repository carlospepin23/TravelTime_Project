package main;

import javax.swing.*;

import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;
import interfaces.List;
import interfaces.Map;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrainStationGUI extends JFrame {
	
    private Map<String,String> s_departureTimes = new HashTableSC<String, String>(1, new SimpleHashFunction<String>());
    private Map<String,Double> travel_times = new HashTableSC<String, Double>(1, new SimpleHashFunction<String>());
    private List<String> stations;
    
    

    public TrainStationGUI() {
    	
    	super("Station Departure and Arrival Times");
    	 
    	s_departureTimes.put("Bugapest","09:35 AM");
    	s_departureTimes.put("Dubay","10:30 AM");
    	s_departureTimes.put("Berlint","08:25 AM");
    	s_departureTimes.put("Mosbull","06:00 AM");
    	s_departureTimes.put("Cayro","06:40 AM");
    	s_departureTimes.put("Bostin","10:25 AM");
    	s_departureTimes.put("Los Angelos","12:30 PM");
    	s_departureTimes.put("Dome","01:30 PM");
    	s_departureTimes.put("Takyo","03:35 PM");
    	s_departureTimes.put("Unstabul","04:45 PM");
    	s_departureTimes.put("Chicargo","07:25 AM");
    	s_departureTimes.put("Loondun","02:00 PM");
    	
    	TrainStationManager tsmGUI = new TrainStationManager();
        travel_times = tsmGUI.getTravelTimes();
 	    stations=tsmGUI.getStations().getKeys();
 	    
        initComponents();
    	
    }
    
    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(stations.size(), 2));

        for (int i=0;i<stations.size(); i++) {
        	if(stations.get(i).equals("Westside")) {
        		continue;
        	}
        	
            LocalTime departure = LocalTime.parse(s_departureTimes.get(stations.get(i)), DateTimeFormatter.ofPattern("hh:mm a"));
			LocalTime arrival = departure.plusMinutes(travel_times.get(stations.get(i)).longValue());
			System.out.println(arrival);

            JLabel stationLabel = new JLabel(stations.get(i));
            JLabel timesLabel = new JLabel(String.format("Departure: %s, Arrival: %s",
                    departure.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    arrival.format(DateTimeFormatter.ofPattern("hh:mm a"))));

            panel.add(stationLabel);
            panel.add(timesLabel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
    	TrainStationGUI SGUI = new TrainStationGUI();
    	
    	;
    }
}