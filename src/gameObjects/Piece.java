package gameObjects;

import gameObjects.ROOMS.Room;

/**
 * This interface allows us to treat Characters and NPCs in similar 
 * fashions, such as summoning them to a room for the purposes of 
 * suggestion. 
 * */
public interface Piece {

	/**
	 * VERY simple check to see if the Piece is a Character or an NPC
	 * */
	public boolean isCharacter();
	
	/**
	 * EVERY Piece has a location, so it's important we can get it.
	 * */
	public Location getPosition();
	
	/**
	 * EVERY Piece has a token.
	 * */
	public int getToken();

	public boolean isActive();

	public Room getRoom();
	
	public void setRoom(Room room);

	public void setOldRoom(Room room);

	public void setPosition(Location l);

	public boolean isInRoom();
	
	public void setInRoom(boolean b);
}
