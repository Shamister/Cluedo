package gameObjects;

import gameObjects.CARDS.Card;
import gameObjects.ROOMS.Room;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a character that will be moving around the board.
 * */
public class Character implements Piece{
	
	public final String name;
	public final int token;		/* 1 = Miss Scarlett
	// 2 = Colonel Mustard
	// 3 = Mrs. White
	// 4 = The Reverend Green
	// 5 = Mrs. Peacock
	// 6 = Professor Plum */
	
	private String data = "";	
	private Location position;
	private Set<Card> hand = new HashSet<>();
	private boolean inRoom = false;
	private boolean suggestionMade = false;
	private boolean movementMade = false;
	private boolean movementOK = true;
	private boolean active = false;
	private boolean eliminated = false;
	private Room room = null;
	private Room oldRoom = room;
	
	public Character(String name, int t, Location start){
		this.name = name;
		if (t > 6 || t < 1)	throw new IllegalArgumentException("WHAT THE-!? HOW!? HOW DID YOU BREAK THE GAME?!?!?!");
		token = t;
		position = start;
		data += "Welcome to Cluedo, " + name + ".\n";
	}

	public Character(String name, int t){
		this.name = name;
		if (t > 6 || t < 1)	throw new IllegalArgumentException("WHAT THE-!? HOW!? HOW DID YOU BREAK THE GAME?!?!?!");
		token = t;
		data += "Welcome to Cluedo, " + name + ".\n";
	}
	
	/**
	 * This method returns all the Cards in the character's hand.
	 * */
	public Set<Card> getHand() {
		return hand;
	}

	/**
	 * This method sets the character's hand to the set.
	 * 
	 * PLEASE! DO *NOT* USE THIS AFTER THE GAME IS SET UP!
	 * */
	public void setHand(Set<Card> hand) {
		this.hand = hand;
	}
	
	/**
	 * This method adds one Card to the character's hand.
	 * 
	 * PLEASE! DO *NOT* USE THIS AFTER THE GAME IS SET UP!
	 * */
	public void addToHand(Card card) {
		hand.add(card);
	}

	/**
	 * This method returns all the information the character has.
	 * */
	public String getData() {
		return data;
	}

	/**
	 * This method ADDS TO the information the character has - Crucially, NOTHING IS REPLACED.<br /><br />
	 * For Example, all characters start with the data:<br />
	 * "Welcome to Cluedo, [Player Name Here]."<br /><br />
	 * Using setData("You've been shown the LEAD PIPE by [Other Player Name Here]!") will make data show:<br />
	 * 
	 * "Welcome to Cluedo, [Player Name Here]."<br /><br />
	 * 
	 * "You've been shown the LEAD PIPE by [Other Player Name Here]!"
	 * @param data The String to add to your notes.
	 * */
	public void setData(String data) {
		this.data = this.data + data + "\n\n";
	}

	/**
	 * This method returns the character's position.
	 * */
	public Location getPosition() {
		return position;
	}

	/**
	 * This method changes the character's position
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
		//suggestionMade = false;
	}

	/**
	 * This method returns the character's name
	 * 
	 * This Method is NOT actually necessary, but it's here for completion
	 * */
	public String getName() {
		return name;
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
	
	/**
	 * This method returns the room the character was in MOST RECENTLY (set to room if isInRoom() == true).
	 * */
	public Room getOldRoom() {
		return oldRoom;
	}

	/**
	 * This method sets the room the character was in MOST RECENTLY (set to room if isInRoom() == true).
	 * */
	public void setOldRoom(Room oldRoom) {
		this.oldRoom = oldRoom;
	}

	/**
	 * This method returns whether the character made a suggestion since entering the room.
	 * */
	public boolean isSuggestionMade() {
		return suggestionMade;
	}

	
	/**
	 * This method sets whether the character made a suggestion since entering the room
	 * (false upon leaving a room, true upon making one in a room).
	 * */
	public void setSuggestionMade(boolean suggestionMade) {
		this.suggestionMade = suggestionMade;
	}
	

	public boolean isMovementMade() {
		return movementMade;
	}

	public void setMovementMade(boolean movementMade) {
		this.movementMade = movementMade;
	}

	public boolean isMovementOK() {
		return movementOK;
	}

	public void setMovementOK(boolean movementOK) {
		this.movementOK = movementOK;
	}

	
	public boolean isEliminated() {
		return eliminated;
	}

	public void setEliminated(boolean eliminated) {
		this.eliminated = eliminated;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String toString(){
		String string = "";
		string += "Character: " + name + "\nToken: ";
		switch (token){
		case 1:
			string += Data.charNames[0];
			break;
		case 2:
			string += Data.charNames[1];
			break;
		case 3:
			string += Data.charNames[2];
			break;
		case 4:
			string += Data.charNames[3];
			break;
		case 5:
			string += Data.charNames[4];
			break;
		case 6:
			string += Data.charNames[5];
			break;
		default:
			string += "WHAT?!?!? IMPOSSIBRUE!!!";
			break;
		}
		if (!eliminated){
			string += "\nLocation: " + position + "\n";
			if (inRoom)
				string += "In Room: " + getRoom().getRoomName() + "\n";
			else
				string += "Not in a Room.\n";
		}
		else string += "\nELIMINATED!!";
		return string;
	}

	@Override
	public boolean isCharacter() {
		return true;
	}
}
