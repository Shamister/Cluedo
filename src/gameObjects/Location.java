package gameObjects;

import java.util.ArrayList;
import java.util.List;

import findingLocations.Node;
import gameObjects.ROOMS.Room;

/**
 * Stores all of the locations on the board, and whether a character may move onto it.
 * Also able to return its direct neighbours if it's given the Cluedo board 2D spaces 
 * array see {@link getNeighbours()} 
 * */
public class Location {
	
	public final int x;
	public final int y;
	
	private boolean accessible = true;
	private Piece pieceOn = null;
	private Node node;
	private Room partOfRoom = null;
	
	private boolean selected = false;
	
	public Location(int x, int y, boolean a){
		this.x = x;
		this.y = y;
		accessible = a;
		node = new Node(x, y, false, this);
	}

	/**
	 * Returns whether the location can be moved onto (i.e. wall or character)
	 * */
	public boolean getAccessible(){
		return accessible;
	}
	
	/**
	 * Sets whether the location can be moved onto (i.e. when a character has moved from its square to an ACCESSIBLE square)
	 * */
	public void setAccessible(boolean accessible){
		this.accessible = accessible;
	}
	
	/**
	 * Returns the piece on this location (null if no piece is present)
	 * */
	public Piece getPieceOn(){
		return pieceOn;
	}
	
	/**
	 * Sets the piece that's on this square (or null if a piece is getting moved off)
	 * */
	public void setPieceOn(Piece pieceOn){
		this.pieceOn = pieceOn;
	}
	
	public Room getPartOfRoom() {
		return partOfRoom;
	}

	public void setPartOfRoom(Room partOfRoom) {
		this.partOfRoom = partOfRoom;
	}

	public Node getNode() {
		return node;
	}

	/**
	 * This method can ONLY be used by classes that have knowledge of the Board's spaces[][] array.
	 * This method returns the four immediately adjacent squares:
	 * <br />
	 * [?][?][?][?][?]<br />
	 * [?][?][X][?][?]<br />
	 * [?][X][O][X][?]<br />
	 * [?][?][X][?][?]<br />
	 * [?][?][?][?][?]<br />
	 * <br />
	 * If [O] is current Location (this), then [X] are the neighbours returned. All the others ( [?] ) are 
	 * not direct neighbours (i.e. a Character cannot move diagonally, and therefore diagonals are 
	 * ignored and not considered to be neighbours)
	 * @param allLocations The spaces[][] 2D array from the Board class - mainly used for the Board
	 * class's move method.
	 * @return a list of all of the location's immediately adjacent squares
	 * */
	public List<Location> getNeighbours(Location[][] allLocations){
		List<Location> neighbours = new ArrayList<>();
		int width = allLocations.length;
		int height = allLocations[0].length;
		
		/*
		 * EDGE CASES: When the door is on a corner:
		 * [*][*][*][*][*]
		 * [*][*][*][*][*]
		 * [R][R][D][*][*]
		 * [R][R][R][*][*]
		 * [R][R][R][1][*]
		 * 
		 * if 1 is Player 1 (or ANY player), * is where they can move, R 
		 * is the room, and D is the Door, then the player should only
		 * be allowed to enter the room from one direction, not two
		 * 
		 * */
		
		// This is the ONLY exit allowed from the Lounge
		if (x == 6 && y == 19 && allLocations[x][y-1].accessible)		{	neighbours.add(allLocations[x][y-1]);	}
		// To the right of the Lounge - CANNOT enter lounge from right (have to be "above" the door)
		else if (x == 7 && y == 19){
			if (allLocations[x+1][y].accessible) neighbours.add(allLocations[x+1][y]);
			if (allLocations[x][y+1].accessible) neighbours.add(allLocations[x][y+1]);
			if (allLocations[x][y-1].accessible) neighbours.add(allLocations[x][y-1]);
		}
		// This is the ONLY exit allowed from the Study
		else if (x == 17 && y == 21 && allLocations[x][y-1].accessible)	{	neighbours.add(allLocations[x][y-1]);	}
		// To the left of the Study - CANNOT enter Study from left (have to be "above" the door)
		else if (x == 16 && y == 21){
			if (allLocations[x-1][y].accessible) neighbours.add(allLocations[x-1][y]);
			if (allLocations[x][y+1].accessible) neighbours.add(allLocations[x][y+1]);
			if (allLocations[x][y-1].accessible) neighbours.add(allLocations[x][y-1]);
		}
		// This is the ONLY exit allowed from the Conservatory
		else if (x == 18 && y == 4 && allLocations[x][y+1].accessible)	{	neighbours.add(allLocations[x][y+1]);	}
		// To the left of the Conservatory - CANNOT enter Conservatory from left (have to be "below" the door)
		else if (x == 17 && y == 4){
			if (allLocations[x-1][y].accessible) neighbours.add(allLocations[x-1][y]);
			if (allLocations[x][y+1].accessible) neighbours.add(allLocations[x][y+1]);
			if (allLocations[x][y-1].accessible) neighbours.add(allLocations[x][y-1]);
		}
		/*
		 * General Case: We don't need to worry about the doors anymore
		 * if statement explained - basically, only 3 / 4 corner rooms (secret passage rooms) have doors
		 * on corners. SO, if we're NOT in a room, OR we are NOT in a corner room (except for the Kitchen), 
		 * then do the following block.
		 * 
		 * We are NOT in a corner room (except for the Kitchen) = The room we are in has NO secret passage.
		 * IF the room we are in DOES have a secret passage, and it's the Kitchen, that's okay, run the block.
		 * 
		 * OTHERWISE we are either: 
		 * 		In a room with a corner door and someone is outside blocking us, OR
		 * 		We are NEXT TO a corner door, but we aren't allowed in that way.
		 * 
		 * If there are no neighbours, then we're at the end of our movement range, or unable to move at all.
		 * */
		else if (getPartOfRoom() == null || (!getPartOfRoom().hasSecretPassage() || getPartOfRoom().getRoomName().equals("Kitchen"))){
			if (x+1 >= 0 && x+1 < width && y >= 0 && y < height && allLocations[x+1][y].getAccessible())
			neighbours.add(allLocations[x+1][y]);
			
			if (x >= 0 && x < width && y+1 >= 0 && y+1 < height && allLocations[x][y+1].getAccessible())
			neighbours.add(allLocations[x][y+1]);
			
			if (x-1 >= 0 && x-1 < width && y >= 0 && y < height && allLocations[x-1][y].getAccessible())
				neighbours.add(allLocations[x-1][y]);
				
			if (x >= 0 && x < width && y-1 >= 0 && y-1 < height && allLocations[x][y-1].getAccessible())
			neighbours.add(allLocations[x][y-1]);
		}
		return neighbours;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String toString(){
		String string = "";
		string += "(" + x + ", " + y + "), Accessible: " + accessible;
		if (partOfRoom != null)
			string += "\nThis location is part of the " + partOfRoom.getRoomName();
		return string;
	}
}
