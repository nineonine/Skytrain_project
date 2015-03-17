package model;

import java.util.ArrayList;
import java.util.Arrays;

public class StationDAO {

	// You might want to add another factoryClass which will create a DB
	// connection object
	// this db object might be handy here !

	public Station station;

	public StationDAO() {
		super();
	}

	public Station getStation(String stationName) {

		// HERE SHOULD BE IMPLEMENTED SOME LOGIC OF RETREIVING A STATION FROM A
		// DB TABLE
		// WE HAVE TO USE OUR DB CONNECTION OBJECT OR SMTH

		return null; // <------ it should return STATION object
	}

	public ArrayList<String> getListOfStations() {
		
		//getting all names of stations from the DB and returning ArrayList<String> of names
		
		
		
		
		//STUB FOR TESTING
		ArrayList<String> temporaryList = new ArrayList<String>
		(Arrays.asList("22nd Street", "29th Avenue", "Aberdeen",
				"Braid", "Brentwood Town Centre", "Bridgeport", "Broadway City Hall", "Burrard",
				"Columbia", "Commercial Broadway", "Edmonds", "Gateway", "Gilmore", "Granville",
				"Holdom", "Joyce Collingwood", "King Edward", "King George", "Lake City Way",
				"Langara 49th Avenue", "Lansdowne", "Lougheed Town Centre", "Main Street Science World",
				"Marine Drive", "Metrotown", "Nanaimo", "New Westminster", "Oakridge 41st Avenue",
				"Olympic Village", "Patterson", "Production Way University", "Renfrew",
				"Richmond Brighouse", "Royal Oak", "Rupert", "Sapperton", "Scott Road",
				"Sea Island Centre", "Sperling Burnaby Lake", "Stadium Chinatown",
				"Surrey Central", "Templeton", "Vancouver City Centre", "VCC Clark",
				"Waterfront", "Yaletown Roundhouse", "YVR Airport"));
		return temporaryList;
	}

}
