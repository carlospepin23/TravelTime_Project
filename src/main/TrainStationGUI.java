package main;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import interfaces.List;
import interfaces.Map;
import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;

/**
 * The TrainStationGUI class represents the graphical user interface for the train station management system.
 * It displays the train schedules and departure times for various destinations from Westside Station.
 * 
 * @author Carlos J. Pepin Delgado
 * @version 4/19/2023
 */
public class TrainStationGUI extends JFrame {
    
    private Map<String,String> departureTimes;
    private Map<String,Double> travel_times;
    private List<String> stations;
    private TrainStationManager tsmGUI;
    
    /**
     * Constructs a new TrainStationGUI object.
     * Initializes the train station manager, sets departure times, retrieves travel times and station list,
     * and initializes the graphical user interface.
     */
    public TrainStationGUI() {
        super("Welcome to Westside Station!");
        tsmGUI = new TrainStationManager();
        
        setDepartureTimes();
        travel_times = tsmGUI.getTravelTimes();
        stations = tsmGUI.getStations().getKeys();
        
        initGUI();
    }
    
    /**
     * Initializes the graphical user interface by creating a panel to display station schedules and departure times.
     */
    private void initGUI() {
        JPanel panel = new JPanel(new GridLayout(getStations().size(), 2));
        for (int i = 0; i < getStations().size(); i++) {
            if (stations.get(i).equals("Westside")) { //if its Westside skip
                continue;
            }
            //processes departure time
            LocalTime depart = LocalTime.parse(getDepartureTimes().get(getStations().get(i)), DateTimeFormatter.ofPattern("hh:mm a"));
            //processes arrival time
            LocalTime arr = depart.plusMinutes(getTravelTimes().get(getStations().get(i)).longValue());
            //formatted text in gui
            JLabel stationLabel = new JLabel(getStations().get(i));
            JLabel timesLabel = new JLabel(String.format("Departure: %s, Arrival: %s",
            		depart.format(DateTimeFormatter.ofPattern("hh:mm a")),
            		arr.format(DateTimeFormatter.ofPattern("hh:mm a"))));
            
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
    
    /**
     * Sets the departure times for various destinations from Westside Station.
     */
    public void setDepartureTimes() {
        Map<String,String> dT = new HashTableSC<String, String>(1, new SimpleHashFunction<String>());
        dT.put("Bugapest","09:35 AM");
        dT.put("Dubay","10:30 AM");
        dT.put("Berlint","08:25 AM");
        dT.put("Mosbull","06:00 AM");
        dT.put("Cayro","06:40 AM");
        dT.put("Bostin","10:25 AM");
        dT.put("Los Angelos","12:30 PM");
        dT.put("Dome","01:30 PM");
        dT.put("Takyo","03:35 PM");
        dT.put("Unstabul","04:45 PM");
        dT.put("Chicargo","07:25 AM");
        dT.put("Loondun","02:00 PM");
        this.departureTimes = dT;
    }
    
    /**
     * Retrieves the list of stations.
     * 
     * @return The list of stations.
     */
    public List<String> getStations(){
        return this.stations;
    }
    
    /**
     * Retrieves the travel times to various destinations from Westside Station.
     * 
     * @return The map of travel times.
     */
    public Map<String, Double> getTravelTimes(){
        return this.travel_times;
    }
    
    /**
     * Retrieves the departure times for various destinations from Westside Station.
     * 
     * @return The map of departure times.
     */
    public Map<String, String> getDepartureTimes(){
        return this.departureTimes;
    }
    
    /**
     * The main method to start the train station GUI.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        new TrainStationGUI();
    }
}