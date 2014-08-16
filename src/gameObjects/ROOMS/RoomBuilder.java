package gameObjects.ROOMS;

import gameObjects.Location;
import java.util.ArrayList;
import java.util.List;

/**	
 * Builder Class: The whole purpose of this class is to Build the Rooms and Return them.
 * This Class marks all the Locations in a Room as being INACCESSIBLE, then goes back and
 * marks the Doors as ACCESSIBLE.
 * */
public class RoomBuilder {

	private List<Room> rooms = new ArrayList<>(); // The FINISHED Rooms

	// XXXXXLocs = All Locations in Room XXXXX
	// XXXXXDoors = All Locations in Room XXXXX THAT REPRESENT DOORS
	private List<Location> kitchenLocs = new ArrayList<>();				private List<Location> kitchenDoors = new ArrayList<>();
	private List<Location> studyLocs = new ArrayList<>();				private List<Location> studyDoors = new ArrayList<>();
	private List<Location> loungeLocs = new ArrayList<>();				private List<Location> loungeDoors = new ArrayList<>();
	private List<Location> conservatoryLocs = new ArrayList<>();		private List<Location> conservatoryDoors = new ArrayList<>();
	private List<Location> diningRoomLocs = new ArrayList<>();			private List<Location> diningRoomDoors = new ArrayList<>();
	private List<Location> ballroomLocs = new ArrayList<>();			private List<Location> ballroomDoors = new ArrayList<>();
	private List<Location> billiardLocs = new ArrayList<>();			private List<Location> billiardDoors = new ArrayList<>();
	private List<Location> libraryLocs = new ArrayList<>();				private List<Location> libraryDoors = new ArrayList<>();
	private List<Location> hallLocs = new ArrayList<>();				private List<Location> hallDoors = new ArrayList<>();

	/**
	 * CONSTRUCTOR
	 * 
	 * @param spaces The Cluedo Board - This is used to get and mark Locations as belonging to a room 
	 * */
	public RoomBuilder(Location[][] spaces) {
		buildRooms(spaces);
	}

	/**
	 * This method calls a method that constructs the XXXXXLocs and
	 * XXXXXDoors Lists, then uses the lists to build a new Room.
	 * */
	private void buildRooms(Location[][] spaces) {
		buildLocLists(spaces);
		rooms.add(new Room("Kitchen",		true,	"Study",		kitchenLocs,		kitchenDoors		));
		rooms.add(new Room("Study",			true,	"Kitchen",		studyLocs,			studyDoors			));
		rooms.add(new Room("Lounge",		true,	"Conservatory",	loungeLocs,			loungeDoors			));
		rooms.add(new Room("Conservatory",	true,	"Lounge",		conservatoryLocs,	conservatoryDoors	));
		rooms.add(new Room("Dining Room",	false,	null,			diningRoomLocs,		diningRoomDoors		));
		rooms.add(new Room("Ballroom",		false,	null,			ballroomLocs,		ballroomDoors		));
		rooms.add(new Room("Billiard Room",	false,	null,			billiardLocs,		billiardDoors		));
		rooms.add(new Room("Library",		false,	null,			libraryLocs,		libraryDoors		));
		rooms.add(new Room("Hall",			false,	null,			hallLocs,			hallDoors			));
		
		List<List<Location>> locLists = new ArrayList<List<Location>>();

		locLists.add(kitchenLocs);
		locLists.add(studyLocs);	
		locLists.add(loungeLocs);
		locLists.add(conservatoryLocs);
		locLists.add(diningRoomLocs);
		locLists.add(ballroomLocs);
		locLists.add(billiardLocs);
		locLists.add(libraryLocs);
		locLists.add(hallLocs);
		
		int i = 0;
		for (List<Location> list : locLists){
			for (Location loc : list){
				loc.setPartOfRoom(rooms.get(i));
			}
			i++;
		}
		
	}

	/**
	 * This is responsible for building the list of all the room locations 
	 * (i.e. what board spaces are inside rooms). It also designates which 
	 * spaces are doors. After this method, any space that is in a room 
	 * (EXCEPT DOORS) will be inaccessible, as well as previously 
	 * inaccessible areas.
	 * */
	private void buildLocLists(Location[][] spaces) {
		setupKitchen(spaces);
		setupBallroom(spaces);
		setupConservatory(spaces);
		setupDiningRoom(spaces);
		setupBilliardRoom(spaces);
		setupLibrary(spaces);
		setupLounge(spaces);
		setupHall(spaces);
		setupStudy(spaces);

		/* 
		 * NOW, I'm going to put the lists into a list (loc lists into a list of loc lists and 
		 * door lists into a list of door lists), then iterate through the loc lists and make
		 * them inaccessible.
		 * 
		 * After that, I'll iterate through the door list and make them accessible.
		 * 
		 * The net result will be ONLY doors and tiles on the board can be moved to (excluding middle).
		 */

		List<List<Location>> locLists = new ArrayList<List<Location>>();
		List<List<Location>> doorLists = new ArrayList<List<Location>>();

		locLists.add(kitchenLocs);		doorLists.add(kitchenDoors);
		locLists.add(studyLocs);		doorLists.add(studyDoors);	
		locLists.add(loungeLocs);		doorLists.add(loungeDoors);
		locLists.add(conservatoryLocs);	doorLists.add(conservatoryDoors);
		locLists.add(diningRoomLocs);	doorLists.add(diningRoomDoors);
		locLists.add(ballroomLocs);		doorLists.add(ballroomDoors);
		locLists.add(billiardLocs);		doorLists.add(billiardDoors);
		locLists.add(libraryLocs);		doorLists.add(libraryDoors);
		locLists.add(hallLocs);			doorLists.add(hallDoors);

		for (List<Location> list : locLists){
			for (Location l : list){
				l.setAccessible(false);
			}
		}

		for (List<Location> list : doorLists){
			for (Location l : list){
				l.setAccessible(true);
			}
		}
	}

