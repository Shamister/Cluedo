package gameObjects.WEAPONS;

import gameObjects.ROOMS.Room;

public class Weapon{

	private String name;
	private Room room = null;
	
	public Weapon(String n) {	name = n;	}
	
	public Weapon(String n, Room r) {
		name = n;
		room = r;
	}
	
	/**
	 * This method returns the name of the Weapon.
	 * This is used mainly to check against actual objects - e.g. checking Rope to Rope.
	 * */
	public String getWeaponName() {
		return name;
	}

	/**
	 * This method returns the Room the Weapon is in.
	 * ALL WEAPONS MUST HAVE A ROOM (i.e. room != null)
	 * */
	public Room getRoom() {
		return room;
	}

	/**		This method sets the Room the Weapon is in.		*/
	public void setRoom(Room room) {
		this.room = room;
	}
}