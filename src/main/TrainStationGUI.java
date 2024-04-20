package main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import interfaces.List;
import interfaces.Map;
import data_structures.HashTableSC;
import data_structures.SimpleHashFunction;

public class TrainStationGUI extends JFrame {
    
    private Map<String,String> departureTimes;
    private Map<String,Double> travel_times;
    private List<String> stations;
    private TrainStationManager tsmGUI;
    private Image backgroundImage;
    
    public TrainStationGUI() {
        super("Welcome to Westside Station!");
        tsmGUI = new TrainStationManager();
        
        setDepartureTimes();
        travel_times = tsmGUI.getTravelTimes();
        stations = tsmGUI.getStations().getKeys();
        
        loadBackgroundImage();
        initC();
    }
    
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("inputFiles/thomas.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
    
    private void initC() {
        JPanel panel = new JPanel(new GridLayout(getStations().size(), 2));
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).equals("Westside")) {
                continue;
            }
            LocalTime departure = LocalTime.parse(getDepartureTimes().get(getStations().get(i)), DateTimeFormatter.ofPattern("hh:mm a"));
            LocalTime arrival = departure.plusMinutes(getTravelTimes().get(getStations().get(i)).longValue());
            JLabel stationLabel = new JLabel(getStations().get(i));
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
    
    public List<String> getStations(){
        return this.stations;
    }
    
    public Map<String, Double> getTravelTimes(){
        return this.travel_times;
    }
    
    public Map<String, String> getDepartureTimes(){
        return this.departureTimes;
    }
    
    public static void main(String[] args) {
        new TrainStationGUI();
    }
}