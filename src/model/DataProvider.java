package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataProvider {
	public static HashMap<String,List<String>> getInfo()
	{
		
		HashMap<String, List<String>> TrainDetails = new HashMap<String, List<String>>();
		
		List<String> Expo_Line = new ArrayList<String>();
		Expo_Line.add("Time of Day                            Frequency");
		Expo_Line.add("          Peak Hours                               2-4 mins.");
		Expo_Line.add("          Midday                                          6 mins.");
		Expo_Line.add("          Evening                                         6 mins.");
		Expo_Line.add("          Late Night                                8-10 mins.");
		Expo_Line.add("          Early Sat & Sun                             8 mins.");
		Expo_Line.add("          Sat, Sun & Hol.                         7-10 mins.");
		Expo_Line.add("                                                                      First Train/Last Train");
		Expo_Line.add("King George to Waterfront: ");
		Expo_Line.add("          Monday-Friday                                    5:08am/12:38am");
		Expo_Line.add("          Saturday                                              6:08am/12:38am");
		Expo_Line.add("          Sunday/Holidays                               7:08am/11:38pm");
		Expo_Line.add("Waterfront to King George: ");
		Expo_Line.add("          Monday-Friday                                    5:35am/1:16am");
		Expo_Line.add("          Saturday                                              6:50am/1:16am");
		Expo_Line.add("          Sunday/Holidays                              7:50am/12:15am");
		
		List<String> Millenium_Line = new ArrayList<String>();
		Millenium_Line.add("Time of Day                            Frequency");
		Millenium_Line.add("          Peak Hours                               5-6 mins.");
		Millenium_Line.add("          Midday                                          6 mins.");
		Millenium_Line.add("          Evening                                         6 mins.");
		Millenium_Line.add("          Late Night                                8-10 mins.");
		Millenium_Line.add("          Early Sat & Sun                             8 mins.");
		Millenium_Line.add("          Sat, Sun & Hol.                         7-10 mins.");
		Millenium_Line.add("                                                                      First Train/Last Train");
		Millenium_Line.add("VCC-Clark to Waterfront: ");
		Millenium_Line.add("          Monday-Friday                                    5:34am/12:09am");
		Millenium_Line.add("          Saturday                                              6:34am/12:09am");
		Millenium_Line.add("          Sunday/Holidays                               7:34am/11:09pm");
		Millenium_Line.add("Waterfront to VCC-Clark: ");
		Millenium_Line.add("          Monday-Friday                                   5:54am/12:31am");
		Millenium_Line.add("          Saturday                                             6:54am/12:31am");
		Millenium_Line.add("          Sunday/Holidays                              7:54am/11:31pm");
		
		List<String> Canada_Line = new ArrayList<String>();
		Canada_Line.add("Time of Day                            Frequency");
		Canada_Line.add("          Peak Hours                               6-7 mins.");
		Canada_Line.add("          Midday                                      6-7 mins.");
		Canada_Line.add("          Evening                                      12 mins.");
		Canada_Line.add("          Late Night                                  20 mins.");
		Canada_Line.add("          Early Sat & Sun                         12 mins.");
		Canada_Line.add("          Sat, Sun & Hol.                       7-20 mins.");
		Canada_Line.add("                                                                      First Train/Last Train");
		Canada_Line.add("Waterfront to YVR:                                         4:48am/1:05am");
		Canada_Line.add("Waterfront to Richmond-Brighouse:           5:30am/1:15am");
		Canada_Line.add("YVR to Waterfront:                                      5:07am/12:56am");
		Canada_Line.add("Richmond-Brighouse to Waterfront:        5:02am/12:47am");
		
		TrainDetails.put("Expo Line", Expo_Line);
		TrainDetails.put("Millenium Line", Millenium_Line);
		TrainDetails.put("Canada Line", Canada_Line);
		
		
		return TrainDetails;
	
	}
}