	private void setupStudy(Location[][] spaces) {
		for (int i = 17; i < 24; i++){
			for (int j = 21; j < 24; j++){
				studyLocs.add(spaces[i][j]);
			}
		}
		studyDoors.add(spaces[17][21]);
	}

	private void setupHall(Location[][] spaces) {
		hallLocs.add(spaces[10][24]);
		hallLocs.add(spaces[11][24]);
		hallLocs.add(spaces[12][24]);	
		hallLocs.add(spaces[13][24]);
		for (int i = 9; i < 15; i++){
			for (int j = 18; j < 24; j++){
				hallLocs.add(spaces[i][j]);
			}
		}
		hallDoors.add(spaces[11][18]);
		hallDoors.add(spaces[12][18]);
		hallDoors.add(spaces[14][21]);
	}

	private void setupLounge(Location[][] spaces) {
		for (int i = 0; i < 7; i++){
			for (int j = 19; j < 24; j++){
				loungeLocs.add(spaces[i][j]);
			}
		}
		loungeDoors.add(spaces[6][19]);
	}

	private void setupLibrary(Location[][] spaces) {
		libraryLocs.add(spaces[17][15]);
		libraryLocs.add(spaces[17][16]);
		libraryLocs.add(spaces[17][17]);
		libraryLocs.add(spaces[23][15]);
		libraryLocs.add(spaces[23][16]);
		libraryLocs.add(spaces[23][17]);
		for (int i = 18; i < 23; i++){
			for (int j = 14; j < 19; j++){
				libraryLocs.add(spaces[i][j]);
			}
		}
		libraryDoors.add(spaces[17][16]);
		libraryDoors.add(spaces[20][14]);
	}

	private void setupBilliardRoom(Location[][] spaces) {
		for (int i = 18; i < 24; i++){
			for (int j = 8; j < 13; j++){
				billiardLocs.add(spaces[i][j]);
			}
		}
		billiardDoors.add(spaces[18][9]);
		billiardDoors.add(spaces[22][12]);
	}

	private void setupDiningRoom(Location[][] spaces) {
		diningRoomLocs.add(spaces[0][9]);
		diningRoomLocs.add(spaces[1][9]);
		diningRoomLocs.add(spaces[2][9]);
		diningRoomLocs.add(spaces[3][9]);
		diningRoomLocs.add(spaces[4][9]);
		for (int i = 0; i < 8; i++){
			for (int j = 10; j < 16; j++){
				diningRoomLocs.add(spaces[i][j]);
			}
		}
		diningRoomDoors.add(spaces[6][15]);
		diningRoomDoors.add(spaces[7][12]);
	}

	private void setupConservatory(Location[][] spaces) {
		for (int i = 18; i < 24; i++){
			for (int j = 1; j < 6; j++){
				conservatoryLocs.add(spaces[i][j]);
			}
		}
		conservatoryLocs.remove(spaces[18][5]);
		conservatoryDoors.add(spaces[18][4]);
	}

	private void setupBallroom(Location[][] spaces) {
		ballroomLocs.add(spaces[11][0]);
		ballroomLocs.add(spaces[12][0]);
		ballroomLocs.add(spaces[10][1]);
		ballroomLocs.add(spaces[11][1]);
		ballroomLocs.add(spaces[12][1]);
		ballroomLocs.add(spaces[13][1]);
		for (int i = 8; i < 16; i++){
			for (int j = 2; j < 8; j++){
				ballroomLocs.add(spaces[i][j]);
			}
		}
		ballroomDoors.add(spaces[8][5]);
		ballroomDoors.add(spaces[9][7]);
		ballroomDoors.add(spaces[14][7]);
		ballroomDoors.add(spaces[15][5]);
	}

	private void setupKitchen(Location[][] spaces) {
		for (int i = 0; i < 6; i++){
			for (int j = 1; j < 7; j++){
				kitchenLocs.add(spaces[i][j]);
			}
		}
		kitchenDoors.add(spaces[4][6]);
	}

	public List<Room> getRooms(){
		return rooms;
	}
}