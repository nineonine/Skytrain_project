package model;

import android.content.ContentValues;

public class StationDetails {
	
	public static String UPDATE_WHERE = SkytrainOpenHelper.STN_NAME_COL + " = ?";

	private String[] desc;
	private String[] loc;
	private String[] lat;
	private String[] lon;
	private String[] img;
	private String[] name;
	
	public StationDetails(){
		desc = new String[53];
		desc[0] = "22nd Street Station is located on an elevated portion of the SkyTrain rapid transit system serving Metro Vancouver,British Columbia. The station opened in 1985 as part of the original Expo Line, and is currently serving both Expo Line and Millennium Line trains.";
		desc[1] = "29th Avenue Station is located at an at-grade portion of the Expo Line, a part of Metro Vancouver's SkyTrain rapid transit system.\n\n" + 
				"The station was built in 1985 as part of the original Expo Line, and provides services to a number of TransLink bus routes serving east Vancouver.";
		desc[2] = "Aberdeen Station is a SkyTrain station on the Canada Line in Richmond, British Columbia, Canada, in the Metro Vancouver region. It is named after the adjacent Aberdeen Centre, the largest of Richmond's Asian-themed malls.";
		desc[3] = "";
		desc[4] = "Brentwood Town Centre Station (sometimes abbreviated as Brentwood Station) is located on an elevated portion of the Millennium Line. The station is a part of Metro Vancouver's SkyTrain rapid transit system, and is one of the busier stations on the Millennium Line.";
		desc[5] = "Bridgeport Station is a SkyTrain station on the Canada Line in Richmond, British Columbia, Canada, south of Vancouver.";
		desc[6] = "Broadway-City Hall Station is a station on the SkyTrain's Canada Line in Vancouver, British Columbia, Canada. This station has a large double-height ceiling over the platform area.";
		desc[7] = "Burrard Station is a SkyTrain station in Vancouver, British Columbia, Canada, served by the Expo and Millennium Lines. The station is located in the Dunsmuir Tunnel located beneath Downtown Vancouver, and opened in 1985.";
		desc[8] = "Columbia Station is located in the short underground section of the Expo Line in New Westminster, British Columbia, Canada. It is a part of Metro Vancouver's SkyTrain rapid transit system, and one of two transfer points to the newer Millennium Line.";
		desc[9] = "Commercial-Broadway Station (formerly Broadway Station and Commercial Drive Station, separately) is located on an elevated portion of the Expo Line and an at-grade portion of the Millennium Line, a part of Metro Vancouver's SkyTrain rapid transit system. It is a major transit hub, with the most boardings of any SkyTrain station, and a terminus of Metro Vancouver's busiest bus route, the 99 B-Line. The station is one of only two in the world where the same metro line runs through it twice in a pretzel configuration, the other being Monument Metro station on the Tyne and Wear Metro in Newcastle upon Tyne, England.";
		desc[10] = "Edmonds Station is located on an at-grade portion of the Expo Line, a part of Vancouver, British Columbia's SkyTrain rapid transit system. It was built in 1985 as part of the original Expo Line.";
		desc[11] = "Gateway Station is a station on an elevated portion of the Expo Line, a part of Vancouver, British Columbia's SkyTrainrapid transit system.";
		desc[12] = "Gilmore Station is a station on the Millennium Line of the SkyTrain system in Burnaby, British Columbia, Canada.\n\n" + 
				"The station is adjacent to several new high rise condominium complexes which are a component of the Brentwood Town Centre Development Plan, responsible for overseeing the area transformed from light industrial/lower occupancy commercial, into an urbanized centre. It is also next to the Vancouver Film Studios." + 
				"The station, completed in 2002, is elevated with two platforms each on either side of the SkyTrain tracks. The station was designed by Busby and Associates who also designed the impressive Brentwood Town Centre Station. Dominion Construction built the Gilmore Station as part of a $14.3-million contract that also included Brentwood Town Centre Station and the Gilmore power substation.";
		desc[13] = "Granville Station is an underground SkyTrain station in Vancouver, British Columbia, Canada, served by the Expo and Millennium Lines. The station is located in the Dunsmuir Tunnel located beneath Downtown Vancouver and opened in 1985." + 
				"The station is named for nearby Granville Street, whose name in turn derives from " + "Granville" + ", the name of the original settlement that preceded Vancouver prior to its incorporation in 1886. The portion of Granville Street on which this station is located is known as the Granville Mall." + 
				"At approximately 25 metres underground, Granville Station is the deepest subway station on the Expo Line.";
		desc[14] = "Holdom Station is an elevated Millennium Line SkyTrain station, a part of Metro Vancouver's SkyTrain rapid transit system.";
		desc[15] = "Joyce-Collingwood Station is on the old right-of-way once used by the old British Columbia Electric Railway Central Park Line; this line ran from just west of Nanaimo Station all the way to where the current New Westminster Station is located.";
		desc[16] = "King Edward Station is a SkyTrain station on the Canada Line in Vancouver, British Columbia, Canada. The station is designed to accommodate the future construction of a transit-connected development over the station entrance.";
		desc[17] = "King George Station is located on an elevated portion of the Expo Line, a part of Metro Vancouver's SkyTrain rapid transit system. The station itself is in Surrey and is the current eastbound terminus of the Expo Line.";
		desc[18] = "Lake City Way Station is a station on the Millennium Line of the SkyTrain system in Metro Vancouver, Canada.";
		desc[19] = "Langara-49th Avenue Station is a SkyTrain station on the Canada Line, located at the intersection of West 49th Avenue and Cambie Street in Vancouver, British Columbia, Canada.";
		desc[20] = "Lansdowne Station is a SkyTrain station on the Canada Line in Richmond, British Columbia, Canada, in the Metro Vancouver region.";
		desc[21] = "Lougheed Town Centre Station (sometimes abbreviated as Lougheed Station) is a station on the Millennium Line, part of the SkyTrain rapid transit system in Metro Vancouver, British Columbia.";
		desc[22] = "Main Street-Science World Station (formerly Main Street Station) is an elevated station on the Expo Line of the SkyTrain system in Vancouver, British Columbia, Canada.";
		desc[23] = "Marine Drive Station is a SkyTrain station on the Canada Line in Vancouver, British Columbia, Canada.";
		desc[24] = "Metrotown Station is an elevated SkyTrain station on the Expo Line in the Metrotown area of Burnaby, British Columbia, Canada. It is the second-busiest station in the SkyTrain system.";
		desc[25] = "Nanaimo Station of the SkyTrain system is an elevated railway station located on the Expo Line in East Vancouver.";
		desc[26] = "The station was completed and opened in December 1985, providing a transportation link to Expo 86 being held in the City of Vancouver, and also serving development and shopping districts along the New Westminster Quay.\n\n" +  
				"New Westminster Station still serves as a temporary terminus for the Expo Line towards closing hours of revenue service. During the station's period as terminus station, a temporary platform had been built over the westbound guideway with trains thus arriving and departing on the eastbound track.";
		desc[27] = "Oakridge-41st Avenue Station is a SkyTrain station on the Canada Line, located at the intersection of West 41st Avenue and Cambie Street in Vancouver, British Columbia, Canada.";
		desc[28] = "Olympic Village Station is a SkyTrain station on the Canada Line in Vancouver, British Columbia, Canada. The use of the term " + "Olympic" + " has been licensed for use by the International Olympic Committee.";
		desc[29] = "The Patterson Station of the SkyTrain system is an elevated station located on the Expo Line in Burnaby, British Columbia, Canada. It is the westernmost Expo Line station in Burnaby.";
		desc[30] = "Production Way - University Station is a rapid transit station on the Millennium Line, a part of Metro Vancouver's SkyTrain rapid transit system.";
		desc[31] = "Renfrew Station is a SkyTrain station on the Millennium Line in east Vancouver.";
		desc[32] = "Richmond-Brighouse Station is the southern terminus railway station of the Canada Line, part of the SkyTrain system in Richmond, British Columbia, Canada, in the Metro Vancouver region.";
		desc[33] = "Royal Oak Station is located on an elevated portion of the Expo Line, a part of Metro Vancouver's SkyTrain rapid transit system. The station itself is in southern Burnaby.";
		desc[34] = "Rupert Station is located on an elevated portion of the Millennium Line. The station is a part of Metro Vancouver's SkyTrain rapid transit system, run by TransLink.";
		desc[35] = "Sapperton Station is located on an elevated portion of the Millennium Line, a part of Metro Vancouver's SkyTrain rapid transit system. The station itself is located in eastern New Westminster. The station will serve the major redevelopment of the nearby Labatt Brewery site.";
		desc[36] = "Scott Road Station is located on an elevated portion of the Expo Line, a part of Vancouver, British Columbia's SkyTrain rapid transit system. The station itself is in northern Surrey.";
		desc[37] = "Sea Island Centre Station is a SkyTrain station on the Canada Line in Richmond, British Columbia, Canada, in the Metro Vancouver region.";
		desc[38] = "Sperling - Burnaby Lake Station is an elevated Millennium Line SkyTrain station, a part of Metro Vancouver's SkyTrain rapid transit system.";
		desc[39] = "Stadium-Chinatown Station (formerly Stadium Station) is part of the SkyTrain system in Metro Vancouver, British Columbia, Canada. It serves both the Expo Line and the Millennium Line at the eastern entrance of the Dunsmuir Tunnel, located beneath Downtown Vancouver. It is one of four stations on the Expo and Millennium Lines currently serving Downtown Vancouver, and is partially elevated with an entrance both above and below street level.";
		desc[40] = "Surrey Central Station is a station in Surrey, British Columbia, on an elevated portion of the Expo Line, a part of Metro Vancouver's SkyTrain rapid transit system.";
		desc[41] = "Templeton Station is a SkyTrain station on the Canada Line in Richmond, British Columbia, Canada, south of Vancouver. The Vancouver International Airport Authority contributed up to $300 million toward the airport branch of the Canada Line, which includes YVR-Airport Station.";
		desc[42] = "Vancouver City Centre Station is a SkyTrain station on the Canada Line in Vancouver, British Columbia, Canada.";
		desc[43] = "VCC-Clark Station is a station on the Millennium Line of the SkyTrain system in Vancouver, British Columbia, Canada. Original plans called for the VCC Station to be located underground under Broadway to the south of Vancouver Community College, but the City of Vancouver wanted the line to run to the north through the emerging technology zone on the False Creek Flats.";
		desc[44] = "Waterfront Station is a major intermodal public transportation facility and the main transit terminus in Downtown Vancouver, British Columbia, Canada.";
		desc[45] = "Yaletown-Roundhouse Station is a SkyTrain station on the Canada Line in Vancouver, British Columbia, Canada.";
		desc[46] = "YVR-Airport Station is one of the two southern intermodal termini on the SkyTrain's Canada Line in Richmond, British Columbia, Canada. The railway station serves both the domestic and international terminals of Vancouver International Airport (YVR), and provides the first direct, transfer-free public transit connection between Downtown Vancouver and the airport.";
		desc[48] = "on planning";
		desc[49] = "on planning";
		desc[50] = "on planning";
		desc[51] = "on planning";
		desc[52] = "on planning";
		
		loc = new String[53];
		loc[0] = "The station is located on 7th Ave between 21 St and 22 St in the western end of New Westminster, a area of low-density residential neighbourhoods. The main functionality of this station, however, is to connect passengers to regional bus routes as it is located close to a major crossroads of the region, where the Queensborough Bridge meets Marine Way and Stewardson Way.\n\n" + 
				"As the station is located on a hill above the Queensborough interchange, it has expansive view of Fraser River and Mount Baker on a clear day.\n\n" + 
				"SkyTrain's operation and maintenance centre is located northwest of this station.";
		loc[1] = "The station is on 29th Avenue at Atlin Street, adjacent to Slocan Park, in the area of Vancouver known as" + "Renfrew Heights" + ", an older, compact community. All of the Skytrain line through this community (from Nanaimo, to well past 29th Avenue) is located at ground level.\n\n" + 
"This station is located on the old right-of-way once used by the old British Columbia Electric Railway Central Park Line; this line ran from just west of Nanaimo Station all the way to where the current New Westminster Station is located.";
		loc[2] = "Aberdeen Station is located south of the intersection of Number 3 Road and Cambie Road. The station is located in close proximity to numerous Asian-themed shopping centres along Richmond's Golden Village, including (from north to south) Yaohan Centre, President Plaza, Aberdeen Centre, and Parker Place.";
		loc[3] = "Braid Station is located at the northwest corner of Brunette Avenue and Braid Street, just south of the Trans-Canada Highway.";
		loc[4] = "The station is located near the intersection of Lougheed Highway and Willingdon Avenue in Burnaby.";
		loc[5] = "Bridgeport Station is located near the intersection of River Road and Great Canadian Way, north of Bridgeport Road and in the same general area as the River Rock Casino, and is the northernmost SkyTrain station in Richmond. The Canada Line's Operations and Maintenance Centre is located northeast of the station. There is a large park-and-ride facility adjacent to the station. The City of Richmond anticipates that the area surrounding this station will be heavily redeveloped, and proposals include the building of office suites and hotels.";
		loc[6] = "The station is located at the intersection of Cambie Street and West Broadway, with its entrance at the southeast corner of the intersection. It serves a number of nearby landmarks: Vancouver City Hall, City Square Mall, Vancouver General Hospital and related facilities; as well as surrounding Fairview and Mount Pleasant neighbourhoods.";
		loc[7] = "Burrard Station is located in the heart of Vancouver's financial district and is very close to Coal Harbour and the West End. The subway station is accessible from the surface via an entrance through an urban park named Discovery Square where Burrard Street meets Melville and Dunsmuir Streets, or via the underground shopping malls of the Royal Centre and Bentall Centre skyscraper complexes.";
		loc[8] = "Columbia Station is located near the corner of 4th Street and Carnarvon Street in New Westminster, and is named after Columbia Street one block south. The station was built horizontally on a slope, so that the inbound platform in the north is underground while the outbound platform in the south is predominantly at grade. The track at both ends of the station dips underground below Clarkson Street.";
		loc[9] = "Commercial-Broadway Station is located just east of the intersection of E.Broadway and Commercial Drive in East Vancouver. Part of the station is elevated over East Broadway, while another part is located deep within the Grandview Cut south of Grandview Highway North. The two parts are connected via an elevator and skyway. It is on the outskirts of Vancouver's Little Italy district.";
		loc[10] = "The station is located in southern Burnaby. It is immediately (130 m) southwest of the intersection of Griffiths Drive and 18th Avenue, near Edmonds Street, a short walk (700 m) south of Kingsway. There are several City in the Park high-rises adjacent to the station as well as plans for more transit-oriented development nearby. SkyTrain's maintenance and storage facility serving both Expo and Millennium lines is one block east of this station, linked to and from the main guideway by multiple switch tracks.";
		loc[11] = "Gateway Station is located on 108th Avenue in downtown Surrey. It was opened in 1994 when the system was extended into Surrey's city centre.";
		loc[12] = "The station is located at Gilmore Avenue and Dawson Street. ";
		loc[13] = "Granville Station is located in the heart of the Granville Entertainment District which is known for urban shopping, trendy culture, and nightlife. The station is accessible from the surface via entrances on Granville Street and Seymour Street (both between Georgia and Dunsmuir Streets), and the Dunsmuir entrance between Granville and Seymour. The station can be accessed underground via Pacific Centre mall, through the Downtown Vancouver premises of the Hudson's Bay Company.";
		loc[14] = "The station is located at the intersection of Lougheed Highway and Holdom Avenue in Burnaby. The station structure is topped with a sculpture by glass artist Graham Scott, including square lanterns made of sandblasted mirror that is lit by stage-quality lighting. The colour of the lights, controlled by a computer, continually and randomly changes.";
		loc[15] = "Joyce-Collingwood Station (formerly Joyce Station) is located on an elevated portion of the Expo Line, a part of Metro Vancouver's SkyTrain rapid transit system. The station is on Joyce Street at Vanness Avenue, in the area of Vancouver known as Collingwood. It is the sixth busiest station on Expo Line.";
		loc[16] = "The station is located at the intersection of Cambie Street and King Edward Avenue, with the station entrance at the northwest corner of the intersection, replacing a small strip mall. The station serves the surrounding residential area, as well as the retail area along Cambie Street. King Edward Station is also the closest station to Queen Elizabeth Park, located 400 metres to the south.";
		loc[17] = "King George Station is located in Surrey City Centre at the corner of King George Boulevard and 100th Ave, just north of the western terminus of the Fraser Highway. On the south side of the station there used to be a large 815 space parking lot operated by Impark. The station has also recently spurred a series of nearby mixed-use developments, like the King George Tower. The most recent proposed development to the south of the station has taken half of the park and ride space out of service.";
		loc[18] = "The station is located at the intersection of Lougheed Highway and Lake City Way in Burnaby, British Columbia.";
		loc[19] = "The station entrance is located on the northeast corner of the intersection. This station primarily serves the residential area surrounding to the station, as well as Langara College and the South Slope YMCA nearby.";
		loc[20] = "Lansdowne Station is located at the intersection of Lansdowne Road and Number 3 Road. ";
		loc[21] = "The station is located on Lougheed Highway in an elevated structure across Austin Road in Burnaby, and is directly adjacent to Lougheed Town Centre, a mid-size shopping mall, from where it takes its name. A large Korean neighbourhood, referred to colloquially as Korea Town, exists in the area surrounding North Road south of Lougheed Highway. Outside the eastern exit of the station, near the passenger waiting area, are the Standing Stones of Lougheed.";
		loc[22] = "The station is accessible from both sides of Main Street at the intersection of Main Street and Terminal Avenue, and is adjacent to Pacific Central Station, the city's inter-city railway and bus terminal.";
		loc[23] = "The station is located on the southeast corner of the intersection of Southwest Marine Drive and Cambie Street. It serves the residential areas of Marpole and the Vancouver South Slope. There are also some shops located across the street from the station.";
		loc[24] = "The station is located on the south side of Central Boulevard, east of Willingdon Avenue.";
		loc[25] = "The station is accessible from the east side of Nanaimo Street at Vanness Avenue. It is located on the crest of a hill, providing all riders a wonderful view of Downtown and the west side of Vancouver.";
		loc[26] = "New Westminster Station is located at Columbia Street and 8th Street in New Westminster, British Columbia, Canada, and was the original eastern terminus of SkyTrain until 1989 prior to the partial completion and opening of the phase two extension of SkyTrain to Surrey.";
		loc[27] = "The station entrance is located on the southwest corner of the intersection, adjacent to the entrance to Oakridge Centre. It serves the Oakridge area, consisting of a residential neighbourhood, streetside stores along Cambie Street, and the Oakridge Centre complex, and is within walking distance of Queen Elizabeth Park. The station is located 6 metres underground.";
		loc[28] = "The station is located underground at the intersection of Cambie Street and West 2nd Avenue, adjacent to the Cambie Street Bridge. It serves the existing South False Creek residential and commercial area, as well as the Olympic Athletes' Village for the 2010 Winter Olympics.";
		loc[29] = "The station is located at the northeastern fringe of Burnaby's Central Park, at the corner of Patterson Avenue and Central Boulevard. A stretch of the SkyTrain line west of the station actually crosses through the park.\n\n" +
"This station is located above the old rights-of-way of the now-defunct British Columbia Electric Railway Central Park Line; this line ran from just west of Nanaimo Station all the way to where the current New Westminster Station is located.";
		loc[30] = "It is located at the intersection of Lougheed Highway and Production Way in Burnaby, British Columbia.";
		loc[31] = "Located on Renfrew Street and 12th Avenue North of Grandview Highway.";
		loc[32] = "The station is located within Richmond's commercial heart, close to the Richmond Centre shopping mall and Richmond City Hall. The station is within a short walking distance of such amenities as Richmond Public Market, Richmond Hospital, and the Minoru civic complex, as well as other nearby office, commercial, and residential buildings.";
		loc[33] = "The station is on Royal Oak Avenue at Beresford Street, a short walk south of Kingsway and Imperial Avenue. It was built in 1985 as part of the original Expo Line.";
		loc[34] = "The station is located on Rupert Street in Vancouver, British Columbia and next to a Real Canadian Superstore and a liquor distribution centre. Falaise Park is just south of Rupert Station. The Vancouver Film Studio is less than half a kilometer from Rupert Station.";
		loc[35] = "Sapperton Station is located along Brunette Avenue in the Sapperton area of New Westminster, above a Canadian Pacific right-of-way, on the shores of the Fraser River. It is connected to ground level by a walkway over Brunette Avenue that exits on Keary Street, across from the Royal Columbian Hospital.";
		loc[36] = "The Scott Road Station is located at the interchange between the King George Highway and Scott Road, at the south end of the Pattullo Bridge, connecting Surrey and New Westminster.";
		loc[37] = "The station is located on Sea Island, along the Canada Line branch serving Vancouver International Airport (YVR), and serves the airline-related industrial area along Grant McConachie Way east of the airport. Both Sea Island Centre and Templeton stations were built at ground level to allow for the future construction of an elevated aircraft taxiway over the Canada Line guideway.";
		loc[38] = "The station is located on the southeast corner of Sperling Avenue and Lougheed Highway, across from Burnaby Lake Regional Park in Burnaby.";
		loc[39] = "Chinatown is located north east of the intersection of Taylor and Keefer. The station entrance closest to Chinatown is marked by traditional Chinese characters in addition to English. The sign reads: " + "Stadium - Chinatown [long string of bakemono here]"+ ". This makes the station the only station on the system to be officially marked in Chinese.";
		loc[40] = "Surrey Central Station is located in downtown Surrey just east of the North Surrey Recreation Centre and near to the Central City Shopping Centre (formerly Surrey Place Mall) and the Surrey Campus of Simon Fraser University.";
		loc[41] = "The station is located on the Grauer Lands on eastern Sea Island, along the airport branch of the Canada Line. ";
		loc[42] = "Vancouver City Centre Station is located on Granville Street, between West Georgia Street and Robson Street in Downtown Vancouver. The station serves the shopping and entertainment districts along Granville and Robson streets, and the office and shopping complexes of Pacific Centre and Vancouver Centre.\n\n" + 
"The station is also within walking distance of such amenities as Robson Square (home of the Vancouver Art Gallery, the Provincial Court of British Columbia, and a UBC satellite campus), the Orpheum Theatre, Vancouver Library Square, TD Tower, Scotia Tower and the HSBC Canada Building.";
		loc[43] = "Named after the nearby Vancouver Community College, near Clark Drive, the station serves as the terminus of the Millennium Line. ";
		loc[44] = "Waterfront Station is located on the south shore of Burrard Inlet, just east of the north foot of Granville Street at 601 West Cordova Street. The station is within walking distance of Vancouver's historical Gastown district, the Canada Place cruise ship terminal, the Helijet International helipad, and Vancouver Harbour Water Aerodrome, the downtown float plane terminals for Harbour Air, West Coast Air, Salt Spring Air, and other airlines.";
		loc[45] = "The station is located on Davie Street at Mainland Street, approximately 80 metres northwest of Pacific Boulevard. The station is located 17 metres underground. The station serves the surrounding rapidly growing residential and retail areas of Yaletown and Downtown South. The station's name derives from the neighbourhood's name, as well as its history as a former train hub (a community centre in the area is also called the Roundhouse).";
		loc[46] = "Located at Sea Island";
		loc[47] = "on planning";
		loc[48] = "on planning";
		loc[49] = "on planning";
		loc[50] = "on planning";
		loc[51] = "on planning";
		loc[52] = "on planning";
		
		lat = new String[53];
		lat[0] = "49.2";
		lat[1] = "49.244084";
		lat[2] = "49.183889";
		lat[3] = "49.23322";
		lat[4] = "49.26633";
		lat[5] = "49.195556";
		lat[6] = "49.262778";
		lat[7] = "49.285616";
		lat[8] = "49.20476";
		lat[9] = "49.2625";
		lat[10] = "49.212054";
		lat[11] = "49.198945";
		lat[12] = "49.26489";
		lat[13] = "49.28275";
		lat[14] = "49.26469";
		lat[15] = "49.23835";
		lat[16] = "49.249167";
		lat[17] = "49.1827";
		lat[18] = "49.25458";
		lat[19] = "49.226389";
		lat[20] = "49.174722";
		lat[21] = "49.24846";
		lat[22] = "49.273114";
		lat[23] = "49.209722";
		lat[24] = "49.225463";
		lat[25] = "49.248184";
		lat[26] = "49.201354";
		lat[27] = "49.233056";
		lat[28] = "49.266389";
		lat[29] = "49.22967";
		lat[30] = "49.25337";
		lat[31] = "49.258889";
		lat[32] = "49.168056";
		lat[33] = "49.220004";
		lat[34] = "49.260833";
		lat[35] = "49.22443";
		lat[36] = "49.20442";
		lat[37] = "49.193056";
		lat[38] = "49.25914";
		lat[39] = "49.279444";
		lat[40] = "49.28202";
		lat[41] = "49.196667";
		lat[42] = "49.28202";
		lat[43] = "49.265753";
		lat[44] = "49.285833";
		lat[45] = "49.27455";
		lat[46] = "49.194167";
		lat[47] = "0";
		lat[48] = "0";
		lat[49] = "0";
		lat[50] = "0";
		lat[51] = "0";
		lat[52] = "0";
		
		lon = new String[53];
		lon[0] = "-122.949167";
		lon[1] = "-123.045931";
		lon[2] = "-123.136389";
		lon[3] = "-122.88283";
		lon[4] = "-123.00163";
		lon[5] = "-123.126111";
		lon[6] = "-123.114444";
		lon[7] = "-123.120157";
		lon[8] = "-122.906161";
		lon[9] = "-123.068889";
		lon[10] = "-122.959226";
		lon[11] = "-122.850559";
		lon[12] = "-123.01351";
		lon[13] = "-123.116639";
		lon[14] = "-122.98222";
		lon[15] = "-123.031704";
		lon[16] = "-123.115833";
		lon[17] = "-122.8446";
		lon[18] = "-122.93903";
		lon[19] = "-123.116111";
		lon[20] = "-123.136389";
		lon[21] = "-122.89702";
		lon[22] = "-123.100348";
		lon[23] = "-123.116944";
		lon[24] = "-123.003182";
		lon[25] = "-123.05564";
		lon[26] = "-122.912716";
		lon[27] = "-123.116667";
		lon[28] = "-123.115833";
		lon[29] = "-123.012376";
		lon[30] = "-122.91815";
		lon[31] = "-123.045278";
		lon[32] = "-123.136389";
		lon[33] = "-122.988381";
		lon[34] = "-123.032778";
		lon[35] = "-122.88964";
		lon[36] = "-122.874157";
		lon[37] = "-123.158056";
		lon[38] = "-122.96391";
		lon[39] = "-123.109444";
		lon[40] = "-123.11875";
		lon[41] = "-123.146389";
		lon[42] = "-123.11875";
		lon[43] = "-123.078825";
		lon[44] = "-123.111667";
		lon[45] = "-123.1219";
		lon[46] = "-123.178333";
		lon[47] = "0";
		lon[48] = "0";
		lon[49] = "0";
		lon[50] = "0";
		lon[51] = "0";
		lon[52] = "0";
		
		img = new String[53];
		name = new String[53];
		img[0] = "22ndSt_2.jpg";
		name[0] = "22nd Street";
		img[1] = "29th_Ave_Station.jpg";
		name[1] = "29th Avenue";
		img[2] = "Aberdeen_Stn.jpg";
		name[2] = "Aberdeen";
		img[3] = "braid.jpg";
		name[3] = "Braid";
		img[4] = "Brentwood.jpg";
		name[4] = "Brentwood Town Centre";
		img[5] = "bridgeport.jpg";
		name[5] = "Bridgeport";
		img[6] = "broadway_cityhall.jpg";
		name[6] = "Broadway-City Hall";
		img[7] = "burrard.jpg";
		name[7] = "Burrard";
		img[8] = "columbia.jpg";
		name[8] = "Columbia";
		img[9] = "commercial_broadway.jpg";
		name[9] = "Commercial-Broadway";
		img[10] = "edmonds.jpg";
		name[10] = "Edmonds";
		img[11] = "gateway.jpg";
		name[11] = "Gateway";
		img[12] = "gilmore.jpg";
		name[12] = "Gilmore";
		img[13] = "granville.jpg";
		name[13] = "Granville";
		img[14] = "holdom.jpg";
		name[14] = "Holdom";
		img[15] = "joyce-collingswood.jpg";
		name[15] = "Joyce-Collingwood";
		img[16] = "king_edwards.jpg";
		name[16] = "King Edward";
		img[17] = "king_george.jpg";
		name[17] = "King George";
		img[18] = "lake_city_way.jpg";
		name[18] = "Lake City Way";
		img[19] = "langara_49.jpg";
		name[19] = "Langara-49th Avenue";
		img[20] = "lansdowne.jpg";
		name[20] = "Lansdowne";
		img[21] = "lougheed.jpg";
		name[21] = "Lougheed Town Centre";
		img[22] = "main_street_science_world.jpg";
		name[22] = "Main Street-Science World";
		img[23] = "marine_drive.jpg";
		name[23] = "Marine Drive";
		img[24] = "metrotown.jpg";
		name[24] = "Metrotown";
		img[25] = "nanaimo.jpg";
		name[25] = "Nanaimo";
		img[26] = "new_westminster.jpg";
		name[26] = "New Westminster";
		img[27] = "oakridge-41st_avenue.jpg";
		name[27] = "Oakridge-41st Avenue";
		img[28] = "olympic_village.jpg";
		name[28] = "Olympic Village";
		img[29] = "patterson.jpg";
		name[29] = "Patterson";
		img[30] = "production-way-university.jpg";
		name[30] = "Production Way-University";
		img[31] = "renfrew.jpg";
		name[31] = "Renfrew";
		img[32] = "richmond_brighthouse.jpg";
		name[32] = "Richmond-Brighouse";
		img[33] = "royal-oak.jpg";
		name[33] = "Royal Oak";
		img[34] = "rupert.jpg";
		name[34] = "Rupert";
		img[35] = "sapperton.jpg";
		name[35] = "Sapperton";
		img[36] = "scott_road.jpg";
		name[36] = "Scott Road";
		img[37] = "sea-island-centre.jpg";
		name[37] = "Sea Island Centre";
		img[38] = "sperling-burnaby-lake.jpg";
		name[38] = "Sperling-Burnaby Lake";
		img[39] = "stadium-chinatown.jpg";
		name[39] = "Stadium-Chinatown";
		img[40] = "surrey_central.jpg";
		name[40] = "Surrey Central";
		img[41] = "templeton.jpg";
		name[41] = "Templeton";
		img[42] = "vancouver_city_centre.jpg";
		name[42] = "Vancouver City Centre";
		img[43] = "vcc_clark.jpg";
		name[43] = "VCC-Clark";
		img[44] = "waterfront.jpg";
		name[44] = "Waterfront";
		img[45] = "yaletown_roundhouse.jpg";
		name[45] = "Yaletown-Roundhouse";
		img[46] = "YVR-Airport.jpg";
		name[46] = "YVR-Airport";
		img[47] = "130607_BurquitlamStation";
		name[47] = "Burquitlam";
		img[48] = "130607_MoodyCtrStation";
		name[48] = "Moody Centre";
		img[49] = "130703_InletCentreStation";
		name[49] = "Inlet Centre";
		img[50] = "130717_Coquitlam_Central_Station";
		name[50] = "Coquitlam Central";
		img[51] = "120901_LincolnStation";
		name[51] = "Lincoln";
		img[52] = "130717_Lafarge_Lake-Douglas Station";
		name[52] = "Lafarge Lake-Douglas";
	}
	
	public String getDesc(int a){
		return desc[a];
	}
	
	public String getLoc(int a){
		return loc[a];
	}
	
	public String getLat(int a){
		return lat[a];
	}
	
	public String getLon(int a){
		return lon[a];
	}
	
	public String getImg(int a){
		return img[a];
	}
	
	public String getName(int a){
		return name[a];
	}
	
	public ContentValues getValues(int a){
		ContentValues cv = new ContentValues();
		cv.put(SkytrainOpenHelper.STN_DESC_COL, desc[a]);
		cv.put(SkytrainOpenHelper.STN_LOC_COL, loc[a]);
		cv.put(SkytrainOpenHelper.STN_LAT_COL, Float.parseFloat(lat[a]));
		cv.put(SkytrainOpenHelper.STN_LON_COL, Float.parseFloat(lon[a]));
		return cv;
	}
}
