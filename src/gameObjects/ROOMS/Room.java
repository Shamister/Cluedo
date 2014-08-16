package gameObjects.ROOMS;

import gameObjects.Location;
import gameObjects.Piece;
import gameObjects.WEAPONS.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Room {

	private Weapon weapon = null; // The Weapon that is in the Room - THERE MAY ONLY BE A MAXIMUM OF ONE AT A TIME.
	private List<Piece> characters = new ArrayList<>(); // All the Characters that are currently in the room. 
	public final boolean hasSecretPassage;
	public final String connectingRoom;
	public final String name;
	public final List<Location> spaces;
	public final List<Location> doors;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param name The name of the Room.
	 * 
	 * @param hasSecretPassage Whether the Room has a Secret Passage or not (Study <=> Kitchen and 
	 * Lounge <=> Conservatory ONLY).
	 * 
	 * @param connectingRoom The name of the Connecting Room - ONLY used when the Room has a Secret Passage 
	 * (Study <=> Kitchen and Lounge <=> Conservatory ONLY).
	 * 
	 * @param spaces The list of all the Locations the Room occupies - INCLUDES THE DOOR/S
	 * 
	 * @param doors The list of all the Locations in the Room that represent Doors.
	 * These are the ONLY ACCESSIBLE LOCATIONS in a Room
	 * */
	public Room(String name, boolean hasSecretPassage, String connectingRoom, List<Location> spaces, List<Location> doors){
		this.name = name;
		this.hasSecretPassage = hasSecretPassage;
		this.connectingRoom = connectingRoom;
		this.spaces = spaces;
		this.doors = doors;
	}
	
	/**		This method gets the name of the room.		*/
	public String getRoomName() {	return name;	}
	
	/**		This method gets the weapon in the room (or null).		*/
	public Weapon getWeapon() 			{	return weapon;	}
	
	public void	  setWeapon(Weapon w) 	{	weapon = w;		}

	/**
	 * This method gets a list of all of the characters in the room 
	 * (can DEFINITELY be empty).
	 *
	 * */
	public List<Piece> getCharacters() {	return characters;	}
	
	/**
	 * This method sets the list of all of the characters in the room 
	 * (can DEFINITELY be empty).
	 * 
	 * */
	public void setCharacters(List<Piece> c){	characters = c;	}

	/**		This method checks if the room has a Secret Passage.		*/
	public boolean hasSecretPassage() {	return hasSecretPassage;			}

	/**		This method gets the name of the room connected to it (or null).		*/
	public String getConnectingRoom() {	return connectingRoom;	}

	public String toString() {
		String s = "Name of Room: " + name + "\n";
		if (hasSecretPassage){
			s += "This room has a Secret Passage that connects to " + connectingRoom + "!";
		}
		return s;	
	}
}
