package gameObjects;

import gameObjects.ROOMS.Room;

/**
 * ALL player pieces must be on the board - this class simulates the playerless pieces.
 * Crucially, It doesn't have cards (and therefore no hand), and will not be able to move on its own.
 * HOWEVER, if a Character makes a suggestion that involves an NPC, it will be moved to the
 * Character's Room.
 * 
 * THIS IS NOT USED YET, BUT WILL BE EVENTUALLY
 * */
public class NPC implements Piece{
	public final String name;
	public final int token;		/* 1 = Miss Scarlett
		// 2 = Colonel Mustard
		// 3 = Mrs. White
		// 4 = The Reverend Green
		// 5 = Mrs. Peacock
		// 6 = Professor Plum */

	private Location position;
	private boolean inRoom = false;
	private Room room = null;
	private Room oldRoom;

	public NPC(int t, Location start){
		if (t > 6 || t < 1)	throw new IllegalArgumentException("WHAT THE-!? HOW!? HOW DID YOU BREAK THE GAME?!?!?!");
		token = t;
		switch (token){
		case 1: name = "Miss Scarlett";				break;
		case 2: name = "Colonel Mustard";			break;
		case 3: name = "Mrs. White";				break;
		case 4: name = "The Reverend Green";		break;
		case 5: name = "Mrs. Peacock";				break;
		case 6: name = "Professor Plum";			break; 
		default: name = "WHAT?!?!? IMPOSSIBRUE!!!";	break;
		}
		position = start;
	}
	
	/**
	 * This method returns the character's position.
	 * */
	public Location getPosition() {
		return position;
	}

	/**
	 * This method changes the character's position //FIXME: AT PRESENT, THIS CLASS DOES NOTHING
	 * */
	public void setPosition(Location position) {
		if (this.position != null){
			this.position.setPieceOn(null);
			if (this.position != null && this.position.getPartOfRoom() == null)
				this.position.setAccessible(true);
		}
		this.position = position;
		if (position != null){
			this.position.setAccessible(false);
			this.position.setPieceOn(this);
		}
	}

	/**
	 * This method returns the character's <b>token</b>, which is who they are in game.<br />
	 * This is represented by Miss Scarlett starting at 1, then moving around clockwise stopping at Professor Plum at 6.<br />
	 * SO:<br /><br />
	 * 
	 * 1 = Miss Scarlett<br />
	 * 2 = Colonel Mustard<br />
	 * 3 = Mrs. White<br />
	 * 4 = The Reverend Green<br />
	 * 5 = Mrs. Peacock<br />
	 * 6 = Professor Plum <br />
	 * 
	 * */
	public int getToken() {
		return token;
	}	

	/**
	 * This method returns whether the character's in a room.
	 * */
	public boolean isInRoom() {
		return inRoom;
	}

	/**
	 * This method changes whether the character's in a room.
	 * */
	public void setInRoom(boolean inRoom) {
		this.inRoom = inRoom;
	}

	/**
	 * This method returns the room the character's in.
	 * */
	public Room getRoom() {
		return room;
	}

	/**
	 * This method changes the room the character's in.
	 * */
	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	public boolean isCharacter() {
		return false;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public void setOldRoom(Room room) {
		oldRoom = room;
	}
}
